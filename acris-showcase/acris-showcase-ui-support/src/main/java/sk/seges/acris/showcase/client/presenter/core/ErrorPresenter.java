package sk.seges.acris.showcase.client.presenter.core;

import sk.seges.acris.showcase.client.i18n.ShowcaseMessages;
import sk.seges.acris.showcase.client.presenter.FailureHandler;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class ErrorPresenter extends Presenter<ErrorPresenter.ErrorDisplay, ErrorPresenter.ErrorProxy> implements FailureHandler {

	private ShowcaseMessages messages = GWT.create(ShowcaseMessages.class);
	
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
		RevealRootContentEvent.fire(this, this);
	}
}