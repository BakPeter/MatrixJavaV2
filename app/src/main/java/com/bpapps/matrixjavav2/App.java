package com.bpapps.matrixjavav2;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App sInstance = null;

    public static Context getAppContext() throws IllegalStateException {
        if (sInstance == null) {
            throw new IllegalStateException("App is not initialized");
        }

        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    @Override
    public void onTerminate() {
        sInstance = null;

        super.onTerminate();
    }
}
