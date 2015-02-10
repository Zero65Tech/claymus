package com.claymus.commons.shared;

public enum ClaymusPageType implements PageType {

	GENERIC( "/page/" ),
	BLOG( "/blog/" ),
	FILEBROWSER( "/filebrowser/" );
	

	private String urlPrefix;
	
	
	private ClaymusPageType( String urlPrefix ) {
		this.urlPrefix = urlPrefix;
	}
	
	public String getUrlPrefix() {
		return this.urlPrefix;
	}
	
}
