package sk.seges.acris.recorder.client.recorder.support;

public enum RecorderMode {
	REALTIME(1), //real-time event logging via RPC 
	BATCH(30); // events logging using batch
	
	private int batchSize = 1;
	
	private RecorderMode(int batchSize) {
		this.batchSize = batchSize;
	}
	
	public int getBatchSize() {
		return batchSize;
	}
}
