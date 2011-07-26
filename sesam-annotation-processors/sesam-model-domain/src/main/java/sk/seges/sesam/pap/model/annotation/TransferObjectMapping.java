package sk.seges.sesam.pap.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.pap.model.TransferObjectConvertorProcessor;
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
	 * Do not modify the name unless change it also in the processor {@link TransferObjectConvertorProcessor}
	 */
	Class<?> dtoClass() default Void.class;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectConvertorProcessor}
	 */
	Class<?> domainClass() default Void.class;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectConvertorProcessor}
	 */
	Class<?> configuration() default Void.class;

	/**
	 * Do not modify the name unless change it also in the processor {@link TransferObjectConvertorProcessor}
	 */
	Class<? extends DtoConverter<?, ?>> converter() default NotDefinedConverter.class;
}