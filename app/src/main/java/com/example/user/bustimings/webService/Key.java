package com.example.user.bustimings.webService;

/**
 * Created by user on 3/2/2018.
 */

public class Key {

    private String accountKey = "SFtHKwbETzi/0lasguUW5g==";
    private String accept = "application/json";

    public Key(){
    }

    public String GetKey(){
        return accountKey;
    }

    public String GetAccept(){
        return accept;
    }

}