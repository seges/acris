package sk.seges.acris.server.image.converter;



abstract class BaseRGBConverter implements RGBConverter {
	protected int r;
	protected int g;
	protected int b;
	
	@Override
	public int getR() {
		return r;
	}

	@Override
	public int getG() {
		return g;
	}

	@Override
	public int getB() {
		return b;
	}
}