package sk.seges.acris.security.client.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.acris.security.rpc.user_management.domain.Permission;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RUNTIME)
public @interface Secured {
	public static final String DEFAULT_ROLE_PREFIX = "ROLE_"; 
	public String[] value() default "";
	public Permission permission() default Permission.EMPTY;
}