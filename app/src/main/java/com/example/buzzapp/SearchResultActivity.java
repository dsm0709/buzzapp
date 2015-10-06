package com.example.buzzapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import Tool.PostUtil;
import model.Friend;
import model.User;


public class SearchResultActivity extends Activity {

    private TextView name;
    private ImageView avatar;
    private Button addFriend;
    private Friend friend;

    private String buildJsonForAddFriend(){


        JSONObject request = new JSONObject();
        JSONObject info = new JSONObject();
        User instance = User.getInstance();
        if(friend == null) return null;
               try {
            info.put("userId",instance.getId());
            info.put("friendId",friend.getId());
            request.put("info",info);
            request.put("type","request");
            request.put("action","addFriend");
            return request.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        getActionBar().hide();
        friend = (Friend) getIntent().getSerializableExtra("friend");

        name = (TextView)findViewById(R.id.friendName);
        avatar = (ImageView)findViewById(R.id.friendAvatar);
        addFriend = (Button)findViewById(R.id.friendAddButton);

        name.setText(friend.getName());
        Bitmap img = friend.getAvatar();
        if(img == null)
            img = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
        avatar.setImageBitmap(img);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String json = buildJsonForAddFriend();
                        String result = "";
                        result = PostUtil.POST(getString(R.string.add),json);
                    }
                }).start();


            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();

    }
}
