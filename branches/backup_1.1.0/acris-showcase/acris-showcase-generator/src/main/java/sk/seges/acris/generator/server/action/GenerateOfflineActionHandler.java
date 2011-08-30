package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.service.OfflineContentRunner;
import sk.seges.acris.generator.shared.action.GenerateOfflineAction;
import sk.seges.acris.generator.shared.action.GenerateOfflineResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GenerateOfflineActionHandler extends AbstractActionHandler<GenerateOfflineAction, GenerateOfflineResult> {

	public GenerateOfflineActionHandler() {
		super(GenerateOfflineAction.class);
	}

	@Override
	public GenerateOfflineResult execute(GenerateOfflineAction action, ExecutionContext context) throws ActionException {
		new OfflineContentRunner().startGenerator();
		return new GenerateOfflineResult(true);
	}

	@Override
	public void undo(GenerateOfflineAction action, GenerateOfflineResult result, ExecutionContext context) throws ActionException {
		// No undo support
	}
}