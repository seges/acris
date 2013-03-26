package sk.seges.corpis.server.dao.hibernate.spring;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.server.dao.hibernate.HibernateCountryDao;

@Transactional
public class SpringHibernateCountryDao extends HibernateCountryDao {
}