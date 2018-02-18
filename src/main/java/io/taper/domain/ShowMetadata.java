package io.taper.domain;

import lombok.Data;

@Data
public class ShowMetadata {

	private String date;
	private String identifier;
	private String addeddate;
	private int    downloads;
	private String avg_rating;
	private int    num_reviews;
	private String title;
	private String mediatype;
	
}
