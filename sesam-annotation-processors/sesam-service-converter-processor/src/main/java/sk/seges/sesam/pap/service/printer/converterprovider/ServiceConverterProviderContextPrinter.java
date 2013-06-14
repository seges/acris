package sk.seges.sesam.pap.service.printer.converterprovider;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.model.annotation.ConverterProviderDefinition;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceConverterTypeElement;
import sk.seges.sesam.pap.service.printer.api.ConverterContextElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterProviderPrinterContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

public class ServiceConverterProviderContextPrinter implements ConverterContextElementPrinter {
	
	private final ConverterProviderContextPrinterDelegate converterProviderContextPrinterDelegate;

	protected final ClassPathTypes classPathTypes;
	protected TransferObjectProcessingEnvironment processingEnv;

    public ServiceConverterProviderContextPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolverProvider parametersResolverProvider,
			ClassPathTypes classPathTypes) {
        this.processingEnv = processingEnv;
        this.converterProviderContextPrinterDelegate = new ConverterProviderContextPrinterDelegate(parametersResolverProvider);
		this.classPathTypes = classPathTypes;
	}
	
	protected void finalize() {
		converterProviderContextPrinterDelegate.finalize();

	}
	
	protected Set<? extends Element> getConverterProviderDelegates() {
		return classPathTypes.getElementsAnnotatedWith(ConverterProviderDefinition.class);
	}


    @Override
	public void initialize(ConverterProviderContextType contextType) {
		

		converterProviderContextPrinterDelegate.initialize(processingEnv, contextType,
				UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR, getConverterProviderDelegates());
    }

    @Override
	public void print(ConverterProviderType serviceConverterProvider) {
	}

    @Override
    public void finish(ConverterProviderContextType contextType) {
    }

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

}