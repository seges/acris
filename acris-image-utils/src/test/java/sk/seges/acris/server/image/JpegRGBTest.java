package sk.seges.acris.server.image;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class JpegRGBTest extends ImageTest {

	@Test
	public void testRGBImage() throws IOException {
		interateDirectory("sk" + File.separator + "seges" + File.separator + "acris" + File.separator +
                "server" + File.separator + "image" + File.separator + "rgb" + File.separator, new DefaultImageProcessor());
	}

    @Test
    public void testLargeRGBImage() throws IOException {
        interateDirectory("sk" + File.separator + "seges" + File.separator + "acris" + File.separator +
                "server" + File.separator + "image" + File.separator + "rgb" + File.separator +
                "large" + File.separator, new DefaultImageProcessor());
        //interateDirectory("sk/seges/acris/server/image/rgb/large", new DefaultImageProcessor());
    }

}