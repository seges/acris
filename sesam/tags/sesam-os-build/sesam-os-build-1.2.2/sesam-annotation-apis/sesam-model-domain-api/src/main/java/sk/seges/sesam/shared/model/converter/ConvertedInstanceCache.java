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
	
	<S> S getInstance(Object source);
	<S> S putInstance(Object source, S result);

	<S> S getInstance(Class<S> instanceClass, Serializable id);
	<S> S putInstance(S instance, Serializable id);
}