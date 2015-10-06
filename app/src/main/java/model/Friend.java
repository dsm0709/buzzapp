package model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by July on 4/1/15.
 */
public class Friend implements Serializable{

    private String id;
    private String phoneNumber;
    private String name;
    private String email;
    private Bitmap avatar;


    public Friend() {}

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
