package sk.seges.acris.jeesupport.tomcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.sun.messaging.jmq.jmsclient.runtime.BrokerInstance;
import com.sun.messaging.jmq.jmsclient.runtime.ClientRuntime;
import com.sun.messaging.jmq.jmsservice.BrokerEvent;
import com.sun.messaging.jmq.jmsservice.BrokerEventListener;


public class ImqBrokerListener implements LifecycleListener {
	private static final String[] keys = {"imq.port", "imq.imqhome", "imq.varhome", "imq.name"};
	private final Log log = LogFactory.getLog(ImqBrokerListener.class);
	
	private BrokerInstance brokerInstance;
	
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		if (Lifecycle.START_EVENT.equals(event.getType())) {
			start();
		} else if (Lifecycle.STOP_EVENT.equals(event.getType())) {
			brokerInstance.stop();
			brokerInstance.shutdown();
			log.info("Broker shut down");
		}
	}
	
	private void start() {
		// obtain the ClientRuntime singleton object
		ClientRuntime clientRuntime = ClientRuntime.getRuntime();

		// create the embedded broker instance
		
		try {
			brokerInstance = clientRuntime.createBrokerInstance();
		} catch (Exception e) {
			throw new RuntimeException("Problem initializing the broker", e);
		}

		String[] args = retrieveArguments();
		// convert the specified broker arguments into Properties
		// this is a utility function: it doesn't change the broker
		Properties props = brokerInstance.parseArgs(args);

		BrokerEventListener listener = new BrokerEventListener() {
			
			@Override
			public boolean exitRequested(BrokerEvent event, Throwable t) {
				log.info("Broker exit requested, event = " + event, t);
				return true;
			}
			
			@Override
			public void brokerEvent(BrokerEvent event) {
				log.info("Broker event = " + event);
			}
		};
		// initialise the broker instance 
		// using the specified properties
		// and a BrokerEventListener
		brokerInstance.init(props, listener);

		// now start the embedded broker
		brokerInstance.start();
		log.info("Broker started with arguments " + Arrays.toString(args));
	}

	private String[] retrieveArguments() {
		List<String> args = new ArrayList<String>();
		
		for(String key : keys) {
			String value = System.getProperty(key);
			if(value != null && !value.isEmpty()) {
				args.add("-" + key.substring(4));
				args.add(value);
			}
		}
		
		String[] array = new String[args.size()];
		return args.toArray(array);
	}
}
