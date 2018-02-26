package io.taper.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class HttpHelperTest {

	@Test(expected = IOException.class)
	public void givenUrlDoesNotExist_whenHttpGet_thenExceptionThrown() throws IOException {
		// Given
		URL url = new URL("https://this.is.an.unknown.url.io");
		// When
		@SuppressWarnings("unused")
		InputStream response = HttpHelper.getStream(url);
	}
	
	@Test
	public void givenUrlExists_whenHttpGet_thenSuccess() throws IOException {
		// Given
		URL url = new URL("https://www.google.com");
		// When
		InputStream response = HttpHelper.getStream(url);

		@SuppressWarnings("resource")
		Scanner s = new Scanner(response).useDelimiter("\\A");
		String responseString = s.hasNext() ? s.next() : "";
		// Then
		assertThat(responseString).containsIgnoringCase("Search");
	}

}
