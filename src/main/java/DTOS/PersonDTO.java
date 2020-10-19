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

    public PersonDTO() {
    }

    public PersonDTO(Person p) {
        this.email = p.getEmail();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
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
