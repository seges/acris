/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.corpis.ie.server.domain.CopyOfCSVHandlerContext;
import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * @author ladislav.gazo
 */
public abstract class CSVImportExportService {
	private static final String ENTRY_MAPPING_METHOD = "getMapping";
	private static final String FILE_NOT_FOUND = "fileNotFound";
	private static final Logger log = LoggerFactory.getLogger(CSVImportExportService.class);

	protected Map<String, CSVHandler<?, ?>> handlerMapping;

	protected abstract String detectFormat();

	protected abstract String getDestination(CopyOfCSVHandlerContext contextTemplate);

	protected abstract CopyOfCSVHandlerContext instantiateContext();

	public CSVImportExportService(Map<String, CSVHandler<?, ?>> handlerMapping) {
		super();
		this.handlerMapping = handlerMapping;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String> getCsvMapping(Class<? extends CsvEntry> clz) {
		try {
			Method method = clz.getMethod(ENTRY_MAPPING_METHOD, (Class<?>[]) null);
			return (Map<String, String>) method.invoke(null, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException("No static method " + ENTRY_MAPPING_METHOD + "() found in " + clz, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ImportExportViolation> importCSV(CopyOfCSVHandlerContext contextTemplate) {
		List<ImportExportViolation> violations = new ArrayList<ImportExportViolation>();

		CSVHandler handler = handlerMapping.get(detectFormat());

		CsvToBean csv = new CsvToBean();
		HeaderColumnNameTranslateMappingStrategy strat = new HeaderColumnNameTranslateMappingStrategy();
		Class handledCsvEntryClass = handler.getHandledCsvEntryClass();
		strat.setType(handledCsvEntryClass);
		strat.setColumnMapping(getCsvMapping(handledCsvEntryClass));

		String file = getDestination(contextTemplate);

		List<CsvEntry> entries = null;
		try {
			entries = csv.parse(strat, new BufferedReader(new FileReader(file)), ',');
		} catch (FileNotFoundException e) {
			log.warn("CSV File not found = " + file, e);
			violations.add(new ImportExportViolation(FILE_NOT_FOUND, file));
			return violations;
		}
		int i = 0;
		for (CsvEntry entry : entries) {
			CopyOfCSVHandlerContext newContext = instantiateContext();
			contextTemplate.injectInto(newContext);
			newContext.setRow(i);
			violations.addAll(handler.handle(newContext, entry));
			i++;
		}

		return violations;
	}
}
