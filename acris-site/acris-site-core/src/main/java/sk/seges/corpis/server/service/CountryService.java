package sk.seges.corpis.server.service;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.server.dao.ICountryDao;
import sk.seges.corpis.shared.domain.api.CountryData;
import sk.seges.corpis.shared.service.ICountryService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

public class CountryService implements ICountryService {

	private ICountryDao<? extends CountryData<?>> countryDao;

	private static final String DEFAULT_COUNTRY = "us";

	public CountryService(ICountryDao<? extends CountryData<?>> countryDao) {
		this.countryDao = countryDao;
	}

	@Override
	public CountryData<?> findByCountry(String country) {
		Page page = new Page(0, 1);
		
		// TODO: switch to @BeanWrapper
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(CountryData.COUNTRY);
		eq.setValue(country);
		page.setFilterable(eq);
		List<CountryData<?>> result = countryDao.findAll(page).getResult();
		if (result.size() == 0) {
			return null;
		}
		return result.get(0);
	}
	
	@Override
	public CountryData<?> findDefaultCountry() {
		return findByCountry(DEFAULT_COUNTRY);
	}
}