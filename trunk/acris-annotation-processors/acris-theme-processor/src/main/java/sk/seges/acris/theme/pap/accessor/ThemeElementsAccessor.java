package sk.seges.acris.theme.pap.accessor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.acris.theme.client.annotation.ThemeElements;
import sk.seges.acris.theme.client.annotation.ThemeElements.ThemeElement;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public class ThemeElementsAccessor extends AnnotationAccessor {

	public class ThemeElementAccessor extends AnnotationAccessor {

		private final ThemeElement themeElement;
		
		ThemeElementAccessor(ThemeElement themeElement, MutableProcessingEnvironment processingEnv) {
			super(processingEnv);
			
			this.themeElement = themeElement;
		}

		@Override
		public boolean isValid() {
			return themeElement != null;
		}
		
		public String getValue() {
			if (isValid()) {
				return themeElement.value();
			}
			
			return null;
		}
	}
	
	private final ThemeElements themeElements;
	
	public ThemeElementsAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		
		themeElements = getAnnotation(element, ThemeElements.class);
	}

	@Override
	public boolean isValid() {
		return (themeElements != null && themeElements.value() != null && themeElements.value().length > 0);
	}

	public List<ThemeElementAccessor> getValue() {
		if (!isValid()) {
			return new ArrayList<ThemeElementsAccessor.ThemeElementAccessor>();
		}
		
 		ThemeElement[] value = themeElements.value();
		
		List<ThemeElementAccessor> result = new ArrayList<ThemeElementsAccessor.ThemeElementAccessor>();
		
		for (ThemeElement themeElement: value) {
			result.add(new ThemeElementAccessor(themeElement, processingEnv));
		}
		
		return result;
	}
}