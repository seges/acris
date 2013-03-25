package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class JpegRGBTest extends ImageTest {

	@Test
	public void testRGBImage() throws IOException {
		interateDirectory("sk/seges/acris/server/image/rgb/", new DefaultImageProcessor());
	}
}