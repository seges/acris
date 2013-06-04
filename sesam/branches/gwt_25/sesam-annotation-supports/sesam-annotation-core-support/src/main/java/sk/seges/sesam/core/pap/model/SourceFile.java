package sk.seges.sesam.core.pap.model;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.io.InputStream;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.api.Source;

public class SourceFile implements Source {

	private final InputStream inputStream;
	private CompilationUnit cu;
	
	public SourceFile(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getMethodBody(ExecutableElement element) {

		if (cu == null) {
	        try {
	        	cu = JavaParser.parse(inputStream);
	        } catch (ParseException e) {
	        	return null;
			} finally {
	        	try {
					inputStream.close();
				} catch (IOException e) {}
	        }
		}
		
        MethodVisitor methodVisitor = new MethodVisitor();
        methodVisitor.visit(cu, element);        
        return methodVisitor.getMethodBody();
	}

	private static class MethodVisitor extends VoidVisitorAdapter<ExecutableElement> {

		private String methodBody;
		
        @Override
        public void visit(MethodDeclaration n, ExecutableElement element) {
        	if (element.getSimpleName().toString().equals(n.getName()) && n.getParameters().size() == element.getParameters().size()) {
        		this.methodBody = n.getBody().toString();
        	}
        }
        
        public String getMethodBody() {
			return methodBody;
		}
    }
}
