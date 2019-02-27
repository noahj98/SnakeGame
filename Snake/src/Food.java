import java.util.List;

public interface Food {
	
	public Point getPoint();
	public void setNewPoint(List<Point> impossible_locations);
	public boolean areBadPoints();
	public List<Point> getBadPoints();
	public void reset();
	
}
