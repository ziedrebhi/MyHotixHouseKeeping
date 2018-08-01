package com.hotix.myhotixhousekeeping.utils;

import com.hotix.myhotixhousekeeping.entities.LoginModel;

/**
 * Created by ziedrebhi on 17/03/2017.
 */


public class UserInfoModel {
    private static final UserInfoModel holder = new UserInfoModel();

    private String Login;
    private LoginModel User;

    public static UserInfoModel getInstance() {

        return holder;
    }

    public LoginModel getUser() {
        return User;
    }

    public void setUser(LoginModel user) {
        User = user;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }


}
