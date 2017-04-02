package com.imes.rnd.jpa.dellstore;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
		try (AutoCloseableEntityManager emWrapper = new AutoCloseableEntityManager(emf)) {
			EntityManager em = emWrapper.getEntityManager();
			
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			
			Customer customer = em.find(Customer.class, 1);
			Assert.assertNotNull(customer);
			
			Session session = em.unwrap(Session.class);
			
			if (Math.abs(-1) == 2) {
				testSQLQueryList(session);
			}
			
			testSQLQueryStreaming(session);
			
			tx.commit();
		}
	}

	private void testSQLQueryList(Session session) {
		SQLQuery query = session.createSQLQuery("select customerid, firstname, lastname, address1, phone, email, creditcard, username, password from customers");
		
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.list();
		
		int count = 0;
		for (Object[] row : result) {
			if (row[0] != null) {
				++count;
			}
		}
		
		Assert.assertEquals(20000, count);
	}
	
	private void testSQLQueryStreaming(Session session) {
		SQLQuery query = session.createSQLQuery("select customerid, firstname, lastname, address1, phone, email, creditcard, username, password from customers");
		Stream<Object[]> resultStream = StreamSupport.stream(new SQLQueryResultSpliterator(query), false);
		long count = resultStream.count();
		Assert.assertEquals(20000, count);
	}
}