package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import sk.seges.acris.server.image.converter.GhostscriptCMYKtoRGBConverter;
import sk.seges.acris.server.image.converter.RGBConverter;

import com.sun.media.jai.codec.ImageDecoder;

public class CmykTiffImageLoader extends TiffImageLoader {
	
	public CmykTiffImageLoader(File file) {
		super(file);
	}

	public CmykTiffImageLoader(ImageDecoder dec) {
		super(dec);
	}
		
	protected BufferedImage getBufferedImage(ImageDecoder dec) {
		try {
			return convertRenderedImage(dec.decodeAsRenderedImage());
		} catch (IOException e1) {
			return null;
		}
	}

	public BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage)img;	
		}	
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		processRaster(img.getData(), result.getRaster());
		return result;
	}

	@Override
	protected RawColor processPixel(RawColor pixel) {

		RGBConverter convert = new GhostscriptCMYKtoRGBConverter().convert(
								255 - pixel.getParts()[0], 
								255 - pixel.getParts()[1], 
								255 - pixel.getParts()[2], 
								255 - pixel.getParts()[3]);

		return new RawColor(convert.getR(), convert.getG(), convert.getB());
	}
}