package sk.seges.acris.security.server.spring.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.intercept.method.AbstractFallbackMethodDefinitionSource;

import sk.seges.acris.security.server.core.annotation.EPreExecutionAclEvaluationMode;
import sk.seges.acris.security.server.core.annotation.PostExecutionAclEvaluation;
import sk.seges.acris.security.server.core.annotation.PreExecutionAclEvaluation;
import sk.seges.acris.security.server.core.annotation.RunAs;
import sk.seges.acris.security.server.spring.runas.RoleRunAsManagerImpl;
import sk.seges.sesam.dao.PagedResult;

public class DefaultSecurityAnnotationProcessor extends AbstractFallbackMethodDefinitionSource {

	protected static final String AFTER_ACL_READ_TOKEN = "AFTER_ACL_READ";
	protected static final String AFTER_ACL_COLLECTION_READ_TOKEN = "AFTER_ACL_COLLECTION_READ";
	protected static final String AFTER_ACL_INJECT_COLLECTION_READ_TOKEN = "AFTER_ACL_INJECT_COLLECTION_READ";
	protected static final String ACL_LIST_OBJECTS_PREFIX = "ACL_LIST_OBJECTS_";
	protected static final String RUN_AS_PREFIX = "RUN_AS_";
	protected static final String ACL_OBJECT_PREFIX = "ACL_OBJECT_";

	protected Class<?>[] annotationClasses = getAnnotationClasses();

	protected Class<?>[] getAnnotationClasses() {
		return new Class<?>[] {RolesAllowed.class, RunAs.class, PostExecutionAclEvaluation.class, PreExecutionAclEvaluation.class};
	}

	protected ConfigAttributeDefinition findAttributes(Class clazz) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (Class<?> annotationClass : annotationClasses) {
			Annotation annotation = clazz.getAnnotation(annotationClass);
			if (annotation != null) annotations.add(annotation);
		}

		return handleAnnotation(annotations, null, null);
	}

	protected ConfigAttributeDefinition findAttributes(Method method, Class targetClass) {
		List<Annotation> annotations = new ArrayList<Annotation>();

		for (Class annotationClass : annotationClasses) {
			Annotation annotation = AnnotationUtils.findAnnotation(method, annotationClass);
			if (annotation != null) annotations.add(annotation);
		}

		return handleAnnotation(annotations, method.getReturnType(), method.getParameterTypes());
	}

	public Collection getConfigAttributeDefinitions() {
		return new ArrayList();
	}

	protected void handleRolesAllowedAnnotation(List<String> atributeTokens, RolesAllowed rolesAllowed) {
		if (rolesAllowed.value() == null) {
			return;
		}

		for (String allowedRole : rolesAllowed.value()) {
			atributeTokens.add(RoleRunAsManagerImpl.ROLE_PREFIX + allowedRole);
		}
	}

	protected void handleRunAsAnnotation(List<String> atributeTokens, RunAs runAs) {
		atributeTokens.add(RUN_AS_PREFIX + runAs.value());
	}

	protected void handlePreExecutionAclEvaluationAnnotation(List<String> atributeTokens, PreExecutionAclEvaluation preExecutionAclEvaluationInstance) {
		if (preExecutionAclEvaluationInstance.checkType().equals(EPreExecutionAclEvaluationMode.EVALUATE_FIRST_ANNOTATED_PARAMETER)) {
			atributeTokens.add(ACL_OBJECT_PREFIX + preExecutionAclEvaluationInstance.value().name());
		} else if (preExecutionAclEvaluationInstance.checkType().equals(EPreExecutionAclEvaluationMode.EVALUATE_ALL_ANNOTATED_PARAMETERS)) {
			atributeTokens.add(ACL_LIST_OBJECTS_PREFIX + preExecutionAclEvaluationInstance.value().name());
		}
	}

	protected void handlePostExecutionAclEvaluationAnnotation(List<String> atributeTokens, PostExecutionAclEvaluation postExecutionAclEvaluationInstance,
			Class<?> returnType, Class<?>[] parameterTypes) {
		if (returnType == null) {
			return;
		}

		if (Collection.class.isAssignableFrom(returnType) || (returnType.isArray()) || PagedResult.class.isAssignableFrom(returnType)) {
			//otherwise we have to check result after method execution 
			atributeTokens.add(AFTER_ACL_COLLECTION_READ_TOKEN);
		} else if (Object.class.isAssignableFrom(returnType)) {
			atributeTokens.add(AFTER_ACL_READ_TOKEN);
		}
	}

	protected ConfigAttributeDefinition handleAnnotation(List<Annotation> annotations, Class returnType, Class<?>[] parameterTypes) {
		List<String> tokenList = new ArrayList<String>();

		for (Annotation annotation : annotations) {
			if (annotation instanceof RolesAllowed) {
				handleRolesAllowedAnnotation(tokenList, (RolesAllowed) annotation);
			} else if (annotation instanceof RunAs) {
				handleRunAsAnnotation(tokenList, (RunAs) annotation);
			} else if (annotation instanceof PostExecutionAclEvaluation) {
				handlePostExecutionAclEvaluationAnnotation(tokenList, (PostExecutionAclEvaluation) annotation, returnType, parameterTypes);
			} else if (annotation instanceof PreExecutionAclEvaluation) {
				handlePreExecutionAclEvaluationAnnotation(tokenList, (PreExecutionAclEvaluation) annotation);
			}
		}

		if (tokenList.size() == 0) {
			return new ConfigAttributeDefinition(Collections.EMPTY_LIST);
		}

		return new ConfigAttributeDefinition(tokenList.toArray(new String[tokenList.size()]));
	}
}
