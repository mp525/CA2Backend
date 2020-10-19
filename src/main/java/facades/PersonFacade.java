/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Person;
import entities.Phone;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Mathias
 */
public class PersonFacade {
     private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {

    }

    public static PersonFacade getGMPFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
}
    public int getPersonIDByPhone(int phone) {
        EntityManager em=emf.createEntityManager();
    TypedQuery<Phone> query = em.createQuery(
        "SELECT p FROM Phone p WHERE p.number = '" + phone + "'",
        Phone.class);
    return query.getSingleResult().getNumber();
  }
    public Person getPersonByPhone(int phone){
        EntityManager em=emf.createEntityManager();
        int id = getPersonIDByPhone(phone);
    TypedQuery<Person> query = em.createQuery(
        "SELECT p FROM Person p WHERE p.id = '" + id + "'",
        Person.class);
    return query.getSingleResult();
  }
}
