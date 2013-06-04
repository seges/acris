package sk.seges.acris.server.image.converter;

abstract class BaseCMYKtoRGBConverter extends BaseRGBConverter {

	public abstract RGBConverter convert(float c, float m, float y, float k);

	public RGBConverter convert(CMYKConverter converter) {
		return convert(converter.getC(), converter.getM(), converter.getY(), converter.getK());
	}
}