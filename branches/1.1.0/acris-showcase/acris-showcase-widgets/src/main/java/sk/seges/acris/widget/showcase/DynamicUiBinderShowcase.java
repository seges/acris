package sk.seges.acris.widget.showcase;

import sk.seges.acris.widget.showcase.dynamicui.DynamicallyBoundPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author ladislav.gazo
 *
 */
public class DynamicUiBinderShowcase implements EntryPoint {

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new DynamicallyBoundPanel());
	}

}
