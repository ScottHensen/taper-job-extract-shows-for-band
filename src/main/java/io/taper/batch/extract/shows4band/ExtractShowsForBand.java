package io.taper.batch.extract.shows4band;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.taper.config.IOConfig;
import io.taper.util.HttpHelper;
import io.taper.util.ParmStringBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@ComponentScan("io.taper.config")
@Slf4j
public class ExtractShowsForBand implements CommandLineRunner {

	@Autowired
	private IOConfig ioConfig;
	
	@Override
	public void run(String... args) throws Exception {
		
		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		String bandName   = Arrays.stream(args).findFirst().get(); 
		String fileName   = ioConfig.getPath() + bandName + ".json";
		File   outFile    = new File(fileName);
		
		log.info("App started with args =" + commandLineArgs);
		log.debug("Write to   =" + fileName);
		log.debug("url fields =" + ioConfig.getScrapeUrlFields());
		log.debug("url sorts  =" + ioConfig.getScrapeUrlSorts());

		Map<String, String> urlParms = new HashMap<>();
		urlParms.put("q",      ioConfig.getScrapeUrlQueryKey() + bandName); 
		urlParms.put("fields", ioConfig.getScrapeUrlFields()             );
		urlParms.put("sorts",  ioConfig.getScrapeUrlSorts()              );

		Files.copy(
				HttpHelper.getStream( buildUrl(urlParms) ), 
				outFile.toPath(), 
				StandardCopyOption.REPLACE_EXISTING);
	}

	private URL buildUrl(Map<String, String> parms) throws UnsupportedEncodingException, MalformedURLException {
		log.debug("in parms=" + parms.toString());
		
		String urlParms  = ParmStringBuilder.getParmString(parms);
		String urlString = ioConfig.getScrapeUrlBase() + urlParms;
		
		URL url = new URL(urlString);
		log.info("url=" + urlString);
		
		return url;
	}
	
}
