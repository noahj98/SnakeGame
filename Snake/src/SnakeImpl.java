import java.util.ArrayList;
import java.util.List;

public class SnakeImpl implements Snake {

	private Direction direction;
	private int board_width;
	private int board_height;
	private int snake_width;
	private int snake_height;
	private boolean is_alive;
	private List<Point> snake_body;
	private Food food;
	private int length_to_gain;

	public SnakeImpl(int w, int h, int sw, int sh) {
		direction = Direction.EAST;
		is_alive = true;
		snake_body = new ArrayList<Point>();
		snake_body.add(new Point(w / 4, h / 2));
		board_width = w;
		board_height = h;
		snake_width = sw;
		snake_height = sh;
		food = new FoodImpl(w, h, sw, sh);
		length_to_gain = 0;
	}
	
	public void reset() {
		direction = Direction.EAST;
		is_alive = true;
		snake_body.clear();
		snake_body.add(new Point(board_width / 4, board_height / 2));
		length_to_gain = 0;
		food.reset();
	}

	@Override
	public void stop() {
		is_alive = false;
	}

	public Food getFood() {
		return food;
	}

	@Override
	public void changeDirection(Direction d) {
		if (!areOppositeDirections(d, direction))
			direction = d;
	}

	public Direction getDirection() {
		return direction;
	}

	@Override
	public int getBoardWidth() {
		return board_width;
	}

	@Override
	public int getBoardHeight() {
		return board_height;
	}

	@Override
	public int getSnakeLength() {
		return snake_body.size();
	}

	@Override
	public void moveSnake() {
		if (!isAlive())
			return;

		if (length_to_gain > 0) {
			switch (direction) {
			case NORTH:
				snake_body.add(0, new Point(snake_body.get(0).getX(), snake_body.get(0).getY() - snake_height));
				break;
			case EAST:
				snake_body.add(0, new Point(snake_body.get(0).getX() + snake_width, snake_body.get(0).getY()));
				break;
			case SOUTH:
				snake_body.add(0, new Point(snake_body.get(0).getX(), snake_body.get(0).getY() + snake_height));
				break;
			case WEST:
				snake_body.add(0, new Point(snake_body.get(0).getX() - snake_width, snake_body.get(0).getY()));
				break;
			}
			length_to_gain--;
		} else {
			Point p = snake_body.get(snake_body.size()-1);
			
			switch (direction) {
			case NORTH:
				p.setX(snake_body.get(0).getX());
				p.setY(snake_body.get(0).getY() - snake_height);
				snake_body.add(0, p);
				break;
			case EAST:
				p.setX(snake_body.get(0).getX() + snake_width);
				p.setY(snake_body.get(0).getY());
				snake_body.add(0, p);
				break;
			case SOUTH:
				p.setX(snake_body.get(0).getX());
				p.setY(snake_body.get(0).getY() + snake_height);
				snake_body.add(0, p);
				break;
			case WEST:
				p.setX(snake_body.get(0).getX() - snake_width);
				p.setY(snake_body.get(0).getY());
				snake_body.add(0, p);
				break;
			}
		}
		
		if (snake_body.get(0).equals(food.getPoint())) {
			food.setNewPoint(snake_body);

			if (snake_body.size() > 95) {
				length_to_gain++;
				return;
			}
			if (snake_body.size() > 65) {
				length_to_gain += 2;
				return;
			}
			length_to_gain += 3;
			return;
		} else  if (length_to_gain == 0) {
			snake_body.remove(snake_body.size() - 1);
		}
		
		hasCollided();
	}

	public boolean isAlive() {
		return is_alive;
	}

	private void hasCollided() {
		Point p = snake_body.get(0);

		if (p.getX() < 0 || p.getX() >= board_width || p.getY() < 0 || p.getY() >= board_height) {
			is_alive = false;
			return;
		}

		for (int i = 1; i < snake_body.size(); i++) {
			if (p.equals(snake_body.get(i))) {
				is_alive = false;
				return;
			}
		}

		if (food.areBadPoints()) {
			for (int i = 0; i < food.getBadPoints().size(); i++) {
				if (snake_body.get(0).equals(food.getBadPoints().get(i))) {
					is_alive = false;
					return;
				}
			}
		}

	}

	@Override
	public List<Point> getPoints() {
		return snake_body;
	}
}
