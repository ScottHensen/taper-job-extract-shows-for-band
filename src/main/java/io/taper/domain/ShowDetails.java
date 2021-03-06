package io.taper.domain;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShowDetails {
	
	private String avg_rating;
	private int    num_reviews;
	private int    downloads;
	private ShowDetailsMetadata metadata;
	private ArrayList<ShowFile> files;

}
