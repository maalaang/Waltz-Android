package com.maalaang.waltz.view;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codebutler.android_websockets.WebSocketClient;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.maalaang.waltz.CallVideo;
import com.maalaang.waltz.Constants;
import com.maalaang.waltz.R;
import com.maalaang.waltz.VideoStreamsView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 2015-02-12.
 */
public class CallVideoActivity extends ActionBarActivity{

    private VideoStreamsView vsv;
    private MediaConstraints sdpMediaConstraints;
    private WebSocketClient client;
    private PeerConnection pc;
    private VideoSource videoSourceBack;
    private VideoSource videoSourceFront;
    private VideoCapturer backVideoCapturer;
    private VideoCapturer frontVideoCapturer;
    private PeerConnectionFactory factory;
    MediaConstraints videoConstraints;
    VideoRenderer localVideoRenderer;
    private final PCObserver pcObserver = new PCObserver();
    private final SDPObserver sdpObserver = new SDPObserver();
    LinkedList<PeerConnection.IceServer> iceServers= new LinkedList<PeerConnection.IceServer>();
    final String TAG = "CallVideoActivity";
    int i;
    MediaStream lMS=null;
    FrameLayout fv;
    LinearLayout ll;
    String myid;
    String otherid;
    String caller;
    String name;
    String pnum;
    String Channel_tokken;
    String Channel_msg;
    boolean camon = true;
    boolean micon = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.call);
        Intent intent = getIntent();
        caller = intent.getExtras().getString("caller").replaceAll(" ","").replaceAll("[+]","");
        Log.e("caller ", caller);
        name = intent.getExtras().getString("name");
        pnum = intent.getExtras().getString("pnum");
        init();

        PeerConnectionFactory.initializeAndroidGlobals(this, true, true);
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        vsv = new VideoStreamsView(this, displaySize);

        fv = (FrameLayout) findViewById(R.id.call_fl_vsv);
        fv.addView(vsv);

        sdpMediaConstraints = new MediaConstraints();
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        sdpMediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement","true"));

        List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=Sunrise"));
        client = new WebSocketClient(URI.create(Constants.CHANNEL_URL), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d(TAG, "Connected!");
                onConnectSuccess();
            }
            @Override
            public void onMessage(String message) {
                Log.d(TAG, String.format("Got string message! %s", message));
                JSONObject msg = null;
                try {
                    msg = new JSONObject(message);
                    String type = msg.getString("type");
                    Log.d(TAG, "type :" + type);
                    if(type.equals("channel")){
                        String subtype = msg.getString("subtype");
                        if (subtype.equals("open")) {
                            // when channel server connected
                            String id = msg.getString("participant_id");
                            String cnt = msg.getString("participant_cnt");
                            myid = id;
                            Log.d(TAG, "ID :" + id);
                            if (cnt.equals("0")){
                                Log.d(TAG, "no one in room");
                            } else {
                                String from = message.split(",")[4];
                                from = from.split(":")[1].replace("\"", "").replace("{", "");
                                otherid = from;
                                Log.d(TAG,from);
                                pc.createOffer(sdpObserver, sdpMediaConstraints);

                            }
                        }
                    } //end open channel
                    else if (type.equals("focus")){
                        ;
                    }else if (type.equals("offer")||type.equals("answer")){
                        String from = msg.getString("sender");
                        otherid = from;
                        Log.d(TAG, otherid+"offer");
                        SessionDescription sdp = new SessionDescription(
                                SessionDescription.Type.fromCanonicalForm(msg.getString("type")),
                                msg.getString("sdp")
                        );
                        pc.setRemoteDescription(sdpObserver, sdp);
                        if(type.equals("offer"))
                            pc.createAnswer(sdpObserver, sdpMediaConstraints);
                    }else if(type.equals("candidate")){
                        IceCandidate candidate = new IceCandidate(
                                msg.getString("id"),
                                msg.getInt("label"),
                                msg.getString("candidate")
                        );
                        pc.addIceCandidate(candidate);
                    }else if(type.equals("bye")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(byte[] data) {}

            @Override
            public void onDisconnect(int code, String reason) {
                Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
                i=0;
            }

            @Override
            public void onError(Exception error) {
                Log.e(TAG, "Error!", error);
            }

        }, extraHeaders);

        client.connect();

    }

    private void init() {
        ll = (LinearLayout) findViewById(R.id.call_ll_callingview);
        TextView tv_name = (TextView) findViewById(R.id.call_tv_name);
        Button bt_hangup = (Button) findViewById(R.id.call_bt_hangup);

        tv_name.setText(name);
        bt_hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallVideo call = new CallVideo(pnum, "hangup");
                call.start();
                try {
                    call.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                end();
            }
        });
        FloatingActionButton actionA = (FloatingActionButton)findViewById(R.id.call_bt_fam_disconnect);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });

        FloatingActionButton actionB = (FloatingActionButton)findViewById(R.id.call_bt_fam_micoff);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(micon) {
                    pc.removeStream(lMS);
                    lMS = factory.createLocalMediaStream("ARDAMS");
                    lMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSourceFront));
                    //lMS.addTrack(factory.createAudioTrack("ARDAMSa0", factory.createAudioSource(new MediaConstraints())));
                    localVideoRenderer = new VideoRenderer(new VideoCallbacks(vsv, VideoStreamsView.Endpoint.LOCAL));
                    lMS.videoTracks.get(0).addRenderer(localVideoRenderer);
                    pc.addStream(lMS, new MediaConstraints());
                    micon=false;
                }else{
                    pc.removeStream(lMS);
                    lMS = factory.createLocalMediaStream("ARDAMS");
                    lMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSourceFront));
                    lMS.addTrack(factory.createAudioTrack("ARDAMSa0", factory.createAudioSource(new MediaConstraints())));
                    localVideoRenderer = new VideoRenderer(new VideoCallbacks(vsv, VideoStreamsView.Endpoint.LOCAL));
                    lMS.videoTracks.get(0).addRenderer(localVideoRenderer);
                    pc.addStream(lMS, new MediaConstraints());
                    micon=true;
                }
            }
        });

        FloatingActionButton actionC = (FloatingActionButton)findViewById(R.id.call_bt_fam_camoff);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camon) {
                    VideoTrack currVideoTrack = lMS.videoTracks.get(0);
                    currVideoTrack.removeRenderer(localVideoRenderer);
                    lMS.removeTrack(currVideoTrack);
                    pc.removeStream(lMS);

                    videoConstraints = new MediaConstraints();
                    videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", "640"));
                    videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", "480"));
                    lMS = factory.createLocalMediaStream("ARDAMS");
                    lMS.addTrack(factory.createAudioTrack("ARDAMSa0", factory.createAudioSource(new MediaConstraints())));
                    localVideoRenderer = new VideoRenderer(new VideoCallbacks(vsv, VideoStreamsView.Endpoint.LOCAL));
                    pc.addStream(lMS, new MediaConstraints());
                    camon=false;
                }else{
                    pc.removeStream(lMS);
                    lMS = factory.createLocalMediaStream("ARDAMS");
                    lMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSourceFront));
                    lMS.addTrack(factory.createAudioTrack("ARDAMSa0", factory.createAudioSource(new MediaConstraints())));
                    //lMS.addTrack(factory.createAudioTrack("ARDAMSa0"));
                    localVideoRenderer = new VideoRenderer(new VideoCallbacks(vsv, VideoStreamsView.Endpoint.LOCAL));
                    lMS.videoTracks.get(0).addRenderer(localVideoRenderer);
                    pc.addStream(lMS, new MediaConstraints());
                    camon=true;
                }
            }
        });
    }

    public void sendMessage(String to, String type, JSONObject payload) throws JSONException {
        Log.d(TAG,"sendmessage"+payload);
        if(type!=null)
            payload.put("type", type);
        client.send(payload.toString());
    }

    private void onConnectSuccess(){
        GetThread thread = new GetThread(Constants.ROOM_URL+caller);
        thread.start();
        iceServers.add(new PeerConnection.IceServer("stun:27.102.207.172"));
        iceServers.add(new PeerConnection.IceServer("turn:27.102.207.172:3478","whale88", "whale"));

        factory = new PeerConnectionFactory();
        pc = factory.createPeerConnection(iceServers,sdpMediaConstraints, pcObserver);
        videoConstraints = new MediaConstraints();
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxHeight", "640"));
        videoConstraints.mandatory.add(new MediaConstraints.KeyValuePair("maxWidth", "480"));
        frontVideoCapturer = getVideoCapturer("front");
        videoSourceFront = factory.createVideoSource(frontVideoCapturer,  videoConstraints);
        frontVideoCapturer.dispose();
        lMS = factory.createLocalMediaStream("ARDAMS");
        lMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSourceFront));
        lMS.addTrack(factory.createAudioTrack("ARDAMSa0", factory.createAudioSource(new MediaConstraints())));
        //lMS.addTrack(factory.createAudioTrack("ARDAMSa0"));
        localVideoRenderer = new VideoRenderer(new VideoCallbacks(vsv, VideoStreamsView.Endpoint.LOCAL));
        lMS.videoTracks.get(0).addRenderer(localVideoRenderer);
        pc.addStream(lMS, new MediaConstraints());

        String msg = "{\"type\":\"channel\",\"subtype\":\"open\",\"channel_token\":"+Channel_tokken+",\"user_name\":\"Android\",\"recipient\": \"ns\"}";
        client.send(msg);
    }

    private VideoCapturer getVideoCapturer(String cameraFacing) {
        int[] cameraIndex = { 0, 1 };
        int[] cameraOrientation = { 0, 90, 180, 270 };
        for (int index : cameraIndex) {
            for (int orientation : cameraOrientation) {
                String name = "Camera " + index + ", Facing " + cameraFacing +
                        ", Orientation " + orientation;
                VideoCapturer capturer = VideoCapturer.create(name);
                if (capturer != null) {
                    return capturer;
                }
            }
        }
        throw new RuntimeException("Failed to open capturer");
    }

    private static void abortUnless(boolean condition, String msg) {
        if (!condition) {
            throw new RuntimeException(msg);
        }
    }

    class GetThread extends Thread {
        String mAddr;

        GetThread(String addr) {
            mAddr = addr;
        }

        public void run() {
            String result = DownloadHtml(mAddr);
            Message message = mAfterDown.obtainMessage();
            message.obj = result;
            mAfterDown.sendMessage(message);
        }

        String DownloadHtml(String addr) {
            StringBuilder html = new StringBuilder();
            try {
                URL url = new URL(addr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));
                        for (;;) {
                            String line = br.readLine();
                            if (line == null) break;
                            html.append(line + '\n');
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (NetworkOnMainThreadException e) {
                return "Error : NetworkOnMainThread - " + e.getMessage();
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
            return html.toString();
        }
    }

    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            Channel_msg = (String)msg.obj;
            Channel_tokken = Channel_msg.split(",")[6];
            Channel_tokken = Channel_tokken.split(":")[1];
        }
    };

    private class VideoCallbacks implements VideoRenderer.Callbacks {
        private final VideoStreamsView view;
        private final VideoStreamsView.Endpoint stream;

        public VideoCallbacks(VideoStreamsView view,
                              VideoStreamsView.Endpoint stream) {
            this.view = view;
            this.stream = stream;
        }

        @Override
        public void setSize(final int width, final int height) {
            view.queueEvent(new Runnable() {
                public void run() {
                    view.setSize(stream, width, height);
                }
            });
        }

        @Override
        public void renderFrame(VideoRenderer.I420Frame frame) {
            view.queueFrame(stream, frame);
        }
    }

    private class SDPObserver implements SdpObserver {
        //private PeerConnection pc;
        final String TAG = "SDP";
        public void onCreateSuccess(final SessionDescription sdp) {
            try {
                Log.d(TAG,"onCreateSuccess Peer");
                JSONObject payload = new JSONObject();
                payload.put("sdp", sdp.description);
                payload.put("type",sdp.type.canonicalForm());
                payload.put("recipient",otherid);
                payload.put("sender",myid);
                payload.put("name","Android");
                sendMessage(myid, null,payload);
                pc.setLocalDescription(sdpObserver, sdp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCreateFailure(String arg0) {}

        @Override
        public void onSetFailure(String arg0) {}

        @Override
        public void onSetSuccess() {
            Log.e(TAG,"In onsetSuccess of sdp");
            if (pc.getRemoteDescription() != null) {
                // We've set our local offer and received & set the
                // remote
                // answer, so drain candidates.
                Log.e(TAG,"drain remote candidates");
                //drainRemoteCandidates();
            }
        }
    }

    private class PCObserver implements PeerConnection.Observer {
        final String TAG = "Peer";
        @Override
        public void onIceCandidate(final IceCandidate candidate) {
            // TODO Auto-generated method stub
            try {
                JSONObject payload = new JSONObject();
                payload.put("label", candidate.sdpMLineIndex);
                payload.put("id", candidate.sdpMid);
                payload.put("candidate", candidate.sdp);
                payload.put("recipient",otherid);
                sendMessage(myid, "candidate", payload);
                Log.d(TAG,"onIceCandidate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAddStream(final MediaStream stream) {
            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.e(TAG, "onAddStream");
                    //logAndToast("onAddStream");
                    ll.setVisibility(View.GONE);
                    fv.setVisibility(View.VISIBLE);
                    abortUnless(stream.audioTracks.size() == 1
                                    && stream.videoTracks.size() == 1,
                            "Weird-looking stream: " + stream);
                    stream.videoTracks.get(0).addRenderer(
                            new VideoRenderer(new VideoCallbacks(vsv,
                                    VideoStreamsView.Endpoint.REMOTE)));
                }
            });

        }

        @Override
        public void onDataChannel(DataChannel arg0) {}

        @Override
        public void onError() {}

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            if(iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED){
                //finish();
                end();
            }

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState arg0) {}

        @Override
        public void onRemoveStream(final MediaStream stream) {
            // TODO Auto-generated method stub
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.e(TAG, "on Remove Stream Called");
                    stream.videoTracks.get(0).dispose();
                }
            });
        }

        @Override
        public void onRenegotiationNeeded() {}

        @Override
        public void onSignalingChange(PeerConnection.SignalingState arg0) {}
    }

    private void end() {
        if(pc !=null){
            pc.dispose();
            pc = null;
        }
        if(client !=null) {
            client.disconnect();
            client = null;
        }
        if(videoSourceFront !=null){
            videoSourceFront.dispose();
            videoSourceFront = null;
        }
        if(videoSourceBack !=null){
            videoSourceBack.dispose();
            videoSourceBack = null;
        }
        if(factory !=null){
            factory.dispose();
            factory = null;
        }
        finish();
    }

}