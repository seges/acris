package sk.seges.sesam.pap.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.service.annotation.ExportService;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;
import sk.seges.sesam.pap.service.configurer.ServiceExporterProcessorConfigurer;
import sk.seges.sesam.pap.service.model.ServiceExporterTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceExporterProcessor extends MutableAnnotationProcessor {
	
	private Map<MutableTypeMirror, ExecutableElement> methodsCache = new HashMap<MutableTypeMirror, ExecutableElement>();

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ServiceExporterProcessorConfigurer();
	}

	protected TypeElement getLocalServiceConverter(TypeElement element, MutableTypeMirror serviceType) {
		ExecutableElement executableElement = methodsCache.get(serviceType);

		if (executableElement == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unknown service type " + serviceType.toString(ClassSerializer.CANONICAL) + 
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
//	
//	protected TypeElement getRemoteServiceType(TypeElement element, MutableTypeMirror serviceType) {
//
//		TypeElement localServiceConverterType = getLocalServiceConverter(element, serviceType);
//
//		if (localServiceConverterType == null) {
//			return null;
//		}
//		
//		LocalServiceConverter localServiceConveter = localServiceConverterType.getAnnotation(LocalServiceConverter.class);
//
//		if (localServiceConveter == null) {
//			return null;
//		}
//
//		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(localServiceConveter, new AnnotationClassProperty<LocalServiceConverter>() {
//			
//			@Override
//			public Class<?> getClassProperty(LocalServiceConverter annotation) {
//				return annotation.remoteService();
//			}
//		});
//	}
		
	protected Set<MutableDeclaredType> getAffectedServices(TypeElement element) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		Set<MutableDeclaredType> result = new HashSet<MutableDeclaredType>();
		
		for (ExecutableElement method: methods) {
			if (method.getAnnotation(ExportService.class) != null || isLocalService(method.getReturnType())) {
				if (method.getReturnType().getKind().equals(TypeKind.DECLARED)) {
					MutableDeclaredType returnType = processingEnv.getTypeUtils().toMutableType((DeclaredType)method.getReturnType());
					if (returnType != null) {
						result.add(returnType);
						methodsCache.put(returnType, method);
					}
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
	
	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {

		Set<MutableDeclaredType> affectedServices = getAffectedServices(context.getTypeElement());
		Iterator<MutableDeclaredType> iterator = affectedServices.iterator();
		
		Set<MutableDeclaredType> result = new HashSet<MutableDeclaredType>();
		while (iterator.hasNext()) {
			result.add(new ServiceExporterTypeElement(iterator.next()));
		}
		
		return result.toArray(new MutableDeclaredType[] {});
	}
	
//	protected MutableDeclaredType toService(MutableDeclaredType exporterType) {
//		return exporterType.setName(exporterType.getSimpleName().replaceAll(EXPORTER_SUFFIX, ""));
//	}
//	
	@Override
	protected void processElement(ProcessorContext context) {
//		TypeElement remoteServiceType = getRemoteServiceType(element, toService((ImmutableType)outputName));
//		
//		if (remoteServiceType == null) {
//			processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to process unsupported type. Most probably this is sesam " +
//					" bug - please report this bug somewhere.");
//		}
//		
//		
//		int a = 0;
	}
}