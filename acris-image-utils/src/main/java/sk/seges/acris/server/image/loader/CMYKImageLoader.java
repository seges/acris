package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;

import sk.seges.acris.server.image.converter.GhostscriptCMYKtoRGBConverter;
import sk.seges.acris.server.image.converter.RGBConverter;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class CMYKImageLoader extends ProcessingImageLoader {

	protected final File file;

	CMYKImageLoader(File file) {
		this.file = file;
	}

	@Override
	public BufferedImage getBufferedImage() {
		Raster srcRaster;
		
		BufferedImage ret;
		try {
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(new FileInputStream(file));
			srcRaster = decoder.decodeAsRaster();

			ret = new BufferedImage(srcRaster.getWidth(), srcRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
		} catch (Exception ex) {
			return null;
		}

		processRaster(srcRaster, ret.getRaster());
		
		return ret;
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