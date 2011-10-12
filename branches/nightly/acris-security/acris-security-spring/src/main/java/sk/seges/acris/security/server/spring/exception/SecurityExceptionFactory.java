package sk.seges.acris.security.server.spring.exception;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.SpringSecurityException;

import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.exception.ServerException;

public class SecurityExceptionFactory {

	public static ServerException get(final SpringSecurityException springException) {
		SecurityException gwtException = null;
		if (springException instanceof AccessDeniedException) {
			gwtException = new sk.seges.acris.security.shared.exception.AccessDeniedException(springException.getMessage(), springException.getCause());
		} else if (springException instanceof AuthenticationException) {
			gwtException = new sk.seges.acris.security.shared.exception.AuthenticationException(springException.getMessage(), springException.getCause());
		} else {
			gwtException = new sk.seges.acris.security.shared.exception.SecurityException(springException.getMessage(), springException.getCause());
		}
		return new ServerException(gwtException);
	}
}