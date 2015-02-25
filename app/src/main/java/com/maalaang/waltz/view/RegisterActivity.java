package com.maalaang.waltz.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maalaang.waltz.ContactDB;
import com.maalaang.waltz.DBHelper;
import com.maalaang.waltz.PhoneNumber;
import com.maalaang.waltz.R;
import com.maalaang.waltz.gcm.GcmModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 2015-02-06.
 */
public class RegisterActivity extends ActionBarActivity {
    private ProgressDialog Progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_name);
        Intent intent = getIntent();
        GcmModule gcm = new GcmModule();
        final String pnum = intent.getExtras().getString("pnum");
        final String reg_id = gcm.getRegistrationId(getApplicationContext());
        final EditText firstname = (EditText) findViewById(R.id.register_et_firstname);
        final EditText lastname = (EditText) findViewById(R.id.register_et_lastname);
        final EditText middlename = (EditText) findViewById(R.id.register_et_middlename);
        final Button bt_ifmiddle = (Button) findViewById(R.id.button);
        Button send = (Button) findViewById(R.id.button2);

        bt_ifmiddle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                middlename.setVisibility(View.VISIBLE);
                bt_ifmiddle.setVisibility(View.GONE);
            }
        });
        send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber pnc = new PhoneNumber();
                HttpPostData sendData = new HttpPostData(reg_id, pnc.PhoneNumberChange(pnum), firstname.getText().toString(), lastname.getText().toString());
                sendData.start();
                Progress = new ProgressDialog(RegisterActivity.this);
                Progress.show();
            }
        });
    }

    public class HttpPostData extends Thread {
        String reg_id;
        String pnum;
        String firstName;
        String lastName;

        public void run() {
            super.run();
            try {
                URL url = new URL("http://dev.maalaang.com/redhair/Sunrise/d/m_main/signup/");       // URL Setting
                HttpURLConnection http = (HttpURLConnection) url.openConnection();   // connection

                http.setDefaultUseCaches(false);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestMethod("POST");

                http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                StringBuffer buffer = new StringBuffer();
                buffer.append("signup_reg_id").append("=").append(reg_id).append("&");
                buffer.append("signup_pnum").append("=").append(pnum).append("&");
                buffer.append("first_name").append("=").append(firstName).append("&");
                buffer.append("last_name").append("=").append(lastName);

                OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outStream);
                writer.write(buffer.toString());
                writer.flush();
                InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }

                String myResult = builder.toString();
                Log.e("error", myResult);
                Progress.dismiss();
                mHandler.sendEmptyMessage(0);
            } catch (MalformedURLException e) {
                Log.e("URLException", e.toString());
            } catch (IOException e) {
                Progress.dismiss();
                mHandler.sendEmptyMessage(1);
            } // try
        }

        public HttpPostData(String reg_id, String pnum, String s, String s1) {
            this.reg_id = reg_id;
            this.pnum = pnum;
            this.firstName = s;
            this.lastName = s1;
        }
    } // HttpPostData

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    new DBHelper(RegisterActivity.this);
                    ContactDB contactDB = new ContactDB(RegisterActivity.this);
                    contactDB.saveDB();
                    contactDB.startcheckDB();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                    alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alert.setMessage("Please check your network.\nServer connection fail");
                    alert.show();
                    break;
            }
        }
    };

}
