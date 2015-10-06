package com.example.buzzapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import model.User;


public class SettingsActivity extends Activity {

    private TextView name, id, phoneNumber, imei, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

    }

    @Override
    protected void onResume(){
        super.onResume();
        User instance = User.getInstance();
        name = (TextView)findViewById(R.id.showName);
        id = (TextView)findViewById(R.id.showId);
        imei = (TextView)findViewById(R.id.showImei);
        email = (TextView)findViewById(R.id.showEmail);
        phoneNumber = (TextView)findViewById(R.id.showPhoneNumber);
        name.setText(instance.getName());
        id.setText(instance.getId());
        imei.setText(instance.getImei());
        phoneNumber.setText(instance.getPhoneNumber());
        email.setText(instance.getEmail());
    }
}
