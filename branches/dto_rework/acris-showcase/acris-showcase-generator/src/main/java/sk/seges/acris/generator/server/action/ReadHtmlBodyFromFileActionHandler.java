package sk.seges.acris.generator.server.action;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileAction;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ReadHtmlBodyFromFileActionHandler extends AbstractActionHandler<ReadHtmlBodyFromFileAction, ReadHtmlBodyFromFileResult> {

	private GeneratorService generatorService;

	public ReadHtmlBodyFromFileActionHandler(GeneratorService generatorService) {
		super(ReadHtmlBodyFromFileAction.class);
		this.generatorService = generatorService;
	}

	@Override
	public ReadHtmlBodyFromFileResult execute(ReadHtmlBodyFromFileAction action, ExecutionContext context) throws ActionException {
		Tuple<String, String> result = generatorService.readHtmlBodyFromFile(action.getFilename());
		return new ReadHtmlBodyFromFileResult(result.getFirst(), result.getSecond());
	}

	@Override
	public void undo(ReadHtmlBodyFromFileAction action, ReadHtmlBodyFromFileResult result, ExecutionContext context) throws ActionException {
	}
}