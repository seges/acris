/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.List;

import sk.seges.corpis.ie.server.domain.CopyOfCSVHandlerContext;
import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;

/**
 * @author ladislav.gazo
 *
 * @param <T> CSV entry object
 */
public interface CSVHandler<T extends CsvEntry, C extends CopyOfCSVHandlerContext> {
	List<ImportExportViolation> handle(C context, T entry);

	Class<T> getHandledCsvEntryClass();
}