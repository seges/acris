package sk.seges.acris.server.image.loader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import sk.seges.acris.server.image.model.ColorType;

import com.sun.media.jai.codec.ImageDecoder;

public class ImageLoaderFactory {

	public static enum ImageFormat {
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

		public static ImageFormat fromImageExtension(String fileName) {
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
		
	public static ImageFormat getFormatName(File file) {
	    try {
	        ImageInputStream iis = ImageIO.createImageInputStream(file);

	        if (iis == null) {
	        	//strange unsupported "image"
	        	return null;
	        }
	        
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

		if (formatName == null) {
			return null;
		}
		
		switch (formatName) {
			case BMP:
				return new DefaultImageLoader(file);
			case PNG:
				return new RGBImageLoader(file);
			case TIFF:
				{
					ImageDecoder imageDecoder = TiffImageLoader.getImageDecoder(file);
					ColorType colorType = TiffImageLoader.getTiffColorType(imageDecoder);
					if (colorType == null) {
						return null;
					}
					switch (colorType) {
					case CMYK:
						return new CmykTiffImageLoader(imageDecoder);
					case RGB:
						return new RGBTiffImageLoader(file);
					}
					
					return null;
				}
			case JPEG:
				{
					ColorType colorType = JpegImageLoader.getJpegColorType(file);
					if (colorType == null) {
						return null;
					}
		
					switch (colorType) {
						case CMYK:
							return new CMYKImageLoader(file);
						case RGB:
							return new RGBImageLoader(file);
						case YCCK:
							return new YCCKImageLoader(file);
					}
				}
		}
		
		return null;
	}
}