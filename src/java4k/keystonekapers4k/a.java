package java4k.keystonekapers4k;

/*
 * Keystone Kapers 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Keystone Kapers 4K.
 *
 * Keystone Kapers 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Keystone Kapers 4K is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class a extends Applet implements Runnable {

	// keys
	private boolean[] a = new boolean[32768];

	@Override
	public void start() {
		enableEvents(8);
		new Thread(this).start();
	}

	public void run() {

		final String S = "\u0000\u00ec\uecec\u6f6f\u6f32\u8432\uaaaa\uaafc\ufc54\ue8e8\u4ad2\ud240\ubbbb\u35a2\ua22a\u8686\u1d48\u4800\ud2a4\u4a18\u1aa7\ud6d6\ud64a\u4a4a\ua71a\u1ab8\u3232\uc848\u48e8\ucc63\u0044\u0042\u72c2\u65a0\ue142\u9e82\u84fc\ud400\u0000\u0006\u1011\u1212\u1110\u0006\u0202\u0202\u0202\n\u0000\u0606\u0606\u0600\u0000\u000e\u0e13\u1313\u1414\u1414\u1414\u1414\u1414\n\u0000\u0a09\u0808\u0808\u090b\n\u0e0e\u0e0e\u0e0e\u0e0e\u0000\f\u0808\u090b\u0806\u0506\u0708\u090a\u0014\u0000\u0000\f\u0c0c\u0c0c\u0d0d\u0d0d\u0d0d\u0d0d\u0d0d\u0014\u0e0f\u0e0c\u0c0c\u0c0c\u0f0e\u0f0e\u0f0e\u0f0e\u0f0f\u0f0f\u0006\u0000\u0000\u0000\u0001\u0202\u0303\u0405\u0607\u0707\u0707\u0707\u0808\u0808\u0809\u003c\u6666\u6666\u663c\u0018\u1c18\u1818\u183c\u003c\u6260\u3c06\u067e\u003c\u6260\u3060\u623c\u0030\u3834\u327e\u3030\u007e\u0606\u3e60\u623e\u003c\u4606\u3e66\u663c\u007e\u4260\u3018\u1818\u003c\u6666\u3c66\u663c\u003c\u6666\u7c60\u623c\u3c7e\u7e7e\u7e3c\u585a\ufafa\ufefe\u001e\uc0cc\ufbf7\u6f1e\u0406\u001e\uc1cd\ufbf6\u6e1e\u0406\u2481\u0000\u3c66\u5adb\ua5ff\ubde7\ubde7\u0000\u1842\u3c66\u5adb\ua5ff\ubde7\ubde7\u3c24\uffff\uffff\uffff\uffff\uff55\u7f55\u7f55\u7f90\ufe66\u006c\u3810\u387c\ufefe\ufefe\ufe7c\u183c\u3c3c\u7e1c\u3c1c\u1c18\u2c7e\u6e7e\u6e7e\u7c7c\u2c7c\u183c\u3c3c\u7e1c\u3c1c\u1c18\u2cbe\uee7e\u6c7e\u7eff\uc101\u183c\u3c3c\u7e9c\ubc9c\u9c98\u7e2f\u7d6c\u7fcd\u0000\u0000\u183c\u3c3c\u7e1c\u3c1c\u1c18\u2cbe\uee7e\u2c3c\u3f3f\uc140\u183c\u3c3c\u7e1c\u3c1c\u1c18\u2c3c\u3c7c\u7c3c\u7e7e\u0a18\u183c\u3c3c\u7e1c\u3c1c\u1c18\u2c3c\u7c7c\u2c7e\u7e3e\u6204\u3078\u7878\ufc38\u7838\u3830\u1c1e\u3f6f\u7f1e\u0e00\u0000\u1c1c\u1c1c\u3c1c\u1c0c\u0e4e\u7e3e\u0c1c\u3e36\u2663\u0101\u1c1c\u1c1c\u3c1c\u1c4c\u4c7e\u3f1d\u1d3c\u7d4f\u46c0\u0000\u1c1c\u1c1c\u3c1c\u1c0c\u0e4e\u7e3e\u0c1c\u1c14\u1711\u6120\u1c1c\u1c1c\u3c1c\u1c0c\u0c1c\u1c3c\u3c1c\u3c68\u7e0a\u0a18\u1c1c\u1c1c\u3c1c\u1c0c\u0c1c\u3c3c\u1c3c\u7466\u4622\u4204\u3078\u7878\u78fc";

		final float JUMP_SPEED = -1.2f;
		final float GRAVITY = 0.07f;
		final float BALL_GRAVITY = 0.007f;
		final float LOW_BALL_SPEED = -.4f;
		final float HIGH_BALL_SPEED = -.55f;
		final float ROBBER_SPEED = 0.5f;

		final int VK_LEFT = 0x25;
		final int VK_RIGHT = 0x27;
		final int VK_DOWN = 0x28;
		final int VK_JUMP = 0x44;

		final int COLOR_BLACK = 0;
		final int COLOR_WHITE = 1;
		final int COLOR_CITYSCAPE = 2;
		final int COLOR_WALL = 3;
		final int COLOR_BASEMENT = 4;
		final int COLOR_FLOOR_1 = 8;
		final int COLOR_FLOOR_2 = 9;
		final int COLOR_PILLAR = 14;
		final int COLOR_STAIRS = 14;
		final int COLOR_ESCALATOR = 20;
		final int COLOR_ELEVATOR_DOOR = 20;
		final int COLOR_TABLE_BOTTOM = 21;
		final int COLOR_TABLE_TOP = 22;
		final int COLOR_ELEVATOR_BOTTOM = 23;
		final int COLOR_ELEVATOR_TOP = 24;

		final int DIGITS_BLACK = 0;
		final int DIGITS_WHITE = 1;

		final int ORIENTATION_ORIGINAL = 0;
		final int ORIENTATION_REVERSED = 1;

		final int SPRITE_BALL = 0;
		final int SPRITE_CITYSCAPE = 1;
		final int SPRITE_AIRPLANE_1 = 2;
		final int SPRITE_AIRPLANE_2 = 3;
		final int SPRITE_RADIO_1 = 4;
		final int SPRITE_RADIO_2 = 5;
		final int SPRITE_SUITCASE = 6;
		final int SPRITE_SHOPPING_CART = 7;
		final int SPRITE_MONEY_BAG = 8;
		final int SPRITE_COP_STANDING = 9;
		final int SPRITE_COP_RUNNING_1 = 10;
		final int SPRITE_COP_RUNNING_2 = 11;
		final int SPRITE_COP_JUMPING = 11; // same sprite as cop running 2
		final int SPRITE_COP_RUNNING_3 = 12;
		final int SPRITE_COP_RUNNING_4 = 13;
		final int SPRITE_COP_RUNNING_5 = 14;
		final int SPRITE_COP_DUCKING = 15;
		final int SPRITE_ROBBER_1 = 16;
		final int SPRITE_ROBBER_2 = 17;
		final int SPRITE_ROBBER_3 = 18;
		final int SPRITE_ROBBER_4 = 19;
		final int SPRITE_ROBBER_5 = 20;
		final int SPRITE_HAT = 21;

		final int ELEVATOR_OPENING = 8;
		final int ELEVATOR_OPENED = 122;
		final int ELEVATOR_CLOSING = 130;
		final int ELEVATOR_CLOSED = 244;

		final int MAP_MONEY_BAG = 0;
		final int MAP_SUITCASE = 1;
		final int MAP_BALL = 2;
		final int MAP_RADIO = 3;
		final int MAP_SHOPPING_CART = 4;
		final int MAP_AIRPLANE = 5;
		final int MAP_EMPTY = 6;

		final int OBJ_X = 0;
		final int OBJ_Y = 1;
		final int OBJ_SPRITE = 2;
		final int OBJ_ORIENTATION = 3;
		final int OBJ_Y1 = 4;
		final int OBJ_Y2 = 5;
		final int OBJ_Y3 = 6;
		final int OBJ_TYPE = 7;
		final int OBJ_FLOOR = 8;

		final Color[] SKY_COLORS = new Color[36];

		int i;
		int j;
		int k;
		int x;
		int y;
		int z;
		int score = 0;
		int timeCounter = 0;
		int cameraX = 0;
		int stairsCounter = 0;
		int elevatorCounter = 0;
		int penaltyCount = 0;
		int penaltyFloor = 0;
		int enemyDirection = 0;
		int copVx = 0;
		int level = 0;
		int robberCounter = 0;
		int hitPlaneDelay = 0;
		int colorCounter = 0;
		int timeRemaining = 50;
		int stairsOffset = 3;
		int extraLives = 3;
		int room = 7;
		int oldRoom = 7;
		int groundY = 133;
		int elevatorFloor = 1;
		int elevatorDirection = -1;
		int radioSprite = SPRITE_RADIO_1;
		int airplaneSprite = SPRITE_AIRPLANE_1;
		int copX = 1132;
		int copSprite = SPRITE_COP_STANDING;
		int copOrientation = ORIENTATION_ORIGINAL;
		int robberY = 101;
		int robberSprite = SPRITE_ROBBER_1;
		int robberOrientation = ORIENTATION_ORIGINAL;
		int robberVy = -32;

		int[] object;
		final int[] pixels = new int[8];
		final int[] pixels2 = new int[8];
		final int[][] map = new int[4][8];

		boolean jumping = false;
		boolean jumpReleased = true;
		boolean stepping = false;
		boolean insideElevator = false;
		boolean resetRequest = false;
		boolean newGameRequest = false;
		boolean advancing = false;
		boolean playing = false;

		float ballBounceY = 0;
		float ballBounceVy = 0;
		float copVy = 0;
		float copY = 133;
		float robberX = 524;
		float robberVx = ROBBER_SPEED;

		final BufferedImage image = new BufferedImage(152, 177, 1);
		final Graphics2D g = (Graphics2D) image.getGraphics();
		Graphics2D g2 = null;
		final ArrayList<int[]> queue = new ArrayList<int[]>();
		final Random random = new Random();

		// decompress colors
		final Color[] colors = new Color[26];
		final int[] colorValues = new int[26];
		for (i = 0; i < 13; i++) {
			j = S.charAt(i * 3 + 1);
			colorValues[i << 1] = 0xFF000000 | (S.charAt(i * 3) << 8) | (j >> 8);
			colorValues[(i << 1) + 1] = 0xFF000000 | ((j & 0xFF) << 16) | S.charAt(i * 3 + 2);
			colors[i << 1] = new Color(colorValues[i << 1]);
			colors[(i << 1) + 1] = new Color(colorValues[(i << 1) + 1]);
		}

		// decompress palettes
		final int[][] palettes = new int[10][];
		for (i = 0, k = 39; i < 10; i++) {
			palettes[i] = new int[S.charAt(k++)];
			for (j = 0; j < palettes[i].length; j += 2) {
				x = S.charAt(k++);
				palettes[i][j] = x >> 8;
				palettes[i][j + 1] = x & 0xFF;
			}
		}

		// decompress digits
		final BufferedImage[][] digitSprites = new BufferedImage[4096][10];
		for (i = 0; i < 4096; i++) {
			for (j = 0; j < 10; j++) {
				digitSprites[i][j] = new BufferedImage(8, 8, 2);
				for (k = 0; k < 8; k++) {
					x = S.charAt(117 + (j << 2) + (k >> 1));
					if ((k & 1) == 0) {
						x >>= 8;
					}
					for (y = 0; y < 8; y++) {
						pixels[y] = ((x & 1) == 1) ? (i < 2 ? colorValues[i] : Color.HSBtoRGB(i / 32f, 1, 1)) : 0;
						x >>= 1;
					}
					digitSprites[i][j].setRGB(0, k, 8, 1, pixels, 0, 8);
				}
			}
		}

		// decompress sprites
		final BufferedImage[][] sprites = new BufferedImage[2][22];
		j = 157;
		for (i = 0; i < 22; i++) {
			z = S.charAt(106 + (i >> 1));
			if ((i & 1) == 0) {
				z >>= 8;
			}
			z &= 0xFF;
			sprites[ORIENTATION_ORIGINAL][i] = new BufferedImage(8, palettes[z].length, 2);
			sprites[ORIENTATION_REVERSED][i] = new BufferedImage(8, palettes[z].length, 2);
			for (k = 0; k < palettes[z].length; k++) {
				x = S.charAt(j + (k >> 1));
				if ((k & 1) == 0) {
					x >>= 8;
				}
				for (y = 0; y < 8; y++) {
					pixels2[7 - y] = pixels[y] = ((x & 1) == 1) ? colorValues[palettes[z][k]] : 0;
					x >>= 1;
				}
				sprites[ORIENTATION_REVERSED][i].setRGB(0, k, 8, 1, pixels2, 0, 8);
				sprites[ORIENTATION_ORIGINAL][i].setRGB(0, k, 8, 1, pixels, 0, 8);
			}
			j += palettes[z].length >> 1;
		}

		// initialize sky colors
		for (i = 0; i <= 35; i++) {
			float scale = (i / 35f);
			scale = scale * scale * scale;
			scale *= scale;
			SKY_COLORS[i] = new Color(Color.HSBtoRGB((184 * scale - 124) / 360f, .6f + 0.11f * scale, .83f - 0.1f * scale));
		}

		long nextFrameStartTime = System.nanoTime();
		while (true) {

			do {
				nextFrameStartTime += 16666667;

				// -- update starts ----------------------------------------------------

				colorCounter++;
				if (!a[VK_JUMP]) {
					jumpReleased = true;
				}

				if (!playing) {
					if (jumpReleased && a[VK_JUMP]) {
						resetRequest = true;
						newGameRequest = true;
					} else {
						continue;
					}
				}

				if (resetRequest) {
					// reset the game
					if (newGameRequest) {
						level = 0;
						score = 0;
						extraLives = 3;
					}

					playing = true;
					resetRequest = false;
					newGameRequest = false;
					jumpReleased = false;
					jumping = false;
					stepping = false;
					insideElevator = false;
					advancing = false;
					copVx = 0;
					copVy = 0;
					robberCounter = 0;
					timeCounter = 0;
					ballBounceY = 0;
					penaltyCount = 0;
					hitPlaneDelay = 0;
					copOrientation = ORIENTATION_ORIGINAL;
					robberOrientation = ORIENTATION_ORIGINAL;
					copSprite = SPRITE_COP_STANDING;
					robberSprite = SPRITE_ROBBER_1;
					room = 7;
					oldRoom = 7;
					timeRemaining = 50;
					robberY = 101;
					copY = 133;
					robberX = 524;
					copX = 1132;
					robberVy = -32;
					robberVx = ROBBER_SPEED;
					groundY = 133;

					elevatorFloor = random.nextInt(3);
					elevatorDirection = 1 - (random.nextInt(2) << 1);

					// create map randomly
					for (i = 0; i < 4; i++) {
						for (j = 0; j < 8; j++) {
							map[i][j] = (j == 0 || j == 7) ? MAP_EMPTY : (j == 3) ? MAP_SHOPPING_CART + random.nextInt(2) : random.nextInt(6);
							if (map[i][j] > level + MAP_BALL) {
								map[i][j] = MAP_EMPTY;
							}
						}
					}

					queue.clear();
				}

				// update clock
				if (++timeCounter == 128) {
					timeCounter = 0;
					if (penaltyCount == 0 && !advancing) {
						timeRemaining--;
					}
				}

				if (advancing) {
					// player gains points before advancing to next level
					if ((timeCounter & 3) == 3) {
						if (timeRemaining > 0) {
							x = score;
							score += 100 * (level < 8 ? 1 : level < 16 ? 2 : 3);
							if (extraLives < 3 && x / 10000 != score / 10000) {
								extraLives++;
							}
							timeRemaining--;
						} else {
							timeRemaining = 0;
							advancing = false;
							resetRequest = true;
							level++;
						}
					}
					continue;
				}

				if (penaltyCount > 0) {
					if ((timeCounter & 3) == 3) {
						// subject time due to player collision with enemy
						timeRemaining--;
						if (--penaltyCount == 0) {
							for (i = queue.size() - 1; i >= 0; i--) {
								if (queue.get(i)[OBJ_FLOOR] == penaltyFloor) {
									queue.remove(i);
								}
							}
						}
					}
					if (timeRemaining >= 0) {
						continue;
					}
				}

				if (hitPlaneDelay > 0) {
					// pause after player collides with plane
					if (--hitPlaneDelay == 0) {
						// trigger player to lose life
						timeRemaining = -1;
					} else {
						continue;
					}
				}

				if (timeRemaining < 0) {
					timeRemaining = 0;
					if (extraLives > 0) {
						// player lost a life
						extraLives--;
						resetRequest = true;
					} else {
						// game over            
						playing = false;
					}
					continue;
				}

				// update radio and airplane sprites
				if ((timeCounter & 1) == 1) {
					radioSprite = SPRITE_RADIO_1 + random.nextInt(2);
					airplaneSprite = airplaneSprite != SPRITE_AIRPLANE_1 ? SPRITE_AIRPLANE_1 : SPRITE_AIRPLANE_2;
				}

				// update ball bounce
				ballBounceVy += BALL_GRAVITY;
				ballBounceY += ballBounceVy;
				if (ballBounceVy > 0 && ballBounceY >= 0) {
					ballBounceY = 0;
					ballBounceVy = level < 4 ? LOW_BALL_SPEED : HIGH_BALL_SPEED;
				}

				// update elevator
				if (++elevatorCounter == ELEVATOR_CLOSED) {
					elevatorCounter = 0;
					if (elevatorFloor == 0) {
						elevatorDirection = 1;
					} else if (elevatorFloor == 2) {
						elevatorDirection = -1;
					}
					elevatorFloor += elevatorDirection;
				}

				// animate stairs
				if (--stairsCounter < 0) {
					stairsCounter = 3;
					if (--stairsOffset < 0) {
						stairsOffset = 3;
					}
					if (stepping) {
						if (copY == groundY) {
							stepping = false;
						} else {
							copY--;
							copX += groundY == 69 ? 1 : -1;
						}
					}
				}

				// update player
				if (insideElevator) {
					copY = groundY = 69 + (elevatorFloor << 5);
					if (a[VK_DOWN] && elevatorCounter >= ELEVATOR_OPENING && elevatorCounter < ELEVATOR_OPENED) {
						insideElevator = false;
					}
				} else if (!stepping) {
					if (jumping) {
						// player jumps
						copVy += GRAVITY;
						copY += copVy;
						copSprite = SPRITE_COP_JUMPING;
						if (copY >= groundY) {
							copY = groundY;
							jumping = false;
							copSprite = SPRITE_COP_STANDING;
						}
					} else if (a[VK_DOWN]) {
						copSprite = SPRITE_COP_DUCKING;
						copY = groundY + 3;
						copVx = 0;
					} else {
						copY = groundY;
						if (a[VK_LEFT]) {
							copVx = -1;
							copOrientation = ORIENTATION_REVERSED;
						} else if (a[VK_RIGHT]) {
							copVx = 1;
							copOrientation = ORIENTATION_ORIGINAL;
						} else {
							copVx = 0;
						}
						if (jumpReleased && a[VK_JUMP]) {
							if (copX > 520 && copX < 530 && elevatorCounter >= ELEVATOR_OPENING && elevatorCounter < ELEVATOR_OPENED && groundY == 69 + (elevatorFloor << 5)) {
								if (copX < 523) {
									copX = 523;
								} else if (copX > 525) {
									copX = 525;
								}
								insideElevator = true;
							} else {
								jumpReleased = false;
								jumping = true;
								copVy = JUMP_SPEED;
							}
						} else if (copVx != 0) {
							// player runs
							if ((copX & 1) == 1 && ++copSprite > SPRITE_COP_RUNNING_5) {
								copSprite = SPRITE_COP_RUNNING_1;
							}
						} else {
							copSprite = SPRITE_COP_STANDING;
						}
					}
					if (!insideElevator) {
						copX += copVx;
						if (copX < 0) {
							copX = 0;
							copVx = 0;
							copSprite = jumping ? SPRITE_COP_JUMPING : SPRITE_COP_STANDING;
						} else if (copX > 1208) {
							copX = 1208;
							copVx = 0;
							copSprite = jumping ? SPRITE_COP_JUMPING : SPRITE_COP_STANDING;
						}
					}

					// test if player entered escalator
					if ((groundY == 133 || groundY == 69) && copY - copX >= groundY - 91) {
						stepping = true;
						jumping = false;
						copY = groundY - 91 + copX;
						while (((((int) copY) + 1) & 3) != stairsOffset) {
							copX--;
							copY--;
						}
						copSprite = SPRITE_COP_STANDING;
						groundY -= 32;
					} else if (groundY == 101 && copY + copX >= 1211) {
						stepping = true;
						jumping = false;
						copY = 1211 - copX;
						while (((((int) copY) + 1) & 3) != stairsOffset) {
							copX++;
							copY--;
						}
						copSprite = SPRITE_COP_STANDING;
						groundY -= 32;
					}
				}

				// determine which room the player occupies
				room = (copX + 4) / 152;
				if (room != oldRoom) {
					enemyDirection = oldRoom - room;
					ballBounceY = 0;
					ballBounceVy = level < 4 ? LOW_BALL_SPEED : HIGH_BALL_SPEED;

					// create enemies and items for the newly entered room
					queue.clear();
					for (i = 0; i < 4; i++) {
						j = map[i][room];
						if (j != MAP_EMPTY) {
							object = new int[32];
							queue.add(object);
							object[OBJ_TYPE] = j;
							object[OBJ_FLOOR] = i;
							x = room * 152 - 4;
							y = x + ((enemyDirection > 0) ? 0 : 152);
							object[OBJ_ORIENTATION] = (enemyDirection > 0) ? ORIENTATION_REVERSED : ORIENTATION_ORIGINAL;
							if (j == MAP_MONEY_BAG) {
								// create money bag
								object[OBJ_X] = x + 72;
								object[OBJ_Y] = 45 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_MONEY_BAG;
								object[OBJ_Y2] = 10;
							} else if (j == MAP_SUITCASE) {
								// create suitcase
								object[OBJ_X] = x + 72;
								object[OBJ_Y] = 47 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_SUITCASE;
								object[OBJ_Y2] = 9;
							} else if (j == MAP_BALL) {
								// create balls
								object[OBJ_X] = y;
								object[OBJ_Y3] = 51 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_BALL;
								object[OBJ_Y2] = 4;

								if (level > 7) {
									object = new int[32];
									queue.add(object);
									object[OBJ_TYPE] = j;
									object[OBJ_FLOOR] = i;
									object[OBJ_X] = y + ((enemyDirection > 0) ? 64 : -64);
									object[OBJ_Y3] = 51 + (i << 5);
									object[OBJ_SPRITE] = SPRITE_BALL;
									object[OBJ_Y2] = 4;
								}

							} else if (j == MAP_RADIO) {
								// create radios
								object[OBJ_X] = x + (level > 4 ? 40 : 72);
								object[OBJ_Y] = 43 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_RADIO_1;
								object[OBJ_Y1] = 6;
								object[OBJ_Y2] = 13;

								if (level > 4) {
									object = new int[32];
									queue.add(object);
									object[OBJ_TYPE] = j;
									object[OBJ_FLOOR] = i;
									object[OBJ_X] = x + 104;
									object[OBJ_Y] = 43 + (i << 5);
									object[OBJ_SPRITE] = SPRITE_RADIO_1;
									object[OBJ_Y1] = 6;
									object[OBJ_Y2] = 13;
								}
								if (level > 8) {
									object = new int[32];
									queue.add(object);
									object[OBJ_TYPE] = j;
									object[OBJ_FLOOR] = i;
									object[OBJ_X] = x + 72;
									object[OBJ_Y] = 43 + (i << 5);
									object[OBJ_SPRITE] = SPRITE_RADIO_1;
									object[OBJ_Y1] = 6;
									object[OBJ_Y2] = 13;
								}

							} else if (j == MAP_SHOPPING_CART) {
								// create shopping carts
								object[OBJ_X] = y;
								object[OBJ_Y] = 47 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_SHOPPING_CART;
								object[OBJ_Y1] = 2;
								object[OBJ_Y2] = 9;

								if (level > 9) {
									object = new int[32];
									queue.add(object);
									object[OBJ_TYPE] = j;
									object[OBJ_FLOOR] = i;
									object[OBJ_X] = y + ((enemyDirection > 0) ? 64 : -64);
									object[OBJ_Y] = 47 + (i << 5);
									object[OBJ_SPRITE] = SPRITE_SHOPPING_CART;
									object[OBJ_Y1] = 2;
									object[OBJ_Y2] = 9;
									object[OBJ_ORIENTATION] = (enemyDirection > 0) ? ORIENTATION_REVERSED : ORIENTATION_ORIGINAL;
								}

							} else if (j == MAP_AIRPLANE) {
								// create airplane
								object[OBJ_X] = y;
								object[OBJ_Y] = 30 + (i << 5);
								object[OBJ_SPRITE] = SPRITE_AIRPLANE_1;
								object[OBJ_Y2] = 8;
							}
						}
					}
				}
				oldRoom = room;

				// update robber
				robberX += robberVx;
				if (++robberCounter == 4) {
					robberCounter = 0;
					if (++robberSprite > SPRITE_ROBBER_5) {
						robberSprite = SPRITE_ROBBER_1;
					}
				}
				if (robberY == groundY) {
					// robber attempts to evade the cop by switching direction
					if (robberX > copX) {
						robberVx = ROBBER_SPEED;
						robberOrientation = ORIENTATION_ORIGINAL;
					} else {
						robberVx = -ROBBER_SPEED;
						robberOrientation = ORIENTATION_REVERSED;
					}
				}
				if (robberX < -8 || robberX > 1216) {
					if (robberX < -8) {
						robberVx = ROBBER_SPEED;
						robberOrientation = ORIENTATION_ORIGINAL;
					} else {
						robberVx = -ROBBER_SPEED;
						robberOrientation = ORIENTATION_REVERSED;
					}
					if (robberY == 37) {
						robberVy = 32;
					} else if (robberY == 133) {
						robberVy = -32;
					} else if (robberY + robberVy == groundY) {
						// robber attempts to evade the cop by switching direction
						robberVy = -robberVy;
					}
					robberY += robberVy;
					if (robberY == groundY) {
						// robber attempts to evade the cop staying on the same floor
						robberY -= robberVy;
					}
				}

				// update enemies/items
				for (i = queue.size() - 1; i >= 0; i--) {
					object = queue.get(i);
					j = object[OBJ_TYPE];

					if (j == MAP_RADIO) {
						// update radio sprite
						object[OBJ_SPRITE] = radioSprite;
					} else if (j == MAP_AIRPLANE) {
						// update airplane sprite
						object[OBJ_SPRITE] = airplaneSprite;
					} else if (j == MAP_BALL) {
						// update ball bounce y-coordinate
						object[OBJ_Y] = object[OBJ_Y3] + (int) ballBounceY;
					}

					// move enemy
					if (j == MAP_BALL || j == MAP_AIRPLANE || j == MAP_SHOPPING_CART) {
						if (j != MAP_BALL || (timeCounter & 1) == 1) {
							object[OBJ_X] += enemyDirection;
						}
						if (j == MAP_AIRPLANE) {
							if (level > 6) {
								object[OBJ_X] += enemyDirection;
							}
							if (level > 10) {
								object[OBJ_X] += enemyDirection;
							}
							if (level > 11) {
								object[OBJ_X] += enemyDirection;
							}
						}
						if (j == MAP_SHOPPING_CART) {
							if (level > 5 && level < 10) {
								object[OBJ_X] += enemyDirection;
							}
						}
						if (enemyDirection > 0) {
							if (object[OBJ_X] > (room + 1) * 152 + 8) {
								object[OBJ_X] = room * 152 - 16;
							}
						} else {
							if (object[OBJ_X] < room * 152 - 16) {
								object[OBJ_X] = (room + 1) * 152 + 8;
							}
						}
					}

					if (!insideElevator) {
						// test for player-enemy/item collisions
						y = (int) copY + (jumping ? 12 : 19);
						if (copY <= object[OBJ_Y] + object[OBJ_Y2] && y >= object[OBJ_Y] && copX <= object[OBJ_X] + 5 && copX + 5 >= object[OBJ_X]) {
							// player collided with enemy/item
							if (j <= MAP_SUITCASE) {
								map[(groundY - 37) >> 5][room] = MAP_EMPTY;
								x = score;
								score += 50;
								if (extraLives < 3 && x / 10000 != score / 10000) {
									extraLives++;
								}
								queue.remove(i);
							} else if (j == MAP_AIRPLANE) {
								hitPlaneDelay = 60;
							} else {
								penaltyCount = 9;
								penaltyFloor = object[OBJ_FLOOR];
							}
						}
					}
				}

				// test for cop-robber collision
				if (!insideElevator && copY <= robberY + 19 && copY + 19 >= robberY && copX + 1 <= robberX + 7 && copX + 6 >= robberX) {
					advancing = true;
				}

				// -- update ends ------------------------------------------------------

			} while (nextFrameStartTime < System.nanoTime());

			// -- render starts ------------------------------------------------------

			// draw wall
			g.setColor(colors[COLOR_WALL]);
			g.fillRect(0, 62, 152, 90);

			// draw sky
			for (i = 0; i < 36; i++) {
				g.setColor(SKY_COLORS[i]);
				g.drawLine(0, i, 152, i);
			}

			// draw cityscape
			g.setColor(colors[COLOR_CITYSCAPE]);
			g.fillRect(0, 36, 152, 20);
			for (i = -1; i < 5; i++) {
				g.drawImage(sprites[ORIENTATION_ORIGINAL + (i & 1)][SPRITE_CITYSCAPE], 8 + (i << 5), 30, 40 + (i << 5), 36, 0, 0, 8, 6, null);
			}

			// draw basement
			g.setColor(colors[COLOR_BASEMENT]);
			g.fillRect(0, 158, 152, 19);

			// draw map
			g.setColor(colors[COLOR_WALL]);
			g.fillRect(32, 163, 80, 10);
			for (i = 0; i < 4; i++) {
				g.setColor(colors[COLOR_FLOOR_1]);
				g.fillRect(32, 161 + (i << 2), 80, 1);
				g.setColor(colors[COLOR_FLOOR_2]);
				g.fillRect(32, 162 + (i << 2), 80, 1);
			}
			g.setColor(colors[COLOR_BLACK]);
			for (i = 0; i < 3; i++) {
				g.fillRect(35 + i, 163 + i, 2, 1);
				g.fillRect(35 + i, 171 + i, 2, 1);
				g.fillRect(107 - i, 167 + i, 2, 1);
			}
			g.setColor(colors[COLOR_BASEMENT]);
			g.fillRect(66, 163 + (elevatorFloor << 2), 2, 2);
			i = 32 + copX * 5 / 76;
			j = 159 + (((int) copY - 37) >> 3);
			g.setColor(colors[COLOR_BLACK]);
			g.fillRect(i, j, 1, 3);
			i = 32 + (int) robberX * 5 / 76;
			j = 159 + ((robberY - 37) >> 3);
			g.setColor(colors[COLOR_WHITE]);
			g.fillRect(i, j, 1, 3);

			// draw floors
			for (i = 0; i < 4; i++) {
				g.setColor(colors[COLOR_FLOOR_1]);
				g.fillRect(0, 56 + (i << 5), 152, 3);
			}

			// draw score
			j = score;
			x = 54;
			do {
				g.drawImage(digitSprites[DIGITS_WHITE][j % 10], x, 2, null);
				x -= 8;
				j /= 10;
			} while (j > 0);

			// draw time remaining
			j = timeRemaining;
			x = 54;
			for (i = 0; i < 2; i++) {
				g.drawImage(digitSprites[timeRemaining > 10 || advancing || !playing ? DIGITS_BLACK : (colorCounter & 4095)][j % 10], x, 13, null);
				x -= 8;
				j /= 10;
			}

			// draw extra lives
			for (i = 0; i < extraLives; i++) {
				g.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_HAT], 14 + (i << 3), 15, null);
			}

			// draw pillars
			if (room == 2 || room == 4 || room == 6) {
				g.setColor(colors[COLOR_PILLAR]);
				for (i = 0; i < 3; i++) {
					if ((i == 1 && room == 4) || !(i == 1 || room == 4)) {
						for (j = 20; j < 152; j += 32) {
							if (j == 84) {
								j += 4;
							}
							g.fillRect(j, 62 + (i << 5), 4, 27);
						}
					}
				}
			}

			// draw tables
			if (room == 1 || room == 5) {
				// 2 tables, 4 tables, 2 tables
				for (i = 0; i < 3; i++) {
					for (j = 20; j <= 112; j += 28) {
						if (j == 76) {
							j = 84;
						}
						if (i == 1 || j == 20 || j == 112) {
							g.setColor(colors[COLOR_TABLE_BOTTOM]);
							g.fillRect(j, 80 + (i << 5), 12, 9);
							g.setColor(colors[COLOR_TABLE_TOP]);
							g.fillRect(j, 78 + (i << 5), 12, 2);
						}
					}
				}

			} else if (room == 2 || room == 6) {
				// 4 pillars, 2 tables, 4 pillars
				g.setColor(colors[COLOR_TABLE_BOTTOM]);
				g.fillRect(32, 112, 24, 9);
				g.fillRect(88, 112, 24, 9);

				g.setColor(colors[COLOR_TABLE_TOP]);
				g.fillRect(32, 110, 24, 2);
				g.fillRect(88, 110, 24, 2);
			} else if (room == 4) {
				// 2 tables, 4 pillars, 2 tables
				g.setColor(colors[COLOR_TABLE_BOTTOM]);
				g.fillRect(88, 80, 24, 9);
				g.fillRect(88, 144, 24, 9);
				g.fillRect(32, 80, 24, 9);
				g.fillRect(32, 144, 24, 9);

				g.setColor(colors[COLOR_TABLE_TOP]);
				g.fillRect(88, 78, 24, 2);
				g.fillRect(88, 142, 24, 2);
				g.fillRect(32, 78, 24, 2);
				g.fillRect(32, 142, 24, 2);
			} else if (room == 7) {
				// empty, escalator, 2 tables
				g.setColor(colors[COLOR_TABLE_BOTTOM]);
				g.fillRect(32, 144, 24, 9);
				g.fillRect(88, 144, 24, 9);

				g.setColor(colors[COLOR_TABLE_TOP]);
				g.fillRect(32, 142, 24, 2);
				g.fillRect(88, 142, 24, 2);
			} else if (room == 3) {
				// draw elevator        
				for (i = 0; i < 3; i++) {
					// draw elevator interiors
					g.setColor(colors[COLOR_ELEVATOR_TOP]);
					g.fillRect(68, 62 + (i << 5), 8, 2);
					g.setColor(colors[COLOR_ELEVATOR_BOTTOM]);
					g.fillRect(68, 64 + (i << 5), 8, 22);
					g.setColor(colors[COLOR_FLOOR_1]);
					g.fillRect(68, 86 + (i << 5), 8, 2);

					if (insideElevator) {
						// draw player inside elevator
						g.drawImage(sprites[copOrientation][SPRITE_COP_STANDING], copX - cameraX, 66 + (i << 5), null);
					}

					// draw door
					g.setColor(colors[COLOR_ELEVATOR_DOOR]);
					g.fillRect(68, 62 + (i << 5), elevatorFloor == i ? elevatorCounter < ELEVATOR_OPENED ? ELEVATOR_OPENING - elevatorCounter : elevatorCounter < ELEVATOR_CLOSING ? elevatorCounter
							- ELEVATOR_OPENED : 8 : 8, 25);
				}
			}

			// draw enemy and item sprites
			cameraX = room * 152;
			for (i = queue.size() - 1; i >= 0; i--) {
				object = queue.get(i);
				g.drawImage(sprites[object[OBJ_ORIENTATION]][object[OBJ_SPRITE]], object[OBJ_X] - cameraX, object[OBJ_Y], null);
			}

			// draw robber
			g.drawImage(sprites[robberOrientation][robberSprite], (int) robberX - cameraX, robberY, null);

			// draw cop
			if (!insideElevator) {
				g.drawImage(sprites[copOrientation][copSprite], copX - cameraX, (int) copY, null);
			}

			// draw escalators
			if (room == 0 || room == 7) {
				if (room == 7) {
					g.scale(-1, 1);
					g.translate(-143, 0);
				}
				for (j = 0; j < 3; j++) {
					if ((room == 0 && (j == 0 || j == 2)) || (room == 7 && j == 1)) {
						g.translate(0, j << 5);

						g.setColor(colors[COLOR_STAIRS]);
						for (i = 0; i < 7; i++) {
							g.fillRect((i << 2) + 61 + stairsOffset + (room == 7 ? -1 : 0), (i << 2) + 59 + stairsOffset, 4, 4);
						}

						g.setColor(colors[COLOR_ESCALATOR]);
						for (i = 0; i < 36; i++) {
							g.drawLine(i + 60, i + 49, i + 61, i + 49);
							g.drawLine(i + 51, i + 52, i + 54, i + 52);
						}
						g.drawLine(87, 87, 93, 87);
						g.drawLine(87, 88, 92, 88);
						g.drawLine(94, 85, 95, 85);
						g.drawLine(93, 86, 94, 86);
						g.drawLine(52, 49, 61, 49);
						g.drawLine(51, 50, 60, 50);
						g.drawLine(50, 51, 55, 51);

						g.setColor(colors[COLOR_BLACK]);
						g.drawLine(56, 57, 87, 57);
						g.setColor(colors[COLOR_FLOOR_1]);
						g.drawLine(56, 58, 87, 58);
						g.drawLine(87, 89, 92, 89);

						g.translate(0, -(j << 5));
					}
				}
				if (room == 7) {
					g.translate(143, 0);
					g.scale(-1, 1);
				}
			}

			// draw ceilings
			for (i = 0; i < 4; i++) {
				g.setColor(colors[COLOR_FLOOR_2]);
				g.fillRect(0, 59 + (i << 5), 152, 3);
			}

			// -- render ends --------------------------------------------------------

			// show the hidden buffer
			if (g2 == null) {
				g2 = (Graphics2D) getGraphics();
				requestFocus();
			} else {
				g2.drawImage(image, 0, 0, 608, 354, null);
			}

			// burn off extra cycles
			while (nextFrameStartTime - System.nanoTime() > 0) {
				Thread.yield();
			}
		}
	}

	@Override
	public void processKeyEvent(KeyEvent keyEvent) {
		final int VK_LEFT = 0x25;
		final int VK_RIGHT = 0x27;
		final int VK_DOWN = 0x28;
		final int VK_JUMP = 0x44;
		final int VK_A = 0x41;
		final int VK_D = 0x44;
		final int VK_S = 0x53;

		int k = keyEvent.getKeyCode();
		if (k > 0) {
			if (k == VK_A) {
				k = VK_LEFT;
			} else if (k == VK_D) {
				k = VK_RIGHT;
			} else if (k == VK_S) {
				k = VK_DOWN;
			}
			a[(k == VK_LEFT || k == VK_RIGHT || k == VK_DOWN) ? k : VK_JUMP] = keyEvent.getID() != 402;
		}
	}

	// to run in window, uncomment below
	//  public static void main(String[] args) throws Throwable {
	//    javax.swing.JFrame frame = new javax.swing.JFrame("Keystone Kapers 4K");
	//    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	//    a applet = new a();
	//    applet.setPreferredSize(new java.awt.Dimension(608, 354));
	//    frame.add(applet, java.awt.BorderLayout.CENTER);
	//    frame.setResizable(false);
	//    frame.pack();
	//    frame.setLocationRelativeTo(null);
	//    frame.setVisible(true);
	//    Thread.sleep(250);
	//    applet.start();
	//  }
}
