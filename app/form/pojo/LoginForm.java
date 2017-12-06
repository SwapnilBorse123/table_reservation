package form.pojo;

import org.mindrot.jbcrypt.BCrypt;

import models.User;
import play.Logger;
import play.data.validation.Constraints.Required;

public class LoginForm {
	
	@Required
	public String username;
	@Required
	public String password;
	

	public User authenticate() {
		User currentUser = User.find.where().eq("empEmail",this.username).findUnique();
		//Use this string to create new username and passwords.
		String hashedPwd = BCrypt.hashpw(this.password, BCrypt.gensalt());
		Logger.info("Hashed password: " + hashedPwd);
		if(currentUser!=null && BCrypt.checkpw(this.password, currentUser.empPassword)) {
        	Logger.info("User authenticated");
        	return currentUser;
        }else{
        	Logger.info("User Fails Authentication");
        	return null;
        }
	}
}
