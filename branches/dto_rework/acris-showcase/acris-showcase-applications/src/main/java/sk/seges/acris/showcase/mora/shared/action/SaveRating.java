package sk.seges.acris.showcase.mora.shared.action;

import sk.seges.acris.showcase.mora.shared.domain.api.MovieData;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch
public class SaveRating {

	@In(1)
	MovieData movie;

	@Out(1)
	boolean result;
}