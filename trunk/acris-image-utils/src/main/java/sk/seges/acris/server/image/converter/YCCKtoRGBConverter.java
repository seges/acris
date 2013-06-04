package sk.seges.acris.server.image.converter;

public class YCCKtoRGBConverter extends BaseRGBConverter {

	public RGBConverter convert(float y, float cb, float cr, float k) {

		double val = y + 1.402 * (cr - 128) - k;
		this.r = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
		val = y - 0.34414 * (cb - 128) - 0.51414 * (cr - 128) - k;
		this.g = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
		val = y + 1.772 * (cb - 128) - k;
		this.b = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

		return this;
	}

}