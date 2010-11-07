package sk.seges.acris.reporting.client.panel.maintainance;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.acris.reporting.rpc.service.IReportDescriptionServiceAsync;
import sk.seges.acris.widget.client.table.BeanTableSpec;
import sk.seges.acris.widget.client.table.SpecColumn;
import sk.seges.acris.widget.client.table.SpecLoader;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

@SpecLoader(serviceChocolate = IReportDescriptionServiceAsync.REPORT_DESCRIPTION_SERVICE_NAME, serviceClass = IReportDescriptionServiceAsync.class, loaderMethodName = IReportDescriptionServiceAsync.FIND_ALL_REPORTS_METHOD)
public interface IReportTableSpec extends BeanTableSpec<ReportDescription> {

	@SpecColumn(filterWidgetType = TextBox.class, filterOperation = "ilike", field = ReportDescription.NAME_ATTR)
	void name();
	
	@SpecColumn(filterWidgetType = DateBox.class, filterOperation = "eq", field = ReportDescription.CREATION_DATE_ATTR)
	void creationDate();
}
