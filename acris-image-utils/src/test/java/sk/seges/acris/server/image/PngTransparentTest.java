package sk.seges.acris.server.image;

import java.io.IOException;

import org.junit.Test;

public class PngTransparentTest extends ImageTest {

	@Test
	public void testTransparentPngImages() throws IOException {
		interateDirectory("sk/seges/acris/server/image/png_transparent/", new DefaultImageProcessor());
	}
}