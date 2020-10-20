/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOS.PersonDTO;
import entities.Person;
import entities.Phone;
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
    public PersonDTO getByPhone(int phonenr){
        EntityManager enf= emf.createEntityManager();
        TypedQuery<Person> query = enf.createQuery(
        "SELECT p FROM Phone p INNER JOIN p.person e WHERE e.number='"+ phonenr +"'", Person.class);
        Person result = query.getSingleResult();
        return new PersonDTO(result);
    }
    
    public List<PersonDTO> getAllByHobby(int hobby){
        EntityManager enf= emf.createEntityManager();
        TypedQuery<Person> query = enf.createQuery(
        "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='"+hobby+"'", Person.class);
        List<Person> result = query.getResultList();
        PersonDTO pdto= new PersonDTO();
        List<PersonDTO>listDTO= pdto.toDTO(result);
    return listDTO;
    }
    
    public static void main(String[] args) {
        instance.getByPhone(11111112);
    }
    
    public int countWithGivenHobby(String hobbyName) {
        EntityManager em = emf.createEntityManager();
        try {
            int personCount = (int) em.createQuery(
            "SELECT COUNT(*) FROM PERSON JOIN HOBBY_PERSON ON HOBBY_PERSON.persons_ID = PERSON.ID WHERE HOBBY_PERSON.hobbies_NAME = '"+hobbyName+"'")
            .getSingleResult();
            return personCount;
        } finally {
            em.close();
        }
    }
    
    public PersonDTO deletePerson(int id) {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            System.out.println("Error, make exception!!");
        } else {
            try {
                em.getTransaction().begin();
                em.remove(person);
                //Delete all phone numbers associated with person
                List<Phone> phones = person.getPhones();
                for(Phone phone : phones) {
                    em.remove(phone);
                }
                
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }
        return new PersonDTO(person);
    }
}
