/**
 * 
 */
package sk.seges.sesam.remote.jmx;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;

/**
 * @author LGazo
 */
public interface XInvocationCollectorMBean {
    CompositeData getConfiguration() throws OpenDataException;
    void startInvocationInfoCollection();
    void stopInvocationInfoCollection();
    TabularData listCollectedInvocationInfos() throws OpenDataException;
    void clearCollectedInfos();
}
