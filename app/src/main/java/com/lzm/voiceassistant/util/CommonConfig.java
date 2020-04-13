package com.lzm.voiceassistant.util;

import android.app.Application;
import android.content.Context;

public class CommonConfig {
    private static Application sApp;
    public static final String TAG = "Morris";

    public static void init(Application context) {
        sApp = context;
    }

    public static Application getApplication() {
        if (null == sApp) {
            throw new IllegalArgumentException("context cannot be null in getApplication method!!! check is init");
        }
        return sApp;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
}