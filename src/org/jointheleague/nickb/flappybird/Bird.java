package org.jointheleague.nickb.flappybird;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bird implements ActionListener, KeyListener {
	int yPos = 400;
	int xPos = 100;
	boolean isflapping = false;
	long startHop = 0;
	long lastKeyPress;
	BufferedImage fall;
	BufferedImage flap;
	int screenHeight = FlappyPanel.FRAME_HEIGHT;

	Bird(BufferedImage Fall, BufferedImage Flap) {
		this.fall = Fall;
		this.flap = Flap;
	}

	public void drawSelf(Graphics2D g2) {
		AffineTransform cached = g2.getTransform();
		if(isflapping){
			g2.drawImage(flap, null, xPos, yPos);
		} else{
			g2.drawImage(fall, null, xPos, yPos);
		}
		g2.setTransform(cached);
	}

	public void crash() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (startHop - lastKeyPress < 1000
				&& System.currentTimeMillis() < startHop + 300 && isflapping) {
			yPos -= 16;
		} else {
			isflapping = false;
			yPos += 13;
		}
		lastKeyPress = startHop;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			hop();
		}
	}

	private void hop() {
		startHop = System.currentTimeMillis();
		isflapping = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public Rectangle getShape(){
		return new Rectangle(xPos, yPos, fall.getWidth(), fall.getHeight());
	}
}
