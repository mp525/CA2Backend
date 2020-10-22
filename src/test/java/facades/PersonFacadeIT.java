/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOS.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.HobbyNotFoundException;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

/**
 *
 * @author matti
 */
public class PersonFacadeIT {

    private static EntityManagerFactory emf;

    private static PersonFacade facade;

    private Person p1;
    private Person p2;
    private Person p3;

    public PersonFacadeIT() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();
        facade = PersonFacade.getGMPFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Person("fornavn", "efternavn", "email1");
        p2 = new Person("navn", "navn2", "email2");
        p3 = new Person("navnet", "navnet2", "email3");

        Phone ph1 = new Phone(1, "Home");
        Phone ph2 = new Phone(11111112, "Home");
        Phone ph3 = new Phone(11111113, "Home");
        p1.addPhone(ph1);
        p2.addPhone(ph2);
        p2.addPhone(ph3);

        Hobby h1 = new Hobby("name", "wikilink", "categoy", "type");
        Hobby h2 = new Hobby("dnd", "wikilink", "categoy", "type");
        CityInfo cf = new CityInfo("2750", "Ballerup");
        Address a1 = new Address("Street", "2");
        Address a2 = new Address("street2", "3");
        Address a3 = new Address("street3", "4");
        a1.setCityInfo(cf);
        a2.setCityInfo(cf);
        a3.setCityInfo(cf);

        a1.addPerson(p1);
        a1.addPerson(p2);
        a2.addPerson(p3);
        p1.addHobby(h1);
        p1.addHobby(h2);
        p3.addHobby(h1);
        try {

            em.getTransaction().begin();
            em.createQuery("DELETE from Phone").executeUpdate();
            em.createQuery("DELETE from Person").executeUpdate();
            em.createQuery("DELETE from Hobby").executeUpdate();

            em.createQuery("DELETE from Address").executeUpdate();
            em.createQuery("DELETE from CityInfo").executeUpdate();

            em.persist(h1);
            em.persist(h2);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetByPhone() {

        PersonDTO exp = facade.getByPhone(1);
        String result = "fornavn";
        assertEquals(result, exp.getFirstName());

    }

    @Test
    public void testgetAllByHobby() {

        List<PersonDTO> exp = facade.getAllByHobby("dnd");
        String result = "fornavn";

        assertThat(exp, everyItem(hasProperty("email")));
        assertThat(exp, hasItems(
                Matchers.<PersonDTO>hasProperty("email", is("email1"))
        )
        );

    }

    @Test

    public void testShowZips() {
        List<String> lissy = facade.showAllZips();

        assertNotNull(lissy);
    }

    
    @Test
    public void testCountWithGivenHobby() throws HobbyNotFoundException {
        int res = facade.countWithGivenHobby("name");
        int res2 = facade.countWithGivenHobby("dnd");
        assertEquals(2, res);
        assertEquals(1, res2);
    }

    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        List<PersonDTO> listBefore = facade.getAllPersons();
        int listBefNum = listBefore.size();

        PersonDTO pDTO = facade.deletePerson(p3.getId());

        List<PersonDTO> listAfter = facade.getAllPersons();
        int listAftNum = listAfter.size();

        assertEquals(3, listBefNum);
        assertEquals(2, listAftNum);
    }

    @Test
    public void testGetAllPersons() {
        List<PersonDTO> persons = facade.getAllPersons();
        assertThat(persons, everyItem(hasProperty("firstName")));
        assertThat(persons, hasItems(
                Matchers.<PersonDTO>hasProperty("firstName", is("fornavn")),
                Matchers.<PersonDTO>hasProperty("firstName", is("navn")),
                Matchers.<PersonDTO>hasProperty("firstName", is("navnet"))
        ));

    }

    @Test
    public void testEditPersonDTO() throws PersonNotFoundException {
//        CityInfo ci1 = new CityInfo("2383", "Super City");
//        Address a1 = new Address("Cool Street", "35");
//        Hobby h1 = new Hobby("Dnd", "dnd.com", "Nørdstas", type)
//        a1.setCityInfo(ci1);
//        a1.addPerson(pEt);

        p1.setFirstName("Niels");
//        System.out.println("Her er Hobby data: " + p1.getHobbies());
        PersonDTO pd1 = new PersonDTO(p1);
//        System.out.println("Her er Hobby data: " + pd1.getHobbies());

        p1.setFirstName("Mandibles");
        PersonDTO pd2 = new PersonDTO(p1);
//        System.out.println("Here is the DTO" + pd1.toString());

        System.out.println("Her er DTO data: " + pd1);

//        System.out.println("Her er Hobby data: " + pd1.getHobbies().toString());

        pd1 = facade.editPerson(pd1);


        assertThat(pd1.getFirstName(), is(not(pd2.getFirstName())));
        assertEquals(pd1.getEmail(), pd2.getEmail());
        assertEquals(pd1.getLastName(), pd2.getLastName());
    }

//    @Test
//    public void testAddPerson(){
//        PersonDTO p = new PersonDTO(p1);
//        PersonDTO result = facade.addPerson(p);
//        assertEquals(p.getFirstName(), result.getFirstName());
//    }
    @Test
    public void testAddPerson() {
        PersonDTO p = new PersonDTO("fName", "lName", "mailbro", "streets", "numberhouse", "2750", "dnd", 21202120, "home");
        PersonDTO result = facade.addPerson(p);
        assertEquals(p.getFirstName(), result.getFirstName());
    }

    @Test
    public void testGetAllByZip() {
        List<PersonDTO> resultList = facade.getAllByZip("2750");
        System.out.println("All by zip: " + resultList);
        assertThat(resultList, everyItem(hasProperty("zip")));
        assertThat(resultList, hasItems( // or contains or containsInAnyOrder 
                Matchers.<PersonDTO>hasProperty("zip", is("2750")),
                Matchers.<PersonDTO>hasProperty("zip", is("2750")),
                Matchers.<PersonDTO>hasProperty("zip", is("2750"))
        )
        );
    }

}
