package org.gwt.beansbinding.core.client;

/**
 * Provides default converters defined in {@link Converter}. <p>There are basic types
 * of converters defined in beans binding library and this provider interconnects our modules 
 * with third party beans binding library. </p>
 * <p>Converters are mostly used for converting basic types to <code>String</code> representation
 * and back from <code>String</code> to basic types (like. <code>Byte</code>, <code>Short</code>, 
 * <code>Long</code>, <code>Integer</code>, ...)</p>
 * <p>See {@link Converter} for more information how to use converters or how to define own
 * converters </p> 
 *  
 * @author fat
 */
public class ConverterProvider {
	public static final Object defaultConvert(Object source, Class<?> targetType) {
	    return Converter.defaultConvert(source, targetType);
	}
}
