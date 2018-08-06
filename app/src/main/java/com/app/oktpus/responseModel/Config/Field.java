package com.app.oktpus.responseModel.Config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gyandeep on 8/12/16.
 */

public class Field {

    @SerializedName("label")
    private String label;

    @SerializedName("step")
    private int step;

    @SerializedName("step")
    private int duplicateStep;

    public String getLabel() {
        return label;
    }

    public int getStep() {
        return step;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDuplicateStep() {
        return duplicateStep;
    }

    public void setDuplicateStep(int duplicateStep) {
        this.duplicateStep = duplicateStep;
    }
}
