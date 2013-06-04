package sk.seges.sesam.pap.service.model;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.delegate.DelegateMutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

public class LocalServiceTypeElement extends DelegateMutableDeclaredType {

	public static final String REMOTE_SUFFIX = "Remote";
	public static final String LOCAL_SUFFIX = "Local";

	private final boolean isGenerated;
	
	private final MutableProcessingEnvironment processingEnv;
	private final RemoteServiceTypeElement remoteService;
	private final TypeElement localServiceType;
	
	public LocalServiceTypeElement(RemoteServiceTypeElement remoteService, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.isGenerated = true;
		this.remoteService = remoteService;
		this.localServiceType = null;
		setKind(MutableTypeKind.INTERFACE);
		prefixTypeParameter(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
	}

	public LocalServiceTypeElement(TypeElement localServiceType, MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.isGenerated = false;
		this.localServiceType = localServiceType;
		this.remoteService = getRemoteServiceElement();
	}

	protected RemoteServiceTypeElement getRemoteServiceElement() {
		if (!isValid()) {
			return null;
		}
		
		LocalServiceDefinition localServiceDefinition = this.localServiceType.getAnnotation(LocalServiceDefinition.class);
		TypeElement remoteService = AnnotationClassPropertyHarvester.getTypeOfClassProperty(localServiceDefinition,  new AnnotationClassProperty<LocalServiceDefinition>() {

			@Override
			public Class<?> getClassProperty(LocalServiceDefinition annotation) {
				return annotation.remoteService();
			}
			
		});
		
		return new RemoteServiceTypeElement(remoteService, this, processingEnv);
	}
	
	public boolean isValid() {
		if (isGenerated) {
			return true;
		}
		if (localServiceType == null) {
			return false;
		}
		return localServiceType.getAnnotation(LocalServiceDefinition.class) != null;
	}
	
	@Override
	protected MutableDeclaredType getDelegate() {
		if (localServiceType != null) {
			return ((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(localServiceType.asType()));
		}
		return getLocalServiceClass(remoteService.clone());
	}
	
	public MutableDeclaredType getLocalServiceClass(MutableDeclaredType mutableType) {
		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(REMOTE_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - REMOTE_SUFFIX.length());
		}
		PackageValidator packageValidator = new DefaultPackageValidatorProvider().get(mutableType.getPackageName());
		packageValidator.moveTo(LocationType.SERVER);
		mutableType = mutableType.changePackage(packageValidator);
		mutableType.cloneTypeVariables(remoteService);
		return mutableType.setSimpleName(simpleName + LOCAL_SUFFIX);
	}

	public boolean isGenerated() {
		return isGenerated;
	}
	
	public RemoteServiceTypeElement getRemoteServiceInterface() {
		return remoteService;
	}
	
	public TypeElement asElement() {
		return localServiceType;
	}
}