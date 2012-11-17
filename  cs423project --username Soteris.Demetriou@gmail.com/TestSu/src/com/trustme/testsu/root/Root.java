package com.trustme.testsu.root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.trustme.testsu.MyApplication;
import com.trustme.testsu.utils.Constants;

import android.util.Log;
import android.widget.Toast;

/**
 * Class to perform root activities
 * @author sdemetr2
 *
 */
public class Root {
	public static final String TAG = "Root";
	//public static boolean isRoot = false;
	
	
	/**
	 * Kills a running process
	 * @param pid The PID to kill
	 */
	public void kill(int pid){
		//android.os.Process.killProcess(pid);
		try {
			//Toast.makeText(MyApplication.getAppContext(), "LOLOLOLOLOLOLOLOLOL", Toast.LENGTH_LONG).show();
			Process process = Runtime.getRuntime().exec("su");
			//Toast.makeText(MyApplication.getAppContext(), "LALALALALALALALALA", Toast.LENGTH_LONG).show();
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			DataInputStream osRes = new DataInputStream(process.getInputStream());
			Object commands;
			//for (String single : commands) {
			   os.writeBytes("kill " + pid + "\n");
			   os.flush();
			   //res.add(osRes.readLine());
			//}
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "IOException while executing su kill. " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Copies a file from a restricted area to the application's private area and
	 * 	changes the file's permissions allowing read and write by everyone
	 * You can access the new_file with the help of Constants.INTERNAL_STORAGE_PATH
	 * @param or_path The path of the original restricted file
	 * @param or_filename The name of the original restricted file
	 * @param new_filename The new masquerade name for the file in the Internal Storage
	 */
	public void getSecretFile(String[] commands){
		//android.os.Process.killProcess(pid);
		try {
			Process process = Runtime.getRuntime().exec("su");
			//Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			DataInputStream osRes = new DataInputStream(process.getInputStream());
			
			for (String command : commands){
			   //Log.i(TAG, command);
			   os.writeBytes(command);
			   os.flush();
			   
			   Thread.sleep(200);
			}
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "IOException while executing su getSecretFiles. " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "!!!!!!!!!!InterruptedException while executing su getSecretFiles. " + e);
		}
	}		
	
}
