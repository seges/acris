package sk.seges.acris.security.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public class LogoutPresenter<D extends LogoutDisplay> extends BasePresenter<D>{

	public LogoutPresenter(D display) {
		super(display);
	}

	@Override
	public void bind(HasWidgets parent) {
		super.bind(parent);
	}
	
	protected void doLogout() {
	}
}
