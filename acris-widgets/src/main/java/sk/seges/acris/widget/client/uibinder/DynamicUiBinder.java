package sk.seges.acris.widget.client.uibinder;

import com.google.gwt.uibinder.client.UiBinder;

/**
 * @author ladislav.gazo
 */
public interface DynamicUiBinder<U, O> extends UiBinder<U, O> {
	void setHtml(String html);
}
