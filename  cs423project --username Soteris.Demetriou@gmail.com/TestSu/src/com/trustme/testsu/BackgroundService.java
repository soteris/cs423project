package com.trustme.testsu;



import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class BackgroundService extends Service{
	private final static String TAG = "BackgroundService";
	
	private static final Handler handlerIpCheck = new Handler();

	public static final int INTERVAL_NET = 1000; //between threat looping 100ms

	private static final int DELAY = 500; //when starting the thread 1second 
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		Log.i(TAG, "onCreate");
	}
	
	/**
	 * It starts the Looper Thread that checks for the IP
	 */
	private void startReader(){
			new IpCheck().start();
			handlerIpCheck.removeCallbacks(readARP);
	        handlerIpCheck.postDelayed(readARP, DELAY);
	}
	
	/**
	 * It stops the Looper Thread that checks for the IP
	 */
	private void stopReader(){
			// TODO (xzhou) stop network logger thread here. 
			handlerIpCheck.removeCallbacks(readARP);
			IpCheck.handler.getLooper().quit();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		int cmd = intent.getExtras().getInt("CMD");
		Log.i(TAG, "onStartCommand: " + cmd);
		if(cmd == WiFiTracker.START_IP_CHECK){
			Log.i(TAG, "xxxx");
			startReader();
		}else if (cmd == WiFiTracker.STOP_IP_CHECK){
			stopReader();
		}
		return START_NOT_STICKY;
	}
	
	 /**
     * Runnable that a handler triggers every time INTERVAL to 
     * start the IpCheck Thread that checks the arp file for an IP address
     * @author sdemetr2
     */
	private Runnable readARP = new Runnable() 
	{
	    public void run() 
	    {
	    	if (IpCheck.handler != null){
	    		//Log.i(TAG, "NetRequest Interval:" + (SystemClock.elapsedRealtime() - netRequestInterval));
				Message read_arp = IpCheck.handler.obtainMessage();
				Bundle b = new Bundle();
//				b.putString("fileSuffix", fileSuffix);
//				b.putString("packageName", packageName);
//				b.putLong("serviceBeginTime", serviceRunningTime);
				read_arp.setData(b);
				//Log.i(TAG, "Take measurement: " + netCounter);
				IpCheck.handler.sendMessage(read_arp);
				handlerIpCheck.postDelayed(this, INTERVAL_NET);
			}
	    }
	};	
	
	@Override
	public void onDestroy(){
		// TODO(xzhou)
		Log.i(TAG, "onDestory()");

		stopReader();
		super.onDestroy();
		return;
	}
}

