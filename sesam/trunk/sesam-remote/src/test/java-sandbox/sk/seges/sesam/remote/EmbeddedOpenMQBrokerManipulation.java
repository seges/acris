package sk.seges.sesam.remote;


import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.jms.ConnectionFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.sun.messaging.jmq.jmsclient.runtime.BrokerInstance;
import com.sun.messaging.jmq.jmsclient.runtime.ClientRuntime;
import com.sun.messaging.jmq.jmsservice.BrokerEvent;
import com.sun.messaging.jmq.jmsservice.BrokerEventListener;

/**
 * @author eldzi
 */
public class EmbeddedOpenMQBrokerManipulation implements IBrokerManipulation, ApplicationContextAware {
    private ApplicationContext context;
    private BrokerInstance brokerInstance;
    private BrokerEventListener listener;
    private Properties props;
    
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }
    
    public ConnectionFactory createFactory(String cfName) throws Exception {
        stop();
        start();
        return (ConnectionFactory) context.getBean(cfName);
    }

    public void tearDown() throws Exception {
        stop();
    }
    
    private void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClientRuntime clientRuntime = ClientRuntime.getRuntime();

        // create the embedded broker instance
        brokerInstance = clientRuntime.createBrokerInstance();

        // convert the specified broker arguments into Properties
        // this is a utility function: it doesn't change the broker
        URL defaultProperties = getClass().getClassLoader().getResource("imq/lib/props/broker/default.properties");
        String imqDir = new File(defaultProperties.getFile()).getParentFile().getParentFile().getParentFile().getParent();
        //target\\test-classes\\imq
        props = brokerInstance.parseArgs(new String[] {"-imqhome", imqDir, "-varhome", imqDir + File.separator + "var", "-name", "embeddedExample", "-port", "7676"});

        // initialise the broker instance
        // using the specified properties
        // and a BrokerEventListener
        listener = new BrokerEventListener() {

           public void brokerEvent(BrokerEvent arg0) {
               System.out.println("Broker event = " + arg0);
           }

           public boolean exitRequested(BrokerEvent arg0, Throwable arg1) {
               System.out.println("Exit requested =  " + arg0);
               arg1.printStackTrace();
               return true;
           }
            
        };
        brokerInstance.init(props, listener);
    }
    
    private void start() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        init();
        // now start the embedded broker
        brokerInstance.start();
    }
    
    private void stop() {
        if(brokerInstance != null && brokerInstance.isBrokerRunning()) {
            brokerInstance.shutdown();
        }
    }
}
