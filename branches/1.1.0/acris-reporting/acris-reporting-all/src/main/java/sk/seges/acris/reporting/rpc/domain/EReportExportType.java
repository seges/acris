package sk.seges.acris.reporting.rpc.domain;

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
