package sk.seges.acris.showcase.mora.shared.domain.api.common;

import java.io.Serializable;


public interface RatingData extends Serializable {

	IRatingValue getRating();

	void setRating(IRatingValue value);

	IRatingValue[] getRatingValues();
	
	void setRatingValues(IRatingValue[] values);

}
