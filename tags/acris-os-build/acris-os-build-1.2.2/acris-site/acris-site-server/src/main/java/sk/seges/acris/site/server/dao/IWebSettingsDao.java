package sk.seges.acris.site.server.dao;

import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;
import sk.seges.sesam.dao.ICrudDAO;


public interface IWebSettingsDao<T extends WebSettingsData> extends ICrudDAO<T> {

	WebSettingsData createDefaultEntity();

}