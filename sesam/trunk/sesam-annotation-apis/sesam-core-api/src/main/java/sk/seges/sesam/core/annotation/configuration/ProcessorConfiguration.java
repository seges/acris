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
package sk.seges.sesam.core.annotation.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.processing.AbstractProcessor;

/**
 * Annotation indicates that java class configures the specific annotation
 * processor. You should explicitly configure which classes has to be processed
 * or you can declare interfaces that implementors will be processed by annotation
 * processor. 
 * 
 * Although the configuration class can be annotated by any annotation, it won't 
 * be processed by annotation processor. It is used just a processor starter.
 * 
 * @author Peter Simun (sinmun@seges.sk)
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ProcessorConfiguration {
	
	Class<? extends AbstractProcessor> processor();

}
