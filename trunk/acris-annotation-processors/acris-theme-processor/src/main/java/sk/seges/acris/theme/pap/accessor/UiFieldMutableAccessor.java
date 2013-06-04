package sk.seges.acris.theme.pap.accessor;

import javax.lang.model.element.Element;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

import com.google.gwt.uibinder.client.UiField;

public class UiFieldMutableAccessor extends MutableAnnotationAccessor {

	private final UiField uiField;
	private final boolean accessorAlso;
	
	public UiFieldMutableAccessor(MutableProcessingEnvironment processingEnv) {
		super(UiField.class, processingEnv);
		this.uiField = null;
		this.accessorAlso = false;
	}

	public UiFieldMutableAccessor(Element element, MutableProcessingEnvironment processingEnv) {
		super(UiField.class, processingEnv);
		this.uiField = getAnnotation(element, UiField.class);
		this.accessorAlso = true;
	}

	public UiFieldMutableAccessor(UiField uiField, MutableProcessingEnvironment processingEnv) {
		super(UiField.class, processingEnv);
		this.uiField = uiField;
		this.accessorAlso = true;
	}

	public boolean isProvided() {
		return uiField.provided();
	}
	
	@Override
	public boolean isValid() {
		if (accessorAlso) {
			return uiField != null;
		}
		return super.isValid();
	}
	
	public UiFieldMutableAccessor setProvided(boolean provided) {
		setAnnotationValue("provided", provided);
		return this;
	}
}