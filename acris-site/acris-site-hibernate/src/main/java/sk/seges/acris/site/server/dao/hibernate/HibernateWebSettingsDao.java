package sk.seges.acris.site.server.dao.hibernate;

import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;


public class HibernateWebSettingsDao extends AbstractHibernateCRUD<WebSettingsData> implements IWebSettingsDao<WebSettingsData>{

	public HibernateWebSettingsDao() {
		super(JpaWebSettings.class);
	}

	@Override
	public JpaWebSettings createDefaultEntity() {
		return new JpaWebSettings();
	}
	
}