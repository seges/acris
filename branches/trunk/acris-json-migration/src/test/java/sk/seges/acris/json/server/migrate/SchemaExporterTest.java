/**
 * 
 */
package sk.seges.acris.json.server.migrate;

import java.io.File;

import org.junit.Test;

/**
 * @author ladislav.gazo
 */
public class SchemaExporterTest {
	@Test
	public void testExportJSONClass() {
		String version = "1.0.0";
		File output = new File("target/test-out/schema");
		if(!output.exists()) {
			output.mkdirs();
		}
		SchemaExporter exporter = new SchemaExporter(version, output, MockJSONv1.class.getName(), MockJSONv2.class.getName());
		exporter.export();
	}
}
