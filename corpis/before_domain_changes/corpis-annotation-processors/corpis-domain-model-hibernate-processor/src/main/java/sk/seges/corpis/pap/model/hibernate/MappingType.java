package sk.seges.corpis.pap.model.hibernate;

import java.lang.annotation.Annotation;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public enum MappingType {
	
	ONE_TO_MANY {

		@Override
		public Class<? extends Annotation> getAnnotationClass() {
			return OneToMany.class;
		}
		
		@Override
		public Class<?> getTargetEntityFromAnnotation(Annotation annotation) {
			return ((OneToMany)annotation).targetEntity();
		}

		@Override
		public FetchType getFetchTypeFromAnnotation(Annotation annotation) {
			return ((OneToMany)annotation).fetch();
		}
	},

	MANY_TO_MANY {

		@Override
		public Class<? extends Annotation> getAnnotationClass() {
			return ManyToMany.class;
		}
		
		@Override
		public Class<?> getTargetEntityFromAnnotation(Annotation annotation) {
			return ((ManyToMany)annotation).targetEntity();
		}

		@Override
		public FetchType getFetchTypeFromAnnotation(Annotation annotation) {
			return ((ManyToMany)annotation).fetch();
		}
	},

	MANY_TO_ONE {

		@Override
		public Class<? extends Annotation> getAnnotationClass() {
			return ManyToOne.class;
		}
		
		@Override
		public Class<?> getTargetEntityFromAnnotation(Annotation annotation) {
			return ((ManyToOne)annotation).targetEntity();
		}

		@Override
		public FetchType getFetchTypeFromAnnotation(Annotation annotation) {
			return ((ManyToOne)annotation).fetch();
		}
	},

	ONE_TO_ONE {

		@Override
		public Class<? extends Annotation> getAnnotationClass() {
			return OneToOne.class;
		}
		
		@Override
		public Class<?> getTargetEntityFromAnnotation(Annotation annotation) {
			return ((OneToOne)annotation).targetEntity();
		}

		@Override
		public FetchType getFetchTypeFromAnnotation(Annotation annotation) {
			return ((OneToOne)annotation).fetch();
		}
	},
	
	BASIC {
		@Override
		public Class<? extends Annotation> getAnnotationClass() {
			return Basic.class;
		}
		
		@Override
		public Class<?> getTargetEntityFromAnnotation(Annotation annotation) {
			return null;
		}

		@Override
		public FetchType getFetchTypeFromAnnotation(Annotation annotation) {
			return ((Basic)annotation).fetch();
		}
	};

	public abstract Class<? extends Annotation> getAnnotationClass();

	public abstract FetchType getFetchTypeFromAnnotation(Annotation annotation);
	public abstract Class<?> getTargetEntityFromAnnotation(Annotation annotation);
}