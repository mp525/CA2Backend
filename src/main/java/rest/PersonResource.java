/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOS.PersonDTO;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import exceptions.MissingInputException;

import exceptions.HobbyNotFoundException;

import exceptions.PersonNotFoundException;
import facades.PersonFacade;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 * @author Mathias
 */
@Path("person")
public class PersonResource {


    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade FACADE = PersonFacade.getGMPFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    Gson g;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PersonResource
     */
    public PersonResource() {
    }

    /**
     * Retrieves representation of an instance of rest.PersonResource

*
     * @return an instance of java.lang.String
     */
    @Path("byhobby/{hobbyID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String getJson(@PathParam("hobbyID")String hobbyID) throws PersonNotFoundException {
        
        List<PersonDTO> p =FACADE.getAllByHobby(hobbyID);
       return new Gson().toJson(p);

       

    
    }

//    @PUT
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public String updatePerson(@PathParam("id") int id, String p) throws NullPointerException, PersonNotFoundException {
//        PersonDTO person = GSON.fromJson(p, PersonDTO.class);
//        
//        person.setId(id);
//        PersonDTO edited = FACADE.editPerson(person);
//        return GSON.toJson(edited);
//
//    }
    @Path("phone/{phone}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonByPhone(@PathParam("phone")int phone) throws PersonNotFoundException {
      PersonDTO p = FACADE.getByPhone(phone);
       
        return new Gson().toJson(p);


    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String show(){
        
        
        return "Shit work ma dude";
        
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPerson(String person) throws MissingInputException, HobbyNotFoundException{
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class);
        PersonDTO dto = FACADE.addPerson(personDTO);
        String json = GSON.toJson(dto);
        return json;
    }
    
    @Path("allWithZip/{zip}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String allWithZip(@PathParam("zip") String zip) throws MissingInputException, PersonNotFoundException{
        List<PersonDTO> list = FACADE.getAllByZip(zip);
        String json = GSON.toJson(list);
        return json;
    }
    
    @Path("countByHobby/{hobbyName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String countWithGivenHobby(@PathParam("hobbyName") String hobbyName) throws HobbyNotFoundException {
        int count = FACADE.countWithGivenHobby(hobbyName);
        //return GSON.toJson(count);
        return "{\"count\":" + count + "}";
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO person = FACADE.deletePerson(id);
        return GSON.toJson(person);
    }

    
}
