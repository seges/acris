package sk.seges.sesam.core.test.webdriver.model;

public class Color {

	private Float h; // [0,360] Hue
	private Float s; // [0-1] Saturation
	private Float v; // [0-1] Value
	private Float r; // [0-1] Red
	private Float g; // [0-1] Green
	private Float b; // [0-1] Blue
	private String hex; // Hexadecimal notation of RGB

	private Integer colorPickerXPosition;
	private Integer colorPickerYPosition;
	private Integer range;
	
	public Color() {
	}

	public Color(String hex) {
		setHex(hex);
	}
	
	public Color(Float h, Float s, Float v) throws Exception {
		setHSV(h, s, v);
	}
	
	public Color(Long r, Long g, Long b) throws Exception {
		setRGB(r.intValue(), g.intValue(), b.intValue());
	}
	
	public Color(int colorPickerXPosition, int colorPickerYPosition, int range) {
		this.colorPickerXPosition = colorPickerXPosition;
		this.colorPickerYPosition = colorPickerYPosition;
		this.range = range;
	}
	
	public Integer getColorPickerXPosition() {
		return colorPickerXPosition;
	}
	
	public Integer getColorPickerYPosition() {
		return colorPickerYPosition;
	}
	
	public Integer getRange() {
		return range;
	}
	
	public boolean equals(Color color) {
		return r == color.r && g == color.g && b == color.b;
	}
	
	public void setHSV(float h, float s, float v) throws Exception {
		if (h < 0 || h > 360)
			throw new Exception();
		if (s < 0 || s > 100)
			throw new Exception();
		if (v < 0 || v > 100)
			throw new Exception();

		this.h = h;
		this.s = s / 100;
		this.v = v / 100;

		HSVtoRGB(this.h, this.s, this.v);

		setHex();
	}

	public void setRGB(int r, int g, int b) throws Exception {
		if (r < 0 || r > 255)
			throw new Exception();
		if (g < 0 || g > 255)
			throw new Exception();
		if (b < 0 || b > 255)
			throw new Exception();

		this.r = (float) r / 255;
		this.g = (float) g / 255;
		this.b = (float) b / 255;

		RGBtoHSV(this.r, this.g, this.b);

		setHex();
	}

	public void setColor(String color) throws Exception {
		if (color == null) {
			return;
		}
		if (color.startsWith("#")) {
			setHex(color.trim().replaceAll("#", ""));
		} else if (color.trim().toLowerCase().startsWith("rgb")) {
			color = color.trim().toLowerCase().replaceAll("rgb\\(", "");
			color = color.toLowerCase().replaceAll("\\)", "");
			String[] colors = color.trim().split(",");
			setRGB(Integer.parseInt(colors[0].trim()), Integer.parseInt(colors[1].trim()), Integer.parseInt(colors[2].trim()));
		} else {
			throw new Exception();
		}
	}

	public void setHex(String hex) {
		if (hex.length() == 6) {
			try {
				setRGB(Integer.parseInt(hex.substring(0, 2), 16), Integer.parseInt(hex.substring(2, 4), 16), Integer.parseInt(hex.substring(4, 6), 16));
			} catch (Exception e) {
				throw new RuntimeException("Cannot set hex color value = " + hex);
			}
		} else if (hex.length() != 0) {
			throw new RuntimeException("Cannot set hex color value = " + hex);
		}
	}

	private void setHex() {
		String hRed = Integer.toHexString(getRed());
		String hGreen = Integer.toHexString(getGreen());
		String hBlue = Integer.toHexString(getBlue());

		if (hRed.length() == 0) {
			hRed = "00";
		}
		if (hRed.length() == 1) {
			hRed = "0" + hRed;
		}
		if (hGreen.length() == 0) {
			hGreen = "00";
		}
		if (hGreen.length() == 1) {
			hGreen = "0" + hGreen;
		}
		if (hBlue.length() == 0) {
			hBlue = "00";
		}
		if (hBlue.length() == 1) {
			hBlue = "0" + hBlue;
		}

		this.hex = hRed + hGreen + hBlue;
	}

	public Integer getRed() {
		if (r == null) {
			return null;
		}
		return (int) (r * 255);
	}

	public Integer getGreen() {
		if (g == null) {
			return null;
		}
		return (int) (g * 255);
	}

	public Integer getBlue() {
		if (b == null) {
			return null;
		}
		return (int) (b * 255);
	}

	public Float getHue() {
		return h;
	}

	public Float getSaturation() {
		if (s == null) {
			return s;
		}
		return s * 100;
	}

	public Float getValue() {
		if (v == null) {
			return v;
		}
		return v * 100;
	}

	public String getHex() {
		return hex;
	}

	private void RGBtoHSV(float r, float g, float b) {
		float min = 0;
		float max = 0;
		float delta = 0;

		min = MIN(r, g, b);
		max = MAX(r, g, b);

		this.v = max; // v

		delta = max - min;

		if (max != 0) {
			this.s = delta / max; // s
		} else {
			// r = g = b = 0 // s = 0, v is undefined
			this.s = 0F;
			this.h = 0F;
			return;
		}

		if (delta == 0) {
			h = 0F;
			return;
		}

		if (r == max) {
			this.h = (g - b) / delta; // between yellow & magenta
		} else if (g == max) {
			this.h = 2 + (b - r) / delta; // between cyan & yellow
		} else {
			this.h = 4 + (r - g) / delta; // between magenta & cyan
		}

		this.h *= 60; // degrees

		if (this.h < 0)
			this.h += 360;
	}

	private void HSVtoRGB(float h, float s, float v) {
		int i;
		float f;
		float p;
		float q;
		float t;
		if (s == 0) {
			// achromatic (grey)
			this.r = v;
			this.g = v;
			this.b = v;
			return;
		}
		h /= 60; // sector 0 to 5
		i = (int) Math.floor(h);
		f = h - i; // factorial part of h
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
		switch (i) {
		case 0:
			this.r = v;
			this.g = t;
			this.b = p;
			break;
		case 1:
			this.r = q;
			this.g = v;
			this.b = p;
			break;
		case 2:
			this.r = p;
			this.g = v;
			this.b = t;
			break;
		case 3:
			this.r = p;
			this.g = q;
			this.b = v;
			break;
		case 4:
			this.r = t;
			this.g = p;
			this.b = v;
			break;
		default: // case 5:
			this.r = v;
			this.g = p;
			this.b = q;
			break;
		}
	}

	private float MAX(float a, float b, float c) {
		return Math.max(Math.max(a, b), c);
	}

	private float MIN(float a, float b, float c) {
		return Math.min(Math.min(a, b), c);
	}
}