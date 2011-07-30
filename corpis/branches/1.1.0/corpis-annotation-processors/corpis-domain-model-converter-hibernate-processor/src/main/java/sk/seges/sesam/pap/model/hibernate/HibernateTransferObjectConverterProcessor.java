package sk.seges.sesam.pap.model.hibernate;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.mutable.MutableVariableElement;
import sk.seges.sesam.pap.model.TransferObjectConvertorProcessor;
import sk.seges.sesam.pap.model.hibernate.util.HibernateHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateTransferObjectConverterProcessor extends TransferObjectConvertorProcessor {

	private HibernateHelper hibernateHelper;
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		hibernateHelper = new HibernateHelper();
	}
	
	@Override
	protected TypeMirror getTargetEntityType(ExecutableElement method) {
		return hibernateHelper.getTargetEntityType(method, processingEnv);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return hibernateHelper.shouldHaveIdMethod(configurationElement, domainElement);
	}

	protected MutableVariableElement[] getAdditionalConstructorParameters() {
		MutableVariableElement entityManagerParameter = new MutableVariableElement(processingEnv.getElementUtils().getTypeElement(EntityManager.class.getCanonicalName()), processingEnv);
		entityManagerParameter.setSimpleName("entityManager");
		return new MutableVariableElement[] {entityManagerParameter};
	};
	
	@Override
	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("if (id != null) {");
		pw.println("return entityManager.find(" + type.getSimpleName() + ".class, id);");
		pw.println("}");
		pw.println();
		super.printDomainInstancer(pw, type);
	}
}
