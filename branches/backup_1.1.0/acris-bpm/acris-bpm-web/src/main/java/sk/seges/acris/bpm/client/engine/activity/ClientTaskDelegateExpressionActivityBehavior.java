/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity;

import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import sk.seges.acris.bpm.client.engine.BeanProvider;

/**
 * @author ladislav.gazo
 */
public class ClientTaskDelegateExpressionActivityBehavior extends DefaultActivity {
	private final BeanProvider provider;
	private final String beanName;
	private final List<FieldDeclaration> fieldDeclarations;

	public ClientTaskDelegateExpressionActivityBehavior(BeanProvider provider, String beanName, List<FieldDeclaration> fieldDeclarations) {
		super();
		this.provider = provider;
		this.beanName = beanName;
		this.fieldDeclarations = fieldDeclarations;
	}

	@Override
	public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
		Object delegate = getDelegate(execution);
		if (delegate instanceof SignallableActivityBehavior) {
			((SignallableActivityBehavior) delegate).signal(execution, signalName, signalData);
		}
	}

	private Object getDelegate(ActivityExecution execution) {
		ActivityImpl activity = (ActivityImpl) execution.getActivity();
		for(FieldDeclaration declaration : fieldDeclarations) {
			activity.setProperty(declaration.getName(), declaration.getValue());
		}
		Object delegate = provider.getChocolate(beanName);
		return delegate;
	}

	public void execute(ActivityExecution execution) throws Exception {

		// Note: we can't cache the result of the expression, because the
		// execution can change: eg.
		// delegateExpression='${mySpringBeanFactory.randomSpringBean()}'
		Object delegate = getDelegate(execution);

		if (delegate instanceof ActivityBehavior) {
			((ActivityBehavior) delegate).execute(execution);

//		} else if (delegate instanceof JavaDelegate) {
//			((JavaDelegate) delegate).execute(execution);
//			leave(execution);

		} else {
			throw new ActivitiException("Delegate expression " + beanName
					+ " did not resolve to an implementation");
		}
	}
}
