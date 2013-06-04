package sk.seges.corpis.server.configuration;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import sk.seges.corpis.server.dao.ICountryDao;
import sk.seges.corpis.server.dao.hibernate.HibernateCountryDao;
import sk.seges.corpis.server.dao.hibernate.spring.SpringHibernateCountryDao;
import sk.seges.corpis.server.domain.server.model.data.CountryData;

public class SiteConfiguration {

	@Autowired
	private EntityManager entityManager;
	
	@Bean
	public ICountryDao<? extends CountryData> countryDao() {
		HibernateCountryDao countryDao = new SpringHibernateCountryDao();
		countryDao.setEntityManager(entityManager);
		return countryDao;
	}
}