package controllers;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import form.pojo.LoginForm;
import form.utils.UtilFunctions;
import models.User;
import play.mvc.Controller;
import play.mvc.Http.Session;
import util.AppConstants;
import play.mvc.*;
import play.data.Form;
import views.html.*;
import play.Logger;

//@play.mvc.Security.Authenticated(Secured.class)
public class LoginController extends Controller{
	
	public Result login() {
		return ok(login.render(Form.form(LoginForm.class),""));
	}
	
	public Result authenticate() {
		Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
	    if (loginForm.hasErrors()) {
	    	Logger.info("Form has errors...");   	
	        return badRequest(login.render(loginForm,""));
	    }else {
	    	LoginForm userDetails=loginForm.get();
	    	User user=userDetails.authenticate();
	    	if(user!=null){
	    		session("emp_name", user.empFirstName+" "+user.empLastName);
	    		session("emp_email",user.empEmail);
	    		return redirect(routes.HomeController.index());
	    	}else{
	    		flash(AppConstants.FLASHERROR,"Wrong username or password");
	    		return badRequest(login.render(loginForm,"false"));	
	    	}
	    }
	}
	
	public Result logout() {
		Session session = Http.Context.current().session();
		session.clear();
		Logger.info("Logout function called..");
		return ok(login.render(Form.form(LoginForm.class),""));
	}
	
	public Result changePassword(){
		Map<String, String> map = Form.form().bindFromRequest().get().getData();
		String oldPass = map.get("oldPassword");
		String newPass = map.get("newPassword");
		String conNewPass = map.get("conNewPassword");
		if(oldPass.equalsIgnoreCase("") ||
				newPass.equalsIgnoreCase("")||
					conNewPass.equalsIgnoreCase("")){
			flash(AppConstants.FLASHERROR,"Please fill all fields.");
			return redirect("/");
		}else if(!conNewPass.equalsIgnoreCase(newPass)){
			flash(AppConstants.FLASHERROR,"New and confirm new passwords don't match.");
			return redirect("/");
		}
		User user = UtilFunctions.getCurrentUser();
		// check if the old password is same
		if(!BCrypt.checkpw(oldPass, user.empPassword)){
			flash(AppConstants.FLASHERROR,"Old password incorrect");
			return redirect("/");
		}else{
			String newHashedPass = BCrypt.hashpw(newPass, BCrypt.gensalt());
			user.empPassword = newHashedPass;
			user.update();
			flash(AppConstants.FLASHSUCCESS,"Password changed successfully.");
			return redirect("/");
		}
	}
	
}
