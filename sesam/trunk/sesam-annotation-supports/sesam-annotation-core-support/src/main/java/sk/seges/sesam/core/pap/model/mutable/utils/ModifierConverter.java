package sk.seges.sesam.core.pap.model.mutable.utils;

import javax.lang.model.element.Modifier;

public class ModifierConverter {

	public int getModifierType(Modifier modifier) {
		switch (modifier) {
			case PUBLIC:
			case PRIVATE:
			case PROTECTED:
				return 1;
				
			case ABSTRACT:
			case FINAL:
				return 2;
				
			case NATIVE:
				return 3;
			case STATIC:
				return 4;
			case STRICTFP:
				return 5;
			case SYNCHRONIZED:
				return 6;
			case TRANSIENT:
				return 7;
			case VOLATILE:
				return 8;
		}
		
		return 0;
	}

}
