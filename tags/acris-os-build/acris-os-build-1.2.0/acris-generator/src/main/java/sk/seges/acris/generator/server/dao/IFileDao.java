package sk.seges.acris.generator.server.dao;

import sk.seges.acris.generator.server.domain.api.FileData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.IEntityInstancer;


public interface IFileDao<T extends FileData> extends ICrudDAO<T>, IEntityInstancer<T> {
}