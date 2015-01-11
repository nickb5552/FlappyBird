package org.jointheleague.nickb.flappybird;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Pipe implements ActionListener {

	private int xPos;
	private int yPos = 350;
	private int speed = 30;
	BufferedImage pipe;
	private static final Random RNG = new Random();

	Pipe(BufferedImage pipe, int xPos) {
		this.pipe = pipe;
		this.xPos = xPos;
	}

	public void drawSelf(Graphics2D g2) {
		if (xPos > 0 - pipe.getWidth()) {
			g2.drawImage(pipe, null, xPos, yPos);
		} else if (xPos < 0 - pipe.getWidth()) {
			yPos = 150 + RNG.nextInt(FlappyPanel.FRAME_HEIGHT / 2);
			xPos = 2100;
		}
	}

	public Rectangle getShape() {
		return new Rectangle(xPos, yPos, pipe.getWidth(), pipe.getHeight());
	}

	public void actionPerformed(ActionEvent e) {
		xPos -= speed;
	}

	public void increaseSpeed() {
		speed += 5;
	}
}
