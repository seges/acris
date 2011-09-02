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
 * Class containing logic for every transformer written.
 * 
 * @author ladislav.gazo
 * 
 * @param <T>
 *            Represents type of transformation used (e.g. File with
 *            transformation script or String with transformation script class)
 */
public abstract class Transformer<T> {
	protected final File inputDir;
	protected final File outputDir;

	public Transformer(File inputDir, File outputDir) throws ScriptException, FileNotFoundException {
		this.inputDir = inputDir;
		this.outputDir = outputDir;
	}

	public abstract void transform(T transformation);

	public static String readFileAsString(File file) throws java.io.IOException {
		StringBuilder fileData = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
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
