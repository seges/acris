package sk.seges.acris.security.server.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.acris.security.rpc.domain.Permission;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeAclCheck {
	  public Permission value();
	  public BeforeAclCheckMode checkType() default BeforeAclCheckMode.FIRST_PARAMETER;
}