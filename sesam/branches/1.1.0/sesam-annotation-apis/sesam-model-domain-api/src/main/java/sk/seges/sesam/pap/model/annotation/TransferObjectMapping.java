package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TransferObjectMapping {

	public class NotDefinedConverter implements DtoConverter<Void, Void> {

		@Override
		public Void toDto(Void domain) {
			return domain;
		}

		@Override
		public Void fromDto(Void dto) {
			return dto;
		}

		@Override
		public Void convertToDto(Void result, Void domain) {
			return result;
		}

		@Override
		public Void convertFromDto(Void result, Void domain) {
			return result;
		}

		@Override
		public boolean equals(Void domain, Void dto) {
			return false;
		}
	}

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	Class<?> dtoClass() default Void.class;
	String 	dtoClassName() default Constants.NULL;

	Class<?> dtoInterface() default Void.class;
	String dtoInterfaceName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration} 
	 */
	Class<?> domainClass() default Void.class;
	String domainClassName() default Constants.NULL;

	Class<?> domainInterface() default Void.class;
	String domainInterfaceName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	Class<?> configuration() default Void.class;
	String configurationClassName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends DtoConverter> converter() default NotDefinedConverter.class;
	String converterClassName() default Constants.NULL;
}