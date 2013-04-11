package sk.seges.sesam.core.pap.builder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

public abstract class ClassPathFinder {

	protected static final String PATH_SEPARATOR = "path.separator";
	protected static final String PROCESSOR_CLASS_PATH = "processor.class.path";

	interface FileTypeHandler {

		boolean isFileOfType(String canonicalName);
		
		String getExtension();
		
		void handleFile(InputStreamProvider inputStreamProvider, String canonicalName, ProcessingEnvironment processingEnv, Map<String, Set<String>> annotatedClasses);
		
		boolean continueProcessing();
	}

	interface InputStreamProvider {
		InputStream getInputStream();
	}
	
	class FileInputStreamProvider implements InputStreamProvider {

		private final File file;
		
		public FileInputStreamProvider(File file) {
			this.file = file;
		}
		
		@Override
		public InputStream getInputStream() {
	        try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return null;
			}
		}		
	}

	class JarEntryInputStreamProvider implements InputStreamProvider {

		private final JarFile jarFile;
		private final JarEntry jarEntry;

		public JarEntryInputStreamProvider(JarFile jarFile, JarEntry jarEntry) {
			this.jarFile = jarFile;
			this.jarEntry = jarEntry;
		}

		@Override
		public InputStream getInputStream() {
			try {
				return jarFile.getInputStream(jarEntry);
			} catch (IOException e) {
				return null;
			}
		}
	}

	private static FileFilter DIRECTORIES_ONLY = new FileFilter() {
		@Override
		public boolean accept(File f) {
			return (f.exists() && f.isDirectory());
		}
	};

	private static Comparator<URL> URL_COMPARATOR = new Comparator<URL>() {
		@Override
		public int compare(URL u1, URL u2) {
			return String.valueOf(u1).compareTo(String.valueOf(u2));
		}
	};

	protected ProcessingEnvironment processingEnv;
	protected String packageName;

	protected ClassPathFinder(ProcessingEnvironment processingEnv, String packageName) {
		this.processingEnv = processingEnv;
		this.packageName = packageName;
	}
	
	private Map<URL, String> getClasspathLocations(String classpath) {
		Map<URL, String> map = new TreeMap<URL, String>(URL_COMPARATOR);
		File file = null;

		String pathSep = System.getProperty(PATH_SEPARATOR);
		
		List<String> classPathElements = new ArrayList<String>();
		
		if (classpath != null && pathSep != null) {
			StringTokenizer st = new StringTokenizer(classpath, pathSep);
			while (st.hasMoreTokens()) {
				String path = st.nextToken().trim();
				if (!classPathElements.contains(path)) {
					classPathElements.add(path);
					file = new File(path);
					include(null, file, map);
				}
			}
		}
						
		return map;
	}

	private void include(String name, File file, Map<URL, String> map) {
		if (!file.exists()) {
			return;
		}
		
		if (!file.isDirectory()) {
			// could be a JAR file
			includeJar(file, map);
			return;
		}

		if (name == null) {
			name = "";
		} else {
			name += ".";
		}
		
		// add subpackages
		File[] dirs = file.listFiles(DIRECTORIES_ONLY);
		for (int i = 0; i < dirs.length; i++) {
			try {
				// add the present package
				map.put(new URL("file://" + dirs[i].getCanonicalPath()), name + dirs[i].getName());
			} catch (IOException ioe) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find " + name + dirs[i].getName() + "." + ioe.getMessage());
				return;
			}

			include(name + dirs[i].getName(), dirs[i], map);
		}
	}

	private static String getOsName() {
		return System.getProperty("os.name");
	}

	private boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	private void includeJar(File file, Map<URL, String> map) {
		if (file.isDirectory()) {
			return;
		}

		URL jarURL = null;
		JarFile jar = null;
		try {
			if (isWindows()) {
				jarURL = new URL("file:/" + file.getCanonicalPath());
			} else {
				jarURL = new URL("file://" + file.getCanonicalPath());
			}
			jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
			JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
			jar = conn.getJarFile();
		} catch (Exception e) {
			try {
				processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find " + file.getCanonicalPath() + "." + e.getMessage());
			} catch (IOException e1) {
				processingEnv.getMessager().printMessage(Kind.WARNING, e.getMessage() + " -- " + file); 
			}
			// not a JAR or disk I/O error
			// either way, just skip
			return;
		}

		if (jar == null || jarURL == null) {
			return;
		}

		// include the jar's "default" package (i.e. jar's root)
		map.put(jarURL, "");

		Enumeration<JarEntry> e = jar.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();

			if (entry.isDirectory()) {
				if (entry.getName().toUpperCase().equals("META-INF/")) {
					continue;
				}

				try {
					map.put(new URL(jarURL.toExternalForm() + entry.getName()), packageNameFor(entry));
				} catch (MalformedURLException murl) {
					// whacky entry?
					continue;
				}
			}
		}
	}

	private String packageNameFor(JarEntry entry) {
		if (entry == null) {
			return "";
		}
		String s = entry.getName();
		if (s == null) {
			return "";
		}
		if (s.length() == 0) {
			return s;
		}
		if (s.startsWith("/")) {
			s = s.substring(1, s.length());
		}
		if (s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}
		return s.replace('/', '.');
	}
	
	protected Map<String, Set<String>> getSubclasses(String classpath, FileTypeHandler fileTypeHandler) {

		Map<URL, String> classpathLocations = getClasspathLocations(classpath);
		
		Iterator<URL> it = classpathLocations.keySet().iterator();
		
		Map<String, Set<String>> annotatedClasses = new HashMap<String, Set<String>>();
		
		while (it.hasNext()) {
			URL url = it.next();
			if (!findSubclasses(url, classpathLocations.get(url), annotatedClasses, fileTypeHandler)) {
				return annotatedClasses;
			}
		}
		
		return annotatedClasses;
	}

	private boolean findSubclasses(URL location, String packageName, Map<String, Set<String>> annotatedClasses, FileTypeHandler fileTypeHandler) {

		if (!packageName.startsWith(this.packageName)) {
			return true;
		}
		
		// TODO: add getResourceLocations() to this list

		// Get a File object for the package
		File directory = new File(location.getFile());

		if (directory.exists()) {
			// Get the list of the files contained in the package
			File[] files = directory.listFiles();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in files with specific extension
				if (files[i].getPath().endsWith(fileTypeHandler.getExtension())) {
					String canonicalName = packageName + "." + files[i].getName().substring(0, files[i].getName().length() - fileTypeHandler.getExtension().length());
					
					if (fileTypeHandler.isFileOfType(canonicalName)) {
						fileTypeHandler.handleFile(new FileInputStreamProvider(files[i]), canonicalName, processingEnv, annotatedClasses);
						
						if (!fileTypeHandler.continueProcessing()) {
							return false;
						}
					}
				}
			}
		} else {
			try {
				// It does not work with the filesystem: we must
				// be in the case of a package contained in a jar file.
				JarURLConnection conn = (JarURLConnection) location.openConnection();
				// String starts = conn.getEntryName();
				JarFile jarFile = conn.getJarFile();

				Enumeration<JarEntry> e = jarFile.entries();
				while (e.hasMoreElements()) {
					JarEntry entry = e.nextElement();
					String entryname = entry.getName();

					if (!entry.isDirectory() && entryname.endsWith(fileTypeHandler.getExtension())) {
						String classname = entryname.substring(0, entryname.length() - fileTypeHandler.getExtension().length());
						if (classname.startsWith("/")) {
							classname = classname.substring(1);
						}
						classname = classname.replace('/', '.');

						if (fileTypeHandler.isFileOfType(classname)) {
							fileTypeHandler.handleFile(new JarEntryInputStreamProvider(jarFile, entry), classname, processingEnv, annotatedClasses);
							
							if (!fileTypeHandler.continueProcessing()) {
								return false;
							}
						}
					}
				}
			} catch (Throwable ex) {
			}
		}
		
		return true;
	}
}