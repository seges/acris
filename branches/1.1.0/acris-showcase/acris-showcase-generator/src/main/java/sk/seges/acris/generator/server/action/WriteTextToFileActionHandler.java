package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.shared.action.WriteTextToFileAction;
import sk.seges.acris.generator.shared.action.WriteTextToFileResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class WriteTextToFileActionHandler extends AbstractActionHandler<WriteTextToFileAction, WriteTextToFileResult> {

	private GeneratorService generatorService;

	public WriteTextToFileActionHandler(GeneratorService generatorService) {
		super(WriteTextToFileAction.class);
		this.generatorService = generatorService;
	}

	@Override
	public WriteTextToFileResult execute(WriteTextToFileAction action, ExecutionContext context) throws ActionException {
		generatorService.writeTextToFile(action.getContent(), action.getToken());
		return new WriteTextToFileResult();
	}

	@Override
	public void undo(WriteTextToFileAction action, WriteTextToFileResult result, ExecutionContext context) throws ActionException {
	}
}