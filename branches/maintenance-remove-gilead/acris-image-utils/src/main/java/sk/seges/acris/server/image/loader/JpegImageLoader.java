package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sk.seges.acris.server.image.model.ColorType;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public abstract class JpegImageLoader extends ProcessingImageLoader {

	protected final File file;

	protected JpegImageLoader(File file){
		this.file = file;
	}
	
	static ColorType getJpegColorType(File file) {
		
		JPEGImageDecoder decoder;
		try {
			decoder = JPEGCodec.createJPEGDecoder(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			return null;
		}
		
		try {
			decoder.decodeAsRaster();
		} catch (ImageFormatException e) {
			return ColorType.RGB;
		} catch (IOException e) {
			return ColorType.RGB;
		}
		
		int colorType = decoder.getJPEGDecodeParam().getEncodedColorID();	
		
		switch (colorType) {
			case 4:
				return ColorType.CMYK;
			case 7:
				return ColorType.YCCK;
			default:
				return ColorType.RGB;
		}
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
}