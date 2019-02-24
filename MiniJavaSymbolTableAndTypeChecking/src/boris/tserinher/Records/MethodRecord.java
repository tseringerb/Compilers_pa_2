package boris.tserinher.Records;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MethodRecord extends Record {

	private String containingClass;

	private List<Record> parametersList = new ArrayList<>();
	
	public MethodRecord(String id, String type) {
		super(id, type);
	}

	public String getContainingClass() {
		return containingClass;
	}

	public void setContainingClass(String containingClass) {
		this.containingClass = containingClass;
	}
	
//	public void setParametersList(LinkedHashMap<String, VarRecord> parametersList) {
//		this.parametersList = parametersList;
//	}
//
//	public HashMap<String, VarRecord> getParametersList() {
//		return parametersList;
//	}

	public void putParameterRecord(Record parameterRecord) {
		parametersList.add(parameterRecord);
	}

//	public VarRecord getParameterRecord(String parameterId) {
//		return parametersList.get(parameterId);
//	}

	public void printParametersList() {
		parametersList.forEach((record) -> {
			System.out.format("Parameter: %s \n", record);
		});
	}

	public List<Record> getParametersList() {
		return parametersList;
	}

	public void setParametersList(List<Record> parametersList) {
		this.parametersList = parametersList;
	}
}