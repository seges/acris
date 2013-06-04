package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class JpegYCCKTest extends ImageTest {
	
	@Test
	public void testYcckImage() throws IOException {
		interateDirectory("sk/seges/acris/server/image/ycck/", new DefaultImageProcessor());
	}
}