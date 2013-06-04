package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class PngTest extends ImageTest {

	@Test
	public void testPngImages() throws IOException {
		interateDirectory("sk/seges/acris/server/image/png/", new DefaultImageProcessor());
	}
}