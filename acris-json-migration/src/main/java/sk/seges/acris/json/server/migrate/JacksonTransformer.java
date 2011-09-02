package sk.seges.acris.json.server.migrate;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import javax.script.ScriptException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author ladislav.gazo
 */
public class JacksonTransformer extends Transformer<String> {

	public JacksonTransformer(File inputDir, File outputDir) throws ScriptException, FileNotFoundException {
		super(inputDir, outputDir);
	}

	@Override
	public void transform(String transformationClass) {
		for (File data : inputDir.listFiles()) {
			ObjectMapper m = new ObjectMapper();
			try {
				JsonNode jsonNode = m.readValue(data, JsonNode.class);

				Object transformation = Class.forName(transformationClass).newInstance();
				Method method = transformation.getClass().getMethod("execute", JsonNode.class);
				method.invoke(transformation, jsonNode);

				m.writeValue(new File(outputDir, data.getName()), jsonNode);
			} catch (Exception e) {
				throw new RuntimeException("Unable to transform data = " + data + " using transformationClass = "
						+ transformationClass, e);
			}
		}
	}

}
