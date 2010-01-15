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

import sk.seges.corpis.dao.IOrderDAO;
import sk.seges.corpis.dataset.TestDataSetHelper;
import sk.seges.corpis.domain.Location;
import sk.seges.corpis.domain.Order;
import sk.seges.corpis.domain.Street;
import sk.seges.corpis.domain.User;
import sk.seges.corpis.domain.VAT;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-corpis-dao-context.xml"})
public class ProjectablesTest {
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
		
		for (int i = 0; i < ITEM_COUNT; i++) {
			Street s = new Street();
			s.setName("somewhere");
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
	public void testProjectableOnDirectField() throws Exception {
		Page page = new Page(0, 0);
		page.setProjectableResult(Order.class.getName());
		page.addProjectable("ordered");
		
		List<Order> orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for(Order order : orders) {
			assertNotNull(order.getOrdered());
			assertNull(order.getDelivered());
			assertNull(order.getUser());
		}
		
		page.addProjectable("delivered");
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for(Order order : orders) {
			assertNotNull(order.getOrdered());
			assertNotNull(order.getDelivered());
			assertNull(order.getUser());
		}
	}
	
	@Test
	public void testProjectableOnChainedFields() throws Exception {
		Page page = new Page(0, 0);
		page.setProjectableResult(Order.class.getName());
		page.addProjectable("user.password");
		
		List<Order> orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for(Order order : orders) {
			assertNotNull(order.getUser());
			assertNotNull(order.getUser().getPassword());
			assertNull(order.getOrdered());
			assertNull(order.getDelivered());
		}

		// test complex projectable deep in object
		page = new Page(0, 0);
		page.setProjectableResult(Order.class.getName());
		page.addProjectable("user.birthplace.street.name");
		
		orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for(Order order : orders) {
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
		for(Order order : orders) {
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
		page.setProjectableResult(Order.class.getName());
		page.addProjectable("orderId");
		page.addProjectable("user.birthplace.street.name");
		page.addProjectable("ordered");
		
		SimpleExpression<String> filterable = Filter.ilike("orderId");
		filterable.setValue("%my%");
		page.setFilterable(filterable);
		
		List<Order> orders = orderDAO.findAll(page).getResult();
		assertEquals(ITEM_COUNT, orders.size());
		for(Order order : orders) {
			assertNotNull(order.getOrdered());
			assertNotNull(order.getOrderId());
			assertNull(order.getDelivered());
			assertNotNull(order.getUser());
		}
	}
}
