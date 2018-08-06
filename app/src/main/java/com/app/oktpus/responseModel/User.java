package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 4/12/16.
 */

public class User {

    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private int id;

    public int getId() { return id; }

    public String getEmail() {
        return email;
    }

}
