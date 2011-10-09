package sk.seges.acris.mvp.client.configuration.mock;

import sk.seges.acris.mvp.client.action.ActionManager;
import sk.seges.acris.mvp.client.action.mocks.MockAddUserActionHandler;
import sk.seges.acris.mvp.client.action.mocks.MockFetchUsersActionHandler;
import sk.seges.acris.mvp.client.action.mocks.manager.MockActionManager;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MockModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(MockAddUserActionHandler.class).asEagerSingleton();
		bind(MockFetchUsersActionHandler.class).asEagerSingleton();

		bind(ActionManager.class).to(MockActionManager.class).in(Singleton.class);

	}
}