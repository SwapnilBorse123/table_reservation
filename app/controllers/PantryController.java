package controllers;

import views.html.*;
import views.html.helper.form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;

import form.utils.PantryView;
import form.utils.UtilFunctions;
import models.Employee;
import models.PantryMapping;
import models.Reservation;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Http.Session;
import util.AppConstants;

@play.mvc.Security.Authenticated(Secured.class)
public class PantryController extends Controller{
	
	public Result pantry(){
		long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	
    	User user = UtilFunctions.getCurrentUser();
    	Integer empId = user.empId;
    	String name = user.empFirstName + " " + user.empLastName;
    	
    	List<Employee> empList = new ArrayList<>();
    	List<PantryMapping> pmList = new ArrayList<>();
    	List<PantryMapping> pmResList = PantryMapping.find.where()
    			.eq("resEmpId", user.id)
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("dTag", "N").findList();
    	List<PantryMapping> pmResdList = PantryMapping.find.where()
    			.eq("resdEmpId", user.id)
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("dTag", "N").findList();
    	
    	if(pmResList != null && pmResList.size() != 0){
    		flash(AppConstants.FLASHERROR,"You have already booked a table.");
    		return redirect("/viewPantryHistory");
    	}
    	if(pmResdList != null && pmResdList.size() != 0){
    		flash(AppConstants.FLASHERROR,"Table has been booked for you.");
    		return redirect("/viewPantryHistory");
    	}
    	
    	empList = Employee.find.where()
    			.eq("dTag","N")
    			.ne("empId", user.empId)
    			.orderBy("empFirstName")
    			.findList();
    	pmList = PantryMapping.find.where()
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("dTag", "N").findList();
    	
    	List<String> bookedSlots = new ArrayList<>();
    	for (PantryMapping pm : pmList) {
			bookedSlots.add(UtilFunctions.getHHmmDate(pm.startTime) + "-" + UtilFunctions.getHHmmDate(pm.endTime));
		}
    	
    	bookedSlots =  new ArrayList<String>(new LinkedHashSet<String>(bookedSlots));
    	
    	return ok(pantryReservation.render(name,empId,"PANTRY",empList
    			,pmList,bookedSlots,Form.form(Reservation.class)));
    }
	
	
	public Result handlePantry(){
    	Map<String, String> map = Form.form().bindFromRequest().get().getData();
    	String selectedMembers = map.get("selectedMembers");
    	String[] membersArray = null;
    	// check if table is selected
    	String tableType = map.get("tableType");
    	if(tableType.equalsIgnoreCase("")){
    		flash(AppConstants.FLASHERROR,"Please select table.");
			return redirect("/pantry");
    	}
    	
    	// check if members are selected
    	if(!selectedMembers.equalsIgnoreCase("")){
    		selectedMembers = selectedMembers.substring(0,selectedMembers.length()-3);
    		membersArray = selectedMembers.split("###");
    		if(membersArray.length > 8){
    			flash(AppConstants.FLASHERROR,"Maximum allowed members is 8.");
    			return redirect("/pantry");
    		}
    	}else{
    		flash(AppConstants.FLASHERROR,"Please select members.");
			return redirect("/pantry");
    	}
    	//check if start and end time is missing
    	if(map.get("startTime").equalsIgnoreCase("NaN") || map.get("endTime").equalsIgnoreCase("NaN")){
    		flash(AppConstants.FLASHERROR,"Please put start/end time.");
			return redirect("/pantry");
    	}
    	
    	long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<PantryMapping> pmList = PantryMapping.find.where()
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("dTag", "N").findList();

    	int uiStart = Integer.parseInt(UtilFunctions.getHHmmDate(map.get("startTime")).replace(":", ""));
    	int uiEnd = Integer.parseInt(UtilFunctions.getHHmmDate(map.get("endTime")).replace(":", ""));
    	int dbStart;
    	int dbEnd;
    	// check if the timeslot is available
    	boolean overlapped = false;
    	for (PantryMapping pantryMapping : pmList) {
    		dbStart = Integer.parseInt(UtilFunctions.getHHmmDate(pantryMapping.startTime).replace(":", ""));
    		dbEnd = Integer.parseInt(UtilFunctions.getHHmmDate(pantryMapping.endTime).replace(":", ""));
    		if(((uiStart >= dbStart) && (uiStart < dbEnd))
    				||((uiEnd > dbStart) && (uiEnd <= dbEnd))){
    			flash(AppConstants.FLASHERROR,"Time Slot Not Available. Please check start/end time.");
    			return redirect("/viewPantryHistory");
    		}
		}
    	
    	try {
    		User user = UtilFunctions.getCurrentUser();
    		Employee reservingEmp = Employee.find.where().eq("empEmail", user.empEmail).findUnique();
    		if(membersArray != null){
	    		for (int i=0; i < membersArray.length; ++i) {
	    			Ebean.beginTransaction();
	    			
	    			PantryMapping pm = new PantryMapping();
	    			Employee reservedEmp = Employee.find.where().eq("empId", membersArray[i].split(":")[1]).findUnique();
	    	    	pm.resEmpId = reservingEmp.id;
	    	    	pm.resdEmpId = reservedEmp.id;
	    	    	pm.resDate = System.currentTimeMillis();
	    	    	pm.startTime = Long.parseLong(map.get("startTime"));
	    	    	pm.endTime = Long.parseLong(map.get("endTime"));
	    	    	pm.dTag = "N";
	    	    	pm.tableType = tableType.equalsIgnoreCase("bigTable") ? "Big Table" : "Small Table" ;
	    	    	pm.save();
	    	    	
	    			Ebean.commitTransaction();
	    		}
    		}else{
    			flash(AppConstants.FLASHERROR,"Please select members.");
    			return redirect("/viewPantryHistory");
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return redirect("/viewPantryHistory");
    }
	
	public Result viewPantryHistory(){
    	List<PantryMapping> pmList = UtilFunctions.getCurrentPantryMappingList();
    	List<PantryView> pantryViewList = PantryView.getViewList(pmList);
    	return ok(pantryResHistory.render(pantryViewList));
    }
	
	public Result cancelPantryReservation(String id){
    	User user = UtilFunctions.getCurrentUser();
    	List<PantryMapping> pm = PantryMapping.find.where().eq("resEmpId", user.id).eq("dTag", "N").findList();
    	Employee reservingEmp = Employee.find.where().eq("id",id).findUnique();
    	
    	
    	if(user.empId != reservingEmp.empId){
    		flash(AppConstants.FLASHERROR,"You are not allowed to cancel this reservation.");
			return redirect("/viewPantryHistory");
    	}
    	
    	for (PantryMapping pantryMapping : pm) {
			pantryMapping.dTag = "Y";
			pantryMapping.update();
		}
    	return redirect("/viewPantryHistory");
    }
    
    public Result cancelPantryMember(String id){
    	User user = UtilFunctions.getCurrentUser();
    	long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<PantryMapping> pmList = PantryMapping.find.where()
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("resdEmpId", id)
    			.eq("dTag", "N")
    			.orderBy("resEmpId").findList();
    	PantryMapping pmObj = PantryMapping.find.where()
    			.eq("resdEmpId", id)
    			.le("endTime", currentHighTime)
    			.ge("startTime", currentLowTime)
    			.eq("dTag", "N").findUnique();
    	Employee reservingEmp = Employee.find.where()
    			.eq("id",pmObj.resEmpId).findUnique();
    	
    	if(user.empId != reservingEmp.empId){
    		flash(AppConstants.FLASHERROR,"You are not allowed to cancel this reservation.");
			return redirect("/viewPantryHistory");
    	}
    	
    	PantryMapping pm = PantryMapping.find.where().eq("resdEmpId", id).eq("resEmpId", reservingEmp.id).eq("dTag", "N").findUnique();
    	if(pm != null){
    		pm.dTag = "Y";
        	pm.update();
    	}
    	return redirect("/viewPantryHistory");
    }
	
}
