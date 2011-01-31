/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;
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
	private final CsvEntryMappingLoader mappingLoader;

	protected abstract String detectFormat();

	protected abstract String getDestination(RowBasedHandlerContext contextTemplate);

	protected abstract RowBasedHandlerContext instantiateContext();

	public CSVImportExportService(Map<String, CSVHandler<?, ?>> handlerMapping, CsvEntryMappingLoader mappingLoader) {
		super();
		this.handlerMapping = handlerMapping;
		this.mappingLoader = mappingLoader;
	}

	@SuppressWarnings("unchecked")
	public List<ImportExportViolation> importCSV(RowBasedHandlerContext contextTemplate) {
		List<ImportExportViolation> violations = new ArrayList<ImportExportViolation>();

		CSVHandler handler = handlerMapping.get(detectFormat());

		CsvToBean csv = new CsvToBean();
		HeaderColumnNameTranslateMappingStrategy strat = new HeaderColumnNameTranslateMappingStrategy();
		Class handledCsvEntryClass = handler.getHandledCsvEntryClass();
		strat.setType(handledCsvEntryClass);
		strat.setColumnMapping(mappingLoader.loadMapping(handledCsvEntryClass));

		Map<String, String> fieldToColumnMapping = mappingLoader.loadFieldToColumnMapping(handledCsvEntryClass);
		handler.setFieldToColumnMapping(fieldToColumnMapping);
		
		String file = getDestination(contextTemplate);

		List<CsvEntry> entries = null;
		try {
			entries = csv.parse(strat, new BufferedReader(new FileReader(file)), ',');
		} catch (FileNotFoundException e) {
			log.warn("CSV File not found = " + file, e);
			violations.add(new ImportExportViolation(FILE_NOT_FOUND, file));
			return violations;
		}
		
		// one for header and one for not starting at 0
		int i = 2;
		for (CsvEntry entry : entries) {
			RowBasedHandlerContext newContext = instantiateContext();
			contextTemplate.injectInto(newContext);
			newContext.setRow(i);
			violations.addAll(handler.handle(newContext, entry));
			i++;
		}

		return violations;
	}
}
