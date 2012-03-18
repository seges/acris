package sk.seges.sesam.pap.model.printer.constructor;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.ConstructorPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.MapConvertedInstanceCache;

public class ConverterConstructorPrinter extends ConstructorPrinter {

	protected class ConverterConstructorHelper extends DefaultConstructorHelper {
		@Override
		public void construct(TypeMirror type, FormattedPrintWriter pw) {
			construct(processingEnv.getTypeUtils().toMutableType(type), pw);
		}

		@Override
		public void construct(MutableTypeMirror type, FormattedPrintWriter pw) {
			if (processingEnv.getTypeUtils().isSameType(type,
					processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class))) {
				type = processingEnv.getTypeUtils().toMutableType(MapConvertedInstanceCache.class);
			}
			pw.print("new ", type, "()");
		}
	}

	private final MutableProcessingEnvironment processingEnv;
	
	public ConverterConstructorPrinter(FormattedPrintWriter pw, MutableDeclaredType outputName, MutableProcessingEnvironment processingEnv) {
		super(pw, outputName);
		this.processingEnv = processingEnv;
	}

	@Override
	public void printConstructors(TypeElement fromType, final ExecutableElement superTypeConstructorElement, boolean callSuperConstructor, ParameterElement... additionalParameters) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			printConstructor(constructor, superTypeConstructorElement, new ConverterConstructorHelper(), callSuperConstructor, additionalParameters);
		}
	}

	@Override
	protected void printConstructor(ExecutableElement constructor, ParameterElement... additionalParameters) {
		printConstructor(constructor, constructor, new ConverterConstructorHelper(), true, additionalParameters);
	}	
}