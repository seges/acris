package sk.seges.sesam.pap.service.printer.converterprovider;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.printer.TypePrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.converter.printer.ConverterVerifier;
import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.converterprovider.ConverterProviderPrinterDelegate;
import sk.seges.sesam.pap.model.annotation.ConverterProvider;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.AbstractServiceMethodPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterProviderPrinterContext;

public class ServiceConverterProviderPrinter extends AbstractServiceMethodPrinter {
	
	private final ConverterProviderPrinterDelegate converterProviderPrinterDelegate;
	protected UsageType previousUsageType;
	protected final ClassPathTypes classPathTypes;
	
	public ServiceConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter,
			ClassPathTypes classPathTypes) {
		super(processingEnv, parametersResolverProvider, pw, converterProviderPrinter);
		this.converterProviderPrinterDelegate = new ConverterProviderPrinterDelegate(parametersResolverProvider, pw);
		this.classPathTypes = classPathTypes;
	}
	
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	protected void finalize() {
		converterProviderPrinterDelegate.finalize();
		pw.println("}");
		
		converterProviderPrinter.changeUsage(previousUsageType);
	}
	
	protected Set<? extends Element> getConverterProviderDelegates() {
		return classPathTypes.getElementsAnnotatedWith(ConverterProvider.class);
	}

	protected void initialize(ServiceTypeElement serviceTypeElement) {
				
		ServiceConvertProviderType serviceConvertProviderType = new ServiceConvertProviderType(serviceTypeElement, processingEnv);

		new TypePrinter(processingEnv, pw).printTypeDefinition(serviceConvertProviderType);
		pw.println(" {");
		pw.println();

		converterProviderPrinterDelegate.initialize(serviceConvertProviderType, UsageType.USAGE_CONSTRUCTOR_CONVERTER_PROVIDER, getConverterProviderDelegates());

		previousUsageType = converterProviderPrinter.changeUsage(UsageType.USAGE_INSIDE_CONVERTER_PROVIDER);
	}
	
	protected ConverterProviderElementPrinter[] getNestedPrinters() {
		return new ConverterProviderElementPrinter[] {
			new ServiceMethodDomainConverterProviderPrinter(parametersResolverProvider, processingEnv, pw, converterProviderPrinter),
			new ServiceMethodDtoConverterProviderPrinter(parametersResolverProvider, processingEnv, pw, converterProviderPrinter)	
		};
	}
	
	@Override
	public void print(ServiceConverterPrinterContext context) {
		ConverterVerifier converterVerifier = new ConverterVerifier();
		context.setNestedPrinter(converterVerifier);
		super.print(context);
		
		if (converterVerifier.isContainsConverter()) {
			initialize(context.getService());
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
				nestedPrinter.print(new ServiceConverterProviderPrinterContext(rawDto, localMethod));

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
				ServiceConverterProviderPrinterContext context = new ServiceConverterProviderPrinterContext((DtoDeclaredType)dtoType, localMethod);
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