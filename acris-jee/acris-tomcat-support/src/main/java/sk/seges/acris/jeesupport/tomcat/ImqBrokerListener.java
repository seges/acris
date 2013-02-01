package sk.seges.acris.jeesupport.tomcat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	public static final String CONFIG_DEFAULT_PROPERTIES_FILE = "lib" + File.separator + "props" + File.separator + "broker" + File.separator + "default.properties";
	public static final String CONFIG_INSTALL_PROPERTIES_FILE = "lib" + File.separator + "props" + File.separator + "broker" + File.separator + "install.properties";

	private static final String IMQ_NAME = "imq.name";
	private static final String IMQ_VARHOME = "imq.varhome";
	private static final String IMQ_IMQHOME = "imq.imqhome";
	private static final String IMQ_PORT = "imq.port";
	
	private static final String[] keys = {IMQ_PORT, IMQ_IMQHOME, IMQ_VARHOME, IMQ_NAME};
	private final Log log = LogFactory.getLog(ImqBrokerListener.class);
	
	private BrokerInstance brokerInstance;
	
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		if (Lifecycle.START_EVENT.equals(event.getType())) {
			checkEnvironment();
			start();
		} else if (Lifecycle.STOP_EVENT.equals(event.getType())) {
			brokerInstance.stop();
			brokerInstance.shutdown();
			log.info("Broker shut down");
		}
	}
	
	private void checkEnvironment() {
		String catalinaHome = System.getenv("CATALINA_HOME");
		if(catalinaHome == null || catalinaHome.isEmpty()) {
			catalinaHome = System.getProperty("catalina.home");	
		}
		if(catalinaHome == null || catalinaHome.isEmpty()) {
			log.warn("Unable to locate Catalina home therefore environment might not be set up properly");
			return;
		}
		
		String imqhome = System.getProperty(IMQ_IMQHOME);
		if(imqhome == null || imqhome.isEmpty()) {
			imqhome = catalinaHome + File.separator + "mq";
			System.setProperty(IMQ_IMQHOME, imqhome);
		}
		
		String varhome = System.getProperty(IMQ_VARHOME);
		if(varhome == null || varhome.isEmpty()) {
			varhome = catalinaHome + File.separator + "mq" + File.separator + "var";
			System.setProperty(IMQ_VARHOME, varhome);
		}
		
		String property = System.getProperty(IMQ_NAME);
		if(property == null || property.isEmpty()) {
			System.setProperty(IMQ_NAME, "imqEmbedded");
		}
		
		property = System.getProperty(IMQ_PORT);
		if(property == null || property.isEmpty()) {
			System.setProperty(IMQ_PORT, "7676");
		}
		
		File defaultProperties = new File(imqhome, CONFIG_DEFAULT_PROPERTIES_FILE);
		File installProperties = new File(imqhome, CONFIG_INSTALL_PROPERTIES_FILE);
		
		File dir = defaultProperties.getParentFile();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		copy(CONFIG_DEFAULT_PROPERTIES_FILE, defaultProperties);
		copy(CONFIG_INSTALL_PROPERTIES_FILE, installProperties);
	}
	
	private void copy(String sourcePath, File target) {
		if(!target.exists()) {
			InputStream source = getClass().getResourceAsStream("environment/provided/mq/" + sourcePath.replace(File.separator, "/"));
			try {
				copy(source, target);
			} catch (IOException e) {
				log.error("Unable to create default environment, source = " + sourcePath + ", target = " + target, e);
			}
		}
	}
	
	/**
	 * TODO: when migrated to JDK7 - http://docs.oracle.com/javase/tutorial/essential/io/copy.html
	 * @throws IOException 
	 */
	private void copy(InputStream in, File target) throws IOException {
        //For Append the file.
        //OutputStream out = new FileOutputStream(f2,true);

        //For Overwrite the file.
        OutputStream out = new FileOutputStream(target);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0){
          out.write(buf, 0, len);
        }
        in.close();
        out.close();
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
