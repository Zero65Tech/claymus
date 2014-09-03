package com.pratilipi.service.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.claymus.commons.client.IllegalArgumentException;
import com.claymus.commons.client.InsufficientAccessException;
import com.claymus.commons.server.ClaymusHelper;
import com.claymus.data.access.DataListCursorTuple;
import com.claymus.data.transfer.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pratilipi.commons.shared.UserReviewState;
import com.pratilipi.data.access.DataAccessor;
import com.pratilipi.data.access.DataAccessorFactory;
import com.pratilipi.data.transfer.Author;
import com.pratilipi.data.transfer.Book;
import com.pratilipi.data.transfer.Language;
import com.pratilipi.data.transfer.Publisher;
import com.pratilipi.data.transfer.UserBook;
import com.pratilipi.pagecontent.authors.AuthorsContentProcessor;
import com.pratilipi.pagecontent.book.BookContentProcessor;
import com.pratilipi.pagecontent.languages.LanguagesContentProcessor;
import com.pratilipi.service.client.PratilipiService;
import com.pratilipi.service.shared.AddAuthorRequest;
import com.pratilipi.service.shared.AddAuthorResponse;
import com.pratilipi.service.shared.AddBookRequest;
import com.pratilipi.service.shared.AddBookResponse;
import com.pratilipi.service.shared.AddLanguageRequest;
import com.pratilipi.service.shared.AddLanguageResponse;
import com.pratilipi.service.shared.AddPublisherRequest;
import com.pratilipi.service.shared.AddPublisherResponse;
import com.pratilipi.service.shared.AddUserBookRequest;
import com.pratilipi.service.shared.AddUserBookResponse;
import com.pratilipi.service.shared.GetAuthorListRequest;
import com.pratilipi.service.shared.GetAuthorListResponse;
import com.pratilipi.service.shared.GetBookListRequest;
import com.pratilipi.service.shared.GetBookListResponse;
import com.pratilipi.service.shared.GetBookRequest;
import com.pratilipi.service.shared.GetBookResponse;
import com.pratilipi.service.shared.GetLanguageListRequest;
import com.pratilipi.service.shared.GetLanguageListResponse;
import com.pratilipi.service.shared.GetPublisherListRequest;
import com.pratilipi.service.shared.GetPublisherListResponse;
import com.pratilipi.service.shared.GetUserBookListRequest;
import com.pratilipi.service.shared.GetUserBookListResponse;
import com.pratilipi.service.shared.GetUserBookRequest;
import com.pratilipi.service.shared.GetUserBookResponse;
import com.pratilipi.service.shared.UpdateBookRequest;
import com.pratilipi.service.shared.UpdateBookResponse;
import com.pratilipi.service.shared.data.AuthorData;
import com.pratilipi.service.shared.data.BookData;
import com.pratilipi.service.shared.data.LanguageData;
import com.pratilipi.service.shared.data.PublisherData;
import com.pratilipi.service.shared.data.UserBookData;

@SuppressWarnings("serial")
public class PratilipiServiceImpl
		extends RemoteServiceServlet
		implements PratilipiService {

	@Override
	public AddBookResponse addBook( AddBookRequest request )
			throws IllegalArgumentException, InsufficientAccessException {
		
		if( ! ClaymusHelper.isUserAdmin() )
			throw new InsufficientAccessException();
		
		BookData bookData = request.getBook();
		if( bookData.hasId() )
			throw new IllegalArgumentException(
					"BookId exist already. Did you mean to call updateBook ?" );
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Book book = dataAccessor.newBook();
		book.setTitle( bookData.getTitle() );
		book.setLanguageId( bookData.getLanguageId() );
		book.setAuthorId( bookData.getAuthorId() );
		book.setPublisherId( bookData.getPublisherId() );
		book.setPublicationYear( bookData.getPublicationYear() );
		book.setSummary( bookData.getSummary() );
		book.setWordCount( bookData.getWordCount() );
		
		book.setListingDate( new Date() );

		book = dataAccessor.createOrUpdateBook( book );
		dataAccessor.destroy();
		
		return new AddBookResponse( book.getId() );
	}

	@Override
	public UpdateBookResponse updateBook( UpdateBookRequest request )
			throws IllegalArgumentException, InsufficientAccessException {

		BookData bookData = request.getBook();
		if( ! bookData.hasId() )
			throw new IllegalArgumentException(
					"BookId is not set. Did you mean to call addBook ?" );

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Book book = dataAccessor.getBook( bookData.getId() );
		
		ClaymusHelper claymusHelper = new ClaymusHelper( this.getThreadLocalRequest() );
		
		if ( ( claymusHelper.getCurrentUserId() == book.getAuthorId()
				&& ! claymusHelper.hasUserAccess( BookContentProcessor.ACCESS_ID_BOOK_ADD, false ) )
				|| ! claymusHelper.hasUserAccess( BookContentProcessor.ACCESS_ID_BOOK_UPDATE, false ) )
			throw new InsufficientAccessException();
		
		if( bookData.hasTitle() )
			book.setTitle( bookData.getTitle() );
		if( bookData.hasLanguageId() )
			book.setLanguageId( bookData.getLanguageId() );
		if( bookData.hasAuthorId() )
			book.setAuthorId( bookData.getAuthorId() );
		if( bookData.hasPublisherId() )
			book.setPublisherId( bookData.getPublisherId() );
		if( bookData.hasPublicationYear() )
			book.setPublicationYear( bookData.getPublicationYear() );
		if( bookData.hasSummary() )
			book.setSummary( bookData.getSummary() );
		if( bookData.hasWordCount() )
			book.setWordCount( bookData.getWordCount() );
		
		book = dataAccessor.createOrUpdateBook( book );
		dataAccessor.destroy();

		return new UpdateBookResponse();
	}
	
	@Override
	public GetBookListResponse getBookList( GetBookListRequest request ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Book> bookList = dataAccessor.getBookList();
		
		ArrayList<BookData> bookDataList = new ArrayList<>( bookList.size() );
		for( Book book : bookList ) {
			Language language = dataAccessor.getLanguage( book.getLanguageId() );
			Author author = dataAccessor.getAuthor( book.getAuthorId() );
			Publisher publisher = dataAccessor.getPublisher( book.getPublisherId() );
			
			BookData bookData = new BookData();
			bookData.setId( book.getId() );
			bookData.setTitle( book.getTitle() );
			bookData.setLanguageId( language.getId() );
			bookData.setLanguageName( language.getName() );
			bookData.setAuthorId( author.getId() );
			bookData.setAuthorName( author.getFirstName() + " " + author.getLastName() );
			bookData.setPublisherId( publisher.getId() );
			bookData.setPublisherName( publisher.getName() );
			bookData.setPublicationDate( book.getPublicationYear() );
			bookData.setListingDate( book.getListingDate() );
			bookData.setSummary( book.getSummary() );
			bookData.setWordCount( book.getWordCount() );
			
			bookDataList.add( bookData );
		}

		dataAccessor.destroy();
		
		return new GetBookListResponse( bookDataList );
	}
	
	@Override
	public GetBookResponse getBookById( GetBookRequest request ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Book book = dataAccessor.getBook(request.getBookId());
		
		Language language = dataAccessor.getLanguage( book.getLanguageId() );
		Author author = dataAccessor.getAuthor( book.getAuthorId() );
		Publisher publisher = dataAccessor.getPublisher( book.getPublisherId() );
		
		BookData bookData = new BookData();
		bookData.setId( book.getId() );
		bookData.setTitle( book.getTitle() );
		bookData.setLanguageId( language.getId() );
		bookData.setLanguageName( language.getName() );
		bookData.setAuthorId( author.getId() );
		bookData.setAuthorName( author.getFirstName() + " " + author.getLastName() );
		bookData.setPublisherId( publisher.getId() );
		bookData.setPublisherName( publisher.getName() );
		bookData.setPublicationDate( book.getPublicationYear() );
		bookData.setListingDate( book.getListingDate() );
		bookData.setSummary( book.getSummary() );
		bookData.setWordCount( book.getWordCount() );
		
		dataAccessor.destroy();

		return new GetBookResponse(bookData);
	}

	
	@Override
	public AddLanguageResponse addLanguage( AddLanguageRequest request )
			throws IllegalArgumentException, InsufficientAccessException {
		
		LanguageData languageData = request.getLanguage();
		if( languageData.getId() != null )
			throw new IllegalArgumentException(
					"LanguageId exist already. Did you mean to call updateLanguage ?" );

		
		ClaymusHelper claymusHelper =
				new ClaymusHelper( this.getThreadLocalRequest() );
		
		if( ! claymusHelper.hasUserAccess( LanguagesContentProcessor.ACCESS_ID_LANGUAGE_ADD, false ) )
			throw new InsufficientAccessException();
		
		
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Language language = dataAccessor.newLanguage();
		language.setName( languageData.getName() );
		language.setNameEn( languageData.getNameEn() );
		language.setCreationDate( new Date() );
		language = dataAccessor.createOrUpdateLanguage( language );
		dataAccessor.destroy();
		
		return new AddLanguageResponse( language.getId() );
	}

	@Override
	public GetLanguageListResponse getLanguageList(
			GetLanguageListRequest request ) throws InsufficientAccessException {
		
		ClaymusHelper claymusHelper =
				new ClaymusHelper( this.getThreadLocalRequest() );
		
		if( ! claymusHelper.hasUserAccess( LanguagesContentProcessor.ACCESS_ID_LANGUAGE_LIST, false ) )
			throw new InsufficientAccessException();

		boolean sendMetaData = claymusHelper.hasUserAccess(
				LanguagesContentProcessor.ACCESS_ID_LANGUAGE_READ_META_DATA, false );
		
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Language> languageList = dataAccessor.getLanguageList();
		dataAccessor.destroy();
		
		
		ArrayList<LanguageData> languageDataList = new ArrayList<>( languageList.size() );
		for( Language language : languageList ) {
			LanguageData languageData = new LanguageData();
			languageData.setId( language.getId() );
			languageData.setName( language.getName() );
			languageData.setNameEn( language.getNameEn() );
			if( sendMetaData )
				languageData.setCreationDate( language.getCreationDate() );
			
			languageDataList.add( languageData );
		}

		
		return new GetLanguageListResponse( languageDataList );
	}


	@Override
	public AddAuthorResponse addAuthor( AddAuthorRequest request )
			throws IllegalArgumentException, InsufficientAccessException {
		
		AuthorData authorData = request.getAuthor();
		if( authorData.getId() != null )
			throw new IllegalArgumentException(
					"AuthorId exist already. Did you mean to call updateAuthor ?" );
		
		
		ClaymusHelper claymusHelper =
				new ClaymusHelper( this.getThreadLocalRequest() );
		
		if( ! claymusHelper.hasUserAccess( AuthorsContentProcessor.ACCESS_ID_AUTHOR_ADD, false ) )
			throw new InsufficientAccessException();

		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		Author author = dataAccessor.newAuthor();
		author.setLanguageId( authorData.getLanguageId() );
		author.setFirstName( authorData.getFirstName() );
		author.setLastName( authorData.getLastName() );
		author.setPenName( authorData.getPenName() );
		author.setFirstNameEn( authorData.getFirstNameEn() );
		author.setLastNameEn( authorData.getLastNameEn() );
		author.setPenNameEn( authorData.getPenNameEn() );
		author.setEmail( authorData.getEmail().toLowerCase() );
		author.setRegistrationDate( new Date() );
		author = dataAccessor.createOrUpdateAuthor( author );
		dataAccessor.destroy();
		
		return new AddAuthorResponse( author.getId() );
	}

	@Override
	public GetAuthorListResponse getAuthorList( GetAuthorListRequest request )
			throws InsufficientAccessException {
		
		ClaymusHelper claymusHelper =
				new ClaymusHelper( this.getThreadLocalRequest() );
		
		if( ! claymusHelper.hasUserAccess( AuthorsContentProcessor.ACCESS_ID_AUTHOR_LIST, false ) )
			throw new InsufficientAccessException();

		boolean sendMetaData = claymusHelper.hasUserAccess(
				AuthorsContentProcessor.ACCESS_ID_AUTHOR_READ_META_DATA, false );

		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		DataListCursorTuple<Author> authorListCursorTuple =
				dataAccessor.getAuthorList( request.getCursor(), request.getResultCount() );
		List<Author> authorList = authorListCursorTuple.getDataList();
		String cursor = authorListCursorTuple.getCursor();
		
		ArrayList<AuthorData> authorDataList = new ArrayList<>( authorList.size() );
		for( Author author : authorList ) {
			Language language = dataAccessor.getLanguage( author.getLanguageId() );

			AuthorData authorData = new AuthorData();
			authorData.setId( author.getId() );
			authorData.setLanguageId( language.getId() );
			authorData.setLanguageName( language.getName() );
			authorData.setFirstName( author.getFirstName() );
			authorData.setLastName( author.getLastName() );
			authorData.setPenName( author.getPenName() );
			authorData.setFirstNameEn( author.getFirstNameEn() );
			authorData.setLastNameEn( author.getLastNameEn() );
			authorData.setPenNameEn( author.getPenNameEn() );
			authorData.setEmail( author.getEmail() );
			if( sendMetaData )
				authorData.setRegistrationDate( author.getRegistrationDate() );
			
			authorDataList.add( authorData );
		}

		dataAccessor.destroy();

		
		return new GetAuthorListResponse( authorDataList, cursor );
	}

	
	@Override
	public AddPublisherResponse addPublisher(AddPublisherRequest request)
			throws InsufficientAccessException {
		
		if( ! ClaymusHelper.isUserAdmin() )
			throw new InsufficientAccessException();
		
		PublisherData publisherData = request.getPublisher();
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Publisher publisher = dataAccessor.newPublisher();
		publisher.setName( publisherData.getName() );
		publisher.setEmail( publisherData.getEmail() );
		publisher.setRegistrationDate( new Date() );
		
		publisher = dataAccessor.createOrUpdatePublisher( publisher );
		dataAccessor.destroy();
		
		return new AddPublisherResponse( publisher.getId() );
	}

	@Override
	public GetPublisherListResponse getPublisherList(
			GetPublisherListRequest request) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<Publisher> publisherList = dataAccessor.getPublisherList();
		
		ArrayList<PublisherData> publisherDataList = new ArrayList<>( publisherList.size() );
		for( Publisher publisher : publisherList ) {
			PublisherData publisherData = new PublisherData();
			publisherData.setId( publisher.getId() );
			publisherData.setName( publisher.getName() );
			publisherData.setEmail( publisher.getEmail() );
			publisherData.setRegistrationDate( publisher.getRegistrationDate() );
			
			publisherDataList.add( publisherData );
		}

		dataAccessor.destroy();
		
		return new GetPublisherListResponse( publisherDataList );
	}

	
	@Override
	public AddUserBookResponse addUserBook( AddUserBookRequest request )
			throws IllegalArgumentException, InsufficientAccessException {
		
		UserBookData userBookData = request.getUserBook();

		ClaymusHelper claymusHelper =
				new ClaymusHelper( this.getThreadLocalRequest() );

		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		
		Book book = dataAccessor.getBook( userBookData.getBookId() );
		UserBook userBook = dataAccessor.getUserBook(
				claymusHelper.getCurrentUserId(), book.getId() );
		
		if( claymusHelper.getCurrentUserId() == book.getAuthorId()
				|| ( userBook != null && userBook.getReviewState() != UserReviewState.NOT_SUBMITTED )
				|| ! claymusHelper.hasUserAccess( BookContentProcessor.ACCESS_ID_BOOK_REVIEW_ADD, false ) )
			throw new InsufficientAccessException();

		userBook = dataAccessor.newUserBook();
		userBook.setUserId( claymusHelper.getCurrentUserId() );
		userBook.setBookId( book.getId() );
		userBook.setRating( userBookData.getRating() );
		userBook.setReview( userBookData.getReview() );
		userBook.setReviewState( UserReviewState.PENDING_APPROVAL );
		userBook.setReviewDate( new Date() );

		userBook = dataAccessor.createOrUpdateUserBook( userBook );
		dataAccessor.destroy();
		
		return new AddUserBookResponse( userBook.getId() );
	}
	
	@Override
	public GetUserBookResponse getUserBook( GetUserBookRequest request ) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		UserBook userBook = dataAccessor.getUserBook(
				request.getUserId(),
				request.getBookId());
		
		User user = dataAccessor.getUser( userBook.getUserId() );
		
		UserBookData userBookData = new UserBookData();
		userBookData.setId( userBook.getId() );
		userBookData.setUserId( user.getId() );
		userBookData.setUserName( user.getFirstName() + " " + user.getLastName() );
		userBookData.setBookId( userBook.getBookId() );
		userBookData.setRating( userBook.getRating() );
		userBookData.setReview( userBook.getReview() );
		userBookData.setReviewState( userBook.getReviewState() );
		userBookData.setReviewDate( userBook.getReviewDate() );
		
		return new GetUserBookResponse( userBookData );
	}

	@Override
	public GetUserBookListResponse getUserBookList(GetUserBookListRequest request) {
		
		DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
		List<UserBook> userBookList = dataAccessor.getUserBookList( request.getBookId() );
		
		ArrayList<UserBookData> userBookDataList = new ArrayList<>( userBookList.size() );
		for( UserBook userBook : userBookList ) {
			User user = dataAccessor.getUser( request.getUserId() );
			
			UserBookData userBookData = new UserBookData();
			userBookData.setBookId(userBook.getBookId());
			userBookData.setUserId(userBook.getUserId());
			userBookData.setRating(userBook.getRating());
			userBookData.setReview(userBook.getReview());
			userBookData.setReviewState(userBook.getReviewState());
			userBookData.setReviewDate(userBook.getReviewDate());
			userBookData.setUserName( user.getFirstName() + " " + user.getLastName() );
			userBookData.setId(userBook.getUserId()+"-"+userBook.getBookId());
			
			userBookDataList.add( userBookData );
			
		}

		dataAccessor.destroy();
		
		return new GetUserBookListResponse( userBookDataList );
	}

}
