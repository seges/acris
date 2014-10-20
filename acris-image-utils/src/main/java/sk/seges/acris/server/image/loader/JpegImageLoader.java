package sk.seges.acris.server.image.loader;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import sk.seges.acris.server.image.model.ColorType;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Sources:
 * https://docs.atlassian.com/atlassian-core/4.6.11/xref/com/atlassian/core/util/thumbnail/loader/CMYKImageLoader.html
 * http://stackoverflow.com/questions/3123574/how-to-convert-from-cmyk-to-rgb-in-java-correctly
 */
public abstract class JpegImageLoader extends ProcessingImageLoader {

	protected final File file;

	protected JpegImageLoader(File file){
		this.file = file;
	}
	
	static ColorType getJpegColorType(File file) {

        int colorType;

        try {
            colorType = Imaging.getImageInfo(file).getColorType();
        } catch (Exception e) {
            return null;
        }

		switch (colorType) {
			case ImageInfo.COLOR_TYPE_CMYK:
				return ColorType.CMYK;
			case ImageInfo.COLOR_TYPE_YCCK:
				return ColorType.YCCK;
            default:
//            case ImageInfo.COLOR_TYPE_RGB:
                return ColorType.RGB;
//			default:
//				throw new RuntimeException("Unsupported image type (" + colorType + ") " + file.getAbsolutePath());
		}
	}

	@Override
	public BufferedImage getBufferedImage() {
		Raster srcRaster;

		FileInputStream fileInputStream;
        BufferedImage ret;

		try {
			fileInputStream = new FileInputStream(file);
			srcRaster = getRasterFromStream(fileInputStream);

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

    private Raster getRasterFromStream(InputStream inputStream) throws IOException {

        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        try {
            while (readers.hasNext()) {
                reader = readers.next();
                if (reader.canReadRaster()) {
                    break;
                } else {
                    reader.dispose();
                    reader = null;
                }
            }
            if (reader == null) {
                return null;
            }
            reader.setInput(ImageIO.createImageInputStream(inputStream));
            return reader.readRaster(0, null);
        } finally {
            if (reader != null) {
                reader.dispose();
            }
        }
    }
}