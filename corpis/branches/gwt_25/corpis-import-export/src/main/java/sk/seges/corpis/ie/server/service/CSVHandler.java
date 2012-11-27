/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;

/**
 * @author ladislav.gazo
 * 
 * @param <T>
 *            CSV entry object
 */
public interface CSVHandler<T extends CsvEntry, C extends RowBasedHandlerContext> {
	void setFieldToColumnMapping(Map<String, String> fieldToColumnMapping);

	List<ImportExportViolation> handle(C context, T entry, Set<String> fieldNames);
	
	ImportExportViolation hideAllProducts(C context);

	Class<T> getHandledCsvEntryClass();
	
	List<ImportExportViolation> checkRestrictions(C context, List<T> entries);
		
	ImportExportViolation postImportCleanup(C context);
}