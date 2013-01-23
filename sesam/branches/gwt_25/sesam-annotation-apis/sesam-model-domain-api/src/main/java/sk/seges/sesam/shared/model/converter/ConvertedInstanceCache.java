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
package sk.seges.sesam.shared.model.converter;

import java.io.Serializable;

public interface ConvertedInstanceCache {
	
	<S> S getDtoInstance(Object domain);
	<S> S getDtoInstance(Class<S> domainClass, Serializable domainId);

	<S> S putDtoInstance(Object dto, S domain);
	<S> S putDtoInstance(Object dto, S domain, Serializable dtoId);

	<S> S getDomainInstance(Object dto);
	<S> S getDomainInstance(Class<S> dtoClass, Serializable dtoId);
	
	<S> S putDomainInstance(Object domain, S dto);
	<S> S putDomainInstance(Object domain, S dto, Serializable domainId);
}