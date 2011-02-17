package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.service.GeneratorService;
import sk.seges.acris.generator.shared.action.GetOfflineContentHtmlAction;
import sk.seges.acris.generator.shared.action.GetOfflineContentHtmlResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetOfflineContentHtmlActionHandler extends AbstractActionHandler<GetOfflineContentHtmlAction, GetOfflineContentHtmlResult> {

	private GeneratorService generatorService;

	public GetOfflineContentHtmlActionHandler(GeneratorService generatorService) {
		super(GetOfflineContentHtmlAction.class);
		this.generatorService = generatorService;
	}

	@Override
	public GetOfflineContentHtmlResult execute(GetOfflineContentHtmlAction action, ExecutionContext context) throws ActionException {
		return new GetOfflineContentHtmlResult(generatorService.getOfflineContentHtml(action.getEntryPointFileName(), action.getHeader(),
				action.getContentWrapper(), action.getContent(), action.getToken(), action.getCurrentServerURL()));
	}

	@Override
	public void undo(GetOfflineContentHtmlAction action, GetOfflineContentHtmlResult result, ExecutionContext context) throws ActionException {
	}
}