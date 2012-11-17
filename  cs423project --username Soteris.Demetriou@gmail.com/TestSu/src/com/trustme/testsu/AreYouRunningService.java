package com.trustme.testsu;

import com.trustme.testsu.root.Root;
import com.trustme.testsu.utils.Constants;
import com.trustme.testsu.utils.HelpFunctions;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AreYouRunningService extends Service{
	public static final String TAG = "AreYouRunningService";
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(TAG, "onCreate: Service got created");
        //Toast.makeText(this, "ServiceClass.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    	Log.i(TAG, "onDestroy: AreYouRunningService");
        super.onDestroy();
    }

    @Override
	public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStart: Service got started");
        
        int pid = HelpFunctions.getPidOfPack(Constants.COMPETITOR_PACK);
        if (pid != 0){
        	Toast.makeText(this, getString(R.string.GoogleMaps_EncounteredProblem), Toast.LENGTH_LONG).show();
        	Root rt = new Root();
        	rt.kill(pid);
        	Toast.makeText(this, getString(R.string.GoogleMaps_NotResponding), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, getString(R.string.GoogleMaps_IsTryingToCompromise), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, getString(R.string.GoogleMaps_NeedsToShutdown), Toast.LENGTH_LONG).show();
        	Toast.makeText(this, getString(R.string.SystemRecovery), Toast.LENGTH_LONG).show();
        	//Toast.makeText(this, "Not any more!", Toast.LENGTH_LONG).show();
        }
        this.stopSelf();
        
        return START_NOT_STICKY;
    }

}