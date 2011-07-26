package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.pap.model.TransferObjectProcessor;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;
import sk.seges.sesam.shared.model.converter.DtoConverter;

@Target({ElementType.TYPE, ElementType.FIELD})
public @interface TransferObjectMapping {

	public class NotDefinedConverter implements DtoConverter<Void, Void> {

		@Override
		public Void toDto(Void domain) {
			return null;
		}

		@Override
		public Void fromDto(Void dto) {
			return null;
		}
		
	}

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	Class<?> dtoClass() default Void.class;
	String 	dtoClassName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration} 
	 */
	Class<?> domainClass() default Void.class;
	String domainClassName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	Class<?> configuration() default Void.class;
	String configurationClassName() default Constants.NULL;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectProcessor} and {@link TransferObjectConfiguration}
	 */
	Class<? extends DtoConverter<?, ?>> converter() default NotDefinedConverter.class;
	String converterClassName() default Constants.NULL;
}