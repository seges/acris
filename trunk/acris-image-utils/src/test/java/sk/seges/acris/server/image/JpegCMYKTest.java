package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class JpegCMYKTest extends ImageTest {

	@Test
	public void testCmykImage() throws IOException {
		interateDirectory("sk/seges/acris/server/image/cmyk/", new DefaultImageProcessor());
	}
}