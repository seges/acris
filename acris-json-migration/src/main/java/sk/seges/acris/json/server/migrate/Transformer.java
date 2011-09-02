/**
 * 
 */
package sk.seges.acris.json.server.migrate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptException;

/**
 * @author ladislav.gazo
 */
public abstract class Transformer<T> {
	protected final File inputDir;
	protected final File outputDir;

	public Transformer(File inputDir, File outputDir) throws ScriptException, FileNotFoundException {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
	}

	public abstract void transform(T transformation);
	
	protected static String readFileAsString(File transformation)
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
