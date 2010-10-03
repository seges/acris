package sk.seges.acris.mvp.client.view.smartgwt.user;

import sk.seges.acris.mvp.client.event.LoginEvent;
import sk.seges.acris.mvp.client.event.LoginEvent.LoginEventHandler;
import sk.seges.acris.mvp.client.form.smartgwt.LoginSmartForm;
import sk.seges.acris.mvp.client.model.ModelAdapter;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginDisplay;
import sk.seges.acris.mvp.client.view.core.adapter.smartgwt.SmartViewImpl;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;


public class LoginSmartView extends SmartViewImpl implements LoginDisplay {

	private final VLayout panelcontainer;

	private final LoginSmartForm loginForm;
	private final Button submit;
	
	@Inject
	public LoginSmartView(ModelAdapter modelAdapter) {
		panelcontainer = new VLayout();
		panelcontainer.setWidth("95%");
		panelcontainer.setHeight("95%");
		panelcontainer.setAlign(VerticalAlignment.CENTER);
		
		HLayout container = new HLayout();

		panelcontainer.setMembers(container);
		
		container.setWidth("95%");
		container.setHeight("95%");
		container.setAlign(Alignment.CENTER);

		SectionStack sectionStack = new SectionStack();
		sectionStack.setWidth(320);
		sectionStack.setHeight(140);
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setOverflow(Overflow.HIDDEN);

		VLayout navLayout = new VLayout();

		SectionStackSection summarySection = new SectionStackSection();
		summarySection.setTitle("Login");
		summarySection.setExpanded(true);
		summarySection.setItems(navLayout);

		sectionStack.setSections(summarySection);

		loginForm = GWT.create(LoginSmartForm.class);
		loginForm.setBean(new GenericUserDTO());

		submit = new Button();
		submit.setTitle("Submit");
		submit.setWidth(80);

		navLayout.setMembers(loginForm, submit);

		container.setMembers(sectionStack);
		
		initializeHandlers();
	}
	
	private void initializeHandlers() {
		submit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LoginEvent loginEvent = new LoginEvent(loginForm.getBean());
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
