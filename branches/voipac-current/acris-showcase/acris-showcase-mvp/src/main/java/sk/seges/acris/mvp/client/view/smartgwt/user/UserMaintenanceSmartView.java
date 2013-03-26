package sk.seges.acris.mvp.client.view.smartgwt.user;

import java.util.List;

import sk.seges.acris.mvp.client.i18n.UserMessages;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter.UserMaintenanceDisplay;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataMetaModel;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTOMetaModel;
import sk.seges.acris.showcase.client.model.ModelAdapter;
import sk.seges.acris.showcase.client.view.core.adapter.smartgwt.SmartViewImpl;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class UserMaintenanceSmartView extends SmartViewImpl implements UserMaintenanceDisplay {

	private final VLayout container;
	
	private UserMessages messages = GWT.create(UserMessages.class);
	
	private ModelAdapter modelAdapter;
	private ListGrid grid;
	
	@Inject
	public UserMaintenanceSmartView(ModelAdapter modelAdapter) {
		this.modelAdapter = modelAdapter;
		
		container = new VLayout();
		container.setWidth("95%");
		container.setHeight("90%");

		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight("80%");
		ListGridField userNameField = new ListGridField(UserDataMetaModel.USERNAME, messages.userName());
		ListGridField descriptionField = new ListGridField(GenericUserDTOMetaModel.DESCRIPTION, messages.identifier());
		
		grid.setFields(new ListGridField[] {userNameField, descriptionField});

		HLayout buttonsPanel = new HLayout();
		buttonsPanel.setPadding(10);
		buttonsPanel.setWidth100();
		buttonsPanel.setHeight("10%");
		Button newUserButton = new Button(messages.newUser());
		buttonsPanel.setMembers(newUserButton);

		container.setMembers(grid, buttonsPanel);
		container.draw();
	}
	
	@Override
	public Widget asWidget() {
		return container;
	}

	@Override
	public void setData(PagedResult<List<UserData<?>>> users) {
		ListGridRecord[] records = modelAdapter.convertDataForGrid(users.getResult());
		grid.setData(records);
	}
}