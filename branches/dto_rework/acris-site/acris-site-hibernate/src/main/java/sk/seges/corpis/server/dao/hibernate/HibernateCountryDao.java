package sk.seges.corpis.server.dao.hibernate;

import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.server.dao.ICountryDao;
import sk.seges.corpis.server.domain.jpa.JpaCountry;
import sk.seges.corpis.shared.domain.api.CountryData;


public class HibernateCountryDao extends AbstractHibernateCRUD<CountryData<?>> implements ICountryDao<CountryData<?>> {

	public HibernateCountryDao() {
		super(JpaCountry.class);
	}
	
	@Override
	public JpaCountry createDefaultEntity() {
		return new JpaCountry();
	}

}