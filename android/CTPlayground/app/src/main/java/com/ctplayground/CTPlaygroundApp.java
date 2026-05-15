package com.ctplayground;

import android.app.Application;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;

public class CTPlaygroundApp extends Application {

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.VERBOSE);
        CleverTapAPI.getDefaultInstance(this); // initialises using manifest credentials
    }
}
