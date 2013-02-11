/**
 * 
 */
package sk.seges.sesam.remote.jmx;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import sk.seges.sesam.remote.domain.ProtocolConfiguration;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;
import sk.seges.sesam.remote.jmx.domain.Side;
import sk.seges.sesam.remote.jmx.domain.Status;
import sk.seges.sesam.remote.jmx.util.CollectorUtils;

/**
 * @author LGazo
 */
public class XInvocationCollector implements IInvocationCollector, DynamicMBean, XInvocationCollectorMBean {
    private OpenMBeanInfoSupport OMBEAN_INFO;
    private Side side;
    private JMXSupport support;

    private ProtocolConfiguration configuration;
    private List<InvocationInfo> infos = new LinkedList<InvocationInfo>();
    private Status status = Status.STOPPED;

    public XInvocationCollector() throws OpenDataException {
        support = new JMXSupport();
        constructMBeanInfo();
    }

    public MBeanInfo getMBeanInfo() {
        return OMBEAN_INFO;
    }

    private void constructMBeanInfo() throws OpenDataException {
        OMBEAN_INFO = new OpenMBeanInfoSupport(this.getClass().getName(), "Remote Invocation Open MBean",
                constructAttributes(), constructConstructors(), constructOperations(), constructNotifications());
    }

    private OpenMBeanAttributeInfoSupport[] constructAttributes() throws OpenDataException {
        OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[3];
        attributes[0] = new OpenMBeanAttributeInfoSupport("collectingStatus", "Status of collecting",
                SimpleType.STRING, true, false, false);
        attributes[1] = new OpenMBeanAttributeInfoSupport("configuration", "Bridge configuration",
                createConfigurationType(), true, false, false);
        attributes[2] = new OpenMBeanAttributeInfoSupport("numberOfInvocations", "Number of invocations",
                SimpleType.INTEGER, true, false, false);

        return attributes;
    }

    private OpenMBeanConstructorInfoSupport[] constructConstructors() {
        OpenMBeanConstructorInfoSupport[] constructors = new OpenMBeanConstructorInfoSupport[0];
        return constructors;
    }

    private MBeanNotificationInfo[] constructNotifications() {
        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[4];
        return notifications;
    }

    private OpenMBeanOperationInfoSupport createStartInvocationInfoCollectionOperation() {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[0];
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("startInvocationInfoCollection",
                "startInvocationInfoCollection", parameters, SimpleType.VOID, MBeanOperationInfo.ACTION);
        return operation;
    }

    private OpenMBeanOperationInfoSupport createStopInvocationInfoCollectionOperation() {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[0];
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("stopInvocationInfoCollection",
                "stopInvocationInfoCollection", parameters, SimpleType.VOID, MBeanOperationInfo.ACTION);
        return operation;
    }

    private OpenMBeanOperationInfoSupport createClearCollectedInfosOperation() {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[0];
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("clearCollectedInfos",
                "ClearCollectedInfos", parameters, SimpleType.VOID, MBeanOperationInfo.ACTION);
        return operation;
    }

    private OpenMBeanOperationInfoSupport createListCollectedInvocationInfosOperation() throws OpenDataException {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[0];
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("listCollectedInvocationInfos",
                "ListCollectedInvocationInfos", parameters, createInvocationInfosTabularType(),
                MBeanOperationInfo.ACTION);
        return operation;
    }
    
    private OpenMBeanOperationInfoSupport createPairOperation() throws OpenDataException {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[] {
                new OpenMBeanParameterInfoSupport("serviceURL", "JMX service URL of target for pairing", SimpleType.STRING)
        };
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("pair",
                "Pair", parameters, createInvocationInfosTabularType(),
                MBeanOperationInfo.ACTION);
        return operation;
    }
    
    private OpenMBeanOperationInfoSupport createExportOperation() throws OpenDataException {
        OpenMBeanParameterInfoSupport[] parameters = new OpenMBeanParameterInfoSupport[] {
                new OpenMBeanParameterInfoSupport("fileName", "CSV file name", SimpleType.STRING)
        };
        OpenMBeanOperationInfoSupport operation = new OpenMBeanOperationInfoSupport("export",
                "Export to CSV", parameters, SimpleType.VOID,
                MBeanOperationInfo.ACTION);
        return operation;
    }

    private OpenMBeanOperationInfoSupport[] constructOperations() throws OpenDataException {
        OpenMBeanOperationInfoSupport[] operations = new OpenMBeanOperationInfoSupport[] {
                createStartInvocationInfoCollectionOperation(), createStopInvocationInfoCollectionOperation(),
                createClearCollectedInfosOperation(), createListCollectedInvocationInfosOperation(), createPairOperation(), createExportOperation() };
        return operations;
    }

    private CompositeType createConfigurationType() throws OpenDataException {
        String[] names = support.constructFieldNames(ProtocolConfiguration.class);
        String[] descriptions = support.constructFieldDescriptions(ProtocolConfiguration.class);
        OpenType[] types = support.constructFieldTypes(ProtocolConfiguration.class);

        CompositeType type = new CompositeType("configuration", "Configuration", names, descriptions, types);

        return type;
    }

    public CompositeData getConfiguration() throws OpenDataException {
        Object[] object = support.constructFieldValues(configuration); 
        CompositeData data = new CompositeDataSupport(createConfigurationType(), support.constructFieldNames(ProtocolConfiguration.class), object);
        return data;
    }
    
    private CompositeType createInvocationInfoType() throws OpenDataException {
        String[] names = support.constructFieldNames(InvocationInfo.class);
        String[] descriptions = support.constructFieldDescriptions(InvocationInfo.class);
        OpenType[] types = support.constructFieldTypes(InvocationInfo.class);
        
        CompositeType type = new CompositeType("invocationInfo", "Invocation Info", names, descriptions, types);

        return type;
    }

    private TabularType createInvocationInfosTabularType() throws OpenDataException {
        String[] indexes = new String[] { "id" };
        TabularType type = new TabularType("invocationInfoTable", "Invocation Info Table", createInvocationInfoType(),
                indexes);
        return type;
    }

    public TabularData listCollectedInvocationInfos() throws OpenDataException {
        TabularDataSupport table = new TabularDataSupport(createInvocationInfosTabularType());

        int id = 0;
        for (InvocationInfo info : infos) {
            info.setId(id++);
            Object[] data = support.constructFieldValues(info);
            CompositeData infoData = new CompositeDataSupport(createInvocationInfoType(),
                    support.constructFieldNames(InvocationInfo.class), data);
            table.put(infoData);
        }
        return table;
    }

    public List<InvocationInfo> getCollectedInvocationInfos() {
        return infos;
    }
    
    public void startInvocationInfoCollection() {
        status = Status.STARTED;
    }

    public void stopInvocationInfoCollection() {
        status = Status.STOPPED;
    }

    public void addInvocationInfo(InvocationInfo info) {
        if (Status.STARTED.equals(status))
            infos.add(info);
    }

    public void clearCollectedInfos() {
        infos.clear();
    }

    public void setConfiguration(ProtocolConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getCollectingStatus() {
        if (status == null)
            return "N/A";
        return status.name();
    }
    
    public boolean shouldCollect() {
        return Status.STARTED.equals(status);
    }
    
    public int getNumberOfInvocations() {
        if(infos != null)
            return infos.size();
        else 
            return -1;
    }

    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if ("configuration".equals(attribute)) {
            try {
                return getConfiguration();
            } catch (OpenDataException e) {
                throw new MBeanException(e);
            }
        } else if ("collectingStatus".equals(attribute)) {
            return getCollectingStatus();
        } else if ("numberOfInvocations".equals(attribute)) {
            return getNumberOfInvocations();
        }
        return null;
    }

    public AttributeList getAttributes(String[] attributeNames) {
        if (attributeNames == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames[] cannot be null"),
                    "Cannot call getAttributes with null attribute names");
        }
        AttributeList resultList = new AttributeList();

        if (attributeNames.length == 0)
            return resultList;

        for (int i = 0; i < attributeNames.length; i++) {
            try {
                Object value = getAttribute((String) attributeNames[i]);
                resultList.add(new Attribute(attributeNames[i], value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (resultList);
    }

    public Object invoke(String operationName, Object[] params, String[] signature) throws MBeanException,
            ReflectionException {
        if (operationName == null) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"),
                    "Cannot call invoke with null operation name");
        }

//        if (operationName.equals("startInvocationInfoCollection")) {
//            startInvocationInfoCollection();
//            return null;
//        } else if (operationName.equals("stopInvocationInfoCollection")) {
//            stopInvocationInfoCollection();
//            return null;
//        } else if (operationName.equals("clearCollectedInfos")) {
//            clearCollectedInfos();
//            return null;
//        } else if (operationName.equals("listCollectedInvocationInfos")) {
//            try {
//                return listCollectedInvocationInfos();
//            } catch (OpenDataException e) {
//                throw new ReflectionException(e);
//            }
//        } else if (operationName.equals("pair")) {
//            return pair((String)params[0]);
//        } else if (operationName.equals("export")) {
//            export((String)params[0]);
//            return null;
//        } 

        Class[] paramClasses = new Class[params.length];
        for(int i = 0; i < params.length; i++) {
            paramClasses[i] = params[i].getClass();
        }
        
        try {
            Method method = getClass().getMethod(operationName, paramClasses);
            return method.invoke(this, params);
        } catch (Exception e) {
            throw new ReflectionException(e, "Cannot find the operation "
                    + operationName);
        }
        
        
//        throw new ReflectionException(new NoSuchMethodException(operationName), "Cannot find the operation "
//                + operationName);
    }

    
    
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
            MBeanException, ReflectionException {
        // TODO Auto-generated method stub

    }

    public AttributeList setAttributes(AttributeList attributes) {
        return new AttributeList(); // always empty
    }
    
    public void setSide(Side side) {
        this.side = side;
    }
    
    /**
     * 
     * @param serviceURL i.e. service:jmx:rmi:///jndi/rmi://:6666/jmxrmi
     * @return
     */
    public TabularData pair(String serviceURL) {
        if(Side.CLIENT.equals(side)) {
            return pairClientWithServers(serviceURL);
        } else if(Side.SERVER.equals(side)) {
            return pairServerWithClients(serviceURL);
        }
        throw new RuntimeException("Cannot determine on which side to pair... side = " + side);   
    }
    
    private TabularData pairServerWithClients(String serviceURL) {
        try {
            JMXServiceURL url =
                new JMXServiceURL(serviceURL);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection connection = jmxc.getMBeanServerConnection();
            
            Set<?> mbeans = connection.queryMBeans(CollectorUtils.constructName(Side.CLIENT), null);
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Cannot pair server with clients.", e);
        }
    }
    
    private TabularData pairClientWithServers(String serviceURL) {
        try {
            JMXServiceURL url =
                new JMXServiceURL(serviceURL);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection connection = jmxc.getMBeanServerConnection();
            
            Set<ObjectInstance> mbeans = connection.queryMBeans(CollectorUtils.constructName(Side.SERVER), null);
            TabularDataSupport table = new TabularDataSupport(createListOfInvocationsTabularType(mbeans.size()));
            int id = 0;
            
            for(InvocationInfo info : infos) {  
                Object[] data = new Object[mbeans.size() + 1];
                data[0] = Integer.valueOf(id++);
                int j = 1;
                for(ObjectInstance mbean : mbeans) {
                    XInvocationCollectorMBean collector = (XInvocationCollectorMBean)MBeanServerInvocationHandler.newProxyInstance(connection, mbean.getObjectName(), XInvocationCollectorMBean.class, false);
                    TabularData remoteInfos = collector.listCollectedInvocationInfos();
                    
                    for(CompositeData remoteInfoData : (Collection<CompositeData>)remoteInfos.values()) {
                        String uuid = (String)remoteInfoData.get("uuid");
                        if(info.getUuid() != null && info.getUuid().equals(uuid))
                            data[j++] = remoteInfoData;
                    }
                }
                
                CompositeData infoData = new CompositeDataSupport(createListOfInvocationsType(mbeans.size()),
                        createListOfInvocationNames(mbeans.size()), data);
                table.put(infoData);
            }
            
            return table;            
        } catch (Exception e) {
            throw new RuntimeException("Cannot pair clients with server.", e);
        }
    }
    
    private TabularType createListOfInvocationsTabularType(int count) throws OpenDataException {
        String[] indexes = new String[] { "id" };
        TabularType type = new TabularType("invocationInfoTables", "Invocation Info Tables", createListOfInvocationsType(count),
                indexes);
        return type;
    }
    
    private CompositeType createListOfInvocationsType(int count) throws OpenDataException {
        String[] names = new String[count + 1];
        String[] descriptions = new String[count + 1];
        OpenType[] types = new TabularType[count + 1];
        
        CompositeType ttype = createInvocationInfoType();
        
        names[0] = "id";
        descriptions[0] = "id";
        types[0] = SimpleType.INTEGER;
        
        for(int i = 1; i < count; i++) {
            names[i] = "invocation " + i;
            descriptions[i] = "invocation " + i;
            types[i] = ttype;
        }
        
        CompositeType type = new CompositeType("invocationInfos", "Invocation Infos", names, descriptions, types);

        return type;
    }

    private String[] createListOfInvocationNames(int count) throws OpenDataException {
        String[] names = new String[count + 1];
        
        names[0] = "id";
        
        for(int i = 1; i < count; i++) {
            names[i] = "invocation " + i;
        }
        return names;
    }
    
    private void export(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);

            String[] names = support.constructFieldNames(InvocationInfo.class);
            for (int i = 0; i < names.length; i++) {
                writer.append("\"" + names[i] + "\"");
                if (i != names.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            for (InvocationInfo info : infos) {
                Object[] values = support.constructFieldValues(info);
                for (int i = 0; i < values.length; i++) {
                    writer.append("\"" + values[i] + "\"");
                    if (i != values.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot export to file = " + fileName, e);
        }
    }
    

}
