package com.example.buzzapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import Tool.PostUtil;
import model.User;


public class LoginActivity extends Activity {
	private static final String[] m={"+1","+86","+30"};
    private Spinner spinner;  
    private ArrayAdapter<String> adapter; 
    private Button signup;
    private TextView phoneNumber;

        @Override
        protected void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            getActionBar().hide();

            signup = (Button) findViewById(R.id.signup);
            phoneNumber = (TextView) findViewById(R.id.login_phone_number);
            spinner = (Spinner) findViewById(R.id.Spinner01);

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setVisibility(View.VISIBLE);

            signup.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub

                    MyApplication app = (MyApplication) getApplication();
                    app.setLogin(false);

                    String str = phoneNumber.getText().toString();
                    Random rand = new Random(System.currentTimeMillis());
                    String verifyCode = String.valueOf(1000 + rand.nextInt(9000));
                    if (str.equals("")) {
                        Toast.makeText(LoginActivity.this, "Phone number can't be empty!", Toast.LENGTH_SHORT).show();
                    } else {

                        User instance = User.getInstance();
                        instance.setPhoneNumber(str);

                        String sendTo = spinner.getSelectedItem().toString() + str;
                        SmsManager smsManager = SmsManager.getDefault();
                        //smsManager.sendTextMessage(sendTo, null, "Your Verify Code is :" + verifyCode, null, null);
                        Toast.makeText(LoginActivity.this, "Your Verify code is sent to your phone number", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, verifyCode, Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(LoginActivity.this, ActivationActivity.class);
                        intent.putExtra("verifyCode", verifyCode);
                        startActivity(intent);
                        finish();
                    }

                }

            });

        }


}
