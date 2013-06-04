package sk.seges.acris.server.image.converter;

public class GhostscriptCMYKtoRGBConverter extends BaseCMYKtoRGBConverter {
	
	public RGBConverter convert(float c, float m, float y, float k) {
		
		int colors = (int) (255 - k);
		r = (int) (colors * (255 - c)/255);
		g = (int) (colors * (255 - m)/255);
		b = (int) (colors * (255 - y)/255);

		return this;
	}
}