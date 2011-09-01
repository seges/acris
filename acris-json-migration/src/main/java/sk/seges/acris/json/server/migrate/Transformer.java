/**
 * 
 */
package sk.seges.acris.json.server.migrate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author ladislav.gazo
 */
public class Transformer {
	private final ScriptEngine engine;
	private final String script = "jsont.js";
	
	private final File inputDir;
	private final File outputDir;

	public Transformer(File inputDir, File outputDir) throws ScriptException, FileNotFoundException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("ECMAScript");
		
		this.inputDir = inputDir;
		this.outputDir = outputDir;
	}

	public void transform(File transformation) {
		for(File data : inputDir.listFiles()) {
			try {
				String result = execute(data, transformation);
				FileWriter writer = new FileWriter(new File(outputDir, data.getName()));
				writer.write(result);
				writer.close();
			} catch (Exception e) {
				throw new RuntimeException("Unable to transform data = " + data + " using transformation = " + transformation, e);
			}
		}
	}
	
	private String execute(File data, File transformation)
			throws ScriptException, IOException {
		Reader reader = new InputStreamReader(getClass().getResourceAsStream(script));
		Bindings binding = engine.createBindings();
		binding.put("data", readFileAsString(data));
		binding.put("transformation", readFileAsString(transformation));
		return (String) engine.eval(reader, binding);
	}

	private static String readFileAsString(File transformation)
			throws java.io.IOException {
		StringBuilder fileData = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(transformation));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}
