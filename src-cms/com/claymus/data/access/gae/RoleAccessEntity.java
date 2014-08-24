package com.claymus.data.access.gae;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.claymus.data.transfer.RoleAccess;

@PersistenceCapable( table = "ROLE_ACCESS" )
public class RoleAccessEntity implements RoleAccess {
	
	@PrimaryKey
	@Persistent( column = "ROLE_ACCESS_ID" )
	private String id;
	
	@Persistent( column = "ROLE_ID" )
	private Long roleId;
	
	@Persistent( column = "ACCESS_ID" )
	private String accessId;
	
	
	@Override
	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	@Override
	public Long getRoleId() {
		return roleId;
	}

	@Override
	public void setRoleId( Long roleId ) {
		this.roleId = roleId;
	}

	@Override
	public String getAccessId() {
		return accessId;
	}

	@Override
	public void setAccessId( String accessId ) {
		this.accessId = accessId;
	}

}