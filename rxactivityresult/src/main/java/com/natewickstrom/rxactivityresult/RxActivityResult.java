package com.natewickstrom.rxactivityresult;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Provide a way to receive the results of the {@link Activity} with RxJava.
 */
public class RxActivityResult {

    static final String INTENT = "intent";
    static final String REQUEST_CODE = "request";
    static final String BUNDLE = "Bundle";

    private static RxActivityResult sSingleton;

    private Context context;
    private Map<Integer, PublishSubject<ActivityResult>> results = new HashMap<>();

    public static RxActivityResult getInstance(Context ctx) {
        if (sSingleton == null) {
            sSingleton = new RxActivityResult(ctx.getApplicationContext());
        }
        return sSingleton;
    }

    private RxActivityResult(Context ctx) {
        context = ctx;
    }

    /**
     * Create new {@link Launchable} from launch source component({@link Activity}) of other activity.
     *
     * @param activity requesting result
     * @return New {@link Launchable} instance.
     */
    @CheckResult
    public Launchable from(@NonNull final Activity activity) {
        return new Launcher(this) {
            @Override
            protected void startActivity(Intent intent) {
                activity.startActivity(intent);
            }
        };
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        PublishSubject<ActivityResult> subject = results.remove(requestCode);
        ActivityResult result = new ActivityResult(requestCode, resultCode, data);
        subject.onNext(result);
        subject.onCompleted();
    }

    void onError(int requestCode, ActivityNotFoundException exception) {
        PublishSubject<ActivityResult> subject = results.remove(requestCode);
        subject.onError(exception);
    }

    private Intent prepareIntent(final Intent intent, final int requestCode, @Nullable final Bundle options) {
        Intent shadowIntent = new Intent(context, ShadowActivity.class);
        shadowIntent.putExtra(INTENT, intent);
        shadowIntent.putExtra(REQUEST_CODE, requestCode);
        if (options != null)
            shadowIntent.putExtra(BUNDLE, options);
        return shadowIntent;
    }

    private PublishSubject<ActivityResult> prepareSubject(final int requestCode) {
        PublishSubject<ActivityResult> subject = results.get(requestCode);
        if (subject == null)
                subject = PublishSubject.create();
        results.put(requestCode, subject);
        return subject;
    }

    public static abstract class Launcher implements Launchable {

        private final RxActivityResult rxActivityResult;

        protected abstract void startActivity(Intent intent);

        public Launcher(RxActivityResult rxActivityResult) {
            this.rxActivityResult = rxActivityResult;
        }

        @Override
        public Observable<ActivityResult> startActivityForResult(@NonNull Intent intent, int requestCode) {
            return startActivityForResult(intent, requestCode, null);
        }

        @Override
        public Observable<ActivityResult> startActivityForResult(@NonNull Intent intent, int requestCode,
                                                                 @Nullable Bundle options) {
            if (requestCode < 0)
                throw new IllegalArgumentException("requestCode must be greater than 0");

            PublishSubject<ActivityResult> subject = rxActivityResult.prepareSubject(requestCode);
            Intent shadowIntent = rxActivityResult.prepareIntent(intent, requestCode, options);
            startActivity(shadowIntent);
            return subject;
        }
    }

}
