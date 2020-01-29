package src.models;

import java.util.HashMap;

public class VM {
	private String name;
	private Organisation org;
	private VMCategory category;
	private Integer cpuCores;
	private Integer gpuCores;
	private Integer ram;
	private HashMap<String, Disc> discs;
	
	public boolean checkRequired() {
		if (this.name == null || this.cpuCores == null || this.gpuCores == null || this.ram == null || this.category == null)
			return false;
		return true;
	}
	
}
