package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.shared.action.WriteOfflineContentHtmlAction;
import sk.seges.acris.generator.shared.action.WriteOfflineContentHtmlResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class WriteOfflineContentHtmlActionHandler extends AbstractActionHandler<WriteOfflineContentHtmlAction, WriteOfflineContentHtmlResult> {

	private GeneratorService generatorService;

	public WriteOfflineContentHtmlActionHandler(GeneratorService generatorService) {
		super(WriteOfflineContentHtmlAction.class);
		this.generatorService = generatorService;
	}

	@Override
	public WriteOfflineContentHtmlResult execute(WriteOfflineContentHtmlAction action, ExecutionContext context) throws ActionException {
		generatorService.writeOfflineContentHtml(action.getEntryPointFileName(), action.getHeader(),
				action.getContentWrapper(), action.getContent(), action.getToken(), action.getCurrentServerURL());
		return new WriteOfflineContentHtmlResult();
	}

	@Override
	public void undo(WriteOfflineContentHtmlAction action, WriteOfflineContentHtmlResult result, ExecutionContext context) throws ActionException {
	}
}