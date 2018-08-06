package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 4/10/16.
 */

public enum ResponseCode {

    STATUS_FAILURE (0),
    STATUS_SUCCESS (1);

    private int value;
    ResponseCode(int value) {
        this.value = value;
    }
    public int getValue() { return value;}
    public static ResponseCode fromValue(int value) {
        for (ResponseCode at : values()) {
            if (at.getValue() == value) {
                return at;
            }
        }
        return null;
    }

}