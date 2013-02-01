package sk.seges.acris.widget.client.resize;

public abstract class ResizableListener {

	abstract public void onResize(int width, int height);
	
	public void onResizing(int width, int height) {}
}
