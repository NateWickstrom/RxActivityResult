package com.natewickstrom.rxactivityresult.sample;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.natewickstrom.rxactivityresult.ActivityResult;
import com.natewickstrom.rxactivityresult.Launchable;
import com.natewickstrom.rxactivityresult.RxActivityResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageOptions;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxActivityResult";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);

        final RxActivityResult rxActivityResult = RxActivityResult.getInstance(this);
        final RxPermissions rxPermissions = RxPermissions.getInstance(this);

        final Launchable launchable = rxActivityResult.from(this);

        /**
         * Rx chain based on a button click:
         *     onClick -> check permissions -> get an image -> crop the image -> display it
         */
        RxView.clicks(findViewById(R.id.button))
                .compose(rxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                })
                .flatMap(new Func1<Boolean, Observable<ActivityResult>>() {
                    @Override
                    public Observable<ActivityResult> call(Boolean aBoolean) {
                        return launchable.startActivityForResult(getImageChooserIntent(), 12);
                    }
                })
                .filter(new Func1<ActivityResult, Boolean>() {
                    @Override
                    public Boolean call(ActivityResult result) {
                        return result.isOk() && result.getRequestCode() == 12;
                    }
                })
                .flatMap(new Func1<ActivityResult, Observable<ActivityResult>>() {
                    @Override
                    public Observable<ActivityResult> call(ActivityResult result) {
                        Uri uri = result.getData().getData();
                        return launchable.startActivityForResult(getCropImageIntent(uri), 34);
                    }
                })
                .filter(new Func1<ActivityResult, Boolean>() {
                    @Override
                    public Boolean call(ActivityResult result) {
                        return result.isOk() && result.getRequestCode() == 34;
                    }
                })
                .subscribe(
                        new Action1<ActivityResult>() {
                            @Override
                            public void call(ActivityResult result) {
                                Log.d(TAG, "OnResult");
                                displayImage(CropImage.getActivityResult(result.getData()).getUri());
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable t) {
                                Log.d(TAG, "OnError", t);
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                Log.d(TAG, "OnComplete");
                            }
                        }
                );
    }

    private void displayImage(Uri imageUri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent getImageChooserIntent() {
        return Intent.createChooser(
                new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Choose");
    }

    private Intent getCropImageIntent(Uri imageUri) {
        return new Intent(getApplicationContext(), CropImageActivity.class)
                .putExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE, imageUri)
                .putExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS, new CropImageOptions());
    }

}
