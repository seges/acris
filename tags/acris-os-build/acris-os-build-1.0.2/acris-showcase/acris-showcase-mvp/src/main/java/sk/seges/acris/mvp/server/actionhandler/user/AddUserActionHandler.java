package sk.seges.acris.mvp.server.actionhandler.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.seges.acris.mvp.server.actionhandler.core.DefaultActionHandler;
import sk.seges.acris.mvp.server.service.IUserMaintenanceService;
import sk.seges.acris.mvp.shared.action.user.AddUserAction;
import sk.seges.acris.mvp.shared.result.user.AddUserResult;

import com.philbeaudoin.gwtp.dispatch.server.ExecutionContext;
import com.philbeaudoin.gwtp.dispatch.shared.ActionException;

@Component
public class AddUserActionHandler extends DefaultActionHandler<AddUserAction, AddUserResult> {

	@Autowired
	private IUserMaintenanceService userMaintenanceService;
	
	@Override
	public Class<AddUserAction> getActionType() {
		return AddUserAction.class;
	}

	@Override
	public AddUserResult execute(AddUserAction action, ExecutionContext context) throws ActionException {
//		return new AddUserResult(action.getUser());
		return new AddUserResult(userMaintenanceService.persist(action.getUser()));
	}

	@Override
	public void undo(AddUserAction action, AddUserResult result, ExecutionContext context) throws ActionException {
	}
}