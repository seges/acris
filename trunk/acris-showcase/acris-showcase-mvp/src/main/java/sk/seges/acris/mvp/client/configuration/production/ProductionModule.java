package sk.seges.acris.mvp.client.configuration.production;

import sk.seges.acris.showcase.client.action.ActionManager;

import com.google.gwt.inject.client.AbstractGinModule;

public class ProductionModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(ActionManager.class).asEagerSingleton();
	}
}
