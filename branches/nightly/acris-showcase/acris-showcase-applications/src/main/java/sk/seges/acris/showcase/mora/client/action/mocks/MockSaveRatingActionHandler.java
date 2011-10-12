package sk.seges.acris.showcase.mora.client.action.mocks;

import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;
import sk.seges.acris.showcase.mora.shared.action.SaveRatingAction;
import sk.seges.acris.showcase.mora.shared.action.SaveRatingResult;

public class MockSaveRatingActionHandler extends MockActionHandler<SaveRatingAction, SaveRatingResult> {

	public MockSaveRatingActionHandler() {
	}
	
	@Override
	public SaveRatingResult execute(SaveRatingAction action) {
		return new SaveRatingResult(true);
	}

	@Override
	protected Class<SaveRatingAction> getActionType() {
		return SaveRatingAction.class;
	}
}
