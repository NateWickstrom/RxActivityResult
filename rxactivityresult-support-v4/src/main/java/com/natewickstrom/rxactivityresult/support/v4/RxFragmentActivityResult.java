package com.natewickstrom.rxactivityresult.support.v4;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.natewickstrom.rxactivityresult.Launchable;
import com.natewickstrom.rxactivityresult.RxActivityResult;

/**
 * Provide a way to receive the results of the {@link Activity} with RxJava.
 */
public class RxFragmentActivityResult {

    private static RxFragmentActivityResult sSingleton;

    private RxActivityResult rxActivityResult;

    public static RxFragmentActivityResult getInstance(Context ctx) {
        if (sSingleton == null) {
            sSingleton = new RxFragmentActivityResult(RxActivityResult.getInstance(ctx));
        }
        return sSingleton;
    }

    private RxFragmentActivityResult(RxActivityResult rxActivityResult) {
        this.rxActivityResult = rxActivityResult;
    }

    /**
     * Create new {@link Launchable} from launch source component({@link Activity}) of other activity.
     *
     * @param activity requesting result
     * @return New {@link Launchable} instance.
     */
    @CheckResult
    public Launchable from(@NonNull Activity activity) {
        return rxActivityResult.from(activity);
    }


    /**
     * Create new {@link Launchable} from launch source component({@link android.app.Fragment}) of other activity.
     *
     * @param fragment requesting result
     * @return New {@link Launchable} instance.
     */
    @CheckResult
    public Launchable from(@NonNull final android.app.Fragment fragment) {
        return new RxActivityResult.Launcher(rxActivityResult) {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            protected void startActivity(Intent intent) {
                fragment.startActivity(intent);
            }
        };
    }

    /**
     * Create new {@link Launchable} from l launch source component({@link Fragment}) of other
     * activity.
     *
     * @param fragment requesting result
     * @return New {@link Launchable} instance.
     */
    @CheckResult
    public Launchable from(@NonNull final Fragment fragment) {
        return new RxActivityResult.Launcher(rxActivityResult) {
            @Override
            protected void startActivity(Intent intent) {
                fragment.startActivity(intent);
            }
        };
    }

}
