package sk.seges.acris.site.server.gilead;

import net.sf.gilead.core.beanlib.IClassMapper;
import net.sf.gilead.core.beanlib.mapper.ExplicitClassMapper;
import net.sf.gilead.core.beanlib.mapper.ProxyClassMapper;

public class ExplicitProxyClassMapper implements IClassMapper {

	ProxyClassMapper proxyClassMapper;
	ExplicitClassMapper explicitClassMapper;

	@Override
	public Class<?> getTargetClass(Class<?> sourceClass) {
		Class<?> clazz = explicitClassMapper.getTargetClass(sourceClass);
		if (clazz != null) clazz = proxyClassMapper.getTargetClass(clazz);
		else clazz = proxyClassMapper.getTargetClass(sourceClass);
		return clazz;
	}

	@Override
	public Class<?> getSourceClass(Class<?> targetClass) {
		Class<?> clazz = proxyClassMapper.getSourceClass(targetClass);
		if (clazz != null) clazz = explicitClassMapper.getSourceClass(clazz);
		else clazz = explicitClassMapper.getSourceClass(targetClass);
		return clazz;
	}

	public ProxyClassMapper getProxyClassMapper() {
		return proxyClassMapper;
	}

	public void setProxyClassMapper(ProxyClassMapper proxyClassMapper) {
		this.proxyClassMapper = proxyClassMapper;
	}

	public ExplicitClassMapper getExplicitClassMapper() {
		return explicitClassMapper;
	}

	public void setExplicitClassMapper(ExplicitClassMapper explicitClassMapper) {
		this.explicitClassMapper = explicitClassMapper;
	}
}