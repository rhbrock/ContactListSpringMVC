/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.contactlistspringmvc.dao;

import com.sg.contactlistspringmvc.model.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ward
 */
public class ContactListDaoTest {

    private ContactListDao dao;

    public ContactListDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        //ask Spring for DAO
        ApplicationContext ctx
                = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        dao = ctx.getBean("contactListDao", ContactListDao.class);
        
        //remove all contacts
        List<Contact> contacts = dao.getAllContacts();
        for(Contact currentContact : contacts){
            dao.removeContact(currentContact.getContactId());
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addGetDeleteContact() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("John");
        nc.setLastName("Doe");
        nc.setCompany("Oracle");
        nc.setEmail("john@doe.com");
        nc.setPhone("1234445678");
        dao.addContact(nc);
        Contact fromDb = dao.getContactById(nc.getContactId());
        assertEquals(fromDb, nc);
        dao.removeContact(nc.getContactId());
        assertNull(dao.getContactById(nc.getContactId()));
    }

    @Test
    public void addUpdateContact() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        dao.addContact(nc);
        nc.setPhone("9999999999");
        dao.updateContact(nc);
        Contact fromDb = dao.getContactById(nc.getContactId());
        assertEquals(fromDb, nc);
    }

    @Test
    public void getAllContacts() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        dao.addContact(nc);
        // Create new contact
        Contact nc2 = new Contact();
        nc2.setFirstName("John");
        nc2.setLastName("Jones");
        nc2.setCompany("Apple");
        nc2.setEmail("john@jones.com");
        nc2.setPhone("5556667777");
        dao.addContact(nc2);
        List<Contact> cList = dao.getAllContacts();
        assertEquals(cList.size(), 2);
    }

    @Test
    public void searchContacts() {
        // Create new contact
        Contact nc = new Contact();
        nc.setFirstName("Jimmy");
        nc.setLastName("Smith");
        nc.setCompany("Sun");
        nc.setEmail("jimmy@smith.com");
        nc.setPhone("1112223333");
        dao.addContact(nc);
        // Create new contact
        Contact nc2 = new Contact();
        nc2.setFirstName("John");
        nc2.setLastName("Jones");
        nc2.setCompany("Apple");
        nc2.setEmail("john@jones.com");
        nc2.setPhone("5556667777");
        dao.addContact(nc2);
        // Create new contact - same last name as first 
        // contact but different company
        Contact nc3 = new Contact();
        nc3.setFirstName("Steve");
        nc3.setLastName("Smith");
        nc3.setCompany("Microsoft");
        nc3.setEmail("steve@msft.com");
        nc3.setPhone("5552221234");
        dao.addContact(nc3);
        // Create search criteria
        Map<SearchTerm, String> criteria = new HashMap<>();
        criteria.put(SearchTerm.LAST_NAME, "Jones");
        List<Contact> cList = dao.searchContacts(criteria);
        assertEquals(1, cList.size());
        assertEquals(nc2, cList.get(0));

        // New search criteria - look for Smith
        criteria.put(SearchTerm.LAST_NAME, "Smith");
        cList = dao.searchContacts(criteria);
        assertEquals(2, cList.size());

        // Add company to search criteria
        criteria.put(SearchTerm.COMPANY, "Sun");
        cList = dao.searchContacts(criteria);
        assertEquals(1, cList.size());
        assertEquals(nc, cList.get(0));

        // Change company to Microsoft, should get back nc3
        criteria.put(SearchTerm.COMPANY, "Microsoft");
        cList = dao.searchContacts(criteria);
        assertEquals(1, cList.size());
        assertEquals(nc3, cList.get(0));
        // Change company to Apple, should get back nothing
        criteria.put(SearchTerm.COMPANY, "Apple");
        cList = dao.searchContacts(criteria);
        assertEquals(0, cList.size());
    }
}