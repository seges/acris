/**
 * 
 */
package sk.seges.acris.reporting.client.panel.maintainance;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class ReportViewEvent extends GwtEvent<ReportViewHandler> {
	public static final Type<ReportViewHandler> TYPE = new Type<ReportViewHandler>();

	private final Widget widget;
	private final Long reportId;

	public ReportViewEvent(Widget widget, Long reportId) {
		super();
		this.widget = widget;
		this.reportId = reportId;
	}
	
	public Widget getWidget() {
		return widget;
	}
	
	public Long getReportId() {
		return reportId;
	}

	@Override
	protected void dispatch(ReportViewHandler handler) {
		handler.onChange(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ReportViewHandler> getAssociatedType() {
		return TYPE;
	}
}
