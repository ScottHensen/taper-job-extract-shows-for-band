package io.taper.batch.extract.shows4band;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.taper.util.ParmStringBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication 
@Slf4j
public class Application {

	
	public static void main(String[] args) {
		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		log.info("App started with args=" + commandLineArgs);
		SpringApplication.run(Application.class, args);
	}
	
//	@Override
//	public void run(String... args) throws Exception {
//		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
//		String bandName = Arrays.stream(args).findFirst().get(); 
//		String collection = "collection:" + bandName;
//		
//		log.info("App started with args=" + commandLineArgs);
//		log.debug("Band Name=" + bandName);
//
//		// Get request URL Parms
//		//TODO: make fields & sorts configurable
//		Map<String, String> parms = new HashMap<>();
//		parms.put("q",collection); 
//		parms.put("fields","addeddate,identifier,date,num_reviews,avg_rating,downloads,title,mediatype");
//		parms.put("sorts","addeddate desc,identifier asc");
//
//		// build url. example = "https://archive.org/services/search/v1/scrape?sorts=addeddate%20desc%2cidentifier%20asc&fields=addeddate%2cidentifier%2cdate%2cnum_reviews%2cavg_rating%2cdownloads%2ctitle%2cmediatype&q=collection%3ADeadAndCompany";
//		String urlBase = "https://archive.org/services/search/v1/scrape?";
//		String urlParms = ParmStringBuilder.getParmString(parms);
//		String urlString = urlBase + urlParms;
//		URL url = new URL(urlString);
//		log.info("url=" + urlString);
//		
//		// build the http connection
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setRequestMethod("GET");
//		con.setDoOutput(true);
//		con.setRequestProperty("Content-Type", "application/json");
//		con.setConnectTimeout(15000);
//		con.setReadTimeout(15000);
//		
//		// make http request
//		int httpRespCode = con.getResponseCode();
//		log.info("http responseCode=" + httpRespCode);
//
//		// read http response into string
//		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		StringBuilder stringBuilder = new StringBuilder();
//		String inputLine = null;
//		while ((inputLine = reader.readLine()) != null) {
//			stringBuilder.append(inputLine);
//		}
//		String httpResp = stringBuilder.toString();
//		log.debug("http response= \n" + httpResp);
//		
//		// write http response to file
//		
//	}
}
