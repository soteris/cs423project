package com.trustme.testsu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class RestartActivity extends Activity{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // the prototype RestartActivity there is a possibility that this will be the root activity of the app.
	        // If that is the case, this activity will boot the main activity of the app in a new task, before signing off.
	        // However, this is not the case for this app, as restarts are only used once a call diversion has taken place,
	        // form within the app.
	        if (isTaskRoot())
	        {
	            // Start the app before finishing
	        	Log.i("RestartActivity", "RestartActivity");
	            String packageName = this.getBaseContext().getPackageName();
	            Intent startAppIntent = this.getBaseContext().getPackageManager().getLaunchIntentForPackage(packageName);
	            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
	            startActivity(startAppIntent);
	        }
	        else{
	        	Log.i("RestartActivity", "RestartActivity !isTaskRoot");
	        }
	        // Now finish, which will drop the user in to the activity that was at the top of the task stack
	        finish();
	    }
}
