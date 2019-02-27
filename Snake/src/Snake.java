import java.util.List;

public interface Snake {

	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	};
	
	default public boolean areOppositeDirections(Direction a, Direction b) {
		if (getSnakeLength() == 1) return false;
		if (a.equals(Direction.NORTH) && b.equals(Direction.SOUTH)) return true;
		if (a.equals(Direction.EAST) && b.equals(Direction.WEST)) return true;
		if (a.equals(Direction.SOUTH) && b.equals(Direction.NORTH)) return true;
		if (a.equals(Direction.WEST) && b.equals(Direction.EAST)) return true;
		return false;
	}
	
	public void reset();
	
	public Food getFood();
	
	public void changeDirection(Direction d);
	
	public Direction getDirection();

	public int getBoardWidth();

	public int getBoardHeight();

	public int getSnakeLength();

	public void moveSnake();

	public void stop();

	public boolean isAlive();

	public List<Point> getPoints();
}
