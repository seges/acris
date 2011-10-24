/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity.bpmn;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.Condition;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

/**
 * @author ladislav.gazo
 */
public class ExclusiveGatewayActivity extends GatewayActivity {
	private static Logger log = Logger.getLogger(ExclusiveGatewayActivity.class.getName());
	
	@Override
	protected void leave(ActivityExecution execution) {
		 if (log.isLoggable(Level.FINE)) {
		      log.fine("Leaving activity '" + execution.getActivity().getId() + "'");
		    }
		    
		    PvmTransition outgoingSeqFlow = null;
		    Iterator<PvmTransition> transitionIterator = execution.getActivity().getOutgoingTransitions().iterator();
		    while (outgoingSeqFlow == null && transitionIterator.hasNext()) {
		      PvmTransition seqFlow = transitionIterator.next();
		      
		      // TODO conditions should go into the activity behaviour configuration (probably base BpmnActivity as all activities need conditions)
		      Condition condition = (Condition) seqFlow.getProperty(BpmnConstants.PROPERTYNAME_CONDITION);
		      if ( condition==null || condition.evaluate(execution) ) {
		        if (log.isLoggable(Level.FINE)) {
		          log.fine("Sequence flow '" + seqFlow.getId() + " '"
		                  + "selected as outgoing sequence flow.");
		        }
		        outgoingSeqFlow = seqFlow;
		      }
		    }
		    
		    if (outgoingSeqFlow != null) {
		      execution.take(outgoingSeqFlow);
		    } else {
		      //No sequence flow could be found
		      throw new ActivitiException("No outgoing sequence flow of the exclusive gateway '"
		              + execution.getActivity().getId() + "' could be selected for continuing the process");
		    }
	}
}
