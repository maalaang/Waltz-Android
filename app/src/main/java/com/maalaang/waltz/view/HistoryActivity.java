package com.maalaang.waltz.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.maalaang.waltz.DBHandler;
import com.maalaang.waltz.HistoryAdapter;
import com.maalaang.waltz.R;

/**
 * Created by User on 2015-02-03.
 */
public class HistoryActivity extends ActionBarActivity{
    DBHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        dbHandler = DBHandler.open(this);
        dbHandler.insert_history("010-4121-7700","name",10,1);

        ListView list = (ListView)findViewById(R.id.history_lv_list);
        Cursor cursor = dbHandler.show_history();
        if(cursor.getCount()>0){
            startManagingCursor(cursor);
            HistoryAdapter hsAdapter = new HistoryAdapter(this, cursor);
            list.setAdapter(hsAdapter);
        }
    }
}
