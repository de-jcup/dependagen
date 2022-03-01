package de.jcup.dependagen.model;

public class RootDependencyNode extends DependencyNode{
	
	@Override
	public DependencyNode getParent() {
		throw new IllegalStateException("A root node has no parent");
	}

}
