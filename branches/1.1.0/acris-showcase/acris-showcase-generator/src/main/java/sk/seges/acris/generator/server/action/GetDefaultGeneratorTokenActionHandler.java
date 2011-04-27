package sk.seges.acris.generator.server.action;

import sk.seges.acris.generator.shared.action.GetDefaultGeneratorTokenAction;
import sk.seges.acris.generator.shared.action.GetDefaultGeneratorTokenResult;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetDefaultGeneratorTokenActionHandler extends AbstractActionHandler<GetDefaultGeneratorTokenAction, GetDefaultGeneratorTokenResult> {

	public GetDefaultGeneratorTokenActionHandler() {
		super(GetDefaultGeneratorTokenAction.class);
	}

	//TODO
	@Override
	public GetDefaultGeneratorTokenResult execute(GetDefaultGeneratorTokenAction action, ExecutionContext context) throws ActionException {
		GeneratorToken token = new GeneratorToken();
		token.setWebId(action.getWebId());
		token.setLanguage(action.getLanguage());
		return new GetDefaultGeneratorTokenResult(token);
	}

	@Override
	public void undo(GetDefaultGeneratorTokenAction action, GetDefaultGeneratorTokenResult result, ExecutionContext context) throws ActionException {
	}
}