/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;
import sk.seges.corpis.ie.shared.domain.ViolationConstants;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * @author ladislav.gazo
 */
public abstract class CSVImportExportService {
	private static final String ENTRY_MAPPING_METHOD = "getMapping";
	private static final Logger log = LoggerFactory.getLogger(CSVImportExportService.class);
	
	private static final String CUSTOM_SUFFIX = "_CUSTOM";

	protected Map<String, CSVHandler<?, ?>> handlerMapping;
	private final CsvEntryMappingLoader mappingLoader;

	protected abstract String detectFormat();

	protected abstract String getDestination(RowBasedHandlerContext contextTemplate);

	protected abstract RowBasedHandlerContext instantiateContext();
	
	protected char getCsvDelimiter() {
		return ',';
	}
	
	protected String getCsvEncoding() {
		return "UTF-8";
	}

	public CSVImportExportService(Map<String, CSVHandler<?, ?>> handlerMapping, CsvEntryMappingLoader mappingLoader) {
		super();
		this.handlerMapping = handlerMapping;
		this.mappingLoader = mappingLoader;
	}

	private void logViolations(List<ImportExportViolation> violations) {
		for (ImportExportViolation violation: violations) {
			log.error("I/E violation occured: " + toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ImportExportViolation> importCSV(RowBasedHandlerContext contextTemplate, Set<String> fieldNames) {
		List<ImportExportViolation> violations = new ArrayList<ImportExportViolation>();

		
		String format = detectFormat();
		if (format == null) {
			violations.add(new ImportExportViolation(ViolationConstants.WRONG_FORMAT));
			logViolations(violations);
			return violations;
		}
		
		CSVHandler handler = handlerMapping.get(format);

		Class handledCsvEntryClass = handler.getHandledCsvEntryClass();

		Map<String, String> fieldToColumnMapping = mappingLoader.loadFieldToColumnMapping(handledCsvEntryClass);
		handler.setFieldToColumnMapping(fieldToColumnMapping);
		
		String file = getDestination(contextTemplate);
		
		
		List<CsvEntry> entries  = null;
		
		if (format.toUpperCase().endsWith(CUSTOM_SUFFIX)) {
			entries = readCustomEntries(file, violations);
		} else {
			entries = readCsvEntries(file, handledCsvEntryClass, violations);
		}
		
		RowBasedHandlerContext restrContext = instantiateContext();
		contextTemplate.injectInto(restrContext);
		violations.addAll(handler.checkRestrictions(restrContext, entries));
		
		if (!violations.isEmpty()) {
			logViolations(violations);
			return violations;
		}
		
		if (fieldNames != null && fieldNames.contains("hide")) {
			RowBasedHandlerContext newContext = instantiateContext();
			contextTemplate.injectInto(newContext);
			handler.hideAllProducts(newContext);
		}
		
		// one for header and one for not starting at 0
		int i = 2;
		for (CsvEntry entry : entries) {
			RowBasedHandlerContext newContext = instantiateContext();
			contextTemplate.injectInto(newContext);
			newContext.setRow(i);
			try {
				List<ImportExportViolation> handle = handler.handle(newContext, entry, fieldNames);
				if (handle != null && handle.size() > 0) {
					for (ImportExportViolation violation: handle) {
						log.error("Unable to import entry: " + violation.toString());
					}
				}
				violations.addAll(handle);
			} catch (Exception ex) {
				log.error("Unable to import entry. ", ex);
			}
			
			i++;
		}
		
		RowBasedHandlerContext newContext = instantiateContext();
		contextTemplate.injectInto(newContext);
		handler.hideDeletedProducts(newContext);

		logViolations(violations);

		return violations;
	}
	
	private List<CsvEntry> readCsvEntries(String file, Class handledCsvEntryClass, List<ImportExportViolation> violations) {
		List<CsvEntry> entries = null;
		
		CsvToBean csv = new CsvToBean();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
		HeaderColumnNameTranslateMappingStrategy strat = new HeaderColumnNameTranslateMappingStrategy();
		strat.setType(handledCsvEntryClass);
		strat.setColumnMapping(mappingLoader.loadMapping(handledCsvEntryClass));
		
		try {
//			Reader in = new BufferedReader(new FileReader(file));
			Reader in = new InputStreamReader(new FileInputStream(file), getCsvEncoding());
			entries = csv.parse(strat, in, getCsvDelimiter());
		} catch (FileNotFoundException e) {
			log.warn("CSV File not found = " + file, e);
			violations.add(new ImportExportViolation(ViolationConstants.FILE_NOT_FOUND, file));
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported encoding = " + file, e);
			violations.add(new ImportExportViolation(ViolationConstants.UNSUPPORTED_ENCODING, file));
		}
		return entries;
	}
	
	
	/**
	 * Dummy method, should be overridden for custom import 
	 * 
	 * @param file
	 * @param violations
	 * @return
	 */
	protected List<CsvEntry> readCustomEntries(String file, List<ImportExportViolation> violations) {
		return null;
	}
	
	
	
	
}
