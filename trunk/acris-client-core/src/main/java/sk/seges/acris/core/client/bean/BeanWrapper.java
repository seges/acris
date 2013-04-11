package sk.seges.acris.core.client.bean;

/**
 * @author eldzi
 */
public interface BeanWrapper<T> {
	public static final String CONTENT = "beanWrapperContent";
	
	/**
	 * Used for setting wrapped content. BeanWrapper delegates all get and set
	 * method requests to this instance. If no value is set, than
	 * {@link NullPointerException} are thrown because bean wrapper over the
	 * null object is not valid.
	 */
	void setBeanWrapperContent(T content);

	/**
	 * User can obtain original bean from bean wrapper with set values using
	 * this method.
	 */
	T getBeanWrapperContent();

	/**
	 * Method will delegate request to read method for appropriate property in
	 * wrapped bean
	 * 
	 * <pre>
	 * @code getAttribute("name")}
	 * </pre>
	 * 
	 * will call:
	 * 
	 * <pre>
	 * @code content.getName();}
	 * </pre>
	 */
	Object getBeanAttribute(String attr);

	/**
	 * Method will delegate request to write method for appropriate property in
	 * wrapped bean.
	 * 
	 * <pre>
	 * @code setBeanAttribute("name","test")}
	 * </pre>
	 * 
	 * will call:
	 * 
	 * <pre>
	 * @code content.setName("test");}
	 * </pre>
	 */
	void setBeanAttribute(String attr, Object value);
}