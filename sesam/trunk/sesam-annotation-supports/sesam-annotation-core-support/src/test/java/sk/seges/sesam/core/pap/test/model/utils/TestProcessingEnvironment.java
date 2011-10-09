package sk.seges.sesam.core.pap.test.model.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public class TestProcessingEnvironment implements ProcessingEnvironment {

	@Override
	public Map<String, String> getOptions() {
		return new HashMap<String, String>();
	}

	@Override
	public Messager getMessager() {
		return new Messager() {
			
			@Override
			public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
				System.out.println(kind + " " + msg + " " + e.toString());
			}
			
			@Override
			public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
				System.out.println(kind + " " + msg + " " + e.toString());
			}
			
			@Override
			public void printMessage(Kind kind, CharSequence msg, Element e) {
				System.out.println(kind + " " + msg + " " + e.toString());
			}
			
			@Override
			public void printMessage(Kind kind, CharSequence msg) {
				System.out.println(kind + " " + msg + " ");
			}
		};
	}

	@Override
	public Filer getFiler() {
		throw new RuntimeException("Unable to use filer in the test environment");
	}

	@Override
	public Elements getElementUtils() {
		return null;
	}

	@Override
	public Types getTypeUtils() {
		return null;
	}

	@Override
	public SourceVersion getSourceVersion() {
		return SourceVersion.latest();
	}

	@Override
	public Locale getLocale() {
		return Locale.ENGLISH;
	}
}