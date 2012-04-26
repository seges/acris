package sk.seges.corpis.appscaffold.model.pap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.corpis.appscaffold.model.pap.configurer.DomainDataInterfaceProcessorConfigurer;
import sk.seges.corpis.appscaffold.model.pap.model.DomainDataInterfaceType;
import sk.seges.corpis.appscaffold.shared.annotation.DomainData;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

/**
 * 
 * @author psloboda
 *
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DomainDataInterfaceProcessor extends AbstractDataProcessor {
	
	
	@Override
	protected void processElement(ProcessorContext context) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(context.getTypeElement().getEnclosedElements());
		
		Collections.sort(methods, new Comparator<ExecutableElement>() {
			@Override
			public int compare(ExecutableElement method1, ExecutableElement method2) {
				return method1.getSimpleName().toString().compareTo(method2.getSimpleName().toString());	
			}
		});
		FormattedPrintWriter pw = context.getPrintWriter();
		
		for (ExecutableElement method: methods) {
			
			MutableTypeMirror returnType = castToDomainDataInterface(method.getReturnType());
			
			pw.println("public static final String " + method.getSimpleName().toString().toUpperCase() + " = \"" + method.getSimpleName() + "\";");
			pw.println();
			pw.println(toPrintableType(returnType), " " + MethodHelper.toGetter(method) + ";");
			pw.println();
			pw.println("void ", MethodHelper.toSetter(method) + "(", toPrintableType(returnType), " " + method.getSimpleName() + ");");
			pw.println();
		}
	}
	
	@Override
	protected void printAnnotations(ProcessorContext context) {
		super.printAnnotations(context);
		context.getPrintWriter().println("@", DomainData.class);
	}
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { 
				new DomainDataInterfaceType(context.getMutableType(), processingEnv) 
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new DomainDataInterfaceProcessorConfigurer();
	}

}
