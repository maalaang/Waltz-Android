package com.maalaang.waltz.view;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.maalaang.waltz.Constants;
import com.maalaang.waltz.ContactUtil;
import com.maalaang.waltz.R;
import com.maalaang.waltz.gcm.GcmModule;

import java.io.File;

/**
 * Created by User on 2015-02-03.
 */
public class LogoActivity extends ActionBarActivity{

    Button selectCtyBt;
    Button nextBt;
    String pn;
    private final String PACKEGE = Constants.PACKEGE;
    private final String DB = Constants.DBNAME;
    private GcmModule gcm = new GcmModule();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        gcm.initGcm(getApplicationContext());
        String reg_id = gcm.getRegistrationId(getApplicationContext());
        Loading(reg_id);
    }


    private void Loading(final String reg_id) {
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
                    String  telPhoneNo = ContactUtil.myPhoneNumber(getApplicationContext());
                    final EditText tn = (EditText) findViewById(R.id.register_et_number);
                    tn.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                    tn.setText(telPhoneNo);
                    selectCtyBt = (Button) findViewById(R.id.register_bt_country);
                    selectCtyBt.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),SelectCountryActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    });
                    nextBt = (Button)findViewById(R.id.register_bt_next);
                    nextBt.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                            intent.putExtra("pnum", tn.getText().toString());
                            intent.putExtra("pn", pn);
                            intent.putExtra("reg_id",reg_id);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode==1){
                selectCtyBt = (Button) findViewById(R.id.register_bt_country);
                selectCtyBt.setText("aa"+data.getStringExtra("countrycode"));
                selectCtyBt.setTextColor(Color.parseColor("#000000"));
                String[] company = getResources().getStringArray(R.array.tn);
                pn = company[Integer.parseInt(data.getStringExtra("pn"))];
            }
        }
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
