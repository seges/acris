package sk.seges.acris.generator.client.event;

import sk.seges.acris.generator.client.presenter.SummaryPresenter.DialogOptions;

import com.gwtplatform.annotation.GenEvent;
import com.gwtplatform.annotation.Order;

@GenEvent
public class SelectPromt {

	@Order(1)
	DialogOptions dialogOptions;
}