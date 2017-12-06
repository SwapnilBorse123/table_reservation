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
@Table(name = "reservation")
public class Reservation extends Model{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	@Column(name = "conf_name")
	public String confName;
	
	@Column(name = "emp_name")
	public String empName;
	
	@Column(name = "emp_id")
	public int empId;
	
	@Column(name = "start_time")
	public long startTime;
	
	@Column(name = "end_time")
	public long endTime;
	
	@Column(name = "d_tag")
	public String dTag;
	
	public static Finder<Long, Reservation> find = new Finder<Long,Reservation>(Reservation.class);

}
