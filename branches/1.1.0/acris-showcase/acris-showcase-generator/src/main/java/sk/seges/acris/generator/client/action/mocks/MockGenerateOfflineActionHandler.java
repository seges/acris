package sk.seges.acris.generator.client.action.mocks;

import sk.seges.acris.generator.shared.action.GenerateOfflineAction;
import sk.seges.acris.generator.shared.action.GenerateOfflineResult;
import sk.seges.acris.showcase.client.action.mocks.core.MockActionHandler;

public class MockGenerateOfflineActionHandler extends MockActionHandler<GenerateOfflineAction, GenerateOfflineResult> {

	public MockGenerateOfflineActionHandler() {
	}

	@Override
	public GenerateOfflineResult execute(GenerateOfflineAction action) {
		return new GenerateOfflineResult(true);
	}

	@Override
	protected Class<GenerateOfflineAction> getActionType() {
		return GenerateOfflineAction.class;
	}
}