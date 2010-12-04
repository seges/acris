package sk.seges.acris.core.rebind;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;

/**
 * storage of replacers and generators divided in three groups:
 * LinkedList of customGenerators
 * LinkedList of replacers
 * LinkedHashMap of thirdPartyGenerators
 */
public class GeneratorChain {
	static LinkedList<AbstractGenerator> customGenerators;
	
	static LinkedList<AbstractGenerator> replacers;
	
	/**
	 * the key is defined generator
	 * the value is new AbstractGenerator with conditions
	 */
	static LinkedHashMap<Generator, AbstractGenerator> thirdPartyGenerators;
	
	public static LinkedList<AbstractGenerator> getGeneratorChain(
			GeneratorContext context, String typeName) {
		return customGenerators;
	}

	public static LinkedList<AbstractGenerator> getReplaceByChain(
			GeneratorContext context, String typeName) {
		return replacers;
	}
	
	
	public static LinkedHashMap<Generator, AbstractGenerator> getThirdPartyGenerators() {
		return thirdPartyGenerators;
	}	
}