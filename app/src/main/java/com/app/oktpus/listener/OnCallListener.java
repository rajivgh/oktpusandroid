package com.app.oktpus.listener;

import java.io.IOException;

/**
 * Created by Gyandeep on 4/10/16.
 */

public interface OnCallListener<Response> {
    void nwResponseData(String resData) throws IOException;
}