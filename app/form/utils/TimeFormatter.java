package form.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import models.Employee;

public class TimeFormatter {
	
	public static String formatTime(long millis){
		
		Date date = new Date(millis);
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateFormatted = formatter.format(date);
		return dateFormatted;
	}
	
	public String getEmployeeName(String id){
		String name = "";
		Employee emp = Employee.find.where().eq("id", id).findUnique();
		name = emp.empFirstName + " " + emp.empLastName;
		return name;
	}
}
