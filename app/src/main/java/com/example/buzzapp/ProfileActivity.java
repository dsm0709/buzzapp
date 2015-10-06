package com.example.buzzapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import IA.ImageAdapter;
import IA.ProImageAdapter;
import Tool.PostUtil;
import model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends Activity {
	
	private ImageButton imageButton;
	private Button button;
    private EditText name, email;
	private int pos = 0;
	private int[] images = {R.drawable.cat, R.drawable.fish,
            R.drawable.rabbit, R.drawable.turtle,
            R.drawable.horse, R.drawable.bird,};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getActionBar().hide();
        imageButton = (ImageButton) findViewById(R.id.imageButton1);
        name = (EditText)findViewById(R.id.name);
        email= (EditText)findViewById(R.id.email);
        addListenerOnButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        

        
        
        return true;
    }

    private String buildJsonForSignUp(){


        JSONObject request = new JSONObject();
        JSONObject info = new JSONObject();
        JSONObject avatar = new JSONObject();
        User instance = User.getInstance();
        Bitmap bm = instance.getAvatar();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        try {
            avatar.put("content",encodedImage);
            avatar.put("contentType","image/jpg");
            info.put("avatar",avatar);
            info.put("phoneNumber",instance.getPhoneNumber());
            info.put("imei",instance.getImei());
            info.put("userName",instance.getName());
            info.put("email",instance.getEmail());
            request.put("info",info);
            request.put("type","request");
            request.put("action","signUp");
            return request.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public void addListenerOnButton() {
    	 

 
		imageButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				initImageChooseDialog();
				//imageChooseDialog.show();
			}
                
                
			});
		
		button = (Button) findViewById(R.id.profileSubmit);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User instance = User.getInstance();
                        instance.setName(name.getText().toString());
                        instance.setEmail(email.getText().toString());
                        Bitmap bitmap = ((BitmapDrawable)imageButton.getDrawable()).getBitmap();
                        instance.setAvatar(bitmap);
                        String json = buildJsonForSignUp();

                        String result = "";
                        result = PostUtil.POST(getString(R.string.signUp),json);

                        try {
                            JSONObject obj = new JSONObject(result).getJSONObject("0");
                            JSONObject info = obj.getJSONObject("info");

                            if(info.getBoolean("result") && info.getString("userId") != null){

                                instance.setId(info.getString("userId"));
                                showToast("Your account is created!");
                                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                showToast("Failed to create, please try again!");
                                User.clear();
                                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

			}
        	
        });

 
		}
    
    private void initImageChooseDialog() {

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	GridView gridView = new GridView(this);
    	
        gridView.setAdapter(new ProImageAdapter(this));
        gridView.setNumColumns(2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	pos = position;
            	
            }
        });

        // Set grid view to alertDialog
                
        builder.setPositiveButton("confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
                        Resources r = getResources();
						imageButton.setImageResource(images[pos]);

                        User.getInstance().setAvatar(BitmapFactory.decodeResource(r, images[pos]));
//					
					}
				});
        
        builder.setView(gridView);
        builder.setTitle("Choose your photo");
        builder.show();
       
		

	}

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(ProfileActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
 



    
    
    
    
    