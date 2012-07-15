package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import sk.seges.acris.server.image.converter.GhostscriptCMYKtoRGBConverter;
import sk.seges.acris.server.image.converter.RGBConverter;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

public class TiffImageLoader extends ProcessingImageLoader {

	private final File file;

	public TiffImageLoader(File file) {
		this.file = file;
	}

	@Override
	public BufferedImage getBufferedImage() {
		SeekableStream s;
		try {
			s = new FileSeekableStream(file);
		} catch (IOException e) {
			return null;
		}

		TIFFDecodeParam param = null;

		ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
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
//		ColorModel cm = img.getColorModel();
//		int width = img.getWidth();
//		int height = img.getHeight();
		//WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		//boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//		Hashtable<String, Object> properties = new Hashtable<String, Object>();
//		String[] keys = img.getPropertyNames();
//		if (keys != null) {
//			for (int i = 0; i < keys.length; i++) {
//				properties.put(keys[i], img.getProperty(keys[i]));
//			}
//		}
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
//		BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
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