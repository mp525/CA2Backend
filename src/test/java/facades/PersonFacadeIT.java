/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOS.PersonDTO;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
    public PersonFacadeIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        EntityManager em = emf.createEntityManager();
        facade=PersonFacade.getGMPFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        Person p1 = new Person("email1", "fornavn", "efternavn");
        Person p2 = new Person("email2", "navn", "navn2");
        Person p3 = new Person("email3", "navnet", "navnet2");
        Phone ph1 = new Phone(1, "Home");
        Phone ph2 = new Phone(11111112, "Home");
        Phone ph3 = new Phone(11111113, "Home");
        p1.addPhone(ph1);
        p2.addPhone(ph2);
        p2.addPhone(ph3);
        Hobby h1 = new Hobby("name", "wikilink", "categoy", "type");
        Hobby h2 = new Hobby("dnd", "wikilink", "categoy", "type");
        
        p1.addHobby(h1);
        p1.addHobby(h2);
        p3.addHobby(h1);
         try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Phone").executeUpdate();
            em.createQuery("DELETE from Address").executeUpdate();
            em.createQuery("DELETE from Person").executeUpdate();
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
    public void testGetByPhone(){
        
       PersonDTO exp=facade.getByPhone(1);
        String result="fornavn";
        assertEquals(result,exp.getFirstName());
        
    }
    @Test
    public void testgetAllByHobby(){
        
//        List<PersonDTO> exp=facade.getAllByHobby("dnd");
//        String result="fornavn";
//         
//        assertThat(exp, everyItem(hasProperty("email")));
//        assertThat(exp, hasItems( 
//                Matchers.<PersonDTO>hasProperty("email", is("email1"))
//                
//        )
//        );
        
    }
    
    @Test
    public void testCountWithGivenHobby() {
        
    }
}
