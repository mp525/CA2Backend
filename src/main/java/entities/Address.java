/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author SJUBER
 */
@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String additionalInfo;
    
    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private List<Person> persons;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CityInfo cityInfo;

    public Address(String street, String additionalInfo) {
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.persons = new ArrayList();
    }

    public Address() {
    }
    
    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
//        if (address != null) {
//            address.addPerson(this);
//        }
    }

      public List<Person> getPersons() {
        return persons;
    }

     public void addPerson(Person person) {
        this.persons.add(person);
        if(person != null){
            person.setAddress(this);
        }
    }
     
     public void removePerson(Person person){
            if(person != null){
                persons.remove(person);
            }
        }
    
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    
}
