package com.pratilipi.pagecontent.pratilipis.client;

import com.claymus.commons.client.ui.Accordion;
import com.claymus.commons.client.ui.InfiniteScrollPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.pratilipi.commons.client.PratilipiDataInputView;
import com.pratilipi.commons.client.PratilipiDataInputViewImpl;
import com.pratilipi.commons.client.PratilipiView;
import com.pratilipi.commons.client.PratilipiViewBookDetailImpl;
import com.pratilipi.commons.client.PratilipiViewPoemDetailImpl;
import com.pratilipi.commons.shared.PratilipiType;
import com.pratilipi.service.client.PratilipiService;
import com.pratilipi.service.client.PratilipiServiceAsync;
import com.pratilipi.service.shared.AddPratilipiRequest;
import com.pratilipi.service.shared.AddPratilipiResponse;
import com.pratilipi.service.shared.GetAuthorListRequest;
import com.pratilipi.service.shared.GetAuthorListResponse;
import com.pratilipi.service.shared.GetLanguageListRequest;
import com.pratilipi.service.shared.GetLanguageListResponse;
import com.pratilipi.service.shared.GetPratilipiListRequest;
import com.pratilipi.service.shared.GetPratilipiListResponse;
import com.pratilipi.service.shared.data.AuthorData;
import com.pratilipi.service.shared.data.LanguageData;
import com.pratilipi.service.shared.data.PratilipiData;

public class PratilipisContent implements EntryPoint, ClickHandler {

	private static final PratilipiServiceAsync pratilipiService =
			GWT.create( PratilipiService.class );
	

	private final Accordion accordion = new Accordion();
	private PratilipiDataInputView pratilipiDataInputView;
	
	private PratilipiType pratilipiType;
	
	
	public void onModuleLoad() {

		for( PratilipiType pratilipiType : PratilipiType.values() )
			if( RootPanel.get( "PageContent-" + pratilipiType.getName() + "-DataInput" ) != null )
				this.pratilipiType = pratilipiType;
		
		if( pratilipiType == null )
			return;
		
		if( RootPanel.get( "PageContent-" + pratilipiType.getName() + "-DataInput" ) != null ) {
		
			accordion.setTitle( "Add " + pratilipiType.getName() );
			pratilipiDataInputView = new PratilipiDataInputViewImpl( pratilipiType );
			pratilipiDataInputView.addAddButtonClickHandler( this );

			accordion.add( pratilipiDataInputView );
			RootPanel.get( "PageContent-" + pratilipiType.getName() + "-DataInput" ).add( accordion );
			
			
			// Load list of authors.
			pratilipiService.getAuthorList( new GetAuthorListRequest( null , 100 ), new AsyncCallback<GetAuthorListResponse>() {

				@Override
				public void onFailure( Throwable caught ) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess( GetAuthorListResponse response ) {
					for( AuthorData authorData : response.getAuthorList() )
						pratilipiDataInputView.setAuthorList( authorData );
				}
				
			});
			
			// Load list of languages
		    pratilipiService.getLanguageList( new GetLanguageListRequest(), new AsyncCallback<GetLanguageListResponse>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
					
				}

				@Override
				public void onSuccess( GetLanguageListResponse response ) {
					for( LanguageData languageData : response.getLanguageList())
						pratilipiDataInputView.setLanguageList( languageData );
				}
				
		    });
		
		}

		RootPanel.get( "PageContent-" + pratilipiType.getName() + "-List" ).add( new InfiniteScrollPanel() {

			@Override
			protected void loadItems() {
				
				pratilipiService.getPratilipiList( new GetPratilipiListRequest( pratilipiType ), new AsyncCallback<GetPratilipiListResponse>() {
					
					@Override
					public void onSuccess( GetPratilipiListResponse response ) {

						for( PratilipiData pratilipiData : response.getPratilipiDataList() ) {
							PratilipiView pratilipiView = null;
							if( pratilipiType == PratilipiType.BOOK )
								pratilipiView = new PratilipiViewBookDetailImpl();
							else if( pratilipiType == PratilipiType.POEM )
								pratilipiView = new PratilipiViewPoemDetailImpl();
							pratilipiView.setPratilipiData( pratilipiData );
							add( pratilipiView );
						}
						
						loadSuccessful();
					}
					
					@Override
					public void onFailure( Throwable caught ) {
						loadFailed();
						// TODO Auto-generated method stub
						Window.alert( caught.getMessage() );
					}
					
				} );
				
			}
			
		} );
	
	}
	
	
	@Override
	public void onClick( ClickEvent event ) {
		if( ! pratilipiDataInputView.validateInputs() )
			return;
		
		pratilipiDataInputView.setEnabled( false );
		PratilipiData pratilipiData = pratilipiDataInputView.getPratilipiData();
		AddPratilipiRequest request = new AddPratilipiRequest( pratilipiData );
		pratilipiService.addPratilipi( request, new AsyncCallback<AddPratilipiResponse>(){

			@Override
			public void onFailure(Throwable caught) {
				pratilipiDataInputView.setEnabled( true );
				Window.alert( caught.getMessage() );
			}

			@Override
			public void onSuccess( AddPratilipiResponse result ) {
				Window.Location.reload();				
			}
			
		});

	}
	
}