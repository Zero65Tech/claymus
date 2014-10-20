package com.pratilipi.pagecontent.pratilipi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.claymus.commons.server.ClaymusHelper;
import com.claymus.commons.server.SerializationUtil;
import com.claymus.commons.shared.exception.UnexpectedServerException;
import com.claymus.data.transfer.User;
import com.claymus.pagecontent.PageContentProcessor;
import com.claymus.service.shared.data.UserData;
import com.pratilipi.commons.server.PratilipiHelper;
import com.pratilipi.commons.shared.UserReviewState;
import com.pratilipi.data.access.DataAccessor;
import com.pratilipi.data.access.DataAccessorFactory;
import com.pratilipi.data.transfer.Pratilipi;
import com.pratilipi.data.transfer.UserPratilipi;
import com.pratilipi.service.shared.data.PratilipiData;

public class PratilipiContentProcessor extends PageContentProcessor<PratilipiContent> {

	@Override
	public String generateHtml( PratilipiContent pratilipiContent, HttpServletRequest request )
			throws UnexpectedServerException {

		Long pratilipiId = pratilipiContent.getPratilipiId();
		PratilipiHelper pratilipiHelper = PratilipiHelper.get( request );
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Pratilipi pratilipi = dataAccessor.getPratilipi( pratilipiId );
		
		UserPratilipi userPratilipi = null;
		if( pratilipiHelper.isUserLoggedIn() )
			userPratilipi = dataAccessor.getUserPratilipi( pratilipiHelper.getCurrentUserId(), pratilipiId );
		List<UserPratilipi> reviewList = dataAccessor.getUserPratilipiList( pratilipiId );
		
		Map<String, String> userIdNameMap = new HashMap<>();
		for( UserPratilipi review : reviewList ) {
			User user = dataAccessor.getUser( review.getUserId() );
			UserData userData = pratilipiHelper.createUserData( user );
			userIdNameMap.put( userData.getId().toString(), userData.getName() );
		}

		dataAccessor.destroy();
		
		
		PratilipiData pratilipiData = pratilipiHelper.createPratilipiData(
				pratilipiId,
				PratilipiContentHelper.hasRequestAccessToReadMetaData( request ) );
		
		boolean showEditOption = PratilipiContentHelper
				.hasRequestAccessToUpdateData( request, pratilipi );


		// Creating data model required for template processing
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put( "timeZone", pratilipiHelper.getCurrentUserTimeZone() );
		dataModel.put( "userData", pratilipiHelper.createUserData( pratilipiHelper.getCurrentUser() ) );
		dataModel.put( "pratilipiData", pratilipiData );
		dataModel.put( "pratilipiDataEncodedStr", SerializationUtil.encode( pratilipiData ) );
		dataModel.put( "reviewList", reviewList );
		dataModel.put( "userIdNameMap", userIdNameMap );
		dataModel.put( "domain", ClaymusHelper.getSystemProperty( "domain" ) );
		dataModel.put( "showEditOptions", showEditOption );
		dataModel.put( "showReviewedMessage",
				userPratilipi != null
				&& userPratilipi.getReviewState() != UserReviewState.NOT_SUBMITTED );
		dataModel.put( "showReviewOption",
				( userPratilipi == null || userPratilipi.getReviewState() == UserReviewState.NOT_SUBMITTED )
				&& PratilipiContentHelper.hasRequestAccessToAddReview( request, pratilipi ) );


		return super.processTemplate( dataModel, getTemplateName() );
	}
	
}
