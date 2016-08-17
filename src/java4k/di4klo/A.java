package java4k.di4klo;

/* 
*
* Di4klo - version 1.5
*
* Copyright (C) 2011-2012 Stephane Racolin 
*
* Contact : hell_in_4k (a) yahoo.fr
*
* This program is free software; you can redistribute it and/or modify it
* under the terms of the GNU General Public License as published by the 
* Free Software Foundation; either version 3 of the License, or (at your 
* option) any later version.
*
* This program is distributed in the hope that it will be useful, but 
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
* for more details.
* 
* You should have received a copy of the GNU General Public License along 
* with this program; if not, see <http://www.gnu.org/licenses>.
*
*/

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import java4k.GamePanel;

public class A extends GamePanel {

	private static final int WIDTH = 768;
	private static final int HEIGHT = 576;

	int i;
	int j;
	int m;
	int n;
	int g;
	int h;
	int x;
	int y;
	int z;
	int q;
	int xr;
	int yr;
	int xs;
	int ys;
	int xt;
	int yt;
	int mem0 = 0;
	int mem1 = 0;
	int zBoardLength = 0;
	int playerFiring = 0;
	int dungeonLevel = 0;
	int difficulty = 0;
	int currentEntity = 0;
	int endCounter = 0;
	int playerMana = 0;
	int equipmentRating = 0;
	int gameState = 1;

	double oldxe;
	double oldye;

	boolean keyPressed = false;

	Rectangle oldRectangle;

	Color colVar;

	int[] levelMap = new int[1024];

	int[] zBoard = new int[255];
	int[] entityData = new int[255];
	int[] spriteSize = new int[255];

	// entity specific variables
	int[] activity = new int[255];
	int[] life = new int[255];
	int[] xeAim = new int[255];
	int[] yeAim = new int[255];
	int[] direction = new int[255];
	int[] animationCounter = new int[255];
	int[] spriteToDraw = new int[255];
	int[] yei = new int[255];
	int[] speed = new int[255];
	int[] eType = new int[255];
	int[] eDIndex = new int[255];
	int[] firingCounter = new int[255];
	int[] firingDelay = new int[255];
	int[] eWidth = new int[255];
	int[] eHeight = new int[255];
	double[] anglee = new double[255];
	double[] xe = new double[255];
	double[] ye = new double[255];
	Rectangle[] eRectangle = new Rectangle[255];
	// end of entity specific variables

	Color[] colorChart = new Color[255];
	BufferedImage[] spriteChart = new BufferedImage[255];

	BufferedImage lifeOrb;
	BufferedImage manaOrb;
	BufferedImage stairs;
	BufferedImage tile;
	BufferedImage backBuffer;
	BufferedImage backGroundImage;

	Graphics2D g2d;
	Graphics2D g2dScr;

	AffineTransform identity = new AffineTransform();
	AffineTransform trans = new AffineTransform();
	Random rand = new Random();

	final int[] colorTable = { 0x000000, 0x00000F, 0x00001F, 0x0000FF, 0x003F00, 0x003F7F, 0x007F00, 0x00FF00, 0x0F0F0F, 0x1F001F, 0x3F0000, 0x3F3F3F, 0x7F0000, 0x7F3F1F, 0xBF0000, 0xFF0000,
			0xFF3F00, 0xFFFF00, 0xFFFFFF, 0xBF7F3F, 0xBFBFBF, 0x7F7F7F, 0x1F1F1F, 0x3F7F00, 0x1F0F0F, 0xFF7F0F };

	final String gameData = "aaagaaagaaagaaagaaaeaabebcasbaasbcarcdalcaascdacdaakdabjdacieeapfabofgcpcdacefbpegcoabafgaamhaaeiaamiaacjaacjaarjabrjacrkmkminkjijiijihfbkeeaaaaeeaaaaaaaaeeaaaaaaaaaaaaaaaaaaeeeeaaaaaeeeeaeaaaeeaeeeaaaaaaaeeaaaaaaaeeeaaaaaaeeaeaaaaaaeaaeeaaaaaeaaaeaaaaeeaaeeaaaaaaeeaaaaaaaaeeaaaaaaaaaaaaaaaaaaeeeeaaaaaeeeeaeaaaeeaeeeaaaaaaaeeaaaaaaaaeeaaaaaaaeaeaaaaaaaeaeaaaaaaaeaeaaaaaaeeeeaaaaaaaaeeaaaaaaaaeeaaaaaaaaaaaaaaaaaeeeeaaaaaeaaeaeaaaeaaeeeaaaaaaaaeaaaaaaaaeeaaaaaaaeaeaaaaaaeaaaeaaaaaeaaaeaaaaeeaaeeaaaaaaaeeaaaaaaaaeeaaaaaaaaaaaaaaaaaeeeeaaaaaeaaeaeaaaeaaeeeaaaaaaaaeaaaaaaaaeeaaaaaaaeaeaaaaaaaeaeaaaaaaaeaeaaaaaaeeeeaaaaaaeaaaaaaeaeaaaaaeaeaaaaaeaeaaaaaeeeaaaaeeeeeaaeeeeeeaaaeeeeeaaaeeeeeaaaeeeeeaaaeeeeeeaaaeeeeeeaaaeeeeeaaaeaaaaaaeaeaaaaaeaeaaaaaeaeaaaaaeeeaaaaeeeeeaaeeeeeeaaaeeeeeaaaeeeeeaaaeeeeeaaaeeeeeeaaeeeeeeaaaeeeeeeaaaaaaaeaaaeaaaaeaeaeaeaeeaaaaaaaeeeeaeaaeaeeeeeaeeaeeeeeaaeeaaaeaeaaaaaaeaaaeaaaaaeaaaeaaaaeaaaaaaaaeaeaaaaeaaaaaeeaeaeaeaeeeeaaaeaeeeeeaeaeaaeeeeeaeaaaeaeaaaeaaeaaaeaaaaaeaaaeaaaeaaeeaeeeeaaeeeaaeaaeeeaaeeaaeaaaeaeeeeeaeaaeeeaaeaaeeeaaaaeeaeaaaaaaaeeaaaeeaeeaaaaeeeaeaaaeeeaeeaaaeaaaeaeeeeeaeeaeeeaaeaaeeeaaeaaeaeeaeaeeaaaeeeaaaaaeeeeeeaaaaaaaeeaaaaaaeeaaaeeeeaaaeeaaaaeaeeaaaeeaaeeeeeaaeeeaaaaaeeeeeeaeaaaaaeeaaaeeeeaaaeeaaaaaaeaaaeeaaeeaaeaaaaeeeeaaaaaaaaaaaaeeeeeeaaeaeeeeeeaeeaaaaaeaeeeeeeeeaeeeeaeeeaeeeeeeeeaaeeeeeeeaeeeeeeeaeeaaaaaeaeaaaaaaeaaeeaaaaaeaeeeeeeeeaeeeeaeeeaeeeeeeeeaaeeeeeeeaaaaeaaeaaeaaaaeaeaaeaeaeaeaaeaeaeaeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";

	private boolean k[] = new boolean[4];
	private int v, w;

	long nextFrameStart;

	public A() {
		super(true);

		lifeOrb = new BufferedImage(29, 29, BufferedImage.TYPE_INT_ARGB);
		manaOrb = new BufferedImage(29, 29, BufferedImage.TYPE_INT_ARGB);
		stairs = new BufferedImage(66, 33, BufferedImage.TYPE_INT_ARGB);
		tile = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
		backBuffer = new BufferedImage(256, 192, BufferedImage.TYPE_INT_RGB);
		backGroundImage = new BufferedImage(4096, 2048, BufferedImage.TYPE_INT_RGB);

		g2dScr = backBuffer.createGraphics();

		// data tables initialization 
		for (i = 0; i < 26; i++)
			colorChart[i] = new Color(colorTable[i]);
		for (i = 0; i < 120; i++)
			entityData[i] = gameData.charAt(i) - 97;
		for (i = 120; i < 140; i++)
			spriteSize[i - 120] = gameData.charAt(i) - 97;

		// draw life and mana orbs
		g2d = lifeOrb.createGraphics();
		g2d.setColor(colorChart[15]);
		g2d.fillOval(0, 0, 29, 29);
		g2d = manaOrb.createGraphics();
		g2d.setColor(colorChart[3]);
		g2d.fillOval(0, 0, 29, 29);

		// sprite uncompression
		for (i = 0; i < 30; i++) {
			m = 2 * entityData[i * 4];
			n = 2 * entityData[i * 4 + 2] + 2;
			h = spriteSize[m] * spriteSize[m + 1];
			if (i > 22)
				n /= 2;

			for (j = 0; j < 4; j++) {
				spriteChart[i * 4 + j] = new BufferedImage(spriteSize[m] * n, spriteSize[m + 1] * n, BufferedImage.TYPE_INT_ARGB);
				g2d = spriteChart[i * 4 + j].createGraphics();
				g2d.setColor(colorChart[entityData[i * 4 + 3]]);

				if (j < 2) {
					z = 0;
					for (g = 0; g < m / 2; g++)
						z += 2 * (spriteSize[g * 2] * spriteSize[g * 2 + 1]);
					for (g = z + j * h; g < z + (1 + j) * h; g++)
						if (gameData.charAt(g + 140) == 'e')
							g2d.fillRect(((g - z - j * h) % spriteSize[m]) * n, ((g - z - j * h) / spriteSize[m]) * n, n, n);

					if (i == 6 || i == 8 || i == 21) {
						g2d.setColor(colorChart[12]); // draw bow
						g2d.fillRect(2, 4, 2, 2);
						g2d.fillRect(2, 16, 2, 2);
						g2d.fillRect(0, 6, 2, 10);
					}

					if (i == 7) {
						g2d.setColor(colorChart[11]); // draw sword
						g2d.fillRect(0, 0, 2, 16);
					}

				} else
					g2d.drawImage(spriteChart[4 * i + j - 2], 0, 0, spriteSize[m] * n, spriteSize[m + 1] * n, spriteSize[m] * n, 0, 0, spriteSize[m + 1] * n, null);

			}

		}

		nextFrameStart = System.nanoTime();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public void paintComponent(Graphics sg) {

		// handle FPS
		long remaining = nextFrameStart - System.nanoTime();
		if (remaining > 0) {
			return;
		}

		do {

			//************************************************
			//************************************************
			// transitions between dungeon levels

			if (gameState != 0) {
				gameState++;

				if (gameState == 48)
					gameState = 0;

				if (gameState == 2) { // game initialization
					endCounter = 0;
					dungeonLevel = 0;
					difficulty = 0;
					equipmentRating = 0;
					life[0] = playerMana = 127;
				}

				if (gameState == 3) { // dungeon level initialization

					// tile graphic creation (128x63 pixels)
					colVar = colorChart[dungeonLevel + 19];
					g2d = tile.createGraphics();
					g2d.setColor(colVar);
					j = 0;

					for (i = 0; i < 63; i++) {
						g2d.drawLine(62 - j, i, 65 + j, i);
						j = (i < 31) ? j + 2 : j - 2;
					}

					// stairs graphic creation (66x33 pixels)
					g2d = stairs.createGraphics();
					g2d.setColor(colorChart[1]);
					if (dungeonLevel == 5)
						g2d.setColor(colorChart[16]);
					j = 0;

					for (i = 0; i < 33; i++) {
						g2d.drawLine(32 - j, i, 33 + j, i);
						j = (i < 16) ? j + 2 : j - 2;
					}

					m = 32;
					n = 4;
					h = 13;
					if (dungeonLevel != 5)
						g2d.setColor(colVar);

					for (i = 0; i < 3; i++) {

						for (j = 0; j < 3; j++) {

							for (g = 0; g < h; g++) {
								g2d.fillRect(m, n, 2, 1);
								if (j != 2 && g != h - 1)
									g2d.fillRect(m, n + 1, 2, 1);
								m += 2;
								n++;
							}

							m -= 28 - 6 * i;
							n -= 12 - 3 * i;
						}

						m += 2;
						n += 5;
						h -= 3;
					}

					// various variable initialization
					zBoardLength = 0;
					h = 0;
					for (i = 0; i < 255; i++)
						activity[i] = -1;

					// 32x32 dungeon level map generation                      
					for (i = 0; i < 1024; i++)
						levelMap[i] = 0;

					if (dungeonLevel == 0) {

						for (i = 0; i < 11; i++)
							for (j = 0; j < 11; j++)
								levelMap[32 * i + j + 100] = 1;
						mem0 = 265; // player start position
						mem1 = 256 * rand.nextInt(2) + 8 * rand.nextInt(2) + 133; // stairs position

					} else {

						while (h < 10) {
							z = rand.nextInt(9);
							z = 192 * (z / 3) + 6 * (z % 3) + 330;

							if (levelMap[z] != 0 || h == 0) {
								g = rand.nextInt(4) - 1;
								if (g == 0)
									g = -32;
								if (g == 2)
									g = 32;
								mem0 = 6 * g + z;

								if (levelMap[mem0] == 0) {
									if (h == 0)
										mem1 = mem0; // stairs position put in room 0
									if (h == 9)
										if (mem0 == mem1 - 6 || mem0 == mem1 + 6 || mem0 == mem1 - 192 || mem0 == mem1 + 192)
											continue; // avoid player and stairs too closed
									if (h != 0 && h != 9)
										life[h + 224] = mem0; // memorize chest position
									h++;
									m = rand.nextInt(3) + 1;
									n = rand.nextInt(3) + 1;
									x = rand.nextInt(4 - m) - 1;
									y = rand.nextInt(4 - n) - 1;
									if (m == 1 && n == 1)
										x = 0;
									for (i = 0; i < n; i++)
										for (j = 0; j < m; j++)
											levelMap[mem0 + x + j + (y + i) * 32] = 1;
								}

								for (i = 0; i < 7; i++)
									levelMap[i * g + z] = 1;

							}

						}

					} // end of dungeon level map generation

					// mem0 contains player start position
					// mem1 contains stairs position

					// player data creation
					xeAim[0] = 64 * (mem0 % 32) - 64 * (mem0 / 32) + 2037;
					yeAim[0] = 32 * (mem0 % 32) + 32 * (mem0 / 32) + 8;
					xe[0] = xeAim[0];
					ye[0] = yeAim[0];
					zBoard[0] = 0;
					activity[0] = 1;
					firingCounter[0] = 1;
					eType[0] = 7;
					speed[0] = 16;
					firingDelay[0] = 16;
					eWidth[0] = 20;
					eHeight[0] = 24;
					eDIndex[0] = 84;
					direction[0] = 84;
					spriteToDraw[0] = 84;
					eRectangle[0] = new Rectangle((int) xe[0] + 4, (int) ye[0] + 12, 12, 12);

					// clear dungeon level image                                
					colVar = colorChart[0];
					if (dungeonLevel == 0)
						colVar = colorChart[13];
					if (dungeonLevel == 6)
						colVar = colorChart[17];
					g2d = backGroundImage.createGraphics();
					g2d.setPaint(colVar);
					g2d.fillRect(0, 0, 4096, 2048);

					// report chest position on level map
					for (i = 225; i < 233; i++)
						levelMap[life[i]] = i;

					// draw dungeon level map, place chests and enemies
					n = 0;
					for (i = 0; i < 1024; i++)
						if (levelMap[i] != 0) {
							j = levelMap[i];
							q = 64 * (i % 32) - 64 * (i / 32) + 1984;
							z = 32 * (i % 32) + 32 * (i / 32);
							g2d.drawImage(tile, q, z, null);

							if (i == mem1 && dungeonLevel != 6)
								g2d.drawImage(stairs, q, z + 15, null);

							if (j > 1 && dungeonLevel != 0) { // if there is a chest
								xe[j] = q + 55;
								ye[j] = z + 23;
								eWidth[j] = 18;
								eHeight[j] = 16;
								eDIndex[j] = 88;
								spriteToDraw[j] = 88;
								eType[j] = 3;
								eRectangle[j] = new Rectangle((int) xe[j], (int) ye[j] + 8, 18, 8);
								if (rand.nextInt(2) == 0)
									activity[j] = 0;
							}

							mem0 = -16777216;
							if (dungeonLevel == 0)
								mem0 = -8437985;
							if (dungeonLevel == 6)
								mem0 = -256;

							for (h = 0; h <= difficulty; h++) {

								do {
									x = rand.nextInt(127) + q;
									y = rand.nextInt(63) + z;
								} while (backGroundImage.getRGB(x, y) == mem0);

								if (dungeonLevel == 0) {
									if (h == 0)
										g2d.drawImage(spriteChart[92], x, y, null);
									if (rand.nextInt(2) == 0)
										continue;
								}

								if (h > 0)
									if (rand.nextInt(5) != 0)
										continue;
								j = 1;
								if (dungeonLevel == 4)
									j = 2; // for spiders
								if (n == 224)
									n = 1; // n = monster population
								n++;
								eType[n] = rand.nextInt(2);

								if (i == mem1) { // if stair position, place level boss
									eType[1] = 2;
									m = n;
									n = 1;
								}

								eDIndex[n] = eType[n] * 4 + dungeonLevel * 12;
								direction[n] = eDIndex[n];
								spriteToDraw[n] = eDIndex[n];
								eWidth[n] = spriteChart[eDIndex[n]].getWidth();
								eHeight[n] = spriteChart[eDIndex[n]].getHeight();
								xe[n] = x - eWidth[n] / 2;
								ye[n] = y - eHeight[n] / j;
								life[n] = eType[n] + 1;
								speed[n] = 7 + j * 3 + dungeonLevel / 2 + difficulty - eType[n];
								firingCounter[n] = 0;
								if (entityData[eDIndex[n] + 1] != 0)
									firingCounter[n] = 63;
								firingDelay[n] = 192 - 2 * (dungeonLevel + difficulty);
								if (Math.abs(xe[n] - xe[0]) > 192 || Math.abs(ye[n] - ye[0]) > 144 || i == mem1)
									activity[n] = 0;
								if (dungeonLevel == 0)
									life[n] = 1;
								xr = (int) xe[n];
								xs = eWidth[n];
								if (entityData[eDIndex[n]] < 2) {
									xr += 4;
									xs -= 8;
								}
								eRectangle[n] = new Rectangle(xr, (int) ye[n] + eHeight[n] / 2, xs, eHeight[n] / 2);

								if (i == mem1) {
									if (dungeonLevel != 0)
										life[1] = 7 + dungeonLevel + difficulty;
									n = m;
								}

							}

						}

				} // end **if (gameState==3) {**

			} // end **if (gameState!=0) {**

			//************************************************
			//************************************************
			// active game playing

			else { // equal **if (gameState==0) {**

				//************************************************
				// suppress inactive entities from zBoard
				for (i = 0; i <= zBoardLength; i++)
					if (activity[zBoard[i]] <= 0) {
						for (j = i; j <= zBoardLength; j++)
							zBoard[j] = zBoard[j + 1];
						zBoardLength--;
					}

				//************************************************
				// handle player movements and shots
				if ((k[1] == true || k[3] == true) && activity[0] > 0) {

					xeAim[0] = (int) xe[0] + v / 3 - 129;
					yeAim[0] = (int) ye[0] + w / 3 - 104;

					anglee[0] = Math.atan2(yeAim[0] - ye[0], xeAim[0] - xe[0]);

					i = direction[0];
					direction[0] = 84; // west facing
					if (Math.abs(anglee[0]) < Math.PI / 2)
						direction[0] = 86; // east facing
					if (i != direction[0])
						spriteToDraw[0] = direction[0];

					playerFiring = 0;

					if (k[3] == true) {
						playerFiring = 1;

						if (firingCounter[0] == 1 && playerMana > 42) {
							firingCounter[0] = 2;
							playerMana -= 42;
						}

					}

				}

				//************************************************        

				if (playerMana < 127)
					playerMana++; // regenerate player mana

				//************************************************
				// missile creation
				for (i = 0; i <= zBoardLength; i++) {

					n = zBoard[i];

					if (firingCounter[n] == 2) {

						if (n == 0) {
							j = 232;
							while (activity[++j] == 1)
								;
							speed[j] = 64;
							eType[j] = 5; // player arrow
							g = yeAim[n] + 24;
							h = xeAim[n] + 10;

						} else {
							j = 240;
							while (activity[++j] == 1 && j < 254)
								;
							speed[j] = dungeonLevel / 2 + difficulty + 48;
							eType[j] = 6; // enemy missile
							g = (int) ye[0] + 12;
							h = (int) xe[0] + 10;
						}

						eDIndex[j] = spriteToDraw[j] = 92 + 4 * entityData[eDIndex[n] + 1];
						eWidth[j] = spriteChart[spriteToDraw[j]].getWidth();
						eHeight[j] = spriteChart[spriteToDraw[j]].getHeight();
						xe[j] = xe[n] + (eWidth[n] - eWidth[j]) / 2;
						ye[j] = ye[n] + (eHeight[n] - eHeight[j]) / 2 + 4;
						anglee[j] = Math.atan2(g - ye[j], h - xe[j]);
						activity[j] = 1;
						eRectangle[j] = new Rectangle((int) xe[j], (int) ye[j] + eHeight[j] / 2, eWidth[j], eHeight[j] / 2);
						zBoard[++zBoardLength] = j;
					}

					if (firingCounter[n] > 1)
						if (++firingCounter[n] == firingDelay[n])
							firingCounter[n] = 1;

				}

				//************************************************
				// handle entity activities
				for (i = 0; i < 47; i++) {
					if (++currentEntity == 255)
						currentEntity = 1;

					if (activity[currentEntity] != -1) { // if living entity
						if (Math.abs(xe[currentEntity] - xe[0]) < 192 && Math.abs(ye[currentEntity] - ye[0]) < 144) { // if entity in border

							if (activity[currentEntity] == 0) { // if deactivated entity, then activate it
								activity[currentEntity] = 1;
								zBoard[++zBoardLength] = currentEntity;
							}

							if (currentEntity < 225) { // if entity is a monster                                                       
								if (firingCounter[currentEntity] == 1 && life[0] > 0)
									firingCounter[currentEntity] = 2; // activate shooting

								if (activity[currentEntity] == 1) { // if no obstacle, entity aim = player 
									xeAim[currentEntity] = (int) xe[0] + 20 - eWidth[currentEntity] / 2;
									yeAim[currentEntity] = (int) ye[0] + 24 - eHeight[currentEntity];
								}

								if (activity[currentEntity] == 2) { // if obstacle, entity aim = random
									xeAim[currentEntity] = (int) xe[currentEntity] + rand.nextInt(63) - 31;
									yeAim[currentEntity] = (int) ye[currentEntity] + rand.nextInt(63) - 31;
								}

								if (activity[currentEntity] != 1)
									if (++activity[currentEntity] == 7)
										activity[currentEntity] = 1;

								anglee[currentEntity] = Math.atan2(yeAim[currentEntity] - ye[currentEntity], xeAim[currentEntity] - xe[currentEntity]);

								j = direction[currentEntity];
								direction[currentEntity] = eDIndex[currentEntity]; // west facing
								if (Math.abs(anglee[currentEntity]) < Math.PI / 2)
									direction[currentEntity] += 2; // east facing
								if (j != direction[currentEntity])
									spriteToDraw[currentEntity] = direction[currentEntity];

							}

						} else
							activity[currentEntity] = 0; // deactivate out of border and alive entity
					}
				}

				//************************************************
				// handle entity movement and collision
				for (i = 0; i <= zBoardLength; i++) {

					n = zBoard[i];
					oldxe = xe[n];
					oldye = ye[n];
					oldRectangle = eRectangle[n];
					mem1 = 0;

					if (Math.abs(xeAim[n] - oldxe) > 1) {
						xe[n] += Math.cos(anglee[n]) * speed[n] / 16;
						mem1 = 1;
					}

					if (Math.abs(yeAim[n] - oldye) > 1) {
						ye[n] += Math.sin(anglee[n]) * speed[n] / 16;
						mem1 = 1;
					}

					x = eWidth[n];
					y = eHeight[n];

					// wall collision
					z = 1;
					if (entityData[eDIndex[n]] == 3)
						z = 2; // for spider
					g = (int) xe[n] + x / 2;
					h = (int) ye[n] + y / z;
					m = backGroundImage.getRGB(g, h);

					if (m == mem0) {

						if (n > 232) { // if it is a missile
							if (dungeonLevel != 3 && dungeonLevel != 6 || eType[n] == 5)
								activity[n] = -1;

						} else { // if it is not a missile
							if (dungeonLevel != 3 && dungeonLevel != 6 || eType[n] == 7) {
								xe[n] = oldxe;
								ye[n] = oldye;
								eRectangle[n] = oldRectangle;
								mem1 = 0;
								if (activity[n] > 0)
									activity[n] = 2;
							}

						}

					}

					// player detection on stairs
					if (n == 0)
						if (m == -16777201 || m == -49408) {
							gameState = 2;
							dungeonLevel++;
							equipmentRating += difficulty + 2;
						}

					// handle collisions between entities
					g = x;
					h = y / 2;
					xr = (int) xe[n];
					yr = (int) ye[n] + h;

					if (entityData[eDIndex[n]] < 2) {
						g -= 8;
						xr += 4;
					}

					eRectangle[n] = new Rectangle(xr, yr, g, h);

					for (j = 0; j <= zBoardLength; j++) {
						m = zBoard[j];
						if (m == n || m > 224 || activity[m] == -1)
							continue;
						if (activity[n] == -1)
							break;

						if (eRectangle[n].intersects(eRectangle[m])) {

							if (m == 0) { // if collision between entity n and player m

								if (eType[n] == 3) { // collision with locked chest
									eType[n] = 4;
									spriteToDraw[n] = 89;
									life[0] = 127;
									playerMana = 127;
									if (equipmentRating > 0)
										equipmentRating--;
								}

								if (eType[n] == 6) { // collision with enemy missile
									activity[n] = -1;
									life[0] -= entityData[eDIndex[n] + 2] + equipmentRating + 4;
								}

								if (n < 225) { // collision with monster
									life[0] -= equipmentRating / 3 + 1;
								}

							}

							if (m != 0 && eType[n] == 5) { // collision between player arrow and monster
								activity[n] = -1;
								if (rand.nextInt(20) >= equipmentRating)
									life[m]--;
							}

							if (n < 225) { // collision between monsters and player

								if (m == 0 || n == 0) { // if monster-player or player-monster collision         
									xe[n] = oldxe;
									ye[n] = oldye;
									eRectangle[n] = oldRectangle;
									mem1 = 0;
								}

								if (m != 0 && n > 1)
									if (activity[n] == 1)
										activity[n] = 2; // if monster-monster or player-monster collision

							}

							// handle monster or player death
							if (life[m] < 1) {

								activity[m] = -1;
								z = eWidth[m] / 2;
								q = z - 2;
								xt = (int) xe[m] + z;
								yt = (int) ye[m] + eHeight[m];

								g2d.setPaint(colorChart[7]); // set default green blood color

								if (m == 0) { // if the player is dead
									endCounter = 1;
									g2d.setPaint(colorChart[15]);
								} else if (dungeonLevel == 4)
									yt -= eHeight[m] / 2; // if a spider is dead 

								if (m == 1 && dungeonLevel == 6)
									endCounter = 1; // if Di4klo is dead

								if (entityData[eDIndex[m]] == 1 || entityData[eDIndex[m]] == 2) { // if a skeleton or a spectre is defeated
									g2d.setPaint(colorChart[entityData[eDIndex[m] + 3]]);
									z -= 2;
									q -= 2;
								}

								for (g = 0; g <= 2 * q; g++) {
									xs = g - q;
									for (h = 0; h <= 2 * q; h++) {
										ys = h - q;
										if (rand.nextInt(xs * xs + ys * ys + 1) < z) {
											xr = 2 * xs + xt;
											yr = yt + ys;
											if (backGroundImage.getRGB(xr, yr) != -16777216)
												g2d.fillRect(xr, yr, 2, 1);
										}

									}

								}

							}

						}

					}

					// block player when firing
					if (n == 0 && playerFiring == 1) {
						xe[0] = oldxe;
						ye[0] = oldye;
						eRectangle[0] = oldRectangle;
						mem1 = 0;
					}

					if (mem1 != 0 && n < 225)
						if (++animationCounter[n] % 7 == 0)
							if (++spriteToDraw[n] % 2 == 0)
								spriteToDraw[n] -= 2;

					yei[n] = (int) ye[n] + y;

				}

				//************************************************
				// reset game if player is dead, or reset difficulty level if Di4klo is dead
				if (endCounter > 0)
					if (++endCounter == 255)
						if (life[0] < 1)
							gameState = 1;

						else if (++difficulty < 3) {
							gameState = 2;
							dungeonLevel = 0;
						}

				//************************************************
				// sort zBoard
				for (j = 1; j <= zBoardLength; j++) {
					m = zBoard[j];
					i = j - 1;
					while ((i >= 0) && (yei[zBoard[i]] > yei[m]))
						zBoard[i + 1] = zBoard[i--];
					zBoard[i + 1] = m;
				}

			} // end of **if (gameState==0) {**

			nextFrameStart += 19922944; // 20971520  /  19922944  /  18874368

		} while (nextFrameStart < System.nanoTime());

		//************************************************                     
		//************************************************
		// display world and entities

		// display background image                
		g2dScr.drawImage(backGroundImage, 0, 0, 256, 192, (int) xe[0] - 119, (int) ye[0] - 80, (int) xe[0] + 137, (int) ye[0] + 112, null);

		//display entity sprites
		for (j = 0; j <= zBoardLength; j++) {
			i = zBoard[j];
			trans.setTransform(identity);
			trans.translate((int) (xe[i] - xe[0] + 119), (int) (ye[i] - ye[0] + 80));
			if (i > 232)
				trans.rotate(anglee[i] + Math.PI / 2);
			g2dScr.drawImage(spriteChart[spriteToDraw[i]], trans, this);
		}

		// display life and mana orbs
		i = (int) (29 * (1 - ((double) life[0] / 127)));
		j = (int) (29 * (1 - ((double) playerMana / 127)));
		g2dScr.drawImage(lifeOrb, 0, 163 + i, 29, 192, 0, i, 29, 29, null);
		g2dScr.drawImage(manaOrb, 226, 163 + j, 255, 192, 0, j, 29, 29, null);

		// display dungeon level, equipment rating and difficulty level                     
		g2dScr.drawString(String.valueOf(-dungeonLevel), 240, 16);
		g2dScr.drawString(String.valueOf((char) (equipmentRating + 65)), 124, 186);

		if (difficulty == 1)
			g2dScr.drawString("N", 224, 16);
		if (difficulty == 2)
			g2dScr.drawString("H", 224, 16);
		if (difficulty == 3)
			g2dScr.drawString("Play at www.java4k.com to discover the true ending message !", 31, 127);

		// display transition screen with game title         
		if (gameState != 0) {
			g2dScr.setColor(colorChart[0]);
			g2dScr.fillRect(0, 0, 256, 192);
			g2dScr.setColor(colorChart[15]);
			g2dScr.drawString("Di4klo", 110, 80 + gameState / 2);
			g2dScr.setColor(colorChart[5]);
		}

		// draw backbuffer on frontbuffer
		sg.drawImage(backBuffer, 0, 0, 768, 576, 0, 0, 256, 192, null);

	} // end run method

	@Override
	public void processAWTEvent(AWTEvent awtEvent) {
		if (awtEvent instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) awtEvent;
			boolean down = false;
			switch (e.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				down = true;
			case MouseEvent.MOUSE_RELEASED:
				k[((MouseEvent) e).getButton()] = down;
			case MouseEvent.MOUSE_MOVED:
			case MouseEvent.MOUSE_DRAGGED:
				v = ((MouseEvent) e).getX();
				w = ((MouseEvent) e).getY();
			}
		}

	}

}

/************************************************************************************************
*
* About Di4klo
*
* DESCRIPTION
* +Story :
* An ancient evil is lurking in the nearby catacombs. Some say they conceal a gate to Hell...
* The elders considered sending the village idiot to investigate the place for an acceptable loss.
* But as he fell ill the night before starting his quest, you were finally chosen to replace him, 
* in your quality of Daemon Hunted...
* 
*
* +Key features :
* - classic hack-and-slash gameplay ;
* - legions of undead, daemons and other foul creatures to battle ;
* - a unique character class, the Daemon Hunted, with a unique skill, Rapid Fire ;
* - a procedurally generated and multi-level dungeon to explore ;
* - three difficulty levels : normal, nightmarish and hellish ;
* - a true ending on hellish difficulty level, although really not worth the pain to reach it ;
* - hardcore mode only.
*
*
* +Production :
* Diablo, by Blizzard Entertainment, was of course a major source of inspiration for this game.
* 
* Other sources include, among many others :
* - Advanced Dungeons & Dragons : Cloudy Mountain, by Mattel Electronics ;
* - Venture, by Exidy ;
* - Gateway to Apshai, by Epyx.
*
*
* INSTRUCTION
* +Controls :
* Left mouse button : move the Daemon Hunted to the location of the mouse cursor. 
* Right mouse button : shoot arrows in the direction of the mouse cursor.
*
*
*+User interface :
*- top right of the screen, you will find the current deepness or dungeon level ;
*- bottom left of the screen, the Life orb ;
*- bottom center of the screen, the Equipment Rating of the Daemon Hunted ( see below ) ;
*- bottom right of the screen, the Mana orb.
*
*
* +Gameplay :
* - as a level 0 character, the Daemon Hunted can't gain experience, levels, new skills nor 
* improve his life and mana ;
* - bottom center of the screen, a letter indicate the Equipment Rating of the Daemon Hunted,
* relative to the power of the enemies in the current dungeon level. It ranges from 
* A for the best to Z for the worst ;
* - the better the Equipment Rating, the better the offense and defense of the Daemon Hunted ;
* - chests can be found across the dungeon, containing new pieces of armor and bows that will
* improve the Equipment Rating, and also potions that immediately restore life and mana ;
* - count on around 15-20 minutes to finish the game in normal difficulty.
*
************************************************************************************************/
