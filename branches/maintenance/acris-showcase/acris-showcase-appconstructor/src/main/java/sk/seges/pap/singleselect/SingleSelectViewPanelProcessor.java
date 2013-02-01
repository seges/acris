/**
 * 
 */
package sk.seges.pap.singleselect;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.acris.scaffold.model.domain.DomainModel;
import sk.seges.acris.scaffold.model.view.compose2.Singleselect;
import sk.seges.acris.scaffold.mvp.DefaultViewConfiguration;
import sk.seges.pap.printer.table.column.TableColumnPrinter;
import sk.seges.pap.type.DisplayType;
import sk.seges.pap.type.PanelType;
import sk.seges.pap.type.ViewTransferObjectType;
import sk.seges.sesam.core.pap.FluentProcessor;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;

/**
 * @author ladislav.gazo
 */
public class SingleSelectViewPanelProcessor extends FluentProcessor {
	private final DefaultViewConfiguration defaultConfiguration = new DefaultViewConfiguration();

	public SingleSelectViewPanelProcessor() {
		reactsOn(Singleselect.class);
		setSuperClass(Composite.class);
		addImplementedInterface(new AlwaysRule() {
			@Override
			public List<MutableDeclaredType> getTypes(
					MutableDeclaredType typeElement) {
				return asList((MutableDeclaredType) new DisplayType(typeElement));
			}
		});
	}

	@Override
	protected MutableDeclaredType getResultType(MutableDeclaredType inputType) {
		return new PanelType(inputType);
	}

	@Override
	protected void doProcessElement(ProcessorContext context) {
		List<? extends TypeMirror> interfaces = context.getTypeElement()
				.getInterfaces();
		TypeMirror vtoMirror = findDomainModel(interfaces);
		if (vtoMirror == null) {
			throw new RuntimeException(
					"Yet not handled state where there is no background domain model. Maybe in future we can take methods from the interface directly");
		}

		ViewTransferObjectType vto = new ViewTransferObjectType((MutableDeclaredType) processingEnv
				.getTypeUtils().toMutableType(vtoMirror));

		// fields
		pw.println("protected ", CellTable.class, "<", vto, "> table = new ",
				CellTable.class, "<", vto, ">();");

		pw.println("private ", ListDataProvider.class, "<", vto,
				"> provider = new ", ListDataProvider.class, "<", vto, ">();");

		// constructor
		pw.println("public ", context.getOutputType().getSimpleName(), "() {");
		pw.println("provider.addDataDisplay(table);");
		pw.println("initWidget(table);");

		// -- process properties
		final TableColumnElementAction action = new TableColumnElementAction(
				pw, vto);
		doForAllMembers(context.getTypeElement(), ElementKind.METHOD, action);

		pw.println("}");

		// methods
		pw.println("public void setValue(", List.class, "<", vto, "> values) {");
		pw.println("provider.setList(values);");
		pw.println("}");

		System.out.println("");
	}

	private TypeMirror findDomainModel(List<? extends TypeMirror> interfaces) {
		TypeMirror domaimModelMirror = processingEnv.getTypeUtils()
				.fromMutableType(
						processingEnv.getTypeUtils().toMutableType(
								DomainModel.class));

		for (TypeMirror ifaceMirror : interfaces) {
			if (processingEnv.getTypeUtils().isAssignable(ifaceMirror,
					domaimModelMirror)) {
				return ifaceMirror;
			}
		}

		return null;
	}

	public class TableColumnElementAction extends MethodAction {
		protected final FormattedPrintWriter pw;
		protected final MutableDeclaredType vto;

		public TableColumnElementAction(FormattedPrintWriter pw,
				MutableDeclaredType vto) {
			super();
			this.pw = pw;
			this.vto = vto;
		}

		@Override
		protected void doExecute(ExecutableElement element) {
			TypeMirror returnType = element.getReturnType();
			@SuppressWarnings("unchecked")
			Class<? extends TableColumnPrinter> renderComponent = (Class<? extends TableColumnPrinter>) defaultConfiguration
					.getRenderComponent(Singleselect.class,
							toElement(returnType).getQualifiedName().toString());
			if (renderComponent == null) {
				// TODO: handle the ultimate default
				return;
			}
			try {
				Constructor<? extends TableColumnPrinter> constructor = renderComponent
						.getConstructor(FormattedPrintWriter.class,
								MutableDeclaredType.class);
				TableColumnPrinter printer = constructor.newInstance(pw, vto);
				printer.print(element);
			} catch (Exception e) {
				throw new RuntimeException("Unable to construct printer for = "
						+ renderComponent, e);
			}
		}
	}
}
