package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Vibeke
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    private List<Phone> phones;
    
    @ManyToMany(mappedBy = "persons", cascade = CascadeType.PERSIST)
    private List<Hobby> hobbies;

    public Person() {
    }

    public Person(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = new ArrayList();
        this.hobbies = new ArrayList();
    }
    
    public void addHobby(Hobby hobby){
            if(hobby != null){
              this.hobbies.add(hobby);
            hobby.getPersons().add(this);
            }
            
            
        }
        public void removeHobby(Hobby hobby){
            if(hobby != null){
                hobbies.remove(hobby);
                hobby.getPersons().remove(this);
            }
        }
    
     public List<Phone> getPhones() {
        return phones;
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
        if(phone != null){
            phone.setPerson(this);
        }
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
//        if (address != null) {
//            address.addPerson(this);
//        }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Person[ id=" + id + " ]";
    }
    
}
