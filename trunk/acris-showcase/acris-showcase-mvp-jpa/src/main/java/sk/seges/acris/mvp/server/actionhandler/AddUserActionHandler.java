package sk.seges.acris.mvp.server.actionhandler;

import org.springframework.stereotype.Component;

import sk.seges.acris.mvp.server.actionhandler.core.DefaultActionHandler;
import sk.seges.acris.mvp.shared.action.AddUserAction;
import sk.seges.acris.mvp.shared.result.AddUserResult;

import com.philbeaudoin.gwtp.dispatch.server.ExecutionContext;
import com.philbeaudoin.gwtp.dispatch.shared.ActionException;

@Component
public class AddUserActionHandler extends DefaultActionHandler<AddUserAction, AddUserResult> {

	
	@Override
	public Class<AddUserAction> getActionType() {
		return AddUserAction.class;
	}

	@Override
	public AddUserResult execute(AddUserAction action, ExecutionContext context) throws ActionException {
		return new AddUserResult(action.getUser());
//		return new AddUserResult(userMaintenanceService.persist(action.getUser()));
	}

	@Override
	public void undo(AddUserAction action, AddUserResult result, ExecutionContext context) throws ActionException {
	}
}