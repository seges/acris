package sk.seges.acris.generator.shared.domain.dto;

import sk.seges.acris.generator.shared.domain.api.CommentData;
import sk.seges.acris.generator.shared.domain.api.MovieData;
import sk.seges.acris.generator.shared.domain.api.common.RatingData;
import sk.seges.sesam.domain.IMutableDomainObject;

public class MovieDto implements MovieData, IMutableDomainObject<Long> {

	private static final long serialVersionUID = 6634972817141016693L;

	private Long id;
	private String name;
	private String description;
	private RatingData ratingData;
	private CommentData[] comments;
	private String picturePath;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public RatingData getRating() {
		return ratingData;
	}

	@Override
	public void setRating(RatingData rating) {
		this.ratingData = rating;
	}

	@Override
	public CommentData[] getComments() {
		return comments;
	}

	@Override
	public void setComments(CommentData... comments) {
		this.comments = comments;
	}

	@Override
	public String getPicturePath() {
		return picturePath;
	}

	@Override
	public void setPicturePath(String path) {
		this.picturePath = path;
	}
}