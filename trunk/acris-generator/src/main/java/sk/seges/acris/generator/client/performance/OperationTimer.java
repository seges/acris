package sk.seges.acris.generator.client.performance;

import java.util.Date;

public class OperationTimer {

	public enum Operation {
		CONTENT_GENERATING,
		GENERATOR_CLIENT_PROCESSING, 
		GENERATOR_SERVER_READ_PROCESSING, 
		GENERATOR_DOM_MANIPULATION,
		CONTENT_RENDERING;
		
		Operation() {}
		
		private long total = 0;
		private long start = -1;
		
		void clean() {
			total = 0;
			start = -1;
		}
		
		void start() {
			start = new Date().getTime();
		}
		
		void stop() {
			total += new Date().getTime() - start;
		}
		
		long getTotal() {
			return total;
		}
	};
	
	private long total = 0;

	public OperationTimer() {
		for (Operation operation: Operation.values()) {
			operation.clean();
		}
	}
	
	public void start(Operation operation) {
		operation.start();
	}

	public void stop(Operation operation) {
		operation.stop();
		total += operation.getTotal();
	}
	
	private String formatTime(long ms, long totalms) {
		if (totalms == 0) {
			return "0 [100%]";
		}
		return "" + (long)(ms / 1000) + "[" + (ms * 100 / totalms) + "%]";
	}
	
	public String report() {
		return "Total: " + formatTime(Operation.CONTENT_GENERATING.getTotal(), Operation.CONTENT_GENERATING.getTotal()) + "s, Processing time in JS: " + 
			formatTime(Operation.GENERATOR_CLIENT_PROCESSING.getTotal(), Operation.CONTENT_GENERATING.getTotal()) + "s, " + "Rendering time: " + 
			formatTime(Operation.CONTENT_RENDERING.getTotal(), Operation.CONTENT_GENERATING.getTotal()) + "s, Service processing time (reading): " + 
			formatTime(Operation.GENERATOR_SERVER_READ_PROCESSING.getTotal(), Operation.CONTENT_GENERATING.getTotal()) + "s, DOM maninulation: " + 
			formatTime(Operation.GENERATOR_DOM_MANIPULATION.getTotal(), Operation.CONTENT_GENERATING.getTotal()) + "s";
	}
}