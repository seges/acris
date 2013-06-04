package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class GroupPrinter extends AbstractSettingsElementPrinter {

	private AbstractSettingsElementPrinter[] printers;
	
	public GroupPrinter(MutableProcessingEnvironment processingEnv, AbstractSettingsElementPrinter ... printers) {
		super(processingEnv);
		this.printers = printers;
	}
	
	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		for (AbstractSettingsElementPrinter printer: printers) {
			printer.initialize(type, outputName);
		}
	}

	@Override
	public void print(SettingsContext context) {
		for (AbstractSettingsElementPrinter printer: printers) {
			printer.print(context);
		}
	}

	@Override
	public void finish(TypeElement type) {
		for (AbstractSettingsElementPrinter printer: printers) {
			printer.finish(type);
		}
	}

	@Override
	public ElementKind getSupportedType() {
		return ElementKind.METHOD;
	}
}