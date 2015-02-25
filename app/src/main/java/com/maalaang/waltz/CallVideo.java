package com.maalaang.waltz;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by User on 2015-02-13.
 */
public class CallVideo extends Thread{
    private String pnum;
    private String caller;

    public CallVideo(String pnum, String caller) {
        this.pnum = pnum;
        this.caller = caller;
    }

    public void run() {
        super.run();

        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * 60);
            HttpConnectionParams.setSoTimeout(httpParams, 1000 * 30);
            HttpClient client = new DefaultHttpClient(httpParams);
            String url = "http://dev.maalaang.com/redhair/Sunrise/d/m_main/call/";
            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity("pnum="+pnum+"&caller="+caller);
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            Log.e("Response from server", resFromServer);
        } catch (Throwable e) {
            Log.e("error throwable", e.toString());
        }
    }

}