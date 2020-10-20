/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOS.PersonDTO;
import entities.Person;
import java.util.List;
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

    public PersonDTO getByPhone(int phonenr) {
        EntityManager enf = emf.createEntityManager();
        TypedQuery<Person> query = enf.createQuery(
                "SELECT p FROM Phone p INNER JOIN e.person e where e.number" + phonenr + "", Person.class);
        Person result = query.getSingleResult();
        return new PersonDTO(result);
    }

    public List<PersonDTO> getAllByHobby(int hobby) {
        EntityManager enf = emf.createEntityManager();
        TypedQuery<Person> query = enf.createQuery(
                "SELECT p FROM Person p INNER JOIN p.hobbies h where h.name" + hobby + "", Person.class);
        List<Person> result = query.getResultList();
        PersonDTO pdto = new PersonDTO();
        List<PersonDTO> listDTO = pdto.toDTO(result);
        return listDTO;
    }

    public PersonDTO editPerson(String firstName, String lastName, String email) {
        Person person = new Person(firstName, lastName, email);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }
}
