package com.trustme.testsu.utils;

import java.util.List;

import com.trustme.testsu.MainActivity;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * Functions that can be common
 * @author sdemetr2
 *
 */
public class HelpFunctions {
	public static final String TAG = "HelpFunctions";
	
	/**
	 * Check whether a package is installed on the device
	 * @param packName the full package name of the app to search for
	 * @return true if the application is present , false if it is not
	 */
	public static boolean isPackagePresent(String packName){
		try {
			ApplicationInfo my_package = MainActivity.ctx.getPackageManager().getApplicationInfo(packName, 0);
			return true;
		} catch (NameNotFoundException e) {
			// Package does not exist!!
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get the PID of an application given its package name
	 * @param packName The full package name of the application to search for
	 * @return the pid of the application if exists or zero if it doesn't
	 */
	public static int getPidOfPack(String packName){
		int pid = 0;
		
		//Process process = Runtime.getRuntime().exec("ps");
		ActivityManager activity_manager = (ActivityManager) MainActivity.ctx.getSystemService(MainActivity.ctx.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> procInfos = activity_manager.getRunningAppProcesses();
		
		for (ActivityManager.RunningAppProcessInfo procInfo : procInfos){
			Log.i(TAG, "procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
			if (procInfo.processName.equals(packName)){
				//found packName
				pid = procInfo.pid;
				Log.i(TAG, "Found it! procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
				break;
			}
		}
		
		return pid;
	}
}
