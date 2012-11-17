package com.trustme.testsu;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Class to hold a package's info.
 * @author sdemetr2
 *
 */
class PInfo {
	public static final String TAG = "PInfo";
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public void prettyPrint() {
        //Log.v(TAG, appname + ";" + pname + ";" + versionName + ";" + versionCode);
    }
}

