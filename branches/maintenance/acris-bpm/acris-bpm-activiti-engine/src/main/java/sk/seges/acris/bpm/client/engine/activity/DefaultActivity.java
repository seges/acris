/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.delegate.SignallableActivityBehavior;

/**
 * @author ladislav.gazo
 * 
 */
public abstract class DefaultActivity implements SignallableActivityBehavior {
	@Override
	public void execute(ActivityExecution execution) throws Exception {
		leave(execution);
	}

	protected void leave(ActivityExecution execution) {
		List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();

		List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
		for (PvmTransition outgoingTransition : outgoingTransitions) {
			// Condition condition = (Condition)
			// outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
			// if (condition == null
			// || !checkConditions
			// || condition.evaluate(execution)) {
			transitionsToTake.add(outgoingTransition);
			// }
		}

		if (transitionsToTake.size() == 1) {
			execution.take(transitionsToTake.get(0));

		} else if (transitionsToTake.size() >= 1) {
			execution.inactivate();

			List<ActivityExecution> joinedExecutions = new ArrayList<ActivityExecution>();
			joinedExecutions.add(execution);

			execution.takeAll(transitionsToTake, joinedExecutions);

		} else {
			execution.end();
		}
	}
	
	@Override
	public void signal(ActivityExecution execution, String signalEvent, Object signalData) throws Exception {
		throw new RuntimeException("Nothing to signal here");
	}
}
