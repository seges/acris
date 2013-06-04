package sk.seges.sesam.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class for accessing localized messages based on either object, class or
 * string-represented bundle taking into account either default or specified
 * locale.
 * 
 * @author eldzi
 */
public final class Messages {
	private Messages() {
	}

	public static String getString(Object source, String key) {
		return getString(source.getClass(), key);
	}

	public static String getString(Object source, String key, Locale locale) {
		return getString(source.getClass(), key, locale);
	}
	
	public static String getString(Class<?> source, String key) {
		return getString(source, key, null);
	}

	public static String getString(Class<?> source, String key, Locale locale) {
		return getString(source.getName(), key, locale);
	}

	public static String getString(String bundle, String key) {
		return getString(bundle, key, null);
	}

	public static String getString(String bundle, String key, Locale locale) {
		ResourceBundle rb;
		if (locale != null)
			rb = ResourceBundle.getBundle(bundle, locale);
		else
			rb = ResourceBundle.getBundle(bundle);

		try {
			return rb.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
