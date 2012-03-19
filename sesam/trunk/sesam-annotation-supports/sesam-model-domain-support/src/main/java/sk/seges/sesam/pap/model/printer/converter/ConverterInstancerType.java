package sk.seges.sesam.pap.model.printer.converter;

public enum ConverterInstancerType {
	
	SERVICE_CONVERETR_INSTANCER {
		@Override
		public int getConstructorIndex() {
			
			//TODO isn't -1 better?
			return 1;
		}
	}, 
	
	REFERENCED_CONVERTER_INSTANCER {
		@Override
		public int getConstructorIndex() {
			return 0;
		}
	}; 
	
	public abstract int getConstructorIndex();
}