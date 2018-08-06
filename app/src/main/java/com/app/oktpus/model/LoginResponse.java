package com.app.oktpus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 27/9/16.
 */

public class LoginResponse {

    @SerializedName("status")
    private int status;
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("message")
    private String message;

    public void setStatus(int status){ this.status = status; }
    public int getStatus() { return this.status;}

    public void setUserId(int userId){ this.user_id = userId; }
    public int getUserId() { return this.user_id;}

    public void setMessage(String message){ this.message = message; }
    public String getMessage() { return this.message;}


}
