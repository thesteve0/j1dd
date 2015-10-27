package org.openshift.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.util.JsonSerialization;
import org.openshift.model.*;
import org.openshift.model.Character;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
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
    @Produces("application/json")
    public HashMap MakeACharacter(){
        return character.getAllAttributes();
    }

    @GET()
    @Produces("application/json")
    @Path("{name}")
    public HashMap MakeACharacterWithAName(@PathParam("name") String name){

        character.setName(name);
        return character.getAllAttributes();

    }

    @GET()
    @Produces("application/json")
    @Path("dd")
    public String MakeACharacterForMongo(@Context HttpServletRequest request) throws Exception{

        ///get the userid from keycloak here and use it for the name
        String name = securityContext.getUserPrincipal().getName();
        character.setName(name);

        // turn the character to JSON


        //now send the character on to the Mongo Service
        //Get the token
        KeycloakSecurityContext mySecurityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        String token = mySecurityContext.getTokenString();

        //build the POST

        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost("https://j1mongo-thesteve0.rhcloud.com/ws/players");
            post.addHeader("Authorization", "Bearer " + token);
            post.addHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(JsonSerialization.writeValueAsString(character.getAllAttributes())));
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("request failed for some non-helpful reason");
                }

                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                try {
                    return org.apache.commons.io.IOUtils.toString(is);
                } finally {
                    is.close();
                }

        } finally {
            client.getConnectionManager().shutdown();
        }
        /*
        Client client = ClientBuilder.newClient();
        WebTarget resourceTarget = client.target("https://j1mongo-thesteve0.rhcloud.com/ws/players");
        Invocation.Builder invocationBuilder = resourceTarget.request("application/json").header("Authorization", "bearer " + token);
        Invocation invocation = invocationBuilder.buildPost(Entity.json(character));
        Response response = invocation.invoke();
        */
        //System.out.println("response code: " + response.getStatus());

        /*

        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/Client.html
        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/WebTarget.html
        https://jax-rs-spec.java.net/nonav/2.0-SNAPSHOT/apidocs/javax/ws/rs/client/Invocation.Builder.html
         */
        //return character.getAllAttributes();

    }

}
