package form.pojo;

import models.Reservation;
import play.Logger;
import play.data.Form;
import com.avaje.ebean.Ebean;
import play.data.validation.Constraints.Required;

public class ReservationForm {
	
	@Required
	public String empName;
	
	@Required
	public int empId;
	
	@Required
	public long startTime;
	
	@Required
	public long endTime;

	public String dTag;
		
	public String confName;
	
	
	public boolean save(){
		try{
			Ebean.beginTransaction();
			Reservation res = new Reservation();
			res.confName = this.confName;
			res.empName = this.empName;
			res.empId = this.empId;
			res.startTime = this.startTime;
			res.endTime = this.endTime;
			res.dTag = "N";
			res.save();
			Ebean.commitTransaction();
			return true;
		}catch(Exception e){
			Logger.info("Reservation failed");
			Ebean.endTransaction();
			return false;
		}
	}

}
