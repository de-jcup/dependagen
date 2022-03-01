package de.jcup.dependagen.model;

import java.util.ArrayList;
import java.util.List;

public class DependencyNode {
	
	private List<DependencyNode> dependencies = new ArrayList<>();

	public List<DependencyNode> getDependencies() {
		return dependencies;
	}
	@Override
	public String toString() {
		return "DependencyNode artifact=" + artifact
				+ ", version=" + version + ", group=" + group + ", ]";
	}
	private String group;
	private String artifact;
	private String version;
	private DependencyNode parent;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setParent(DependencyNode parent) {
		if (this.parent!=null) {
			this.parent.getDependencies().remove(this);
		}
		this.parent=parent;
		this.parent.getDependencies().add(this);
		
	}

	public DependencyNode getParent() {
		return parent;
	}

}
