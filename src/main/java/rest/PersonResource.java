/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOS.PersonDTO;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
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
    @Path("/allbyhobby/{hobbyID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {

        return null;

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updatePerson(@PathParam("id") int id, String p) throws NullPointerException {
        PersonDTO person = GSON.fromJson(p, PersonDTO.class);
        
        person.setId(id);
        PersonDTO edited = FACADE.editPerson(person.getFirstName(), person.getLastName(), person.getEmail());
        return GSON.toJson(edited);
    }

    /**
     * PUT method for updating or creating an instance of PersonResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
