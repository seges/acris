package sk.seges.acris.server.image.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sk.seges.acris.server.image.model.ColorType;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class ImageLoaderFactory {

	enum ImageFormat {
		JPEG {
			@Override
			boolean appliedTo(String formatName) {
				return formatName.toLowerCase().equals("jpeg");
			}

			@Override
			boolean hasExtension(String fileExtension) {
				return fileExtension.toLowerCase().equals("jpg") || fileExtension.toLowerCase().equals("jpeg");
			}
		}, 
		BMP {
			@Override
			boolean appliedTo(String formatName) {
				return formatName.toLowerCase().equals("bmp");
			}

			@Override
			boolean hasExtension(String fileExtension) {
				return fileExtension.toLowerCase().equals("bmp");
			}
		}, 
		PNG {
			@Override
			boolean appliedTo(String formatName) {
				return formatName.toLowerCase().equals("png");
			}

			@Override
			boolean hasExtension(String fileExtension) {
				return fileExtension.toLowerCase().equals("png");
			}
		}, 
		TIFF {
			@Override
			boolean appliedTo(String formatName) {
				return formatName.toLowerCase().equals("tiff");
			}

			@Override
			boolean hasExtension(String fileExtension) {
				return fileExtension.toLowerCase().equals("tif") || fileExtension.toLowerCase().equals("tiff");
			}
		};
		
		abstract boolean appliedTo(String formatName);
		abstract boolean hasExtension(String fileExtension);

		static ImageFormat fromImageExtension(String fileName) {
			String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1).trim();
			
			for (ImageFormat imageFormat: ImageFormat.values()) {
				if (imageFormat.hasExtension(extensionName)) {
					return imageFormat;
				}
			}

			return null;
		}
		
		static ImageFormat fromImageFormat(String formatName) {
			for (ImageFormat imageFormat: ImageFormat.values()) {
				if (imageFormat.appliedTo(formatName)) {
					return imageFormat;
				}
			}
			
			return null;
		}
	}
	
	private static ColorType getColorType(FileInputStream in) {
		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
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

	private static ImageFormat getFormatName(File file) {
	    try {
	        ImageInputStream iis = ImageIO.createImageInputStream(file);

	        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
	        if (!iter.hasNext()) {
	            return ImageFormat.fromImageExtension(file.getName());
	        }

	        ImageReader reader = iter.next();

	        iis.close();

	        return ImageFormat.fromImageFormat(reader.getFormatName());
	    } catch (IOException e) {
	    }
	    
	    return null;
	}
	
	//http://software.intel.com/sites/products/documentation/hpc/ipp/ippi/ippi_ch6/ch6_color_models.html
	public static ImageLoader getImageLoader(File file) {
		
		ImageFormat formatName = getFormatName(file);

		switch (formatName) {
			case BMP:
				return new DefaultImageLoader(file);
			case PNG:
				return new DefaultImageLoader(file);
			case TIFF:
				return new TiffImageLoader(file);
			case JPEG:
				ColorType colorType;
				try {
					colorType = getColorType(new FileInputStream(file));
				} catch (FileNotFoundException e) {
					return null;
				}
	
				switch (colorType) {
					case CMYK:
		//				return new RGBImageLoader(file);
						return new CMYKImageLoader(file);
					case RGB:
						return new RGBImageLoader(file);
					case YCCK:
						return new YCCKImageLoader(file);
				}
		}
		
		return null;
	}
}