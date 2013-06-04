package sk.seges.sesam.pap.model.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public class ConfigurationCache {

	private Map<String, List<ConfigurationTypeElement>> dtoCache = new HashMap<String, List<ConfigurationTypeElement>>();
	private Map<String, List<ConfigurationTypeElement>> domainCache = new HashMap<String, List<ConfigurationTypeElement>>();
	
	public List<ConfigurationTypeElement> registerDto(MutableTypeMirror dto, List<ConfigurationTypeElement> configurations) {
		String dtoName = dto.toString(ClassSerializer.CANONICAL, false);
		dtoCache.remove(dtoName);
		dtoCache.put(dtoName, configurations);
		return configurations;
	}

	public List<ConfigurationTypeElement> registerDomain(MutableTypeMirror domain, List<ConfigurationTypeElement> configurations) {
		String domainName = domain.toString(ClassSerializer.CANONICAL, false);
		domainCache.remove(domainName);
		domainCache.put(domainName, configurations);
		return configurations;
	}

	public List<ConfigurationTypeElement> registerDto(MutableTypeMirror dto, ConfigurationTypeElement configuration) {
		
		String dtoName = dto.toString(ClassSerializer.CANONICAL, false);

		List<ConfigurationTypeElement> configurations = dtoCache.get(dtoName);
		if (configurations == null) {
			configurations = new ArrayList<ConfigurationTypeElement>();
			dtoCache.put(dtoName, configurations);
		}
		
		configurations.add(configuration);
		return configurations;
	}
	
	public List<ConfigurationTypeElement> registerDomain(MutableTypeMirror domain, ConfigurationTypeElement configuration) {
		
		String domainName = domain.toString(ClassSerializer.CANONICAL, false);

		List<ConfigurationTypeElement> configurations = domainCache.get(domainName);
		if (configurations == null) {
			configurations = new ArrayList<ConfigurationTypeElement>();
			domainCache.put(domainName, configurations);
		}
		
		configurations.add(configuration);
		
		return configurations;
	}
	
	public List<ConfigurationTypeElement> getConfigurationForDomain(MutableTypeMirror domain) {
		String domainName = domain.toString(ClassSerializer.CANONICAL, false);
		List<ConfigurationTypeElement> result = domainCache.get(domainName);
		if (result == null) {
			return result;
		}
		return Collections.unmodifiableList(result);
	}

	public List<ConfigurationTypeElement> getConfigurationForDTO(MutableTypeMirror dto) {
		String dtoName = dto.toString(ClassSerializer.CANONICAL, false);
		List<ConfigurationTypeElement> result = dtoCache.get(dtoName);
		if (result == null) {
			return result;
		}
		return Collections.unmodifiableList(result);
	}
}