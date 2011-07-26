package sk.seges.sesam.pap.model.model;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;


public abstract class AbstractElementPrinter implements ElementPrinter {
	
	protected PrintWriter pw;
	
	public AbstractElementPrinter(PrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement typeElement) {
	}
	
	@Override
	public void finish(TypeElement typeElement) {
	}
}