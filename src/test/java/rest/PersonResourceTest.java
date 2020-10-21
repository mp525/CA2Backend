/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package rest;
//
//import entities.Person;
//import io.restassured.RestAssured;
//import io.restassured.parsing.Parser;
//import java.net.URI;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.ws.rs.core.UriBuilder;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import utils.EMF_Creator;
//
///**
// *
// * @author Mathias
// */
//public class PersonResourceTest {
//   private static final int SERVER_PORT = 7777;
//    private static final String SERVER_URL = "http://localhost/api/";
//    private static Person p1, p2, p3;
//
//    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
//    private static HttpServer httpServer;
//    private static EntityManagerFactory emf;
//
//    static HttpServer startServer() {
//        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
//        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        //This method must be called before you request the EntityManagerFactory
//        EMF_Creator.startREST_TestWithDB();
//        emf = EMF_Creator.createEntityManagerFactoryForTest();
//
//        httpServer = startServer();
//        //Setup RestAssured
//        RestAssured.baseURI = SERVER_URL;
//        RestAssured.port = SERVER_PORT;
//        RestAssured.defaultParser = Parser.JSON;
//    }
//
//    @AfterAll
//    public static void closeTestServer() {
//        //System.in.read();
//        //Don't forget this, if you called its counterpart in @BeforeAll
//        EMF_Creator.endREST_TestWithDB();
//        httpServer.shutdownNow();
//    }
//
//    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//       
//        try {
//            em.getTransaction().begin();
//            
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//
//
//    
//}
