package io.taper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class ShowFile {
	private String name;
	private String track;
	private String format;
	private String title;
	private String creator;
	private String album;
}