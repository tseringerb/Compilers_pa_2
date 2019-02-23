package boris.tserinher.Records;

import java.util.HashMap;

public class MethodRecord extends Record {

	public MethodRecord(String id, String type) {
		super(id, type);
	}
	private HashMap<String , VarRecord> parametersList = new HashMap<>();
	
	public void putParameterRecord(VarRecord parameterRecord){
		parametersList.put(parameterRecord.getId(), parameterRecord);
	}
	
	public VarRecord getParameterRecord(String parameterId){
		return parametersList.get(parameterId);
	}
	
	public void printParametersList(){
		parametersList.forEach((id, record)->{
			System.out.format("Parameter: %s \n", record);
		});
	}
}
