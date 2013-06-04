package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class BmpTest extends ImageTest {
	
	@Test
	public void testBmpImage() throws IOException {
		interateDirectory("sk/seges/acris/server/image/bmp/", new DefaultImageProcessor());
	}
}