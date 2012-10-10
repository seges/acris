package sk.seges.acris.core.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.RebindMode;
import com.google.gwt.core.ext.RebindResult;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class RemoteServiceWrapperCreator {

	private static final String PROVIDER_SUFFIX = "Provider";
	private static final String ASYNC_WRAPPER_SUFFIX = "AsyncWrapper";
	
	/**
	 * The {@code TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	private JClassType typeName;
	
	public RemoteServiceWrapperCreator(JClassType typeName) {
		this.typeName = typeName;
	}
	
	public RebindResult create(TreeLogger logger, GeneratorContext context) throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		return generateServiceWrapper(typeName);
	}

	protected SourceWriter getServiceWrapperSourceWriter(String packageName,
			String serviceInterfaceName, JClassType classType) {
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				serviceInterfaceName);
		if (printWriter == null) {
			return null;
		}
		
		List<String> imports = collectImports(classType);
		
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				packageName, serviceInterfaceName);

		String GWTClassname = GWT.class.getCanonicalName();
		if (!imports.contains(GWTClassname)) {
			imports.add(GWTClassname);
		}
		
		String parentClassName = classType.getQualifiedSourceName();
		if (!imports.contains(parentClassName)) {
			imports.add(parentClassName);
		}

		String wrapperClassName = packageName + "." + serviceInterfaceName;
		wrapperClassName.replaceAll(ASYNC_WRAPPER_SUFFIX, PROVIDER_SUFFIX);
		
		if (!imports.contains(wrapperClassName)) {
			imports.add(wrapperClassName);
		}

		for (String importName : imports) {
			composerFactory.addImport(importName);
		}
		
		composerFactory.addImplementedInterface(parentClassName);
		
		return composerFactory.createSourceWriter(context, printWriter);
	}

	private List<String> collectImports(JClassType classType) {
		List<String> imports = new ArrayList<String>();
		
		for (JMethod method : classType.getMethods()) {
			if (method.getReturnType().isPrimitive() == null) {
				String importName = method.getReturnType().getQualifiedSourceName();
				if (!imports.contains(importName)) {
					imports.add(importName);
				}
			}
			
			for (JParameter parameter : method.getParameters()) {
				if (parameter.getType().isPrimitive() == null) {
					String importName = parameter.getType().getQualifiedSourceName();
					if (!imports.contains(importName)) {
						imports.add(importName);
					}
				}
			}
		}
		
		return imports;
	}
	
	protected RebindResult generateServiceWrapper(JClassType classType) throws UnableToCompleteException {

		String packageName = classType.getPackage().getName();
		String simpleClassName = classType.getSimpleSourceName();
		String className = simpleClassName + ASYNC_WRAPPER_SUFFIX;
		
		String asyncInterface = classType.getQualifiedSourceName() + "Async";
		JClassType asyncClassType = null;
		try {
			asyncClassType = context.getTypeOracle().getType(asyncInterface);
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}

		SourceWriter sourceWriter = getServiceWrapperSourceWriter(packageName, className, asyncClassType);

		if (sourceWriter == null) {
			//File is already generated
		    return new RebindResult(RebindMode.USE_EXISTING, packageName + "." + className);
		}
				
		sourceWriter.println("private static " + asyncInterface + " service;");
		sourceWriter.println("private static final " + simpleClassName + "Provider provider = new " + simpleClassName + "Provider();");
		sourceWriter.println("static {");
		sourceWriter.indent();
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.println("");
		
		sourceWriter.println("public " + className + "(){");
		sourceWriter.indent();
		sourceWriter.println("if (service == null) {");
		sourceWriter.indent();
		sourceWriter.println("service = provider.get();");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.println("");

		writeMethods(sourceWriter, asyncClassType.getMethods());
		JClassType extendedInterfaceType;
		for(JClassType interfaceType : asyncClassType.getImplementedInterfaces()) {
			writeMethods(sourceWriter, interfaceType.getMethods());
			extendedInterfaceType = interfaceType.getSuperclass();
			while(extendedInterfaceType != null) {
				writeMethods(sourceWriter, extendedInterfaceType.getMethods());
				extendedInterfaceType = extendedInterfaceType.getSuperclass();
			}
		}
				
		sourceWriter.commit(logger);

	    RebindResult result =
	          new RebindResult(RebindMode.USE_ALL_NEW, packageName + "." + className);

		return result;
	}
	
	
	private void writeMethods(SourceWriter sourceWriter, JMethod[] methodArray) {
		for (JMethod method : methodArray) {
			sourceWriter.println("@Override");
			sourceWriter.print("public ");
			sourceWriter.print(method.getReturnType().getSimpleSourceName() + " ");
			sourceWriter.print(method.getName()+"(");
			int parametersCount = method.getParameters().length;
			int index = 0;
			for (JParameter parameter : method.getParameters()) {
				sourceWriter.print(parameter.getType().getQualifiedSourceName() + " ");
				sourceWriter.print(parameter.getName());
				if (index < parametersCount - 1) {
					sourceWriter.print(", ");	
				}
				index++;
			}
			sourceWriter.println(") {");
			sourceWriter.indent();
			
			sourceWriter.print("service." + method.getName() + "(");
			
			index = 0;
			
			for (JParameter parameter : method.getParameters()) {
				sourceWriter.print(parameter.getName());
				if (index < parametersCount - 1) {
					sourceWriter.print(", ");	
				}
				index++;
			}
			
			sourceWriter.println(");");
			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.println("");
		}
	}
}