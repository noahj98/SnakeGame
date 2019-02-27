import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodImpl implements Food {

	private Point current_location;
	private int board_width;
	private int board_height;
	private int food_width;
	private int food_height;
	private List<Point> bad_points;
	private Random r;

	public FoodImpl(int w, int h, int sw, int sh) {
		board_width = w;
		board_height = h;
		food_width = sw;
		food_height = sh;
		bad_points = new ArrayList<Point>(0);
		current_location = new Point(0,0);
		r = new Random();
		reset();
	}
	
	public void reset() {
		bad_points.clear();
		current_location.setX(board_width*3/4);
		current_location.setY(board_height/2);
	}

	@Override
	public Point getPoint() {
		return current_location;
	}

	@Override
	public void setNewPoint(List<Point> impossible_locations) {
		
		impossible_locations.addAll(bad_points);

		current_location.setX(getAvailableX(impossible_locations));

		current_location.setY(getPoint(impossible_locations, current_location.getX()));
		
		if (impossible_locations.size() > 95 && r.nextInt(20) < 14) {
			impossible_locations.add(current_location);
			setBadPoint(impossible_locations);
			impossible_locations.remove(current_location);
		} else if (impossible_locations.size() > 65 && r.nextInt(20) < 11) {
			impossible_locations.add(current_location);
			setBadPoint(impossible_locations);
			impossible_locations.remove(current_location);
		} else if (impossible_locations.size() > 45 && r.nextInt(20) < 8) {
			impossible_locations.add(current_location);
			setBadPoint(impossible_locations);
			impossible_locations.remove(current_location);
		} else if (impossible_locations.size() > 25 && r.nextInt(20) < 4) {
			impossible_locations.add(current_location);
			setBadPoint(impossible_locations);
			impossible_locations.remove(current_location);
		}
		
		impossible_locations.removeAll(bad_points);
		
	}

	private int getAvailableX(List<Point> pts) {
		
		int x = r.nextInt(board_width / food_width) * food_width;

		int count = 0;

		for (int i = 0; i < pts.size(); i++) {
			if (x == pts.get(i).getX())
				count++;
		}
		if (count < board_width / food_width)
			return x;
		return getAvailableX(pts);
	}

	private int getPoint(List<Point> pts, int x) {
		
		int y = r.nextInt(board_height / food_height) * food_height;

		for (int i = 0; i < pts.size(); i++) {
			if (pts.get(i).equals(x,y))
				return getPoint(pts, x);
		}
		return y;
	}
	
	private void setBadPoint(List<Point> pts) {
		
		int x = getAvailableX(pts);

		bad_points.add(new Point(x,getPoint(pts, x)));
		
	}
	
	public boolean areBadPoints() {
		return !bad_points.isEmpty();
	}

	@Override
	public List<Point> getBadPoints() {
		return bad_points;
	}
}
