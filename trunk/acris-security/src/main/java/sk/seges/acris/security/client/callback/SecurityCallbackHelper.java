package sk.seges.acris.security.client.callback;

import sk.seges.acris.security.rpc.exception.AccessDeniedException;
import sk.seges.acris.security.rpc.exception.ApplicationSecurityException;
import sk.seges.acris.security.rpc.exception.AuthenticationException;

public class SecurityCallbackHelper {
	public static ApplicationSecurityException convertToSecurityException(
			final Throwable exception) {
		if (exception != null && exception.getMessage() != null) {
			String exceptionMessage = exception.getMessage();
			if (exceptionMessage.contains(ApplicationSecurityException.class
					.getName())) {
				return new ApplicationSecurityException(exception.getMessage()
						.replaceAll(
								ApplicationSecurityException.class.getName(),
								"").trim());
			}
			if (exceptionMessage
					.contains(AccessDeniedException.class.getName())) {
				return new AccessDeniedException(exception.getMessage()
						.replaceAll(AccessDeniedException.class.getName(), "")
						.trim());
			}
			if (exceptionMessage.contains(AuthenticationException.class
					.getName())) {
				return new AuthenticationException(
						exception.getMessage().replaceAll(
								AuthenticationException.class.getName(), "")
								.trim());
			}
		}

		return null;
	}
}