/**
 * 
 */
package sk.seges.sesam.spring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * @author eldzi
 */
public class RelativeFileSystemResource extends AbstractResource {
    private final File file;

    private final String path;

    public RelativeFileSystemResource(String property, String path) {
    	this(property, path, false);
    }
    
    /**
     * Create a new FileSystemResource.
     * @param path a file path
     */
    public RelativeFileSystemResource(String property, String path, boolean environment) {
        Assert.notNull(path, "Path must not be null");
        this.file = new File(getProperty(property, environment), path);
        this.path = StringUtils.cleanPath(path);
    }

    private File getProperty(String property, boolean environment) {
        String propertyValue;
        if(environment)
        	propertyValue = System.getenv(property);
        else
        	propertyValue = System.getProperty(property);
        
        if(propertyValue == null)
            throw new IllegalStateException("Property (environment = " + environment + " ) " + property + " was not set!");
        return new File(propertyValue);
    }
    
    /**
     * Return the file path for this resource.
     */
    public final String getPath() {
        return path;
    }


    /**
     * This implementation returns whether the underlying file exists.
     * @see java.io.File#exists()
     */
    public boolean exists() {
        return this.file.exists();
    }

    /**
     * This implementation opens a FileInputStream for the underlying file.
     * @see java.io.FileInputStream
     */
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    /**
     * This implementation returns a URL for the underlying file.
     * @see java.io.File#getAbsolutePath()
     */
    public URL getURL() throws IOException {
        return new URL(ResourceUtils.FILE_URL_PREFIX + this.file.getAbsolutePath());
    }

    /**
     * This implementation returns the underlying File reference.
     */
    public File getFile() {
        return file;
    }

    /**
     * This implementation creates a FileSystemResource, applying the given path
     * relative to the path of the underlying file of this resource descriptor.
     * @see org.springframework.util.StringUtils#applyRelativePath(String, String)
     */
    public Resource createRelative(String relativePath) {
        String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
        return new FileSystemResource(pathToUse);
    }

    /**
     * This implementation returns the name of the file.
     * @see java.io.File#getName()
     */
    public String getFilename() {
        return this.file.getName();
    }

    /**
     * This implementation returns a description that includes the absolute
     * path of the file.
     * @see java.io.File#getAbsolutePath()
     */
    public String getDescription() {
        return "file [" + this.file.getAbsolutePath() + "]";
    }


    /**
     * This implementation compares the underlying File references.
     */
    public boolean equals(Object obj) {
        return (obj == this ||
            (obj instanceof RelativeFileSystemResource && this.path.equals(((RelativeFileSystemResource) obj).path)));
    }

    /**
     * This implementation returns the hash code of the underlying File reference.
     */
    public int hashCode() {
        return this.path.hashCode();
    }
}
