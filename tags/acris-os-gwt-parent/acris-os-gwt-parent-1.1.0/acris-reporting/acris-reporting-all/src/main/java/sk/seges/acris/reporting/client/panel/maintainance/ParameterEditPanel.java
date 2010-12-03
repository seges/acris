package sk.seges.acris.reporting.client.panel.maintainance;

import java.util.Arrays;

import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.acris.reporting.shared.domain.api.EReportParameterType;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class ParameterEditPanel extends Composite {
	
	private ReportingMessages reportingMessages = GWT.create(ReportingMessages.class);	
	
	private EnumListBoxWithValue<EReportParameterType> paramTypeListBox = new EnumListBoxWithValue<EReportParameterType>(EReportParameterType.class);
	private TextBox paramDisplayedNameTextBox = GWT.create(TextBox.class);
	private TextBox paramNameTextBox = GWT.create(TextBox.class);
	private TextBox parentIdTextBox = GWT.create(TextBox.class);
	private CheckBox paramHiddenCheckBox = GWT.create(CheckBox.class);
	private TextArea paramDescTextArea = GWT.create(TextArea.class);
	private FlowPanel container = GWT.create(FlowPanel.class);
	private TextBox paramOrderNumber = GWT.create(TextBox.class);
	private Label paramIdLabel;
	private Long paramId = null;
	private Long parentParamId = null;

	public ParameterEditPanel() {
		initWidget(container);
	}
	
	public void initComponents() {
		container.clear();
		paramTypeListBox.load(Arrays.asList(EReportParameterType.values()));
		paramTypeListBox.removeItem(0);
		FlexTable flexT = new FlexTable();
		Label paramNameLabel = new Label(reportingMessages.name());
		flexT.setWidget(0, 0, paramNameLabel);
		flexT.getCellFormatter().setWidth(0, 0, ReportEditPanel.LABEL_WIDTH);
		paramNameTextBox.setWidth(ReportEditPanel.TEXTBOX_WIDTH);
		flexT.setWidget(0, 1, paramNameTextBox);
		paramIdLabel = new Label("ParamId");
		flexT.setWidget(0, 2, paramIdLabel);
		flexT.getFlexCellFormatter().setColSpan(0, 2, 4);

		Label paramDisplayedNameLabel = new Label(reportingMessages.parameterDisplayedName());
		flexT.setWidget(1, 0, paramDisplayedNameLabel);
		paramDisplayedNameTextBox.setWidth(ReportEditPanel.TEXTBOX_WIDTH);
		flexT.setWidget(1, 1, paramDisplayedNameTextBox);
		Label paramHiddenLabel = new Label(reportingMessages.parameterHidden());
		flexT.setWidget(1, 2, paramHiddenLabel);
		paramHiddenCheckBox.setValue(false);
		flexT.setWidget(1, 3, paramHiddenCheckBox);
		flexT.getFlexCellFormatter().setColSpan(1, 3, 2);

		Label parentParamIdLabel = new Label("parent id");
		flexT.setWidget(2, 0, parentParamIdLabel);
		parentIdTextBox.setWidth(ReportEditPanel.TEXTBOX_WIDTH);
		flexT.setWidget(2, 1, parentIdTextBox);
		
		Label orderNumber = new Label("Order nr.");
		flexT.setWidget(3, 0, orderNumber);
		flexT.setWidget(3, 1, paramOrderNumber);
		
		Label paramTypeLabel = new Label(reportingMessages.parameterType());
		flexT.setWidget(4, 0, paramTypeLabel);
		flexT.setWidget(4, 1, paramTypeListBox);
		
		
		Label paramDescLabel = new Label(reportingMessages.description());
		flexT.setWidget(5, 0, paramDescLabel);
		paramDescTextArea.setWidth(ReportEditPanel.TEXTAREA_WIDTH);
		flexT.setWidget(5, 1, paramDescTextArea);
		flexT.getFlexCellFormatter().setColSpan(5, 1, 5);

		container.add(flexT);
	}

	public void initComponents(ReportParameter param) {
		initComponents();
		if (param == null)
			return;
		paramId = param.getId();
		setParentParamId((param.getParent() == null) ? null : param.getParent().getId());
		parentIdTextBox.setText(parentParamId == null ? "" : String.valueOf(parentParamId));
		setParamType(param.getClassName());
		paramIdLabel.setText("ParamId: "+paramId);
		paramNameTextBox.setText(param.getName());
		paramDisplayedNameTextBox.setText(param.getDisplayedName());
		paramDescTextArea.setText(param.getDescription());
		paramHiddenCheckBox.setValue((param.getHidden()==null) ? false : param.getHidden());
		paramOrderNumber.setText((param.getOrderNumber()==null) ? "" : String.valueOf(param.getOrderNumber()));
	}
	
	public ReportParameter getReportParameter() {
		ReportParameter param = new ReportParameter();
		param.setId(paramId);
		param.setName(paramNameTextBox.getText());
		if (!parentIdTextBox.getText().isEmpty()) {
			ReportParameter parent = new ReportParameter();
			try { 
				parent.setId(Long.valueOf(parentIdTextBox.getValue()));
				param.setParent(parent);
			} catch (Exception e) {
			}
		}
		param.setDisplayedName(paramDisplayedNameTextBox.getText());
		param.setDescription(paramDescTextArea.getText());
		param.setClassName(getParamType().name());
		param.setHidden(paramHiddenCheckBox.getValue());
		if(!paramOrderNumber.getText().isEmpty()) {
			param.setOrderNumber(Integer.valueOf(paramOrderNumber.getValue()));
		}
		return param;
	}
	
	public Enum<?> getParamType() {
		Enum<?> value = paramTypeListBox.getValue();
		return value;
	}

	public void setParamType(String className) {
		paramTypeListBox.setValue(paramTypeListBox.getEnumValue(className));
	}

	public void setParentParamId(Long parentParamId) {
		this.parentParamId = parentParamId;
	}

	public Long getParentParamId() {
		return parentParamId;
	}


}
