package src.models;

import java.util.HashMap;

public class VM {
	private String name;
	private String org;
	private String category;
	private Integer cpuCores;
	private Integer gpuCores;
	private Integer ram;
//	private HashMap<String, Disc> discs;
	
	
	public boolean checkRequired() {
		if (this.name == null || this.cpuCores == null || this.gpuCores == null || this.ram == null || this.category == null)
			return false;
		return true;
	}

	public VM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VM(String name, String org, String category, Integer cpuCores, Integer gpuCores, Integer ram) {
	super();
	this.name = name;
	this.org = org;
	this.category = category;
	this.cpuCores = cpuCores;
	this.gpuCores = gpuCores;
	this.ram = ram;
}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
