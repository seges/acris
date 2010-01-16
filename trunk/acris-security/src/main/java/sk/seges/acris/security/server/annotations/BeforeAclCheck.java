package sk.seges.acris.security.server.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.acris.security.rpc.domain.Permission;

@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface BeforeAclCheck {
	  public abstract Permission value();
	  public ACLCheckType checkType() default ACLCheckType.FIRST_PARAM;
}