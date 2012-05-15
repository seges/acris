/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import sk.seges.corpis.ie.server.domain.CsvEntry;

/**
 * @author ladislav.gazo
 */
public class ResourceBundleCsvEntryMappingLoader implements CsvEntryMappingLoader {
	private final String defaultLocale;
	
	public ResourceBundleCsvEntryMappingLoader(String defaultLocale) {
		super();
		assert defaultLocale != null;
		
		this.defaultLocale = defaultLocale;
	}

	@Override
	public Map<String,String> loadMapping(Class<? extends CsvEntry> entryClass) {
		assert entryClass != null;
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle(entryClass.getName(), new Locale(defaultLocale));
		Map<String, String> mapping = new HashMap<String, String>();
		for(String key : resourceBundle.keySet()) {
			mapping.put(resourceBundle.getString(key), key);
		}
		
		return mapping;
	}

	@Override
	public Map<String, String> loadFieldToColumnMapping(Class<? extends CsvEntry> entryClass) {
assert entryClass != null;
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle(entryClass.getName(), new Locale(defaultLocale));
		Map<String, String> mapping = new HashMap<String, String>();
		for(String key : resourceBundle.keySet()) {
			mapping.put(key, resourceBundle.getString(key));
		}
		
		return mapping;
	}
}
