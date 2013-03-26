/**
 * 
 */
package sk.seges.acris.scaffold.mvp;

import sk.seges.acris.mvp.AbstractDisplayAwareActivity;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ladislav.gazo
 *
 */
public class SlotPresenter extends AbstractDisplayAwareActivity<SlotPresenter.SlotDispay> {
	public interface SlotDispay extends IsWidget {
		AcceptsOneWidget getNextSlot();
		AcceptsOneWidget getSlot(String name);
	}
	
	private final AbstractDisplayAwareActivity<? extends IsWidget>[] presenters;
	
	public SlotPresenter(
			SlotDispay display,
			AbstractDisplayAwareActivity<? extends IsWidget>... presenters) {
		super(display);
		this.presenters = presenters;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(display);
		for(AbstractDisplayAwareActivity<? extends IsWidget> presenter : presenters) {
			presenter.start(display.getNextSlot(), eventBus);
		}
	}

}
