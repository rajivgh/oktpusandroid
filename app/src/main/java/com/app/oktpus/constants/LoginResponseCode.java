package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 16/8/17.
 */

public enum LoginResponseCode {
    SUCCESS(1),
    FAILURE(0),
    USER_NOT_FOUND(2);

    private int value;
    LoginResponseCode(int value) {this.value = value;}
    public int getValue() {return this.value;}
    public static LoginResponseCode fromValue(int val) {
        for(LoginResponseCode t : values()) {
            if(t.getValue() == val) {
                return t;
            }
        }
        return null;
    }
}
