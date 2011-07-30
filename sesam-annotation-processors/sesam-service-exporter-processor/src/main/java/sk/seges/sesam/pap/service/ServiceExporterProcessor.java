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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.service.annotation.ExportService;
import sk.seges.sesam.pap.service.annotation.LocalServiceConverter;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceExporterProcessor extends AbstractConfigurableProcessor {

	private static final String EXPORTER_SUFFIX = "Exporter";
	
	private Map<NamedType, ExecutableElement> methodsCache = new HashMap<NamedType, ExecutableElement>();

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceExporterProcessorConfigurer();
	}

	protected TypeElement getLocalServiceConverter(TypeElement element, NamedType serviceType) {
		ExecutableElement executableElement = methodsCache.get(serviceType);

		if (executableElement == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unknown service type " + serviceType.getCanonicalName() + 
					". Most probably this is sesam bug - please report this bug somewhere.");
			return null;
		}

		ExportService service = executableElement.getAnnotation(ExportService.class);
		
		if (service != null) {
			TypeElement localServiceConverter = AnnotationClassPropertyHarvester.getTypeOfClassProperty(service, new AnnotationClassProperty<ExportService>() {
	
				@Override
				public Class<?> getClassProperty(ExportService annotation) {
					return annotation.localServiceConverter();
				}
			});

			return localServiceConverter;
		}
		
		return null;
	}
	
	protected TypeElement getRemoteServiceType(TypeElement element, NamedType serviceType) {

		TypeElement localServiceConverterType = getLocalServiceConverter(element, serviceType);

		if (localServiceConverterType == null) {
			return null;
		}
		
		LocalServiceConverter localServiceConveter = localServiceConverterType.getAnnotation(LocalServiceConverter.class);

		if (localServiceConveter == null) {
			return null;
		}

		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(localServiceConveter, new AnnotationClassProperty<LocalServiceConverter>() {
			
			@Override
			public Class<?> getClassProperty(LocalServiceConverter annotation) {
				return annotation.remoteService();
			}
		});
	}
		
	protected Set<ImmutableType> getAffectedServices(TypeElement element) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		Set<ImmutableType> result = new HashSet<ImmutableType>();
		
		for (ExecutableElement method: methods) {
			if (method.getAnnotation(ExportService.class) != null || isLocalService(method.getReturnType())) {
				ImmutableType returnType = (ImmutableType) getNameTypes().toType(method.getReturnType());
				if (returnType != null) {
					result.add(returnType);
					methodsCache.put(returnType, method);
				}
			}
		}
		
		return result;
	}
	
	private boolean isLocalService(TypeMirror typeMirror) {
		if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		LocalServiceDefinition localServiceDefinition = ((DeclaredType)typeMirror).asElement().getAnnotation(LocalServiceDefinition.class);
		return (localServiceDefinition != null);
	}
	
	public static ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix(EXPORTER_SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		
		if (mutableType.asType() == null || !mutableType.asType().getKind().equals(TypeKind.DECLARED)) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to process " + mutableType.getCanonicalName() + " - unsupported type. Most probably this is sesam bug - please report this bug somewhere.");
			return new NamedType[] {};
		}
		
		TypeElement typeElement = (TypeElement)((DeclaredType)mutableType.asType()).asElement();
		
		Set<ImmutableType> affectedServices = getAffectedServices(typeElement);
		Iterator<ImmutableType> iterator = affectedServices.iterator();
		
		Set<NamedType> result = new HashSet<NamedType>();
		while (iterator.hasNext()) {
			result.add(getOutputClass(iterator.next()));
		};
		
		return result.toArray(new NamedType[] {});
	}

	protected ImmutableType toService(ImmutableType exporterType) {
		return exporterType.setName(exporterType.getSimpleName().replaceAll(EXPORTER_SUFFIX, ""));
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		TypeElement remoteServiceType = getRemoteServiceType(element, toService((ImmutableType)outputName));
		
		if (remoteServiceType == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to process unsupported type. Most probably this is sesam " +
					" bug - please report this bug somewhere.");
		}
		
		
		int a = 0;
	}
}