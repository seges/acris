package sk.seges.acris.showcase.mora.client.event;

import sk.seges.acris.showcase.mora.shared.domain.api.MovieData;

import com.gwtplatform.dispatch.annotation.GenEvent;
import com.gwtplatform.dispatch.annotation.Order;

@GenEvent
public class RateMovie {

	@Order(1)
	MovieData movie;

}