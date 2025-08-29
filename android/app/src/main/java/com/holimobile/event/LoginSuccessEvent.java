package com.holimobile.event;

public class LoginSuccessEvent {

    //登录完成并且获取个人信息完成后回调事件
    private boolean isLogin;

    public LoginSuccessEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
