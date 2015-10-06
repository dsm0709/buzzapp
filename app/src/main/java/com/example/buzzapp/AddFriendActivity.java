package com.example.buzzapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Tool.PostUtil;
import model.Friend;
import model.User;

public class AddFriendActivity extends Activity{
	private Button back, sendRequest;
    private EditText phoneNumber;
	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);

        back = (Button) findViewById(R.id.addBack);
        sendRequest = (Button) findViewById(R.id.sendRequest);
        phoneNumber = (EditText) findViewById(R.id.add_phoneNumber);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(AddFriendActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        sendRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView textview = (TextView) findViewById(R.id.addtext);
                textview.setText("the request sent to phone");
                textview.setTextColor(Color.GREEN);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String json = buildJsonForAddFriend(phoneNumber.getText().toString());
                        String result = "";
                        result = PostUtil.POST(getString(R.string.search),json);

                        try {
                            Friend friend = new Friend();
                            JSONObject obj = new JSONObject(result).getJSONObject("0");
                            JSONObject info = obj.getJSONObject("info");

                            if(info.getBoolean("result")) {
                                JSONObject profile = info.getJSONObject("profile");
                                friend.setId(profile.getString("userId"));
                                friend.setName(profile.getString("userName"));
                                String encodedImage = profile.getJSONObject("avatar").getString("content");
                                byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                friend.setAvatar(decodedByte);
                                friend.setEmail(profile.getString("email"));
                                friend.setPhoneNumber(profile.getString("phoneNumber"));
                                Intent intent = new Intent(AddFriendActivity.this, SearchResultActivity.class);
                                intent.putExtra("friend",friend);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ).start();
            }
        });
    }

    private String buildJsonForAddFriend(String phoneNumber){

        try {
            JSONObject info = new JSONObject();
            JSONObject request = new JSONObject();

            info.put("phoneNumber",phoneNumber);
            request.put("action","search");
            request.put("type","request");
            request.put("info",info);
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
                Toast.makeText(AddFriendActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
	
}
