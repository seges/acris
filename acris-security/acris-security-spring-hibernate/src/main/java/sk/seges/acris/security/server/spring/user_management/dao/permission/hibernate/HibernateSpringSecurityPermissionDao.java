package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.HibernateSecurityPermissionDao;

@Component
@Qualifier(value = "SecurityPermissionDao")
public class HibernateSpringSecurityPermissionDao extends HibernateSecurityPermissionDao {}
