package src.models;

public class Disc {
	private String name;
	private DiscType type;
	private Integer capacity;
	private String vm;
	private String org;
	
	public boolean checkRequired() {
		if (this.name == null || this.type== null || this.capacity == null || this.org == null )
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DiscType getType() {
		return type;
	}

	public void setType(DiscType type) {
		this.type = type;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getVm() {
		return vm;
	}

	public void setVm(String vm) {
		this.vm = vm;
	}

	public Disc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Disc(String name, DiscType type, Integer capacity, String vm, String org) {
		super();
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		this.vm = vm;
		this.org = org;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}
	
	
}
