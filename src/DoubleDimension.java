import java.awt.geom.Dimension2D;

public class DoubleDimension extends Dimension2D {

	private double x, y;
	
	@Override
	public double getHeight() {

		return y;
	}

	@Override
	public double getWidth() {

		return x;
	}

	@Override
	public void setSize(double arg0, double arg1) {
		
		x = arg0;
		y = arg1;
		
	}

	@Override
	public String toString() {
		
		return "DoubleDimension [" + x + ", " + y + "]";
		
	}
	
	public boolean equals(DoubleDimension other) {
		
		if (this.getWidth() == other.getWidth() && this.getHeight() == other.getHeight()) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
