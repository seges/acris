package sk.seges.acris.core.rebind;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import sk.seges.acris.core.rebind.GenPredicGroup.EOperator;
import sk.seges.acris.core.rebind.GeneratorPredicate.EPredicts;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.cfg.CompoundCondition;
import com.google.gwt.dev.cfg.Condition;
import com.google.gwt.dev.cfg.ConditionAll;
import com.google.gwt.dev.cfg.ConditionAny;
import com.google.gwt.dev.cfg.ConditionNone;
import com.google.gwt.dev.cfg.ConditionWhenPropertyIs;
import com.google.gwt.dev.cfg.ConditionWhenTypeAssignableTo;
import com.google.gwt.dev.cfg.ConditionWhenTypeIs;
import com.google.gwt.dev.cfg.Conditions;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.Rule;
import com.google.gwt.dev.cfg.RuleGenerateWith;
import com.google.gwt.dev.cfg.RuleReplaceWith;
import com.google.gwt.dev.cfg.Rules;

/**
 * abstract generator class - parent of all custom generators which are
 * going to be combined(mixed) together
 * 
 * all dependencies between interfaces and generators are stored in
 * LinkedMaps {@link GeneratorChain}
 * 
 * @author PSimun
 * 
 */
public abstract class AbstractGenerator extends Generator {
	/**
	 * superclassName - actual name of superclass, need for generators chain
	 */
	protected String superclassName;

	/**
	 * conditions - rules
	 */
	private GenPredicGroup conditions = new GenPredicGroup();

	/**
	 * 
	 * @param superclassName
	 */
	public void setSuperclassName(String superclassName) {
		this.superclassName = superclassName;
	}

	/**
	 * main method for generating source code
	 * 
	 * @param logger
	 * @param context
	 * @param typeName
	 * @return name of newly generated class
	 * @throws UnableToCompleteException
	 */
	abstract public String doGenerate(TreeLogger logger,
			GeneratorContext context, String typeName)
			throws UnableToCompleteException;

	/**
	 * via java reflection returns value of private field of instance
	 * 
	 * @param instance
	 * @param field
	 * @return
	 */
	private Object getPrivateField(Object instance, String field) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param logger
	 * @param context
	 * @param typeName
	 * @param gr
	 * @return
	 */
	public boolean isGenerate(TreeLogger logger, GeneratorContext context,
			String typeName, GenPredicGroup gr) {
		boolean result = false;
		if ((gr.getMyGroups() != null) && (gr.getMyGroups().size() > 0)) {
			for (GenPredicGroup group : gr.getMyGroups()) {
				switch (gr.getOperator()) {
				case ANY:
					result = result
							|| isGenerate(logger, context, typeName, group);
					break;
				case NONE:
					result = !isGenerate(logger, context, typeName, group);
					if (!result)
						return false;
					break;
				case ALL:
					result = isGenerate(logger, context, typeName, group);
					if (!result)
						return false;
				default:
					break;
				}
			}
		}
		if ((gr.getGeneratorPredicts() != null)
				&& (gr.getGeneratorPredicts().size() > 0)) {
			for (GeneratorPredicate entry : gr.getGeneratorPredicts()) {
				String value = null;
				try {
					switch (entry.getEPredict()) {
					case ASSIGNABLE:
						if (context.getTypeOracle().findType(typeName)
								.isAssignableTo(
										context.getTypeOracle().getType(
												entry.getValue()))) {
							value = entry.getValue();
						} else {
							value = typeName;
						}
						break;
					case TYPEIS:
						value = typeName;
						break;
					case PROPERTY:
						value = context.getPropertyOracle().getPropertyValue(
								logger, entry.getName());
						break;
					default:
						value = null;
						break;
					}
				} catch (Exception e) {
					value = null;
					result = false;
				}

				if (value != null) {
					switch (gr.getOperator()) {
					case ANY:
						result = result || entry.getValue().equals(value);
						break;
					case NONE:
						result = !entry.getValue().equals(value);
						if (!result)
							return false;
						break;
					case ALL:
						result = entry.getValue().equals(value);
						if (!result)
							return false;
					default:
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * gets GeneratorPredicate from condition
	 * 
	 * @param cond
	 *            Condition
	 * @return GeneratorPredicate
	 */
	private GeneratorPredicate getGP(Condition cond) {
		GeneratorPredicate gp = new GeneratorPredicate();
			// ===================when assignable to
		if (cond instanceof ConditionWhenTypeAssignableTo) {
			gp.setEPredict(EPredicts.ASSIGNABLE);
			gp.setName(GeneratorPredicate.CLASS);
			gp.setValue(((ConditionWhenTypeAssignableTo) cond)
					.getAssignableToTypeName());
			// ===================when type is
		} else if (cond instanceof ConditionWhenTypeIs) {
			gp.setEPredict(EPredicts.TYPEIS);
			gp.setName(GeneratorPredicate.CLASS);
			gp.setValue((String) getPrivateField(cond, "exactTypeName"));
			// ===================when property is
		} else if (cond instanceof ConditionWhenPropertyIs) {
			gp.setEPredict(EPredicts.PROPERTY);
			gp.setName((String) getPrivateField(
					((ConditionWhenPropertyIs) cond), "propName"));
			gp.setValue((String) getPrivateField(
					((ConditionWhenPropertyIs) cond), "value"));
		}
		return gp;
	}

	/**
	 * according to .gwt.xml files generates a LinkedMap which has
	 * interfaces as keys array of generators as values keys are sorted
	 * according order of <generate-with> elements in .gwt.xml files
	 * 
	 * @param context
	 * @throws UnableToCompleteException 
	 */
	private void fillUpGeneratorChainMap(TreeLogger logger,GeneratorContext context) throws UnableToCompleteException {
				
		GeneratorChain.customGenerators = new LinkedList<AbstractGenerator>();
		GeneratorChain.replacers = new LinkedList<AbstractGenerator>();
		GeneratorChain.thirdPartyGenerators = new LinkedHashMap<Generator, AbstractGenerator>();
		ModuleDef moduleDef = (ModuleDef) getPrivateField(context,
				"module");
		Rules rules = moduleDef.getRules();
		Iterator<Rule> rulesIter = rules.iterator();
		while (rulesIter.hasNext()) {
			Rule rul = rulesIter.next();
			Generator gen = null;

			// =================replace with
			if (rul instanceof RuleReplaceWith) {
				String replaceClass = (String) getPrivateField(rul,
						"replacementTypeName");
				gen = new ReplaceByGenerator(replaceClass);
				// gen = null;

				// =================generate with
			} else if (rul instanceof RuleGenerateWith) {
				Class<? extends Generator> generatorClass = (Class<? extends Generator>)getPrivateField(rul, "generatorClass");
				Constructor<?> constructor;
				try {
					constructor = generatorClass.getDeclaredConstructor();
				} catch (Exception e) {
					logger.log(Type.ERROR, "Unable to obtain default constructor of generator " + generatorClass);
					throw new UnableToCompleteException();
				}
			    constructor.setAccessible(true);
			    try {
					gen = (Generator)constructor.newInstance();
				} catch (Exception e) {
					logger.log(Type.ERROR, "Unable to create instance of generator " + generatorClass);
					throw new UnableToCompleteException();
				}
			}

			if (gen != null) {
				if (gen instanceof AbstractGenerator) {
					GenPredicGroup newGroup = null;
					AbstractGenerator myGen = (AbstractGenerator) gen;
					if (GeneratorChain.customGenerators.contains(gen) || GeneratorChain.replacers.contains(gen)) {
						newGroup = addPredicsToExisting(rul, myGen.getConditions());
						myGen.setConditions(newGroup);
					} else {
						newGroup = getGroupConditions(rul.getRootCondition().getConditions(), null);
						myGen.setConditions(newGroup);
						if(gen instanceof ReplaceByGenerator){
							GeneratorChain.replacers.addFirst(myGen);
						}else{
							GeneratorChain.customGenerators.addFirst(myGen);
						}
					}
					
				} else {
					if(GeneratorChain.thirdPartyGenerators.containsKey(gen)){
						AbstractGenerator myGen = GeneratorChain.thirdPartyGenerators.get(gen);
						GenPredicGroup newGroup = addPredicsToExisting(rul, myGen.getConditions());
						myGen.setConditions(newGroup);
						GeneratorChain.thirdPartyGenerators.put(gen, myGen);
					}else{
						AbstractGenerator myGen = new AbstractGenerator() {
	
									@Override
									public String doGenerate(TreeLogger logger,
											GeneratorContext context,
											String typeName)
											throws UnableToCompleteException {
										return null;
									}
								};
						myGen.setConditions(getGroupConditions(rul.getRootCondition().getConditions(), null));
						GeneratorChain.thirdPartyGenerators.put(gen, myGen);
					}
				}
			}
		}
	}

	/**
	 * @param rul
	 * @param myGen
	 * @return
	 */
	private GenPredicGroup addPredicsToExisting(Rule rul, GenPredicGroup previousConds) {
		List<GenPredicGroup> list = new ArrayList<GenPredicGroup>();
		GenPredicGroup gpg = getGroupConditions(rul
				.getRootCondition().getConditions(), null);
		list.add(gpg);
		list.add(previousConds);
		GenPredicGroup newGroup = new GenPredicGroup();
		newGroup.setMyGroups(list);
		newGroup.setOperator(EOperator.ANY);
		return newGroup;
	}

	/**
	 * recursive method to get all conditions from "condition tree"
	 * 
	 * @param conds
	 *            - at the beginning root conditions, at lower level conditions
	 *            under CompoundCondition
	 * @param op
	 *            - at the beginning null, at lower level operator of Compound
	 *            Condition
	 * @return group of its subgroups and of conditions
	 */
	private GenPredicGroup getGroupConditions(Conditions conds,
			GenPredicGroup.EOperator op) {
		GenPredicGroup myConds = new GenPredicGroup();
		myConds.setOperator((op != null) ? op : GenPredicGroup.EOperator.ALL);
		Iterator<Condition> it = conds.iterator();
		while (it.hasNext()) {
			Condition cond = it.next();
			// if true, a new condition group is created and filled
			if (cond instanceof CompoundCondition) {
				// set group operator
				GenPredicGroup.EOperator actualOp = null;
				if (cond instanceof ConditionAll) {
					actualOp = GenPredicGroup.EOperator.ALL;
				} else if (cond instanceof ConditionAny) {
					actualOp = GenPredicGroup.EOperator.ANY;
				} else if (cond instanceof ConditionNone) {
					actualOp = GenPredicGroup.EOperator.NONE;
				}

				if (myConds.getMyGroups() == null) {
					myConds.setMyGroups(new ArrayList<GenPredicGroup>());
				}
				// call recursively to get conditions of actual group
				myConds.getMyGroups().add(
						getGroupConditions(((CompoundCondition) cond)
								.getConditions(), actualOp));
			} else {
				// get condition
				if (myConds.getGeneratorPredicts() == null) {
					myConds
							.setGeneratorPredicts(new ArrayList<GeneratorPredicate>());
				}
				myConds.getGeneratorPredicts().add(getGP(cond));
			}
		}

		return myConds;
	}

	/**
	 * gets generator chain for typeName, calls every generator from chain with
	 * superclass name set to previously generated class
	 * 
	 * 
	 * @param logger
	 * @param context
	 * @param typeName
	 * @return name of generated class
	 * @throws UnableToCompleteException
	 */
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		if (GeneratorChain.customGenerators == null || GeneratorChain.context != context) {
			fillUpGeneratorChainMap(logger, context);
			GeneratorChain.context = context;
			
//			new RemoteServiceGinInjectorGenerator().generate(logger, context, RemoteServiceDefinition.class.getCanonicalName());
		}

//		if (!BeansBindingInit.isInitialized()) {
//			BeansBindingInit.setInitialized(true);
//			BeansBindingInitGenerator beansBindingInitGenerator = new BeansBindingInitGenerator();
//			beansBindingInitGenerator.generate(logger, context, BeansBindingInit.class.getCanonicalName());
//			//GWT.create(BeansBindingInit.class);
//		}

		//can be changed by generator
		String superClass = "";		
		//can be changed by replacer
		String actualTypeName = typeName;
		
		
		//Replace-with rules
		List<AbstractGenerator> replacers = GeneratorChain.getReplaceByChain(context, typeName);
		for (AbstractGenerator replacer : replacers) {
			if (replacer.isGenerate(logger, context, typeName, replacer
					.getConditions())) {
				actualTypeName = replacer.doGenerate(logger, context,
						actualTypeName);
			}
			
		}
		
		// generators from third party - can get only one (last) generator
		LinkedHashMap<Generator, AbstractGenerator> thirdPartyGens = GeneratorChain
				.getThirdPartyGenerators();
		for (Entry<Generator, AbstractGenerator> entry : thirdPartyGens
				.entrySet()) {
			Generator gen = (Generator) entry.getKey();
			AbstractGenerator aGen = (AbstractGenerator) entry.getValue();
			if (superClass != null && !("".equals(superClass))) {
				aGen.setSuperclassName(superClass);
			} else {
				aGen.setSuperclassName(actualTypeName);
			}
			if (aGen.isGenerate(logger, context, actualTypeName, aGen.getConditions())) {
				superClass = gen.generate(logger, context, actualTypeName);
			}
		}

		//custom generators
		List<AbstractGenerator> generators = GeneratorChain.getGeneratorChain(
				context, typeName);

//		List<AbstractGenerator> awaitingGeneratos = new ArrayList<AbstractGenerator>();
//		for (AbstractGenerator abstractGenerator : generators) {
//			awaitingGeneratos.add(abstractGenerator);
//		}
//		
		superClass = generateByCustomGenerators(logger, context, typeName, superClass, actualTypeName, generators);
		
		if (superClass.length() == 0) {
			return actualTypeName;
		}
		
		return superClass;
	}

	private String generateByCustomGenerators(TreeLogger logger, GeneratorContext context,
			String typeName, String superClass, String actualTypeName, List<AbstractGenerator> awaitingGeneratos) throws UnableToCompleteException {
		
//		AbstractGenerator processedGenerator = null;
		
		for (AbstractGenerator generator : awaitingGeneratos) {
			if (superClass != null && !("".equals(superClass))) {
				generator.setSuperclassName(superClass);
			} else {
				generator.setSuperclassName(actualTypeName);
			}

			if (generator.isGenerate(logger, context, actualTypeName, generator
					.getConditions())) {
				superClass = generator.doGenerate(logger, context,
						actualTypeName);
//				processedGenerator = generator;
//				break;
			}
		}
		
//		if (processedGenerator != null) {
//			awaitingGeneratos.remove(processedGenerator);
//			superClass = generateByCustomGenerators(logger, context, typeName, superClass, actualTypeName, awaitingGeneratos);
//		}
		
		return superClass; 
	}
	
	public GenPredicGroup getConditions() {
		return conditions;
	}

	public void setConditions(GenPredicGroup conditions) {
		this.conditions = conditions;
	}

}
