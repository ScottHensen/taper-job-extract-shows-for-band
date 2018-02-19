package io.taper.util;

import static org.assertj.core.api.Assertions.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParmStringBuilderTest {

	@Test
	public void returnsEncodedUrlParmList() throws UnsupportedEncodingException {
		
		Map<String, String> parmMap = new HashMap<>();
		parmMap.put("key1", "foo"); 
		parmMap.put("key2", "bar"); 
		String urlParms = ParmStringBuilder.getParmString(parmMap);
		assertThat(urlParms).isEqualTo("key1=foo&key2=bar");
	}

}
