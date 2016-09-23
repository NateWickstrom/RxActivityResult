package com.natewickstrom.rxactivityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

/**
 * Interface to delegate the launch of an activity and return the result as an {@link Observable}
 */
public interface Launchable {

    /**
     * Launch an activity for which you would like a result when it finished.
     * When this activity exits, the {@link Observable} will be called with the given
     * {@link ActivityResult}. Using a negative requestCode is the same as calling
     * {@link Activity#startActivity} (the activity is not launched as a sub-activity),
     * and hence will result in a {@link IllegalArgumentException}.
     *
     * <p>Note that this method should only be used with Intent protocols
     * that are defined to return a result.  In other protocols (such as
     * {@link Intent#ACTION_MAIN} or {@link Intent#ACTION_VIEW}), you may
     * not get the result when you expect.  For example, if the activity you
     * are launching uses the singleTask launch mode, it will not run in your
     * task and thus you will never receive a result.
     *
     * <p>This method throws {@link IllegalArgumentException}
     * if there was no Activity found to run the given Intent.
     *
     * @param intent The intent to start.
     * @param requestCode If >= 0, this code will be returned in
     *                    onActivityResult() when the activity exits.
     * @param options Additional options for how the Activity should be started.
     * See {@link android.content.Context#startActivity(Intent, Bundle)
     * Context.startActivity(Intent, Bundle)} for more details.
     *
     * @throws IllegalArgumentException when requestCode is less than zero
     *
     */
    @CheckResult
    Observable<ActivityResult> startActivityForResult(@NonNull Intent intent, int requestCode,
                                                      @Nullable Bundle options);

    /**
     * Same as calling #startActivityForResult(Intent, int, Bundle)
     * with no options.
     *
     * @param intent The intent to start.
     * @param requestCode If >= 0, this code will be returned in {@link ActivityResult}
     *                    when the activity exits.
     * @return the result
     *
     * @throws IllegalArgumentException when requestCode is less than zero
     */
    @CheckResult
    Observable<ActivityResult> startActivityForResult(@NonNull Intent intent, int requestCode);
}
