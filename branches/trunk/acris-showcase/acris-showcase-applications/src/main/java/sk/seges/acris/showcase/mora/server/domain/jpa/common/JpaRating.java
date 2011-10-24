package sk.seges.acris.showcase.mora.server.domain.jpa.common;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import sk.seges.acris.showcase.mora.shared.domain.api.MovieData.MovieRating;
import sk.seges.acris.showcase.mora.shared.domain.api.common.IRatingValue;
import sk.seges.acris.showcase.mora.shared.domain.dto.common.RatingDto;

@Entity
public class JpaRating extends RatingDto {

	private static final long serialVersionUID = 6217882853808181700L;

	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Transient
	public IRatingValue getRating() {
		return super.getRating();
	}

	@Column
	public Integer getRatingValue() {
		IRatingValue ratingValue = super.getRating();
		if (ratingValue == null) {
			return null;
		}
		return ratingValue.getRating();
	}
	
	public void setRatingValue(Integer rating) {
		if (rating == null) {
			return;
		}
		
		for (MovieRating movieRating: MovieRating.values()) {
			if (movieRating.getRating() == rating) {
				super.setRating(movieRating);
			}
		}
	}
	
	@Override
	@ElementCollection(targetClass = MovieRating.class)
	public IRatingValue[] getRatingValues() {
		return super.getRatingValues();
	}

}
