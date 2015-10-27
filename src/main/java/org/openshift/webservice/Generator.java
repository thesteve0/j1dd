package org.openshift.webservice;

import org.openshift.model.*;
import org.openshift.model.Character;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;

/**
 * Created by spousty on 8/22/14.
 */

@Path("/char")
public class Generator {

    private org.openshift.model.Character character = new Character();

    @Context
    private SecurityContext securityContext;

    @GET()
    @Produces("text/json")
    public HashMap MakeACharacter(){
        return character.getAllAttributes();
    }

    @GET()
    @Produces("text/json")
    @Path("{name}")
    public HashMap MakeACharacterWithAName(@PathParam("name") String name){

        character.setName(name);
        return character.getAllAttributes();

    }

    @GET()
    @Produces("text/json")
    @Path("dd")
    public HashMap MakeACharacterForMongo(){

        //TODO get the userid from keycloak here and use it for the name
        String name = securityContext.getUserPrincipal().getName();
        character.setName(name);
        //TODO now send the character on to the Mongo Service
        Client requester = ClientBuilder.newClient();

        /*

        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/Client.html
        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/WebTarget.html
        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/Invocation.Builder.html
         */
        return character.getAllAttributes();

    }

}
