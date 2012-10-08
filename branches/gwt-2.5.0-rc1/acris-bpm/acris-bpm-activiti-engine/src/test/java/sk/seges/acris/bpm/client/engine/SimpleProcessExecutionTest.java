/**
 * 
 */
package sk.seges.acris.bpm.client.engine;

import static org.junit.Assert.assertNotNull;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmExecution;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.junit.Test;

/**
 * @author ladislav.gazo
 */
public class SimpleProcessExecutionTest {
	@Test
	public void testRunSimpleProcess() {
		PvmProcessDefinition processDefinition = new ProcessDefinitionBuilder().createActivity("a").initial()
				.behavior(new WaitState()).transition("b").endActivity().createActivity("b")
				.behavior(new WaitState()).transition("c").endActivity().createActivity("c")
				.behavior(new WaitState()).endActivity().buildProcessDefinition();

		PvmProcessInstance processInstance = processDefinition.createProcessInstance();
		processInstance.start();

		PvmExecution activityInstance = processInstance.findExecution("a");
		assertNotNull(activityInstance);

		activityInstance.signal(null, null);

		activityInstance = processInstance.findExecution("b");
		assertNotNull(activityInstance);

		activityInstance.signal(null, null);

		activityInstance = processInstance.findExecution("c");
		assertNotNull(activityInstance);
	}

	class WaitState implements SignallableActivityBehavior {

		public void execute(ActivityExecution execution) throws Exception {}

		public void signal(ActivityExecution execution, String signalName, Object signalData)
				throws Exception {
			PvmTransition transition = execution.getActivity().getOutgoingTransitions().get(0);
			execution.take(transition);
		}
	}

}
