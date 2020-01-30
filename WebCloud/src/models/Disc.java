package src.models;

public class Disc {
	private String name;
	private Organisation org;
	private DiscType type;
	private Integer capacity;
	private VM vm;
	
	public boolean checkRequired() {
		if (this.name == null || this.type== null || this.capacity == null || this.vm == null )
			return false;
		return true;
	}
}
