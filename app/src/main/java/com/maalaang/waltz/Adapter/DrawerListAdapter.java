package com.maalaang.waltz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maalaang.waltz.R;
import com.maalaang.waltz.model.DrawerListData;

import java.util.ArrayList;

/**
 * Created by User on 2015-02-03.
 */



public class DrawerListAdapter extends ArrayAdapter<DrawerListData> {

    private LayoutInflater inflater = null;
    private ArrayList<DrawerListData> drawerList = null;
    private ViewHolder viewHolder = null;
    private Context mContext = null;


    public DrawerListAdapter(Context c, int textViewResourceId, ArrayList<DrawerListData> arrays) {
        super(c, textViewResourceId, arrays);
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.drawerList = arrays;
    }


    @Override
    public int getCount() {
        return drawerList.size();
    }


    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.drawer_item, null);
            viewHolder.tv_title = (TextView)v.findViewById(R.id.drawer_tv_list);
            viewHolder.iv_image = (ImageView)v.findViewById(R.id.drawer_iv_list);

            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }
        viewHolder.tv_title.setText(getItem(position).getTitle());
        viewHolder.iv_image.setTag(position);
        return v;
    }

    @Override
    public DrawerListData getItem(int position) {
        return drawerList.get(position);
    }



    class ViewHolder{
        public TextView tv_title = null;
        public ImageView iv_image = null;
    }

    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }

    private void free(){
        inflater = null;
        drawerList = null;
        viewHolder = null;
        mContext = null;
    }
}
