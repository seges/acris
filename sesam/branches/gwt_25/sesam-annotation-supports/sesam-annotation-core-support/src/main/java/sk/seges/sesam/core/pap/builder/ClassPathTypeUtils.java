package sk.seges.sesam.core.pap.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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

import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.processor.ConfigurableAnnotationProcessor;

public class ClassPathTypeUtils extends ClassPathFinder implements ClassPathTypes {

	static class Project {
		
		private static Set<Project> projects = new HashSet<Project>();

		private String projectName;
		private Map<String, Set<String>> annotatedElements;
		private boolean targetChanged = true;
		
		private Project(String projectName) {
			this.projectName = projectName;
		}
		
		public boolean isTargetChanged() {
			return targetChanged;
		}
		
		public void setTargetChanged(boolean targetChanged) {
			this.targetChanged = targetChanged;
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
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Project other = (Project) obj;
			if (projectName == null) {
				if (other.projectName != null) {
					return false;
				}
			} else if (!projectName.equals(other.projectName)) {
				return false;
			}
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
	
	private Project project;
	
	public ClassPathTypeUtils(ProcessingEnvironment processingEnv, String projectName, String packageName) {
		super(processingEnv, packageName);
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
        
        try {
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return result;
	}

	protected static final String ROOT_DIRECTORY_OPTION = "rootDirectory";
	protected static final String M2_REPO_OPTION = "M2_REPO";
	private static final String M2_REPO = "M2_REPO"; //variable in the factory path
	private static final String FACTORY_PATH_FILE_NAME = ".factorypath";

	
	private String getClasspathFromList(String classPath) {
		if (classPath != null) {
			if (classPath.startsWith("[")) {
				classPath = classPath.substring(1, classPath.length() - 2);
				classPath = classPath.replaceAll(",", System.getProperty(PATH_SEPARATOR));
			}
		}
		
		return classPath;
	}
	
	private void initializeSystemProperties() {
		
		String classPath = processingEnv.getOptions().get(ConfigurableAnnotationProcessor.CLASSPATH_OPTION);
		
		if (classPath != null) {
			System.setProperty(PROCESSOR_CLASS_PATH, getClasspathFromList(classPath));
		}

		String testClassPath = processingEnv.getOptions().get(ConfigurableAnnotationProcessor.TEST_CLASSPATH_OPTION);

		if (testClassPath != null) {
			if (classPath != null) {
				System.setProperty(PROCESSOR_CLASS_PATH, System.getProperty(PROCESSOR_CLASS_PATH) + System.getProperty(PATH_SEPARATOR) + getClasspathFromList(testClassPath));
			} else {
				System.setProperty(PROCESSOR_CLASS_PATH, getClasspathFromList(testClassPath));
			}
		}

		processingEnv.getMessager().printMessage(Kind.WARNING, "No classpath is defined! Check your eclipse or maven settings.");
		
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
					JAXBElement<FactoryPath> element = unmarshaller.unmarshal(new StreamSource(new StringReader(readContent(rootDirectoryFile))), FactoryPath.class);
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

	class ClassFileTypeHandler implements FileTypeHandler {
		
		@Override
		public String getExtension() {
			return ".class";
		}

		@Override
		public void handleFile(InputStreamProvider inputStreamProvider, String canonicalName, ProcessingEnvironment processingEnv, Map<String, Set<String>> annotatedClasses) {
			addyTypeElement(canonicalName, processingEnv.getElementUtils().getTypeElement(canonicalName), annotatedClasses);
		}

		@Override
		public boolean continueProcessing() {
			return true;
		}

		@Override
		public boolean isFileOfType(String canonicalName) {
			return true;
		}
	}
	
	protected Map<String, Set<String>> getSubclasses() {
		return getSubclasses(System.getProperty(PROCESSOR_CLASS_PATH), new ClassFileTypeHandler());
	}

	private static void addyTypeElement(String canonicalName, TypeElement type, Map<String, Set<String>> annotatedClasses) {
		if (type == null) {
			return;
		}

		List<? extends AnnotationMirror> annotationMirrors = type.getAnnotationMirrors();
		for (AnnotationMirror annotationMirror: annotationMirrors) {
			
			String qualifiedName = ((TypeElement)annotationMirror.getAnnotationType().asElement()).getQualifiedName().toString();
			
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
		
		if (project.isTargetChanged()) {
			final String rootDirectory = processingEnv.getOptions().get(ROOT_DIRECTORY_OPTION);
			
			if (rootDirectory != null) {
				//TODO read from options
				long start = new Date().getTime();
				String classPath = rootDirectory + File.separator + "target" + File.separator + "classes";
				classPath += System.getProperty(PATH_SEPARATOR) + rootDirectory + File.separator + "target" + File.separator + "test-classes";
				if (File.separator.equals("\\")) {
					classPath = classPath.replace("\\", "/");
				}
	
				project.addAnnotatedElementNames(getSubclasses(classPath, new ClassFileTypeHandler()));
				long totalTime = new Date().getTime() - start;
				processingEnv.getMessager().printMessage(Kind.NOTE, "[1]Processing classpath classes tooks " + totalTime / 1000 + "s, " + totalTime % 1000 + " ms.");
			}
			
			project.setTargetChanged(false);
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
	public void setTargetChanged() {
		project.setTargetChanged(true);
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