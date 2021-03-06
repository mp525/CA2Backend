/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOS.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;

import java.util.ArrayList;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Mathias
 */
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api/";

    private static Person p1, p2, p3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("fornavn", "efternavn", "email1");
        p2 = new Person("navn", "navn2", "email2");
        p3 = new Person("navnet", "navnet2", "email3");
        Phone ph1 = new Phone(1, "Home");
        Phone ph2 = new Phone(11111112, "Home");
        Phone ph3 = new Phone(11111113, "Home");
        p1.addPhone(ph1);
        p2.addPhone(ph2);
        p2.addPhone(ph3);
        Hobby h1 = new Hobby("name", "wikilink", "categoy", "type");
        Hobby h2 = new Hobby("dnd", "wikilink", "categoy", "type");
        CityInfo cf = new CityInfo("2750", "Ballerup");
        Address a1 = new Address("Street", "2");
        Address a2 = new Address("street2", "3");
        Address a3 = new Address("street3", "4");
        a1.setCityInfo(cf);
        a2.setCityInfo(cf);
        a3.setCityInfo(cf);
        a1.addPerson(p1);
        a1.addPerson(p2);
        a2.addPerson(p3);
        p1.addHobby(h1);
        p1.addHobby(h2);
        p3.addHobby(h1);
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Phone").executeUpdate();
            em.createQuery("DELETE from Person").executeUpdate();
            em.createQuery("DELETE from Hobby").executeUpdate();
            em.createQuery("DELETE from Address").executeUpdate();
            em.createQuery("DELETE from CityInfo").executeUpdate();
            em.persist(h1);
            em.persist(h2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testAddPerson() {
        given()
                .contentType("application/json")
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "2750", "dnd", 21213030, "homephone"))
                .when()
                .post("person")
                .then()
                .body("firstName", equalTo("LaterPostBoy"))
                .body("lastName", equalTo("Jensen"))
                .body("id", notNullValue());
    }
    @Test
    public void testAddPersonErrorName() {
        String message;
        message = given()
                .contentType("application/json")
                .body(new PersonDTO("", "", "postemail", "nyby 22", "21a", "2750", "dnd", 21213030, "homephone"))
                .when()
                .post("person")
                .then()
                .extract().body().jsonPath().getString("message");
        assertThat(message, is("First Name and/or Last Name is missing!"));
    }
    
    @Test
    public void testEditPerson() {
        
    }
    
    @Test
    public void testAddPersonErrorZip() {
        String message;
        message = given()
                .contentType("application/json")
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "1", "dnd", 21213030, "homephone"))
                .when()
                .post("person")
                .then()
                .extract().body().jsonPath().getString("message");
        assertThat(message, is("Zipcode was not of appropriate length of 3 or 4 digits!"));
    }
    @Test
    public void testAddPersonErrorPhoneNr() {
        String message;
        message = given()
                .contentType("application/json")
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "1234", "dnd", 0, "homephone"))
                .when()
                .post("person")
                .then()
                .extract().body().jsonPath().getString("message");
        assertThat(message, is("Phonenumber is missing!"));
    }
    @Test
    public void testAddPersonErrorHobby() {
        String message;
        message = given()
                .contentType("application/json")
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "1234", "notahobby", 99999999, "homephone"))
                .when()
                .post("person")
                .then()
                .extract().body().jsonPath().getString("message");
        assertThat(message, is("Could not find hobby by the name: notahobby in database"));
    }
    @Test
    public void testAddPersonErrorDupePhone() {
        String message;
        message = given()
                .contentType("application/json")
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "1234", "dnd", 1, "homephone"))
                .when()
                .post("person")
                .then()
                .extract().body().jsonPath().getString("message");
        assertThat(message, is("Phone number 1 already exists in database!"));
    }
    


    @Test
    public void testGetAllByZip() {
        given()
                .pathParam("zip", "2750")
                .get("person/allWithZip/{zip}")
                .then()
                .assertThat()
                .body("zip[0]", equalTo("2750"))
                .assertThat()
                .body("zip[1]", equalTo("2750"))
                .assertThat()
                .body("zip[2]", equalTo("2750"));
    }


    
    @Test
    public void testCountByHobby() {
        given()
                .get("/person/countByHobby/dnd")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(1));
    }

    @Test
    public void testFindByPhone() {
         given()
         .get("person/phone/1")
         .then()
         .assertThat()
         .body("firstName", equalTo("fornavn"));      
    }
//person/byhobby/Animation
    @Test
    public void testFindbyhobby() {
          List<PersonDTO> personsDTOs;
        personsDTOs = given()
                .contentType("application/json")
                .when()
                .get("person/byhobby/dnd")
                .then()
                .extract().body().jsonPath().getList("",PersonDTO.class);
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        System.out.println("______");
        System.out.println("What i get "+personsDTOs.get(0));
        PersonDTO p=personsDTOs.get(0);
        System.out.println("What i want "+p1DTO);
        assertEquals(p.getEmail(),p1DTO.getEmail());
        assertEquals(p.getLastName(),p1DTO.getLastName());
        assertEquals(p.getFirstName(),p1DTO.getFirstName());
    }
    
    @Test
    public void testDeletePerson() {
//        PersonDTO x = given()
//                .pathParam("id", p1.getId())
//                .delete("/person/{id}")
//                .then()
//                .extract().body().jsonPath();
//        String x = given()
//                .contentType("application/json")
//                .body(new PersonDTO(p1))
//                .when()
//                .pathParam("id", p1.getId())
//                .delete("/person/{id}")
//                .then()
//                .extract().body().jsonPath().getString(p1.toString());
//        assertEquals(x, p1.toString());
        
        String y;
        y = given()
                .contentType("application/json")
                .pathParam("id", p1.getId())
                .delete("person/{id}")
                .then()
                .extract().body().jsonPath().getString("firstName"); //getString tager kolonne/feltnavn! Hvis flere har samme navn, bliver det et array
        System.out.println(y);
        assertEquals(p1.getFirstName(), y);
        /*
                given()
                .contentType("application/json")//String firstName, String lastName, String email, String street, String houseNr, String zip, String hobbyName, int phoneNr, String phoneDisc
                .body(new PersonDTO("LaterPostBoy", "Jensen", "postemail", "nyby 22", "21a", "2750", "dnd", 21213030, "homephone"))
                .when()
                .post("person")
                .then()
                .body("firstName", equalTo("LaterPostBoy"))
                .body("lastName", equalTo("Jensen"))
                .body("id", notNullValue());
                */
    }

}
