package io.taper.batch.extract.shows4band;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.taper.config.IOConfig;
import io.taper.domain.ShowDetails;
import io.taper.domain.ShowOverview;
import io.taper.domain.ShowsForBand;
import lombok.extern.slf4j.Slf4j;


@Component
@Order(2)
@ComponentScan("io.taper.config")
@Slf4j
public class ExtractDetailsForEachShow implements CommandLineRunner {
	
	@Autowired
	private IOConfig ioConfig;
	
	@Override
	public void run(String... args) throws Exception {
		
		String commandLineArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		String bandName        = Arrays.stream(args).findFirst().get(); 
		String inFileName      = ioConfig.getPath() + bandName + ".json";

		log.info("App started with args=" + commandLineArgs);
		log.debug("Band Name=" + bandName);
		
		ObjectMapper mapper = new ObjectMapper();
		ShowsForBand shows  = mapper.readValue(new File(inFileName), ShowsForBand.class);
		log.debug("shows(0)=" + shows.getItems().get(0).toString());

		List<ShowOverview> showItems = shows.getItems();
		
		showItems.forEach(item -> {
			String outFileName = ioConfig.getPath() + bandName + "_" + item.getIdentifier() + ".json";
			try {
				writeShowMetadata(
						mapper,
						getShowMetadata(mapper, item),
						outFileName
					);
				TimeUnit.SECONDS.sleep(1);	//is this necessary? i don't want to look like a ddos attempt.
			} 
			// exit with non-zero return code or throw exception?
			catch (IOException e) {
				log.error("writeShowMetadata threw exception for item=" + item, e);
			} 
			catch (InterruptedException e) {
				log.error("WriteShowMetadata loop was interrupted for item=" + item, e);
			}
		});
		
	}

	private ShowDetails getShowMetadata(ObjectMapper mapper, ShowOverview item) 
			throws IOException 
	{
		URL url = new URL(ioConfig.getMetadataUrlBase() + item.getIdentifier());	
		ShowDetails bar = mapper.readValue(url, ShowDetails.class);
		log.debug("metadata = " + bar);
		return bar;
	}

	private void writeShowMetadata(ObjectMapper mapper, ShowDetails object, String fileName) 
			throws JsonGenerationException, JsonMappingException, IOException 
	{
		File file = new File(fileName);
		mapper.writeValue(file, object);
	}
	
}
