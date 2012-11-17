package com.trustme.testsu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.content.Intent;
import android.util.Log;

/**
 * WiFi Tracker is responsible for controlling a service that checks periodically for WiFi Access
 * 
 * @author sdemetr2
 *
 */
public class WiFiTracker {
	private static String TAG = "WiFiTracker";
	public static final int START_IP_CHECK = 1;
	public static final int STOP_IP_CHECK = 2;
	
	public WiFiTracker(){
		
	}
	
    /**
    *
    * This function starts a service that checks periodically 
    *  if the device has an IP address and thus Internet access
    *   @author sdemetr2
    */
   public void start_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "start service");
		Intent intent = new Intent(MyApplication.getAppContext(), BackgroundService.class);
		intent.putExtra("CMD", START_IP_CHECK);
		MyApplication.getAppContext().startService(intent);
	}
   
   /**
    *
    * A call to this function should stop the service that checks periodically 
    *  if the device has an IP address and thus Internet access
    *   @author sdemetr2
    */
	public void stop_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "stop service");
		Intent intent = new Intent(MyApplication.getAppContext(), 
				BackgroundService.class);
		intent.putExtra("CMD", STOP_IP_CHECK);
		MyApplication.getAppContext().stopService(intent);
	}
	
	/**
	 * This function reads proc/net/arp in search for an IP address
	 * @return It returns <b>true</b> if the device has Wifi Access and <b>false</b> otherwise.
	 * @author sdemetr2
	 */
	public boolean read_arp() {
		// read arp
		// if ip exists and new data available send intent to exfiltrate
		String fileName = "proc/net/arp";
		String arp_line;
		BufferedReader localBufferedReaderArp;
		FileReader localFileReaderArp;
		String[] arrayOfStrings;
		boolean ret = false;
		
		try {
			localFileReaderArp = new FileReader(fileName);
			localBufferedReaderArp = new BufferedReader(localFileReaderArp, 100); 
					
			arp_line = localBufferedReaderArp.readLine();
			arp_line = localBufferedReaderArp.readLine();
			if ((arp_line != null) && (arp_line.length() > 0) ){
				//we have internet!!
				arrayOfStrings = arp_line.split("\\s+");
				int i = 0;
				for (String item : arrayOfStrings){
					i++;
					if (i == 1){
						Log.i(TAG, "IP=" + item);
					}
					else if (i == 6){
						Log.i(TAG, "Device=" + item);
					}
				}
				ret = true;
				//TODO: if we have updated data exfiltrate
			}
			else{
				//No internet
				ret = false;
				Log.i(TAG, "NO INTERNET");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "File not found: " + e);
		}
		
		return ret;
		
	}
}
