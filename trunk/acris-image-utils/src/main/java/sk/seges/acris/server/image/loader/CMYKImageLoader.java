package sk.seges.acris.server.image.loader;

import java.io.File;

import sk.seges.acris.server.image.converter.GhostscriptCMYKtoRGBConverter;
import sk.seges.acris.server.image.converter.RGBConverter;

public class CMYKImageLoader extends JpegImageLoader {

	CMYKImageLoader(File file) {
		super(file);
	}

	protected RawColor processPixel(RawColor pixel) {

		RGBConverter convert = new GhostscriptCMYKtoRGBConverter().convert(
								255 - pixel.getParts()[0], 
								255 - pixel.getParts()[1], 
								255 - pixel.getParts()[2], 
								255 - pixel.getParts()[3]);

		return new RawColor(convert.getR(), convert.getG(), convert.getB());
	}
}