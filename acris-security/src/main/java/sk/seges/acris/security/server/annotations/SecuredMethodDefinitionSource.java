package sk.seges.acris.security.server.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.intercept.method.AbstractFallbackMethodDefinitionSource;

import sk.seges.acris.security.server.acl.RoleRunAsManagerImpl;
import sk.seges.sesam.dao.PagedResult;

public class SecuredMethodDefinitionSource extends AbstractFallbackMethodDefinitionSource {

	protected Class[] annotationClasses = getAnnotationClasses();
	
	protected Class[] getAnnotationClasses() {
		return new Class[] {Secured.class, RunAs.class, AfterAclCheck.class, BeforeAclCheck.class};
	}
	                
	protected ConfigAttributeDefinition findAttributes(Class clazz) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (Class<?> annotationClass : annotationClasses) {
			Annotation annotation = clazz.getAnnotation(annotationClass);
			if (annotation != null)
				annotations.add(annotation);
		}
		
		return processAnnotation(annotations, null, null);
	}

	protected ConfigAttributeDefinition findAttributes(Method method, Class targetClass) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		
		for (Class annotationClass : annotationClasses) {
			Annotation annotation = AnnotationUtils.findAnnotation(method, annotationClass);
			if (annotation != null)
				annotations.add(annotation);
		}

		return processAnnotation(annotations, method.getReturnType(), method.getParameterTypes());
	}
	
    public Collection getConfigAttributeDefinitions() {
        return null;
    }
    
    protected void processSecuredAnnotation(List<String> atributeTokens, Secured secured) {
		if (secured.value() == null) {
			return;
		}

		for (String rolePermission : secured.value()) {
			atributeTokens.add(RoleRunAsManagerImpl.DEFAULT_ROLE_PREFIX + rolePermission);
		}
    }

    protected void processRunAsAnnotation(List<String> atributeTokens, RunAs runAs) {
		atributeTokens.add("RUN_AS_" + runAs.value());
    }

    protected void processBeforeAclCheckAnnotation(List<String> atributeTokens, BeforeAclCheck beforeAclCheck) {
    	if (beforeAclCheck.checkType().equals(ACLCheckType.FIRST_PARAM)) {
    		atributeTokens.add("ACL_OBJECT_" + beforeAclCheck.value().name());
    	} else if (beforeAclCheck.checkType().equals(ACLCheckType.ALL_PARAMS)) {
    		atributeTokens.add("ACL_LIST_OBJECTS_" + beforeAclCheck.value().name());
    	}
    }

    protected void processAfterAclCheckAnnotation(List<String> atributeTokens, AfterAclCheck afterAclCheck, Class<?> returnType, Class<?>[] parameterTypes) {
    	if (returnType == null) {
    		return;
    	}
    	
        if (Collection.class.isAssignableFrom(returnType) || (returnType.isArray()) || PagedResult.class.isAssignableFrom(returnType)) {
    		//if method has DetachedCriteria parameter, then process injection before execution
    		//add specific voter for injecting ACL specific criteria
        	if (parameterTypes != null) {
        		for (Class<?> parameterTypeClass : parameterTypes) {
        			if (parameterTypeClass.isAssignableFrom(DetachedCriteria.class)) {
        	    		atributeTokens.add("AFTER_ACL_INJECT_COLLECTION_READ");
        	    		return;
        			}
        		}
        	}
        	//otherwise we have to check result after method execution 
    		atributeTokens.add("AFTER_ACL_COLLECTION_READ");
        } else if (Object.class.isAssignableFrom(returnType)) {
            //added for the case of a findUniqueResult type method, which takes DetachedCriteria 
            //as the input parameter and returns only one object
            if (parameterTypes != null) {
                for (Class<?> parameterTypeClass : parameterTypes) {
                    if (parameterTypeClass.isAssignableFrom(DetachedCriteria.class)) {
                        atributeTokens.add("AFTER_ACL_INJECT_COLLECTION_READ"); //TODO - think of a better name for the parameter
                        return;
                    }
                }
            }
        	atributeTokens.add("AFTER_ACL_READ");
        }
    }

	protected ConfigAttributeDefinition processAnnotation(List<Annotation> annotations, Class returnType, Class<?>[] parameterTypes) {
		List<String> atributeTokens = new ArrayList<String>();
		
		for (Annotation annotation : annotations) {
			if (annotation instanceof Secured) {
				processSecuredAnnotation(atributeTokens, (Secured)annotation);
			} else if (annotation instanceof RunAs) {
				processRunAsAnnotation(atributeTokens, (RunAs)annotation);
			} else if (annotation instanceof AfterAclCheck) {
				processAfterAclCheckAnnotation(atributeTokens, (AfterAclCheck)annotation, returnType, parameterTypes);
			} else if (annotation instanceof BeforeAclCheck) {
				processBeforeAclCheckAnnotation(atributeTokens, (BeforeAclCheck)annotation);
			}
		}

		if (atributeTokens.size() == 0) {
			return new ConfigAttributeDefinition(Collections.EMPTY_LIST);
		}
		
		return new ConfigAttributeDefinition(atributeTokens.toArray(new String[atributeTokens.size()]));
	}
}
