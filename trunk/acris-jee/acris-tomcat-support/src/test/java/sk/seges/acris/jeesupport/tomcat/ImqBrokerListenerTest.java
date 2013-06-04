/**
 * 
 */
package sk.seges.acris.jeesupport.tomcat;

import static org.junit.Assert.assertTrue;

import java.io.File;

import mockit.Mocked;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.junit.Test;

/**
 * @author ladislav.gazo
 */
public class ImqBrokerListenerTest {
	@Mocked
	Lifecycle lifecycle;
	
	@Test
	public void testCreatingDefaultDir() {
		File root = new File("." + File.separator + "target" + File.separator + System.currentTimeMillis());
		System.setProperty("catalina.home", root.getAbsolutePath());
		ImqBrokerListener listener = new ImqBrokerListener();
		LifecycleEvent event = new LifecycleEvent(lifecycle, Lifecycle.START_EVENT, null);
		listener.lifecycleEvent(event);
		File file = new File(root, "mq" + File.separator + ImqBrokerListener.CONFIG_DEFAULT_PROPERTIES_FILE);		
		assertTrue(file.exists());
		file = new File(root, "mq" + File.separator + "var" + File.separator + "instances" + File.separator + "imqEmbedded");
		assertTrue(file.exists());
	}
}
