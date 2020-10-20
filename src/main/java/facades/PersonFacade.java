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
    public PersonDTO getByPhone(int phonenr){
        EntityManager enf= emf.createEntityManager();
        Person result;
        try{
        TypedQuery<Person> query = enf.createQuery(
        "SELECT p.person FROM Phone p INNER JOIN p.person pers WHERE p.number='"+ phonenr +"'", Person.class);
        result = query.getSingleResult();
        }finally{
            enf.close();
        }
         
        return new PersonDTO(result);
    }
    
    public List<PersonDTO> getAllByHobby(String hobby){
        EntityManager enf= emf.createEntityManager();
        List<PersonDTO> listDTO;
        try{
        TypedQuery<Person> query = enf.createQuery(
        "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='"+hobby+"'", Person.class);
        List<Person> result = query.getResultList();
        PersonDTO pdto= new PersonDTO();
        listDTO= pdto.toDTO(result);
        }finally{
            enf.close();
        }
         
    return listDTO;
    }
   
}
