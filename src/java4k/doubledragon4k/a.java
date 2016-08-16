package java4k.doubledragon4k;

/*
 * Double Dragon 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Double Dragon 4K.
 *
 * Double Dragon 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Double Dragon 4K is distributed in the hope that it will be useful,
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
import java.awt.geom.AffineTransform;
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

		final int Z0 = 256;
		final int FLOOR_Y = -64;
		final int FLAT_FLOOR_Y = -59;
		final float ENEMY_WALK_SPEED = 1;
		final float FLYING_SPEED = 4;
		final float FLYING_SPEED_X = 1;
		final float GRAVITY = -0.15f;
		final float ANGLE_SPEED = 0.03f;
		final float STANDING_ANGLE_SPEED = 0.05f;
		final float STANDING_Y_SPEED = 0.16f;

		final int VK_LEFT = 0x25;
		final int VK_RIGHT = 0x27;
		final int VK_UP = 0x26;
		final int VK_DOWN = 0x28;
		final int VK_ATTACK = 0x42;

		final int COLOR_SIDEWALK_1 = 0xB0BDC5;
		final int COLOR_SIDEWALK_2 = 0xA5B0B7;
		final int COLOR_CURB_1 = 0xECC158;
		final int COLOR_CURB_2 = 0xD8A13A;
		final int COLOR_ROAD = 0x6B7984;
		final int COLOR_STORM_DRAIN_1 = 0x7B4A29;
		final int COLOR_STORM_DRAIN_2 = 0x3D2414;

		final int SPRITE_MAN_LEGS_1 = 0;
		final int SPRITE_MAN_LEGS_2 = 1;
		final int SPRITE_MAN_LEGS_3 = 2;
		final int SPRITE_WOMAN_LEGS_1 = 3;
		final int SPRITE_WOMAN_LEGS_2 = 4;
		final int SPRITE_WOMAN_LEGS_3 = 5;
		final int SPRITE_MAN_BODY_1 = 6;
		final int SPRITE_MAN_BODY_2 = 7;
		final int SPRITE_WOMAN_BODY_1 = 8;
		final int SPRITE_WOMAN_BODY_2 = 9;
		final int SPRITE_MAN_HEAD_1 = 10;
		final int SPRITE_MAN_HEAD_2 = 11;
		final int SPRITE_WOMAN_HEAD_1 = 12;
		final int SPRITE_WOMAN_HEAD_2 = 13;
		final int SPRITE_ARM = 14;
		final int SPRITE_MEAT = 15;
		final int SPRITE_HAND = 16;
		final int SPRITE_WINDOW = 17;
		final int SPRITE_BRICKS = 18;
		final int SPRITE_DRAIN = 19;

		final int SPRITE_COUNT = 20;

		final int OBJ_X = 0;
		final int OBJ_Z = 1;
		final int OBJ_REVERSED = 2;
		final int OBJ_LEGS_INDEX = 3;
		final int OBJ_PUNCHING = 4;
		final int OBJ_TYPE = 5;
		final int OBJ_STATE = 6;
		final int OBJ_COUNTER = 7;
		final int OBJ_VX = 8;
		final int OBJ_VZ = 9;
		final int OBJ_POWER = 10;
		final int OBJ_ANGLE = 11;
		final int OBJ_VY = 12;
		final int OBJ_Y = 13;
		final int OBJ_VA = 14;
		final int OBJ_FADE = 15;
		final int OBJ_GENDER = 16;
		final int OBJ_COLOR = 17;
		final int OBJ_PUNCH_DELAY = 18;
		final int OBJ_HITS = 19;
		final int OBJ_BOSS = 20;

		final int TYPE_PLAYER = 0;
		final int TYPE_ENEMY = 1;
		final int TYPE_MEAT = 2;

		final int STATE_PAUSED = 0;
		final int STATE_WALKING = 1;
		final int STATE_STUNNED = 2;
		final int STATE_FLYING = 3;
		final int STATE_GROUNDED = 4;
		final int STATE_STANDING = 5;

		final int GENDER_MALE = 0;
		final int GENDER_FEMALE = 1;

		final int FADE_NONE = 0;
		final int FADE_IN = 1;
		final int FADE_OUT = 2;

		final Color COLOR_POWER_BAR_1 = new Color(0xD8C868);
		final Color COLOR_POWER_BAR_2 = new Color(0xC01810);

		final String S = "\u1010\uff00\u003f\uff0a\uaa3f\ufc2a\ua28f\ufcaa\u8a8f\ufcaa\u0aa3\ufca8\ucaa3\uf2a8\uf2a3\uf0a3\uf003\uc50f\uf153\uc54f\uf14f\uc53f\uf14f\uc53f\uf14f\uc54f\uf14f\uf153\uf153\ufc03\ufc54\uffff\uff00\u1010\ufc00\u003f\uff00\uaa3f\uff0a\ua83f\uff2a\u8a3f\uff2a\u8a3f\uff2a\u8a3f\uff0a\u8a3f\uff2a\u8a3f\uff0a\u08ff\ufc50\u08ff\ufc54\u03ff\ufc50\u13ff\ufc50\u4fff\ufc54\u03ff\ufc15\u14ff\uff00\u003f\u1010\uffc0\u003f\uffc2\ua23f\uffca\ua83f\uffca\uaa3f\ufff2\uaa3f\ufff0\uaa8f\uffc0\u2a8f\uffc4\u0a8f\uff10\u000f\ufc54\u153f\ufc53\u153f\ufc0f\u14ff\ufc4f\u14ff\ufc53\u153f\uff03\uc54f\uffff\uf00f\u1010\uff2a\ua8ff\uff15\u553f\uff11\u503f\uff14\u0a3f\uffc4\u2a8f\ufff0\u2a8f\uffc2\u0aa3\uffca\u02a3\uff2a\u30a3\ufca8\uf28f\ufca3\uf28f\ufc0f\uf23f\ufc4f\uf03f\ufc53\uf13f\uff03\ufc4f\uffff\uff03\u1010\uff2a\ua8ff\ufc55\u54ff\ufc45\u54ff\ufc45\u54ff\ufc00\u00ff\ufcaa\u28ff\ufcaa\u28ff\ufca8\u23ff\ufca8\u23ff\uf0a0\ua3ff\uf280\u8fff\uf280\u8fff\uf200\u8fff\uf000\u0fff\uf050\u43ff\ufc0c\u00ff\u1010\uff2a\ua8ff\uff15\u553f\ufc55\u503f\ufc45\u4a8f\ufc00\u0a8f\ufca8\ucaa3\uf0a3\uf2a3\uf283\uf2a3\uf28f\uf2a3\uca3f\uf28f\uca3f\uf28f\uc8ff\ufc8f\uc13f\ufc8f\uf14f\ufc0f\ufc03\ufc53\uffff\uff00\u1008\ufc00\u2a3f\uf2a1\u000f\ucaa1\u284f\u2821\u2803\u2a80\u2828\u2aaa\u28a8\u2aaa\u28a8\uc0aa\u28a0\u1008\ufc15\u0000\uf214\uaaaa\uc854\uaaaa\u2154\uaaaa\u2155\u2a80\u2055\u403f\uc015\u53ff\uc155\u4fff\u1008\uff00\u2a3f\ufcaa\u00ff\uf2aa\ua8ff\uf2aa\u8a3f\uca80\u550f\uca2a\u14a3\ucaaa\u12a3\uf000\u200f\u1008\uff00\u2a00\ufcaa\u802a\uf2aa\ua8aa\uca2a\u8a2a\u2a81\u5520\u2aa8\u550f\u2aa8\u553f\uc000\ua8ff\u0808\uc003\u1554\u1414\u1284\u1280\u1023\u1223\u0aa3\u0808\uc003\u1554\u1014\u0a84\u2aa0\u20a8\u2020\uc823\u1008\ufc30\u000f\uf004\u0003\uf010\u0003\uc0c0\u020f\uc3c0\u0a0f\uf0c8\uaa3f\uffc2\uaa3f\ufff0\uaa3f\u1008\uff00\u030f\ufc00\u0403\uf000\u0103\uf002\ua0f3\uf00a\ua8ff\uf008\u0a3f\ufc20\u023f\uff08\u023f\u0806\ufc03\u02a8\uaaa8\uaaa8\ua803\u03ff\u1008\uffff\uf00f\uffff\uc453\uffff\u1554\u0ffc\u5554\u2001\u4514\uca85\u5544\u2000\u1453\u0fff\uc00f\u1010\uffff\uffff\ufffc\u57ff\ufff6\ua9ff\ud550\u5a5f\u6aaa\uaaa3\u15a6\uaaa4\uc005\uaaa9\uff56\uaaa9\uffd6\uaaa9\uffc5\uaaa9\uffca\uaa95\uffc5\u6964\ufff1\u9690\ufff5\u6940\ufff0\u540f\ufffc\u003f\u1008\u8555\u5552\u8400\u0012\u855a\u8012\u8556\u9012\u8655\u9012\u8695\u4412\u86a5\u4012\u85a9\u4012\u1008\u0000\u0000\uaa02\uaa2a\u5521\u5524\u1515\u5524\u0000\u0000\u2aaa\u2aaa\u2555\u1555\u0555\u1555\u1010\ua9a5\u56aa\ua695\u5556\u8000\u0006\u4000\u0006\ua556\uaaaa\u955a\uaaaa\u4109\u6596\u4118\u6186\u4618\u6186\u4618\u6186\u4618\u6186\u8618\u6186\u8618\u6186\u8618\u6186\u8618\u6186\uaaaa\uaaaa";

		int i;
		int j;
		int k;
		int x;
		int y;
		int z;

		int fadeState = FADE_OUT;
		int fadeCount = 247;

		int counter = 0;
		int cameraX = 0;
		int minCameraX = 0;
		int enemyPower = 0;
		int enemyPowerCounter = 0;
		int enemyCountdown = 0;
		int enemiesRemaining = 0;
		int enemiesAlive = 0;
		int advancesRemaining = 0;
		int level = 0;
		int hitPoints = 0;

		boolean fadeRed = false;
		boolean advanceLevel = false;
		boolean attackReleased = true;
		boolean walking;
		boolean createBoss = false;

		AffineTransform affineTransform = new AffineTransform();
		int[] pixels = new int[32768];
		int[][][] cityscape = new int[2][24][256];
		BufferedImage[][][] fadedSprites = new BufferedImage[64][64][SPRITE_COUNT];
		BufferedImage[] sprites;
		BufferedImage skyImage = null;
		BufferedImage image = new BufferedImage(256, 256, 1);
		Graphics2D g = (Graphics2D) image.getGraphics();
		Graphics2D g2 = null;
		Random random = new Random();
		ArrayList<float[]> queue = new ArrayList<float[]>();
		float[] player = null;

		// decompress the sprites
		for (int alpha = 0; alpha < 64; alpha++) {
			for (int colors = 0; colors < 64; colors++) {
				for (i = 0, k = 0; i < SPRITE_COUNT; i++) {

					int flesh = 0xF4C9A7;
					float cloth = 0.58f + ((colors >> 3) & 7) / 8f;

					j = S.charAt(k++);
					int width = j >> 8;
					int height = j & 0xFF;
					fadedSprites[alpha][colors][i] = new BufferedImage(width, height, 2);
					for (y = 0; y < height; y++) {
						long value = S.charAt(k++);
						if (width == 16) {
							value <<= 16;
							value |= S.charAt(k++);
						}
						for (x = 0; x < width; x++) {
							z = ((int) value) & 3;
							if (z == 3) {
								pixels[x] = 0;
							} else {
								if (i == SPRITE_DRAIN) {
									pixels[x] = z == 0 ? 0 : z == 2 ? COLOR_STORM_DRAIN_1 : COLOR_STORM_DRAIN_2;
								} else if (i == SPRITE_HAND) {
									pixels[x] = z == 0 ? 0x502000 : z == 2 ? 0xF8B8A8 : 0xB87070;
								} else if (i == SPRITE_MEAT) {
									pixels[x] = z == 0 ? 0x000000 : z == 2 ? 0xEEECD3 : 0xA6674D;
								} else if (i >= SPRITE_MAN_HEAD_1 && i < SPRITE_WINDOW) {
									pixels[x] = z == 0 ? 0x000000 : z == 2 ? flesh : Color.HSBtoRGB(0.16f + (colors & 7) / 8f, 0.64f, 1);
								} else if (i <= SPRITE_MAN_LEGS_3 || i >= SPRITE_WINDOW) {
									pixels[x] = z == 0 ? 0x000000 : Color.HSBtoRGB(cloth, 0.73f, z == 1 ? 0.5f : 1);
								} else if (i <= SPRITE_WOMAN_LEGS_3) {
									pixels[x] = z == 0 ? 0x000000 : z == 1 ? Color.HSBtoRGB(cloth, 0.73f, 1) : flesh;
								} else {
									pixels[x] = z == 0 ? 0x000000 : z == 1 ? Color.HSBtoRGB(cloth, 0.73f, 1) : flesh;
								}
								pixels[x] = (alpha << 26) | (0x3FFFFFF & pixels[x]);
							}
							value >>= 2;
						}
						fadedSprites[alpha][colors][i].setRGB(0, y, width, 1, pixels, 0, width);
					}
				}
			}
		}

		// generate floor image
		int[][] floorPixels = new int[128][256];
		for (x = 0; x < 256; x++) {
			for (y = 0; y < 128; y++) {
				float s = (float) Math.sin(.049f * x);
				float f = 1;
				z = y < 64 ? ((x & 63) == 0 ? COLOR_SIDEWALK_2 : COLOR_SIDEWALK_1) : y < 70 ? ((x & 63) == 0 ? COLOR_CURB_2 : COLOR_CURB_1) : y < 74 ? COLOR_CURB_2 : COLOR_ROAD;
				if (x >= 8 && x < 56 && y >= 76 && y < 105) {
					z = fadedSprites[63][0][SPRITE_DRAIN].getRGB((x - 8) & 15, (y - 9) & 15);
				} else {
					f -= (random.nextFloat() - s * s) / 16 + (127 - y) / 512f;
				}
				i = z >> 16;
				j = (z >> 8) & 0xFF;
				k = z & 0xFF;
				i *= f;
				j *= f;
				k *= f;
				floorPixels[y][x] = (i << 16) | (j << 8) | k;
			}
		}

		// generate floor mapping
		int[][][] floorMapping = new int[64][256][2];
		for (y = 0; y < 64; y++) {
			for (x = 0; x < 256; x++) {
				float K = FLOOR_Y / (float) (FLOOR_Y - y);
				floorMapping[y][x][0] = (int) (128 + K * (x - 128));
				floorMapping[y][x][1] = (int) (Z0 - K * Z0);
			}
		}

		long nextFrameStartTime = System.nanoTime();
		while (true) {

			do {
				nextFrameStartTime += 16666667;

				// -- update starts ----------------------------------------------------

				counter++;
				if (enemyPowerCounter > 0) {
					enemyPowerCounter--;
				}

				if (!a[VK_ATTACK]) {
					attackReleased = true;
				}

				// update fade
				if (fadeState == FADE_IN) {

					if (level == 6) {
						// kill screen at level 6
						continue;
					}

					fadeCount -= 8;
					if (fadeCount <= 0) {
						fadeState = FADE_NONE;
						fadeRed = false;
						fadeCount = 0;
					}
					continue;
				} else if (fadeState == FADE_OUT) {
					fadeCount += 8;
					if (fadeCount >= 255) {
						fadeCount = 255;
						fadeState = FADE_IN;

						if (advanceLevel) {
							level++;
							advanceLevel = false;
						}

						// ---- reset level begin ----

						counter = 0;
						cameraX = 0;
						minCameraX = 128;
						enemyPower = -1;
						enemyPowerCounter = 0;
						enemyCountdown = 0;
						enemiesRemaining = 4 + level;
						enemiesAlive = 0;
						advancesRemaining = 2;
						createBoss = false;
						hitPoints = 25 - (level << 1);

						// create player
						player = new float[64];
						player[OBJ_Y] = FLOOR_Y;
						player[OBJ_Z] = 64;
						player[OBJ_REVERSED] = 1;
						player[OBJ_FADE] = 63;
						player[OBJ_HITS] = 3;
						player[OBJ_POWER] = 100;
						queue.clear();
						queue.add(player);

						// generate cityscape
						x = 0;
						int lastColor = 0;
						int lastHeight = 0;
						while (x < 240) {
							do {
								z = random.nextInt(8) << 3;
								k = 2 + (random.nextInt(6) << 1);
							} while (z == lastColor || k == lastHeight);
							lastColor = z;
							lastHeight = k;
							y = 5 + random.nextInt(4);
							for (i = 0; i < 24; i++) {
								for (j = 0; j < y; j++) {
									cityscape[0][i][j + x] = (i < k) ? -1 : z;
									cityscape[1][i][j + x] = (i < k + 3) ? SPRITE_BRICKS : SPRITE_WINDOW;
								}
							}
							x += y;
						}

						// generate sky image
						for (i = 0; i < 96; i++) {
							for (j = 0; j < 256; j++) {
								pixels[(i << 8) | j] = Color.HSBtoRGB(.06f + 0.17f * level, .92f, i / 95f);
							}
						}
						skyImage = new BufferedImage(256, 96, 1);
						skyImage.setRGB(0, 0, 256, 96, pixels, 0, 256);

						// reset timer
						nextFrameStartTime = System.nanoTime();

						// ---- reset level end ----
					}

					continue;
				}

				if (cameraX >= minCameraX && --enemyCountdown < 0) {
					enemyCountdown = 255;
					if (enemiesRemaining > 0) {
						// create enemy
						float[] enemy = new float[64];
						queue.add(enemy);
						enemy[OBJ_Z] = random.nextInt(123);
						enemy[OBJ_X] = (144 - 0.5f * enemy[OBJ_Z]);
						enemy[OBJ_X] = cameraX + (random.nextBoolean() ? -enemy[OBJ_X] : enemy[OBJ_X]);
						enemy[OBJ_Y] = FLOOR_Y;
						enemy[OBJ_TYPE] = TYPE_ENEMY;
						enemy[OBJ_POWER] = 100;
						enemy[OBJ_FADE] = 63;
						enemy[OBJ_GENDER] = random.nextInt(2);
						enemy[OBJ_COLOR] = 8 + random.nextInt(56);
						enemy[OBJ_HITS] = 3;
						enemy[OBJ_PUNCH_DELAY] = random.nextInt(63);
						enemy[OBJ_BOSS] = createBoss ? 1 : 0;
						createBoss = false;
						enemiesAlive++;
						enemiesRemaining--;
					}
				}

				if (enemiesRemaining == 0 && enemiesAlive == 0) {
					// advance to the next segment
					enemyCountdown = 0;
					minCameraX += 256 + random.nextInt(128);
					enemiesRemaining = level + 4 + random.nextInt(6);
					if (advancesRemaining > 0) {
						if (--advancesRemaining == 0) {
							// create boss
							createBoss = true;
							enemiesRemaining = 12;
						}
					} else {
						advanceLevel = true;
						fadeState = FADE_OUT;
						continue;
					}
				}

				// update player
				if (player[OBJ_STATE] < STATE_STUNNED) {
					if (player[OBJ_PUNCHING] == 0) {
						if (attackReleased && a[VK_ATTACK]) {
							attackReleased = false;
							player[OBJ_LEGS_INDEX] = SPRITE_MAN_LEGS_1;
							player[OBJ_PUNCHING] = 12;
						}
						walking = false;
						if (a[VK_LEFT]) {
							player[OBJ_X]--;
							player[OBJ_REVERSED] = 0;
							walking = true;
						} else if (a[VK_RIGHT]) {
							player[OBJ_X]++;
							player[OBJ_REVERSED] = 1;
							walking = true;
						}
						if (a[VK_UP]) {
							if (player[OBJ_Z] > 0) {
								player[OBJ_Z]--;
								walking = true;
							}
						} else if (a[VK_DOWN]) {
							if (player[OBJ_Z] < 122) {
								player[OBJ_Z]++;
								walking = true;
							}
						}

						// do not allow the player to walk beyond the frame boundaries
						while (true) {
							float MAX_X = 128 - 0.5f * player[OBJ_Z];
							float X = player[OBJ_X] - cameraX;
							if (cameraX <= minCameraX && 8 - X >= MAX_X) {
								player[OBJ_X]++;
							} else if (cameraX >= minCameraX + 256 && 8 + X > MAX_X) {
								player[OBJ_X]--;
							} else {
								break;
							}
						}
						if (walking && (counter & 7) == 7) {
							if (++player[OBJ_LEGS_INDEX] == 4) {
								player[OBJ_LEGS_INDEX] = 0;
							}
						}
					} else {
						player[OBJ_PUNCHING]--;
					}
				}

				// update camera position
				if (player[OBJ_X] < cameraX) {
					if (player[OBJ_X] >= minCameraX) {
						cameraX--;
					}
				} else if (player[OBJ_X] > cameraX) {
					if (player[OBJ_X] <= minCameraX + 256) {
						cameraX++;
					}
				}

				// update objects
				for (i = queue.size() - 1; i >= 0; i--) {
					float[] object = queue.get(i);

					if (object[OBJ_TYPE] == TYPE_PLAYER || object[OBJ_TYPE] == TYPE_ENEMY) {

						if (object[OBJ_STATE] == STATE_STANDING) {

							// person stands back up

							if (object[OBJ_ANGLE] > STANDING_ANGLE_SPEED) {
								object[OBJ_ANGLE] -= STANDING_ANGLE_SPEED;
								object[OBJ_Y] -= STANDING_Y_SPEED;
							} else if (object[OBJ_ANGLE] < -STANDING_ANGLE_SPEED) {
								object[OBJ_ANGLE] += STANDING_ANGLE_SPEED;
								object[OBJ_Y] -= STANDING_Y_SPEED;
							} else {
								object[OBJ_ANGLE] = 0;
								object[OBJ_STATE] = STATE_PAUSED;
								object[OBJ_Y] = FLOOR_Y;
							}
						} else if (object[OBJ_STATE] == STATE_GROUNDED) {

							// person lies on the ground

							if (object[OBJ_POWER] == 0) {
								if (--object[OBJ_FADE] < 0) {

									queue.remove(i);
									if (object == player) {
										// player killed
										fadeRed = true;
										fadeState = FADE_OUT;
									} else {
										// enemy killed
										enemiesAlive--;

										if (object[OBJ_BOSS] == 1) {
											// kill all remaining enemies when the boss is killed
											enemiesRemaining = 0;
											for (j = 0; j < queue.size(); j++) {
												float[] enemy = queue.get(j);
												if (enemy[OBJ_TYPE] == TYPE_ENEMY) {
													enemy[OBJ_POWER] = 0;
													enemy[OBJ_STATE] = STATE_FLYING;
													enemy[OBJ_VY] = FLYING_SPEED;
													enemy[OBJ_PUNCHING] = 0;
													if (player[OBJ_REVERSED] == 0) {
														enemy[OBJ_REVERSED] = 1;
														enemy[OBJ_VX] = -FLYING_SPEED_X;
														enemy[OBJ_VA] = -ANGLE_SPEED;
													} else {
														enemy[OBJ_REVERSED] = 0;
														enemy[OBJ_VX] = FLYING_SPEED_X;
														enemy[OBJ_VA] = ANGLE_SPEED;
													}
												}
											}
										}

										if (random.nextInt(player[OBJ_POWER] < 25 ? 2 : 10) == 0) {
											// create meat
											float[] meat = new float[64];
											queue.add(meat);
											meat[OBJ_COUNTER] = 512;
											meat[OBJ_FADE] = 63;
											meat[OBJ_TYPE] = TYPE_MEAT;
											meat[OBJ_X] = object[OBJ_X] + (object[OBJ_ANGLE] > 0 ? 16 : -16);
											meat[OBJ_Y] = FLOOR_Y;
											meat[OBJ_Z] = object[OBJ_Z] - 5;
											if (meat[OBJ_Z] < 0) {
												meat[OBJ_Z] = 0;
											}
										}
									}

									continue;
								}
							} else if (--object[OBJ_COUNTER] == 0) {
								object[OBJ_STATE] = STATE_STANDING;
							}
						} else if (object[OBJ_STATE] == STATE_FLYING) {

							// person flies across the frame

							object[OBJ_VY] += GRAVITY;
							object[OBJ_Y] += object[OBJ_VY];
							object[OBJ_X] += object[OBJ_VX];
							object[OBJ_ANGLE] += object[OBJ_VA];
							if (object[OBJ_VY] < 0 && object[OBJ_Y] < FLAT_FLOOR_Y) {
								object[OBJ_Y] = FLAT_FLOOR_Y;
								object[OBJ_STATE] = STATE_GROUNDED;
								object[OBJ_COUNTER] = 32;
							}

							// do not allow the person to fly beyond the frame boundaries
							while (true) {
								float X = object[OBJ_X] - cameraX;
								float MAX_X = 128 - 0.5f * object[OBJ_Z];
								if (X + 32 > MAX_X) {
									object[OBJ_X]--;
								} else if (32 - X >= MAX_X) {
									object[OBJ_X]++;
								} else {
									break;
								}
							}
						} else if (object[OBJ_STATE] == STATE_STUNNED) {
							if (--object[OBJ_COUNTER] == 0) {
								object[OBJ_STATE] = STATE_PAUSED;
								object[OBJ_PUNCHING] = 0;
							}
						}
					}

					if (object[OBJ_TYPE] == TYPE_ENEMY) {

						if (player[OBJ_STATE] < STATE_STUNNED && object[OBJ_STATE] < STATE_STUNNED) {

							// collision detection
							j = (int) (object[OBJ_Z] - player[OBJ_Z]);
							j = j < 0 ? -j : j;
							boolean leftOverlap = object[OBJ_X] < player[OBJ_X] && object[OBJ_X] > player[OBJ_X] - 24;
							boolean rightOverlap = object[OBJ_X] > player[OBJ_X] && object[OBJ_X] < player[OBJ_X] + 24;
							if (j < 16) {
								if (((leftOverlap && player[OBJ_REVERSED] == 0) || (rightOverlap && player[OBJ_REVERSED] == 1)) && player[OBJ_PUNCHING] == 10) {
									// player punches enemy
									object[OBJ_STATE] = STATE_STUNNED;
									object[OBJ_COUNTER] = 8;
									enemyPowerCounter = 128;
									object[OBJ_POWER] -= object[OBJ_BOSS] == 1 ? 5 : hitPoints;
									if (object[OBJ_POWER] <= 0 || --object[OBJ_HITS] <= 0) {
										if (object[OBJ_POWER] < 0) {
											object[OBJ_POWER] = 0;
										}
										object[OBJ_HITS] = 3;
										object[OBJ_STATE] = STATE_FLYING;
										object[OBJ_VY] = FLYING_SPEED;
										object[OBJ_PUNCHING] = 0;
										if (player[OBJ_REVERSED] == 0) {
											object[OBJ_REVERSED] = 1;
											object[OBJ_VX] = -FLYING_SPEED_X;
											object[OBJ_VA] = -ANGLE_SPEED;
										} else {
											object[OBJ_REVERSED] = 0;
											object[OBJ_VX] = FLYING_SPEED_X;
											object[OBJ_VA] = ANGLE_SPEED;
										}
									}
									enemyPower = (int) object[OBJ_POWER];
								} else if ((leftOverlap || rightOverlap) && object[OBJ_PUNCHING] == 0) {
									if (--object[OBJ_PUNCH_DELAY] <= 0) {
										// enemy punches player
										object[OBJ_LEGS_INDEX] = SPRITE_MAN_LEGS_1;
										object[OBJ_PUNCHING] = 12;
										object[OBJ_PUNCH_DELAY] = random.nextInt(63);
										player[OBJ_STATE] = STATE_STUNNED;
										player[OBJ_COUNTER] = 8;
										player[OBJ_POWER] -= 5;

										if (player[OBJ_POWER] <= 0 || --player[OBJ_HITS] <= 0) {
											if (player[OBJ_POWER] < 0) {
												player[OBJ_POWER] = 0;
											}
											player[OBJ_HITS] = 3;
											player[OBJ_STATE] = STATE_FLYING;
											player[OBJ_VY] = FLYING_SPEED;
											player[OBJ_PUNCHING] = 0;
											if (object[OBJ_REVERSED] == 0) {
												player[OBJ_REVERSED] = 1;
												player[OBJ_VX] = -FLYING_SPEED_X;
												player[OBJ_VA] = -ANGLE_SPEED;
											} else {
												player[OBJ_REVERSED] = 0;
												player[OBJ_VX] = FLYING_SPEED_X;
												player[OBJ_VA] = ANGLE_SPEED;
											}
										}
									}
								}
							}
						}

						if (object[OBJ_STATE] < STATE_STUNNED) {
							if (object[OBJ_PUNCHING] > 0) {
								object[OBJ_PUNCHING]--;
							} else {
								object[OBJ_REVERSED] = player[OBJ_X] > object[OBJ_X] ? 1 : 0;
								if (--object[OBJ_COUNTER] < 0) {
									object[OBJ_STATE] = random.nextInt(10) > 2 ? STATE_WALKING : STATE_PAUSED;
									object[OBJ_COUNTER] = (1 + random.nextInt(6)) << (object[OBJ_STATE] == STATE_PAUSED ? 4 : 6);
									if (object[OBJ_STATE] == STATE_WALKING) {
										float z2 = random.nextInt(123);
										float x2 = cameraX + (1 - 2 * random.nextFloat()) * (120 - 0.5f * z2);
										if (player[OBJ_STATE] < STATE_STUNNED && random.nextInt(10) > 2) {
											z2 = player[OBJ_Z];
											x2 = player[OBJ_X] + ((object[OBJ_X] < player[OBJ_X]) ? -16 : 16);
										}
										object[OBJ_VX] = x2 - object[OBJ_X];
										object[OBJ_VZ] = z2 - object[OBJ_Z];
										float mag = ENEMY_WALK_SPEED / (float) Math.hypot(object[OBJ_VX], object[OBJ_VZ]);
										object[OBJ_COUNTER] = (int) (1 / mag);
										object[OBJ_VX] *= mag;
										object[OBJ_VZ] *= mag;
									}
								} else if (object[OBJ_STATE] == STATE_WALKING) {
									object[OBJ_X] += object[OBJ_VX];
									object[OBJ_Z] += object[OBJ_VZ];
									if ((counter & 7) == 7) {
										if (++object[OBJ_LEGS_INDEX] == 4) {
											object[OBJ_LEGS_INDEX] = 0;
										}
									}
								}
							}
						}
					} else if (object[OBJ_TYPE] == TYPE_MEAT) {

						if (object[OBJ_COUNTER] > 0) {
							object[OBJ_COUNTER]--;
						} else if (object[OBJ_FADE] > 0) {
							object[OBJ_FADE]--;
						} else {
							queue.remove(i);
							continue;
						}

						float dx = player[OBJ_X] - object[OBJ_X];
						float dy = player[OBJ_Z] - object[OBJ_Z];
						if (dx * dx + dy * dy < 256) {
							queue.remove(i);
							player[OBJ_POWER] += 50;
							if (player[OBJ_POWER] > 100) {
								player[OBJ_POWER] = 100;
							}
						}
					}
				}

				// -- update ends ------------------------------------------------------

			} while (nextFrameStartTime < System.nanoTime());

			// -- render starts ------------------------------------------------------

			// draw sky
			g.drawImage(skyImage, 0, 0, null);

			// draw cityscape
			i = cameraX >> 4;
			j = cameraX & 15;
			for (y = 0; y < 24; y++) {
				for (x = 0; x < 17; x++) {
					if (cityscape[0][y][x + i] >= 0) {
						g.drawImage(fadedSprites[63][cityscape[0][y][x + i]][cityscape[1][y][x + i]], (x << 4) - j, y << 3, null);
					}
				}
			}

			// draw floor
			for (y = 0, k = 0; y < 64; y++) {
				for (x = 0; x < 256; x++, k++) {
					pixels[k] = floorPixels[floorMapping[y][x][1]][0xFF & (floorMapping[y][x][0] + cameraX)];
				}
			}
			image.setRGB(0, 192, 256, 64, pixels, 0, 256);

			// draw sprites      
			for (j = 0; j < 123; j++) {
				for (i = 0; i < queue.size(); i++) {
					float[] object = queue.get(i);
					if (j == (int) object[OBJ_Z]) { // draw in sorted order
						float K = Z0 / (Z0 - object[OBJ_Z]);
						g.translate(K * (object[OBJ_X] - cameraX) + 128, 128 - K * object[OBJ_Y]);
						if (object[OBJ_ANGLE] != 0) {
							g.rotate(object[OBJ_ANGLE]);
						}
						g.scale(object[OBJ_REVERSED] == 1 ? -K : K, K);
						sprites = fadedSprites[(int) object[OBJ_FADE]][(int) object[OBJ_COLOR]];

						if (object[OBJ_BOSS] == 1) {
							g.scale(2, 2);
						}

						if (object[OBJ_TYPE] == TYPE_MEAT) {
							g.drawImage(sprites[SPRITE_MEAT], -8, -5, null);
						} else if (object[OBJ_GENDER] == GENDER_FEMALE) {
							g.drawImage(sprites[3 + (object[OBJ_LEGS_INDEX] > SPRITE_MAN_LEGS_3 ? SPRITE_MAN_LEGS_2 : (int) object[OBJ_LEGS_INDEX])], -8, -13, null);
							if (object[OBJ_STATE] == STATE_STUNNED) {
								g.drawImage(sprites[SPRITE_WOMAN_BODY_1], -6, -21, null);
								g.drawImage(sprites[SPRITE_WOMAN_HEAD_2], -6, -29, null);
							} else if (object[OBJ_PUNCHING] == 0) {
								g.drawImage(sprites[SPRITE_WOMAN_BODY_1], -8, -21, null);
								g.drawImage(sprites[SPRITE_WOMAN_HEAD_1], -8, -29, null);
							} else {
								g.drawImage(sprites[SPRITE_WOMAN_BODY_2], -9, -21, null);
								g.drawImage(sprites[SPRITE_ARM], -17, -23, null);
								g.drawImage(sprites[SPRITE_WOMAN_HEAD_1], -9, -29, null);
							}
						} else {
							g.drawImage(sprites[object[OBJ_LEGS_INDEX] > SPRITE_MAN_LEGS_3 ? SPRITE_MAN_LEGS_2 : (int) object[OBJ_LEGS_INDEX]], -8, -13, null);
							if (object[OBJ_STATE] == STATE_STUNNED) {
								g.drawImage(sprites[SPRITE_MAN_BODY_1], -6, -21, null);
								g.drawImage(sprites[SPRITE_MAN_HEAD_2], -4, -29, null);
							} else if (object[OBJ_PUNCHING] == 0) {
								g.drawImage(sprites[SPRITE_MAN_BODY_1], -8, -21, null);
								g.drawImage(sprites[SPRITE_MAN_HEAD_1], -6, -29, null);
							} else {
								g.drawImage(sprites[SPRITE_MAN_BODY_2], -12, -21, null);
								g.drawImage(sprites[SPRITE_ARM], -20, -23, null);
								g.drawImage(sprites[SPRITE_MAN_HEAD_1], -9, -29, null);
							}
						}

						g.setTransform(affineTransform);
					}
				}
			}

			// draw player power bar
			g.setColor(COLOR_POWER_BAR_2);
			g.fillRect(16, 16, 100, 8);
			g.setColor(COLOR_POWER_BAR_1);
			g.drawRect(16, 16, 100, 8);
			g.fillRect(16, 16, (int) player[OBJ_POWER], 8);

			// draw enemy power bar
			if (enemyPowerCounter > 0) {
				g.setColor(COLOR_POWER_BAR_2);
				g.fillRect(139, 16, 100, 8);
				g.setColor(COLOR_POWER_BAR_1);
				g.drawRect(139, 16, 100, 8);
				g.fillRect(139, 16, enemyPower, 8);
			}

			// draw fade
			if (fadeState != FADE_NONE) {
				g.setColor(new Color(fadeRed ? 255 : 0, 0, 0, fadeCount));
				g.fillRect(0, 0, 256, 256);
			} else if (cameraX < minCameraX && (counter & 32) == 32) {
				// draw hand
				g.drawImage(fadedSprites[63][0][SPRITE_HAND], 192, 120, 24, 16, null);
			}

			// -- render ends --------------------------------------------------------

			// show the hidden buffer
			if (g2 == null) {
				g2 = (Graphics2D) getGraphics();
				requestFocus();
			} else {
				g2.drawImage(image, 0, 0, 512, 512, null);
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
		final int VK_UP = 0x26;
		final int VK_DOWN = 0x28;
		final int VK_ATTACK = 0x42;
		final int VK_W = 0x57;
		final int VK_S = 0x53;
		final int VK_A = 0x41;
		final int VK_D = 0x44;

		int k = keyEvent.getKeyCode();
		if (k > 0) {
			k = k == VK_W ? VK_UP : k == VK_D ? VK_RIGHT : k == VK_A ? VK_LEFT : k == VK_S ? VK_DOWN : k;
			a[(k >= VK_LEFT && k <= VK_DOWN) ? k : VK_ATTACK] = keyEvent.getID() != 402;
		}
	}

	// to run in window, uncomment below
	/*public static void main(String[] args) throws Throwable {
	  javax.swing.JFrame frame = new javax.swing.JFrame("Double Dragon 4K");
	  frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	  a applet = new a();
	  applet.setPreferredSize(new java.awt.Dimension(512, 512));
	  frame.add(applet, java.awt.BorderLayout.CENTER);
	  frame.setResizable(false);
	  frame.pack();
	  frame.setLocationRelativeTo(null);
	  frame.setVisible(true);
	  Thread.sleep(250);
	  applet.start();
	}*/
}
