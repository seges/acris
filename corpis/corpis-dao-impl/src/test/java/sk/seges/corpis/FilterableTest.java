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

import sk.seges.corpis.dao.IOrderDAO;
import sk.seges.corpis.dataset.TestDataSetHelper;
import sk.seges.corpis.domain.Location;
import sk.seges.corpis.domain.Order;
import sk.seges.corpis.domain.Street;
import sk.seges.corpis.domain.User;
import sk.seges.corpis.domain.VAT;
import sk.seges.sesam.dao.BetweenExpression;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.Filter;
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
	private IOrderDAO orderDAO;
	@Resource
	private TestDataSetHelper helper;
	
	private List<Order> dataSet;
	
	@Before
	public void setUp() {
		dataSet = new LinkedList<Order>();

		Street street = new Street();
		street.setName("za rozkami");
		street.setNumber(15);
		
		Location birth = new Location();
		birth.setCity("mrkvovce");
		birth.setState("drundulakovo");
		birth.setStreet(street);
		
		User user = new User();
		user.setLogin("franta");
		user.setName("Frantisek Dobrota");
		user.setPassword("atnarf");
		user.setBirthplace(birth);
		orderDAO.persistObject(user);
		
		VAT vat19 = new VAT();
		vat19.setVat((short)19);
		vat19.setValidFrom(new Date());
		orderDAO.persistObject(vat19);
		
		for (int i = 0; i < ITEM_COUNT / 2; i++) {
			Street s = new Street();
			s.setName(SOMEWHERE);
			s.setNumber(i);
			
			Location l = new Location();
			l.setStreet(s);
			l.setCity("somecity " + i);
			
			Order o = new Order();
			o.setUser(user);
			o.setDeliveryLocation(l);
			o.setOrdered(new Date());
			o.setDelivered(new Date());
			o.setOrderId("myid-" + i);

			o = orderDAO.persist(o);
			dataSet.add(o);
		}

		for (int i = 0; i < ITEM_COUNT / 2; i++) {
			Street s = new Street();
			s.setName(SOMEWHERE2);
			s.setNumber(i);
			
			Location l = new Location();
			l.setStreet(s);
			l.setCity("somecity " + i);
			
			Order o = new Order();
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
		helper.deleteAllInEntityHQL(Order.class.getName());
		helper.deleteAllInEntityHQL(VAT.class.getName());
		helper.deleteAllInEntityHQL(User.class.getName());
		helper.deleteAllInEntityHQL(Location.class.getName());
		helper.deleteAllInEntityHQL(Street.class.getName());
		
		dataSet.clear();
	}
	
	@Test
	public void testFilterBySimpleField() throws Exception {
		Page p = new Page(0, 7);
		SimpleExpression<String> filterable = Filter.ilike("orderId");
		filterable.setValue("my");
		p.setFilterable(filterable);
		
		PagedResult<List<Order>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 5, filtered.getResult().size());
	}
	
	@Test
	public void testFilterByStringAndNumberField() throws Exception {
		Page p = new Page(0, 7);
		SimpleExpression<String> filterable = Filter.eq("deliveryLocation.street.name");
		filterable.setValue(SOMEWHERE);
		p.setFilterable(filterable);
		
		PagedResult<List<Order>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", ITEM_COUNT / 2, filtered.getResult().size());
		for(Order order : filtered.getResult()) {
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
		for(Order order : filtered.getResult()) {
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
		
		PagedResult<List<Order>> filtered = orderDAO.findAll(p);
		assertEquals("Unexpected number of filtered returned", 4, filtered.getResult().size());
		for (Order order : filtered.getResult()) {
			if (!(order.getDeliveryLocation().getStreet().getNumber().equals(0) || order
					.getDeliveryLocation().getStreet().getNumber().equals(4))) {
				fail("Neither 0 nor 4 in street number");
			}
		}
	}
}
