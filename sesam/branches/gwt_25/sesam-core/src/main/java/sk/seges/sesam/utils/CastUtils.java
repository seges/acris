package sk.seges.sesam.utils;

import java.util.List;
import java.util.Set;

public final class CastUtils {

	@SuppressWarnings("unchecked")
	public static <S> List<S> cast(List<?> list, Class<S> type) {
		return (List<S>) list;
	}

	@SuppressWarnings("unchecked")
	public static <S> S cast(Object object, Class<S> type) {
		return (S) object;
	}

	@SuppressWarnings("unchecked")
	public static <S> Set<S> cast(Set<?> set, Class<S> type) {
		return (Set<S>) set;
	}
}