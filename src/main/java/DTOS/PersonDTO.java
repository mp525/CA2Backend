/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOS;

import entities.Person;
import java.util.List;

/**
 *
 * @author matti
 */
public class PersonDTO {
     private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String street;
    private String houseNr;
    private String zip;
    private String hobbyName;

    public PersonDTO() {
    }

    public PersonDTO(Person p) {
        this.email = p.getEmail();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.street = p.getAddress().getStreet();
        this.houseNr = p.getAddress().getHouseNr();
        this.zip = p.getAddress().getCityInfo().getZipCode();  
        
    }

    public PersonDTO(String firstName, String lastName, String email, String street, String houseNr, String zip, String hobbyName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNr = houseNr;
        this.zip = zip;
        this.hobbyName = hobbyName;
    }
    
    
    public List<PersonDTO>toDTO(List<Person>persons){
        List<PersonDTO>dtoes = null;
        for(Person p: persons){
            dtoes.add(new PersonDTO(p));
        }
         return dtoes;        
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }
    
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
