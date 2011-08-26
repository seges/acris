package sk.seges.sesam.pap.model.hibernate;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.mutable.MutableVariableElement;
import sk.seges.sesam.pap.model.TransferObjectConverterProcessor;
import sk.seges.sesam.pap.model.hibernate.util.HibernateHelper;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateTransferObjectConverterProcessor extends TransferObjectConverterProcessor {

	private static final String ENTITY_MANAGER_NAME = "entityManager";

	private HibernateHelper hibernateHelper;
	
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		hibernateHelper = new HibernateHelper(methodHelper);
	}
	
	@Override
	protected ElementHolderTypeConverter getElementTypeConverter() {
		return new HibernatePersistentElementHolderConverter(processingEnv);
	}
	
	@Override
	protected TypeMirror getTargetEntityType(ExecutableElement method) {
		return hibernateHelper.getTargetEntityType(method, processingEnv);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return hibernateHelper.shouldHaveIdMethod(configurationElement, domainElement);
	}

	@Override
	protected MutableVariableElement[] getAdditionalConstructorParameters() {
		MutableVariableElement entityManagerParameter = new MutableVariableElement(processingEnv.getElementUtils().getTypeElement(EntityManager.class.getCanonicalName()), processingEnv);
		entityManagerParameter.setSimpleName(ENTITY_MANAGER_NAME);
		return new MutableVariableElement[] {entityManagerParameter};
	};
	
	@Override
	protected void printIsInitializedMethod(PrintWriter pw, String instanceName) {
		pw.println("return " + Hibernate.class.getCanonicalName() + ".isInitialized(" + instanceName + ");");
	}
	
	@Override
	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("if (id != null) {");
		pw.println(type.getSimpleName() + " result = " + ENTITY_MANAGER_NAME + ".find(" + type.getSimpleName() + ".class, id);");
		pw.println("if (result != null) {");
		pw.println("return result;");
		pw.println("}");
		pw.println("}");
		pw.println();
		super.printDomainInstancer(pw, type);
	}
}
