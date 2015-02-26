package com.maalaang.waltz.view;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maalaang.waltz.CheckDB;
import com.maalaang.waltz.ContactDB;
import com.maalaang.waltz.DBHandler;
import com.maalaang.waltz.Adapter.DrawerListAdapter;
import com.maalaang.waltz.model.DrawerListData;
import com.maalaang.waltz.R;
import com.maalaang.waltz.model.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    ListView mDrawerList;
    DBHandler dbHandler;
    ListView mContactList;
    ContactAdapter mContactAdapter;
    LinearLayout slidingpage;

    Animation translate_up;
    Animation translate_down;
    Boolean isPageOpen =false;

    private final int HISTORY = 0;
    private final int INVITE_MY_FRIEND = 1;
    private final int SETTING = 2;
    private final int ABOUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        CheckDB checkDB = new CheckDB(this);
        checkDB.start();
        try {
            checkDB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeDrawer();
        initializeContact();
    }



    private void initializeDrawer() {
        dlDrawer = (DrawerLayout) findViewById(R.id.main_dl_root);
        setSupportActionBar(toolbar);
        mDrawerList = (ListView) findViewById(R.id.drawer_lv_list);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        ArrayList<DrawerListData> drawer_list = new ArrayList<DrawerListData>();


        drawer_list.add(new DrawerListData("History",getResources().getDrawable(R.drawable.ic_launcher)));
        drawer_list.add(new DrawerListData("Invite my Friend",getResources().getDrawable(R.drawable.ic_launcher)));
        drawer_list.add(new DrawerListData("Setting",getResources().getDrawable(R.drawable.ic_launcher)));
        drawer_list.add(new DrawerListData("About",getResources().getDrawable(R.drawable.ic_launcher)));


        mDrawerList.setAdapter(new DrawerListAdapter(this,R.layout.drawer_item,drawer_list));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case HISTORY:
                        Intent intent = new Intent(getApplication(), HistoryActivity.class);
                        startActivity(intent);
                        break;
                    case INVITE_MY_FRIEND:
                        break;
                    case SETTING:
                        break;
                    case ABOUT:
                        break;
                    default:

                }
            }
        });
    }

    private void initializeContact() {
        mContactList = (ListView) findViewById(R.id.listView);
        mContactAdapter = new ContactAdapter();

        mContactList.setAdapter(mContactAdapter);
        mContactList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = (User) parent.getAdapter().getItem(position);
                if(user.isRegistered()){
                    Log.e("ereer",user.getPnum());
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    intent.putExtra("pnum",user.getPnum());
                    intent.putExtra("name",user.getUserName());
                    //intent.putExtra("photo",user.getPhoto());
                    startActivity(intent);
                    overridePendingTransition(R.anim.translate_up,R.anim.translate_out);
                }else{
                    invite_Message(user.getPnum(), user.getUserName());
                }
            }
        });

        dbHandler = DBHandler.open(this);
        Cursor cursor = dbHandler.show_contact();
        cursor.moveToFirst();
        Log.d("error",cursor.getCount()+"");
        while(cursor.getPosition()!=cursor.getCount()){
            mContactAdapter.addContact(new User(cursor.getString(1),getResources().getDrawable(R.drawable.ic_launcher),cursor.getString(2),cursor.getString(4),(cursor.getInt(5)!=0)));
            cursor.moveToNext();
        }
        dbHandler.close();

    }


    public void invite_Message(final String pnum, String name){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        alert_confirm.setMessage("Would you like to invite"+ name +"?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        SmsManager mSmsManager = SmsManager.getDefault();
                        mSmsManager.sendTextMessage(pnum, null, "watalk watalk", null, null);
                        Toast.makeText(getApplicationContext(),"Send invite message.",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected  void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(dtToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ContactAdapter extends BaseAdapter {
        private List<User> contactList;

        public ContactAdapter() {
            this.contactList = new ArrayList<User>();
        }

        public void addContact(User user) {
            contactList.add(user);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public User getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position){
            User user = getItem(position);
            return user.isRegistered()?0:1;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = getItem(position);
            ViewHolder holder = null;
            ViewHolder2 holder2 = null;
            int type = getItemViewType(position);

            if(type==0){
                if(convertView == null){
                    holder = new ViewHolder();
                    convertView = getLayoutInflater().inflate(R.layout.list_item_contact_user, null);
                    holder.tvName =  (TextView) convertView.findViewById(R.id.list_item_contact_user_tv_name);
                    holder.tvBio = (TextView) convertView.findViewById(R.id.list_item_contact_user_tv_bio);
                    holder.ivPhoto = (ImageView) convertView.findViewById(R.id.list_item_contact_user_iv_photo);
                    convertView.setTag(holder);
                }else{
                    if(convertView.getTag() instanceof ViewHolder){
                        holder = (ViewHolder)convertView.getTag();
                    }else{
                        holder = new ViewHolder();
                        convertView = getLayoutInflater().inflate(R.layout.list_item_contact_user, null);
                        holder.tvName =  (TextView) convertView.findViewById(R.id.list_item_contact_user_tv_name);
                        holder.tvBio = (TextView) convertView.findViewById(R.id.list_item_contact_user_tv_bio);
                        holder.ivPhoto = (ImageView) convertView.findViewById(R.id.list_item_contact_user_iv_photo);
                        convertView.setTag(holder);
                    }
                }
                holder.tvName.setText(user.getUserName());
                holder.tvBio.setText(user.getBio());
                holder.ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            }else{
                if(convertView == null){
                    holder2 = new ViewHolder2();
                    convertView = getLayoutInflater().inflate(R.layout.list_item_contact_non_user, null);
                    holder2.tvName2= (TextView) convertView.findViewById(R.id.list_item_contact_non_user_tv_name);
                    convertView.setTag(holder2);
                }else {
                    if(convertView.getTag() instanceof ViewHolder2) {
                        holder2 = (ViewHolder2)convertView.getTag();
                    }else{
                        holder2 = new ViewHolder2();
                        convertView = getLayoutInflater().inflate(R.layout.list_item_contact_non_user, null);
                        holder2.tvName2= (TextView) convertView.findViewById(R.id.list_item_contact_non_user_tv_name);
                        convertView.setTag(holder2);
                    }
                }
                holder2.tvName2.setText(user.getUserName());
            }
            return convertView;
        }

    }
    static class ViewHolder{
        TextView tvName = null;
        TextView tvBio = null;
        ImageView ivPhoto = null;
    }
    static class ViewHolder2{
        TextView tvName2 = null;
    }


}
