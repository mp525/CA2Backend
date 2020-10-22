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
import exceptions.HobbyNotFoundException;
import exceptions.MissingInputException;
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
    public PersonDTO getByPhone(int phonenr) throws PersonNotFoundException {
        EntityManager enf = emf.createEntityManager();
        Person p;
        try {
            TypedQuery<Person> query = enf.createQuery(
                    "SELECT p.person FROM Phone p INNER JOIN p.person pers WHERE p.number='" + phonenr + "'", Person.class);

            List<Person>plist = query.getResultList();
            if(plist.isEmpty()){
                throw new PersonNotFoundException("Personen var ikke fundet");
            }else{
                p=plist.get(0);
            }
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
        return new PersonDTO(p.getFirstName(), p.getLastName(), p.getEmail(), p.getAddress().getStreet(), p.getAddress().getHouseNr(), p.getAddress().getCityInfo().getZipCode(), list2, list3);

    }

    public List<PersonDTO> getAllByHobby(String hobby) throws PersonNotFoundException {
        EntityManager enf = emf.createEntityManager();
        List<PersonDTO> listDTO;
        try {

            TypedQuery<Person> query = enf.createQuery(
                    "SELECT p FROM Person p INNER JOIN p.hobbies h WHERE h.name='" + hobby + "'", Person.class
            );

            List<Person> p = query.getResultList();
            if(p.isEmpty()){
                throw new PersonNotFoundException("ingen personer var fundet");
            }
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
        System.out.println("xxxxxxxxxxxxxxxxxxx");
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

                a1.setStreet(p.getStreet());
                a1.setHouseNr(p.getHouseNr());
                CityInfo ci1 = em.find(CityInfo.class, p.getZip());


                a1.setCityInfo(ci1);
                a1.getCityInfo().getZipCode();
//                p.getAddress().getCityInfo().getZipCode()

                a1.addPerson(pFind);
                pFind.setAddress(a1);


                Phone ph1 = new Phone(p.getPhoneNr(), p.getPhoneDisc());
                    
//                TypedQuery tq = em.createQuery("Select p from Phone p where p.person.id = :id\", Phone.class");
                
                pFind.addPhone(ph1);
                if (pFind.getPhones().contains(ph1.getNumber())) {
                    pFind.removePhone(ph1);
                }

                //Hobby data
//                List<HobbyDTO> hobbyDTOList = p.getHobbies();
//                if (hobbyDTOList == null) {
//                    hobbyDTOList = new ArrayList<HobbyDTO>();
//                }
//                List<Hobby> hobbyList = new ArrayList<Hobby>();
                Hobby hobbyFound = em.find(Hobby.class, p.getHobbyName());
    

                if (!pFind.getHobbies().contains(hobbyFound)) {

                    pFind.addHobby(hobbyFound);
                }

//                    hobbyList.add(hobbyFound);
//                pFind.setHobbies(hobbyList);
                em.persist(pFind);
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

    public List<PersonDTO> getAllByZip(String zip) throws MissingInputException, PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        if (zip.length() < 3 || zip.length() > 4) {
            throw new MissingInputException("Zipcode was not of appropriate length of 3 or 4 digits!");
        }
        List<PersonDTO> persons = null;

        try {
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p "
                    + "JOIN p.address a WHERE a.cityInfo.zipCode = :zip", Person.class);
            query.setParameter("zip", zip);
            List<Person> list = query.getResultList();
            if (list.isEmpty()) {
                throw new PersonNotFoundException(String.format("Could not find zipcode %s in database", zip));
            }
            PersonDTO dto = new PersonDTO();
            persons = dto.toDTO(list);
        } finally {
            em.close();
        }

        return persons;
    }

    public PersonDTO addPerson(PersonDTO p) throws MissingInputException, HobbyNotFoundException {
        EntityManager em = emf.createEntityManager();
        if (nameInvalid(p)) {
            throw new MissingInputException("First Name and/or Last Name is missing!");
        }
        if (zipInvalid(p)) {
            throw new MissingInputException("Zipcode was not of appropriate length of 3 or 4 digits!");
        }
        if (phoneNrInvalid(p)) {
            throw new MissingInputException("Phonenumber is missing!");
        }
        Person person = new Person(p.getFirstName(), p.getLastName(), p.getEmail());
        PersonDTO p2 = null;
        try {
            CityInfo cityInfo = em.find(CityInfo.class, p.getZip());
            Address address = new Address(p.getStreet(), p.getHouseNr());
            address.setCityInfo(cityInfo);
            person.setAddress(address);

            Hobby hobby = em.find(Hobby.class, p.getHobbyName());
            if (hobby == null) {
                throw new HobbyNotFoundException(String.format("Could not find hobby by the name: %s in database", p.getHobbyName()));
            }
            person.addHobby(hobby);
            Phone phone = new Phone(p.getPhoneNr(), p.getPhoneDisc());
            TypedQuery<Phone> query = em.createQuery("select p from Phone p where p.number = :number", Phone.class);
            query.setParameter("number", p.getPhoneNr());
            List<Phone> listP = query.getResultList();
            if (!listP.isEmpty()) {
                throw new MissingInputException(String.format("Phone number %s already exists in database!", p.getPhoneNr()));
            }
            person.addPhone(phone);

            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            p2 = new PersonDTO(person);

        } finally {
            em.close();
        }
        return p2;

    }


    private static boolean phoneNrInvalid(PersonDTO p) {
        return p.getPhoneNr() <= 0;
    }

    private static boolean zipInvalid(PersonDTO p) {
        return p.getZip().length() < 3 || p.getZip().length() > 4;
    }


    private static boolean nameInvalid(PersonDTO p) {
        return p.getFirstName().length() == 0 || p.getLastName().length() == 0;
    }

    public static void main(String[] args) {
//        instance.getByPhone(11111112);
//
//        Person p1 = new Person("cool@dude.yeah", "Niels", "Petersen");
//        PersonDTO pD1 = new PersonDTO(p1);
//        Address a1 = new Address("Coolstreet", "342");
//                pD1.editPerson();

    }

    public int countWithGivenHobby(String hobbyName) throws HobbyNotFoundException {
        EntityManager em = emf.createEntityManager();

        Hobby hobby = em.find(Hobby.class, hobbyName);
        if (hobby == null) {
            throw new HobbyNotFoundException(String.format("Could not find hobby by the name: %s in database", hobbyName));
        }
        List<Person> list = hobby.getPersons();
        int count = list.size();
        return count;


    }

    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, id);
        if (person == null) {
            throw new PersonNotFoundException(String.format("Could not delete, id: %d not found", id));
        } else {
            try {
                em.getTransaction().begin();
                //Delete all phone numbers associated with person
                List<Phone> phones = person.getPhones();
                for (Phone phone : phones) {
                    em.remove(phone);
                }

                //Delete address if no one else lives there - Help!!
//                List<Person> x = person.getAddress().getPersons();
//                if(x.size() == 1) {
//                    em.remove(person.getAddress());
//                }
                person.getAddress().removePerson(person);
                if (person.getAddress().getPersons().isEmpty()) {
                    em.remove(person.getAddress());
                }

                em.remove(person);
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
