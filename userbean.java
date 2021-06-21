package com.xadmin.NoteTaker.bean;

public class userbean
{
      private String name;
      private int id;
      private String email;
      private String country;
      
      public userbean(String name, int id, String email, String country) {
  		super();
  		this.name = name;
  		this.id = id;
  		this.email = email;
  		this.country = country;
  	}
      
	public userbean(String name, String email, String country) {
		super();
		this.name = name;
		this.email = email;
		this.country = country;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
