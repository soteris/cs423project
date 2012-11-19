package com.trustme.testsu;

/**
 * Class to hold User specific information
 * @author sdemetr2
 *
 */
public class User {
	public static final String phone_number_KEY = "unum";
	public static String phone_number;
	
	/**
	 * Set the telephone number associated with this device
	 * @param num The user's telephone number
	 */
	public static void setPhoneNumber(String num){
		phone_number = num;
	}
	
	/**
	 * Get the user's telephone number
	 * @return The user's telephone number or null;
	 */
	public static String getPhoneNumber(){
		if (phone_number != null) {
			return phone_number;
		}
		else{
			return null;
		}
	}
}
