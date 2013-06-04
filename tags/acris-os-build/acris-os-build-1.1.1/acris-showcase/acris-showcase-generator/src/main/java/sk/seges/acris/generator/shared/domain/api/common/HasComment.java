package sk.seges.acris.generator.shared.domain.api.common;

import sk.seges.acris.generator.shared.domain.api.CommentData;

public interface HasComment {

	CommentData getComment();
	
	void setComment(CommentData comment);
}
