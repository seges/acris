package sk.seges.acris.generator.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import sk.seges.acris.generator.server.domain.jpa.common.JpaRating;
import sk.seges.acris.generator.shared.domain.api.CommentData;
import sk.seges.acris.generator.shared.domain.api.common.RatingData;
import sk.seges.acris.generator.shared.domain.dto.MovieDto;

@Entity
public class JpaMovie extends MovieDto {

	private static final long serialVersionUID = 6474730100488178357L;

	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Column
	public String getName() {
		return super.getName();
	}

	@Override
	@Column
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	@OneToOne(targetEntity = JpaRating.class)
	public RatingData getRating() {
		return super.getRating();
	}

	@Override
	@ElementCollection(targetClass = JpaComment.class)
	public CommentData[] getComments() {
		return super.getComments();
	}

	@Override
	@Column
	public String getPicturePath() {
		return super.getPicturePath();
	}
}