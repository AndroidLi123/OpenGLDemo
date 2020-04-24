package com.demo.opengl;

import android.app.Application;


public class DemoApplication extends Application {
    private static DemoApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static DemoApplication getApplication() {
        return application;
    }

}
