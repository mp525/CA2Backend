/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTOS.HobbyDTO;
import DTOS.PersonDTO;
import DTOS.PhoneDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.PersonNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;

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
    public PersonDTO getByPhone(int phonenr) throws NotFoundException {
        EntityManager enf = emf.createEntityManager();
        Person p;
        try {
            TypedQuery<Person> query = enf.createQuery(
                    "SELECT p.person FROM Phone p INNER JOIN p.person pers WHERE p.number='" + phonenr + "'", Person.class);

            p = query.getSingleResult();
        } finally {
            enf.close();
        }

        System.out.println(p);
        System.out.println(p.getAddress().getStreet() + p.getAddress().getHouseNr());

        List<HobbyDTO> list2 = new ArrayList();
        for (Hobby h : p.getHobbies()) {
            list2.add(new HobbyDTO(h));
        }
        List<PhoneDTO> list3 = new ArrayList();
        for (Phone ph : p.getPhones()) {
            list3.add(new PhoneDTO(ph));
        }
        return new PersonDTO(p.getFirstName(), p.getLastName(), p.getEmail(), p.getAddress().getStreet(), p.getAddress().getHouseNr(), p.getAddress().getCityInfo().getZipCode(), list2,list3);

    }

    public List<PersonDTO> getAllByHobby(String hobby) throws NotFoundException {
        EntityManager enf = emf.createEntityManager();
        List<PersonDTO> listDTO;
        try {

            TypedQuery<Person> query = enf.createQuery(
                    "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='" + hobby + "'", Person.class
            );

            List<Person> p = query.getResultList();
            System.out.println("Get all hobby: her fra");
            System.out.println(p);

            PersonDTO pdto = new PersonDTO();
            listDTO = pdto.toDTO(p);
            System.out.println(listDTO);
            System.out.println("her til");
        } finally {
            enf.close();
        }

        return listDTO;
    }

    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
//              TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p WHERE p.id =:" + p.getId() + "", Person.class);
        Person pFind = em.find(Person.class, p.getId());
        if (pFind == null) {
            throw new PersonNotFoundException(String.format("Person not found, so they coulden't be edited", pFind.toString()));
        } else {

            try {
                em.getTransaction().begin();
                pFind.setFirstName(p.getFirstName());
                pFind.setLastName(p.getLastName());
                pFind.setEmail(p.getEmail());
                Address a1 = new Address(p.getStreet(), p.getHouseNr());
                pFind.getAddress().removePerson(pFind);
                if (pFind.getAddress().getPersons().isEmpty()) {
                    em.remove(pFind.getAddress());
                }
                a1.addPerson(pFind);
                pFind.setAddress(a1);
                TypedQuery<Hobby> query2 = em.createQuery("Select h from Hobby h where h.name = :name", Hobby.class);
                query2.setParameter("name", p.getHobbyName());
                Hobby hobby = query2.getSingleResult();
                List<Hobby> hop = pFind.getHobbies();
                hop.add(hobby);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }
        return new PersonDTO(pFind);
    }

    public List<String> showAllZips() {
        EntityManager em = emf.createEntityManager();
        TypedQuery tqh = em.createQuery("Select z.zipCode from CityInfo z", String.class);
        List<String> zips = tqh.getResultList();

        return zips;
    }

    public List<PersonDTO> getAllByZip(String zip) {
        EntityManager em = emf.createEntityManager();
        List<PersonDTO> persons = null;

        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p "
                    + "JOIN p.address a WHERE a.cityInfo.zipCode = :zip", Person.class);
            query.setParameter("zip", zip);
            List<Person> list = query.getResultList();
            PersonDTO dto = new PersonDTO();
            persons = dto.toDTO(list);
        } finally {
            em.close();
        }

        return persons;
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
            System.out.println(p.getHobbyName());
            query2.setParameter("name", p.getHobbyName());
            Hobby hobby = query2.getSingleResult();
            System.out.println("hej");
            person.addHobby(hobby);

            Phone phone = new Phone(p.getPhoneNr(), p.getPhoneDisc());
            person.addPhone(phone);

//            TypedQuery<Phone> query3 = em.createQuery("Select p from Phone p where p.person.id = :id", Phone.class);
//            query3.setParameter("id", person.getId());
//            List<Phone> phones = query3.getResultList();
//            
//            for (PhoneDTO pdto : p.getPhones()) {
//                person.addPhone(new Phone(pdto.getNumber(), pdto.getDescription()));
//            }
            em.getTransaction().begin();
            em.persist(person);

//            TypedQuery<Phone> query3 = em.createQuery("Select p from Phone p where p.person.id = :id", Phone.class);
//            query3.setParameter("id", person.getId());
//            List<Phone> phones = query3.getResultList();
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

    public List<PersonDTO> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery tq = em.createQuery("SELECT p FROM Person p", Person.class);
            List<PersonDTO> list = tq.getResultList();
            return list;
        } finally {
            em.close();
        }
    }

}
