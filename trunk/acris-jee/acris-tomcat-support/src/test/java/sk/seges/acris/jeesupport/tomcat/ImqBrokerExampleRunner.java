package sk.seges.acris.jeesupport.tomcat;

import java.util.Properties;

import com.sun.messaging.jmq.jmsclient.runtime.BrokerInstance;
import com.sun.messaging.jmq.jmsclient.runtime.ClientRuntime;
import com.sun.messaging.jmq.jmsservice.BrokerEvent;
import com.sun.messaging.jmq.jmsservice.BrokerEventListener;

public class ImqBrokerExampleRunner {
	public void run(String[] args) throws Exception{
		
		// obtain the ClientRuntime singleton object
		ClientRuntime clientRuntime = ClientRuntime.getRuntime();
		
		// create the embedded broker instance
		BrokerInstance brokerInstance = clientRuntime.createBrokerInstance();
		
		// convert the specified broker arguments into Properties
		// this is a utility function: it doesn't change the broker
		Properties props = brokerInstance.parseArgs(args);
		
		// initialise the broker instance 
		// using the specified properties
		// and a BrokerEventListener
		BrokerEventListener listener = new ExampleBrokerEventListener();
		brokerInstance.init(props, listener);
		
		// now start the embedded broker		
		brokerInstance.start();
				
		System.out.println ("Embedded broker started");
		System.in.read();
	

	}
	
	public static void main(String[] args) throws Exception {
		
		ImqBrokerExampleRunner ebe = new ImqBrokerExampleRunner();
		ebe.run(args);
 
	}
	
	class ExampleBrokerEventListener implements BrokerEventListener {

		public void brokerEvent(BrokerEvent brokerEvent) {
	    	System.out.println ("Received broker event:"+brokerEvent);
		}

		public boolean exitRequested(BrokerEvent event, Throwable thr) {
	    	System.out.println ("Broker is requesting a shutdown because of:"+event+" with "+thr);
	    	
	    	// return true to allow the broker to shutdown
	    	return true;
		}
		
	}
}
