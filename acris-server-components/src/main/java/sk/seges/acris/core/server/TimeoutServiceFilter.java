package sk.seges.acris.core.server;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

/**
 * @author psloboda
 */
public class TimeoutServiceFilter implements Filter {

	private static final Logger logger = Logger.getLogger(TimeoutServiceFilter.class);
	private static Integer maxTimeout = null;
	private static ThreadPoolExecutor threadPool;
	
	public void setMaxTimeout(Integer maxTimeout) {
		TimeoutServiceFilter.maxTimeout = maxTimeout;
		TimeoutServiceFilter.threadPool = new ThreadPoolExecutor(10, 50, maxTimeout, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException,
			ServletException {
		logger.debug(String.format("doFilter for request: %s", ((HttpServletRequestWrapper)request).getRequestURL()));
		logger.debug(String.format("threadPool: %s", threadPool));		
		//do not filter rest requests also
		if (maxTimeout != null && maxTimeout > 0 && !((HttpServletRequestWrapper)request).getRequestURL().toString().contains("-rest")) {
			Callable<Void> callable = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					logger.debug("start call");
					chain.doFilter(request, response);
					logger.debug("end call");
					return null;
				}
			};
			Future<Void> future = threadPool.submit(callable);
			try {
				future.get(maxTimeout, TimeUnit.MINUTES);
			} catch (TimeoutException e) {
				logger.error("Timeout occured during call service.", e);
				restartThreadPool(future);
			} catch (Exception e) {
				logger.error("Exception occured during call service.", e);
				restartThreadPool(future);
			}
		} else {
			chain.doFilter(request, response);
		}
	}
	
	private void restartThreadPool(Future<Void> future) {
		if (future != null) future.cancel(true);
		TimeoutServiceFilter.threadPool.shutdown();
		TimeoutServiceFilter.threadPool = new ThreadPoolExecutor(3, 5, maxTimeout, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
		logger.debug(String.format("threadPool after restart: %s", threadPool));
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
