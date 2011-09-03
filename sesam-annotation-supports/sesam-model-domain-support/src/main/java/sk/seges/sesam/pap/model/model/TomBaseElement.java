package sk.seges.sesam.pap.model.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.DelegateImmutableType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

class TomBaseElement extends DelegateImmutableType {

	protected final ProcessingEnvironment processingEnv;
	protected final RoundEnvironment roundEnv;

	protected final TransferObjectHelper toHelper;
	private final NameTypesUtils nameTypesUtils;
	protected final TypeParametersSupport typeParametersSupport;
	
	protected TomBaseElement(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
		this.processingEnv = processingEnv;

		this.nameTypesUtils = new NameTypesUtils(processingEnv.getElementUtils());
		this.toHelper = new TransferObjectHelper(nameTypesUtils, processingEnv, roundEnv, new MethodHelper(processingEnv, nameTypesUtils));
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, nameTypesUtils);
	}
	
	protected NameTypesUtils getNameTypesUtils() {
		return nameTypesUtils;
	}
}