package java4k;

import java.awt.AWTEvent;
import java.awt.Dimension;

import javax.swing.JPanel;

public interface Game  {
	
	public void start();
	
	public void stop();
	
	public JPanel getPanel();
	
	public Dimension getPreferredSize();
	
	public void processAWTEvent(AWTEvent e);
	
//	public void updateGame(); 
//	public void repaintGame(); // TODO override paintComponent() in GamePanel
	
	/*
	  while(running) {
	  	repaint();
	  }
	  
	  
	  
	 */

}
