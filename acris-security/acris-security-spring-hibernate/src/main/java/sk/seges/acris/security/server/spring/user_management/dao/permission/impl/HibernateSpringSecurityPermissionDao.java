package sk.seges.acris.security.server.spring.user_management.dao.permission.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import sk.seges.acris.security.server.user_management.dao.permission.impl.HibernateSecurityPermissionDao;

@Component
@Qualifier(value = "SecurityPermissionDao")
public class HibernateSpringSecurityPermissionDao extends HibernateSecurityPermissionDao {}
