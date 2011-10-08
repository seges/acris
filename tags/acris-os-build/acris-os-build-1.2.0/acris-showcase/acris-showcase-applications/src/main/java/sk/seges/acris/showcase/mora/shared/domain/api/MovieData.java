package sk.seges.acris.showcase.mora.shared.domain.api;

import java.io.Serializable;

import sk.seges.acris.core.shared.common.HasDescription;
import sk.seges.acris.core.shared.common.HasName;
import sk.seges.acris.showcase.mora.shared.domain.api.common.HasComments;
import sk.seges.acris.showcase.mora.shared.domain.api.common.HasPicture;
import sk.seges.acris.showcase.mora.shared.domain.api.common.HasRating;
import sk.seges.acris.showcase.mora.shared.domain.api.common.IRatingValue;

public interface MovieData extends HasName, HasDescription, HasRating, HasComments, HasPicture, Serializable {
	
	public static enum MovieRating implements IRatingValue {
		
		BAD(5), POOR(4), AVERAGE(3), GOOD(2), EXCELLENT(1);
		
		private int rating;
		
		MovieRating(int rating) {
			this.rating = rating;
		}

		public int getRating() {
			return rating;
		}
	}
}