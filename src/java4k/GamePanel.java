package java4k;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public abstract class GamePanel extends JPanel implements Game, MouseListener, MouseMotionListener, KeyListener, Runnable {
	
	protected volatile boolean running = true;
	protected boolean continuousRepaint = false;

	public GamePanel(boolean continuousRepaint) {
		this.continuousRepaint = continuousRepaint;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	public GamePanel() {
		this(false);
	}
	
	public void start() {
		if (continuousRepaint) {
			new Thread(this).start();
		} else {
			repaint();
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public void run() {
		while(running) {
			repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		processAWTEvent(e);

	}

	@Override
	public void mouseExited(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		processAWTEvent(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		processAWTEvent(e);
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}
	
	@Override
	public void processAWTEvent(AWTEvent e) {
		// Do nothing
	}

}
