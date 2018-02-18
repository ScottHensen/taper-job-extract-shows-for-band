package io.taper.batch.extract.shows4band;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.taper.util.HttpHelper;
import io.taper.util.ParmStringBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class ExtractShowsForBand implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		
		final String FILEPATH = "C:\\data\\files\\test\\";

		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		String bandName   = Arrays.stream(args).findFirst().get(); 
		String collection = "collection:" + bandName;
		String fileName   = FILEPATH + bandName + ".json";
		File   outFile    = new File(fileName);
		
		log.info("App started with args=" + commandLineArgs);
		log.debug("Band Name=" + bandName);

		//TODO: make fields & sorts configurable
		Map<String, String> urlParms = new HashMap<>();
		urlParms.put("q",collection); 
		urlParms.put("fields","addeddate,identifier,date,num_reviews,avg_rating,downloads,title,mediatype");
		urlParms.put("sorts","addeddate desc,identifier asc");

		Files.copy(
				HttpHelper.getStream( buildUrl(urlParms) ), 
				outFile.toPath(), 
				StandardCopyOption.REPLACE_EXISTING);
	}

	private URL buildUrl(Map<String, String> parms) throws UnsupportedEncodingException, MalformedURLException {
		log.debug("in parms=" + parms.toString());
		
		//example = "https://archive.org/services/search/v1/scrape?sorts=addeddate%20desc%2cidentifier%20asc&fields=addeddate%2cidentifier%2cdate%2cnum_reviews%2cavg_rating%2cdownloads%2ctitle%2cmediatype&q=collection%3ADeadAndCompany";
		//TODO: make this a property
		String urlBase   = "https://archive.org/services/search/v1/scrape?";
		String urlParms  = ParmStringBuilder.getParmString(parms);
		String urlString = urlBase + urlParms;
		
		URL url = new URL(urlString);
		log.info("url=" + urlString);
		
		return url;
	}
	
}
