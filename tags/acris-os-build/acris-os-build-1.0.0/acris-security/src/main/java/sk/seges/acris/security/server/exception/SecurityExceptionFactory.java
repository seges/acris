package sk.seges.acris.security.server.exception;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.SpringSecurityException;

import sk.seges.acris.security.rpc.exception.SecurityException;
import sk.seges.acris.security.rpc.exception.ServerException;

public class SecurityExceptionFactory {

	public static ServerException get(
			final SpringSecurityException springException) {
		SecurityException gwtException = null;
		if (springException instanceof AccessDeniedException) {
			gwtException = new sk.seges.acris.security.rpc.exception.AccessDeniedException(
					springException.getMessage(), springException.getCause());
		} else if (springException instanceof AuthenticationException) {
			gwtException = new sk.seges.acris.security.rpc.exception.AuthenticationException(
					springException.getMessage(), springException.getCause());
		} else {
			gwtException = new sk.seges.acris.security.rpc.exception.SecurityException(
					springException.getMessage(), springException.getCause());
		}
		return new ServerException(gwtException);
	}
}