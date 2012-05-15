/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.Map;

import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;

/**
 * @author ladislav.gazo
 */
public abstract class AbstractCSVHandler<T extends CsvEntry, C extends RowBasedHandlerContext> implements CSVHandler<T, C> {
	private Map<String, String> fieldToColumnMapping;
	
	@Override
	public void setFieldToColumnMapping(Map<String, String> fieldToColumnMapping) {
		this.fieldToColumnMapping = fieldToColumnMapping;
	}

	protected String getColumnName(String field) {
		if(fieldToColumnMapping == null) {
			return field;
		}
		return fieldToColumnMapping.get(field);
	}
}
