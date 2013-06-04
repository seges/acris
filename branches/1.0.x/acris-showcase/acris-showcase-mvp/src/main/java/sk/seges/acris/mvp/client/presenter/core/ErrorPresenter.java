package sk.seges.acris.mvp.client.presenter.core;

import sk.seges.acris.mvp.client.i18n.UserMessages;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.PresenterImpl;
import com.philbeaudoin.gwtp.mvp.client.View;
import com.philbeaudoin.gwtp.mvp.client.annotations.ProxyStandard;
import com.philbeaudoin.gwtp.mvp.client.proxy.Proxy;
import com.philbeaudoin.gwtp.mvp.client.proxy.RevealRootContentEvent;

public class ErrorPresenter extends PresenterImpl<ErrorPresenter.ErrorDisplay, ErrorPresenter.ErrorProxy> {

	private UserMessages messages = GWT.create(UserMessages.class);
	
	@Inject
	public ErrorPresenter(EventBus eventBus, ErrorDisplay view, ErrorProxy proxy) {
		super(eventBus, view, proxy);
	}

	@ProxyStandard
	public interface ErrorProxy extends Proxy<ErrorPresenter> {}

	public interface ErrorDisplay extends View {

		void displayMessage(String message);
	}

	public void handleFailure(Throwable cause) {
		getView().displayMessage(messages.failure(cause.getMessage()));
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(eventBus, this);
	}
}
