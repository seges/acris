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
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			decoder = JPEGCodec.createJPEGDecoder(fileInputStream);
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
		
		try {
			fileInputStream.close();
		} catch (IOException e) {
			return null;
		}
		
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
		FileInputStream fileInputStream;
		
		try {
			fileInputStream = new FileInputStream(file);
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fileInputStream);
			srcRaster = decoder.decodeAsRaster();

			ret = new BufferedImage(srcRaster.getWidth(), srcRaster.getHeight(), BufferedImage.TYPE_INT_RGB);
		} catch (Exception ex) {
			return null;
		}

		processRaster(srcRaster, ret.getRaster());

		try {
			fileInputStream.close();
		} catch (IOException e) {}
		
		return ret;
	}
}