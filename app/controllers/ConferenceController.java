package controllers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import form.pojo.ReservationForm;
import form.utils.TimeFormatter;
import form.utils.UtilFunctions;
import models.Reservation;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.Session;
import util.AppConstants;
import views.html.*;
import views.html.helper.form;

@play.mvc.Security.Authenticated(Secured.class)
public class ConferenceController extends Controller{
	
	public Result conference() {
    	Map<String, String> map = Form.form().bindFromRequest().get().getData();
    	String selectedConf = map.get("conference");
    	User user = UtilFunctions.getCurrentUser();
    	Integer empId = user.empId;
    	String name = user.empFirstName+" "+user.empLastName;
    	
    	List<String> bookedSlots = new ArrayList<>();
    	long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<Reservation> resList = Reservation.find.where()
    			.le("endTime", currentHighTime)
    			.ge("startTime", currentLowTime)
    			.eq("dTag", "N")
    			.eq("confName", selectedConf)
    			.findList();
    	
    	for (Reservation resObj : resList) {
			bookedSlots.add(UtilFunctions.getHHmmDate(resObj.startTime) + "-" + UtilFunctions.getHHmmDate(resObj.endTime));
		}
    	bookedSlots =  new ArrayList<String>(new LinkedHashSet<String>(bookedSlots));
    	
        return ok(conferenceReservation.render(name,empId,selectedConf.toUpperCase(),bookedSlots,Form.form(Reservation.class)));
    }
	
	public Result handleConference(){
    	Form<ReservationForm> reservationForm = Form.form(ReservationForm.class).bindFromRequest();
    	ReservationForm filledForm = reservationForm.get();
    	List<Reservation> resList = new ArrayList<>();
    	resList = UtilFunctions.getCurrentConfReservationList(filledForm.confName);
    	boolean overlapping = false;
    	for (Reservation res : resList) {
			// Check if the start date and end date coming from UI lies in already booked slot
    		overlapping = checkOverlap(res,filledForm);
    		if(overlapping){
    			break;
    		}
		}
    	if(overlapping){
    		flash(AppConstants.FLASHERROR,"Time Slot Not Available. Please check start/end time.");
    	}else{
    		filledForm.save();
    	}
    	return redirect("/viewHistory");
    }
	
	public Result viewHistory(){
    	List<Reservation> resList = new ArrayList<>();
    	long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	resList = UtilFunctions.getCurrentConfReservationList();
    	return ok(reservationHistory.render("Reservation History",resList));
    }
	
	public Result cancelReservation(String id){
    	Reservation res = Reservation.find.where().eq("id", id).eq("dTag", "N").findUnique();
    	Session session = Http.Context.current().session();
    	if(session.get("emp_name").equalsIgnoreCase(res.empName)){
    		res.dTag = "Y";
        	res.update();
    	}else{
    		flash(AppConstants.FLASHERROR,"You are not allowed to cancel this reservation.");
    	}
    	return redirect("/viewHistory");
    }
	
	
	public boolean checkOverlap(Reservation res, ReservationForm filledForm) {
    	int uiStart = Integer.parseInt(TimeFormatter.formatTime(filledForm.startTime).replace(":", ""));
    	int uiEnd = Integer.parseInt(TimeFormatter.formatTime(filledForm.endTime).replace(":", ""));
    	int dbStart = Integer.parseInt(TimeFormatter.formatTime(res.startTime).replace(":", ""));
    	int dbEnd = Integer.parseInt(TimeFormatter.formatTime(res.endTime).replace(":", ""));
    	if(((uiStart >= dbStart) && (uiStart < dbEnd))
			||((uiEnd > dbStart) && (uiEnd <= dbEnd))){
			return true;
		}
		return false;
	}
	
}