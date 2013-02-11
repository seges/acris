/**
 * 
 */
package sk.seges.corpis.ie.server.domain;

import java.util.Map;

/**
 * @author ladislav.gazo
 */
public class CsvEntryMapping {
	private Map<String, String> mapping;

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
}
