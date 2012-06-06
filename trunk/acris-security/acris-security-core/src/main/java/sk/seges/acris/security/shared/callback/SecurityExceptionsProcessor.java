package sk.seges.acris.security.shared.callback;

import sk.seges.acris.security.shared.exception.AccessDeniedException;
import sk.seges.acris.security.shared.exception.AuthenticationException;
import sk.seges.acris.security.shared.exception.SecurityException;

public class SecurityExceptionsProcessor {

	public static SecurityException convertToSecurityException(final Throwable exception) {
		if (exception != null) {
			if (exception instanceof SecurityException) {
				return (SecurityException)exception;
			}
			if (exception instanceof AccessDeniedException) {
				return (AccessDeniedException)exception;
			}
			if (exception instanceof AuthenticationException) {
				return (AuthenticationException)exception;
			}
		}

		return null;
	}
}