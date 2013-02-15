package sk.seges.acris.theme.client.annotation;

public @interface ThemeElements {

	ThemeElement[] value() default {};

	public @interface ThemeElement {
		
		boolean provided() default false;

		String value();	
	}
}
