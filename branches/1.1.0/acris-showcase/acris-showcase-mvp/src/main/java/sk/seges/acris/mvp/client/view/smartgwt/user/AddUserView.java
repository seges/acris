package sk.seges.acris.mvp.client.view.smartgwt.user;

import sk.seges.acris.mvp.client.view.core.adapter.smartgwt.SmartViewImpl;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.widgets.layout.VLayout;
import com.google.gwt.user.client.ui.TabPanel;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;


public class AddUserView extends SmartViewImpl {

	private final VLayout panelcontainer;
	private DynamicForm groupsForm;

	@Inject
	public AddUserView() {
		panelcontainer = new VLayout();
		
		TabSet tabSet = new TabSet();
		
		Tab generalTab = new Tab("General");
		tabSet.addTab(generalTab);
		
		Tab contactsTab = new Tab("Contacts");
		tabSet.addTab(contactsTab);
		
		Tab groupsTab = new Tab("Groups");
		
		groupsForm = new DynamicForm();
		groupsForm.setSize("100%", "35px");
		groupsForm.setFields(new FormItem[] { new SectionItem("newSectionItem_2", "Groups_assignment")});
		groupsTab.setPane(groupsForm);
		tabSet.addTab(groupsTab);
		panelcontainer.addMember(tabSet);
	}
	
	@Override
 	public Widget asWidget() {
		return panelcontainer;
	}
	public DynamicForm getGroupsForm() {
		return groupsForm;
	}
}