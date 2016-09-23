package com.natewickstrom.rxactivityresult;

import android.app.Activity;
import android.content.Intent;

/**
 * This class holds the data received by {@link Activity#onActivityResult(int, int, Intent)}.
 */
public class ActivityResult {

    private final int resultCode;
    private final int requestCode;
    private final Intent data;

    public ActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public Intent getData() {
        return data;
    }

    public boolean isOk() {
        return resultCode == Activity.RESULT_OK;
    }

    public boolean isCanceled() {
        return resultCode == Activity.RESULT_CANCELED;
    }
}
