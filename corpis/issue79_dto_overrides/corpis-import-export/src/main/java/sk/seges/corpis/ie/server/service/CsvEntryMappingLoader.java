/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.Map;

import sk.seges.corpis.ie.server.domain.CsvEntry;

/**
 * @author ladislav.gazo
 */
public interface CsvEntryMappingLoader {
	Map<String,String> loadMapping(Class<? extends CsvEntry> entryClass);
	Map<String,String> loadFieldToColumnMapping(Class<? extends CsvEntry> entryClass);
}
