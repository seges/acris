package sk.seges.acris.reporting.client.panel.parameter;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public abstract class AbstractTypePanel<T> extends Composite implements IParameterTypePanel<T> {
	
	protected FlowPanel container = GWT.create(FlowPanel.class);
	
	private Image loadingImage = new Image("scrollTableLoading.gif");
	protected SimplePanel imagePanel;

	private ReportParameterData reportParameter;
	
	public AbstractTypePanel() {
		initWidget(container);
	}

	public void init(ReportParameterData reportParameter) {
		this.setReportParameter(reportParameter);
		init();
	}
	
	protected void init() {
		imagePanel = new SimplePanel();
		imagePanel.add(loadingImage);
		container.clear();
		initOwnComponents();
	}
	
	protected abstract void initOwnComponents();

	public void setReportParameter(ReportParameterData reportParameter) {
		this.reportParameter = reportParameter;
	}

	@Override
	public ReportParameterData getReportParameter() {
		return reportParameter;
	}
}
