package sk.seges.corpis.core.pap.transfer;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.processingEnv.getMessager().printMessage(Kind.NOTE,
				"Beer, beer, beer. I'm going for a beeeer. I like drinking beer, lovely, lovely beer. La la la");
		return false;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add(sk.seges.corpis.platform.annotation.TransferObject.class.getCanonicalName());
		return hashSet;
	}
}