package sk.seges.sesam.shared.model.converter;

public interface TransferObjectConverter<T, S> {

	T createDtoInstance();
	
	S createDomainInstance(Object id);

	T toDto(S domain);
	
	S fromDto(T dto);
}