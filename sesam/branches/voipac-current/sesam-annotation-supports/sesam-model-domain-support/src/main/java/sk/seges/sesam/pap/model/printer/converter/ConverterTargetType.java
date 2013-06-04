package sk.seges.sesam.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;

public enum ConverterTargetType {
	DTO {
		@Override
		public String getMethodPrefix() {
			return "Dto";
		}

		@Override
		public MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv) {
			DtoType dto = converterType.getConfiguration().getRawDto();
			if (dto != null && dto.getKind().isDeclared()) {
				MutableDeclaredType dtoDeclared = ((DtoDeclaredType)dto).clone();
				
				MutableTypeVariable[] typeVariables = new MutableTypeVariable[dtoDeclared.getTypeVariables().size()];
				
				for (int i = 0; i < typeVariables.length; i++) {
					typeVariables[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null);
				}
				
				dtoDeclared.setTypeVariables(typeVariables);
				return dtoDeclared;
			}
			
			return dto;
		}

		@Override
		public String getConverterMethodName() {
			return "getConverterForDto";
		}
	}, DOMAIN {
		@Override
		public String getMethodPrefix() {
			return "Domain";
		}

		@Override
		public MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv) {
			DomainType domain = converterType.getConfiguration().getRawDomain();
			if (domain != null && domain.getKind().isDeclared()) {
				MutableDeclaredType domainDeclared = ((DomainDeclaredType)domain).clone();
				
				MutableTypeVariable[] typeVariables = new MutableTypeVariable[domainDeclared.getTypeVariables().size()];
				
				for (int i = 0; i < typeVariables.length; i++) {
					typeVariables[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null);
				}
				
				domainDeclared.setTypeVariables(typeVariables);
				return domainDeclared;
			}
			
			return domain;
		}

		@Override
		public String getConverterMethodName() {
			return "getConverterForDomain";
		}
	};
	
	public abstract String getMethodPrefix();
	public abstract MutableTypeMirror getObject(ConverterTypeElement converterType, MutableProcessingEnvironment processingEnv);
	public abstract String getConverterMethodName();
}