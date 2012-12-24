package sk.seges.acris.server.image.loader;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;

import javax.swing.ImageIcon;

public class RGBImageLoader extends ImageLoader {

	private final Image image;
	
	public RGBImageLoader(File file) {
		Image image = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());
		this.image = new ImageIcon(image).getImage();
	}
	
	@Override
	public BufferedImage getBufferedImage() {
		
		ColorModel cm = getColorModel(image);
		boolean hasAlpha = hasAlpha(cm);

		BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null), hasAlpha ? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB);

		Graphics2D g = result.createGraphics();
		g.drawImage(image, 0, 0, result.getWidth(), result.getHeight(), null);
		g.dispose();

		return result;
	}
}