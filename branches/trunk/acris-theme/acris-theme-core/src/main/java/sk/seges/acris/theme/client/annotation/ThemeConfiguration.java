package sk.seges.acris.theme.client.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

@Target(TYPE)
//@Retention(RetentionPolicy.SOURCE)
public @interface ThemeConfiguration {
	String themeName();
}
