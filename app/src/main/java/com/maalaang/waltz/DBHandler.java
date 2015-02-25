package com.maalaang.waltz;


import android.content.*;
import android.database.*;
import android.database.sqlite.*;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHandler {  //DML 선언
    private Context ctx;
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBHandler(Context ctx) {
        this.ctx = ctx;
        helper = new DBHelper(ctx);
        db = helper.getWritableDatabase(); //DB가 open 됨
    }

    public static DBHandler open(Context ctx) throws SQLException{
        DBHandler handler = new DBHandler(ctx);
        return handler;
    }

    public void close(){
        helper.close();
    }

    //SQL문 작성
    //INSERT
    public long insert_history(String pnum, String name, int callingtime, int type){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("pnum", pnum);
        values.put("name", name);
        values.put("time", dateFormat.format(date));
        values.put("callingtime",callingtime);
        values.put("type",type);

        long result = db.insert("history", null, values);
        return result;
    }

    //SELECT
    public Cursor show_history(){
        String sql = "SELECT * FROM history;";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;
    }

    public long insert_contact(String pnum, String name){

        ContentValues values = new ContentValues();
        values.put("pnum",pnum);
        values.put("name",name);
        values.put("isreg",0);
        long result = db.insert("contact",null, values);
        return result;
    }

    public void update_contact(String pnum, String photo, String bio,int isreg){
        db.execSQL("UPDATE contact SET photo = '"+photo+"',bio = '"+bio+"', isreg = "+isreg+" WHERE pnum = '"+pnum+"'");
    }

    public Cursor show_contact(){
        String sql = "SELECT * FROM contact order by  isreg desc, name;";
        Cursor cursor = db.rawQuery(sql,null);
        return cursor;

    }
}
