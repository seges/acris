package sk.seges.sesam.pap.model.annotation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import sk.seges.sesam.core.pap.Constants;
import sk.seges.sesam.shared.model.converter.CachedConverter;
import sk.seges.sesam.shared.model.converter.DtoConverter;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;

@Target({ElementType.TYPE, ElementType.FIELD})
public @interface TransferObjectMapping {

	public class NotDefinedConverter extends CachedConverter<Void, Void> {

		public NotDefinedConverter(MapConvertedInstanceCache cache) {
			super(cache);
		}

		@Override
		public Void toDto(Void domain) {
			return domain;
		}

		@Override
		public Void fromDto(Void dto) {
			return dto;
		}

		@Override
		protected Class<? extends Void> getDomainClass() {
			return Void.class;
		}

		@Override
		protected Class<? extends Void> getDtoClass() {
			return Void.class;
		}

		@Override
		protected Void createDomainInstance(Serializable id) {
			return null;
		}

		@Override
		protected Void createDtoInstance(Serializable id) {
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