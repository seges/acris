package sk.seges.acris.core.server.utils.io;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class StringFile extends File {

	private static final long serialVersionUID = -7314205250841055939L;
	public static final String FILE_SEPARATOR = "/";

	public StringFile(String pathname) {
		super(pathname);
	}

	public StringFile(URI uri) {
		super(uri);
	}

	public StringFile(String directory, String pathname) {
		super(directory, pathname);
	}

	public StringFile(File file, String pathname) {
		super(file, pathname);
	}

	public static StringFile getFileDescriptor(String fileName) {
		URL url;
		StringFile file;
		
		if (!fileName.startsWith(FILE_SEPARATOR) && !fileName.startsWith(".")) {
			url = StringFile.class.getResource(FILE_SEPARATOR + fileName);

			if (url == null) {
				throw new RuntimeException("File or directory should exists: " + fileName);
			}

			try {
				file = new StringFile(url.toURI());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		} else {
			file = new StringFile(fileName);

			if (!file.exists()) {
				throw new RuntimeException("File or directory should exists: " + fileName);
			}
		}
		
		return file;
	}
	
	public void writeTextToFile(final String content) throws IOException {

		final FileOutputStream fwOut = new FileOutputStream(this);
		fwOut.write(content.getBytes("UTF-8"));
		fwOut.close();
	}

	public String readTextFromFile() throws IOException {
		if (!this.exists()) {
			return null;
		}

		BufferedReader br;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					this)));
		} catch (FileNotFoundException e) {
			return null;
		}

		StringBuilder content = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			content.append(line);
		}
		
		br.close();

		return content.toString();
	}
}