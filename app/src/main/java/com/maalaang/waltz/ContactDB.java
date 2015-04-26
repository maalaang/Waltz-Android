package com.maalaang.waltz;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by User on 2015-02-10.
 */
public class ContactDB {
    DBHandler dbHandler;
    Context context;
    public ContactDB(Context c) {
        this.dbHandler = DBHandler.open(c);
        this.context = c;
    }


    public void saveDB() {
        Map<String, String> phone_address = ContactUtil.getAddressBook(context);
        dbHandler.open(context);
        Iterator ite = phone_address.keySet().iterator();
        while (ite.hasNext()) {
            String phone = ite.next().toString();
            String name = phone_address.get(phone).toString();
            dbHandler.insert_contact(ContactUtil.PhoneNumberChange(phone,context.getResources().getConfiguration().locale.getCountry()), name);
        }
        dbHandler.close();

    }


    public void startcheckDB(){
        CheckDB checkDB = new CheckDB(context);
        checkDB.start();
    }
}
