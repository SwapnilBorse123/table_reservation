package form.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.PantryMapping;
import models.Reservation;
import models.User;
import play.mvc.Http;
import play.mvc.Http.Session;

public class UtilFunctions {

	public static User getCurrentUser(){
		Session session = Http.Context.current().session();
    	String name = session.get("emp_name");
    	String email = session.get("emp_email");
    	User user = User.find.where().eq("empEmail", email).findUnique();
    	return user;
	}
	
	@SuppressWarnings("deprecation")
	public static long getLowerMillSeconds(){
		Date d = new Date();
		d.setHours(0);
		d.setMinutes(0);
		return d.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public static long getUpperMillSeconds(){
		Date d = new Date();
		d.setHours(23);
		d.setMinutes(59);
		return d.getTime();
	}
	
	public static List<PantryMapping> getCurrentPantryMappingList(){
		long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<PantryMapping> pmList = PantryMapping.find.where()
    			.le("resDate", currentHighTime)
    			.ge("resDate", currentLowTime)
    			.eq("dTag", "N")
    			.orderBy("end_time desc")
    			.findList();
    	return pmList;
	}
	
	public static List<Reservation> getCurrentConfReservationList(String confName){
		long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<Reservation> resList = Reservation.find.where()
    			.eq("confName", confName)
    			.le("endTime", currentHighTime)
    			.ge("startTime", currentLowTime)
    			.eq("dTag", "N")
    			.findList();
    	
    	return resList;
	}
	
	public static List<Reservation> getCurrentConfReservationList(){
		long currentLowTime = UtilFunctions.getLowerMillSeconds();
    	long currentHighTime = UtilFunctions.getUpperMillSeconds();
    	List<Reservation> resList = Reservation.find.where()
    			.le("endTime", currentHighTime)
    			.ge("startTime", currentLowTime)
    			.eq("dTag", "N")
    			.findList();
    	
    	return resList;
	}
	
	public static String getHHmmDate(String millis){
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		long milliSeconds= Long.parseLong(millis);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		String dateFormatted = formatter.format(calendar.getTime());
		return dateFormatted;
	}
	
	
	public static String getHHmmDate(long millis){
		Date date = new Date(millis);
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateFormatted = formatter.format(date);
		return dateFormatted;
	}
}
