package sk.seges.corpis.core.server.service.api;

public interface Finder<T> {
	boolean accept(T t);
}