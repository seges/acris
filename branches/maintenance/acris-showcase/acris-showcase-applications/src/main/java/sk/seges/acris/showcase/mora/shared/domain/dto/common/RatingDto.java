package sk.seges.acris.showcase.mora.shared.domain.dto.common;

import sk.seges.acris.showcase.mora.shared.domain.api.common.IRatingValue;
import sk.seges.acris.showcase.mora.shared.domain.api.common.RatingData;
import sk.seges.sesam.domain.IMutableDomainObject;

public class RatingDto implements RatingData, IMutableDomainObject<Long> {

	private static final long serialVersionUID = -2231051242142279393L;

	private IRatingValue ratingValue;
	private IRatingValue[] ratingValues;
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long t) {
		this.id = t;
	}

	@Override
	public IRatingValue getRating() {
		return ratingValue;
	}

	@Override
	public void setRating(IRatingValue value) {
		this.ratingValue = value;
	}

	@Override
	public IRatingValue[] getRatingValues() {
		return ratingValues;
	}

	@Override
	public void setRatingValues(IRatingValue[] values) {
		this.ratingValues = values;
	}
}