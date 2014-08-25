package org.openshift.webservice;

import org.openshift.model.*;
import org.openshift.model.Character;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;

/**
 * Created by spousty on 8/22/14.
 */

@Path("/char")
public class Generator {

    private org.openshift.model.Character character = new Character();

    @GET()
    @Produces("text/json")
    public HashMap MakeACharacter(){
        return character.getAllAttributes();
    }

}