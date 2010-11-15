/**
 * 
 */
package sk.seges.acris.reporting.server.service;

import java.io.File;

/**
 * @author ladislav.gazo
 */
public interface ConfigurationProvider {
	File resolveRootDirectoryPath(String webId);
	String getJasperServerUrl();
	String getJasperServerUser();
	String getJasperServerPassword();
}
