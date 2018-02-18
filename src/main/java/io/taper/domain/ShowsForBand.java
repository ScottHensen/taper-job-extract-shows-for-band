package io.taper.domain;

import java.util.List;

import lombok.Data;

@Data
public class ShowsForBand {

	private List<ShowMetadata> items;
	private int count;
	private int total;
	
}
