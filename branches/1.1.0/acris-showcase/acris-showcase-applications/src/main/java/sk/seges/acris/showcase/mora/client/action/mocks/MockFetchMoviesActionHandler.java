package sk.seges.acris.showcase.mora.client.action.mocks;

import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.showcase.mora.client.action.mocks.factory.MoviesFactory;
import sk.seges.acris.showcase.mora.shared.action.FetchMoviesAction;
import sk.seges.acris.showcase.mora.shared.action.FetchMoviesResult;

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