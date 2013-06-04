package sk.seges.acris.server.image.loader;

import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;

public abstract class ImageLoader {

	public abstract BufferedImage getBufferedImage();

	protected static boolean hasAlpha(ColorModel cm) {
		if (cm == null) {
			return false;
		}

		if (cm.getColorSpace().getType() != ColorSpace.TYPE_RGB) {
			return false;
		}

		return cm.hasAlpha();
	}

	protected static ColorModel getColorModel(Image image) {
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (Exception e) {
			return null;
		}

		return pg.getColorModel();
	}
}