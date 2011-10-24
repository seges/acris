package sk.seges.acris.core.showcase.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ladislav.gazo
 */
public class OrderPanel extends Composite {
	interface OrderViewUiBinder extends UiBinder<Widget, OrderPanel> {}

	private static OrderViewUiBinder uiBinder = GWT.create(OrderViewUiBinder.class);

	private final List<Call> calls;
	
	@UiField
	protected Button milk;

	@UiField
	protected Button butter;

	@UiField
	protected Button sugar;

	public OrderPanel(List<Call> calls) {
		this.calls = calls;
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("milk")
	void handleMilk(ClickEvent e) {
		calls.add(new Call("Milk", 2.0));
	}

	@UiHandler("butter")
	void handleButter(ClickEvent e) {
		calls.add(new Call("Butter", 12.0));
	}
	
	@UiHandler("sugar")
	void handleSugar(ClickEvent e) {
		calls.add(new Call("Sugar", 4.9));
	}
}
