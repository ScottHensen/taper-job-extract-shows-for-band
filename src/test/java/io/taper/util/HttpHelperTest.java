package io.taper.util;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpHelperTest {

	@Test(expected = IOException.class)
	public void testException() throws IOException {
		// Given
		URL url = new URL("https://this.is.an.unknown.url.io");
		// When
		InputStream response = HttpHelper.getStream(url);
	}

}
