package sk.seges.acris.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.gwtwidgets.server.spring.GWTRPCServiceExporter;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

/**
 * Enhanced GWTRPCServiceExporter's serialization policy method except it
 * doesn't use default serialization policy loading mechanism - not loading from
 * module path URL. Serialization policy file has to be located in a server
 * context path.
 * 
 * {@link ConfigurableRPCServiceExporterFactory} is responsible for
 * instantiating service exporter and providing it with correct serialization
 * policy.
 * 
 * This code is copied from {@link GWTRPCServiceExporter} and slightly modified.
 * 
 * FYI: serialization policy file is created with GWT's
 * ProxyCreator.writeSerializationPolicyFile
 * 
 * Example:
 * 
 * Suppose we have client (at /client) and server (at /server). Using old
 * approach serialization policy file had to be located under /client context
 * using filesystem. {@link LocalContextSerializationPolicy} is loading it from
 * the context path location of the service you are calling. So if the service
 * is under /server context it will find out and use that context instead of
 * module context (in this case /client) it is calling from.
 * 
 * @author eldzi
 */
public class LocalContextSerializationPolicy implements ICustomSerializationPolicy {
	@Override
	public SerializationPolicy doGetSerializationPolicy(ServletContext context,
			HttpServletRequest request, String moduleBaseURL, String strongName) {
	    // The request can tell you the path of the web app relative to the
	    // container root.
	    String contextPath = request.getContextPath();

	    String modulePath = null;
	    if (moduleBaseURL != null) {
	      try {
	        modulePath = new URL(moduleBaseURL).getPath();
	      } catch (MalformedURLException ex) {
	        // log the information, we will default
	        context.log("Malformed moduleBaseURL: " + moduleBaseURL, ex);
	      }
	    }

	    SerializationPolicy serializationPolicy = null;

	    /*
	     * Check that the module path must be in the same web app as the servlet
	     * itself. If you need to implement a scheme different than this, override
	     * this method.
	     */
	    if (modulePath == null) {
	      String message = "ERROR: The module path requested, "
	          + modulePath
	          + ", is not in the same web application as this servlet, "
	          + contextPath
	          + ".  Your module may not be properly configured or your client and server code maybe out of date.";
	      context.log(message);
	    } else {
	    	if(!contextPath.endsWith("/"))
	    		contextPath += "/";
	      String serializationPolicyFilePath = SerializationPolicyLoader.getSerializationPolicyFileName(strongName);

	      // Open the RPC resource file read its contents.
	      InputStream is = context.getResourceAsStream(
	          "/" + serializationPolicyFilePath);
	      try {
	        if (is != null) {
	          try {
	            serializationPolicy = SerializationPolicyLoader.loadFromStream(is, null);
	          } catch (ParseException e) {
	            context.log(
	                "ERROR: Failed to parse the policy file '"
	                    + serializationPolicyFilePath + "'", e);
	          } catch (IOException e) {
	            context.log(
	                "ERROR: Could not read the policy file '"
	                    + serializationPolicyFilePath + "'", e);
	          }
	        } else {
	          String message = "ERROR: The serialization policy file '"
	              + serializationPolicyFilePath
	              + "' was not found; did you forget to include it in this deployment?";
	          context.log(message);
	        }
	      } finally {
	        if (is != null) {
	          try {
	            is.close();
	          } catch (IOException e) {
	            // Ignore this error
	          }
	        }
	      }
	    }

	    return serializationPolicy;
	}
}
