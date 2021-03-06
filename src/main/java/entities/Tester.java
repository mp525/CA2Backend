
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package entities;
//
//import DTOS.PersonDTO;
//import exceptions.PersonNotFoundException;
//
//import facades.PersonFacade;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import utils.EMF_Creator;
//
///**
// *
// * @author Mathias
// */
//public class Tester {
//
//    public static void main(String[] args) throws PersonNotFoundException {

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import DTOS.PersonDTO;
import exceptions.PersonNotFoundException;

import facades.PersonFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Mathias
 */
public class Tester {

    public static void main(String[] args) throws PersonNotFoundException {



        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();




        Person p1 = new Person("email1", "fornavn", "efternavn");
        Person p2 = new Person("email2", "navn", "navn2");
        Person p3 = new Person("email3", "navnet", "navnet2");
        CityInfo cf = new CityInfo("2750","Ballerup");

        Address a1 = new Address("Street", "info");
        Address a2 = new Address("street2", "info2");
        Address a3 = new Address("street3", "info3");
        
        a1.addPerson(p1);
        a1.addPerson(p2);
        a2.addPerson(p3);
        
        Phone ph1 = new Phone(11111111, "Home");
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




 

        //Person p1 = new Person("email1", "fornavn", "efternavn");


//String email, String firstName, String lastName, String street, String houseNr, int zip, String hobbyName
//        PersonDTO dto1 = new PersonDTO("Firstname2", "lastname2", "mail@cool.com", "Street2", "houseNr2", "1011", "Animation", 12374933,"Home");
//        PersonDTO dtol2 = new PersonDTO("Firstname3", "lastname3", "mail@cool.com", "Street3", "houseNr2", "1011", "Animation", 12374933,"Home");
        PersonFacade facade = PersonFacade.getGMPFacade(emf);
//        facade.addPerson(dto1);

//        facade.editPerson(dtol2);

         a1.setCityInfo(cf);
        a2.setCityInfo(cf);
        a3.setCityInfo(cf);

        p1.setAddress(a1);
        p2.setAddress(a2);
        p3.setAddress(a3);
        try {
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }



        // facade.deletePerson(dto1.getId());
        //facade.deletePerson(dto3.getId());
        //dto2.setStreet("Teststreet");
        //facade.editPerson(dto2);
        p1.setAddress(a1);
        p2.setAddress(a2);
        p3.setAddress(a3);
        try {
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }




    }
}

