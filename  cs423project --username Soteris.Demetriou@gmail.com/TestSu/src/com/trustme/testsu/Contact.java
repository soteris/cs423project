package com.trustme.testsu;

public class Contact {
	private long id;
	private String name;
	private String middle_name;
	private String surname;
	private String email;
	private String phone; //use list of phones
	private String address;
	
	
	public long getId() {
		return id;
	}

    public void setId(long id) {
		this.id = id;
	}
	
	public String getName(){
		
		return name;
	}
	
	public void setName(String name){
		
		this.name = name;
	}
	
//////////////////////////////////////////////////////////////////////	
	public String getMiddleName(){
		
		return middle_name;
	}
	
	public void setMiddleName(String middle_name){
		
		this.middle_name = middle_name;
	}
	
//////////////////////////////////////////////////////////////////////
	
	public String getSurame(){
		
		return surname;
	}
	
	public void setSurame(String surname){
		
		this.surname = surname;
	}
//////////////////////////////////////////////////////////////////////
	
	public String getEmail(){
		
		return email;
	}
	
	public void setEmail(String email){
		
		this.email = email;
	}
//////////////////////////////////////////////////////////////////////	
	
	public String getPhone(){
		
		return phone;
	}
	
	public void setPhone(String phone){
		
		this.phone = phone;
	}
//////////////////////////////////////////////////////////////////////
	
	public String getAddress(){
		
		return address;
	}
	
	public void setAddress(String address){
		
		this.address = address;
	}
//////////////////////////////////////////////////////////////////////
	
}
