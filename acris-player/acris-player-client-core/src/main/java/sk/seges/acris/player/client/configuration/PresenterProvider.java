package sk.seges.acris.player.client.configuration;

import com.google.gwt.activity.shared.AbstractActivity;

public interface PresenterProvider<T extends AbstractActivity> {

	T getPresenter();
}
