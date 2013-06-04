package sk.seges.acris.generator.client.action.mocks;

import sk.seges.acris.generator.shared.action.SaveRatingAction;
import sk.seges.acris.generator.shared.action.SaveRatingResult;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;

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
