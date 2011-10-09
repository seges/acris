package sk.seges.acris.showcase.mora.shared.domain.api.common;

import sk.seges.acris.showcase.mora.shared.domain.api.CommentData;

public interface HasComments {
	
	CommentData[] getComments();
	
	void setComments(CommentData... comments);

}
