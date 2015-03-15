//package com.maalaang.waltz.Adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.media.session.PlaybackState;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.maalaang.waltz.R;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// * Created by User on 2015-03-15.
// */
//public class CountryListAdapter extends BaseAdapter{
//
//    private ArrayList<String> m_List;
//
//    public CountryListAdapter(){
//        m_List = new ArrayList<String>();
//    }
//    @Override
//    public int getCount() {
//        return m_List.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return m_List.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final int pos = position;
//        final Context context = parent.getContext();
//        if ( convertView == null ) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_item_checkbox, parent, false);
//
//            TextView text = (TextView) convertView.findViewById(R.id.checkbox_tv_name);
//            text.setText(m_List.get(position));
//            text.setTextColor(Color.parseColor("#000000"));
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "리스트 클릭 : " + m_List.get(pos), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
//
//        return convertView;
//    }
//
//    public void add(String arg){
//        m_List.add(arg);
//    }
//}
