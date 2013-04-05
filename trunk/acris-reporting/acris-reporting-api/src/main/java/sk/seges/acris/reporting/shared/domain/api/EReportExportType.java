package sk.seges.acris.reporting.shared.domain.api;

/**
 * types of exported report - same as in jasperserver
 * @author marta
 *
 */
public enum EReportExportType {
//TODO	HTML, 
	PDF, XLS, CSV, DOCX, RTF, SWF, ODT, ODS, XLSX;
	
	public String getName() {
		return this.name().toLowerCase();
	}
}
