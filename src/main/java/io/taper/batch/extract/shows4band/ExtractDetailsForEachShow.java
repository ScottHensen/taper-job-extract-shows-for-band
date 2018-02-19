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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.taper.config.IOConfig;
import io.taper.domain.ShowMetadata;
import io.taper.domain.ShowsForBand;
import io.taper.util.HttpHelper;
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

		List<ShowMetadata> showItems = shows.getItems();
		
		showItems.forEach(item -> {
			String outFileName = ioConfig.getPath() + bandName + "_" + item.getIdentifier() + ".json";
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

		URL url = new URL(ioConfig.getMetadataUrlBase() + item.getIdentifier());
		File outFile = new File(outFileName);

		//TODO: map the stream to pull off the data we care about and write it as csv; not json
		//      ...or map the result.metadata{} to ShowDetailsMetadata.
		Files.copy(
				HttpHelper.getStream(url), 
				outFile.toPath(), 
				StandardCopyOption.REPLACE_EXISTING);

		return null;
	}
}
