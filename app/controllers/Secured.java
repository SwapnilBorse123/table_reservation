package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.mvc.Security;
import models.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("emp_email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.LoginController.login());
    }
}