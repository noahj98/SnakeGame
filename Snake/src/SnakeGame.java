import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame {

	public static void main(String[] e) {
		
		int width = 200;
		int height = 220;
		int snake_width = 10;
		int	snake_height = 10;
		int speed = 130;
		
		JPanel board = new Board(width, height, snake_width, snake_height, speed);
		@SuppressWarnings("unused")
		JFrame game_frame = new FrameSetup(board);
		
		//To-Do list
		//fix quickly pressing keys
		
	}
}
