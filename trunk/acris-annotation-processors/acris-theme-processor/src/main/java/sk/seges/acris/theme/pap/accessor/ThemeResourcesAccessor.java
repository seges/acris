package sk.seges.acris.theme.pap.accessor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import sk.seges.acris.theme.client.annotation.ThemeResources;
import sk.seges.acris.theme.client.annotation.ThemeResources.ThemeResource;
import sk.seges.sesam.core.pap.accessor.AnnotationAccessor;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

import com.google.gwt.resources.client.ClientBundle;

public class ThemeResourcesAccessor extends AnnotationAccessor {

	public class ThemeResourceAccessor extends AnnotationAccessor {

		private final ThemeResource themeResource;
		
		ThemeResourceAccessor(ThemeResource themeResource, MutableProcessingEnvironment processingEnv) {
			super(processingEnv);
			
			this.themeResource = themeResource;
		}

		@Override
		public boolean isValid() {
			return themeResource != null;
		}

		public String getName() {
			return themeResource.name();
		}
			
		public Class<? extends ClientBundle> getResourceClass() {
			return themeResource.resourceClass();
		}

		public UiFieldMutableAccessor getUiField() {
			return new UiFieldMutableAccessor(themeResource.field(), processingEnv);
		}
	}
	
	private final ThemeResources themeResources;
	
	public ThemeResourcesAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		
		themeResources = getAnnotation(element, ThemeResources.class);
	}

	@Override
	public boolean isValid() {
		return themeResources != null && themeResources.value() != null && themeResources.value().length > 0;
	}

	public List<ThemeResourceAccessor> getValue() {
		if (!isValid()) {
			return new ArrayList<ThemeResourceAccessor>();
		}
		
		ThemeResource[] value = themeResources.value();
		
		List<ThemeResourceAccessor> result = new ArrayList<ThemeResourceAccessor>();
		
		for (ThemeResource themeResource: value) {
			result.add(new ThemeResourceAccessor(themeResource, processingEnv));
		}
		
		return result;
	}
}