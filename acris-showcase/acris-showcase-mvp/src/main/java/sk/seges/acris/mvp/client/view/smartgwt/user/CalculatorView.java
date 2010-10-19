package sk.seges.acris.mvp.client.view.smartgwt.user;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VLayout;

import sk.seges.acris.mvp.client.view.core.adapter.smartgwt.SmartViewImpl;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;


public class CalculatorView extends SmartViewImpl {

	private VLayout container;
	
	public CalculatorView() {
		container = new VLayout();
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setSize("407px", "261px");
		container.addMember(absolutePanel);
		
		Button button_1 = new Button("1");
		button_1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				
			}
		});
		absolutePanel.add(button_1, 10, 10);
		button_1.setSize("47px", "24px");
		
		Button button = new Button("2");
		button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
			}
		});
		absolutePanel.add(button, 63, 10);
		button.setSize("47px", "24px");
		
		Button button_2 = new Button("3");
		button_2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
			}
		});
		absolutePanel.add(button_2, 116, 10);
		button_2.setSize("47px", "24px");
		
		Button button_3 = new Button("4");
		absolutePanel.add(button_3, 10, 40);
		button_3.setSize("47px", "24px");
		
		Button button_4 = new Button("5");
		absolutePanel.add(button_4, 63, 40);
		button_4.setSize("47px", "24px");
		
		Button button_5 = new Button("6");
		absolutePanel.add(button_5, 116, 40);
		button_5.setSize("47px", "24px");
		
		Button button_6 = new Button("7");
		absolutePanel.add(button_6, 10, 70);
		button_6.setSize("47px", "24px");
		
		Button button_7 = new Button("8");
		absolutePanel.add(button_7, 63, 70);
		button_7.setSize("47px", "24px");
		
		Button button_8 = new Button("9");
		absolutePanel.add(button_8, 116, 70);
		button_8.setSize("47px", "24px");
		
		Button button_9 = new Button("0");
		absolutePanel.add(button_9, 63, 100);
		button_9.setSize("47px", "24px");
		
		Button button_10 = new Button("+");
		absolutePanel.add(button_10, 211, 10);
		button_10.setSize("47px", "24px");
		
		Button button_11 = new Button("-");
		absolutePanel.add(button_11, 211, 40);
		button_11.setSize("47px", "24px");
		
		Button button_12 = new Button("/");
		absolutePanel.add(button_12, 211, 70);
		button_12.setSize("47px", "24px");
		
		Button button_13 = new Button("=");
		absolutePanel.add(button_13, 211, 100);
		button_13.setSize("47px", "24px");
		
		Label lblUsername = new Label("Username:");
		absolutePanel.add(lblUsername, 48, 194);
		lblUsername.setSize("57px", "14px");
		
		TextBox textBox = new TextBox();
		absolutePanel.add(textBox, 111, 186);
		
		Label lblPassword = new Label("Password");
		absolutePanel.add(lblPassword, 48, 216);
		
		PasswordTextBox passwordTextBox = new PasswordTextBox();
		absolutePanel.add(passwordTextBox, 111, 214);
		
		com.smartgwt.client.widgets.Button btnSubmit = new com.smartgwt.client.widgets.Button("Submit");
		absolutePanel.add(btnSubmit, 264, 214);
	}
	
	@Override
	public Widget asWidget() {
		return container;
	}
}
