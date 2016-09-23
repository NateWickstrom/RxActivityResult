package com.natewickstrom.rxactivityresult;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

/**
 * Wrapper for onActivityResult
 */
public class ShadowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Intent requestIntent = intent.getParcelableExtra(RxActivityResult.INTENT);
        int requestCode = intent.getIntExtra(RxActivityResult.REQUEST_CODE, 0);

        try {
            startActivityForResult(requestIntent, requestCode);
        } catch (ActivityNotFoundException exception) {
            RxActivityResult.getInstance(this).onError(requestCode, exception);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RxActivityResult.getInstance(this).onActivityResult(requestCode, resultCode, data);
        finish();
    }

}
