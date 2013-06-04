/**
   Copyright 2011 Seges s.r.o.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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

	boolean generateDto() default true;
	
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
	
	boolean generateConverter() default true;
}