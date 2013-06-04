package sk.seges.acris.generator.client.event;

import sk.seges.acris.generator.shared.domain.api.MovieData;

import com.gwtplatform.annotation.GenEvent;
import com.gwtplatform.annotation.Order;

@GenEvent
public class RateMovie {

	@Order(1)
	MovieData movie;

}