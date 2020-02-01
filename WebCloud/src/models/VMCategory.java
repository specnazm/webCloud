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
	
	

	public VMCategory(String name, Integer cpuCores, Integer gpuCores, Integer ram) {
		super();
		this.name = name;
		this.cpuCores = cpuCores;
		this.gpuCores = gpuCores;
		this.ram = ram;
	}

	public VMCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCpuCores() {
		return cpuCores;
	}

	public void setCpuCores(Integer cpuCores) {
		this.cpuCores = cpuCores;
	}

	public Integer getGpuCores() {
		return gpuCores;
	}

	public void setGpuCores(Integer gpuCores) {
		this.gpuCores = gpuCores;
	}

	public Integer getRam() {
		return ram;
	}

	public void setRam(Integer ram) {
		this.ram = ram;
	}
	
	
}
