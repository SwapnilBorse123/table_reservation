package form.utils;

import java.util.ArrayList;
import java.util.List;
import models.Employee;
import models.PantryMapping;

public class PantryView {
	
	public Employee reservingEmp;
	public List<Employee> reservedEmpList;
	public long startTime;
	public long endTime;
	public String tableType;
	
	public static List<PantryView> getViewList(List<PantryMapping> pmList){
		List<PantryView> pantryViewList = new ArrayList<>();
		int reservedById,j=0;
		for (int i = 0; i < pmList.size(); ++i) {
			PantryView pv = new PantryView();
			try {
				List<Employee> reservedFor = new ArrayList<>();
				reservedById = pmList.get(i).resEmpId;
				Employee reservedByNameEmp = Employee.find.where().eq("id", reservedById).findUnique();
				j = i;
				while(j< pmList.size() && reservedById == pmList.get(j).resEmpId){
					Employee reservedForNameEmp = Employee.find.where().eq("id", pmList.get(j).resdEmpId).findUnique();
					reservedFor.add(reservedForNameEmp);
					j++;
				}
				pv.reservingEmp = reservedByNameEmp;
				pv.reservedEmpList = reservedFor;
				pv.startTime = pmList.get(i).startTime;
				pv.endTime = pmList.get(i).endTime;
				pv.tableType = pmList.get(i).tableType;
				i = j-1;
				pantryViewList.add(pv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pantryViewList;
	}
	
}
