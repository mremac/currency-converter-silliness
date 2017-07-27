package com.natalie.myapplication;

/**
 * Created by EricMM on 22/07/2017.
 */

public class JsonModel{

    public String UserRole;
    public String UserName;
    public int Id;
    public String Email;

    public String getUserRole(){
        return UserRole;
    }

    public void setUserRole(String _userRole){
        UserRole = _userRole;
    }

    public String getUserName(){
        return  UserName;
    }

    public void setUserName(String _userName){
        UserName = _userName;
    }

    public int getId(){
        return Id;
    }

    public void setId(int _id){
        Id = _id;
    }

    public String getEmail(){
        return Email;
    }

    public void setEmail(String _email){
        Email = _email;
    }

}