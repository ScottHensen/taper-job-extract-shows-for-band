package io.taper.domain;

import java.util.List;

import lombok.Data;

@Data
public class ShowsForBand {

	private List<ShowOverview> items;
	private int count;
	private int total;
	
}
