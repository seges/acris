package sk.seges.acris.generator.client;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public class ContentNavigator {

	private static final String LOCALE_IDENTIFIER = "?locale=";

	public ContentNavigator(EntryPoint entryPoint) {
	}
	
	public void navigateToToken(final GeneratorToken token) {

		String currentHref = Window.Location.getHref();

		int index = currentHref.indexOf(LOCALE_IDENTIFIER);

		if (index != -1) {
			//TODO locale support
			String oldLocale = currentHref.substring(index + LOCALE_IDENTIFIER.length(), index
					+ LOCALE_IDENTIFIER.length() + 2);

			if (!oldLocale.equals(token.getLanguage())) {
				String newHref = currentHref.substring(0, index + LOCALE_IDENTIFIER.length())
						+ token.getLanguage();

				if (currentHref.length() > index + LOCALE_IDENTIFIER.length() + 2) {
					newHref += currentHref.substring(index + LOCALE_IDENTIFIER.length() + 2);
				}

				Window.Location.replace(newHref);
			}
		} else {
			History.newItem(token.getToken());
		}
	}
}
