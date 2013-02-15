package sk.seges.sesam.core.test.webdriver.support;

import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.webdriver.model.Color;

public class StyleSupport {

	public interface CssValue {};

	public class ColorCssValue implements CssValue {

		private final Color color;
		
		public ColorCssValue(Color color) {
			this.color = color;
		}
		
		boolean equals(String color) {
			if (color.startsWith("#") && color.length() == 7) {
				Color col = new Color();
				try {
					col.setHex(color.substring(1));
				} catch (Exception ex) {
					return false;
				}
				return col.equals(this.color);
			}
			
			if (color.toLowerCase().startsWith("rgb(")) {
				color = color.substring(4).trim();
				color = color.substring(0, color.length() - 1).trim();
				String[] factors = color.split(",");
				
				if (factors.length != 3) {
					return false;
				}

				Color col = new Color();
				try {
					col.setRGB(Integer.valueOf(factors[0]), Integer.valueOf(factors[1]), Integer.valueOf(factors[2]));
				} catch (Exception ex) {
					return false;
				}
				
				return col.equals(this.color);
			}
			
			return false;
		}
	}
	
	public enum TextDecoration implements CssValue {
		NONE("none"), UNDERLINE("underline"), OVERLINE("overline"), LINE_THROUGH("line-through"), BLINK("blink");
		
		private String value;
		
		TextDecoration(String value) {
			this.value = value;
		}
		
		String getValue() {
			return value;
		}
	}
	
	public enum FontStyle implements CssValue {
		NORMAL("normal"), ITALIC("italic"), OBLIQUE("oblique");
		
		private String value;
		
		private FontStyle(String value) {
			this.value = value;
		}
		
		String getValue() {
			return value;
		}
	}
	
	public enum FontWeight implements CssValue {
		NORMAL(400, "normal"), BOLD(700, "bold"), V100(100, "100"), V200(200, "200"), V300(300, "300"), V400(400, "400"), V500(500, "500"), V600(600, "600"), V700(700, "700"), V800(800, "800"), V900(900, "900");
		
		private final int value;
		private final String name;
		
		FontWeight(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		int getValue() {
			return value;
		};
		
		public static FontWeight forName(String name) {
			for (FontWeight fontWeight: FontWeight.values()) {
				if (fontWeight.getName() != null && fontWeight.getName().toLowerCase().equals(name.toLowerCase())) {
					return fontWeight;
				}
			}
			
			return null;
		}
	}
	
	public enum CssStyle {
		
		FONT_WEIGHT("font-weight") {

			@Override
			public boolean hasValue(String value, CssValue cssValue) {
				
				FontWeight fontWeight = FontWeight.forName(value);

				Integer intValue = 0;

				if (fontWeight != null) {
					intValue = fontWeight.getValue();
				}
				
				if (intValue == 0) {
					try {
						intValue = Integer.valueOf(value);
					} catch (Exception ex) {
						return false;
					}
				}
				
				if (intValue == null || intValue == 0) {
					return false;
				}
				
				if (cssValue instanceof FontWeight) {
					return intValue.equals(((FontWeight) cssValue).getValue());
				}
				
				return false;
			}
		},
		
		FONT_STYLE("font-style") {

			@Override
			boolean hasValue(String value, CssValue cssValue) {
				if (cssValue instanceof FontStyle) {
					return value.toLowerCase().equals(((FontStyle) cssValue).getValue().toLowerCase());
				}
				return false;
			}
			
		},
		
		TEXT_DECORATION("text-decoration") {

			@Override
			boolean hasValue(String value, CssValue cssValue) {
				if (cssValue instanceof TextDecoration) {
					return value.toLowerCase().equals(((TextDecoration) cssValue).getValue().toLowerCase());
				}
				return false;
			}
			
		},
		
		COLOR("color") {

			@Override
			boolean hasValue(String value, CssValue cssValue) {
				if (cssValue instanceof ColorCssValue) {
					return ((ColorCssValue)cssValue).equals(value);
				}
				
				return false;
			}			
		},

		BACKGROUND_COLOR("background-color") {

			@Override
			boolean hasValue(String value, CssValue cssValue) {
				if (cssValue instanceof ColorCssValue) {
					return ((ColorCssValue)cssValue).equals(value);
				}
				
				return false;
			}			
		};

		private String name;
		
		private CssStyle(String name) {
			this.name = name;
		}
		
		String getName() {
			return name;
		}
		
		abstract boolean hasValue(String value, CssValue cssValue);
	}
	
	public boolean hasCssStyle(WebElement webElement, CssStyle style, CssValue cssValue) {
		String value = webElement.getCssValue(style.getName());
		return style.hasValue(value, cssValue);
	}
}