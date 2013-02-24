package sk.seges.sesam.core.pap.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;

import sk.seges.sesam.core.pap.test.AnnotationTest;

/**
 * @author ladislav.gazo
 */
public class FluentProcessorTest extends AnnotationTest {
	private List<Processor> processorsUnderTest = new ArrayList<Processor>();
	
	protected FluentProcessorTest addProcessor(Processor... processors) {
		for(Processor processor : processors) {
			processorsUnderTest.add(processor);
		}
		return this;
	}

	@Override
	protected final Processor[] getProcessors() {
		if(processorsUnderTest.isEmpty()) {
			throw new RuntimeException("No processors defined");
		}
		Processor[] array = new Processor[processorsUnderTest.size()];
		processorsUnderTest.toArray(array);
		return array;
	}
	
	
}
