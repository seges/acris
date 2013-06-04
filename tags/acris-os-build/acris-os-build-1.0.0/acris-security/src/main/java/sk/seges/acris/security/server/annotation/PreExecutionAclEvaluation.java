package sk.seges.acris.security.server.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.acris.security.rpc.user_management.domain.Permission;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface PreExecutionAclEvaluation {
	  public Permission value();
	  public EPreExecutionAclEvaluationMode checkType() default EPreExecutionAclEvaluationMode.EVALUATE_FIRST_ANNOTATED_PARAMETER;
}