/**
 * 
 */
package sk.seges.acris.rpc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * @author eldzi
 */
public interface ICustomSerializationPolicy {
	SerializationPolicy doGetSerializationPolicy(ServletContext context, HttpServletRequest request, String moduleBaseURL, String strongName);
}
