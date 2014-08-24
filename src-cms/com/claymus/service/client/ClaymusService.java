package com.claymus.service.client;

import com.claymus.commons.client.IllegalArgumentException;
import com.claymus.service.shared.AddUserRequest;
import com.claymus.service.shared.AddUserResponse;
import com.claymus.service.shared.LoginUserRequest;
import com.claymus.service.shared.LoginUserResponse;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../service.claymus")
public interface ClaymusService extends RemoteService {
	
	AddUserResponse addUser( AddUserRequest request );

	LoginUserResponse loginUser( LoginUserRequest request )
			throws IllegalArgumentException;
	
}
