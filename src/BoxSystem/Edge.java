package BoxSystem;

import java.awt.Point;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import static Settings.WindowSettings.BOX_XY_SIZE;

public class Edge {

	// Powerful class that allows me to build the region system. NodeSystem.Node the the hashcode is created using Guava
	// Also note that the box has no input in the hashcode, meaning that we allow for collisions between boxes with the same edge
	
	public static enum directions {
		
		DOWN,
		RIGHT;
		
	}
	
	HashFunction hf = Hashing.md5();
	
	private Box parentBox = null;
	private Point position = new Point();
	directions direction;
	byte length = 0x0;
	private HashCode hash = null;
	
	public Edge(Box box, Point point, directions inputDirection, byte inputLength) {
		
		hash = hf.newHasher()
			.putInt(point.x)
			.putInt(point.y)
			.putInt(inputDirection.hashCode())
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
		
		if (direction == directions.DOWN) {
			
			endPosition.setLocation(endPosition.getX(), endPosition.getY() + length* BOX_XY_SIZE);
			
		}
		
		if (direction == directions.RIGHT) {
			
			endPosition.setLocation(endPosition.getX() + length* BOX_XY_SIZE, endPosition.getY());
			
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
