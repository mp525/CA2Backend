/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOS;

import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
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
    private int phoneNr;
    private String phoneDisc;
    private List<PhoneDTO> phones;
    private String hobbyName;
    private List<HobbyDTO> hobbies;


    public PersonDTO() {
    }

    public PersonDTO(Person p) {
        this.id = p.getId();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.email = p.getEmail();
        this.street = p.getAddress().getStreet();
        this.houseNr = p.getAddress().getHouseNr();



        this.zip = p.getAddress().getCityInfo().getZipCode();  
        this.hobbies = new ArrayList();
        for (Hobby hobby : p.getHobbies()) {
            this.hobbies.add(new HobbyDTO(hobby));
        }
        this.phones = new ArrayList();
        for (Phone phone : p.getPhones()) {
            this.phones.add(new PhoneDTO(phone));
        }
        

    }

    public PersonDTO(String firstName, String lastName, String email, String street, String houseNr, String zip, String hobbyName, int phoneNr, String phoneDisc) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNr = houseNr;
        this.zip = zip;
        this.hobbyName = hobbyName;
        this.phoneNr = phoneNr;
        this.phoneDisc = phoneDisc;
    }

    public PersonDTO(String firstName, String lastName, String email, String street, String houseNr, String zip, List<HobbyDTO> list, List<PhoneDTO>list2) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNr = houseNr;
        this.zip = zip;
        this.hobbies = list;
        this.phones=list2;
    }


    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public int getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(int phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPhoneDisc() {
        return phoneDisc;
    }

    public void setPhoneDisc(String phoneDisc) {
        this.phoneDisc = phoneDisc;
    }
    
    
    
    
    
    public List<PersonDTO>toDTO(List<Person>persons){
        List<PersonDTO>dtoes = new ArrayList();
        for(Person p: persons){

            dtoes.add(new PersonDTO(p));
        }
        return dtoes;
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
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

    @Override
    public String toString() {
        return "PersonDTO{" + "id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", street=" + street + ", houseNr=" + houseNr + ", zip=" + zip + ", hobbyName=" + hobbyName + ", hobbies=" + hobbies + '}';
    }

}
