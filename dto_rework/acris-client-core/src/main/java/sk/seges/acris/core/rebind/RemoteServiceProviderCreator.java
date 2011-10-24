package sk.seges.acris.core.rebind;

import java.io.PrintWriter;

import sk.seges.acris.core.client.annotation.RemoteServicePath;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class RemoteServiceProviderCreator {

	private static final String PROVIDER_SUFFIX = "Provider";
	
	/**
	 * The {@code TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	private JClassType classType;
	
	public RemoteServiceProviderCreator(JClassType classType) {
		this.classType = classType;
	}
	
	public String create(TreeLogger logger, GeneratorContext context) 
		throws UnableToCompleteException {
		this.logger = logger;
		this.context = context;

		return generateServiceProvider(classType);
	}

	protected SourceWriter getServiceProviderSourceWriter(String packageName, String serviceInterfaceName, JClassType classType) {
		PrintWriter printWriter = context.tryCreate(logger, packageName,
				serviceInterfaceName);
		if (printWriter == null) {
			return null;
		}

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
				packageName, serviceInterfaceName);
		
		composerFactory.addImport(GWT.class.getCanonicalName());
		composerFactory.addImport(ServiceDefTarget.class.getCanonicalName());
		composerFactory.addImport(classType.getQualifiedSourceName() + "_Proxy");
		
		return composerFactory.createSourceWriter(context, printWriter);
	}

	protected String generateServiceProvider(JClassType classType) {
		String packageName = classType.getPackage().getName();
		String simpleClassName = classType.getSimpleSourceName();
		String className = simpleClassName + PROVIDER_SUFFIX;
		SourceWriter sourceWriter = getServiceProviderSourceWriter(packageName, className, classType);

		if (sourceWriter == null) {
			//File is already generated
			return packageName + "." + className;
		}
		
		sourceWriter.println("@SuppressWarnings(\"unchecked\")");
		sourceWriter.println("public " + simpleClassName + "Async get() {");
		sourceWriter.indent();
		sourceWriter.println(simpleClassName + "_Proxy service = new " + simpleClassName + "_Proxy();");
		sourceWriter.println("ServiceDefTarget serviceEndpoint = (ServiceDefTarget) service;");
		sourceWriter.println("");
		
		RemoteServicePath remoteServicePathDefinition = classType.getAnnotation(RemoteServicePath.class);
		String path = remoteServicePathDefinition.value();
		
		sourceWriter.println("String serviceURL = GWT.getModuleBaseURL() + \"" + path + "\";");
		sourceWriter.println("serviceEndpoint.setServiceEntryPoint(serviceURL);");
		sourceWriter.println("return service;");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.commit(logger);
	
		return packageName + "." + className;
	}
}