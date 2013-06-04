package sk.seges.sesam.spring.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class AdvancedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    
    private static final Logger logger = Logger.getLogger(AdvancedPropertyPlaceholderConfigurer.class);

    private List<Resource> locations = new ArrayList<Resource>();
    private String localConfigFilePropertyName = "localConfig";

    public AdvancedPropertyPlaceholderConfigurer() {
    }

    public AdvancedPropertyPlaceholderConfigurer(String localConfigFilePropertyName) {
        this.localConfigFilePropertyName = localConfigFilePropertyName;
    }

    @Override
    public void setLocation(Resource location) {
        super.setLocation(analyzeLocation(location));
    }

    @Override
    public void setLocations(Resource[] locations) {
        for (Resource location : locations) {
            this.locations.add(analyzeLocation(location));
        }
        super.setLocations(this.locations.toArray(new Resource[this.locations.size()]));
    }

    private Resource analyzeLocation(Resource location) {
        Properties properties = new Properties();
        try {
            properties.load(location.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize PropertyPlaceholderConfigurer. Can't read location: "
                    + location, e);
        }
        String localConfigFilePath = properties.getProperty(localConfigFilePropertyName);
        if (null == localConfigFilePath) {
            logger.info("Location " + location
                    + " does not contain reference to a local configuration properties file specified with the "
                    + localConfigFilePropertyName + " property. Using as a default location");
            return location;
        }

        properties.remove(localConfigFilePropertyName);

        File localPropertiesFile = new File(System.getProperty("user.home"), localConfigFilePath);

        Resource result = null;
        if (!localPropertiesFile.exists()) {
            result = storeProperties(location, properties, localPropertiesFile);
        } else {
            result = mergeLocalAndDefaultProperties(location, properties, localPropertiesFile);
        }

        try {
            location.getInputStream().close();
        } catch (Exception e) {
            logger.error("Failed to close InputStream of location: " + location);
        }
        
        return result;
    }

    private Resource storeProperties(Resource location, Properties properties, File destinationFile) {
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }

        boolean saved = sortAndSaveProperties(location, properties, destinationFile);
        if(!saved) {
            return location;
        }
        return new FileSystemResource(destinationFile);
    }

    private Resource mergeLocalAndDefaultProperties(Resource location, Properties properties, File localPropertiesFile) {
        boolean mergeNeeded = false;
        Properties localProperties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(localPropertiesFile);
            localProperties.load(fis);
        } catch (IOException e) {
            logger.error("Failed to load local properties for properties file:" + location.getFilename()
                    + ". Using available classpath properties");
            return location;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e2) {
                    logger.error("Failed to close FileInputStream for file: " + localPropertiesFile);

                }
            }
        }
        for (String propertyName : properties.stringPropertyNames()) {
            if (localProperties.getProperty(propertyName) == null) {
                localProperties.setProperty(propertyName, properties.getProperty(propertyName));
                mergeNeeded = true;
            }
        }

        if (mergeNeeded) {
            boolean saved = sortAndSaveProperties(location, localProperties, localPropertiesFile);
            if(!saved) {
                return location;
            }
        }
        return new FileSystemResource(localPropertiesFile);
    }
    
    private boolean sortAndSaveProperties(Resource location, Properties properties, File destinationFile) {
        BufferedWriter fileWriter = null;
        try {
            StringWriter writer = new StringWriter();
            properties.store(writer, null);
            BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
            List<String> lines = new ArrayList<String>();
            String line = null;
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
            Collections.sort(lines);
            fileWriter = new BufferedWriter(new FileWriter(destinationFile));
            for(String s : lines) {
                fileWriter.append(s);
                fileWriter.newLine();
            }
            
        } catch (IOException e) {
            logger.error("Failed to store local properties for properties file:" + location.getFilename()
                    + ". Using available classpath properties");
            return false;
        } finally {
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (Exception e2) {
                    logger.error("Failed to close FileWriter to local properties file: "
                            + location.getFilename());
                }
            }
        }

        return true;
    }
}
