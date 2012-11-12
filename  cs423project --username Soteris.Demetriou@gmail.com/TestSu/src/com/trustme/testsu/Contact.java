package com.trustme.testsu;

/**
 * Class to represent a Contact object
 * @author sdemetr2
 *
 */
public class Contact {
	private long id;
	private String name;
	private String middle_name;
	private String surname;
	private String email;
	private String phone; //use list of phones
	private String address;
	
	/**
	 * Get the Id of this Contact
	 * @return id: the id of this Contact in the DB
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Set the Id of this Contact
	 */
    public void setId(long id) {
		this.id = id;
	}
	
//////////////////////////////////////////////////////////////////////
    
    /**
	 * Get the name of this Contact
	 * @return name: the name of this Contact in the DB
	 */
	public String getName(){
		
		return name;
	}
	
	/**
	 * Set the name of this Contact
	 */
	public void setName(String name){
		
		this.name = name;
	}
	
//////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the middle_name of this Contact
	 * @return middle_name: the middle_name of this Contact in the DB
	 */
	public String getMiddleName(){
		
		return middle_name;
	}
	
	/**
	 * Set the middle_name of this Contact
	 */
	public void setMiddleName(String middle_name){
		
		this.middle_name = middle_name;
	}
	
//////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the surname of this Contact
	 * @return surname: the surname of this Contact in the DB
	 */
	public String getSurame(){
		
		return surname;
	}
	
	/**
	 * Set the surname of this Contact
	 */
	public void setSurame(String surname){
		
		this.surname = surname;
	}
//////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the email of this Contact
	 * @return email: the email of this Contact in the DB
	 */
	public String getEmail(){
		
		return email;
	}
	
	/**
	 * Set the email of this Contact
	 */
	public void setEmail(String email){
		
		this.email = email;
	}
//////////////////////////////////////////////////////////////////////	
	
	/**
	 * Get the phone of this Contact
	 * @return phone: the phone of this Contact in the DB
	 */
	public String getPhone(){
		
		return phone;
	}
	
	/**
	 * Set the phone of this Contact
	 */
	public void setPhone(String phone){
		
		this.phone = phone;
	}
//////////////////////////////////////////////////////////////////////
	
	/**
	 * Get the address of this Contact
	 * @return address: the address of this Contact in the DB
	 */
	public String getAddress(){
		
		return address;
	}
	
	/**
	 * Set the address of this Contact
	 */
	public void setAddress(String address){
		
		this.address = address;
	}
//////////////////////////////////////////////////////////////////////
	
}
