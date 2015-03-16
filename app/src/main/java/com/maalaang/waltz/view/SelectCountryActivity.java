package com.maalaang.waltz.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maalaang.waltz.R;

import java.util.ArrayList;

/**
 * Created by User on 2015-03-05.
 */
public class SelectCountryActivity extends ActionBarActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkboxlist);

        final String[] country = getResources().getStringArray(R.array.country);
        listView = (ListView)findViewById(R.id.checkbox_lv_list);
        CountryListAdapter listviewAdapter = new CountryListAdapter();
        listView.setAdapter(listviewAdapter);
        Log.e("aaa",country.length+"");
        for(int i=0;country.length != i; i++){
            Log.e("add","add");
            listviewAdapter.add(country[i]);
        }


    }

    public class CountryListAdapter extends BaseAdapter{

        private ArrayList<String> m_List;

        public CountryListAdapter(){
            m_List = new ArrayList<String>();
        }
        @Override
        public int getCount() {
            return m_List.size();
        }

        @Override
        public Object getItem(int position) {
            return m_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();
            ViewHolder holder = null;

            if ( convertView == null ) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.list_item_checkbox, null);
                holder.name = (TextView) convertView.findViewById(R.id.checkbox_tv_name);
                convertView.setTag(holder);
            }else{
                if(convertView.getTag() instanceof ViewHolder){
                    holder = (ViewHolder)convertView.getTag();
                }else{
                    holder = new ViewHolder();
                    convertView = getLayoutInflater().inflate(R.layout.checkboxlist, null);
                    holder.name = (TextView) convertView.findViewById(R.id.checkbox_tv_name);
                    convertView.setTag(holder);
                }
            }
            holder.name.setText(m_List.get(position));
            holder.name.setTextColor(Color.parseColor("#000000"));

                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        intent.putExtra("countrycode", m_List.get(pos));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

            return convertView;
        }

        public void add(String arg){
            m_List.add(arg);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder{
        TextView name = null;
    }
}
