package com.trustme.testsu.root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

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
			Process process = Runtime.getRuntime().exec("su");
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
	 * There is something wrong with this..don't use it
	 * @param cmds
	 */
	public void RunAsRoot(String[] cmds){
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());            
		    for (String tmpCmd : cmds) {
		           os.writeBytes(tmpCmd+"\n");
		           os.flush();
		    }           
		    os.writeBytes("exit\n");  
		    os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	
	
}
