package sk.seges.sesam.pap.model.hibernate;

import java.lang.annotation.Annotation;

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
	};

	public abstract Class<? extends Annotation> getAnnotationClass();

	public abstract Class<?> getTargetEntityFromAnnotation(Annotation annotation);
}