package sk.seges.sesam.pap.test.selenium.processor.printer;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.model.setting.SettingsIterator;
import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;
import sk.seges.sesam.pap.configuration.printer.AbstractSettingsElementPrinter;

public class SettingInstancerPrinter extends AbstractSettingsElementPrinter {
	
	private final FormattedPrintWriter pw;
	private final AnnotationMirror annotationMirror;
	private int i = 0;
	private final boolean includeDefaults;
	
	public SettingInstancerPrinter(AnnotationMirror annotationMirror, MutableProcessingEnvironment processingEnv, FormattedPrintWriter pw, boolean includeDefaults) {
		super(processingEnv);
		this.pw = pw;
		this.annotationMirror = annotationMirror;
		this.includeDefaults = includeDefaults;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		pw.print("new ", type, "(");
	}

	public void initialize(MutableDeclaredType type, MutableDeclaredType outputName) {
		pw.print("new ", type, "(");
	}
	
	@Override
	public void print(SettingsContext context) {
		if (i > 0) {
			pw.print(", ");
		}

		Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
		
		if (includeDefaults) {
			elementValues = processingEnv.getElementUtils().getElementValuesWithDefaults(annotationMirror);
		}
		
		AnnotationValue annotationValue = elementValues.get(context.getMethod());

		Object value = null;
		
		if (annotationValue != null) {
			value = annotationValue.getValue();	
			
			if (value instanceof String) {
				value = NullCheck.checkNull((String)value);
			} else if (value instanceof Integer) {
				value = NullCheck.checkNull((Integer)value);
			}
		}
		
		if (context.getNestedElement() != null) {
		
			if (value != null) {
				new SettingInstancerPrinter((AnnotationMirror)value, processingEnv, pw, includeDefaults).print(new SettingsTypeElement((DeclaredType)context.getMethod().getReturnType(), processingEnv), context.getNestedMutableType());
			} else {
				pw.println("null");
			}
		} else {
			if (value != null) {
				if (value instanceof String) {
					pw.print("\"" + value + "\"");
				} else {
					if (value instanceof Element && ((Element)value).getKind().equals(ElementKind.ENUM_CONSTANT)) {
						pw.print(((Element)value).asType(), "." + ((Element)value).getSimpleName().toString());
					} else {
						pw.print(value);
					}
				}
			} else {
				pw.print("null");
			}
		}
		i++;
	}

	public void print(SettingsTypeElement settingsTypeElement, MutableDeclaredType outputName) {
		SettingInstancerPrinter settingInstancerPrinter = new SettingInstancerPrinter(annotationMirror, processingEnv, pw, includeDefaults);
		
		settingInstancerPrinter.initialize(settingsTypeElement, outputName);

		SettingsIterator settingsIterator = new SettingsIterator(annotationMirror, processingEnv);
		while (settingsIterator.hasNext()) {
			settingsIterator.next().handle(settingInstancerPrinter);
		}

		settingInstancerPrinter.finish(settingsTypeElement);
	}
	
	@Override
	public void finish(TypeElement type) {
		finish();
	}

	public void finish(MutableDeclaredType type) {
		finish();
	}
	
	private void finish() {
		pw.print(")");
	}
}