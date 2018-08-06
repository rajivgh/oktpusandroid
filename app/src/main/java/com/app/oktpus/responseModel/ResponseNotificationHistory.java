package com.app.oktpus.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class ResponseNotificationHistory {

    @SerializedName("status")
    private int status;

    @SerializedName("result")
    private List<NotificationHistoryFlagsResult> notifListResult;

    public int getStatus() {
        return status;
    }

    public List<NotificationHistoryFlagsResult> getNotifListResult() {
        return notifListResult;
    }
}
