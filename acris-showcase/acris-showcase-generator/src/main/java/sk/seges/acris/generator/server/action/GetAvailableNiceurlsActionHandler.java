package sk.seges.acris.generator.server.action;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsAction;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsResult;
import sk.seges.acris.showcase.mora.client.configuration.NameTokens;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;


public class GetAvailableNiceurlsActionHandler extends AbstractActionHandler<GetAvailableNiceurlsAction, GetAvailableNiceurlsResult> {

	public GetAvailableNiceurlsActionHandler() {
		super(GetAvailableNiceurlsAction.class);
	}

	@Override
	public GetAvailableNiceurlsResult execute(GetAvailableNiceurlsAction action, ExecutionContext context) throws ActionException {
		List<String> availableTokens = new ArrayList<String>();
		availableTokens.add(NameTokens.HOME_PAGE);
		return new GetAvailableNiceurlsResult(availableTokens);
	}

	@Override
	public void undo(GetAvailableNiceurlsAction action, GetAvailableNiceurlsResult result, ExecutionContext context) throws ActionException {
	}
}