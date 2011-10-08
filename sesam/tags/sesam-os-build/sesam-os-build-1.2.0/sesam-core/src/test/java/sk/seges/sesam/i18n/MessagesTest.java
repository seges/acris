package sk.seges.sesam.i18n;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * @author eldzi
 */
public class MessagesTest {

	@Test
	public void testGetStringObjectString() {
		String message = Messages.getString(this, "test.key");
		assertDefault(message);
	}

	@Test
	public void testGetStringClassOfQString() {
		String message = Messages.getString(MessagesTest.class, "test.key");
		assertDefault(message);
	}

	@Test
	public void testGetStringClassOfQStringLocale() {
		String message = Messages.getString(MessagesTest.class, "test.key", new Locale("sk","SK"));
		assertEquals("Slovensky klucik bez diakritiky", message);
		message = Messages.getString(MessagesTest.class, "test.key", new Locale("en","US"));
		assertEquals("General english key", message);
		message = Messages.getString(MessagesTest.class, "test.key", new Locale("en"));
		assertEquals("General english key", message);
		message = Messages.getString(MessagesTest.class, "test.key", new Locale("en","GB"));
		assertEquals("British key", message);
	}

	@Test
	public void testGetStringStringString() {	
		String message = Messages.getString("sk.seges.sesam.i18n.messages", "test.key");
		assertDefault(message);
	}

	private void assertDefault(String message) {
		String lang = System.getProperty("user.language");
		String country = System.getProperty("user.country");
		
		if("sk".equals(lang.toLowerCase()))
			assertEquals("Slovensky klucik bez diakritiky", message);
		else if("en".equals(lang.toLowerCase()) && "GB".equals(country.toLowerCase()))
			assertEquals("British key", message);
		else if("en".equals(lang.toLowerCase()))
			assertEquals("General english key", message);

	}
	
	@Test
	public void testGetStringStringStringLocale() {
		String message = Messages.getString("sk.seges.sesam.i18n.messages", "test.key", new Locale("sk","SK"));
		assertEquals("Slovensky klucik bez diakritiky", message);
		message = Messages.getString("sk.seges.sesam.i18n.messages", "test.key", new Locale("en","US"));
		assertEquals("General english key", message);
		message = Messages.getString("sk.seges.sesam.i18n.messages", "test.key", new Locale("en"));
		assertEquals("General english key", message);
		message = Messages.getString("sk.seges.sesam.i18n.messages", "test.key", new Locale("en","GB"));
		assertEquals("British key", message);
	}

}
