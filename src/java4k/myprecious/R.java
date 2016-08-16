package java4k.myprecious;
/*
 * Copyright (c) 2005-2013 Dirk Aporius <dirk.aporius@gmail.com>
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

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class R extends Applet implements Runnable {
	
	private boolean left, right, up, down, n, p, r;
	
	public void start() {
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		new Thread(this).start();
	}
	
	public void run() {
//		setSize(400, 400); // für den AppletViewer
		String s = "";
		
		/**
		 * every level is 10 x 14 and then goldsteps (2), silversteps (2)
		 * 0 = wall
		 * 1 = free
		 * 2 = player
		 * 3 = treasure
		 * 4 = spike up to down
		 * 5 = spike right to left
		 * 6 = spike down to up
		 * 7 = spike left to right
		 * 8 = double block
		 * 9 = double block free
		 */
		final String LEVELS = 
				"1113000000" +
				"1111110311" +
				"1101110111" +
				"1011110011" +
				"1111111110" +
				"1110010111" +
				"1003111111" +
				"1011011111" +
				"1001011100" +
				"1001010111" +
				"1111111011" +
				"0111111101" +
				"0001111101" +
				"00021101111122"+
				"1111000000" +
				"1111110111" +
				"1101111113" +
				"1111101111" +
				"1310110100" +
				"0011110111" +
				"1111111111" +
				"1111011111" +
				"1113010001" +
				"0010010011" +
				"1111111111" +
				"0111111111" +
				"0001110111" +
				"00012101111826" +
				"0011031000" +
				"0011111011" +
				"0011111111" +
				"0011100111" +
				"0010110010" +
				"0011110010" +
				"1111111111" +
				"1111011131" +
				"2111010000" +
				"0001010011" +
				"1111111111" +
				"1001111111" +
				"0000110011" +
				"00000111131320" +
				"4444444444"+
				"7011010015" +
				"7011111105" +
				"7311111115" +
				"7110112075" +
				"7110110015" +
				"7110111115" +
				"0116311610" +
				"1110075000" +
				"1614075071" +
				"1011411411" +
				"1111311001" +
				"1101001111" +
				"66666666660915"+
				"0011011100" +
				"0111411111" +
				"0100111311" +
				"0111111071" +
				"0000111071" +
				"0044111111" +
				"7111101001" +
				"7116261631" +
				"7110001001" +
				"0114001041" +
				"0011011415" +
				"1111113115" +
				"1101001115" +
				"01111660111826" +
				"1111031000" +
				"1111111011" +
				"1111111111" +
				"1111111111" +
				"1110101310" +
				"0011111111" +
				"1111111111" +
				"1111011111" +
				"2111011111" +
				"0001010111" +
				"1111111111" +
				"1111111113" +
				"1111110089" +
				"01111111991626"+
				"1111301111" +
				"1111000111" +
				"1111111111" +
				"1111111111" +
				"1111120111" +
				"1110000111" +
				"1111110111" +
				"1111111111" +
				"1111111389" +
				"8910011099" +
				"9911111110" +
				"1000111111" +
				"0001101111" +
				"00000011311726"+
				"1890111111" +
				"1990000011" +
				"0111111111" +
				"1131111111" +
				"1101111111" +
				"1101111111" +
				"1111111111" +
				"1111111111" +
				"1111111111" +
				"1118911189" +
				"1119911199" +
				"1001121011" +
				"1301100011" +
				"00011111131525" +
				"4444444444" +
				"0011111112" +
				"0189113110" +
				"1199111111" +
				"1101111111" +
				"1001118911" +
				"1011119901" +
				"1011111001" +
				"1111111101" +
				"1111111111" +
				"1111110011" +
				"3001113111" +
				"1111111111" +
				"06666666661224"+
				"1111110300" +
				"1101010111" +
				"1101111111" +
				"1001111111" +
				"1131111111" +
				"1111111111" +
				"1001111189" +
				"1018911199" +
				"1119911101" +
				"1110000011" +
				"1111113111" +
				"0111121111" +
				"1111100111" +
				"11111111111632"+
				"1111113110" +
				"1111100011" +
				"0891111111" +
				"1991111111" +
				"1101111111" +
				"1101189111" +
				"1111199011" +
				"0111110011" +
				"1111110011" +
				"1112111111" +
				"1110111113" +
				"1000310110" +
				"1101110011" +
				"66666666662030"+
				"1111110000" +
				"1111100111" +
				"0111111189" +
				"1111891199" +
				"1111991130" +
				"1111011111" +
				"1111111111" +
				"1211111111" +
				"8910111111" +
				"9911111311" +
				"0111189011" +
				"3111199111" +
				"8911110189" +
				"99111101991525";
		
		int i = 0;
		int j = 0;
		int x = 0;
		int y = 0;
		int state = 0;
		
		BufferedImage iBackground = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = iBackground.createGraphics();
		try {
			g.drawImage(ImageIO.read(R.class.getResource("c.gif")), 0, 0, 128, 128, 0, 0, 32, 32, null);
		} catch (Exception e1) {
		}
		
		BufferedImage screenBackground = new BufferedImage(320,480,BufferedImage.TYPE_INT_RGB);
		Graphics2D gBack = screenBackground.createGraphics();
		
		// Graphische Grundlagen für das Double Buffering
		BufferedImage screen = new BufferedImage(320,480,BufferedImage.TYPE_INT_RGB);
		g = screen.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D appletGraphics = (Graphics2D)getGraphics();		
		
		int gold = 0;
		int silver = 0;
		int steps = 0;
		int curLevel = 0;
		/**
		 * 0 = from up to down
		 * 1 = from right to left
		 * 2 = from down to up
		 * 3 = from left to right
		 */
		int gravity = 0;
		int breath = 0;
		final int[][] level = new int[16][12];
		final int[][] levelObjects = new int[16][12];
		final int[][] levelBlocks = new int[32][12];
		level[1][1] = -1;
		boolean bChange = false;
		boolean bFall = true;
		boolean bDeath = false;
		boolean bPlayerFall = false;
		
		// Variablen zum Zeitmessen und genau Timen wann geupdatet werden soll
		long lastTime = System.nanoTime();
		long think = 0;
		
		// Game loop.
		while (true) {
			long now = System.nanoTime();
			long delta = now - lastTime;
			think += delta;
			
			// Update / think
			// Wenn 10 ms vergangen sind, dann denke nach
			while (think >= 10000000L) {
				think -= 10000000L;
				
				if (breath > 0) breath -= 10;
				
				if (state == 0) {
					if (level[1][1] == -1) {
						if (curLevel >= LEVELS.length() / 144) {
							curLevel = 0;
						}
						if (curLevel < 0) {
							curLevel = LEVELS.length() / 144 -1;
						}
						bPlayerFall = bChange = bDeath = bFall = n = p = r = left = right = up = down = false; 
						steps = gravity = 0;
						gold = Integer.valueOf(LEVELS.substring((curLevel + 1) * 144 - 4, (curLevel + 1) * 144 - 2));
						silver = Integer.valueOf(LEVELS.substring((curLevel + 1) * 144 - 2, (curLevel + 1) * 144));
						for (y = 1; y < 15; y += 1) {
							for (x = 1; x < 11; x += 1) {
								if (y == 1) {
									gBack.drawImage(iBackground.getSubimage(2 * 32, 3 * 32, 32, 32), (x - 1) * 32, (y - 1) * 32, null);	
								}
								levelBlocks[y][x] = 0;
								level[y][x] = Integer.valueOf(LEVELS.substring(curLevel * 144 + (y-1) * 10 + (x-1), curLevel * 144 + (y-1) * 10 + x));
								gBack.drawImage(iBackground.getSubimage(0 * 32, 3 * 32, 32, 32), (x-1) * 32, (y) * 32, null);
								if (level[y][x] == 0) {
									gBack.drawImage(iBackground.getSubimage((int)(Math.random() * 3) * 32, 2 * 32, 32, 32), (x-1) * 32, (y) * 32, null);
								}
								levelObjects[y][x] = 0;
								if (level[y][x] == 1) {
									gBack.drawImage(iBackground.getSubimage((int)(Math.random() * 2) * 32, 3 * 32, 32, 32), (x-1) * 32, (y) * 32, null);
								}
								if ((level[y][x] >= 3) && (level[y][x] <= 7)) {
									levelObjects[y][x] = level[y][x];
									level[y][x] = 1;
								}
							}
						}
					} else {
						if (bFall) {
							if (!bChange) {
								i = 0;
								j = 1;
								if (gravity == 0) {
									for (y = 14; y > 0; y -= 1) {
										for (x = 1; x < 11; x += 1) {
											if (level[y][x] == 2) {
												if ((level[y+j][x+i] == 1) || ((levelObjects[y+j][x+i] >= 3) && (levelObjects[y+j][x+i] <= 7))) {
													level[y][x] = 1;
													level[y+j][x+i] = 2;
													if (levelObjects[y+j][x+i] == 3) {
														levelObjects[y+j][x+i] = 0;
													}
													bChange = true;
													levelBlocks[y+j][x+i] = -32;
													if (levelObjects[y+j][x+i] == 6) {
														bDeath = true;
													}
												}
											}
											if (level[y][x] == 8) {
												if ((((level[y+j+1][x+i] >= 1) && (level[y+j+1][x+i] <= 2)) || ((levelObjects[y+j+1][x+i] >= 3) && (levelObjects[y+j+1][x+i] <= 7))) &&
													(((level[y+j+1][x+i+1] >= 1) && (level[y+j+1][x+i+1] <= 2)) || ((levelObjects[y+j+1][x+i+1] >= 3) && (levelObjects[y+j+1][x+i+1] <= 7)))) {
													if ((level[y+j+1][x+i] == 2) || (level[y+j+1][x+i+1] == 2)) {
														if (level[y+j+1][x+i] == 2) levelObjects[y+j+1][x+i] = 2;
														if (level[y+j+1][x+i+1] == 2) levelObjects[y+j+1][x+i+1] = 2;

														bDeath = true;
													}
													level[y][x] = 1;
													level[y][x+1] = 1;
													level[y+j+j][x] = 9;
													level[y+j+j][x+1] = 9;
													level[y+j][x+i+1] = 9;
													level[y+j][x+i] = 8;

													bChange = true;
													levelBlocks[y+j][x+i] = -32;

												}
											}
										}
									}
								}
								if (gravity == 2) {
									j = -1;
									for (y = 1; y < 15; y += 1) {
										for (x = 1; x < 11; x += 1) {
											if (level[y][x] == 2) {
												if ((level[y+j][x+i] == 1) || ((levelObjects[y+j][x+i] >= 3) && (levelObjects[y+j][x+i] <= 7))) {
													if (levelObjects[y+j][x+i] == 4) {
														bDeath = true;
													}
													level[y][x] = 1;
													level[y+j][x+i] = 2;
													if (levelObjects[y+j][x+i] == 3) {
														levelObjects[y+j][x+i] = 0;
													}
													
													bChange = true;
													levelBlocks[y+j][x+i] = 32;
												}
											}
											if (level[y][x] == 8) {
												if ((((level[y+j][x+i] >= 1) && (level[y+j][x+i] <= 2)) || ((levelObjects[y+j][x+i] >= 3) && (levelObjects[y+j][x+i] <= 7))) &&
													(((level[y+j][x+i+1] >= 1) && (level[y+j][x+i+1] <= 2)) || ((levelObjects[y+j][x+i+1] >= 3) && (levelObjects[y+j][x+i+1] <= 7)))) {
													if ((level[y+j][x+i] == 2) || (level[y+j][x+i+1] == 2)) {
														if (level[y+j][x+i] == 2) levelObjects[y+j][x+i] = 2;
														if (level[y+j][x+i+1] == 2) levelObjects[y+j][x+i+1] = 2;
														bDeath = true;
													}
													level[y][x] = 9;
													level[y][x+1] = 9;
													level[y-j][x] = 1;
													level[y-j][x+1] = 1;
													level[y+j][x+i+1] = 9;
													level[y+j][x+i] = 8;
													
													bChange = true;
													levelBlocks[y+j][x+i] = 32;
												}
											}
										}
									}
								}
								if (gravity == 3) {
									i = 1;
									j = 0;
									for (y = 14; y > 0; y -= 1) {
										for (x = 10; x > 0; x -= 1) {
											if (level[y][x] == 2) {
												if ((level[y+j][x+i] == 1) || ((levelObjects[y+j][x+i] >= 3) && (levelObjects[y+j][x+i] <= 7))) {
													level[y][x] = 1;
													level[y+j][x+i] = 2;
													if (levelObjects[y+j][x+i] == 3) {
														levelObjects[y+j][x+i] = 0;
													}
													bChange = true;
													levelBlocks[y+j+16][x+i] = -32;
													if (levelObjects[y+j][x+i] == 5) {
														bDeath = true;
													}
												}
											}
											if (level[y][x] == 8) {
												if ((((level[y][x+i+1] >= 1) && (level[y][x+i+1] <= 2)) || ((levelObjects[y][x+i+1] >= 3) && (levelObjects[y][x+i+1] <= 7))) &&
													(((level[y+1][x+i+1] >= 1) && (level[y+1][x+i+1] <= 2)) || ((levelObjects[y+1][x+i+1] >= 3) && (levelObjects[y+1][x+i+1] <= 7)))) {
													if ((level[y][x+i+1] == 2) || (level[y+1][x+i+1] == 2)) {
														if (level[y][x+i+1] == 2) levelObjects[y][x+i+1] = 2;
														if (level[y+1][x+i+1] == 2) levelObjects[y+1][x+i+1] = 2;
														bDeath = true;
													}
													level[y][x] = 1;
													level[y+1][x] = 1;
													level[y][x+i] = 8;
													level[y+1][x+i] = 9;
													level[y][x+i+i] = 9;
													level[y+1][x+i+i] = 9;
													
													bChange = true;
													levelBlocks[y+16][x+i] = -32;
												}
											}
										}
									}
								}
								if (gravity == 1) {
									i = -1;
									j = 0;
									for (y = 14; y > 0; y -= 1) {
										for (x = 1; x < 11; x += 1) {
											if (level[y][x] == 2) {
												if ((level[y+j][x+i] == 1) || ((levelObjects[y+j][x+i] >= 3) && (levelObjects[y+j][x+i] <= 7))) {
													level[y][x] = 1;
													level[y+j][x+i] = 2;
													if (levelObjects[y+j][x+i] == 3) {
														levelObjects[y+j][x+i] = 0;
													}
													bChange = true;
													levelBlocks[y+j+16][x+i] = 32;
													if (levelObjects[y+j][x+i] == 7) {
														bDeath = true;
													}
												}
											}
											if (level[y][x] == 8) {
												if ((((level[y][x+i] >= 1) && (level[y][x+i] <= 2)) || ((levelObjects[y][x+i] >= 3) && (levelObjects[y][x+i] <= 7))) &&
													(((level[y+1][x+i] >= 1) && (level[y+1][x+i] <= 2)) || ((levelObjects[y+1][x+i] >= 3) && (levelObjects[y+1][x+i] <= 7)))) {
													if ((level[y][x+i] == 2) || (level[y+1][x+i] == 2)) {
														bDeath = true;
														if (level[y][x+i] == 2) levelObjects[y][x+i] = 2;
														if (level[y+1][x+i] == 2) levelObjects[y+1][x+i] = 2;
													}
													level[y][x] = 9;
													level[y+1][x] = 9;
													level[y][x+i] = 8;
													level[y+1][x+i] = 9;
													level[y][x-i] = 1;
													level[y+1][x-i] = 1;
													
													bChange = true;
													levelBlocks[y+16][x+i] = 32;
												}
											}
										}
									}
								}
								if (!bChange) {
									bPlayerFall = bFall = false;
									breath = 100;
									bFall = false;
									boolean bWin = true;
									for (y = 1; y < 15; y += 1) {
										for (x = 1; x < 11; x += 1) {
											if (levelObjects[y][x] == 3) {
												bWin = false;
												break;
											}
										}
									}
									if (bWin) {
										state = 1;
									}
								}
							} else {
								for (y = 14; y > 0; y -= 1) {
									for (x = 1; x < 11; x += 1) {
										for (i = 0; i <= 16; i += 16) {
											if (levelBlocks[y+i][x] < 0) {
												levelBlocks[y+i][x] += 3f;
												if (levelBlocks[y+i][x] >= 0) levelBlocks[y+i][x] = 0;
												
											}
											if (levelBlocks[y+i][x] > 0) {
												levelBlocks[y+i][x] -= 3f;
												if (levelBlocks[y+i][x] <= 0) levelBlocks[y+i][x] = 0;
											}
											if ((levelObjects[y][x] == 2) || (level[y][x] == 2)) {
												if (!bPlayerFall) {
													bPlayerFall = true;
												}
											}
										}
										if (((levelObjects[y][x] == 2) || (level[y][x] == 2)) && (bDeath) && (levelBlocks[y][x] == 0) && (levelBlocks[y+16][x] == 0)) {
											state = 1;
										}
									}
								}
								bChange = false;
								for (y = 14; y > 0; y -= 1) {
									for (x = 1; x < 11; x += 1) {
										if ((levelBlocks[y][x] != 0) || (levelBlocks[y+16][x] != 0)) {
											bChange = true;
											break;
										}
									}
								}
								if (!bChange) {
									think += 10000000L;
								}
							}
						} else {
							if ((left) && (gravity != 1)) {
								bFall = true;
								gravity = 1;
								steps += 1;
							}
							if ((right) && (gravity != 3)) {
								bFall = true;
								gravity = 3;
								steps += 1;
							}
							if ((down) && (gravity != 0)) {
								bFall = true;
								gravity = 0;
								steps += 1;
							}
							if ((up) && (gravity != 2)) {
								bFall = true;
								gravity = 2;
								steps += 1;
							}
							if (n) {
								curLevel += 1;
								level[1][1] = -1;
							}
							if (r) {
								level[1][1] = -1;
							}
							if (p) {
								curLevel -= 1;
								level[1][1] = -1;
							}
						}
					}
				}
				if (state == 1) {
					if ((left) || (right) || (down) || (up) || (r)) {
						if ((!bDeath) && (!r)) {
							curLevel += 1;
						}
						level[1][1] = -1;
						state = 0;
					}
				}
			}

			lastTime = now;

			AffineTransform oldAffineTransform = g.getTransform();

			// Renderabschnitt
			// Hintergrund malen
			g.drawImage(screenBackground, 0, 0, null);
			
			for (y = 1; y < 15; y += 1) {
				for (x = 1; x < 11; x += 1) {
					if (levelObjects[y][x] == 3) {
						g.drawImage(iBackground.getSubimage(0 * 32, 1 * 32, 32, 32), (x-1) * 32, (y) * 32, null);
					}
					if ((levelObjects[y][x] >= 4) && (levelObjects[y][x] <= 7)) {
						g.setTransform(AffineTransform.getRotateInstance(Math.toRadians((levelObjects[y][x] - 6) * 90), (x-1) * 32 + 16, (y) * 32 + 16));
						g.drawImage(iBackground.getSubimage(3 * 32, 2 * 32, 32, 32), (x-1) * 32, (y) * 32, null);
						g.setTransform(oldAffineTransform);
					}
				}
			}
			
			for (y = 1; y < 15; y += 1) {
				for (x = 1; x < 11; x += 1) {
					if (level[y][x] == 8) {
						g.drawImage(iBackground.getSubimage(2 * 32, 0 * 32, 64, 64), (x-1) * 32 + (int)(levelBlocks[y+16][x]), (y) * 32 + (int)(levelBlocks[y][x]), null);
					}
					if (level[y][x] == 2) {
						i = 0;
						j = 0;
						if (breath > 0) {
							i = 1;
							j = 1;
						}
						if (bPlayerFall) {
							i = 1;
						}
						g.setTransform(AffineTransform.getRotateInstance(Math.toRadians((gravity) * 90), (x-1) * 32 + (int)(levelBlocks[y+16][x]) + 16, (y) * 32 + (int)(levelBlocks[y][x]) + 16));
						g.drawImage(iBackground.getSubimage(i * 32, j * 32, 32, 32), (x-1) * 32 + (int)(levelBlocks[y+16][x]), (y) * 32 + (int)(levelBlocks[y][x]), null);	
						g.setTransform(oldAffineTransform);
					}
				}
			}

			g.setColor(new Color(175,175,175));
			g.setFont(getFont().deriveFont(15f).deriveFont(1));
			
			s = "Level "+String.valueOf(curLevel+1)+"/"+LEVELS.length()/144;
			g.drawString(s, 316 - g.getFontMetrics().stringWidth(s), 18);
			
			s = "steps: ";
			g.drawString(s, 4, 18);
			if (steps <= gold) {
				g.setColor(new Color(255, 235, 0));
			}
			if (steps > silver) {
				g.setColor(new Color(215, 100, 50));
			}
			g.drawString(String.valueOf(steps), 4 + g.getFontMetrics().stringWidth(s), 18);
			g.setFont(getFont().deriveFont(28f).deriveFont(1));
			
			if (state == 1) {
				g.drawImage(iBackground.getSubimage(32, 96, 32, 32), 10, 90, 300, 355, 0, 0, 32, 32, null);
				g.setColor(new Color(175,175,175));
				s = "Congratulations";
				if (bDeath) {
					s = "Try again";	
				}
				g.drawString(s, 160 - g.getFontMetrics().stringWidth(s)/2, 120);
				
				g.drawImage(iBackground.getSubimage(0 * 32, 0 * 32, 32, 32), 144, 150, null);

				if (bDeath) {
					s = "Outch";
					g.drawString(s, 160 - g.getFontMetrics().stringWidth(s)/2, 258);
				} else {
					s = "You needed";
					g.drawString(s, 160 - g.getFontMetrics().stringWidth(s)/2, 225);
					
					s = "steps";
					g.drawString(s, 160 - g.getFontMetrics().stringWidth(s)/2, 285);

					y = 2;
					if (steps <= gold) {
						g.setColor(new Color(255, 235, 0));
						y = 3;
					}
					if (steps > silver) {
						g.setColor(new Color(215, 100, 50));
						y = 1;
					}
					s = String.valueOf(steps);
					g.drawString(s, 160 - g.getFontMetrics().stringWidth(s)/2, 258);
					for (i = 0; i < y; i++) {
						g.drawImage(iBackground.getSubimage(0 * 32, 1 * 32, 32, 32), 144 - (y-1) * 24 + 40*i, 315, null);
					}
				}
			}
			
			// Render das Ganze auf den Bildschirm
			appletGraphics.drawImage(screen, 0, 0, null);

			try {
				Thread.sleep(10);
			} catch (Exception e) { /** nicht schön aber selten */
			};

			if (!isActive()) {
				return;
			}
		}
	}
	
	@Override
    public void processEvent(AWTEvent e) {
		if ((e.getID() == KeyEvent.KEY_PRESSED) || (e.getID() == KeyEvent.KEY_RELEASED)) {
			boolean bSet = false;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				bSet = true;
			}
			KeyEvent event = (KeyEvent) e;
			if (event.getKeyCode() == KeyEvent.VK_LEFT) {
				left = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_UP) {
				up = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_DOWN) {
				down = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_N) {
				n = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_P) {
				p = bSet;
			}
			if (event.getKeyCode() == KeyEvent.VK_R) {
				r = bSet;
			}
		}
	}
}