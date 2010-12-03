package sk.seges.acris.generator.client.gin.mock;

import sk.seges.acris.generator.client.action.mocks.MockFetchMoviesActionHandler;
import sk.seges.acris.generator.client.action.mocks.MockGenerateOfflineActionHandler;
import sk.seges.acris.generator.client.action.mocks.MockSaveRatingActionHandler;
import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.mocks.manager.MockActionManager;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class MockModule extends AbstractGinModule {

	@Override
	protected void configure() {

		bind(MockFetchMoviesActionHandler.class).asEagerSingleton();
		bind(MockGenerateOfflineActionHandler.class).asEagerSingleton();
		bind(MockSaveRatingActionHandler.class).asEagerSingleton();

		bind(ActionManager.class).to(MockActionManager.class).in(Singleton.class);

	}
}