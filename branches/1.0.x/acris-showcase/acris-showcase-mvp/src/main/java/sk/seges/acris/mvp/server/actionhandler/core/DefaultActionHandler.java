package sk.seges.acris.mvp.server.actionhandler.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.philbeaudoin.gwtp.dispatch.server.actionHandler.ActionHandler;
import com.philbeaudoin.gwtp.dispatch.server.actionHandler.ActionHandlerRegistry;
import com.philbeaudoin.gwtp.dispatch.server.actionHandler.ClassActionHandlerRegistry;
import com.philbeaudoin.gwtp.dispatch.server.actionHandler.InstanceActionHandlerRegistry;
import com.philbeaudoin.gwtp.dispatch.server.actionValidator.ActionValidator;
import com.philbeaudoin.gwtp.dispatch.server.actionValidator.ActionValidatorRegistry;
import com.philbeaudoin.gwtp.dispatch.server.actionValidator.ClassActionValidatorRegistry;
import com.philbeaudoin.gwtp.dispatch.server.actionValidator.DefaultActionValidator;
import com.philbeaudoin.gwtp.dispatch.server.actionValidator.InstanceActionValidatorRegistry;
import com.philbeaudoin.gwtp.dispatch.shared.Action;
import com.philbeaudoin.gwtp.dispatch.shared.Result;

public abstract class DefaultActionHandler<A extends Action<R>, R extends Result> implements ActionHandler<A, R> {

	@Autowired
	private ActionHandlerRegistry handlerRegistry;

	@Autowired
	private ActionValidatorRegistry validatorRegistry;

	protected Class<? extends ActionValidator> getActionValidatorClass() {
		return DefaultActionValidator.class;
	}
	
	protected ActionValidator getActionValidator() {
		return new DefaultActionValidator();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void registerHandlers() {
		if (handlerRegistry instanceof InstanceActionHandlerRegistry) {
			((InstanceActionHandlerRegistry)handlerRegistry).addHandler(this);
		} else if (handlerRegistry instanceof ClassActionHandlerRegistry){
			((ClassActionHandlerRegistry)handlerRegistry).addHandlerClass(getActionType(), (Class<? extends ActionHandler<A, R>>) getClass());
		}
		
		if (validatorRegistry instanceof InstanceActionValidatorRegistry) {
			((InstanceActionValidatorRegistry) validatorRegistry).addActionValidator(getActionType(), getActionValidator());
		} else if (validatorRegistry instanceof ClassActionValidatorRegistry) {
			((ClassActionValidatorRegistry) validatorRegistry).addActionValidatorClass(getActionType(), getActionValidatorClass());
		}
	}
}