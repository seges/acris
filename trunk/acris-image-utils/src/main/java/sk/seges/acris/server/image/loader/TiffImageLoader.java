package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import sk.seges.acris.server.image.model.ColorType;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public abstract class TiffImageLoader extends ProcessingImageLoader {

	private ImageDecoder dec;
	private File file;

	public TiffImageLoader(File file) {
		this.file = file;
	}

	public TiffImageLoader(ImageDecoder dec) {
		this.dec = dec;
	}

	@Override
	public BufferedImage getBufferedImage() {
		
		if (dec != null) {
			return getBufferedImage(dec);			
		}
		
		return getBufferedImage(getImageDecoder(file));
	}

	protected abstract BufferedImage getBufferedImage(ImageDecoder dec);
	
	static ImageDecoder getImageDecoder(File file) {
		SeekableStream s;
		try {
			s = new FileSeekableStream(file);
		} catch (IOException e) {
			return null;
		}
		
		return ImageCodec.createImageDecoder("tiff", s, null);
	}
	
	static ColorType getTiffColorType(ImageDecoder imageDecoder) {
			
		try {
			if (imageDecoder.decodeAsRaster().getPixel(0, 0, (float[])null).length == 3) {
				return ColorType.RGB;
			}
			return ColorType.CMYK;

		} catch (IOException e) {
			return null;
		}
	}

}
