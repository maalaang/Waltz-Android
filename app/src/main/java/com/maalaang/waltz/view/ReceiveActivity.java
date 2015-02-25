package com.maalaang.waltz.view;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maalaang.waltz.R;

/**
 * Created by User on 2015-02-13.
 */
public class ReceiveActivity extends ActionBarActivity{
    public static Activity activity;
    String pnum;
    String name;
    public static final int NOTIFICATION_ID = 1235;


    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive);
        activity = ReceiveActivity.this;
        Intent intent = getIntent();
        pnum = intent.getExtras().getString("pnum");
        name = intent.getExtras().getString("name");
        init();

    }

    private void init() {
        TextView tv_name = (TextView) findViewById(R.id.receive_tv_name);
        Button bt_answer = (Button) findViewById(R.id.receive_bt_answer);
        Button bt_reject = (Button) findViewById(R.id.receive_bt_reject);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[] {400,400,400,400}, 0);
        tv_name.setText(name);
        bt_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CallVideoActivity.class);
                intent.putExtra("caller",pnum);
                intent.putExtra("name",name);
                intent.putExtra("pnum",pnum);
                startActivity(intent);
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.cancel(NOTIFICATION_ID);
                vibrator.cancel();
            }
        });

        bt_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.cancel(NOTIFICATION_ID);
                vibrator.cancel();
            }
        });
    }
}
