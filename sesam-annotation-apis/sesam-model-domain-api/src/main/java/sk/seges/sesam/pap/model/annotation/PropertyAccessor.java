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

import javax.lang.model.element.ElementKind;

public enum PropertyAccessor {
	FIELD {
		@Override
		public boolean supportsKind(ElementKind kind) {
			return kind.equals(ElementKind.FIELD);
		}
	}, 
	
	PROPERTY {
		@Override
		public boolean supportsKind(ElementKind kind) {
			return kind.equals(ElementKind.METHOD);
		}
	};
	
	public abstract boolean supportsKind(ElementKind kind);
}
