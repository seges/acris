package sk.seges.acris.reporting.client.panel.parameter;

import java.util.Arrays;

import sk.seges.acris.reporting.shared.domain.api.EReportExportType;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

public class ExportFileTypePanel extends AbstractTypePanel<String> {

	private EnumListBoxWithValue<EReportExportType> exportTypeListBox;

	String fileType;
	
	@Override
	protected void initOwnComponents() {
		exportTypeListBox = GWT.create(EnumListBoxWithValue.class);
		exportTypeListBox.setClazz(EReportExportType.class);
		exportTypeListBox.load(Arrays.asList(EReportExportType.values()));
		exportTypeListBox.removeItem(0);
		exportTypeListBox.setSelectedIndex(0);
		container.add(exportTypeListBox);
		exportTypeListBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				fileType = exportTypeListBox.getValue().name();
			}
		});
	}

	@Override
	public String getValue() {
		if (fileType == null || fileType.length() <= 0) {
			return exportTypeListBox.getValue().name();
		}
		return fileType;
	}

	@Override
	public void setValue(String t) {
		try {
			EReportExportType exportType = EReportExportType.valueOf(EReportExportType.class, t);
			exportTypeListBox.setValue(exportType);
			fileType = t;
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
