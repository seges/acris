/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity;

import java.util.Map;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import sk.seges.acris.bpm.client.engine.BeanProvider;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ladislav.gazo
 */
public abstract class GWTRPCServiceActivity<Service, Result> extends DefaultActivity {
	private final BeanProvider provider;
	
	public GWTRPCServiceActivity(BeanProvider provider) {
		super();
		this.provider = provider;
	}

	@Override
	public final void execute(final ActivityExecution execution) throws Exception {
		String chocolate = (String) execution.getActivity().getProperty("serviceChocolate");
		@SuppressWarnings("unchecked")
		Service service = (Service) provider.getChocolate(chocolate);
		AsyncCallback<Result> callback = new AsyncCallback<Result>() {
			@Override
			public void onFailure(Throwable caught) {
				resolveCall(execution, caught);				
			}

			@Override
			public void onSuccess(Result result) {
				resolveCall(execution, result);
			}};
		callService(service, callback, execution.getVariables(), execution.getActivity());
	}

	private void resolveCall(final ActivityExecution execution, Object result) {
		String resultVar = (String) execution.getActivity().getProperty("resultVariableName");
		execution.setVariable(resultVar, result);
		leave(execution);
	}
	
	@Override
	public void signal(ActivityExecution execution, String signalEvent, Object signalData) throws Exception {
	}
	
	protected abstract void callService(Service service, AsyncCallback<Result> callback, Map<String, Object> variables, PvmActivity activity);
}
