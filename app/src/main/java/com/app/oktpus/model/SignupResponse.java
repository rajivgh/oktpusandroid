package com.app.oktpus.model;

/**
 * Created by Gyandeep on 27/9/16.
 */

public class SignupResponse {

    private int status;
    private int code;
    private String message;

    public void setStatus(int status){ this.status = status; }
    public int getStatus() { return this.status;}

    public void setCode(int code){ this.code = code; }
    public int getCode() { return this.code;}

    public void setMessage(String message){ this.message = message; }
    public String getMessage() { return this.message;}
}
