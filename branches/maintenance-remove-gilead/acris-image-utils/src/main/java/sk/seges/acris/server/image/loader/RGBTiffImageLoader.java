package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.sun.media.jai.codec.ImageDecoder;

public class RGBTiffImageLoader extends TiffImageLoader {

	public RGBTiffImageLoader(File file) {
		super(file);
	}

	public RGBTiffImageLoader(ImageDecoder dec) {
		super(dec);
	}

	protected BufferedImage getBufferedImage(ImageDecoder dec) {
		RenderedImage img;
		try {
			img = dec.decodeAsRenderedImage();
		} catch (IOException e) {
			return null;
		}
		ColorModel cm = img.getColorModel();
	    int width = img.getWidth();
	    int height = img.getHeight();
	    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    Hashtable<String, Object> properties = new Hashtable<String, Object>();
	    String[] keys = img.getPropertyNames();
	    if (keys != null) {
	        for (int i = 0; i < keys.length; i++) {
	            properties.put(keys[i], img.getProperty(keys[i]));
	        }
	    }
	    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
	    img.copyData(raster);
	    return result;
	}

	@Override
	protected RawColor processPixel(RawColor pixel) {
		return null;
	}
}