package sk.seges.acris.generator.server.properties;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/sk/seges/acris/generator/properties-context.xml" })
public class SpringPropertyProcessorTest {

	@Autowired
	@Qualifier("string.test")
	private String stringTest;

	@Autowired
	@Qualifier("number.test")
	private Integer numberTest;
 
	@Autowired
	@Qualifier("double.test")
	private Double doubleTest;

	@Autowired
	@Qualifier("boolean.test")
	private Boolean booleanTest;

	@Test
	public void testPath() {
		Assert.assertEquals("text", stringTest);
		Assert.assertEquals(new Integer(1), numberTest);
		Assert.assertEquals(new Double(1.1), doubleTest);
		Assert.assertEquals(new Boolean(true), booleanTest);
	}
}
