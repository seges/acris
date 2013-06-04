/**
 * 
 */
package sk.seges.acris.scaffold.mvp;

import sk.seges.acris.scaffold.mvp.SlotPresenter.SlotDispay;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author ladislav.gazo
 *
 */
public class FlowSlotPanel extends Composite implements SlotDispay {
	private FlowPanel container;
	
	public FlowSlotPanel() {
		container = new FlowPanel();
		initWidget(container);
	}
	
	@Override
	public AcceptsOneWidget getNextSlot() {
		SimplePanel panel = new SimplePanel();
		container.add(panel);
		return panel;
	}

	@Override
	public AcceptsOneWidget getSlot(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
