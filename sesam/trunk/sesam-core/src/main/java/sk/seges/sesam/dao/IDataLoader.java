package sk.seges.sesam.dao;


public interface IDataLoader<T> {
	PagedResult<T> load(Page page);
}
