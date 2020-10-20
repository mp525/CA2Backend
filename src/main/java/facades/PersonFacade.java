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
import java.util.ArrayList;
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
    //Matti
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
                "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='" + hobby + "'", Person.class);

        List<Person> result = query.getResultList();

        PersonDTO pdto= new PersonDTO();
        listDTO= pdto.toDTO(result);
        }finally{
            enf.close();
        }
         
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
        return new PersonDTO(pFind);}
    
    
    
    public List<String> showAllZips() {
        EntityManager em = emf.createEntityManager();
        TypedQuery tqh = em.createQuery("Select z.zipCode from CityInfo z", String.class);
        List<String> zips = tqh.getResultList();
        
        return zips;
    }


    public PersonDTO addPerson(PersonDTO p) {
        EntityManager em = emf.createEntityManager();
        Person person = new Person(p.getFirstName(), p.getLastName(), p.getEmail());
        PersonDTO p2 = null;
        try {

            TypedQuery<CityInfo> query1 = em.createQuery("Select c from CityInfo c where c.zipCode = :zipcode", CityInfo.class);
            query1.setParameter("zipcode", p.getZip());
            CityInfo cityInfo = query1.getSingleResult();
            Address address = new Address(p.getStreet(), p.getHouseNr());
            address.setCityInfo(cityInfo);
            person.setAddress(address);

            TypedQuery<Hobby> query2 = em.createQuery("Select h from Hobby h where h.name = :name", Hobby.class);
            query2.setParameter("name", p.getHobbyName());
            Hobby hobby = query2.getSingleResult();
            person.addHobby(hobby);

            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            p2 = new PersonDTO(person);

        } finally {
            em.close();
        }
        return p2;

    }

    public static void main(String[] args) {
        instance.getByPhone(11111112);

        Person p1 = new Person("cool@dude.yeah", "Niels", "Petersen");
        PersonDTO pD1 = new PersonDTO(p1);
        Address a1 = new Address("Coolstreet", "342");
//                pD1.editPerson();
        

    }

    public int countWithGivenHobby(String hobbyName) {
        EntityManager em = emf.createEntityManager();
        try {
            int personCount = (int) em.createQuery(
                    "SELECT COUNT(*) FROM PERSON JOIN HOBBY_PERSON ON HOBBY_PERSON.persons_ID = PERSON.ID WHERE HOBBY_PERSON.hobbies_NAME = '" + hobbyName + "'")
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
                for (Phone phone : phones) {
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
