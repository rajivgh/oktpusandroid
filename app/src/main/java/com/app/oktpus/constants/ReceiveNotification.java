package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 24/11/16.
 */

public enum ReceiveNotification {

    RECEIVE_NOTIF_DISABLED (0),
    RECEIVE_NOTIF_ENABLED_HIDE (1),
    RECEIVE_NOTIF_ENABLED_SHOW (2);

    private int value;
    ReceiveNotification(int value) {
        this.value = value;
    }
    public int getValue() { return value;}
    public static ReceiveNotification fromValue(int value) {
        for (ReceiveNotification at : values()) {
            if (at.getValue() == value) {
                return at;
            }
        }
        return null;
    }
}