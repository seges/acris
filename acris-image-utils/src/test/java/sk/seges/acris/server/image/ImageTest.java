package sk.seges.acris.server.image;

import org.junit.Assert;
import sk.seges.acris.server.image.loader.ImageLoader;
import sk.seges.acris.server.image.loader.ImageLoaderFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

public class ImageTest {

	protected String SOURCE_PREFIX = "source_";
	protected String RESULT_PREFIX = "result_";
	protected String OUTPUT_TYPE = "JPEG";
	protected String EXPECTED_FILE_EXTENSION = "jpg";

	protected class DefaultImageProcessor implements ImageProcessor {
		
		protected float getMaxAllowedDifference() {
			return 1.8f;
		}
		
		@Override
		public boolean process(String fileName, String expectedFileName) throws IOException {
			String outputName = convertAndSave(fileName, expectedFileName);
			float currentDiff = compareImages(outputName, expectedFileName);
			boolean result = currentDiff < getMaxAllowedDifference();
			System.out.println("Diff with original file for file name " + fileName + " is " + currentDiff + "%");
			Assert.assertTrue("Expected result " + fileName + " should not differ from original!", result);
			return true;
		}
	}
	
	protected interface ImageProcessor {
		boolean process(String fileName, String expectedFileName) throws IOException;
	}
	
	protected void interateDirectory(String directory, ImageProcessor imageProcessor) throws IOException {
		for (String fileName: Image.getFileDescriptor(directory).list()) {
			if (fileName.startsWith(SOURCE_PREFIX)) {
				String expectedFileName = fileName.replace(SOURCE_PREFIX, RESULT_PREFIX);
				expectedFileName = expectedFileName.substring(0, expectedFileName.lastIndexOf(".") + 1) + EXPECTED_FILE_EXTENSION;
				
				if (directory.endsWith(File.separator)) {
					directory = directory + File.separator;
				}
				
				if (Image.getFileDescriptor(directory + expectedFileName).exists()) {
					if (!imageProcessor.process(directory + fileName, directory + expectedFileName)) {
						return;
					}
				}
			}
		}
	}
	
	protected String convertAndSave(String filename, String outputName) throws IOException {

		String path = URLDecoder.decode(filename, "UTF-8").trim();

		ImageLoader imageLoader = ImageLoaderFactory.getImageLoader(Image.getFileDescriptor(path));
		BufferedImage originalImage = imageLoader.getBufferedImage();

		File outputFile = Image.getFileDescriptor("target/result/" + outputName);

		if (!outputFile.exists()) {
			if (!outputFile.mkdirs()) {
                throw new RuntimeException("Unable to create directory " + outputFile.getAbsolutePath());
            }
		}

		ImageIO.write(originalImage, OUTPUT_TYPE, outputFile);
		
		return outputFile.getAbsolutePath();
	}

	protected float compareImages(String outputName, String expectedOutputName) {
		ImageLoader outputImageLoader = ImageLoaderFactory.getImageLoader(Image.getFileDescriptor(outputName));
		Raster outputRaster = outputImageLoader.getBufferedImage().getRaster();

		ImageLoader expectedImageLoader = ImageLoaderFactory.getImageLoader(Image.getFileDescriptor(expectedOutputName));
		Raster expectedRaster = expectedImageLoader.getBufferedImage().getRaster();
		
		float maxDif = 0.0f;
		float avgDif = 0.0f;
		
		for (int x = expectedRaster.getMinX(); x < expectedRaster.getWidth(); ++x) {
		    for (int y = expectedRaster.getMinY(); y < expectedRaster.getHeight(); ++y) {
		    	ComparableColor expectedColor = new ComparableColor(expectedRaster.getPixel(x, y, (float[])null));
		    	ComparableColor color = new ComparableColor(outputRaster.getPixel(x, y, (float[])null));
		    	
		    	float diff = color.getDiff(expectedColor);
		    	
	    		avgDif += diff;
		    	
		    	if (diff > maxDif) {
		    		maxDif = diff;
		    	}
			}
		}
		
		avgDif = avgDif / (expectedRaster.getWidth() * expectedRaster.getHeight());
		return avgDif;
	}
	
	static class ComparableColor {
		
		private final float[] colorParts;
		
		public ComparableColor(float[] colorParts) {
			this.colorParts = colorParts;
		}
		
		public float getDiff(ComparableColor color) {
			
			float result = 0;
			
			for (int i = 0; i < Math.min(color.colorParts.length, colorParts.length); i++) {
				result += (colorParts[i] - color.colorParts[i]) / 2.55f;
			}
			
			return Math.abs(result / 3);
		}
	}
	
	protected static class Image extends File {

		private static final long serialVersionUID = 6114437233989603758L;

		public static final String FILE_SEPARATOR = "/";

		public Image(String pathname) {
			super(pathname);
		}

		public Image(URI uri) {
			super(uri);
		}

		public static Image getFileDescriptor(String fileName) {
			URL url;
			Image file;
			
			if (!fileName.startsWith(FILE_SEPARATOR) && !fileName.startsWith(".")) {
				url = Image.class.getResource(FILE_SEPARATOR + fileName);

				if (url == null) {
					return new Image(fileName);
				}

				try {
					file = new Image(url.toURI());
				} catch (URISyntaxException e) {
					throw new RuntimeException(e);
				}
			} else {
				file = new Image(fileName);
			}
			
			return file;
		}
	}
}