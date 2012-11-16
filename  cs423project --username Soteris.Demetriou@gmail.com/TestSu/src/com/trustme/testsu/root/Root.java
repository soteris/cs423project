package com.trustme.testsu.root;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.trustme.testsu.utils.Constants;

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
	 * Copies a file from a restricted area to the application's private area and
	 * 	changes the file's permissions allowing read and write by everyone
	 * You can access the new_file with the help of Constants.INTERNAL_STORAGE_PATH
	 * @param or_path The path of the original restricted file
	 * @param or_filename The name of the original restricted file
	 * @param new_filename The new masquerade name for the file in the Internal Storage
	 */
	public void getSecretFile(String or_path, String or_filename, String new_filename){
		//android.os.Process.killProcess(pid);
		try {
			Process process = Runtime.getRuntime().exec("su");
			//Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "system/bin/sh"});
			DataOutputStream os = new DataOutputStream(process.getOutputStream());
			DataInputStream osRes = new DataInputStream(process.getInputStream());
			
			String[] commands = new String[1];
			//commands[0] = new String("chmod 777 " + "/data/system/sync/" + Constants.NEW_ACCOUNTS_NAME );
			commands[0] = new String("cp " + or_path + or_filename + " " + Constants.INTERNAL_STORAGE_PATH + new_filename + "\n");
			//commands[1] = new String("");
			//commands[1] = new String("chmod 777 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
			
			for (String command : commands){
//			String command = new String("cp " + or_path + or_filename + " " + Constants.INTERNAL_STORAGE_PATH + new_filename + "\n" +
//			   		"chmod 666 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
//			String command = new String("cp " + or_path + or_filename + " " + Constants.INTERNAL_STORAGE_PATH + new_filename + "\n" +
//					"chown u0_a75 " + Constants.INTERNAL_STORAGE_PATH + new_filename + "\n" +
//			   		"chmod 666 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
			   Log.i(TAG, command);
			   os.writeBytes(command);
			   os.flush();
			   
			   Thread.sleep(200);
			   //res.add(osRes.readLine());
			}
			Log.i(TAG, "flushed commands. Preparing to exit");
			os.writeBytes("exit\n");
			os.flush();
			Log.i(TAG, "flushed exit. Wait for..");
			process.waitFor();
			Log.i(TAG, "Wait for returned!");
			//Process p2 = Runtime.getRuntime().exec("su -c " + "chmod 666 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
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
	
	public void changeMode(String or_path, String or_filename, String new_filename){
		//android.os.Process.killProcess(pid);
		try {
			Process process = Runtime.getRuntime().exec("su chmod 7777 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
			//Process p2 = Runtime.getRuntime().exec("chmod 7777 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
//			DataOutputStream os = new DataOutputStream(process.getOutputStream());
//			DataInputStream osRes = new DataInputStream(process.getInputStream());
//			
//			String[] commands = new String[1];
//			//commands[0] = new String("chmod 7777 " + Constants.INTERNAL_STORAGE_PATH + new_filename);
//			commands[0] = new String("chown u0_a75" + Constants.INTERNAL_STORAGE_PATH + new_filename);
//			
//			for (String command : commands){
//			   Log.i(TAG, command);
//			   os.writeBytes(command);
//			   os.flush();
//			   //res.add(osRes.readLine());
//			}
//			Log.i(TAG, "flushed commands. Preparing to exit");
//			os.writeBytes("exit\n");
//			os.flush();
//			Log.i(TAG, "flushed exit. Wait for..");
//			process.waitFor();
//			Log.i(TAG, "Wait for returned!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "IOException while executing su getSecretFiles. " + e);
		} 
//		catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.d(TAG, "!!!!!!!!!!InterruptedException while executing su getSecretFiles. " + e);
//		}
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
