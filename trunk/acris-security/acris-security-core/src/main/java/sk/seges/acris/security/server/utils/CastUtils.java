package sk.seges.acris.security.server.utils;

import java.util.List;
import java.util.Set;

public final class CastUtils {

	@SuppressWarnings("unchecked")
	public static <S> List<S> castList(List<?> list, Class<S> type) {
		return (List<S>) list;
	}

	@SuppressWarnings("unchecked")
	public static <S> Set<S> castSet(Set<?> set, Class<S> type) {
		return (Set<S>) set;
	}
}