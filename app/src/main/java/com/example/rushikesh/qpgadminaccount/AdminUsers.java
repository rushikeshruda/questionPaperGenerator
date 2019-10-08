package com.example.rushikesh.qpgadminaccount;

/**
 * Created by Rushikesh on 05/04/2018.
 */

public class AdminUsers {
    public String userId;
    public String userName;
    public String userUid;

    public AdminUsers(){

    }

    public AdminUsers(String userId, String userName,String userUid) {
        this.userId = userId;
        this.userName = userName;
        this.userUid = userUid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserUid(){
        return userUid;
    }
}
