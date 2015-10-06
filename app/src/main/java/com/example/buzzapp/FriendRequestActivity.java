package com.example.buzzapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import Tool.PostUtil;
import model.User;

public class FriendRequestActivity extends ListActivity {
	 
	 
    private List<Map<String, Object>> mData;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }
 
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
 
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requestName", "G1");
        map.put("requsetHeadimg", R.drawable.bird);
        list.add(map);
 
        map = new HashMap<String, Object>();
        map.put("requestName", "G2");
        map.put("requsetHeadimg", R.drawable.horse);
        list.add(map);
 
         
        return list;
    }
     
    // ListView 中某项被选中后的逻辑
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
         
        
    }
     
    /**
     * listview中点击按键弹出对话框
     */
    public void showInfo(){
        new AlertDialog.Builder(this)
        .setTitle("listview")
        .setMessage("...")
        .setPositiveButton("...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .show();
         
    }
     
     
     
    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public Button accBtn;
        public Button refBtn;
    }
     
     
    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.friendrequest, null);
                holder.img = (ImageView) convertView.findViewById(R.id.requsetHeadimg);
                holder.title = (TextView) convertView.findViewById(R.id.requestName);
                holder.accBtn = (Button) convertView.findViewById(R.id.requestAccept);
                holder.refBtn = (Button) convertView.findViewById(R.id.requestRefuse);
                //holder
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }


            holder.img.setBackgroundResource((Integer) mData.get(position).get("requsetHeadimg"));
            holder.title.setText((String) mData.get(position).get("requestName"));


            holder.accBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String friendId = "0";//!!!
                            String json = buildJsonForResponse(friendId, true);
                            String result = "";
                            result = PostUtil.POST(getString(R.string.response), json);
                            // add the code removing the friend request item

                        }
                    }).start();

                }
            });

            holder.refBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String friendId = "0";//!!!
                            String json = buildJsonForResponse(friendId, false);
                            String result = "";
                            result = PostUtil.POST(getString(R.string.response), json);
                            // add the code removing the friend request item

                        }
                    }).start();

                }
            });


            return convertView;
        }

        String buildJsonForResponse(String friendId, boolean result) {
            try {
                JSONObject info = new JSONObject();
                JSONObject request = new JSONObject();
                User instance = User.getInstance();
                info.put("userId", instance.getId());
                info.put("friendId", friendId);
                info.put("result", result);
                request.put("action", "responseFriendRequest");
                request.put("type", "request");
                request.put("info", info);

                return request.toString();

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
     
     
}
