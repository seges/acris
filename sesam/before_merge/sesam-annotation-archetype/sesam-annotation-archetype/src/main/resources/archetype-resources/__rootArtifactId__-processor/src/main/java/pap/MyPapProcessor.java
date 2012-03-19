package ${package}.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

import ${package}.pap.annotation.MyPapAnnotation;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MyPapProcessor extends AbstractConfigurableProcessor {

	public static ImmutableType getOutputClass(ImmutableType mutableType) { 
                String simpleName = mutableType.getSimpleName();
                return mutableType.setName(simpleName + "MyService");
        }

        @Override
        protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
                return new NamedType[] { getOutputClass(mutableType) };
        }

}


