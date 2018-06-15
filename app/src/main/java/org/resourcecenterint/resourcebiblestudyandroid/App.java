package org.resourcecenterint.resourcebiblestudyandroid;

import android.app.Application;
import android.os.Handler;

public class App extends Application {

    private static App Instance;
    public static volatile Handler applicationHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Instance=this;

        applicationHandler = new Handler(getInstance().getMainLooper());

        NativeLoader.initNativeLibs(App.getInstance());

    }

    public static App getInstance()
    {
        return Instance;
    }
}
