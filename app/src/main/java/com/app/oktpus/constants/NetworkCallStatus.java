package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 13/11/17.
 */

public enum NetworkCallStatus {

    STARTED (0),
    IN_PROGRESS (1),
    SUCCESS (2),
    FAILED (3);

    private int value;
    NetworkCallStatus(int value) {
        this.value = value;
    }
    public int getValue() { return value;}
    public static NetworkCallStatus fromValue(int value) {
        for (NetworkCallStatus at : values()) {
            if (at.getValue() == value) {
                return at;
            }
        }
        return null;
    }
}
