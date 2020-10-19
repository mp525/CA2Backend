/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Mathias
 */
public class Tester {

    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("email1", "fornavn", "efternavn");
        Person p2 = new Person("email2", "navn", "navn2");
        Person p3 = new Person("email3", "navnet", "navnet2");

        Address a1 = new Address("Street", "info");
        Address a2 = new Address("street2", "info2");
        Address a3 = new Address("street3", "info3");
        
       
        
        a1.addPerson(p1);
        a1.addPerson(p2);
        a2.addPerson(p3);
        

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
//        p1.setAddress(a1);
//        p2.setAddress(a2);
//        p3.setAddress(a3);
//        try {
//            em.getTransaction().begin();
//            em.persist(p1);
//            em.persist(p2);
//            em.persist(p3);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
    }
}