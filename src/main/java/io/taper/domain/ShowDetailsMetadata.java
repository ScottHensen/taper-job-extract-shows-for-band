package io.taper.domain;

import java.util.List;

import lombok.Data;

@Data
public class ShowDetailsMetadata {

	public ShowDetailsMetadata(ShowMetadata show) {
		this.identifier = show.getIdentifier();
		this.addeddate = show.getAddeddate();
		this.date = show.getDate();
		this.title = show.getTitle();
		this.mediatype = show.getMediatype();
	}
	
	private String identifier;
	private String uploader;
	private String addeddate;
	private String date;
	private String title;
	private String creator;
	private String mediatype;
	private List<String> collection;
	private String type;
	private String description;
	private String year;
	private String subject;
	private String venue;
	private String coverage;
	private String source;
	private String taper;
	private String transferer;
	private String md5s;
	private String notes;
	private String publicdate;
	private String backup_location;
}
