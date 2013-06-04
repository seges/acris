package sk.seges.acris.pivo;

public interface IDependency {
	Object resolve(ChocolateFactory factory);
}
