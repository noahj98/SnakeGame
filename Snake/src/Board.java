import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener, ActionListener {

	private Snake snake;
	private int width;
	private int height;
	private Timer timer;
	private int snake_width;
	private int snake_height;
	private int speed;
	private long start_time;
	private Snake.Direction current_direction;
	int score;
	int delayer;
	double total_area;

	public Board(int width, int height, int sw, int sh, int speed) {
		if (width < 10 || height < 10 || sw < 1 || sh < 1)
			throw new RuntimeException("Dimensions are too small");
		if (sw > width || sh > height || width % sw != 0 || height % sh != 0)
			throw new RuntimeException("object width/height invalid");
		this.width = width;
		this.height = height;
		this.snake_width = sw;
		this.snake_height = sh;
		this.speed = speed;
		delayer = 0;
		score = 0;

		total_area = (double) ((width * height) / (snake_width * snake_height));

		initBoard();
	}

	private void initBoard() {
		setOpaque(true);
		setFocusable(true);
		setBackground(Color.black);
		setPreferredSize(new Dimension(width, height + 14));
		setLayout(new BorderLayout());
		initGame();

		addKeyListener(this);
		start_time = System.nanoTime();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, height + 1, width, 14);
		g.setColor(Color.BLACK);
		g.drawString("Score: " + score, width / 2 - 20, height + 11);

		List<Point> points = snake.getPoints();

		Point p = points.get(0);

		g.setColor(Color.GREEN);
		g.fillOval(p.getX(), p.getY(), snake_width, snake_height);

		for (int i = 1; i < snake.getPoints().size(); i++) {
			p = points.get(i);
			g.setColor(Color.GREEN);
			g.fillOval(p.getX(), p.getY(), snake_width, snake_height);
			g.setColor(Color.BLACK);
			g.drawOval(p.getX(), p.getY(), snake_width, snake_height);
		}

		g.setColor(Color.RED);
		g.fillRect(snake.getFood().getPoint().getX(), snake.getFood().getPoint().getY(), snake_width, snake_height);
		g.setColor(Color.BLACK);
		g.drawRect(snake.getFood().getPoint().getX(), snake.getFood().getPoint().getY(), snake_width, snake_height);

		if (snake.getFood().areBadPoints()) {
			for (int i = 0; i < snake.getFood().getBadPoints().size(); i++) {
				g.setColor(Color.WHITE);
				g.fillRect(snake.getFood().getBadPoints().get(i).getX(), snake.getFood().getBadPoints().get(i).getY(),
						snake_width, snake_height);
				g.setColor(Color.BLACK);
				g.drawRect(snake.getFood().getBadPoints().get(i).getX(), snake.getFood().getBadPoints().get(i).getY(),
						snake_width, snake_height);
			}
		}
	}

	private void initGame() {
		snake = new SnakeImpl(width, height, snake_width, snake_height);
		current_direction = snake.getDirection();

		timer = new Timer(speed, this);
		timer.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		Snake.Direction requested_direction;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			requested_direction = Snake.Direction.NORTH;
			break;
		case KeyEvent.VK_RIGHT:
			requested_direction = Snake.Direction.EAST;
			break;
		case KeyEvent.VK_DOWN:
			requested_direction = Snake.Direction.SOUTH;
			break;
		case KeyEvent.VK_LEFT:
			requested_direction = Snake.Direction.WEST;
			break;
		case KeyEvent.VK_W:
			requested_direction = Snake.Direction.NORTH;
			break;
		case KeyEvent.VK_D:
			requested_direction = Snake.Direction.EAST;
			break;
		case KeyEvent.VK_S:
			requested_direction = Snake.Direction.SOUTH;
			break;
		case KeyEvent.VK_A:
			requested_direction = Snake.Direction.WEST;
			break;
		default:
			return;
		}
		
		if (!snake.areOppositeDirections(current_direction, requested_direction))
			snake.changeDirection(requested_direction);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		snake.moveSnake();
		delayer++;
		if (delayer % 6 == 0) {
			double total_time = ((double) (System.nanoTime() - start_time)) / 1000000000;
			double snake_area = (double) (snake.getSnakeLength() * snake_width * snake_height);
			double filled_area = snake_area / total_area;
			double scr;
			if (snake.getSnakeLength() == 1)
				scr = 0;
			else {
				scr = (26 * (Math.pow(5.2 * filled_area, 1.18) - 1.05 * Math.pow(total_time * 5.15, .6)));
				if (snake.getFood().areBadPoints())
					scr *= 1.08 * Math.pow(1.02 * snake.getFood().getBadPoints().size(), 1.06);
			}
			if (scr < 0)
				scr = 0;
			score = (int) (Math.sqrt(scr) + .5);
		}

		if (!snake.isAlive()) {
			double total_time = ((double) (System.nanoTime() - start_time)) / 1000000000;
			double snake_area = (double) (snake.getSnakeLength() * snake_width * snake_height);
			double filled_area = snake_area / total_area;
			double scr;
			if (snake.getSnakeLength() == 1)
				scr = 0;
			else {
				scr = (26 * (Math.pow(5.2 * filled_area, 1.18) - 1.05 * Math.pow(total_time * 5.15, .6)));
				if (snake.getFood().areBadPoints())
					scr *= 1.08 * Math.pow(1.02 * snake.getFood().getBadPoints().size(), 1.06);
			}
			if (scr < 0)
				scr = 0;
			score = (int) (Math.sqrt(scr) + .5);
			
			String time = String.format("%.2f", total_time);
			String[] options = { "Yes", "No" };
			String percent = String.format("%.1f", filled_area);
			
			String message;
			if (score > 400)
				message = "Get a life.";
			else if (score > 300)
				message = "WINNER WINNER CHICKEN DINNER!";
			else if (score > 215)
				message = "Sooper Dooper!";
			else if (score > 120)
				message = "Dat dur is a nice lookin' board, feller!";
			else if (score > 50)
				message = "Keep practicing, feller!";
			else if (score > 25)
				message = "You can do better, feller.";
			else if (score > 0 && new Random().nextInt(100) > 95) {
				if (new Random().nextInt(100) > 50)
					message = "You couldn't hit the broad side of a barn...";
				else
					message = "Yo mama should disown you.";
			} else if (score > 0)
				message = "Rough all around...";
			else if (new Random().nextInt(10) > 5)
				message = "Maybe Minecraft is more your speed.";
			else
				message = "Just plain awkward...";
			String congrat_msg = message + "\nYour snake got to length: " + snake.getSnakeLength()
					+ ".\nYou survived for: " + time + " seconds.\nYou filled up: " + percent
					+ "% of the board.\nYour score is: " + score + ".\nWould you like to play again?";
			int n = JOptionPane.showOptionDialog(new JFrame(), congrat_msg, "Game Over!", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, null);
			if (n == 1 || n == -1)
				System.exit(0);
			try {
				Thread.sleep(100);
			} catch (Exception a) {
			} finally {
			}
			snake.reset();
			current_direction = snake.getDirection();
			start_time = System.nanoTime();
		} else {
			current_direction = snake.getDirection();
			repaint();
		}
	}
}