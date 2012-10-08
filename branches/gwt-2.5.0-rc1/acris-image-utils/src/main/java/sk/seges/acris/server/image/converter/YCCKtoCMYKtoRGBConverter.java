package sk.seges.acris.server.image.converter;


public class YCCKtoCMYKtoRGBConverter implements CMYKConverter {

	private float c;
	private float m;
	private float y;
	private float k;
	
	@Override
	public float getC() {
		return c;
	}

	@Override
	public float getM() {
		return m;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getK() {
		return k;
	}

	public CMYKConverter convert(float y, float cb, float cr, float k) {

		cb = cb - 128;
		cr = cr - 128;

		float val = 255 - (float) (y +(1.4020d*cr));
		this.c = val < 0.0 ? 0 : val > 255.0 ? 0xff : val;
		val = 255 - (float) (y - (0.3441363d*cb) - (0.71413636d*cr));
		this.m = val < 0.0 ? 0 : val > 255.0 ? 0xff : val;
		val = 255 - (float) (y + (1.7718d*cb));
		this.y = val < 0.0 ? 0 : val > 255.0 ? 0xff : val;

		this.k = k;

		return this;
	}
}