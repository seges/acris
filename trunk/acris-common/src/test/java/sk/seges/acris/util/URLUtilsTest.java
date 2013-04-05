/**
 * 
 */
package sk.seges.acris.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sk.seges.acris.common.util.URLUtils;

/**
 * @author eldzi
 */
public class URLUtilsTest {
	@Test
	public void testTransformToNiceURLAnURLWithoutAdditionalPathElements() throws Exception {
		String url;
		String niceURL = "i-am-very-nice";
		String transformed;
		
		url = "http://seges.sk/sk.seges.web.Site/Site.html";
		transformed = URLUtils.createHRefWithNiceURL(url, niceURL);
		
		assertEquals("http://seges.sk/" + niceURL, transformed);
		
		url = "http://seges.sk/sk.seges.web.Site/Site.html#";
		assertEquals("http://seges.sk/" + niceURL, transformed);
	}
	
	@Test
	public void testTransformToNiceURLAnURLWithParameters() throws Exception {
		String url;
		String niceURL = "i-am-very-nice";
		String transformed;
		
		url = "http://seges.sk/sk.seges.web.Site/Site.html?locale=sk";
		transformed = URLUtils.createHRefWithNiceURL(url, niceURL);
		
		assertEquals("http://seges.sk/" + niceURL, transformed);
		
		url = "http://seges.sk/sk.seges.web.Site/Site.html#myToken";
		assertEquals("http://seges.sk/" + niceURL, transformed);
	}
	
	@Test
	public void testReplaceFirstLevelDomain() {
//		String g = replaceDomain("http://www.seges.sk/");
		String g = URLUtils.replaceFirstLevelDomainName("http://www.seges.sk/sk.seges.web.Site/Site.html", "www.seges.sk", "com");
		assertEquals("http://www.seges.com/sk.seges.web.Site/Site.html", g);
	}
}
