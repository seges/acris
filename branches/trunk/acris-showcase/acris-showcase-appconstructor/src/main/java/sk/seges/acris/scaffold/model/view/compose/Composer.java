package sk.seges.acris.scaffold.model.view.compose;

public class Composer {
	protected Multiselect multiselect(Class<?> clz) {
		return new Multiselect();
	}
	
	public class Multiselect {
		public FilterBy filterBy() {
			return new FilterBy();
		}
	}
	
	public class FilterBy {
		public Disjunction or() {
			return new Disjunction();
		}
	}
	
	public class Disjunction {
		public Disjunction add(Class<?> clz) {
			return this;
		}
	}
}
