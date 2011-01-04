/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.util.List;

import sk.seges.corpis.ie.server.domain.CSVHandlerContext;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;

/**
 * @author ladislav.gazo
 *
 * @param <T> CSV entry object
 */
public interface CSVHandler<T> {
	List<ImportExportViolation> handle(CSVHandlerContext context, T entry);
}