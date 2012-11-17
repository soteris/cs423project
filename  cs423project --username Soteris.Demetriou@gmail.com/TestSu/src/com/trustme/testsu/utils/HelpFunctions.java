package com.trustme.testsu.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.trustme.testsu.MainActivity;
import com.trustme.testsu.MyApplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
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
		ActivityManager activity_manager = (ActivityManager) MyApplication.getAppContext().getSystemService(MyApplication.getAppContext().ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> procInfos = activity_manager.getRunningAppProcesses();
		
		for (ActivityManager.RunningAppProcessInfo procInfo : procInfos){
			//Log.i(TAG, "procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
			if (procInfo.processName.equals(packName)){
				//found packName
				pid = procInfo.pid;
				//Log.i(TAG, "Found it! procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
				break;
			}
		}
		
		return pid;
	}
	
	/**
	 * Creates a file for writing in the Internal Storage and return a BufferedWriter handle
	 * @param filename The name of the file to be created
	 * @param ctx The context of the Application
	 * @return A BufferedWriter to a file with name as filename stored in the private Internal Storage of this app
	 */
	public static BufferedWriter openFile(String filename, Context ctx){
		FileOutputStream fos;
		BufferedWriter bw;
		
		try {
			fos = ctx.openFileOutput(filename, ctx.MODE_PRIVATE);
			bw = new BufferedWriter(new OutputStreamWriter(fos));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bw = null;
		}
		
		return bw;
		
	}
	
}
