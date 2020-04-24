package com.demo.opengl;

import android.app.Application;

/**
 *
 *
 * @author: xwli
 * @created: 2020/04/24
 * @content: 程序主Application
 * @version: 1.0.0
 */
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
