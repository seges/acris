package sk.seges.sesam.core.pap.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;

public class ClassPathTypeUtils implements ClassPathTypes {

	static class Project {
		
		private static Set<Project> projects = new HashSet<Project>();

		private String projectName;
		private Map<String, Set<String>> annotatedElements;

		private Project(String projectName) {
			this.projectName = projectName;
		}
		
		public static Project get(String projectName) {
			Iterator<Project> iterator = projects.iterator();
			
			while (iterator.hasNext()) {
				Project project = iterator.next();
				if (project.getProjectName().equals(projectName)) {
					return project;
				}
			}
			
			Project project = new Project(projectName);
			projects.add(project);
			
			return project;
		}
		
		public boolean isInitialized() {
			return annotatedElements != null;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}
		
		public String getProjectName() {
			return projectName;
		}
		
		public Set<String> getAnnotatedElementNames(String annotationType) {
			if (annotatedElements == null) {
				return null;
			}
			return annotatedElements.get(annotationType);
		}
		
		public void setAnnotatedElementNames(Map<String, Set<String>> annotatedElementNames) {
			this.annotatedElements = annotatedElementNames;
			if (annotatedElements == null) {
				this.annotatedElements = new HashMap<String, Set<String>>();
			}
		}

		public void addAnnotatedElementNames(Map<String, Set<String>> annotatedElementNames) {
			for (Entry<String, Set<String>> entry: annotatedElementNames.entrySet()) {
				Set<String> existingSet = this.annotatedElements.get(entry.getKey());
				
				if (existingSet == null) {
					existingSet = new HashSet<String>();
					this.annotatedElements.put(entry.getKey(), existingSet);
				}
				
				for (String className: entry.getValue()) {
					if (!existingSet.contains(className)) {
						existingSet.add(className);
					}
				}
			}
		}
		
		public void addAnnotatedElementNames(String annotationType, Set<String> elementNames) {
			if (annotatedElements == null) {
				annotatedElements = new HashMap<String, Set<String>>();
			}
			annotatedElements.put(annotationType, elementNames);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Project other = (Project) obj;
			if (projectName == null) {
				if (other.projectName != null)
					return false;
			} else if (!projectName.equals(other.projectName))
				return false;
			return true;
		}	
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "factorypathentry")
	static class FactoryPathEntry {

		@XmlAttribute(name = "kind")
		private String kind;

		@XmlAttribute(name = "id")
		private String id;
		
		@XmlAttribute(name = "enabled")
		private boolean enabled;
		
		@XmlAttribute(name = "runInBatchMode")
		private boolean runInBatchMode;

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isRunInBatchMode() {
			return runInBatchMode;
		}

		public void setRunInBatchMode(boolean runInBatchMode) {
			this.runInBatchMode = runInBatchMode;
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "factorypath")
	static class FactoryPath {

		@XmlElement(name = "factorypathentry")
		private List<FactoryPathEntry> entries;
		
		public List<FactoryPathEntry> getEntries() {
			return entries;
		}
		
		public void setEntries(List<FactoryPathEntry> entries) {
			this.entries = entries;
		}
	}
	
	private ProcessingEnvironment processingEnv;
	private Project project;
	
	public ClassPathTypeUtils(ProcessingEnvironment processingEnv, String projectName) {
		this.processingEnv = processingEnv;
		this.project = Project.get(projectName);

		if (!project.isInitialized()) {
			initializeSystemProperties();
		}
	}
	
	private String readContent(File fileName) {
		String result = "";
		String line = "";
		FileInputStream fin;
		try {
			fin = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			return "";
		}
        BufferedReader fileInput = new BufferedReader(new InputStreamReader(fin));
        try {
			while ((line = fileInput.readLine()) != null) {  
				result += line;
			}
		} catch (IOException e) {
			return "";
		}
        
        return result;
	}

	protected static final String ROOT_DIRECTORY_OPTION = "rootDirectory";
	protected static final String M2_REPO_OPTION = "M2_REPO";
	private static final String M2_REPO = "M2_REPO"; //variable in the factory path
	private static final String PROCESSOR_CLASS_PATH = "processor.class.path";
	private static final String PATH_SEPARATOR = "path.separator";
	private static final String FACTORY_PATH_FILE_NAME = ".factorypath";
	
	private void initializeSystemProperties() {
		
		String classPath = processingEnv.getOptions().get(AbstractConfigurableProcessor.CLASSPATH_OPTION);
		
		if (classPath != null) {
			if (classPath.startsWith("[")) {
				classPath = classPath.substring(1, classPath.length() - 2);
				classPath = classPath.replaceAll(",", System.getProperty(PATH_SEPARATOR));
			}
			System.setProperty(PROCESSOR_CLASS_PATH, classPath);
		}
		
		final String rootDirectory = processingEnv.getOptions().get(ROOT_DIRECTORY_OPTION);
		String m2Repo = processingEnv.getOptions().get(M2_REPO_OPTION);
		
		if (rootDirectory != null && m2Repo != null) {
			if (File.separator.equals("\\")) {
				m2Repo = m2Repo.replace("\\", "/");
			}

			String factoryPathName = rootDirectory + File.separator + FACTORY_PATH_FILE_NAME;
			
			File rootDirectoryFile = new File(factoryPathName);
			if (rootDirectoryFile.exists()) {
				JAXBContext context;
				try {
					context = JAXBContext.newInstance(FactoryPath.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					JAXBElement<FactoryPath> element = (JAXBElement<FactoryPath>) unmarshaller.unmarshal(new StreamSource(new StringReader(readContent(rootDirectoryFile))), FactoryPath.class);
					FactoryPath factoryPath = element.getValue();
					
					String separator = System.getProperty(PATH_SEPARATOR);

					classPath = "";
					if (factoryPath != null && factoryPath.getEntries() != null) {
						for (FactoryPathEntry entry: factoryPath.getEntries()) {
							
							String dependency = entry.getId().replaceAll(M2_REPO, m2Repo);
							
							classPath += dependency + separator;
						}
					}
					
					System.setProperty(PROCESSOR_CLASS_PATH, classPath);
				} catch (JAXBException e) {}
			}
		}
	}

	private Map<URL, String> getClasspathLocations(String classpath) {
		Map<URL, String> map = new TreeMap<URL, String>(URL_COMPARATOR);
		File file = null;

		String pathSep = System.getProperty(PATH_SEPARATOR);
		
		if (classpath != null && pathSep != null) {
			StringTokenizer st = new StringTokenizer(classpath, pathSep);
			while (st.hasMoreTokens()) {
				String path = st.nextToken().trim();
				file = new File(path);
				include(null, file, map);
			}
		}
		
		return map;
	}

	private static FileFilter DIRECTORIES_ONLY = new FileFilter() {
		public boolean accept(File f) {
			return (f.exists() && f.isDirectory());
		}
	};

	private static Comparator<URL> URL_COMPARATOR = new Comparator<URL>() {
		public int compare(URL u1, URL u2) {
			return String.valueOf(u1).compareTo(String.valueOf(u2));
		}
	};

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
				return;
			}

			include(name + dirs[i].getName(), dirs[i], map);
		}
	}

	private void includeJar(File file, Map<URL, String> map) {
		if (file.isDirectory())
			return;

		URL jarURL = null;
		JarFile jar = null;
		try {
			jarURL = new URL("file:/" + file.getCanonicalPath());
			jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
			JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
			jar = conn.getJarFile();
		} catch (Exception e) {
			// not a JAR or disk I/O error
			// either way, just skip
			return;
		}

		if (jar == null || jarURL == null)
			return;

		// include the jar's "default" package (i.e. jar's root)
		map.put(jarURL, "");

		Enumeration<JarEntry> e = jar.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = e.nextElement();

			if (entry.isDirectory()) {
				if (entry.getName().toUpperCase().equals("META-INF/"))
					continue;

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
		if (entry == null)
			return "";
		String s = entry.getName();
		if (s == null)
			return "";
		if (s.length() == 0)
			return s;
		if (s.startsWith("/"))
			s = s.substring(1, s.length());
		if (s.endsWith("/"))
			s = s.substring(0, s.length() - 1);
		return s.replace('/', '.');
	}

	protected Map<String, Set<String>> getSubclasses() {
		return getSubclasses(System.getProperty(PROCESSOR_CLASS_PATH));		
	}
	
	protected Map<String, Set<String>> getSubclasses(String classpath) {

		Map<URL, String> classpathLocations = getClasspathLocations(classpath);
		
		Iterator<URL> it = classpathLocations.keySet().iterator();
		
		Map<String, Set<String>> annotatedClasses = new HashMap<String, Set<String>>();
		
		while (it.hasNext()) {
			URL url = it.next();
			findSubclasses(url, classpathLocations.get(url), annotatedClasses);
		}
		
		return annotatedClasses;
	}

	private void findSubclasses(URL location, String packageName, Map<String, Set<String>> annotatedClasses) {

		// TODO: add getResourceLocations() to this list


		// Get a File object for the package
		File directory = new File(location.getFile());

		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					String classname = files[i].substring(0, files[i].length() - 6);
					addyTypeElement(packageName + "." + classname, processingEnv.getElementUtils().getTypeElement(packageName + "." + classname), annotatedClasses);
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

					if (!entry.isDirectory() && entryname.endsWith(".class")) {
						String classname = entryname.substring(0, entryname.length() - 6);
						if (classname.startsWith("/")) {
							classname = classname.substring(1);
						}
						classname = classname.replace('/', '.');
						addyTypeElement(classname, processingEnv.getElementUtils().getTypeElement(classname), annotatedClasses);
					}
				}
			} catch (Throwable ex) {
			}
		}
	}

	private void addyTypeElement(String canonicalName, TypeElement type, Map<String, Set<String>> annotatedClasses) {
		if (type == null) {
			return;
		}
		List<? extends AnnotationMirror> annotationMirrors = type.getAnnotationMirrors();
		for (AnnotationMirror annotationMirror: annotationMirrors) {
			
			String qualifiedName = ((TypeElement)((DeclaredType)annotationMirror.getAnnotationType()).asElement()).getQualifiedName().toString();
			
			Set<String> types = annotatedClasses.get(qualifiedName);
			if (types == null) {
				types = new HashSet<String>();
				annotatedClasses.put(qualifiedName, types);
			}
			if (!types.contains(canonicalName)) {
				types.add(canonicalName);
			}
		}
	}
	
	@Override
	public Set<? extends Element> getElementsAnnotatedWith(TypeElement a) {
		if (!project.isInitialized()) {
			project.setAnnotatedElementNames(getSubclasses());
		}

		final String rootDirectory = processingEnv.getOptions().get(ROOT_DIRECTORY_OPTION);
		
		if (rootDirectory != null) {
			//TODO read from options
			String classPath = rootDirectory + File.separator + "target" + File.separator + "classes";
			if (File.separator.equals("\\")) {
				classPath = classPath.replace("\\", "/");
			}

			project.addAnnotatedElementNames(getSubclasses(classPath));
		}
		
		Set<String> annotatedClassNames = project.getAnnotatedElementNames(a.getQualifiedName().toString());

		Set<Element> annotatedClassSet = new HashSet<Element>();
		
		if (annotatedClassNames != null) {
			for (String annotatedClassName: annotatedClassNames) {
				TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(annotatedClassName);
				if (typeElement == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find " + annotatedClassName + " in the processing environment!");
				} else {
					annotatedClassSet.add(typeElement);
				}
			}
		}
		
		return annotatedClassSet;
	}

	@Override
	public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a) {
		return getElementsAnnotatedWith(processingEnv.getElementUtils().getTypeElement(a.getCanonicalName()));
	}

	@Override
	public Set<? extends Element> getElementsAnnotatedWith(TypeElement a, RoundEnvironment roundEnv) {
		Set<? extends Element> classpathElements = getElementsAnnotatedWith(a);
		Set<? extends Element> roundElements = roundEnv.getElementsAnnotatedWith(a);

		Set<Element> result = new HashSet<Element>();
		
		if (classpathElements != null) {
			for (Element classPathElement: classpathElements) {
				result.add(classPathElement);
			}
		}

		if (roundElements != null) {
			for (Element roundElement: roundElements) {
				boolean found = false;
				for (Element resultElement: result) {
					if (resultElement.toString().equals(roundElement.toString())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					result.add(roundElement);
				}
			}
		}
		
		return result;
	}

	@Override
	public Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> a, RoundEnvironment roundEnv) {
		return getElementsAnnotatedWith(processingEnv.getElementUtils().getTypeElement(a.getCanonicalName()), roundEnv);
	}
}