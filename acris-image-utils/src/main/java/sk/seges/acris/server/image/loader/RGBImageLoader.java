package sk.seges.acris.server.image.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class RGBImageLoader extends ImageLoader {

	private final BufferedImage image;
	
	public RGBImageLoader(File file) {
        image = createThumbnail(getBufferedImage(file));
	}

    public static BufferedImage createThumbnail(BufferedImage img) {
    	if (img.getWidth() > 3000 || img.getHeight() > 3000) {
    		return Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 3000, 3000, Scalr.OP_ANTIALIAS);
    	}
    	return img;
    }

    protected BufferedImage getBufferedImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load image " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }
}