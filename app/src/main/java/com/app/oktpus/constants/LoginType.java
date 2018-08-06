package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 16/8/17.
 */

public enum LoginType {
    MODE_STANDARD_FORM (1),
    MODE_FB (2),
    MODE_GPLUS (3) ;

    private int value;
    LoginType(int val) { this.value = val; }
    public int getValue() {return this.value;}
    public static LoginType fromValue(int i) {
        for(LoginType lt : values()) {
            if(lt.getValue() == i){
                return lt;
            }
        }
        return null;
    }
}
