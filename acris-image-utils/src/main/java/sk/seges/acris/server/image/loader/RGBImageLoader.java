package sk.seges.acris.server.image.loader;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RGBImageLoader extends ImageLoader {

	private final BufferedImage image;
	
	public RGBImageLoader(File file) {
        image = createThumbnail(getBufferedImage(file));
	}

    public static BufferedImage createThumbnail(BufferedImage img) {
        return Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 2000, 2000, Scalr.OP_ANTIALIAS);
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