package sk.seges.acris.theme.pap.specific;

import sk.seges.acris.theme.client.annotation.ThemeSupport;

public class ThemeContext {

	private String themeName;

	private ThemeSupport themeSupport;

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public ThemeSupport getThemeSupport() {
		return themeSupport;
	}

	public void setThemeSupport(ThemeSupport themeSupport) {
		this.themeSupport = themeSupport;
	}
}