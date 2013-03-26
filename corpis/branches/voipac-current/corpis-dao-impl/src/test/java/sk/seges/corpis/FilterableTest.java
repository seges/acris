/**
 * 
 */
package sk.seges.corpis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import sk.seges.corpis.domain.LocationTestDO;
import sk.seges.corpis.domain.MailTemplateTestEmbeddable;
import sk.seges.corpis.domain.OrderTestDO;
import sk.seges.corpis.domain.StreetTestDO;
import sk.seges.corpis.domain.UserTestDO;
import sk.seges.corpis.domain.VATTestDO;
import sk.seges.sesam.dao.BetweenExpression;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.NullExpression;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;

/**
 * @author eldzi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-corpis-dao-context.xml"})
public class FilterableTest {
	private static final String SOMEWHERE = "somewhere";
	private static final String SOMEWHERE2 = "somewhere2";

	private static final int ITEM_COUNT = 10;
	
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
		
		MailTemplateTestEmbeddable mailTemplate = new MailTemplateTestEmbeddable();
		mailTemplate.setSubject("ProzacAuthorized-Store");
		mailTemplate.setMailBody("Winner Frantisek Dobrota! 70% max discounts.");
		user.setMailTemplate(mailTemplate);
		
		orderDAO.persistObject(user);
		
		VATTestDO vat19 = new VATTestDO();
		vat19.setVat((short)19);
		vat19.setValidFrom(new Date());
		orderDAO.persistObject(vat19);
		

		
		for (int i = 0; i < ITEM_COUNT / 2; i++) {
			StreetTestDO s = new StreetTestDO();
			s.setName(SOMEWHERE);
			s.setNumber(i);
			
			LocationTestDO l = new LocationTestDO();
			l.setStreet(s);
			l.setCity("somecity " + i);
			if(i == 2) {
				l.setState("state" + i);
			}
			
			OrderTestDO o = new OrderTestDO();
			o.setUser(user);
			o.setDeliveryLocation(l);
			o.setOrdered(new Date());
			o.setDelivered(new Date());
			o.setOrderId("myid-" + i);

			mailTemplate = new MailTemplateTestEmbeddable();
			mailTemplate.setSubject("ViagraAuthorized-Store");
			mailTemplate.setMailBody("Discount marathon continues! 80% off. collective presidential USS differs");
			o.setMailTemplate(mailTemplate);
			
			o = orderDAO.persist(o);
			dataSet.add(o);
		}

		for (int i = 0; i < ITEM_COUNT / 2; i++) {
			StreetTestDO s = new StreetTestDO();
			s.setName(SOMEWHERE2);
			s.setNumber(i);
			
			LocationTestDO l = new LocationTestDO();
			l.setStreet(s);
			l.setCity("somecity " + i);
			l.setState("state" + i);
			
			OrderTestDO o = new OrderTestDO();
			o.setUser(user);
			o.setDeliveryLocation(l);
			o.setOrdered(new Date());
			o.setDelivered(new Date());

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
	public void testFilterBySimpleField() throws Exception {
		Page p = new Page(0, 7);
		SimpleExpression<String> filterable = Filter.ilike("orderId");
		filterable.setValue("my");
		p.setFilterable(filterable);
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 5, filtered.getResult().size());
	}
	
	@Test
	public void testFilterByStringAndNumberField() throws Exception {
		Page p = new Page(0, 7);
		SimpleExpression<String> filterable = Filter.eq("deliveryLocation.street.name");
		filterable.setValue(SOMEWHERE);
		p.setFilterable(filterable);
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", ITEM_COUNT / 2, filtered.getResult().size());
		for(OrderTestDO order : filtered.getResult()) {
			assertEquals(SOMEWHERE, order.getDeliveryLocation().getStreet().getName());
		}
		
		SimpleExpression<Integer> filterable2 = Filter.eq("deliveryLocation.street.number");
		filterable2.setValue(2);
		SimpleExpression<String> filterable3 = Filter.eq("deliveryLocation.street.name");
		filterable3.setValue(SOMEWHERE2);
		Criterion filterableFinal = Filter.conjunction().add(
				Filter.disjunction().add(filterable).add(filterable3)).add(filterable2);
		p.setFilterable(filterableFinal);
		
		filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 2, filtered.getResult().size());
		for(OrderTestDO order : filtered.getResult()) {
			if (!(order.getDeliveryLocation().getStreet().getName().equals(SOMEWHERE) || order
					.getDeliveryLocation().getStreet().getName().equals(SOMEWHERE2))) {
				fail("Neither " + SOMEWHERE + " nor " + SOMEWHERE2 + " in street name");
			}
			
			assertEquals(Integer.valueOf(2), order.getDeliveryLocation().getStreet().getNumber());
		}
	}
	
	@Test
	public void testNotAndBetween() throws Exception {
		Page p = new Page(0, 7);
		BetweenExpression<Integer> filterable = Filter.between("deliveryLocation.street.number");
		filterable.setLoValue(1);
		filterable.setHiValue(3);
		p.setFilterable(Filter.not(filterable));
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 4, filtered.getResult().size());
		for (OrderTestDO order : filtered.getResult()) {
			if (!(order.getDeliveryLocation().getStreet().getNumber().equals(0) || order
					.getDeliveryLocation().getStreet().getNumber().equals(4))) {
				fail("Neither 0 nor 4 in street number");
			}
		}
	}
	
	@Test
	public void testNullAndNotNull() throws Exception {
		Page p = new Page(0, 10);
		NullExpression filterable = Filter.isNull("deliveryLocation.state");
		
		p.setFilterable(filterable);
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 4, filtered.getResult().size());
		
		p.setFilterable(Filter.isNotNull("deliveryLocation.state"));
		filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 6, filtered.getResult().size());
	}
	
	@Test
	public void testFilterByStringEmbeddedField() throws Exception {
		Page p = new Page(0, 7);
		SimpleExpression<String> filterable = Filter.eq("mailTemplate.subject");
		filterable.setValue("ViagraAuthorized-Store");
		p.setFilterable(filterable);
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", ITEM_COUNT / 2, filtered.getResult().size());
		
		filterable.setValue("ZoloftAuthorized-Store");
		filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 0, filtered.getResult().size());
	}
	
	@Test
	public void testFilterBySimpleEmbeddedField() throws Exception {
		Page p = new Page(0, 20);
		SimpleExpression<String> filterable = Filter.ilike("user.mailTemplate.subject");
		filterable.setValue("ProzacAuthorized-Store");
		p.setFilterable(filterable);
		
		PagedResult<List<OrderTestDO>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", ITEM_COUNT, filtered.getResult().size());
		
		filterable.setValue("SertralinAuthorized-Store");
		filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 0, filtered.getResult().size());
	}
}
