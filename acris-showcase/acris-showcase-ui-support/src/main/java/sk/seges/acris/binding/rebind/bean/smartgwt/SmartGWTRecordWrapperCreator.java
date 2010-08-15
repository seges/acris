package sk.seges.acris.binding.rebind.bean.smartgwt;

import sk.seges.acris.binding.rebind.bean.core.EnhancedWrapperCreator;
import sk.seges.acris.binding.rebind.configuration.BindingNamingStrategy;

import com.google.gwt.user.rebind.SourceWriter;
import com.smartgwt.client.widgets.tree.TreeNode;


public class SmartGWTRecordWrapperCreator extends EnhancedWrapperCreator {

	@Override
	protected String suggestSuperclass(String typeName) {
		return TreeNode.class.getCanonicalName();
	}

	@Override
	protected void generateAttributeSetter(SourceWriter source, String name, String value) {
		source.println("setAttribute(" + name + ", " + value + ");");
	}

	@Override
	protected void getAttributeGetter(SourceWriter source, String param, String typeSimpleName, String typeQualifiedName) {
		if (typeSimpleName.equals("String")) {
			source.println("return getAttributeAsString(\"" + param + "\");");
			return;
		}

		if (typeSimpleName.equals("Date")) {
			source.println("return getAttributeAsDate(\"" + param + "\");");
			return;
		}

		if (typeSimpleName.equals("byte")) {
			source.println("Integer result = getAttributeAsInt(\"" + param + "\");");
			source.println("return result == null ? null : result.byteValue()");
			return;
		}
		if (typeSimpleName.equals("short")) {
			source.println("Integer result = getAttributeAsInt(\"" + param + "\");");
			source.println("return result == null ? null : result.shortValue()");
		}
		if (typeSimpleName.equals("int")) {
			source.println("return getAttributeAsInt(\"" + param + "\");");
			return;
		}
		if (typeSimpleName.equals("float")) {
			source.println("return getAttributeAsFloat(\"" + param + "\");");
			return;
		}
		if (typeSimpleName.equals("double")) {
			source.println("return getAttributeAsDouble(\"" + param + "\");");
			return;
		}
		if (typeSimpleName.equals("boolean")) {
			source.println("return getAttributeAsBoolean(\"" + param + "\");");
			return;
		}

		source.println("return (" + typeQualifiedName + ")getAttributeAsObject(\"" + param + "\");");
	}
	public SmartGWTRecordWrapperCreator(BindingNamingStrategy nameStrategy) {
		super(nameStrategy);
	}

}
