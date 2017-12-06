package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
@Table(name = "user")
public class User extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	@Column(name="emp_first_name")
	public String empFirstName;
	
	@Column(name="emp_last_name")
	public String empLastName;
	
	@Column(name="emp_id")
	public int empId;
	
	@Column(name="emp_email")
	public String empEmail;
	
	@Column(name="emp_password")
	public String empPassword;
	
	public static Finder<String,User> find = new Finder<String,User>(User.class);
}
