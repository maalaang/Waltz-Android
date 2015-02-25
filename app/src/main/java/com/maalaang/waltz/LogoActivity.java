package com.maalaang.waltz;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.maalaang.waltz.gcm.GcmModule;
import com.maalaang.waltz.view.MainActivity;
import com.maalaang.waltz.view.RegisterActivity;

import java.io.File;

/**
 * Created by User on 2015-02-03.
 */
public class LogoActivity extends ActionBarActivity{


    private final String PACKEGE = "com.maalaang.watalk";
    private final String DB = "Watalk.db";
    private GcmModule gcm = new GcmModule();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        gcm.initGcm(getApplicationContext());
        String reg_id = gcm.getRegistrationId(getApplicationContext());
        Loading(reg_id);
    }

    private void Loading(final String reg_id) {
        // TODO Auto-generated method stub
        Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if(isCheckDB()){
                    Intent intent = new Intent(getApplication(),MainActivity.class);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    startActivity(intent);
                    finish();
                }else{
                    setContentView(R.layout.register);
                    TelephonyManager telephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String  telPhoneNo = telephony.getLine1Number();
                    final EditText tn = (EditText) findViewById(R.id.register_et_number);
                    tn.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                    tn.setText(telPhoneNo);
                    Button nextBt = (Button)findViewById(R.id.register_bt_next);
                    nextBt.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                            intent.putExtra("pnum", tn.getText().toString());
                            intent.putExtra("reg_id",reg_id);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 3000);    // ms, 3초후 종료시킴
    }



    public boolean isCheckDB(){
        String filePath = "/data/data/" + PACKEGE + "/databases/" + DB;
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }
}
