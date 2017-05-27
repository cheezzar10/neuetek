package com.imes.rnd.jpa.dellstore;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.imes.rnd.jpa.dellstore.model.Customer;

public class OperationsTest {
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void createEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("dellstore2");
	}
	
	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}
	
	@Test
	public void testCustomerOperations() {
		Stream<Object[]> resultStream = null;
		
		try (AutoCloseableEntityManager emWrapper = new AutoCloseableEntityManager(emf)) {
			EntityManager em = emWrapper.getEntityManager();
			
			Customer customer = em.find(Customer.class, 1);
			Assert.assertNotNull(customer);
			
			Session session = em.unwrap(Session.class);
			
			// select customerid, firstname, lastname, address1, phone, email, creditcard, username, password from customers
			String query = "select c.customerid, c.firstname, c.lastname, c.address1, c.phone, c.email, c.creditcard, c.username, c.password, o.orderdate, o.totalamount from customers c join orders o on o.customerid = c.customerid";
			
			if (Math.abs(-1) == 2) {
				testSQLQueryList(session, query);
			}
			
			long start = System.currentTimeMillis();
			resultStream = executeQueryUsingStreaming(session, query);
			System.out.printf("query execution time %d ms%n", System.currentTimeMillis() - start);
			
			start = System.currentTimeMillis();
			double totalAmount = resultStream.mapToDouble(r -> ((BigDecimal)r[10]).doubleValue()).sum();
			System.out.printf("total amount calculation time %d ms%n", System.currentTimeMillis() - start);
			Assert.assertTrue(totalAmount == 2567386.79);
		}
	}

	void pause(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException intrEx) {
			return;
		}
	}

	private void testSQLQueryList(Session session, String query) {
		SQLQuery sqlQuery = session.createSQLQuery("select customerid, firstname, lastname, address1, phone, email, creditcard, username, password from customers");
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = sqlQuery.list();
		
		int count = 0;
		for (Object[] row : result) {
			if (row[0] != null) {
				++count;
			}
		}
		
		Assert.assertEquals(20000, count);
	}
	
	private Stream<Object[]> executeQueryUsingStreaming(Session session, String query) {
		SQLQuery sqlQuery = session.createSQLQuery(query);
		return StreamSupport.stream(new SQLQueryResultSpliterator(sqlQuery), false);
	}
}