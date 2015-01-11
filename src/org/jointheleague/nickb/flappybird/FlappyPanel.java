package org.jointheleague.nickb.flappybird;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class FlappyPanel extends JPanel implements Runnable, ActionListener {
	public static int FRAME_WIDTH = 2000;
	public static int FRAME_HEIGHT = 720;
	int score = 0;
	JLabel scoreLabel = new JLabel("Score: 0");
	private BufferedImage dayBackground;
	private BufferedImage flappyImage;
	private BufferedImage dieImage;
	private BufferedImage pipe;

	private Bird flappy;
	private Pipe pipe1;
	private Pipe pipe2;
	private Pipe pipe3;
	private Timer ticker;
	private Pipe[] pipeArray;
	boolean addedPoint = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new FlappyPanel());
	}

	@Override
	public void run() {
		JFrame myFrame = new JFrame("Flappy Bird");
		new BoxLayout(myFrame, BoxLayout.Y_AXIS);
		FlowLayout labelLayout = new FlowLayout(FlowLayout.CENTER, 300, 15);
		JPanel infoPanel = new JPanel();
		Font labelFont = new Font("Times New Roman", Font.PLAIN, 24);
		scoreLabel.setFont(labelFont);

		myFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		scoreLabel.setSize(100, 80);

		infoPanel.setLayout(labelLayout);
		this.add(scoreLabel);
		myFrame.add(this);
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setResizable(false);
		try {
			initializeImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
		flappy = new Bird(dieImage, flappyImage);
		pipe1 = new Pipe(pipe, FRAME_WIDTH);
		pipe2 = new Pipe(pipe, FRAME_WIDTH + FRAME_WIDTH / 3);
		pipe3 = new Pipe(pipe, FRAME_WIDTH + 2 * FRAME_WIDTH / 3);
		pipeArray = new Pipe[] { pipe1, pipe2, pipe3 };
		ticker = new Timer(40, flappy);
		ticker.addActionListener(this);
		for (Pipe p : pipeArray) {
			ticker.addActionListener(p);
		}
		ticker.start();
		myFrame.addKeyListener(flappy);
	}

	private void initializeImages() throws IOException {
		dayBackground = ImageIO.read(getClass().getResourceAsStream(
				"images/Background.gif"));
		flappyImage = ImageIO.read(getClass().getResourceAsStream(
				"images/BirdFlapping.gif"));
		dieImage = ImageIO.read(getClass().getResourceAsStream(
				"images/BirdFalling.gif"));
		pipe = ImageIO.read(getClass().getResourceAsStream("images/Pipe.gif"));
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		tileBackground(g2);
	}

	private void tileBackground(Graphics2D g2) {
		int xPos = 0;
		int w = dayBackground.getWidth(); // image width

		while (xPos < FRAME_WIDTH) {
			g2.drawImage(dayBackground, null, xPos, 0);
			xPos += 2 * w;
		}

		AffineTransform cached = g2.getTransform();
		xPos = -2 * w;
		g2.scale(-1, 1);
		while (-xPos < FRAME_WIDTH * 2) {
			g2.drawImage(dayBackground, null, xPos, 0);
			xPos -= 2 * w;
		}
		g2.setTransform(cached);
		flappy.drawSelf(g2);
		for (Pipe p : pipeArray) {
			p.drawSelf(g2);
		}
	}

	private void collisionTest() {
		for (Pipe p : pipeArray) {
			if (flappy.getShape().intersects(p.getShape())) {
				gameOver();
			}
		}
	}

	private void topScreen() {
		if (flappy.getShape().y < 0) {
			gameOver();
		}
		if (flappy.getShape().y > 750) {
			gameOver();
		}
	}

	public void addScore() {

		for (Pipe p : pipeArray) {
			if (addedPoint == false
					&& flappy.getShape().x - 20 < p.getShape().x
					&& flappy.getShape().x + 30 > p.getShape().x) {
				addedPoint = true;
				score++;
				if (score % 10 == 1) {
					increaseSpeed();
				}
				scoreLabel.setText("Score: " + score);
			} else
				addedPoint = false;
		}
	}

	public void gameOver() {
		ticker.stop();
		int answer = JOptionPane.showConfirmDialog(null, "Your score was: "
				+ score + "\nWould you like to play again?", "Game Over",
				JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			gameRestart();
		} else {
			System.exit(0);
		}
	}

	private void gameRestart() {
		scoreLabel.setText("Score: 0");
		score = 0;
		pipe1 = new Pipe(pipe, FRAME_WIDTH);
		pipe2 = new Pipe(pipe, FRAME_WIDTH + FRAME_WIDTH / 3);
		pipe3 = new Pipe(pipe, FRAME_WIDTH + 2 * FRAME_WIDTH / 3);
		pipeArray = new Pipe[] { pipe1, pipe2, pipe3 };
		for (Pipe p : pipeArray) {
			ticker.addActionListener(p);
		}
		flappy.yPos = 300;
		flappy.xPos = 100;
		ticker.start();
	}

	private void increaseSpeed() {
		for (Pipe p : pipeArray) {
			p.increaseSpeed();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		topScreen();
		addScore();
		collisionTest();
		repaint();
	}

}
