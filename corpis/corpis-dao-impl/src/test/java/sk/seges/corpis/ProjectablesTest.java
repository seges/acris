package sk.seges.corpis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.corpis.dao.IOrderTestDAO;
import sk.seges.corpis.dataset.TestDataSetHelper;
import sk.seges.corpis.domain.CommonMailStuffTestEmbeddable;
import sk.seges.corpis.domain.LocationTestDO;
import sk.seges.corpis.domain.MailTemplateTestEmbeddable;
import sk.seges.corpis.domain.OrderTestDO;
import sk.seges.corpis.domain.StreetTestDO;
import sk.seges.corpis.domain.UserTestDO;
import sk.seges.corpis.domain.VATTestDO;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-corpis-dao-context.xml" })
public class ProjectablesTest {
	private static final int ITEM_COUNT = 1;

	@Resource
	private IOrderTestDAO orderDAO;
	@Resource
	private TestDataSetHelper helper;

	private List<OrderTestDO> dataSet;

	@Before
	public void setUp() {

		dataSet = new LinkedList<OrderTestDO>();

		StreetTestDO street = new StreetTestDO();
		street.setName("za rozkami");
		street.setNumber(15);

		LocationTestDO birth = new LocationTestDO();
		birth.setCity("mrkvovce");
		birth.setState("drundulakovo");
		birth.setStreet(street);

		UserTestDO user = new UserTestDO();
		user.setLogin("franta");
		user.setName("Frantisek Dobrota");
		user.setPassword("atnarf");
		user.setBirthplace(birth);

		UserTestDO anotherUser = new UserTestDO();
		anotherUser.setLogin("jara");
		user.setName("Jara Cimrman");
		anotherUser.setPassword("araj");

		LocationTestDO anotherBirth = new LocationTestDO();
		anotherBirth.setCity("Vieden");
		anotherBirth.setState("Rakuske cisarstvo");
		anotherUser.setBirthplace(anotherBirth);

		MailTemplateTestEmbeddable anotherMailTemplate = new MailTemplateTestEmbeddable();
		anotherMailTemplate.setSubject("another user subject");
		CommonMailStuffTestEmbeddable anotherCommonMailStuff = new CommonMailStuffTestEmbeddable();
		anotherCommonMailStuff.setMailAddress(anotherBirth);
		anotherMailTemplate.setCommonStuff(anotherCommonMailStuff);
		anotherUser.setMailTemplate(anotherMailTemplate);

		orderDAO.persistObject(anotherUser);

		MailTemplateTestEmbeddable mailTemplate = new MailTemplateTestEmbeddable();
		user.setMailTemplate(mailTemplate);
		mailTemplate.setSubject("ViagraAuthorized-Store");
		mailTemplate.setMailBody("Discount marathon continues! 80% off. collective presidential USS differs");

		CommonMailStuffTestEmbeddable commonMailStuff = new CommonMailStuffTestEmbeddable();
		commonMailStuff.setSimpleMailAttribute("yetAnotherAttribute");

		LocationTestDO mailDestination = new LocationTestDO();
		mailDestination.setCity("Liptakov");
		mailDestination.setState("Rakusko-Uhorsko");
		orderDAO.persistObject(mailDestination);

		commonMailStuff.setMailAddress(mailDestination);
		mailTemplate.setCommonStuff(commonMailStuff);

		mailTemplate.setToUser(anotherUser);

		orderDAO.persistObject(user);

		VATTestDO vat19 = new VATTestDO();
		vat19.setVat((short) 19);
		vat19.setValidFrom(new Date());
		orderDAO.persistObject(vat19);

		for (int i = 0; i < ITEM_COUNT; i++) {
			StreetTestDO s = new StreetTestDO();
			s.setName("somewhere");
			s.setNumber(i);

			LocationTestDO l = new LocationTestDO();
			l.setStreet(s);
			l.setCity("somecity " + i);

			OrderTestDO o = new OrderTestDO();
			o.setUser(user);
			o.setDeliveryLocation(l);
			o.setOrdered(new Date());
			o.setDelivered(new Date());
			o.setOrderId("myid-" + i);

			mailTemplate = new MailTemplateTestEmbeddable();
			o.setMailTemplate(mailTemplate);
			mailTemplate.setSubject("ViagraAuthorized-Store");
			mailTemplate.setMailBody("Discount marathon continues! 80% off. collective presidential USS differs");

			o = orderDAO.persist(o);
			dataSet.add(o);
		}
	}

	@After
	public void tearDown() {
		helper.deleteAllInEntityHQL(OrderTestDO.class.getName());
		helper.deleteAllInEntityHQL(VATTestDO.class.getName());
		helper.deleteAllInEntityHQL(UserTestDO.class.getName());
		helper.deleteAllInEntityHQL(LocationTestDO.class.getName());
		helper.deleteAllInEntityHQL(StreetTestDO.class.getName());

		dataSet.clear();
	}

	@Test
	public void testProjectableOnDirectField() throws Exception {
		Page page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("ordered");

		List<OrderTestDO> orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getOrdered());
			assertNull(order.getDelivered());
			assertNull(order.getUser());
		}

		page.addProjectable("delivered");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getOrdered());
			assertNotNull(order.getDelivered());
			assertNull(order.getUser());
		}
	}

	@Test
	public void testProjectableOnChainedFields() throws Exception {
		Page page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.password");

		List<OrderTestDO> orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
			assertNotNull(order.getUser().getPassword());
			assertNull(order.getOrdered());
			assertNull(order.getDelivered());
		}

		// test complex projectable deep in object
		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.birthplace.street.name");

		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
			assertNotNull(order.getUser().getBirthplace().getStreet());
			assertNotNull(order.getUser().getBirthplace().getStreet().getName());
			assertNull(order.getUser().getBirthplace().getStreet().getNumber());
			assertNull(order.getUser().getBirthplace().getCity());
			assertNull(order.getUser().getPassword());
			assertNull(order.getOrdered());
			assertNull(order.getDelivered());
		}

		// test two projectables deep in object (= name & number)
		page.addProjectable("user.birthplace.street.number");

		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
			assertNotNull(order.getUser().getBirthplace().getStreet());
			assertNotNull(order.getUser().getBirthplace().getStreet().getName());
			assertNotNull(order.getUser().getBirthplace().getStreet().getNumber());
			assertNull(order.getUser().getBirthplace().getCity());
			assertNull(order.getUser().getPassword());
			assertNull(order.getOrdered());
			assertNull(order.getDelivered());
		}
	}

	@Test
	public void testProjectableOnVariousFieldsWithFilter() throws Exception {
		Page page = new Page(0, 0);
		List<OrderTestDO> orders;
		SimpleExpression<String> filterable;
		
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("orderId");
		page.addProjectable("user.birthplace.street.name");
		page.addProjectable("ordered");
		
		filterable = Filter.ilike("orderId");
		filterable.setValue("%my%");
		page.setFilterable(filterable);

		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getOrdered());
			assertNotNull(order.getOrderId());
			assertNull(order.getDelivered());
			assertNotNull(order.getUser());
		}
		
		
		
		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.login");

		filterable = Filter.eq("user.login");
		filterable.setValue("franta");
		page.setFilterable(filterable);

		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser().getLogin());
			assertNull(order.getUser().getName());
			assertNull(order.getDelivered());
		}
		
	}

	@Test
	public void testProjectableOnEmbeddedChainedFields() throws Exception {
		Page page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.mailTemplate.toUser.mailTemplate.commonStuff.mailAddress.city");
		List<OrderTestDO> orders;
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
			assertNull(order.getOrderId());
			assertNull(order.getDelivered());

			assertNotNull(order.getUser().getMailTemplate());
			assertNull(order.getUser().getBirthplace());
			assertNull(order.getUser().getName());

			assertNotNull(order.getUser().getMailTemplate().getToUser());
			assertNull(order.getUser().getMailTemplate().getSubject());

			assertNotNull(order.getUser().getMailTemplate().getToUser().getMailTemplate());
			assertNull(order.getUser().getMailTemplate().getToUser().getBirthplace());

			assertNotNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getCommonStuff());
			assertNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getMailBody());

			assertNotNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getCommonStuff()
					.getMailAddress());
			assertNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getCommonStuff()
					.getSimpleMailAttribute());

			assertNotNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getCommonStuff()
					.getMailAddress().getCity());
			assertNull(order.getUser().getMailTemplate().getToUser().getMailTemplate().getCommonStuff()
					.getMailAddress().getState());
			
//			TODO: more asserts
		}

		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.mailTemplate.toUser.mailTemplate.subject");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
//			TODO: more asserts
		}

		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.mailTemplate.commonStuff.mailAddress.city");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
//			TODO: more asserts
		}

		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.mailTemplate.commonStuff.simpleMailAttribute");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
//			TODO: more asserts
		}

		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("user.mailTemplate.mailBody");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getUser());
//			TODO: more asserts
		}

		page = new Page(0, 0);
		page.setProjectableResult(OrderTestDO.class.getName());
		page.addProjectable("mailTemplate.subject");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for (OrderTestDO order : orders) {
			assertNotNull(order.getMailTemplate().getSubject());
			assertNull(order.getMailTemplate().getCommonStuff());
			assertNull(order.getMailTemplate().getMailBody());
			assertNull(order.getMailTemplate().getToUser());
		}
	}

}
