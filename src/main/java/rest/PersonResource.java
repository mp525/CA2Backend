/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import DTOS.PersonDTO;
import com.google.gson.Gson;
import facades.PersonFacade;
import java.util.List;
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

/**
 * REST Web Service
 *
 * @author Mathias
 */
@Path("person")
public class PersonResource {
    private static EntityManagerFactory emf;
        private static PersonFacade FACADE;
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
     * @param hobbyID
     * @return an instance of java.lang.String
     */
    @Path("byhobby/{hobbyID}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("hobbyID")String hobbyID) {
        
//        List<PersonDTO> p =FACADE.getAllByHobby(hobbyID);
//        return new Gson().toJson(p);

        return null;
    }
    @Path("id/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonByPhone(@PathParam("id")int id) {
//       PersonDTO p = FACADE.getByPhone(id);
//        
//        return new Gson().toJson(p);

        return null;
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String show(){
        
        
        return "Shit work ma dude";
        
    }

    
}
