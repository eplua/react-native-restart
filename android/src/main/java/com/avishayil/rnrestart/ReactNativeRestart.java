package com.avishayil.rnrestart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import android.app.Activity;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Avishay on 7/17/16.
 */
public class ReactNativeRestart extends ReactContextBaseJavaModule {

    private static final String REACT_APPLICATION_CLASS_NAME = "com.facebook.react.ReactApplication";
    private static final String REACT_NATIVE_HOST_CLASS_NAME = "com.facebook.react.ReactNativeHost";


    public ReactNativeRestart(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private void loadBundleLegacy() {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            // The currentActivity can be null if it is backgrounded / destroyed, so we simply
            // no-op to prevent any null pointer exceptions.
            return;
        }

        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getReactApplicationContext();
                Intent mStartActivity = new Intent(context, currentActivity.getClass());
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
                //                currentActivity.recreate();
            }
        });
    }

    private void loadBundle() {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    loadBundleLegacy();
                }
            });
        } catch (Exception e) {
            loadBundleLegacy();
        }
    }

    @ReactMethod
    public void Restart() {
        loadBundle();
    }

    @Override
    public String getName() {
        return "RNRestart";
    }

}
