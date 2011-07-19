package sk.seges.sesam.pap.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.service.annotation.ExportService;
import sk.seges.sesam.pap.service.annotation.ServiceExporter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DtoServiceLayerProcessor extends AbstractConfigurableProcessor {

	private static final String EXPORTER_SUFFIX = "Exporter";
	
	private Map<NamedType, ExecutableElement> methodsCache = new HashMap<NamedType, ExecutableElement>();

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(ServiceExporter.class.getCanonicalName());
		return annotations;
	}

	protected TypeElement getAsyncServiceType(TypeElement element, NamedType serviceType) {

		ExecutableElement executableElement = methodsCache.get(serviceType);

		if (executableElement == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unknown service type " + serviceType.getCanonicalName() + ". Most probably this is sesam bug - please report this bug somewhere.");
			return null;
		}

		ExportService service = executableElement.getAnnotation(ExportService.class);
		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(service, new AnnotationClassProperty<ExportService>() {

			@Override
			public Class<?> getClassProperty(ExportService annotation) {
				return annotation.async();
			}
			
		});
	}
		
	protected Set<MutableType> getAffectedServices(TypeElement element) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		Set<MutableType> result = new HashSet<MutableType>();
		
		for (ExecutableElement method: methods) {
			if (method.getAnnotation(ExportService.class) != null) {
				MutableType returnType = getNameTypes().toType(method.getReturnType());
				if (returnType != null) {
					result.add(returnType);
					methodsCache.put(returnType, method);
				}
			}
		}
		
		return result;
	}
	
	public static MutableType getOutputClass(MutableType mutableType) {
		return mutableType.addClassSufix(EXPORTER_SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		if (mutableType.asType() == null || !mutableType.asType().getKind().equals(TypeKind.DECLARED)) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to process " + mutableType.getCanonicalName() + " - unsupported type. Most probably this is sesam bug - please report this bug somewhere.");
			return new NamedType[] {};
		}
		TypeElement typeElement = (TypeElement)((DeclaredType)mutableType.asType()).asElement();
		
		Set<MutableType> affectedServices = getAffectedServices(typeElement);
		Iterator<MutableType> iterator = affectedServices.iterator();
		
		Set<NamedType> result = new HashSet<NamedType>();
		while (iterator.hasNext()) {
			result.add(getOutputClass(iterator.next()));
		};
		
		return result.toArray(new NamedType[] {});
	}

	protected MutableType toService(MutableType exporterType) {
		return exporterType.setName(exporterType.getSimpleName().replaceAll(EXPORTER_SUFFIX, ""));
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		TypeElement asyncServiceType = getAsyncServiceType(element, toService((MutableType)outputName));
		int a = 0;
	}
}