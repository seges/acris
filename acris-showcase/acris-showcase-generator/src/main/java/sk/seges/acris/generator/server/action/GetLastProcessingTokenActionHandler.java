package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenAction;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetLastProcessingTokenActionHandler extends AbstractActionHandler<GetLastProcessingTokenAction, GetLastProcessingTokenResult> {

	private TokenProvider tokenProvider;

	public GetLastProcessingTokenActionHandler(TokenProvider tokenProvider) {
		super(GetLastProcessingTokenAction.class);
	}

	@Override
	public GetLastProcessingTokenResult execute(GetLastProcessingTokenAction action, ExecutionContext context) throws ActionException {
		return new GetLastProcessingTokenResult(tokenProvider.getTokenForProcessing());
	}

	@Override
	public void undo(GetLastProcessingTokenAction action, GetLastProcessingTokenResult result, ExecutionContext context) throws ActionException {
	}
}