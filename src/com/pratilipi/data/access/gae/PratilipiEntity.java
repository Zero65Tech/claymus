package com.pratilipi.data.access.gae;

import java.util.Date;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.pratilipi.commons.shared.PratilipiType;
import com.pratilipi.data.transfer.Pratilipi;

@PersistenceCapable( table = "PRATILIPI" )
@Discriminator( column = "_TYPE", strategy = DiscriminatorStrategy.CLASS_NAME )
public abstract class PratilipiEntity implements Pratilipi {

	@PrimaryKey
	@Persistent( column = "PRATILIPI_ID", valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Long id;
	
	@Persistent( column = "PRATILIPI_TYPE" )
	private PratilipiType type;
	
	@Persistent( column = "PUBLIC_DOMAIN" )
	private Boolean isPublicDomain;
	
	@Persistent( column = "TITLE" )
	private String title;
	
	@Persistent( column = "LANGUAGE_ID" )
	private Long languageId;

	
	@Persistent( column = "AUTHOR_ID" )
	private Long authorId;
	
	@Persistent( column = "PUBLICATION_YEAR" )
	private Long publicationYear;

	@Persistent( column = "LISTING_DATE" )
	private Date listingDate;

	
	@Persistent( column = "SUMMARY" )
	private Text summary;

	@Persistent( column = "CONTENT" )
	private Text content;

	@Persistent( column = "WORD_COUNT" )
	private Long wordCount;

	@Persistent( column = "PAGE_COUNT" )
	private Long pageCount;
	
	@Persistent( column = "REVIEW_COUNT" )
	private Long reviewCount;
	
	@Persistent( column = "RATING_COUNT" )
	private Long ratingCount;
	
	@Persistent( column = "STAR_COUNT" )
	private Long starCount;

	@Persistent( column = "LAST_UPDATED" )
	private Date lastUpdated;

	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public PratilipiType getType() {
		return type;
	}
	
	@Override
	public void setType( PratilipiType pratilipiType ) {
		this.type = pratilipiType;
	}

	@Override
	public boolean isPublicDomain() {
		return isPublicDomain == null ? false : isPublicDomain;
	}
	
	@Override
	public void setPublicDomain( boolean isPublicDomain ) {
		this.isPublicDomain = isPublicDomain;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle( String title ) {
		this.title = title;
	}

	@Override
	public Long getLanguageId() {
		return languageId;
	}

	@Override
	public void setLanguageId( Long languageId ) {
		this.languageId = languageId;
	}

	@Override
	public Long getAuthorId() {
		return authorId;
	}

	@Override
	public void setAuthorId( Long authorId ) {
		this.authorId = authorId;
	}
	
	@Override
	public Long getPublicationYear() {
		return publicationYear;
	}

	@Override
	public void setPublicationYear( Long publicationYear ) {
		this.publicationYear = publicationYear;
	}

	@Override
	public Date getListingDate() {
		return listingDate;
	}

	@Override
	public void setListingDate( Date listingDate ) {
		this.listingDate = listingDate;
	}

	@Override
	public String getSummary() {
		return summary == null ? null : summary.getValue();
	}

	@Override
	public void setSummary( String summary ) {
		this.summary = summary == null ? null : new Text( summary );
	}
	
	@Override
	public String getContent() {
		return content == null ? null : content.getValue();
	}

	@Override
	public void setContent( String content ) {
		this.content = content == null ? null : new Text( content );
	}
	
	@Override
	public Long getWordCount() {
		return wordCount;
	}

	@Override
	public void setWordCount( Long wordCount ) {
		this.wordCount = wordCount;
	}
	
	@Override
	public Long getPageCount() {
		return pageCount;
	}

	@Override
	public void setPageCount( Long pageCount ) {
		this.pageCount = pageCount;
	}
	
	@Override
	public Long getReviewCount() {
		return reviewCount;
	}

	@Override
	public void setReviewCount( Long reviewCount ) {
		this.reviewCount = reviewCount;
	}

	@Override
	public Long getRatingCount() {
		return ratingCount;
	}

	@Override
	public void setRatingCount( Long ratingCount ) {
		this.ratingCount = ratingCount;
	}
	
	@Override
	public Long getStarCount() {
		return starCount;
	}

	@Override
	public void setStarCount( Long starCount ) {
		this.starCount = starCount;
	}
	
	@Override
	public Date getLastUpdated() {
		return lastUpdated;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.lastUpdated = lastUpdated;
	}

}
