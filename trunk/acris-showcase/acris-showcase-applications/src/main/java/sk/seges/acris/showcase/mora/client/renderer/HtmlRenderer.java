package sk.seges.acris.showcase.mora.client.renderer;



public interface HtmlRenderer<E, T> {

	void render(E element, T target);

}