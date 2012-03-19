/**
 * 
 */
package sk.seges.sesam.pap.metadata;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;
import sk.seges.sesam.pap.metadata.cache.MetaCache;
import sk.seges.sesam.pap.metadata.cache.MetaCache.MetaElementType;
import sk.seges.sesam.pap.metadata.configurer.MetaModelProcessorConfigurer;
import sk.seges.sesam.pap.metadata.iterator.ClassIterator;
import sk.seges.sesam.pap.metadata.iterator.ClassIterator.ElementHandler;
import sk.seges.sesam.pap.metadata.model.MetaModelContext;
import sk.seges.sesam.pap.metadata.model.MetaModelConvertProvider;
import sk.seges.sesam.pap.metadata.model.MetaModelTypeElement;
import sk.seges.sesam.pap.metadata.printer.MetaPrinterProvider;
import sk.seges.sesam.pap.metadata.printer.NestedPrinter;

/**
 * Generates meta model interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link MetaModel} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/meta-model.properties file</li>
 * </ul>
 * Bean processor also generates constants for accessing each attribute in the bean in to order to write type-safe code.
 * Let's imagine that you have a bean User POJO with the following fields:
 * <ul>
 * <li>First name - represented by the property firstName</li>
 * <li>Last name - represented by the property lastName</li>
 * <li>Login - represented by the property login</li>
 * </ul>
 * and the MetaModel will contains following fields:
 * <ul>
 * <li>FIRST_NAME targeting "firstName" property</li>
 * <li>LAST_NAME targeting "lastName" property</li>
 * <li>LOGIN targeting "login" property</li>
 * </ul>
 * From now you can use constants from meta model in order to reach type safe coding, e.g. UserMetaModel.FIRST_NAME
 * will reference User.getFirstName() method and when the property firstName will be renamed then also UserMetaModel
 * will be regenerated and you will get a compile error (when using the strings, determining the references is much more
 * harder)
 * 
 * @author ladislav.gazo
 * @author Peter Simun (simun@seges.sk)
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MetaModelProcessor extends MutableAnnotationProcessor {

	private MetaModelConvertProvider metaModelConvertProvider;
	private MetaCache cache;
	private MetaPrinterProvider printerProvider;
	private MetaModelContext context;

	private enum AccessType {
		METHOD {
			@Override
			public boolean supports(ModelPropertyConverter converter) {
				return converter.handleMethods();
			}
		}, 
		
		PROPERTY {
			@Override
			public boolean supports(ModelPropertyConverter converter) {
				return converter.handleFields();
			}
		};
		
		public abstract boolean supports(ModelPropertyConverter converter);
	}
	
	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new MetaModelTypeElement(context.getMutableType())
		};
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new MetaModelProcessorConfigurer();
	}
	
	protected void processInterfaces(TypeElement element) {
		for (TypeMirror interfaceType: element.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				Element interfaceElement = ((DeclaredType)interfaceType).asElement();
				if (interfaceElement.getKind().equals(ElementKind.INTERFACE)) {
					processClass((TypeElement)interfaceElement);
				}
			}
		}
	}
	
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();

		metaModelConvertProvider = new MetaModelConvertProvider(context.getTypeElement(), processingEnv);
		
		this.context = new MetaModelContext();
		this.printerProvider = new MetaPrinterProvider(pw);
		this.cache = new MetaCache();

		for (ModelPropertyConverter selectedConverter : metaModelConvertProvider.getConverters()) {

			TypeElement element = context.getTypeElement();

			this.context.setConverter(selectedConverter);
			this.context.setProcessingElement(element);
			
			processClass(element);
			processInterfaces(element);
	
			while (element.getSuperclass() instanceof DeclaredType) {
				element = (TypeElement) ((DeclaredType) element.getSuperclass()).asElement();
				processClass(element);
				processInterfaces(element);
			}
		}
	}
	
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	};
			
	private boolean processNestedType(TypeElement element) {

		if (!context.getConverter().supportsHierarchy()) {
			return false;
		}

		context.setProcessingElement(element);

		if (cache.isProcessed(context, MetaElementType.TYPE) || cache.isProcessed(context, MetaElementType.PROPERTY)) {
			context.setProcessingElement(null);
			return false;
		}

		NestedPrinter nestedPrinter = printerProvider.getNestedPrinter();

		nestedPrinter.initialize(context);
		
		cache.setProcessed(context);
		processClass(element);

		nestedPrinter.finish(context);
		context.setProcessingElement(null);

		return true;
	}

	private boolean processProperty(DeclaredType declaredType) {

		TypeElement classTypeElement = (TypeElement) declaredType.asElement();
		
		if (configurer.hasSupportedAnnotation(classTypeElement)) {
			return processNestedType(classTypeElement);
		}
		
		return false;
	}

	private void processProperty(Element element, AccessType accessType) {
		boolean interfaceGenerated = false;

		if (!cache.isProcessed(context, MetaElementType.TYPE)) {
			if (element.asType().getKind().equals(TypeKind.DECLARED) && accessType.equals(AccessType.PROPERTY)) {
				interfaceGenerated = processProperty((DeclaredType) element.asType());
			} else if (element.asType().getKind().equals(TypeKind.EXECUTABLE) && accessType.equals(AccessType.METHOD)) {
				final TypeMirror returnTypeElement = ProcessorUtils.getOverrider(context.getProcessingElement(), ((ExecutableElement) element), processingEnv).getReturnType();
				if (returnTypeElement != null && returnTypeElement.getKind() == TypeKind.DECLARED) {
					interfaceGenerated = processProperty((DeclaredType) returnTypeElement);
				}
			}
		}

		if (!interfaceGenerated && accessType.supports(context.getConverter()) && !cache.isProcessed(context, MetaElementType.PROPERTY)) {
			cache.setProcessed(context);
			printerProvider.getConstantsPrinter().print(context);
		}
	}

	private void processClass(TypeElement element) {

		ClassIterator classIterator = new ClassIterator(element, processingEnv);

		classIterator.iterateFields(new ElementHandler() {
			
			@Override
			public void handle(Element element, String propertyName) {
				context.setProperty(propertyName);
				processProperty(element, AccessType.PROPERTY);
				context.setProperty(null);
			}
		});

		classIterator.iterateMethods(new ElementHandler() {
			
			@Override
			public void handle(Element element, String fieldName) {
				context.setProperty(fieldName);
				processProperty(element, AccessType.METHOD);
				context.setProperty(null);
			}
		});

	}
}