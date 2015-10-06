package com.example.buzzapp;

import IA.ImageAdapter;
import Tool.PostUtil;
import model.Friend;
import model.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.util.Base64;
import android.view.Menu;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
	private Button addFriend, friendRequest, mute, settings;
    private GridView gridview;
    private long start, end, duration = -1;;
    private Handler handler = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        addFriend = (Button) findViewById(R.id.addFriend);
        friendRequest = (Button) findViewById(R.id.friendRequest);
        settings = (Button) findViewById(R.id.setting);
        mute = (Button) findViewById(R.id.mute);
        settings = (Button) findViewById(R.id.setting);
        
        addFriend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddFriendActivity.class);
				startActivity(intent);
				//finish();
			}
        	
        });
        friendRequest.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FriendRequestActivity.class);
				startActivity(intent);
				//finish();
			}
        	
        });
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                end = System.currentTimeMillis();

                if(start != 0) {
                    duration = end - start + 1500;
                    start = 0;
                    end = 0;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String json = buildJsonForBuzz(duration);
                            String result = "";
                            result = PostUtil.POST(getString(R.string.buzz),json);
                            showToast("Vibration Sent");
                        }
                    }).start();

                }
                else{
                    showToast("It's too short, please try again!");
                }
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                start = System.currentTimeMillis();
                return false;
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                 while(true){

                String json = buildJsonForCheckingNewEvent();
                String result = "";
                result = PostUtil.POST(getString(R.string.newEvent), json);

                try {
                    JSONObject obj = new JSONObject(result).getJSONObject("0");
                    JSONObject info = obj.getJSONObject("info");
                    String eventType = info.getString("eventType");

                    if (eventType.equals("buzz")) {

                        String friendId = info.getString("friendId");
                        int duration = info.getInt("duration");

                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(duration);
                        showToast("Jack is buzzing you!");

                    } else if (eventType.equals("friendRequest")) {
                        JSONObject friendJson = info.getJSONObject("profile");
                        MyApplication app = (MyApplication) getApplication();
                        Friend friend = new Friend();
                        friend.setId(friendJson.getString("userId"));
                        friend.setName(friendJson.getString("userName"));
                        friend.setEmail(friendJson.getString("email"));
                        friend.setPhoneNumber(friendJson.getString("phoneNumber"));
                        String encodedImage = friendJson.getJSONObject("avatar").getString("content");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        friend.setAvatar(decodedByte);
                        app.setNiceOne(friend);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                friendRequest.setBackgroundColor(0xffff00);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
           }
        }).start();

    }

    private String buildJsonForCheckingNewEvent(){

        JSONObject request = new JSONObject();
        JSONObject info = new JSONObject();
        try {
            info.put("userId",User.getInstance().getId());
            request.put("info",info);
            request.put("action","buzz");
            request.put("type","request");
            return request.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    private String buildJsonForBuzz(long duration){

        JSONObject request = new JSONObject();
        JSONObject info = new JSONObject();
        try {
            info.put("userId", User.getInstance().getId());
            info.put("friendId","24");///!!!!
            info.put("duration",duration);
            request.put("info",info);
            request.put("action","buzz");
            request.put("type","request");
            return request.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
