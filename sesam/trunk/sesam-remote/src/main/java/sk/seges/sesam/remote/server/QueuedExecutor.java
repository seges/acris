package sk.seges.sesam.remote.server;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.RemoteCommand;

/**
 * @author eldzi
 */
public class QueuedExecutor<T> extends Thread {
	private static final Logger log = Logger.getLogger(QueuedExecutor.class);
	private static final int WAIT_TIME = 1;
    private final T target;
    private final BlockingQueue<RemoteCommand> commands;
	private boolean shutdown;

    public QueuedExecutor(T target, int numOfWaitingCommands) {
        this.target = target;
        commands = new LinkedBlockingDeque<RemoteCommand>(numOfWaitingCommands);
		shutdown = false;

        setName("Queued executor (" + numOfWaitingCommands + ") for " + target.getClass());
    }

    public synchronized void request(RemoteCommand command) {
        commands.offer(command);
    }

    @Override
    public void run() {
		RemoteCommand command = null;

		while(!shutdown) {
			try {
				command = commands.poll(WAIT_TIME, TimeUnit.MINUTES);
				if(command == null)
					// nothing in the queue.... wait again
					continue;

				Method method = target.getClass().getMethod(command.getMethodName(), command.getParamTypes());
				method.invoke(target, command.getParams());
			} catch (InterruptedException ex) {
				throw new RuntimeException("Polling a command from queue failed. Target = " + target, ex);
			} catch(Exception e) {
//				throw new RuntimeException("Cannot execute command = " + command + " on target = " + target, e);
				log.error("Execution thrown exception but will continue listening. Cannot execute command = " + command + " on target = " + target, e);
			}
		}
    }
}
