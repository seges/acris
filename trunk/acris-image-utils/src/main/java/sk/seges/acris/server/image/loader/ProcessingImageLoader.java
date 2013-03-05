package sk.seges.acris.server.image.loader;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public abstract class ProcessingImageLoader extends ImageLoader {

	static class RawColor {
		
		private float[] parts;
		
		public RawColor(float... parts) {
			this.parts = parts;
		}
		
		public void setParts(float[] parts) {
			this.parts = parts;
		}
		
		public float[] getParts() {
			return parts;
		}
	}
	
	protected void processRaster(Raster srcRaster, WritableRaster resultRaster) {
		for (int x = srcRaster.getMinX(); x < srcRaster.getWidth(); ++x) {
		    for (int y = srcRaster.getMinY(); y < srcRaster.getHeight(); ++y) {
				resultRaster.setPixel(x, y, processPixel(new RawColor(srcRaster.getPixel(x, y, (float[])null))).getParts());
			}
		}
	}

	protected abstract RawColor processPixel(RawColor pixel);

	protected ColorSpace getColorSpace(Image image) {
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (Exception e) {
			return null;
		}

		ColorModel cm = pg.getColorModel();
		if (cm == null) {
			return null;
		}

		return cm.getColorSpace();
	}

	protected boolean hasAlpha(Image image) {

		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (Exception e) {
			return false;
		}

		ColorModel cm = pg.getColorModel();
		if (cm == null) {
			return false;
		}

		if (cm.getColorSpace().getType() != ColorSpace.TYPE_RGB) {
			return false;
		}

		return cm.hasAlpha();
	}
}