package sk.seges.acris.json.server.migrate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author ladislav.gazo
 * 
 * @see http://goessner.net/articles/jsont/
 */
public class JSONTransformer extends Transformer<File> {
	private final ScriptEngine engine;
	private final String script = "jsont.js";

	public JSONTransformer(File inputDir, File outputDir) throws ScriptException, FileNotFoundException {
		super(inputDir, outputDir);

		ScriptEngineManager mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("ECMAScript");
	}

	@Override
	public void transform(File transformation) {
		for (File data : inputDir.listFiles()) {
			try {
				String result = execute(data, transformation);
				FileWriter writer = new FileWriter(new File(outputDir, data.getName()));
				writer.write(result);
				writer.close();
			} catch (Exception e) {
				throw new RuntimeException("Unable to transform data = " + data + " using transformation = "
						+ transformation, e);
			}
		}
	}

	private String execute(File data, File transformation) throws ScriptException, IOException, NoSuchMethodException {
		Reader reader = new InputStreamReader(getClass().getResourceAsStream(script));
		String dataString = readFileAsString(data);
		String transformationString = readFileAsString(transformation);
		engine.eval(reader);
		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("transform", dataString, transformationString);
		return (String) result;
	}

}
