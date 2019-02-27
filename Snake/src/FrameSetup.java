import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FrameSetup extends JFrame {

	public FrameSetup(JPanel p) {
		setTitle("Snake");
		add(p);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);

		try {
			setIconImage(ImageIO.read(new File("./Images/SnakeIcon.png")));
		} catch (Exception e) {}

		setVisible(true);
	}

}
