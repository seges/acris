package sk.seges.acris.pap.service;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.core.client.annotation.RemoteServicePath;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AsyncServiceProcessor extends AbstractConfigurableProcessor {

	public static final String ASYNC_SUFFIX = "Async";

	public static NamedType getOutputClass(ImmutableType inputClass) {
		return inputClass.addClassSufix(ASYNC_SUFFIX);
	}
		
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		Class<?> serviceAnnotationClass = null;
		RemoteServiceRelativePath remoteServiceRelativePath = typeElement.getAnnotation(RemoteServiceRelativePath.class);
		if (remoteServiceRelativePath != null) {
			serviceAnnotationClass = RemoteServiceRelativePath.class;
		}
		RemoteServicePath remoteServicePath = typeElement.getAnnotation(RemoteServicePath.class);
		if (remoteServicePath != null) {
			serviceAnnotationClass = RemoteServicePath.class;
		}

		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		List<Type> imports = new ArrayList<Type>();
		
		for (ExecutableElement method: methodsIn) {
			imports.add(getNameTypes().toType(method.getReturnType()));
			for (VariableElement parameter: method.getParameters()) {
				imports.add(getNameTypes().toType(parameter.asType()));
			}
		}

		imports.add(AsyncCallback.class);
		imports.add(serviceAnnotationClass);

		return imports.toArray(new Type[] {});
	}
	
	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType inputClass) {
		return new NamedType[] { 
				getOutputClass(inputClass) 
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new AsyncServiceProcessorConfigurer();
	}	
	
	@Override
	protected void writeClassAnnotations(PrintWriter pw, Element el) {
		RemoteServiceRelativePath remoteServiceRelativePath = el.getAnnotation(RemoteServiceRelativePath.class);
		if (remoteServiceRelativePath != null) {
			pw.println("@" + RemoteServiceRelativePath.class.getSimpleName() + "(\"" + remoteServiceRelativePath.value() + "\")");
		}
		RemoteServicePath remoteServicePath = el.getAnnotation(RemoteServicePath.class);
		if (remoteServicePath != null) {
			pw.println("@" + RemoteServicePath.class.getSimpleName() + "(\"" + remoteServicePath.value() + "\")");
		}
		super.writeClassAnnotations(pw, el);
	}

	@Override
	protected boolean checkPreconditions(Element element, NamedType outputName, boolean alreadyExists) {
		return element.getAnnotation(Generated.class) == null;
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {
		List<ExecutableElement> methodsIn = ElementFilter.methodsIn(element.getEnclosedElements());
		
		for (ExecutableElement method: methodsIn) {
			pw.print("void " + method.getSimpleName().toString() + "(");
			
			int i = 0;
			for (VariableElement parameter: method.getParameters()) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(getNameTypes().toType(parameter.asType()).toString(null, ClassSerializer.SIMPLE, true) + " " + parameter.getSimpleName().toString());
				i++;
			}
			
			TypeMirror returnType = method.getReturnType();

			if (i > 0) {
				pw.print(", ");
			}
			pw.println(AsyncCallback.class.getSimpleName() + "<" + unBoxType(returnType).toString(null, ClassSerializer.SIMPLE, true) + "> callback);");
			pw.println();
			//TODO add throws
		}
	}
	
	protected NamedType unBoxType(TypeMirror type) {
		switch (type.getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case INT:
		case LONG:
		case DOUBLE:
		case FLOAT:
		case SHORT:
			return getNameTypes().toType(processingEnv.getTypeUtils().boxedClass((PrimitiveType)type).asType());
		case VOID:
			return getNameTypes().toType(Void.class);
		case ARRAY:
			return new ArrayNamedType(unBoxType(((ArrayType)type).getComponentType()));
		case ERROR:
		case NULL:
		case NONE:
		case OTHER:
		case EXECUTABLE:
			processingEnv.getMessager().printMessage(Kind.ERROR, " [ERROR] Cannot unbox type " + type.getKind() + " - unsupported type!");
			return null;
		}
		
		return getNameTypes().toType(type);
	}
}
