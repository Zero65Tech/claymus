package com.claymus.data.transfer.shared;

import java.util.Date;

import com.claymus.commons.shared.UserStatus;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.pratilipi.common.type.Gender;

public class UserData implements IsSerializable {
	
	private Long id;
	
	private String password;
	private boolean hasPassword;
	
	private String firstName;
	private boolean hasFirstName;
	
	private String lastName;
	private boolean hasLastName;
	
	private String name;
	private boolean hasName;

	private String email;
	private boolean hasEmail;
	
	private Date dateOfBirth;
	private boolean hasDateOfBirth;
	
	private Gender gender;
	private boolean hasGender;
	
	private String campaign;
	
	private String referer;
	
	private UserStatus status;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.hasPassword = true;
	}
	
	public boolean hasPassword(){
		return hasPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.hasFirstName = true;
	}
	
	public boolean hasFirstName(){
		return hasFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.hasLastName = true;
	}
	
	public boolean hasLastName(){ 
		return hasLastName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName( String name ){
		this.name = name;
		this.hasName = true;
	}
	
	public boolean hasName(){
		return hasName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		this.hasEmail = true;
	}
	
	public boolean hasEmail(){
		return hasEmail;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		this.hasDateOfBirth = true;
	}
	
	public boolean hasDateOfBirth(){
		return hasDateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
		this.hasGender = true;
	}
	
	public boolean hasGender(){
		return hasGender;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	
	

}
