package sk.seges.acris.widget.showcase.dynamicui;

import sk.seges.acris.widget.client.Hyperlink;
import sk.seges.acris.widget.client.uibinder.DynamicUiBinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class DynamicallyBoundPanel extends Composite {
	interface DynamicallyBoundPanelUiBinder extends DynamicUiBinder<Widget, DynamicallyBoundPanel> {}

	private static final DynamicallyBoundPanelUiBinder binder = GWT.create(DynamicallyBoundPanelUiBinder.class);

	@UiField
	protected Label message;

	// this is acris-widget Hyperlink because GWT's misses wrap method!
	@UiField
	protected Hyperlink description;

	@UiField
	protected TextBox ashtarShips;

	@UiField
	protected TextBox ptahShips;

	@UiField
	protected Button recalculateFleet;

	public DynamicallyBoundPanel() {
		// load the template e.g. from service ...
		String htmlTemplate = "<div class=\"dyn-panel\"><div ui:field=\"message\" class=\"dyn-message\">Status messages are shown here</div><div class=\"fleet-container\"><div ui:field=\"description\" class=\"dyn-description\"></div><input type=\"text\" ui:field=\"ashtarShips\" /><input type=\"text\" ui:field=\"ptahShips\" /><button ui:field=\"recalculateFleet\">Recalculate fleet</button</div></div>";
		binder.setViewTemplate(htmlTemplate);

		// known from GWT UiBinder - initialize
		initWidget(binder.createAndBindUi(this));

		// GWT's hyperlink is represented by "a" tag wrapped in "div". After
		// binding the link, we have no other access to the link's text then
		// using its methods:
		description.setText("For more info, click here");

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				message.setText("You accessed " + event.getValue() + ". Thank you for your interest in AcrIS. For more information, please visit http://acris.googlecode.com");
			}
		});
		
		recalculateFleet.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					Integer aShips = Integer.valueOf(ashtarShips.getValue());
					Integer pShips = Integer.valueOf(ptahShips.getValue());

					if (aShips > pShips) {
						description.setTargetHistoryToken("ashtarHasMajority");
					} else {
						description.setTargetHistoryToken("ptahWillJoin");
					}

					message.setText("Fleet has " + (aShips + pShips) + " space ships !!!");
				} catch (NumberFormatException e) {
					message.setText("Error: " + e.getMessage());
				}
			}
		});		
	}
}
