/**
 * 
 */
package sk.seges.acris.scaffold.mvp;

import sk.seges.acris.mvp.AbstractDisplayAwareActivity;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ladislav.gazo
 */
public class Slot {
	private String name;
	private final AbstractDisplayAwareActivity<IsWidget> presenter;
	
	public Slot(AbstractDisplayAwareActivity<IsWidget> presenter) {
		super();
		this.presenter = presenter;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public AbstractDisplayAwareActivity<IsWidget> getPresenter() {
		return presenter;
	}
}
