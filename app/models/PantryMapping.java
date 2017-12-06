package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.mvc.Http;
import play.mvc.Http.Session;

@Entity
@Table(name = "pantry_mapping")
public class PantryMapping extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	@Column(name = "res_emp_id")
	public int resEmpId;
	
	@Column(name = "resd_emp_id")
	public int resdEmpId;
	
	@Column(name = "res_date")
	public long resDate;

	@Column(name = "start_time")
	public long startTime;
	
	@Column(name = "end_time")
	public long endTime;
	
	@Column(name = "d_tag")
	public String dTag;
	
	@Column(name = "table_type")
	public String tableType;
	
	public static Finder<Long, PantryMapping> find = new Finder<Long,PantryMapping>(PantryMapping.class);
	
}