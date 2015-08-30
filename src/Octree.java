
public class Octree { // Useless right now.

	private Box parentBox = null;
	private Box[] childrenBoxes = new Box[8];
	
	public Octree(Box parent, Box[] children) {
		
		if (children.length != 8) {
			
			throw new ArrayIndexOutOfBoundsException("The Octree must have exactly eight children");
			
		}
		
		parentBox = parent;
		childrenBoxes = children;
		
	}
	
	public Box getParent() {
		
		return parentBox;
		
	}
	
	public Box[] getChildren() {
		
		return childrenBoxes;
		
	}
	
	public Box getChild(int index) {
		
		if (index < 0 || index >= 8) {
			
			throw new ArrayIndexOutOfBoundsException("Tried to access child element " + index + " of an Octree");
			
		}
		
		return childrenBoxes[index];
		
	}
	
	public boolean isParent(Box otherBox) {
		
		return getParent().equals(otherBox);
		
	}
	
	public boolean isChild(Box otherBox) {
		
		for (Box currentChild : childrenBoxes) {
			
			if (otherBox == currentChild) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
}
