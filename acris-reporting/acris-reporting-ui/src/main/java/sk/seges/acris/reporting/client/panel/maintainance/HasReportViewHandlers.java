/**
 * 
 */
package sk.seges.acris.reporting.client.panel.maintainance;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author ladislav.gazo
 */
public interface HasReportViewHandlers extends HasHandlers {
	HandlerRegistration addReportViewHandler(ReportViewHandler handler);
}
