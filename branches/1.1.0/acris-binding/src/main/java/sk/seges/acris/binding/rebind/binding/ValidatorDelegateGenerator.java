/**
 * 
 */
package sk.seges.acris.binding.rebind.binding;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.client.interfaces.IInternalValidator;
import com.google.gwt.validation.client.interfaces.IValidator;
import com.google.gwt.validation.rebind.InterfaceTypeNameStrategy;
import com.google.gwt.validation.rebind.TypeStrategy;
import com.google.gwt.validation.rebind.ValidatorGenerator;

/**
 * gwt-validation ValidatorGenerator delegate which does not generate a
 * validator class extending from different superclass but delegates calls to
 * the generator and is proxying validation behaviour. It is also in generator
 * chain if necessary (what is the case when there is e.g. bean wrapper
 * generator on top of domain object and you want to do a validation also).
 * 
 * @author ladislav.gazo
 */
public class ValidatorDelegateGenerator extends AbstractGenerator {
	private static final String InvalidConstraintSN = InvalidConstraint.class.getSimpleName();
	
	private TreeLogger logger;
	private GeneratorContext context;
	private TypeOracle typeOracle;
	private String typeName;
	
	private JClassType classType;
	
	/* (non-Javadoc)
	 * @see sk.seges.acris.rebind.AbstractGenerator#doGenerate(com.google.gwt.core.ext.TreeLogger, com.google.gwt.core.ext.GeneratorContext, java.lang.String)
	 */
	@Override
	public String doGenerate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;		
		this.typeName = typeName;
		this.typeOracle = context.getTypeOracle();
		
		return create();
	}

	public String create() {
		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Unable to find type name = " + typeName, e);
			return null;
		}

		TypeStrategy typeStrategy = new InterfaceTypeNameStrategy(BeanWrapper.class, "BeanWrapper");
		typeStrategy.setGeneratorContext(context);
		String beanTypeName = typeStrategy.getBeanTypeName(typeName);

		SourceWriter source = getSourceWriter(classType, beanTypeName);
		
		if(source != null) {
			//generateBeanInfoConnection(source, classType, beanTypeName, getFullReturnName());
			
			// generate real validation class for the object
			ValidatorGenerator delegatedGen = new BeanWrapperDrivenValidatorGenerator();
			try {
				delegatedGen.generate(logger, context, typeName);
			} catch (UnableToCompleteException e) {
				logger.log(TreeLogger.ERROR, "Unable to call delegated generator", e);
				return null;
			}
			
			source.println("private " + typeName + "Validator delegated = new " + typeName + "Validator();");
			//implement all interface methods
			source.println("public Set<" + InvalidConstraintSN + "<" + beanTypeName + ">> validate(" + beanTypeName + " object, String... groups) { return delegated.validate(object, groups); }");
			source.println("public Set<"+ InvalidConstraintSN + "<" + beanTypeName + ">> validateProperty(" + beanTypeName + " object,String propertyName,String... groups) { return delegated.validateProperty(object, propertyName, groups); }");
			source.println("public Set<" + InvalidConstraintSN + "<" + beanTypeName + ">> performValidation(" + beanTypeName + " object, String propertyName, List<String> groups, Set<String> processedGroups, Set<String> processedObjects) { return delegated.performValidation(object, propertyName, groups, processedGroups, processedObjects); }");
			
			source.commit(logger);
		}
		return getFullReturnName();
	}
	
	/*private void generateBeanInfoConnection(SourceWriter source, JType type, String mainBean, String delegatedFullName) {
		final String gwtBeanInfo = "com.googlecode.gwtx.java.introspection.client.GwtBeanInfo";
		
		source.println("static {");
		source.indent();
		source.println(BeanWrapperIntrospector.class.getCanonicalName() + ".init();");
		source.println("try {");
		source.indent();

		final String introsp = Introspector.class.getCanonicalName();
		// in devel mode there is GenericBeanInfo instead of GwtBeanInfo and the following code cannot be executed - GwtIntrospector won't accept it
		source.println("if (" + GWT.class.getCanonicalName() + ".isScript()) {");
		source.indent();
		// connecting it with wrapper is probably useless and should be done in wrapper generator but to be sure it is also here....
		source.println(introsp + ".setBeanInfo( " + type.getQualifiedSourceName()
				+ ".class, (" + gwtBeanInfo + ")" + introsp + ".getBeanInfo(" + mainBean + ".class) );");
		// and of course connect the delegator
		source.println(introsp + ".setBeanInfo( " + delegatedFullName
				+ ".class, (" + gwtBeanInfo + ")" + introsp + ".getBeanInfo(" + mainBean + ".class) );");
		source.outdent();
		source.println("}");
		
		source.outdent();
		source.println("} catch(Exception e) {");
		source.indent();
		source.println("throw new RuntimeException(\"Unable to initialize bean wrapper introspection.\", e);");
		source.outdent();
		source.println("}");
		source.outdent();
		source.println("}");
	}*/

	private String getFullReturnName() {
		return classType.getPackage().getName() + "." + getSimpleReturnName();
	}

	private String getSimpleReturnName() {
		return classType.getSimpleSourceName() + "Delegate";
	}
	
	
	private SourceWriter getSourceWriter(JClassType classType, String beanTypeName) {
		String packageName = classType.getPackage().getName();
		String simpleName = getSimpleReturnName();
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);

		composer.setSuperclass(superclassName);
		composer.addImplementedInterface(IValidator.class.getCanonicalName() + "<" + beanTypeName + ">");
		composer.addImplementedInterface(IInternalValidator.class.getCanonicalName() + "<" + beanTypeName + ">");
		
		composer.addImport(IValidator.class.getCanonicalName());
		composer.addImport(InvalidConstraint.class.getCanonicalName());
		composer.addImport(Set.class.getCanonicalName());
		composer.addImport(List.class.getCanonicalName());
		
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
}
