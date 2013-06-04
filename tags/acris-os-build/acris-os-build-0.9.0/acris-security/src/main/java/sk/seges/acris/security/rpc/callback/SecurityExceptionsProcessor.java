package sk.seges.acris.security.rpc.callback;

import sk.seges.acris.security.rpc.exception.AccessDeniedException;
import sk.seges.acris.security.rpc.exception.SecurityException;
import sk.seges.acris.security.rpc.exception.AuthenticationException;

public class SecurityExceptionsProcessor {
	public static SecurityException convertToSecurityException(
			final Throwable exception) {
		if (exception != null && exception.getMessage() != null) {
			String exceptionMessage = exception.getMessage();
			if (exceptionMessage.contains(SecurityException.class
					.getName())) {
				return new SecurityException(exception.getMessage()
						.replaceAll(
								SecurityException.class.getName(),
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