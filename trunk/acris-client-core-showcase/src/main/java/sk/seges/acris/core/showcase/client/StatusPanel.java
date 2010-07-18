/**
 * 
 */
package sk.seges.acris.core.showcase.client;

import sk.seges.acris.core.client.component.semaphore.Semaphore;
import sk.seges.acris.core.client.component.semaphore.SemaphoreEvent;
import sk.seges.acris.core.client.component.semaphore.SemaphoreListener;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * @author ladislav.gazo
 */
public class StatusPanel extends Composite {
	private final FlowPanel container;
	private final Semaphore semaphore;

	public StatusPanel(Semaphore semaphore) {
		this.semaphore = semaphore;

		CaptionPanel group = new CaptionPanel("Status");
		container = new FlowPanel();
		group.add(container);
		initWidget(group);

		raise(0);

		semaphore.addListener(new SemaphoreListener() {
			@Override
			public void change(SemaphoreEvent event) {
				addStatus("step");
				if (event.getCount() == event.getStates()[0] + event.getStates()[1]) {
					if(event.getStates()[1] > 0) {
						// we have failures
						addStatus("failure");
					} else {
						addStatus("success");
					}
				}
			}
		});
	}

	public void raise(int count) {
		semaphore.clear();
		semaphore.raise(count);
		
		container.clear();
		container.add(new Label("No. of ordered items: " + count));
	}
	
	private void addStatus(String style) {
		Label label;
		if(style.equals("step")) {
			label = new Label(" ");
		} else {
			label = new Label(style);
		}
		
		label.setStyleName(style);
		container.add(label);
	}
}
