package EventHandler;

import RegionSystem.Region;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.awt.*;

import static Settings.WindowSettings.VISUALIZATION_WINDOW;

public class RegionEvent implements Runnable { // class that executes the region flashing thing
	
	private static Color transparant = new Color(255, 255, 255, 0);
	private static AlphaComposite compositeApply = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	private static AlphaComposite compositeRemove = AlphaComposite.getInstance(AlphaComposite.CLEAR, 1f);
	
	HashFunction hf = Hashing.md5();
	
	private Region region = null;
	private Graphics g = null;
	private Color color = null;
	private HashCode hash = null;
	
	public RegionEvent(Region inputRegion, Graphics inputG, Color inputColor) {
		
		region = inputRegion;
		g = inputG;
		color = inputColor;
		hash = hf.newHasher()
				.putInt(inputRegion.getWindowPosition().x)
				.putInt(inputRegion.getWindowPosition().y)
				.putInt(inputColor.getRed())
				.putInt(inputColor.getGreen())
				.putInt(inputColor.getBlue())
				.hash();
		
	}
	
	public void drawBeginning() {
		
		region.fillRegionXYDebugColor(g, color, compositeApply);
		
	}
	
	private void drawEnd() {
		
		region.fillRegionXYDebugColor(g, transparant, compositeRemove);
		VISUALIZATION_WINDOW.repaint();
		
	}
	
	public void run() {
		
		drawEnd();
		RegionEventDriver.removeEvent(this);
		
	}
	
	public Region getRegion() {
		
		return region;
		
	}
	
	public Color getColor() {
		
		return color;
		
	}
	
	@Override
	public int hashCode() {
		
		return hash.asInt();
		
	}
	
	@Override
	public boolean equals(Object o) {
		
		RegionEvent otherEvent = (RegionEvent) o;
		
		if (region.getWindowPosition().equals(otherEvent.getRegion().getWindowPosition()) && color.equals(otherEvent.getColor())) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}
