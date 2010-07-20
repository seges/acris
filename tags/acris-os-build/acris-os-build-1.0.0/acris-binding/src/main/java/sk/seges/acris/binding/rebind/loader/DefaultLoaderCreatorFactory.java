package sk.seges.acris.binding.rebind.loader;

import java.lang.reflect.Constructor;

import sk.seges.acris.binding.rebind.GeneratorException;

public class DefaultLoaderCreatorFactory {

	private static Class<? extends ILoaderCreator> loaderCreator;

	public static void setDefaultLoaderCreator(Class<? extends ILoaderCreator> loaderCreator) {
		if (DefaultLoaderCreatorFactory.loaderCreator == null) {
			DefaultLoaderCreatorFactory.loaderCreator = loaderCreator;
		}
	}
	
	public static ILoaderCreator getLoaderCreator() throws GeneratorException {
		Constructor<? extends ILoaderCreator> constructor;
		try {
			constructor = loaderCreator.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			throw new GeneratorException(e.getMessage());
		}
	}
}