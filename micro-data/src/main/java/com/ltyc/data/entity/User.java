package com.ltyc.data.entity;

/**
 * @author ltyc
 * @version 1.0
 * @Description 用户
 * @create 17.12.18
 */

public class User {
    String id;
    String userName;
    String passWord;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
