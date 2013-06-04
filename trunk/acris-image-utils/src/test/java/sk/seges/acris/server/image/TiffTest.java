package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class TiffTest extends ImageTest {
	
	@Test
	public void testTiffImage() throws IOException {
		interateDirectory("sk/seges/acris/server/image/tiff/", new DefaultImageProcessor());
	}
}