package NodeSystem;

import java.util.HashSet;

public abstract class Node implements INode { // Abstract class for RegionNode and BoxNode

    private IDistance object;
	private INode parentNode;
	private double f, g;
	
	public Node(IDistance object, INode node) {

        setParent(node);
        setObject(object);
		
	}
	
	public final void setF(double f) {
		
		this.f = f;
		
	}
	
	public final double getF() {
		
		return this.f;
		
	}
	
	public final void setG(double g) { // Cost from current to beginning. Necessary for A*
		
		this.g = g;
		
	}
	
	public final double getG() {
		
		return this.g;
		
	}

	public final void setObject(IDistance object) {

	    this.object = object;

    }

    public final IDistance getObject() {

        return object;

    }

	public final void setParent(INode node) {
		
		this.parentNode = node;
		
	}

	public final INode getParent() {

	    return this.parentNode;

    }

    public final boolean equals(Object o) {

        if (o instanceof Node) {

            Node node = (Node) o;

            if (this.getObject().equals(node.getObject())) {// && this.parentNode.box.equals(node.parentNode.box) && this.pathCost == node.pathCost) {

                return true;

            }

        }

        return false;

    }

    public final double distanceFrom(INode node) {

        return getObject().distanceFrom(node.getObject());

    }

	public abstract HashSet<INode> findNeighboringNodes();

}
