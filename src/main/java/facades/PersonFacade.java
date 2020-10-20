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
                //                "SELECT p FROM Phone p INNER JOIN e.person e where e.number" + phonenr + "", Person.class);

                "SELECT p FROM Phone p INNER JOIN p.person e WHERE e.number='" + phonenr + "'", Person.class);

        Person result = query.getSingleResult();
        return new PersonDTO(result);
    }

    public List<PersonDTO> getAllByHobby(int hobby) {
        EntityManager enf = emf.createEntityManager();
        TypedQuery<Person> query = enf.createQuery(
                //"SELECT p FROM Person p INNER JOIN p.hobbies h where h.name" + hobby + "", Person.class);
                "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='" + hobby + "'", Person.class);

        List<Person> result = query.getResultList();
        PersonDTO pdto = new PersonDTO();
        List<PersonDTO> listDTO = pdto.toDTO(result);
        return listDTO;
    }


    public PersonDTO editPerson(PersonDTO p) {
        EntityManager em = emf.createEntityManager();

        Person pFind = em.find(Person.class, p.getId());

        try {
            em.getTransaction().begin();
            pFind.setFirstName(p.getFirstName());
            pFind.setLastName(p.getLastName());
            pFind.setEmail(p.getEmail());
            em.getTransaction().commit();

        } finally {
            em.close();

        }
        return new PersonDTO(pFind);
    }

    public static void main(String[] args) {
        instance.getByPhone(11111112);

        Person p1 = new Person("cool@dude.yeah", "Niels", "Petersen");

        PersonDTO pD1 = new PersonDTO(p1);

    }
}
