package sk.seges.acris.server.image.loader;

import java.io.File;

import sk.seges.acris.server.image.converter.GhostscriptCMYKtoRGBConverter;
import sk.seges.acris.server.image.converter.RGBConverter;
import sk.seges.acris.server.image.converter.YCCKtoCMYKtoRGBConverter;

public class YCCKImageLoader extends CMYKImageLoader {

	YCCKImageLoader(File file) {
		super(file);
	}

	protected RawColor processPixel(RawColor pixel) {

//		RGBConverter convert = new YCCKtoRGBConverter().convert(
//				255 - pixel.getParts()[0], 
//				255 - pixel.getParts()[1], 
//				255 - pixel.getParts()[2], 
//				255 - pixel.getParts()[3]);

		RGBConverter convert = new GhostscriptCMYKtoRGBConverter().convert(
						new YCCKtoCMYKtoRGBConverter().convert(
								255 - pixel.getParts()[0], 
								255 - pixel.getParts()[1], 
								255 - pixel.getParts()[2], 
								255 - pixel.getParts()[3]));

		return new RawColor(convert.getR(), convert.getG(), convert.getB());
	}
}