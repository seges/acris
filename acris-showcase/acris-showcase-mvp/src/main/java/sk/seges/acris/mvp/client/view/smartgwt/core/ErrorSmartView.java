package sk.seges.acris.mvp.client.view.smartgwt.core;

import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter.ErrorDisplay;
import sk.seges.acris.mvp.client.view.core.adapter.smartgwt.SmartViewImpl;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;

public class ErrorSmartView extends SmartViewImpl implements ErrorDisplay {

	private final Canvas canvas = new Canvas();

	@Inject
	public ErrorSmartView() {
	}

	@Override
	public Widget asWidget() {
		return canvas;
	}

	@Override
	public void displayMessage(String message) {
		SC.say(message);
	}
}