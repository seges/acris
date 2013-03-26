/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity.bpmn;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

/**
 * @author ladislav.gazo
 *
 */
public class ReceiveTaskActivity extends TaskActivity {
	@Override
	public void execute(ActivityExecution execution) throws Exception {
	}
	
	@Override
	public void signal(ActivityExecution execution, String signalEvent, Object signalData) throws Exception {
		leave(execution);
	}
}
