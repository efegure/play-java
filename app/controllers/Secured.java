package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get("Authorization");
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            User user = models.User.findByAuthToken(authTokenHeaderValues[0]);
            if (user != null) {
                ctx.args.put("user", user);
                return ctx.session().get("email");
            }
        }

        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
    	ObjectNode response =Json.newObject();
		response.put("Error", "Unauthorizated user access.");
		return ok(response);
    }
}