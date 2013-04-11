package sk.seges.acris.server.image.converter;

public class PhotoshopCMYKtoRGBConverter extends BaseCMYKtoRGBConverter {

	public RGBConverter convert(float c, float m, float y, float k) {
		r = (int) (255 - Math.min(255, c + k));
		g = (int) (255 - Math.min(255, m + k));
		b = (int) (255 - Math.min(255, y + k));

		return this;
	}
}