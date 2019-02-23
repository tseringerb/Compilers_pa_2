package boris.tserinher.Records;

public class Record {
	
	private String id;
	private String type;
	
	public Record() {
		super();
	}
	
	public Record(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Record [id=" + id + ", type=" + type + "]";
	}

}
