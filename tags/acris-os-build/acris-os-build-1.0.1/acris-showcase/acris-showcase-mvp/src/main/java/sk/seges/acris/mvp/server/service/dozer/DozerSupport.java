package sk.seges.acris.mvp.server.service.dozer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dozer.Mapper;

import sk.seges.acris.security.server.user_management.domain.twig.TwigGenericUser;
import sk.seges.acris.security.server.user_management.domain.twig.TwigUserPreferences;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;
import sk.seges.acris.security.shared.user_management.domain.dto.UserPreferencesDTO;
import sk.seges.sesam.dao.PagedResult;


public class DozerSupport {

	private Map<Class<?>, Class<?>> mapping = new HashMap<Class<?>, Class<?>>();

	private Mapper mapper;
	
	public DozerSupport(Mapper mapper) {
		addMapping(GenericUserDTO.class, TwigGenericUser.class);
		addMapping(UserPreferencesDTO.class, TwigUserPreferences.class);
		this.mapper = mapper;
	}

	public void addMapping(Class<?> sourceClass, Class<?> destinationClass, boolean bidirectional) {
		mapping.put(sourceClass, destinationClass);
		if (bidirectional) {
			mapping.put(destinationClass, sourceClass);
		}
	}
	
	public void addMapping(Class<?> sourceClass, Class<?> destinationClass) {
		addMapping(sourceClass, destinationClass, true);
	}

	public <T, S> List<T> convert(List<S> s) {
		if (s == null) {
			return null;
		}
		
		List<T> result = new ArrayList<T>();
		
		for (S element: s) {
			T t = (T)convert(element);
			result.add(t);
		}
		
		return result;
	}

	public <T, S> PagedResult<T> convert(PagedResult<S> s) {
		PagedResult<T> result = new PagedResult<T>();
		result.setResult((T)convert(s.getResult()));
		result.setPage(s.getPage());
		result.setTotalResultCount(s.getTotalResultCount());
		return result;
	}

	public <T, S> Set<T> convert(Set<S> s) {
		if (s == null) {
			return null;
		}
		
		Set<T> result = new HashSet<T>();
		
		for (S element: s) {
			T t = (T)convert(element);
			result.add(t);
		}
		
		return result;
	}

	public <T, S> T convert(S s) {
		if (s == null) {
			return null;
		}
		if (s instanceof List) {
			return (T)convert((List<?>)s);
		}
		
		if (s instanceof Set) {
			return (T)convert((Set<?>)s);
		}
		Class<?> targetClass = mapping.get(s.getClass());
		
		if (targetClass == null) {
			return (T)s;
		}
		
		return (T)mapper.map(s, targetClass);
	}
}
