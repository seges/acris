/**
 * 
 */
package sk.seges.acris.json.server.migrate;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;

/**
 * @author ladislav.gazo
 */
public class SchemaExporter {
	private final ObjectMapper mapper = new ObjectMapper();

	private final String version;
	private final File output;
	private final String[] classNames;

	public SchemaExporter(String version, File output, String... classNames) {
		this.version = version;
		this.output = output;
		this.classNames = classNames;
	}

	private void exportSchema(Class<?> clz, File resultFile)
			throws JsonGenerationException, IOException {
		JsonSchema jsonSchema = mapper.generateJsonSchema(clz);
		mapper.writeValue(resultFile, jsonSchema);
	}

	public void export() {
		File versionedOutput = new File(output, version);
		if (!versionedOutput.exists()) {
			versionedOutput.mkdir();
		}
		File resultFile = null;
		try {
			for (String className : classNames) {
				resultFile = new File(versionedOutput, className + ".jsons");
				exportSchema(Class.forName(className), resultFile);
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to export schema for file = " + resultFile, e);
		}
	}
}
