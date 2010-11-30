package sk.seges.acris.generator.client.renderer;



public interface HtmlRenderer<E, T> {

	void render(E element, T target);

}