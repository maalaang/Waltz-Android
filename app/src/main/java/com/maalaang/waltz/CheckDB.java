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

/**
 * Created by User on 2015-02-26.
 */
public  class CheckDB extends Thread{
    DBHandler dbHandler;
    Context context;

    public CheckDB(Context c){
        this.context = c;
    }
    public void run(){
        super.run();
        dbHandler = DBHandler.open(context);
        JSONObject json         = new JSONObject();
        JSONObject jsonResponse = new JSONObject();
        Cursor cursor = dbHandler.show_contact();
        cursor.moveToFirst();
        try{
            json.put("count",cursor.getCount()+"");
            while(cursor.getPosition()<cursor.getCount()){
                json.put(cursor.getPosition()+"",cursor.getString(1));
                cursor.moveToNext();
                //Log.d("error",cursor.getPosition()+cursor.getString(1));
            }
            jsonResponse = sendDB(json);
            cursor.moveToFirst();
            while(cursor.getPosition()<cursor.getCount()){
                String data = jsonResponse.get(cursor.getPosition()+"").toString();
                if(data.equals("F")){

                }else {
                    dbHandler.update_contact(cursor.getString(1), null, data,1);
                }
                cursor.moveToNext();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject sendDB(JSONObject json) {
        JSONObject jsonResponse = null;
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * 60);
            HttpConnectionParams.setSoTimeout(httpParams, 1000 * 30);
            HttpClient client = new DefaultHttpClient(httpParams);
            String url = "http://dev.maalaang.com/redhair/Sunrise/d/m_main/pnumck/";
            HttpPost post = new HttpPost(url);
            StringEntity se = new StringEntity("json="+json.toString());
            post.addHeader("content-type","application/x-www-form-urlencoded");
            post.setEntity(se);

            HttpResponse response = client.execute(post);
            String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());

            jsonResponse = new JSONObject(resFromServer);
            Log.i("Response from server", jsonResponse.toString());
        } catch (Throwable e) {
            Log.e("error throwable", e.toString());
        }
        return jsonResponse;
    } //sendDB
} //checkDB