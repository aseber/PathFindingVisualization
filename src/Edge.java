import java.awt.Point;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Edge {

	// Powerful class that allows me to build the region system. Node the the hashcode is created using Guava
	// Also note that the box has no input in the hashcode, meaning that we allow for collisions between boxes with the same edge
	
	public static final byte RIGHT = 0;
	public static final byte DOWN = 1;
	
	HashFunction hf = Hashing.md5();
	
	private Box parentBox = null;
	private Point position = new Point();
	byte direction = 0x0;
	byte length = 0x0;
	private HashCode hash = null;
	
	public Edge(Box box, Point point, byte inputDirection, byte inputLength) {
		
		hash = hf.newHasher()
			.putInt(point.x)
			.putInt(point.y)
			.putByte(inputDirection)
			.putByte(inputLength)
			.hash();
		position.setLocation(point);
		direction = inputDirection;
		length = inputLength;
		parentBox = box;
		
	}
	
	public Box getBox() {
		
		return parentBox;
		
	}
	
	public Point getStartPoint() {
		
		return position;
		
	}
	
	public Point getEndPoint() {
		
		Point endPosition = new Point(position);
		
		if (direction == Box.DIRECTION_DOWN) {
			
			endPosition.setLocation(endPosition.getX(), endPosition.getY() + length*VisualizationBase.boxXYSize);
			
		}
		
		if (direction == Box.DIRECTION_RIGHT) {
			
			endPosition.setLocation(endPosition.getX() + length*VisualizationBase.boxXYSize, endPosition.getY());
			
		}
		
		return endPosition;
		
	}
	
	public HashCode getHash() {
		
		return hash;
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		Edge otherEdge = (Edge) o;
		
		if (this.getHash().equals(otherEdge.getHash())) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	@Override
	public int hashCode() {
		
		return hash.asInt();
		
	}
	
	public String toString() {
		
		return hash.toString();
		
	}
	
}
