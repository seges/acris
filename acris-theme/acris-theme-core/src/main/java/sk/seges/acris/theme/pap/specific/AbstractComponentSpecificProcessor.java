package sk.seges.acris.theme.pap.specific;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public abstract class AbstractComponentSpecificProcessor implements ComponentSpecificProcessor {

    class ExecutableMethodDefinition {

    	@SuppressWarnings("unused")
		private TypeElement owner;	//Not used for now
    	
		private String name;
		
		private List<TypeMirror> args = new ArrayList<TypeMirror>();

		ExecutableMethodDefinition(String name) {
			this(getComponentClasses()[0], name);
		}

		ExecutableMethodDefinition(Class<?> owner, String name) {
			this.owner = processingEnv.getElementUtils().getTypeElement(owner.getCanonicalName());
			this.name = name;
		}

		ExecutableMethodDefinition param(Class<?> ...classes) {
			for (Class<?> clazz: classes) {
				param(clazz);
			}
			return this;
		}
		
		ExecutableMethodDefinition param(Class<?> clazz) {
			args.add(processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName()).asType());
			return this;
		}

		ExecutableMethodDefinition params(TypeKind ...types) {
			for (TypeKind type: types) {
				param(type);
			}
			return this;
		}
		
		ExecutableMethodDefinition param(TypeKind type) {
			args.add(processingEnv.getTypeUtils().getPrimitiveType(type));
			return this;
		}
		
		public boolean equals(ExecutableElement method) {
			if (!this.name.equals(method.getSimpleName().toString())) {
				return false;
			}
			
			List<? extends VariableElement> parameters = method.getParameters();
			
			if (parameters != null && parameters.size() > 0) {
				if (args.size() != parameters.size()) {
					return false;
				}
				
				int i = 0;
				
				for (VariableElement parameter: parameters) {
					if (!args.get(i).equals(parameter.asType())) {
						return false;
					}
				}
				
				return true;
			}
			
			return args.size() == 0;
		}
    }

	public enum Statement {
		CONSTRUCTOR, SUPER_CONSTRUCTOR_ARGS, CLASS;
	}
	
	protected ProcessingEnvironment processingEnv;
	
	protected abstract Class<?>[] getComponentClasses();
	
	@Override
	public boolean supports(TypeElement typeElement) {
		for (Class<?> clazz: getComponentClasses()) {
			TypeElement checkBoxTypeElement = processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
			if (processingEnv.getTypeUtils().isSameType(typeElement.asType(), checkBoxTypeElement.asType())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	@Override
	public Type[] getImports() {
		return new Type[] {};
	}

	protected ExecutableMethodDefinition[] getOuterMethodDefinitions() {
		return new ExecutableMethodDefinition[] {};
	}

	protected ExecutableMethodDefinition[] getIgnoredMethodDefinitions() {
		return new ExecutableMethodDefinition[] {};
	}

	@Override
	public boolean isComponentMethod(ExecutableElement method) {
		ExecutableMethodDefinition[] outerMethodDefinitions = getOuterMethodDefinitions();
		
		for (ExecutableMethodDefinition outerMethod: outerMethodDefinitions) {
			if (outerMethod.equals(method)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean ignoreMethod(ExecutableElement method) {

		ExecutableMethodDefinition[] ignoredMethodDefinitions = getIgnoredMethodDefinitions();
		
		for (ExecutableMethodDefinition ignoredMethod: ignoredMethodDefinitions) {
			if (ignoredMethod.equals(method)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void process(Statement statement, ThemeContext themeContext, PrintWriter pw) {}
}