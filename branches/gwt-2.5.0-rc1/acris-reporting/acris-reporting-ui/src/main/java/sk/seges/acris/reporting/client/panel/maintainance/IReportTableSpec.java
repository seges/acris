package sk.seges.acris.reporting.client.panel.maintainance;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.dto.ReportDescriptionDTO;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.acris.widget.client.table.BeanTableSpec;
import sk.seges.acris.widget.client.table.FreeSpecLoader;
import sk.seges.acris.widget.client.table.SpecColumn;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

@FreeSpecLoader(serviceClass = IReportDescriptionService.class, serviceEntryPoint="showcase-service/reportDescriptionService", loaderMethodName = IReportDescriptionService.FIND_ALL_REPORTS_METHOD)
public interface IReportTableSpec extends BeanTableSpec<ReportDescriptionDTO> {

	@SpecColumn(filterWidgetType = TextBox.class, filterOperation = "ilike", field = ReportDescriptionData.NAME_ATTR)
	void name();
	
	@SpecColumn(filterWidgetType = DateBox.class, filterOperation = "eq", field = ReportDescriptionData.CREATION_DATE_ATTR)
	void creationDate();
}
