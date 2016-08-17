package java4k.apoone4k;

/*
 * Copyright (c) 2005-2012 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java4k.GamePanel;

public class A extends GamePanel implements Runnable {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private static final int[][] l = new int[][] { {}, { 310, 300, 20, 20 }, { 200, 460, 30, 20, 400, 460, 30, 20 },

	{ 310, 130, 30, 30 }, { 200, 300, 40, 20, 400, 300, 40, 20 }, { 200, 450, 30, 30, 360, 450, 30, 30, 530, 450, 30, 30 },

	{ 200, 130, 30, 30, 400, 130, 30, 30 }, { 200, 285, 50, 35, 400, 295, 50, 25 }, { 200, 430, 30, 50, 400, 430, 35, 54 },

	{ 150, 130, 30, 30, 300, 130, 40, 30, 450, 130, 50, 30 }, { 150, 300, 68, 20, 300, 300, 59, 20, 450, 300, 50, 20 }, { 150, 440, 30, 40, 300, 428, 30, 52, 450, 422, 30, 58 },

	{ 200, 130, 30, 30, 280, 99, 100, 30, 430, 130, 30, 30 }, { 450, 259, 100, 30, 330, 295, 50, 25, 150, 259, 130, 30 }, { 120, 430, 30, 50, 217, 419, 100, 30, 375, 430, 30, 50, 530, 430, 40, 50 },

	{ 200, 150, 30, 10, 267, 99, 100, 30, 404, 150, 30, 10 }, { 430, 300, 80, 20, 300, 254, 10, 66, 100, 259, 100, 30 },
			{ 150, 460, 10, 10, 200, 430, 10, 10, 250, 430, 10, 10, 300, 470, 10, 10, 375, 470, 10, 10, 450, 439, 10, 10, 545, 470, 10, 10, 620, 470, 10, 10 },

			{}, { 310, 300, 150, 20 }, { 150, 460, 100, 20, 400, 460, 150, 20 },

			{ 245, 130, 150, 30 }, { 200, 300, 90, 20, 210, 290, 70, 10, 220, 280, 50, 10, 400, 300, 90, 20, 410, 290, 70, 10, 420, 280, 50, 10 },
			{ 200, 450, 30, 30, 360, 450, 30, 30, 440, 450, 30, 30 },

			{ 120, 0, 10, 110, 160, 150, 10, 10, 520, 0, 10, 110, 560, 150, 10, 10 }, { 220, 300, 175, 20 }, { 200, 417, 20, 63, 500, 412, 20, 68 }, };

	private static final Color[] rc = new Color[] { Color.BLACK, Color.WHITE, Color.YELLOW, Color.BLUE, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.ORANGE };

	private boolean[] pressed = new boolean[256];

	private long lastTime = System.nanoTime();
	private long think = lastTime;

	/**
	 * pressed[0] == Spieler will grad springen
	 * pressed[1] == Spiel ist gestartet oder nicht
	 * pressed[2] == Spieler springt grad oder nicht
	 * pressed[3] == Spiel pausiert
	 */
	private final boolean[] boolValues = new boolean[4];

	/**
	 * p[0] == X-Wert des Spielers
	 * p[1] == Y-Wert des Spielers
	 * p[2] == aktuelles Level
	 * p[3] == aktuelle Tode
	 * p[4] == beste Anzahl von Toden
	 * p[5] == aktuelle velocity in Y-Richtung
	 * p[6] == aktuelle velocity in X-Richtung
	 */
	private final float[] p = new float[7];

	private final Color[] c;

	/** Regel für die Partikeleffekte
	 * 0 = x-Wert
	 * 1 = y-Wert
	 * 2 = velocity in x-Richtung
	 * 3 = velocity in y-Richtung
	 * 4 = Zeit die es noch sichtbar ist */
	private float[] ex = null;

	private BufferedImage offscreenImage;
	private Graphics2D offscreennGraphics;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	public A() {
		super(true);
		// Graphische Grundlagen für das Double Buffering
		offscreenImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		offscreennGraphics = offscreenImage.createGraphics();
		offscreennGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Variablen zum Zeitmessen und genau Timen wann geupdatet werden soll
		lastTime = System.nanoTime();
		think = lastTime;

		c = new Color[2];
		c[0] = Color.WHITE;
		c[1] = Color.BLACK;

		p[4] = -1;
		p[0] = 10;
		p[1] = 130;

	}

	@Override
	public void paintComponent(Graphics appletGraphics) {

		long now = System.nanoTime();
		long delta = now - lastTime;
		think += delta;

		// Update / think
		// Wenn 10 ms vergangen sind, dann denke nach
		while (think >= 10000000L) {
			think -= 10000000L;

			if ((pressed[KeyEvent.VK_SPACE]) || (pressed[KeyEvent.VK_LEFT]) || (pressed[KeyEvent.VK_RIGHT]) || (pressed[KeyEvent.VK_UP]) || (pressed[KeyEvent.VK_DOWN])) {
				if (boolValues[1]) {
					boolValues[0] = true;
				} else {
					boolValues[1] = true;
					boolValues[3] = boolValues[2] = false;
					p[0] = 10;
					p[1] = 130;
					p[2] = 0;
					p[3] = 0;
					c[1] = Color.BLACK;
					c[0] = Color.WHITE;
					p[6] = 1.4f;
					pressed = new boolean[256];
				}
			}
			if ((pressed[KeyEvent.VK_P]) && (boolValues[1])) {
				pressed[KeyEvent.VK_P] = false;
				boolValues[3] = !boolValues[3];
			}
			if (pressed[KeyEvent.VK_ESCAPE]) {
				boolValues[1] = false;
				p[3] = -1;
			}

			// wenn das Spiel gestartet ist
			if ((boolValues[1]) && (!boolValues[3])) {
				// beweg den Spieler in X-Richtung automatisch
				p[0] += p[6];
				// falls in der mittleren Ebene
				if ((int) (p[2] % 3) == 1) {
					p[0] -= p[6] * 2;
					// falls in der mittleren Ebene der Spieler am Rand angekommen dann setze ihn in die 3te Ebene
					if (p[0] < 0) {
						p[2] += 1;
						p[0] = 10;
						p[1] = 450;
					}
				}
				// falls in der oberen Ebene und am Rand dann in die mittlere Ebene setzen
				if ((int) (p[2] % 3) == 0) {
					if (p[0] + 30 > 640) {
						p[2] += 1;
						p[0] = 600;
						p[1] = 290;
					}
				}
				// falls in der unteren Ebene, entweder den Spieler in die obere Ebene setzen oder Spiel stoppen wenn alle Level gespielt worden
				if ((int) (p[2] % 3) == 2) {
					if (p[0] + 30 > 640) {
						p[2] += 1;
						if (p[2] >= l.length) {
							p[2] -= 1;
							boolValues[1] = false;
							if ((p[3] < p[4]) || (p[4] < 0)) {
								p[4] = p[3];
							}
						} else {
							p[0] = 10;
							p[1] = 130;

							if ((int) p[2] == 18) {
								p[6] = 2.6f;
							}

							int rand = (int) (Math.random() * rc.length);
							c[0] = rc[rand];
							c[1] = new Color(255 - c[0].getRed(), 255 - c[0].getGreen(), 255 - c[0].getBlue());
						}
					}
				}
				// Spieler möchte springen, springt aber derzeit nicht
				if ((!boolValues[2]) && (boolValues[0])) {
					boolValues[2] = true;
					p[5] = -3.2f;
				}
				// falls Spieler springt
				if (boolValues[2]) {
					p[1] += p[5];
					p[5] += 0.07f;
				}
				// falls Spieler auf dem Boden angekommen dann setze ihn auf den Boden und sag das er nicht mehr springt
				if (p[1] + 30 > (int) (p[2] % 3 + 1) * 160) {
					p[1] = (int) (p[2] % 3 + 1) * 160 - 30;
					p[5] = 0;
					boolValues[2] = false;
					boolValues[0] = false;
				}
				// überprüfe, ob der Spieler gerade mit einem Rechteck kollidiert
				int lev = (int) (p[2]);
				for (int i = 0; i < l[lev].length; i += 4) {
					if ((p[0] + 30 >= l[lev][i]) && (p[1] + 30 >= l[lev][i + 1]) && (p[0] <= l[lev][i] + l[lev][i + 2]) && (p[1] <= l[lev][i + 1] + l[lev][i + 3])) {
						// falls ja dann Tod += 1 und Partikeleffekte erstellen und Spieler auf die Startposition zurücksetzen
						p[3] += 1;
						ex = new float[(int) (5 * (int) (Math.random() * 15 + 20))];
						for (int z = 0; z < ex.length; z += 5) {
							ex[z] = p[0] + 12;
							ex[z + 1] = p[1] + 12;
							ex[z + 2] = (float) (Math.random() * 3.0) - 1.5f;
							ex[z + 3] = (float) (Math.random() * 3.0) - 2f;
							ex[z + 4] = 1100000000f;
						}
						if ((int) (p[2] % 3) == 0) {
							p[0] = 10;
							p[1] = 130;
						}
						if ((int) (p[2] % 3) == 2) {
							p[0] = 10;
							p[1] = 450;
						}
						if ((int) (p[2] % 3) == 1) {
							p[0] = 600;
							p[1] = 290;
						}
						pressed = new boolean[256];
						boolValues[2] = false;
						boolValues[0] = false;
					}
				}
			}
			// Partikel bewegen und löschen falls die Zeit abgelaufen ist
			if (ex != null) {
				ex[4] -= 10000000L;
				if (ex[4] <= 0) {
					ex = null;
				} else {
					for (int z = 0; z < ex.length; z += 5) {
						ex[z] += ex[z + 2];
						ex[z + 1] += ex[z + 3];
						ex[z + 3] += 0.02f;
					}
				}
			}
		}

		lastTime = now;

		// Renderabschnitt
		// Hintergrund malen
		offscreennGraphics.setColor(c[0]);
		offscreennGraphics.fillRect(0, 0, 640, 160);
		offscreennGraphics.fillRect(0, 320, 640, 160);

		offscreennGraphics.setColor(c[1]);
		offscreennGraphics.fillRect(0, 160, 640, 160);

		offscreennGraphics.setColor(Color.BLACK);
		offscreennGraphics.drawRect(0, 0, 639, 479);

		offscreennGraphics.setColor(c[1]);
		/** draw level objects */
		int lev = (int) (p[2] / 3) * 3;
		for (int i = 0; i < l[lev].length; i += 4) {
			offscreennGraphics.fillRect(l[lev][i], l[lev][i + 1], l[lev][i + 2], l[lev][i + 3]);
		}

		for (int i = 0; i < l[lev + 2].length; i += 4) {
			offscreennGraphics.fillRect(l[lev + 2][i], l[lev + 2][i + 1], l[lev + 2][i + 2], l[lev + 2][i + 3]);
		}

		offscreennGraphics.setColor(c[0]);
		for (int i = 0; i < l[lev + 1].length; i += 4) {
			offscreennGraphics.fillRect(l[lev + 1][i], l[lev + 1][i + 1], l[lev + 1][i + 2], l[lev + 1][i + 3]);
		}

		/** draw player */
		offscreennGraphics.setColor(c[1]);
		if ((int) (p[2] % 3) == 1) {
			offscreennGraphics.setColor(c[0]);
		}
		offscreennGraphics.fillRect((int) (p[0]), (int) (p[1]), 30, 30);

		/** draw particles */
		if (ex != null) {
			for (int z = 0; z < ex.length; z += 5) {
				offscreennGraphics.fillRect((int) (ex[z]), (int) (ex[z + 1]), 5, 5);
			}
		}

		/** render die Spielinfos */
		offscreennGraphics.setColor(c[1]);
		offscreennGraphics.setFont(offscreennGraphics.getFont().deriveFont(20f).deriveFont(1));
		String s = "ApoOne4k";
		offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 30);
		if (p[3] >= 0) {
			offscreennGraphics.drawString("deaths: " + String.valueOf((int) (p[3])), 10, 30);
		}
		offscreennGraphics.drawString("level: " + String.valueOf((int) (p[2]) + 1), 550, 30);

		if (boolValues[3]) {
			s = "Press p to continue the game";
			offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
		}
		if (!boolValues[1]) {
			s = "Press p to pause the game";
			offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			offscreennGraphics.setColor(c[0]);
			s = "Press space to jump and to start the game";
			offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 200);
			if ((p[2] > 0) && (p[3] >= 0)) {
				s = "Congratulation you beat the game with " + String.valueOf((int) (p[3])) + " death";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 250);
			}
			if (p[4] >= 0) {
				s = "Your best result are " + String.valueOf((int) (p[4])) + " death";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 285);
			}
		} else {
			if ((int) p[2] == 0) {
				s = "The game starts really easy ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 2) {
				s = "Nice a little doublejump ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			} else if ((int) p[2] == 3) {
				s = "That level is too easy ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 5) {
				s = "First tripple jump ... *gaehn*";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			} else if ((int) p[2] == 6) {
				s = "Those who died in that level ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 8) {
				s = "You died " + String.valueOf((int) (p[3])) + " times? OMG";
				if (p[3] < 1) {
					s = "Until now you are doing it right ...";
				}
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			} else if ((int) p[2] == 9) {
				s = "Ok, tripple jumps ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 11) {
				s = "The next level will be harder! I promise!";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			} else if ((int) p[2] == 12) {
				s = "I like the tunnel levels";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 14) {
				s = "Then you will love the next level";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			} else if ((int) p[2] == 15) {
				s = "I heard someone died here ... often ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 18) {
				s = "And now FASTER";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 60);
			} else if ((int) p[2] == 26) {
				s = "The last level :'(";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 320);
			}

			offscreennGraphics.setColor(c[0]);
			if ((int) p[2] == 1) {
				s = "You know space is your friend ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 160);
			} else if ((int) p[2] == 4) {
				s = "I hope the next levels will be harder ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 160);
			} else if ((int) p[2] == 7) {
				s = "Can't play and have no skills ...";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 160);
			} else if ((int) p[2] == 13) {
				s = "You like that level too?";
				offscreennGraphics.drawString(s, 320 - offscreennGraphics.getFontMetrics().stringWidth(s) / 2, 40 + 160);
			}
		}

		// Render das Ganze auf den Bildschirm
		appletGraphics.drawImage(offscreenImage, 0, 0, null);

	}

	@Override
	public void processAWTEvent(AWTEvent e) {
		if (e instanceof KeyEvent) {
			KeyEvent event = (KeyEvent) e;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (event.getKeyCode() < 256) {
					pressed[event.getKeyCode()] = true;
				}
			}
			if (e.getID() == KeyEvent.KEY_RELEASED) {
				if (event.getKeyCode() < 256) {
					pressed[event.getKeyCode()] = false;
				}
			}
		}
	}
}