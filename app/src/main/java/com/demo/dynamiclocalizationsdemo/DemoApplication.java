package com.demo.dynamiclocalizationsdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by rahulhariharan on 11/07/18.
 */

public class DemoApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
