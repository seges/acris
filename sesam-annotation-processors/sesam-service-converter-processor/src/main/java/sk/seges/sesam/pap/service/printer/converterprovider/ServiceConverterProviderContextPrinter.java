package sk.seges.sesam.pap.service.printer.converterprovider;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.pap.converter.printer.ConverterVerifier;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.model.annotation.ConverterProviderDefinition;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.AbstractServiceMethodPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterProviderPrinterContext;

public class ServiceConverterProviderContextPrinter extends AbstractServiceMethodPrinter {
	
	private final ConverterProviderContextPrinterDelegate converterProviderContextPrinterDelegate;
	protected UsageType previousUsageType;
	protected final ClassPathTypes classPathTypes;
	protected Boolean skipProcessing;
	
	public ServiceConverterProviderContextPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolverProvider parametersResolverProvider, 
			ConverterProviderPrinter converterProviderPrinter, ClassPathTypes classPathTypes) {
		super(processingEnv, parametersResolverProvider, converterProviderPrinter);
		this.converterProviderContextPrinterDelegate = new ConverterProviderContextPrinterDelegate(parametersResolverProvider);
		this.classPathTypes = classPathTypes;
	}
	
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	protected void finalize() {
		if (skipProcessing == null || skipProcessing) {
			return;
		}
		converterProviderContextPrinterDelegate.finalize();
		converterProviderPrinter.changeUsage(previousUsageType);
		skipProcessing = null;
	}
	
	protected Set<? extends Element> getConverterProviderDelegates() {
		return classPathTypes.getElementsAnnotatedWith(ConverterProviderDefinition.class);
	}
	
	
	protected boolean hasConverterProviderContext(ServiceConverterPrinterContext context) {
		List<MutableDeclaredType> nestedTypes = context.getService().getServiceConverter().getNestedTypes();
		
		if (nestedTypes == null || nestedTypes.size() == 0) {
			return false;
		}
		
		for (MutableDeclaredType nestedType: nestedTypes) {
			if (nestedType.isSameType(context.getConvertProviderContextType())) {
				return true;
			}
		}
		
		return false;
	}
	
	protected void initializeContext(ServiceConverterPrinterContext context) {
		
		if (skipProcessing != null) {
			return;
		}
		
		ServiceTypeElement serviceTypeElement = context.getService();
		final ConverterProviderContextType convertProviderContextType = serviceTypeElement.getServiceConverter().getConvertProviderContextType();
		//new ConverterProviderContextType(serviceTypeElement, processingEnv);
		context.setConvertProviderContextType(convertProviderContextType);

		skipProcessing = hasConverterProviderContext(context);
	}
	
	protected void initialize(ServiceConverterPrinterContext context) {
		
		previousUsageType = converterProviderPrinter.changeUsage(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR);

		converterProviderContextPrinterDelegate.initialize(processingEnv, context.getConvertProviderContextType(), 
				context.getService().getServiceConverter(), UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR, getConverterProviderDelegates());
	}
	
	protected ConverterProviderElementPrinter[] getNestedPrinters() {
		return new ConverterProviderElementPrinter[] {};
	}
	
	@Override
	public void print(ServiceConverterPrinterContext context) {

		if (skipProcessing == null) {
			initializeContext(context);
		}

		if (skipProcessing) {
			return;
		}
		
		ConverterVerifier converterVerifier = new ConverterVerifier();
		context.setNestedPrinter(converterVerifier);
		super.print(context);
		
		if (converterVerifier.isContainsConverter()) {
			initialize(context);
			for (ConverterProviderElementPrinter nestedPrinter: getNestedPrinters()) {
				nestedPrinter.initialize();
				context.setNestedPrinter(nestedPrinter);
				super.print(context);
				nestedPrinter.finish();
			}
			finalize();
		}
	}
	
	@Override
	protected void handleMethod(ServiceConverterPrinterContext context, ExecutableElement localMethod, ExecutableElement remoteMethod) {
		ConverterProviderElementPrinter nestedPrinter = context.getNestedPrinter();
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			DtoType returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());

			if (returnDtoType.getConverter() != null) {
				DomainDeclaredType rawDomain = (DomainDeclaredType)returnDtoType.getDomain();
				//TODO why not raw domain
				//returnDtoType.getConverter().getConfiguration().getRawDomain();
				DtoDeclaredType rawDto = returnDtoType.getConverter().getConfiguration().getRawDto();
				nestedPrinter.print(new ServiceConverterProviderPrinterContext(rawDto, localMethod, returnDtoType.getConverter().getConfiguration()));

				if (rawDomain.getTypeVariables().size() > 0) {
					for (MutableTypeVariable typeVariable: rawDomain.getTypeVariables()) {
						printDtoTypeVariables(typeVariable.getLowerBounds(), localMethod, nestedPrinter);
						printDtoTypeVariables(typeVariable.getUpperBounds(), localMethod, nestedPrinter);
					}
				}
			}
		}

		for (int i = 0; i < localMethod.getParameters().size(); i++) {
			TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
			DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);
			if (parameterDtoType.getConverter() != null) {
				DomainDeclaredType rawDomain = parameterDtoType.getConverter().getConfiguration().getRawDomain();
				nestedPrinter.print(new ServiceConverterProviderPrinterContext(rawDomain, localMethod));
				
				if (rawDomain.getTypeVariables().size() > 0) {
					for (MutableTypeVariable typeVariable: rawDomain.getTypeVariables()) {
						printDomainTypeVariables(typeVariable.getLowerBounds(), localMethod, nestedPrinter);
						printDomainTypeVariables(typeVariable.getUpperBounds(), localMethod, nestedPrinter);
					}
				}
			}
		}
	}

	private void printDtoTypeVariables(Set<? extends MutableTypeMirror> types, ExecutableElement localMethod, ConverterProviderElementPrinter nestedPrinter) {
		for (MutableTypeMirror type: types) {
			DtoType dtoType = processingEnv.getTransferObjectUtils().getDtoType(type);
			if (dtoType.getKind().isDeclared() && dtoType.getConverter() != null) {
				ServiceConverterProviderPrinterContext context = new ServiceConverterProviderPrinterContext((DtoDeclaredType)dtoType, localMethod, dtoType.getConverter().getConfiguration());
				nestedPrinter.print(context);
			}
		}
	}

	private void printDomainTypeVariables(Set<? extends MutableTypeMirror> types, ExecutableElement localMethod, ConverterProviderElementPrinter nestedPrinter) {
		for (MutableTypeMirror type: types) {
			DomainType domainType = processingEnv.getTransferObjectUtils().getDomainType(type);
			if (domainType.getKind().isDeclared() && domainType.getConverter() != null) {
				ServiceConverterProviderPrinterContext context = new ServiceConverterProviderPrinterContext((DomainDeclaredType)domainType, localMethod);
				nestedPrinter.print(context);
			}
		}
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
	}
}