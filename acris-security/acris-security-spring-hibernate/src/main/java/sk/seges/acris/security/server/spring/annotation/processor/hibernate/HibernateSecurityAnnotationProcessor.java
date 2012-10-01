package sk.seges.acris.security.server.spring.annotation.processor.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.security.server.core.annotation.PostExecutionAclEvaluation;
import sk.seges.acris.security.server.spring.annotation.processor.DefaultSecurityAnnotationProcessor;
import sk.seges.sesam.dao.PagedResult;


public class HibernateSecurityAnnotationProcessor extends DefaultSecurityAnnotationProcessor {

	protected void handlePostExecutionAclEvaluationAnnotation(List<String> atributeTokens, PostExecutionAclEvaluation postExecutionAclEvaluationInstance,
			Class<?> returnType, Class<?>[] parameterTypes) {
		if (returnType == null) {
			return;
		}

		if (Collection.class.isAssignableFrom(returnType) || (returnType.isArray()) || PagedResult.class.isAssignableFrom(returnType)) {
			//if method has DetachedCriteria parameter, then process injection before execution
			//add specific voter for injecting ACL specific criteria
			if (parameterTypes != null) {
				for (Class<?> parameterTypeClass : parameterTypes) {
					if (parameterTypeClass.isAssignableFrom(DetachedCriteria.class)) {
						atributeTokens.add(AFTER_ACL_INJECT_COLLECTION_READ_TOKEN);
						return;
					}
				}
			}
			//otherwise we have to check result after method execution 
			atributeTokens.add(AFTER_ACL_COLLECTION_READ_TOKEN);
		} else if (Object.class.isAssignableFrom(returnType)) {
			//added for the case of a findUniqueResult type method, which takes DetachedCriteria 
			//as the input parameter and returns only one object
			if (parameterTypes != null) {
				for (Class<?> parameterTypeClass : parameterTypes) {
					if (parameterTypeClass.isAssignableFrom(DetachedCriteria.class)) {
						atributeTokens.add(AFTER_ACL_READ_TOKEN);
						return;
					}
				}
			}
			atributeTokens.add(AFTER_ACL_READ_TOKEN);
		}
	}

}
