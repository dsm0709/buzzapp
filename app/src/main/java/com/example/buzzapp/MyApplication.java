package com.example.buzzapp;

import android.app.Application;

import java.util.List;

import model.Friend;

/**
 * Created by July on 4/2/15.
 */
public class MyApplication extends Application {

    private List<Friend> friendList;
    private boolean isLogin = true;
    private Friend niceOne;

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Friend getNiceOne() {
        return niceOne;
    }

    public void setNiceOne(Friend niceOne) {
        this.niceOne = niceOne;
    }
}
