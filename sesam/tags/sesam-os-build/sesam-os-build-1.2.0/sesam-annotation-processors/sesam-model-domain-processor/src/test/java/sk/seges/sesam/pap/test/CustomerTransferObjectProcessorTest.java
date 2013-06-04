package sk.seges.sesam.pap.test;

import javax.annotation.processing.Processor;

import org.junit.Test;

import sk.seges.sesam.core.pap.test.AnnotationTest;
import sk.seges.sesam.pap.customer.TestAddressData;
import sk.seges.sesam.pap.model.TransferObjectConverterProcessor;
import sk.seges.sesam.pap.model.TransferObjectProcessor;

import com.sun.org.apache.bcel.internal.classfile.Field;


public class CustomerTransferObjectProcessorTest extends AnnotationTest {

	@Test
	public void testNestedBounds() {

		assertCompilationSuccessful(compileFiles(TestAddressData.class.getPackage(), Field.class.getPackage()));

	//	assertCompilationSuccessful(compileFiles(MockEntityDtoConfiguration.class, DomainObject.class, SecondDomainObject.class));
	//	assertOutput(getResourceFile(TestBeanNestedBounds.class), getOutputFile(TestBeanNestedBounds.class));
	}

	@Override
	protected Processor[] getProcessors() {
		return new Processor[] {
			new TransferObjectProcessor(),
			new TransferObjectConverterProcessor()
		};
	}

//	private File getOutputFile(Class<?> clazz) {
//		OutputClass inputClass = new OutputClass(clazz.getPackage().getName(), clazz.getSimpleName());
//		NamedType outputClass = TransferObjectProcessor.getOutputClass(inputClass);
//		return new File(OUTPUT_DIRECTORY, toPath(outputClass.getPackageName()) + "/" + outputClass.getSimpleName() + SOURCE_FILE_SUFFIX);
//	}
}