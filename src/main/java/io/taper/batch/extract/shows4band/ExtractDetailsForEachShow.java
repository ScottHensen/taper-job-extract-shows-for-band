package io.taper.batch.extract.shows4band;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		String pathName        = ioConfig.getPath();
		String inFileName      = pathName + bandName + ".json";

		log.info("App started with args=" + commandLineArgs);
		
		ObjectMapper mapper = new ObjectMapper();
		ShowsForBand shows  = mapper.readValue(new File(inFileName), ShowsForBand.class);

		log.info("IN  file = " + inFileName);
		log.info("IN  data = " + shows.getCount() + " of " + shows.getTotal() + " shows for " + bandName);
		log.debug("shows(0)=" + shows.getItems().get(0).toString());

		List<ShowOverview> showItems = shows.getItems();

		showItems.forEach(item -> {
			String tempFileName = pathName + bandName + "_" + item.getIdentifier() + ".json";
			try {
				ShowDetails showDetails = getShowMetadata(mapper, item);
				mapObjectToFile(mapper, showDetails, tempFileName);
				log.debug("OUT file = " + tempFileName);
				TimeUnit.SECONDS.sleep(1);	//is this necessary? i don't want to look like a ddos attempt.
			} 
			// exit with non-zero return code or throw exception?
			catch (IOException e) {
				log.error("writeShowMetadata threw exception for item=" + item.toString(), e);
				//add a push msg here we will know the data is incomplete
			} 
			catch (InterruptedException e) {
				log.error("WriteShowMetadata loop was interrupted for item=" + item.toString(), e);
				//add a push msg here we will know the data is incomplete
			}
		});
		List<Path> outFiles = listFiles(pathName, bandName + "_*" );
		log.info("OUT data = " + outFiles.size() + " files named " + bandName + "_*");
		if ( shows.getCount() != outFiles.size() ) {
			log.error("FILE CONTROL ERROR: Input items ("+shows.getCount()+") <> Output files ("+outFiles.size()+")");
			//add a push msg here so we know we didn't get all data
		}
	}

	private ShowDetails getShowMetadata(ObjectMapper mapper, ShowOverview item) 
			throws IOException 
	{
		URL url = new URL(ioConfig.getMetadataUrlBase() + item.getIdentifier());	
		ShowDetails bar = mapper.readValue(url, ShowDetails.class);
		//log.debug("metadata = " + bar);
		return bar;
	}

	private void mapObjectToFile(ObjectMapper mapper, Object object, String fileName) 
			throws JsonGenerationException, JsonMappingException, IOException 
	{
		File file = new File(fileName);
		mapper.writeValue(file, object);
	}
	
	private List<Path> listFiles(String dirName, String str) 
			throws IOException
	{
		Path dir = Paths.get(dirName);
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, str)) {
			for ( Path entry : stream ) {
				result.add(entry);
			}
		}
		catch (DirectoryIteratorException e ) {
			throw e.getCause();
		}
		return result;
	}
}
