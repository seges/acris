/**
 * 
 */
package sk.seges.acris.bpm.client.engine.activity.bpmn;

/**
 * @author ladislav.gazo
 */
public interface BpmnConstants {
	static final String PROPERTYNAME_CONDITION = "condition";
	public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";
	public static final String PROPERTYNAME_INITIAL = "initial";
	
	public static final String ACTIVITI_PREFIX = "activiti:";
	
	  /**
	   * The BPMN 2.0 namespace
	   */
	  public static final String BPMN20_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";
	  
	  /**
	   * The location of the BPMN 2.0 XML schema.
	   */
	  public static final String BPMN_20_SCHEMA_LOCATION = "org/activiti/impl/bpmn/parser/BPMN20.xsd";

	  /**
	   * The namespace of the Activiti custom BPMN extensions.
	   */
	  public static final String ACTIVITI_BPMN_EXTENSIONS_NS = "http://activiti.org/bpmn";
	  
	  /**
	   * The namepace of the BPMN 2.0 diagram interchange elements.
	   */
	  public static final String BPMN_DI_NS = "http://www.omg.org/spec/BPMN/20100524/DI";
	  
	  /**
	   * The namespace of the BPMN 2.0 diagram common elements.
	   */
	  public static final String BPMN_DC_NS = "http://www.omg.org/spec/DD/20100524/DC";
	  
	  /**
	   * The namespace of the generic OMG DI elements (don't ask me why they didnt use the BPMN_DI_NS ...)
	   */
	  public static final String OMG_DI_NS = "http://www.omg.org/spec/DD/20100524/DI";

	  /**
	   * The Schema-Instance namespace.
	   */
	  public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

}
