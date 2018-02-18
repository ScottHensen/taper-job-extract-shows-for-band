package io.taper.batch.extract.shows4band;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.taper.domain.ShowMetadata;
import io.taper.domain.ShowsForBand;
import io.taper.util.HttpHelper;
import lombok.extern.slf4j.Slf4j;


@Component
@Order(2)
@Slf4j
public class ExtractDetailsForEachShow implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {
		//TODO:  put this in config file
		final String FILEPATH  = "C:\\data\\files\\test\\";
		
		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		String bandName        = Arrays.stream(args).findFirst().get(); 
		String inFileName        = FILEPATH + bandName + ".json";
		log.info("App started with args=" + commandLineArgs);
		log.debug("Band Name=" + bandName);
		
		ObjectMapper mapper = new ObjectMapper();
		ShowsForBand shows  = mapper.readValue(new File(inFileName), ShowsForBand.class);
		log.debug("shows(0)=" + shows.getItems().get(0).toString());
		List<ShowMetadata> showItems = shows.getItems();
		
		showItems.forEach(item -> {
			String outFileName = FILEPATH + bandName + "_" + item.getIdentifier() + ".json";
			try {
				extractShowDetailsMetadata(item,outFileName);
				TimeUnit.SECONDS.sleep(1);	//is this necessary? i don't want to look like a ddos attempt.
			} catch (IOException e) {
				log.error("extractShowDetailsMetadata threw exception for item=" + item, e);
			} catch (InterruptedException e) {
				log.error("extractShowDetailsMetadata loop was interrupted for item=" + item, e);
			}
		});
		
	}

	private Object extractShowDetailsMetadata(ShowMetadata item, String outFileName) throws IOException {
		String urlBase   = "https://archive.org/metadata/";
		URL url = new URL(urlBase + item.getIdentifier());
		File outFile = new File(outFileName);
		Files.copy(
				HttpHelper.getStream(url), 
				outFile.toPath(), 
				StandardCopyOption.REPLACE_EXISTING);
		return null;
	}
}
