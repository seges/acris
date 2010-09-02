package sk.seges.acris.mvp.client.view.smartgwt.user;

import sk.seges.acris.mvp.client.event.LoginEvent;
import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;
import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginDisplay;
import sk.seges.acris.mvp.client.view.core.adapter.smartgwt.SmartViewImpl;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;


public class LoginSmartView extends SmartViewImpl implements LoginDisplay {

	private final HLayout panelcontainer;

	private TextItem username;
	private PasswordItem password;
	private ButtonItem submit;
	
	@Inject
	public LoginSmartView(ModelAdapter modelAdapter) {
		panelcontainer = new HLayout();
		panelcontainer.setWidth100();
		panelcontainer.setHeight100();
		panelcontainer.setAlign(Alignment.CENTER);
		
		VLayout container = new VLayout();

		panelcontainer.setMembers(container);
		
		container.setWidth100();
		container.setHeight100();
		container.setAlign(VerticalAlignment.CENTER);

		SectionStack sectionStack = new SectionStack();
		sectionStack.setWidth(320);
		sectionStack.setHeight(140);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setOverflow(Overflow.HIDDEN);

		DynamicForm form = new DynamicForm();

		HLayout navLayout = new HLayout();
		navLayout.setMembers(form);

		SectionStackSection summarySection = new SectionStackSection();
		summarySection.setTitle("Login");
		summarySection.setExpanded(true);
		summarySection.setItems(navLayout);

		sectionStack.setSections(summarySection);

		form.setAutoFocus(true);
		form.setNumCols(3);
		form.setWidth(300);

		username = new TextItem();
		username.setTitle("Username");
		username.setSelectOnFocus(true);
		username.setStartRow(true);

		password = new PasswordItem();
		password.setTitle("Password");
		password.setWrapTitle(false);
		password.setStartRow(true);

		submit = new ButtonItem();
		submit.setTitle("Submit");
		submit.setStartRow(false);
		submit.setWidth(80);

		form.setFields(new SpacerItem(), username, password, submit);

		container.setMembers(sectionStack);
		
		initializeHandlers();
	}
	
	private void initializeHandlers() {
		submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LoginEvent loginEvent = new LoginEvent((String)username.getValue(), (String)password.getValue());
				fireEvent(loginEvent);
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return panelcontainer;
	}

	@Override
	public HandlerRegistration addLoginHandler(LoginEventHandler handler) {
		return addHandler(handler, LoginEvent.getType());
	}

}
