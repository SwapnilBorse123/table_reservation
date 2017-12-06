package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import akka.japi.Util;
import form.pojo.LoginForm;
import form.pojo.ReservationForm;
import form.utils.PantryView;
import form.utils.TimeFormatter;
import form.utils.UtilFunctions;
import models.Employee;
import models.PantryMapping;
import models.Reservation;
import models.User;
import play.data.Form;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.*;
import play.mvc.Http.Session;
import util.AppConstants;
import views.html.*;
import views.html.helper.form;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@play.mvc.Security.Authenticated(Secured.class)
public class HomeController extends Controller {

	public Result login(){
		return ok(login.render(Form.form(LoginForm.class),"Login"));
	}
	
    public Result index() {
    	Session session = Http.Context.current().session();
    	if(session.get("emp_email")!=null){
    		Logger.info("User in Session: "+session.get("emp_name"));
    		return ok(index.render("Your new application is ready."));
    	}else{
    		Logger.info("User in Session: "+session.get("emp_name"));
    		return ok(login.render(Form.form(LoginForm.class),"Login"));
    	}
    }
    
    public Result display(){
    	List<Reservation> resList = new ArrayList<>();
    	resList = Reservation.find.where().eq("dTag", "N").findList();
    	return ok(reservationHistory.render("Reservation History",resList));
	}
    
}
