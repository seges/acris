package sk.seges.acris.openid.client.view;

import sk.seges.acris.security.client.view.OpenIDLoginView;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ShowcaseView extends OpenIDLoginView {

	@Override
	public Widget asWidget() {
		SimplePanel wrapper = new SimplePanel();
		wrapper.addStyleName("acris-login-panel-wrapper");
		wrapper.add(this);

		return wrapper;
	}
}
