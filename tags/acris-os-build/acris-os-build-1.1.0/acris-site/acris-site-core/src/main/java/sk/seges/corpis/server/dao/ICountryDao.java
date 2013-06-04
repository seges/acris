package sk.seges.corpis.server.dao;

import sk.seges.corpis.shared.domain.api.CountryData;
import sk.seges.sesam.dao.ICrudDAO;

public interface ICountryDao<T extends CountryData> extends ICrudDAO<CountryData> {

	CountryData createDefaultEntity();

}