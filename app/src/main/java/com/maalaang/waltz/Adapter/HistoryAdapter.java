package com.maalaang.waltz.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maalaang.waltz.R;


/**
 * Created by User on 2015-02-06.
 */
public class HistoryAdapter extends CursorAdapter {

    public HistoryAdapter(Context c, Cursor cursor) {
        super(c, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.history_item,parent,false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ImageView photo = (ImageView)view.findViewById(R.id.history_iv_photo);
        final ImageView type = (ImageView)view.findViewById(R.id.history_iv_type);
        final TextView name = (TextView)view.findViewById(R.id.history_tv_name);
        final TextView time = (TextView)view.findViewById(R.id.history_tv_time);
        final TextView callingtime = (TextView)view.findViewById(R.id.history_tv_callingtime);

        photo.setImageResource(R.drawable.ic_launcher);
        type.setImageResource(R.drawable.ic_launcher);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        time.setText(cursor.getString(cursor.getColumnIndex("time")));
        callingtime.setText(cursor.getString(cursor.getColumnIndex("callingtime")));
    }
}