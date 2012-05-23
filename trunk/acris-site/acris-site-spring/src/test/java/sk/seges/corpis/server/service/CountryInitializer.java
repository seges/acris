package sk.seges.corpis.server.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sk.seges.corpis.server.dao.ICountryDao;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.service.ICountryService;

public class CountryInitializer {

	private ICountryDao<CountryData> countryDao;

	private ICountryService countryService;
	
	@SuppressWarnings("unchecked")
	public CountryInitializer(ApplicationContext context) {
		this.countryDao = getBean(ICountryDao.class, context);
		this.countryService = getBean(ICountryService.class, context);
	}
	
	private <T> T getBean(Class<T> clazz, ApplicationContext applicationContext) {
		Map<String, T> beansOfType = applicationContext.getBeansOfType(clazz);
		T result = beansOfType.values().iterator().next();
		return result;
	}
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:sk/seges/corpis/server/service/initializeContext.xml"});
		new CountryInitializer(applicationContext).initialize();
	}
	
	public void initialize() {
		
		for (Countries country: Countries.values()) {
			CountryData countryEntity = convert(country);
			if (countryService.findByCountry(country.getCountry()) == null) {
				countryDao.persist(countryEntity);
			}
		}
	}
	
	private CountryData convert(Countries countries) {
		CountryData country = countryDao.createDefaultEntity();
		country.setCountry(countries.getCountry());
		country.setDomain(countries.getDomain());
		country.setEuropeanUnion(countries.isEuropeanUnionCountry());
		country.setLabel(countries.getLabel());
		country.setLanguage(countries.getLang());
		return country;
	}
}