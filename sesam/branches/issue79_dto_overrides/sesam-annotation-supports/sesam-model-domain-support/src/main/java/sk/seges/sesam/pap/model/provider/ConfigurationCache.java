package sk.seges.sesam.pap.model.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;

public class ConfigurationCache {

	private Map<MutableTypeMirror, List<ConfigurationTypeElement>> dtoCache = new HashMap<MutableTypeMirror, List<ConfigurationTypeElement>>();
	private Map<MutableTypeMirror, List<ConfigurationTypeElement>> domainCache = new HashMap<MutableTypeMirror, List<ConfigurationTypeElement>>();
	
	public List<ConfigurationTypeElement> registerDto(MutableTypeMirror dto, List<ConfigurationTypeElement> configurations) {
		dtoCache.remove(dto);
		dtoCache.put(dto, configurations);
		return configurations;
	}

	public List<ConfigurationTypeElement> registerDomain(MutableTypeMirror domain, List<ConfigurationTypeElement> configurations) {
		domainCache.remove(domain);
		domainCache.put(domain, configurations);
		return configurations;
	}

	public List<ConfigurationTypeElement> registerDto(MutableTypeMirror dto, ConfigurationTypeElement configuration) {
		List<ConfigurationTypeElement> configurations = dtoCache.get(dto);
		
		if (configurations == null) {
			configurations = new ArrayList<ConfigurationTypeElement>();
			dtoCache.put(dto, configurations);
		}
		
		configurations.add(configuration);
		return configurations;
	}
	
	public List<ConfigurationTypeElement> registerDomain(MutableTypeMirror domain, ConfigurationTypeElement configuration) {
		List<ConfigurationTypeElement> configurations = domainCache.get(domain);
		if (configurations == null) {
			configurations = new ArrayList<ConfigurationTypeElement>();
			domainCache.put(domain, configurations);
		}
		
		configurations.add(configuration);
		
		return configurations;
	}
	
	public List<ConfigurationTypeElement> getConfigurationForDomain(MutableTypeMirror domain) {
		return domainCache.get(domain);
	}

	public List<ConfigurationTypeElement> getConfigurationForDTO(MutableTypeMirror dto) {
		return dtoCache.get(dto);
	}
}