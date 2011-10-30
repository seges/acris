package sk.seges.sesam.pap.metadata.iterator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.model.metadata.strategy.MetamodelMethodStrategy;
import sk.seges.sesam.pap.metadata.accessor.MetaModelAccessor;

public class ClassIterator {

	private final TypeElement element;
	private final MutableProcessingEnvironment processingEnv;
	
	public interface ElementHandler {
		void handle(Element element, String elementName);
	}
	
	public ClassIterator(TypeElement element, MutableProcessingEnvironment processingEnv) {
		this.element = element;
		this.processingEnv = processingEnv;
	}
	
	public void iterateFields(ElementHandler handler) {
		List<? extends Element> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

		Collections.sort(fields, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
			}

		});
		
		for (Element field : fields) {
			if (field.getModifiers().contains(Modifier.STATIC) || field.getModifiers().contains(Modifier.PRIVATE) || 
				field.getModifiers().contains(Modifier.PROTECTED) || field.getModifiers().contains(Modifier.FINAL)) {
				continue;
			}

			String fieldName = field.getSimpleName().toString();

			String getter = MethodHelper.toGetter(fieldName);

			if (ProcessorUtils.hasMethod(getter, element)) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "Field " + fieldName + " is accessible by public modifier and also using the " + getter + " method.", element);
				continue;
			}

			handler.handle(field, fieldName);
		}
	}
	
	public void iterateMethods(ElementHandler handler) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
		
		Collections.sort(methods, new Comparator<ExecutableElement>() {

			@Override
			public int compare(ExecutableElement o1, ExecutableElement o2) {
				return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
			}

		});

		MetaModelAccessor metaModelAccessor = new MetaModelAccessor(element, processingEnv);
		MetamodelMethodStrategy methodStrategy = metaModelAccessor.getMethodStrategy();
		
		for (ExecutableElement method : methods) {
			if (method.getModifiers().contains(Modifier.STATIC) || method.getModifiers().contains(Modifier.PRIVATE) || method.getModifiers().contains(Modifier.PROTECTED)) {
				continue;
			}

			if (MetamodelMethodStrategy.GETTER_SETTER.equals(methodStrategy) && !MethodHelper.isGetterMethod(method)) {
				//only getters are interesting
				continue;
			}

			String simpleMethodName = method.getSimpleName().toString();

			if (MetamodelMethodStrategy.GETTER_SETTER.equals(methodStrategy)) {
	
				String setterMethodName = MethodHelper.toSetter(method);
				
				ExecutableElement setterMethod = ProcessorUtils.getMethodByParameterType(setterMethodName, element, 0, method.getReturnType(), processingEnv.getTypeUtils());
				if (setterMethod == null) {
					//setter method is not accessible
					continue;
				}
	
				handler.handle(method, MethodHelper.toField(method));
			} else if(MetamodelMethodStrategy.PURE.equals(methodStrategy)) {
				handler.handle(method, simpleMethodName);
			}
		}
	}
}