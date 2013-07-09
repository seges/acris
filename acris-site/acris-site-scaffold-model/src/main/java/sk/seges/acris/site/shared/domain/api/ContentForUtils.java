package sk.seges.acris.site.shared.domain.api;

import java.util.List;

public interface ContentForUtils {

	String getDefaultStyleClass();
	
	List<? extends ContentForUtils> getSubContents();
	
	ContentForUtils getParent();
}
