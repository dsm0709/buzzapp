package com.example.buzzapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import Tool.PostUtil;

import model.User;


public class ActivationActivity extends Activity {
	
	private Button button = null;
    private EditText filledCode;
    private TextView test;


    private String buildJsonForLogin(){



        try {
            JSONObject info = new JSONObject();
            JSONObject request = new JSONObject();

            User instance = User.getInstance();

            info.put("phoneNumber",instance.getPhoneNumber());
            info.put("imei",instance.getImei());
            request.put("action","login");
            request.put("type","request");
            request.put("info",info);
            return request.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);




        filledCode = (EditText)findViewById(R.id.verify_code);
        test = (TextView)findViewById(R.id.testText);
        button = (Button) findViewById(R.id.activateSubmit);
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

                MyApplication app = (MyApplication)getApplication();
                Intent i = getIntent();
                String vc = i.getStringExtra("verifyCode");
                String code = filledCode.getText().toString();

                if(code.equals(vc) && app.isLogin()){ // code valid and is for login

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String json = buildJsonForLogin();
                            String result = "";
                            result = PostUtil.POST(getString(R.string.login),json);
                            try {

                                User instance = User.getInstance();
                                JSONObject obj = new JSONObject(result).getJSONObject("0");
                                JSONObject info = obj.getJSONObject("info");

                                if(info.getBoolean("result")){
                                    JSONObject profile = info.getJSONObject("profile");
                                    instance.setId(profile.getString("userId"));
                                    instance.setName(profile.getString("userName"));
                                    String encodedImage = profile.getJSONObject("avatar").getString("content");
                                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    instance.setAvatar(decodedByte);
                                    instance.setEmail(profile.getString("email"));
                                    showToast("Login Successfully!");
                                    Intent intent = new Intent(ActivationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{ // login failed due to server
                                    showToast("Failed to Login");
                                    User.clear();
                                    Intent intent = new Intent(ActivationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                else if(code.equals(vc) && !app.isLogin()){ // goes to profile activity to fill profile info

                    Intent intent = new Intent(ActivationActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
                else{ // login failed due to code invalid
                    Toast.makeText(ActivationActivity.this,"Failed to Login",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
			}
        });
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(ActivationActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

