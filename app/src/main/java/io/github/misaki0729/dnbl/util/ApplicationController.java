package io.github.misaki0729.dnbl.util;

public class ApplicationController extends com.activeandroid.app.Application {
    private static ApplicationController sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;


    }

    public static synchronized ApplicationController getsInstance() { return sInstance; }


}
