package boris.tserinher.Records;

import java.util.HashMap;

public class ClassRecord extends Record {
		
	public ClassRecord(String id, String type) {
		super(id, type);
	}
	
	private HashMap<String, MethodRecord> methodsList = new HashMap<>();
	private HashMap<String , VarRecord> fieldsList = new HashMap<>();
	
	public void putMethodRecord(MethodRecord methodRecord){
		methodsList.put(methodRecord.getId(), methodRecord);
	}
	
	public MethodRecord getMethodRecord(String methodId){
		return methodsList.get(methodId);
	}
	
	public void putFieldsRecord(VarRecord fieldRecord){
		fieldsList.put(fieldRecord.getId(), fieldRecord);
	}
	
	public VarRecord getFieldRecord(String fieldId){
		return fieldsList.get(fieldId);
	}
	
	public void printMethodsList(){
		methodsList.forEach((id, record)->{
			System.out.format("Method: %s \n", record);
		});
	}
	
	public void printFieldsList(){
		fieldsList.forEach((id, record)->{
			System.out.format("Field: %s \n", record);
		});
	}

}
