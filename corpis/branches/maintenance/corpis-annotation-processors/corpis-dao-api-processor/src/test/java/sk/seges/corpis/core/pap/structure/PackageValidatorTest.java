package sk.seges.corpis.core.pap.structure;

import junit.framework.Assert;

import org.junit.Test;

import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidator;

public class PackageValidatorTest {

	@Test
	public void testValid() {
		assertValid("sk.seges.acris.generator.server.user_management.model");
		assertValid("sk.seges.corpis.shared.model.mock");
		assertValid("sk.seges.corpis.shared.user_management.model.mock");
		assertValid("sk.seges.corpis.shared.user_management.roles.model.mock");
		assertValid("sk.seges.corpis.shared.user_management.roles.model.mock.util");
		assertValid("sk.seges.acris.security.shared.user_management.roles.model.mock.util");
	}

	@Test
	public void testInvalid() {
		assertInvalid("sk.seges.acris.server");
		assertInvalid("sk.seges.acris.server.user_management.roles.mock");
		assertInvalid("sk.seges.acris.user_management.roles.mock");
		assertInvalid("server.user_management.roles.mock");
	}
	
	protected PackageValidator getPackageValidator(String packageName) {
		return new DefaultPackageValidatorProvider().get(packageName);
	}
	
	private void assertValid(String packageName) {
		PackageValidator validator = getPackageValidator(packageName);
		Assert.assertTrue(validator.isValid());
		Assert.assertEquals(validator.toString(), packageName);
	}

	private void assertInvalid(String packageName) {
		PackageValidator validator = getPackageValidator(packageName);
		Assert.assertTrue(!validator.isValid());
		Assert.assertEquals(validator.toString(), packageName);
	}
}