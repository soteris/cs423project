package com.trustme.testsu;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
	private static Context ctx;
	
	public void onCreate(){
		super.onCreate();
		MyApplication.ctx = getApplicationContext();
	}
	
	public static Context getAppContext() {
        return MyApplication.ctx;
    }
}
