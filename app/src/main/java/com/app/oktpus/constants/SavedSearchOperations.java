package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 28/11/16.
 */

public enum SavedSearchOperations {

    RECEIVE_NOTIF (0),
    LAUNCH_SEARCH (1),
    DELETE_SEARCH (2);

    private int value;
    SavedSearchOperations(int value) {
        this.value = value;
    }
    public int getValue() { return value;}
    public static SavedSearchOperations fromValue(int value) {
        for (SavedSearchOperations at : values()) {
            if (at.getValue() == value) {
                return at;
            }
        }
        return null;
    }
}