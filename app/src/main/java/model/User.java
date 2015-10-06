package model;

import android.graphics.Bitmap;

/**
 * Created by July on 4/1/15.
 */
public class User {

    private String id;
    private String phoneNumber;
    private String imei;
    private String name;
    private String email;
    private Bitmap avatar;


    private static User instance;

    private User() {}

    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }

    public static void clear(){
        instance.id = "";
        instance.phoneNumber = "";
        instance.imei = "";
        instance.name = "";
        instance.email = "";
        instance.avatar = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
