package sk.seges.corpis.shared.service;

import sk.seges.corpis.server.domain.server.model.data.CountryData;



public interface ICountryService {

	CountryData findByCountry(String country);
	
	CountryData findDefaultCountry();

}