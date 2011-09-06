package sk.seges.sesam.pap.service.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidator.LocationType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

public class LocalServiceTypeElement extends DelegateImmutableType {

	public static final String REMOTE_SUFFIX = "Remote";
	public static final String LOCAL_SUFFIX = "Local";

	private final boolean isGenerated;
	
	private final ProcessingEnvironment processingEnv;
	private final RemoteServiceTypeElement remoteService;
	private final TypeElement localServiceType;
	private final NameTypesUtils nameTypesUtils;
	
	public LocalServiceTypeElement(RemoteServiceTypeElement remoteService, ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.isGenerated = true;
		this.remoteService = remoteService;
		this.localServiceType = null;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
	}

	public LocalServiceTypeElement(TypeElement localServiceType, ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.isGenerated = false;
		this.localServiceType = localServiceType;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
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
	protected ImmutableType getDelegateImmutableType() {
		if (localServiceType != null) {
			return nameTypesUtils.toImmutableType(localServiceType);
		}
		return getLocalServiceClass(remoteService);
	}
	
	public ImmutableType getLocalServiceClass(ImmutableType mutableType) {
		String simpleName = mutableType.getSimpleName();
		if (simpleName.endsWith(REMOTE_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - REMOTE_SUFFIX.length());
		}
		PackageValidator packageValidator = new DefaultPackageValidatorProvider().get(mutableType.getPackageName());
		packageValidator.moveTo(LocationType.SERVER);
		mutableType = mutableType.changePackage(packageValidator);
		return mutableType.setName(simpleName + LOCAL_SUFFIX);
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