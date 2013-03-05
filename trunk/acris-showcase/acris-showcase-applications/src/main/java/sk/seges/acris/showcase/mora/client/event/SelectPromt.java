package sk.seges.acris.showcase.mora.client.event;

import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter.DialogOptions;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;

@GenEvent
public class SelectPromt {

	@Order(1)
	DialogOptions dialogOptions;
}