package boris.tserinher.Records;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MethodRecord extends Record {

	private String containingClass;

	public String getContainingClass() {
		return containingClass;
	}

	public void setContainingClass(String containingClass) {
		this.containingClass = containingClass;
	}

	public MethodRecord(String id, String type) {
		super(id, type);
	}

	private LinkedHashMap<String, VarRecord> parametersList = new LinkedHashMap<>();

	public HashMap<String, VarRecord> getParametersList() {
		return parametersList;
	}

	public void putParameterRecord(VarRecord parameterRecord) {
		parametersList.put(parameterRecord.getId(), parameterRecord);
	}

	public VarRecord getParameterRecord(String parameterId) {
		return parametersList.get(parameterId);
	}

	public void printParametersList() {
		parametersList.forEach((id, record) -> {
			System.out.format("Parameter: %s \n", record);
		});
	}
}