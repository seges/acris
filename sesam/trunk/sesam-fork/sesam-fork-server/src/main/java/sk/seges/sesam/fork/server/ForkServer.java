/**
 * 
 */
package sk.seges.sesam.fork.server;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.sesam.fork.server.service.JavaForkProcessService;
import sk.seges.sesam.fork.shared.service.RemoteProcessService;
import sk.seges.sesam.remote.domain.EJMSResponseHandling;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.server.JMSCommandListener;




/**
 * @author ladislav.gazo
 * 
 */
public class ForkServer {
	private static final Logger log = LoggerFactory.getLogger(ForkServer.class);
	
	public static void main(String[] args) throws Exception {
		if(args.length != 1) {
			System.out.println("Usage: " + ForkServer.class + " <name>");
			System.exit(84);
		}
		
		String name = args[0].trim();
		
		log.info("Name = {}", name);
		
		InitialContext context = new InitialContext();
		
		String compEnvPrefix = "java:comp/env/";
		QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup(compEnvPrefix + name + "/remoteProcessQCF");
		Queue queue = (Queue) context.lookup(compEnvPrefix + name + "/remoteProcessQueue");
		Queue response = (Queue) context.lookup(compEnvPrefix + name + "/remoteProcessResponseQueue");
		
		JavaForkProcessService service = new JavaForkProcessService();

		JMSBridgeConfiguration configuration = new JMSBridgeConfiguration(qcf, queue);
		configuration.setResponseHandling(EJMSResponseHandling.STATIC_QUEUE);
		configuration.setResponseDestination(response);
		configuration.setInvocationTimeout(500000);
		final JMSCommandListener<RemoteProcessService> listener = new JMSCommandListener<RemoteProcessService>(configuration, service);
		listener.start();
		
		log.info("Listening started");
		Thread sh = new Thread() {
			@Override
			public void run() {
				super.run();
				listener.destroy();
			}
		};
		Runtime.getRuntime().addShutdownHook(sh );
				
		System.in.read();
		
	}
}
