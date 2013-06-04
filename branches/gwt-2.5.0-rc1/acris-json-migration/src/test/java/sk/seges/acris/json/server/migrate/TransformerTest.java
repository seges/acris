/**
 * 
 */
package sk.seges.acris.json.server.migrate;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * @author ladislav.gazo
 */
public class TransformerTest {
	@Test
	public void testTransformFromV1ToV2() throws Exception {
		File input = new File("target/test-out/input");
		if(!input.exists()) {
			input.mkdirs();
		}
		
		File output = new File("target/test-out/output");
		if(!output.exists()) {
			output.mkdirs();
		}

		
		MockJSONv1 v1 = new MockJSONv1();
		v1.setName("jozko");
		v1.setLenght(2);
		v1.setTags(new String[] {"red", "blue"});
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(input, "data.json"), v1);
		
		JacksonTransformer t = new JacksonTransformer(input, output);
		t.transform(MockV1toV2Script.class.getName());
		
		File[] files = output.listFiles();
		assertEquals(input.listFiles().length, files.length);
		
		for(File file : files) {
			MockJSONv1 orig = mapper.readValue(new File(input, file.getName()), MockJSONv1.class);
			MockJSONv2 value = mapper.readValue(file, MockJSONv2.class);
			assertEquals(orig.getName(), value.getTitle());
			assertEquals(orig.getLenght(), value.getLenght());
			assertEquals(orig.getTags().length, value.getTags().length);
		}
	}
}
