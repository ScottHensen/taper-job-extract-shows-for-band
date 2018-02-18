package io.taper.batch.extract.shows4band;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import io.taper.util.ParmStringBuilder;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class GetShowsForBandFromHttp implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		String bandName = Arrays.stream(args).findFirst().get(); 
		String collection = "collection:" + bandName;
		//String showsForBand = null;
		
		log.info("App started with args=" + commandLineArgs);
		log.debug("Band Name=" + bandName);

		// Get request URL Parms
		//TODO: make fields & sorts configurable
		Map<String, String> parms = new HashMap<>();
		parms.put("q",collection); 
		parms.put("fields","addeddate,identifier,date,num_reviews,avg_rating,downloads,title,mediatype");
		parms.put("sorts","addeddate desc,identifier asc");

		URL url = buildUrl(parms);
		String showsForBand = getShowsForBand(url);
		writeToFile(bandName, showsForBand);
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
	
	private String getShowsForBand(URL url) throws IOException {
		log.debug("in url=" + url.toString());
		
		int httpRespCode = 9999;
		String httpResp  = null;
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(15000);
		con.setReadTimeout(15000);
		
		httpRespCode = con.getResponseCode();

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String inputLine = null;
		while ((inputLine = reader.readLine()) != null) {
			stringBuilder.append(inputLine);
		}
		
		httpResp = stringBuilder.toString();

		log.info("http responseCode=" + httpRespCode);
		log.debug("http response= \n" + httpResp);
		
		return httpResp;
	}

	private void writeToFile(String bandName, String showsForBand) throws IOException {
		log.debug("in bandName=" + bandName + ", showsForBand.len()=" + showsForBand.length());
		
		String filePath = "C:\\data\\files\\test\\";
		String fileName = filePath + bandName + ".json";

		Writer writer = new BufferedWriter( new FileWriter(fileName));
		writer.write(showsForBand);
		
		log.info("write to " + fileName + "completed.");
	}


}
