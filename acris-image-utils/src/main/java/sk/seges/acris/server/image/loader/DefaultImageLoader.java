package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DefaultImageLoader extends ImageLoader {

	private final File file;

	public DefaultImageLoader(File file) {
		this.file = file;
	}

	@Override
	public BufferedImage getBufferedImage() {

		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(file.getAbsolutePath()));
		} catch (IOException e) {
			return null;
		}

		BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, null);

		return newBufferedImage;
	}
}