package sk.seges.sesam.core.test.bromine;

import java.util.Hashtable;

public enum BromineCommand {
	
	CUSTOM("customCommand"),
    ASSERT_TRUE("assertTrue"),
    ASSERT_FALSE("assertFalse"),
    ASSERT_EQUALS("assertEquals"),
    ASSERT_NOT_EQUALS("assertNotEquals"),
    VERIFY_TRUE("verifyTrue"),
    VERIFY_FALSE("verifyFalse"),
    VERIFY_EQUALS("verifyEquals"),
    VERIFY_NOT_EQUALS("verifyNotEquals"),
    WAITING("waiting");

	enum BromineParameter {
		STATEMENT1("statement1"),
		STATEMENT2("statement2"),
		ACTION("action"),
		STATUS("status"),
		COMMENT("comment");
		
		private String key;
		
		BromineParameter(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}
	
	private String name;
	
	BromineCommand(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	Hashtable<String, Object> parameters;
	
	public BromineCommand get() {
		parameters = new Hashtable<String, Object>();
		return this;
	}
	
	public BromineCommand statement1(String statement1) {
		parameters.put(BromineParameter.STATEMENT1.getKey(), statement1);
		return this;
	}

	public BromineCommand statement1(Boolean statement1) {
		parameters.put(BromineParameter.STATEMENT1.getKey(), statement1);
		return this;
	}

	public BromineCommand statement2(String statement2) {
		parameters.put(BromineParameter.STATEMENT2.getKey(), statement2);
		return this;
	}
	
	public BromineCommand action(String action) {
		parameters.put(BromineParameter.ACTION.getKey(), action);
		return this;
	}
	
	public BromineCommand status(String status) {
		parameters.put(BromineParameter.STATUS.getKey(), status);
		return this;
	}
	
	public BromineCommand comment(String comment) {
		parameters.put(BromineParameter.COMMENT.getKey(), comment);
		return this;
	}
	
	public Hashtable<String, Object> to() {
		return parameters;
	}
}