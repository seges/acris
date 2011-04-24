package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.generator.shared.action.GetDefaultGeneratorTokenAction;
import sk.seges.acris.generator.shared.action.GetDefaultGeneratorTokenResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDefaultGeneratorTokenActionHandler extends AbstractActionHandler<GetDefaultGeneratorTokenAction, GetDefaultGeneratorTokenResult> {

	private TokenProvider tokenProvider;

	public GetDefaultGeneratorTokenActionHandler(TokenProvider tokenProvider) {
		super(GetDefaultGeneratorTokenAction.class);
	}

	//TODO
	@Override
	public GetDefaultGeneratorTokenResult execute(GetDefaultGeneratorTokenAction action, ExecutionContext context) throws ActionException {
		return new GetDefaultGeneratorTokenResult(tokenProvider.getTokenForProcessing());
	}

	@Override
	public void undo(GetDefaultGeneratorTokenAction action, GetDefaultGeneratorTokenResult result, ExecutionContext context) throws ActionException {
	}
}