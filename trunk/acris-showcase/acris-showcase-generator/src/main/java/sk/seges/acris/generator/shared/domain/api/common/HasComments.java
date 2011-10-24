package sk.seges.acris.generator.shared.domain.api.common;

import sk.seges.acris.generator.shared.domain.api.CommentData;

public interface HasComments {
	
	CommentData[] getComments();
	
	void setComments(CommentData... comments);

}
