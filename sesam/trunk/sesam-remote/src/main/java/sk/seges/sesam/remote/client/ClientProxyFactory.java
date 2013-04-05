/**
 * 
 */
package sk.seges.sesam.remote.client;


/**
 * @author lgazo
 */
public interface ClientProxyFactory<T> {
	T createProxy(Class<T> invokedClass);
	T createProxy(String invokedClassName);
	T createProxy(Class<T> proxyClass, IJMSExceptionAnalyzer analyzer);
}
