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
        String token = getTokenFromHeader(ctx);
        if (token != null) {
            User user = User.findByAuthToken(token);
            if (user != null) {
                return user.email;
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
    private String getTokenFromHeader(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get("Authorization");
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        }
        return null;
    }
}