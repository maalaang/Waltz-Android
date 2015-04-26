package com.maalaang.waltz.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.maalaang.waltz.CallVideo;

import com.maalaang.waltz.ContactUtil;
import com.maalaang.waltz.R;

/**
 * Created by User on 2015-02-12.
 */
public class SelectActivity extends Activity{

    private Button bt_video;
    private Button bt_voice;
    private TextView tv_name;
    private ImageView iv_photo;
    private String pnum;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select);
        Intent intent = getIntent();
        pnum = intent.getExtras().getString("pnum");
        name = intent.getExtras().getString("name");
        init();
    }

    private void init() {

        bt_video = (Button)findViewById(R.id.select_bt_video);
        bt_voice = (Button)findViewById(R.id.select_bt_voice);
        tv_name = (TextView) findViewById(R.id.select_tv_name);
        iv_photo = (ImageView) findViewById(R.id.select_iv_photo);
        bt_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  caller = ContactUtil.PhoneNumberChange(ContactUtil.myPhoneNumber(getApplicationContext()),getApplicationContext().getResources().getConfiguration().locale.getCountry());
                CallVideo call = new CallVideo(pnum, caller);
                call.start();
                try {
                    call.join();
                    Intent intent = new Intent(getApplicationContext(), CallVideoActivity.class);
                    intent.putExtra("caller",caller);
                    intent.putExtra("name",name);
                    intent.putExtra("pnum",pnum);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        bt_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+pnum));
                startActivity(intent);
            }
        });
        tv_name.setText(name);
    }
}