package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 5/1/17.
 */

public enum ModelStatus {
    NO_MODELS_FOUND (0),
    CHOOSE_A_MAKE_FIRST (1),
    SELECT (2),
    LOADING(3),
    READY(4);

    private int value;
    ModelStatus(int value) {
        this.value = value;
    }
    public int getValue() { return value;}
    public static ModelStatus fromValue(int value) {
        for (ModelStatus at : values()) {
            if (at.getValue() == value) {
                return at;
            }
        }
        return null;
    }
}