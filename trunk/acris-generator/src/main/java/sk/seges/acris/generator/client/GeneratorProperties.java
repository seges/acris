package sk.seges.acris.generator.client;

import sk.seges.acris.client.callback.TrackingAsyncCallback;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Properties for specific generator run.
 * @author fat
 */
public class GeneratorProperties {

	/**
	 * Name of the domain, e.g. www.seges.sk
	 * localhost URLs will be replaced by this domains
	 */
	private String virtualServerName;
	
	/**
	 * Server port, by default 80 make sense
	 * internal port (chosen by GWT for test purposes) will be replaced by this port
	 */
	private Integer virtualServerPort;
	
	/**
	 * Server protocol, by default HTTP
	 * protocol (HTTP) used by GWT test case will be replaced by this protocol
	 */
	private String virtualServerProtocol;
	
	/**
	 * Support for specific domains for individual language translations,
	 * e.g. sk language translation can be represented by .sk domain,
	 *      en language translation can be represented by .com domain,
	 *      de language translation can be represented by .eu domain,
	 */
	private Boolean localeSensitiveServer;

	public String getVirtualServerName() {
		return virtualServerName;
	}

	public void setVirtualServerName(String virtualServerName) {
		this.virtualServerName = virtualServerName;
	}

	public Integer getVirtualServerPort() {
		return virtualServerPort;
	}

	public void setVirtualServerPort(Integer virtualServerPort) {
		this.virtualServerPort = virtualServerPort;
	}

	public String getVirtualServerProtocol() {
		return virtualServerProtocol;
	}

	public void setVirtualServerProtocol(String virtualServerProtocol) {
		this.virtualServerProtocol = virtualServerProtocol;
	}

	public Boolean isLocaleSensitiveServer() {
		return localeSensitiveServer;
	}

	public void setLocaleSensitiveServer(Boolean localeSensitiveServer) {
		this.localeSensitiveServer = localeSensitiveServer;
	}
	
	private IGeneratorServiceAsync generatorService;
	
	public GeneratorProperties(IGeneratorServiceAsync generatorService) {
		this.generatorService = generatorService;
	}
	
 	private void handleResult(ValueWrapper count, final AsyncCallback<Void> callback) {
 		count.value++;
		if (count.value ==4) {
			callback.onSuccess(null);
		}
	}

	public void load(final AsyncCallback<Void> callback) {
		final ValueWrapper count = new ValueWrapper();

		generatorService.getVirtualServerName(new AsyncCallback<String>() {
			public void onFailure(Throwable cause) {
				GWT.log("Unable to obtain virtual server name", cause);
				callback.onFailure(cause);
			}

			public void onSuccess(String name) {
				setVirtualServerName(name);
				handleResult(count, callback);
			}
		});

		generatorService.getVirtualServerPort(new TrackingAsyncCallback<Integer>() {
			public void onFailureCallback(Throwable cause) {
				GWT.log("Unable to obtain virtual server port", cause);
				callback.onFailure(cause);
			}

			public void onSuccessCallback(Integer port) {
				setVirtualServerPort(port);
				handleResult(count, callback);
			}
		});

		generatorService.getVirtualServerProtocol(new AsyncCallback<String>() {
			public void onFailure(Throwable cause) {
				GWT.log("Unable to obtain virtual server protocol", cause);
				callback.onFailure(cause);
			}

			public void onSuccess(String protocol) {
				setVirtualServerProtocol(protocol);
				handleResult(count, callback);
			}
		});

		generatorService.isLocaleSensitiveServer(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable cause) {
				GWT.log("Unable to obtain settings for locale server", cause);
				callback.onFailure(cause);
			}

			public void onSuccess(Boolean isLocaleSensitive) {
				setLocaleSensitiveServer(isLocaleSensitive);
				handleResult(count, callback);
			}
		});
	}
}