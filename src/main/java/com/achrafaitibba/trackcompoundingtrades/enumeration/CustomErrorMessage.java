package com.achrafaitibba.trackcompoundingtrades.enumeration;

public enum CustomErrorMessage {
    USER_NOT_FOUND("Error message hh...");

    private final String msg;
    CustomErrorMessage(String msg){
        this.msg = msg;
    }
    public String getMessage(){
        return this.msg;
    }
}
