package src.models;

public class VMCategory {
	private String name;
	private Integer cpuCores;
	private Integer gpuCores;
	private Integer ram;
	
	public boolean checkRequired() {
		if (this.name == null || this.cpuCores == null || this.gpuCores == null || this.ram == null )
			return false;
		return true;
	}
}
