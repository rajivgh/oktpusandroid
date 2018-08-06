package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 2/12/16.
 */

public class ResponseLogin extends CommonResponses{

    @SerializedName("user_id")
    private int userId;
    @SerializedName("message")
    private String message;

    public User getUser() { return user; }

    @SerializedName("user")
    private User user;

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
