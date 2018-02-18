package io.taper.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpHelper {

	public static InputStream getStream(URL url) throws IOException {
		log.debug("in url=" + url.toString());
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(15000);
		con.setReadTimeout(15000);
		
		int httpRespCode = con.getResponseCode();
		InputStream stream = con.getInputStream();
		
		log.info("http responseCode=" + httpRespCode);

		return stream;
	}

}
