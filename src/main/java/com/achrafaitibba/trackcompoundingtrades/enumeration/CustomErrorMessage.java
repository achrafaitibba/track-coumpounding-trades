package com.achrafaitibba.trackcompoundingtrades.enumeration;

public enum CustomErrorMessage {
    ACCOUNT_ALREADY_EXIST("The username you provided already used");
    private final String msg;
    CustomErrorMessage(String msg){
        this.msg = msg;
    }
    public String getMessage(){
        return this.msg;
    }
}
