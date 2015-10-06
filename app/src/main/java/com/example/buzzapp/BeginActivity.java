package com.example.buzzapp;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import Tool.PostUtil;
import model.Friend;
import model.User;


public class BeginActivity extends Activity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        getActionBar().hide();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                //AutoLogin
                String imei = "00000000000002";
                //String imei = ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                User instance = User.getInstance();
                instance.setImei(imei);
                String json = buildJsonForAutoLogin();
                String result = "";
                result = PostUtil.POST(getString(R.string.login), json);
                try {
                    JSONObject obj = new JSONObject(result).getJSONObject("0");
                    JSONObject info = obj.getJSONObject("info");

                    if (info.getBoolean("result")) {
                        JSONObject profile = info.getJSONObject("profile");
                        instance.setId(profile.getString("userId"));
                        instance.setName(profile.getString("userName"));
                        String encodedImage = profile.getJSONObject("avatar").getString("content");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        instance.setAvatar(decodedByte);
                        instance.setEmail(profile.getString("email"));
                        showToast("Login Successfully!");
                        }
                        else{
                             showToast("Welcome to BuzzApp!");
                             Intent intent = new Intent(BeginActivity.this, LoginActivity.class);
                             startActivity(intent);
                             finish();
                             return;
                             }
                } catch (JSONException e){
                        e.printStackTrace();
                    }


                //GetFriendList
                MyApplication app = (MyApplication)getApplication();
                List<Friend> friendList = new ArrayList<Friend>();
                json = buildJsonForFriendList();
                result = PostUtil.POST(getString(R.string.friendList),json);
                try {
                    JSONObject obj = new JSONObject(result).getJSONObject("0");
                    JSONObject info = obj.getJSONObject("info");

                    JSONArray friendListJson = info.getJSONArray("friendList");
                    int length = friendListJson.length();
                    for(int i = 0; i < length; i++){
                        Friend friend = new Friend();
                        JSONObject friendJson = friendListJson.getJSONObject(i);
                        friend.setId(friendJson.getString("userId"));
                        friend.setName(friendJson.getString("userName"));
                        friend.setEmail(friendJson.getString("email"));
                        friend.setPhoneNumber(friendJson.getString("phoneNumber"));
                        String encodedImage = friendJson.getJSONObject("avatar").getString("content");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        friend.setAvatar(decodedByte);
                        friendList.add(friend);
                    }
                    app.setFriendList(friendList);
                    showToast("Getting Friend List successfully!");

                } catch (JSONException e){
                 //   showToast("Friend List code: 3301");
                }
                Intent intent = new Intent(BeginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        t.start();
    }

    private String buildJsonForFriendList() {


        try {
            JSONObject info = new JSONObject();
            JSONObject request = new JSONObject();
            User instance = User.getInstance();
            info.put("userId",instance.getId());
            request.put("action", "getFriendList");
            request.put("type", "request");
            request.put("info", info);
            return request.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String buildJsonForAutoLogin() {


        try {
            JSONObject info = new JSONObject();
            JSONObject request = new JSONObject();
            User instance = User.getInstance();
            info.put("phoneNumber", null);
            info.put("imei", instance.getImei());
            request.put("action", "login");
            request.put("type", "request");
            request.put("info", info);
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
                Toast.makeText(BeginActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
