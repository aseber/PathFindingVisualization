import java.awt.Point;

public class MyUtils {

	public static int clampInt(int a, int b, int c) {
		
		return Math.max(c, Math.min(a, b));
	
	}
	
	public static double clampDouble(double a, double b, double c) {
		
		return Math.max(c, Math.min(a, b));
	
	}
	
	public static double linearDistance(double a, double b) {
		
		return Math.abs(a - b);
		
	}
	
	public static double euclideanDistance(Point p1, Point p2) {
		
		return Math.sqrt(Math.pow(p2.x - p1.x, 2.0) + Math.pow(p2.y - p1.y, 2.0));
		
	}
	
	public static double euclideanDistanceSquared(Point p1, Point p2) {
		
		return Math.pow(p2.x - p1.x, 2.0) + Math.pow(p2.y - p1.y, 2.0);
		
	}
	
	public static double manhattanDistance(Point p1, Point p2) {
		
		return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
		
	}
	
	public static double closestDistanceToLine(Point l0, Point l1, Point p1) {
		
		double distance = -1.0;
		
		double upper = Math.abs((l1.getY() - l0.getY())*p1.getX() - (l1.getX() - l0.getX())*p1.getY() + l1.getX()*l0.getY() - l1.getY()*l0.getX());
		double lower = Math.sqrt(Math.pow((l1.getY() - l0.getY()), 2.0) + Math.pow((l1.getX() - l0.getX()), 2.0));
		distance = upper/lower;
		
		return distance;
		
	}
	
}
