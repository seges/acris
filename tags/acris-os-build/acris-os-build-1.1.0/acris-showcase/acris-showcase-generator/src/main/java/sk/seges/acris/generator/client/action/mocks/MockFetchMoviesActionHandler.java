package sk.seges.acris.generator.client.action.mocks;

import sk.seges.acris.generator.client.action.mocks.factory.MoviesFactory;
import sk.seges.acris.generator.shared.action.FetchMoviesAction;
import sk.seges.acris.generator.shared.action.FetchMoviesResult;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;

public class MockFetchMoviesActionHandler extends MockActionHandler<FetchMoviesAction, FetchMoviesResult> {

	public MockFetchMoviesActionHandler() {
	}

	@Override
	public FetchMoviesResult execute(FetchMoviesAction action) {
		return new FetchMoviesResult(MoviesFactory.createMockMovies());
	}

	@Override
	public Class<FetchMoviesAction> getActionType() {
		return FetchMoviesAction.class;
	}
}