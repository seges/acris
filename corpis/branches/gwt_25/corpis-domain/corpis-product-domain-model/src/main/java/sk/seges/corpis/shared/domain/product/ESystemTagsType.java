package sk.seges.corpis.shared.domain.product;

/**
 * Definition of system tags which is stored
 * in type column of tag table
 * 
 * @author psloboda
 */
public enum ESystemTagsType {

	PS(false), DISCOUNT(false), NEW(false), BESTSELLER(false), ID(false), ADDITIONAL_PRODUCT(false), MASTER(true), TOP(false), NOT_CLASSIFIED(false);
	
	boolean mutable;

	private ESystemTagsType(boolean mutable) {
		this.mutable = mutable;
	}

	public boolean isMutable() {
		return mutable;
	}	
}