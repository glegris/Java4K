package java4k.castlevania4k;

/*
 * Castlevania 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Castlevania 4K.
 *
 * Castlevania 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Castlevania 4K is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import java4k.GamePanel;

public class a extends GamePanel {

	private static final int WIDTH = 512;
	private static final int HEIGHT = 416;

	final int VK_LEFT = 0x25;
	final int VK_RIGHT = 0x27;
	final int VK_DOWN = 0x28;
	final int VK_WHIP = 0x53;
	final int VK_SUBWEAPON = 0x41;
	final int VK_JUMP = 1;

	final int WHIP_HANGING = 30;
	final int WHIP_BOWED = 25;
	final int WHIP_EXTENDED = 20;
	final int WHIP_THROW = 15;

	final int TYPE_CROSS = 0;
	final int TYPE_FIREBALL = 1;
	final int TYPE_FLAME = 2;
	final int TYPE_DRACULA_HEAD = 3;
	final int TYPE_BRICK = 4;
	final int TYPE_IGOR = 5;

	final int OBJ_X = 0;
	final int OBJ_Y = 1;
	final int OBJ_ANGLE = 2;
	final int OBJ_DX = 3;
	final int OBJ_DY = 4;
	final int OBJ_TYPE = 5;
	final int OBJ_COLLIDES = 6;
	final int OBJ_X1 = 7;
	final int OBJ_Y1 = 8;
	final int OBJ_X2 = 9;
	final int OBJ_Y2 = 10;
	final int OBJ_SPRITE = 11;
	final int OBJ_REVERSED = 12;
	final int OBJ_VX = 13;
	final int OBJ_VY = 14;
	final int OBJ_COUNTER = 15;
	final int OBJ_STATE = 16;

	final int CROSS_STATE_FORWARD = 0;
	final int CROSS_STATE_REVERSING = 1;
	final int CROSS_STATE_REVERSED = 2;

	final int GAME_STATE_ENTRY = 0;
	final int GAME_STATE_RISING = 1;
	final int GAME_STATE_FIGHTING = 2;

	final int DRACULA_STATE_FADE_IN = 0;
	final int DRACULA_STATE_PAUSED = 1;
	final int DRACULA_STATE_FIRING = 2;
	final int DRACULA_STATE_HIDDEN = 3;
	final int DRACULA_STATE_DEAD = 4;

	final int GROUND_Y = 145;
	final float PLAYER_JUMP_SPEED = -3.9375f;
	final float PLAYER_WALK_SPEED = 1.5f;
	final float GRAVITY = 0.23625f;
	final float FLAME_ACCELERATION = -0.013f;
	final float CROSS_ACCELERATION = 0.315f;
	final float CROSS_SPEED = 2.25f;
	final float CROSS_RATIO = .03f;
	final float FIREBALL_SPEED = 1.125f;
	final int IGOR_JUMP_SPEED_1 = -2;
	final int IGOR_JUMP_SPEED_2 = -5;
	final int IGOR_SPEED = 1;

	final String S = "\u100f\u01bc\u03bc\u07fc\u0988\u1188\u32c8\u2c58\u3c74\u2426\u2413\u2409\u2809\uc409\u8411\ufc1f\u100f\u0fb0\u0ff0\u0e70\u0640\u0a40\u1240\u1240\u1bc0\u0c60\u0278\u0128\u0128\u0110\u0210\u03e0\u100f\u1f22\u021e\u043e\u18f8\u31c8\u3e48\u3c78\u2474\u2222\u2213\u2409\u2405\u4409\u4411\u7c3f\u1010\u0000\u0f00\u1f80\u13c0\u11c0\u11f8\u1384\u1c0c\u040e\u0f99\u79fd\u99ff\u95fc\u53bc\u3ff8\u02a8\u1010\u3e00\u7e00\u4f00\u4700\u43c0\u4220\u7460\u3db0\u5310\u2790\u1ff0\u1f48\u1628\u2038\u20f8\u1fb8\u1010\u0000\u3e00\u7f00\u4f00\u4780\u43c0\u4320\u7610\u1cf0\u2988\u13dc\u17f4\u1d9c\u080c\u083a\u09da\u100f\u1e00\u3f00\u2780\u2380\u2380\u6300\ubf86\ufc49\u41a9\uc33d\ufe32\uffa2\ufd7c\u8d00\u8700\u0810\u7800\u9efc\u859f\ue387\u9161\ufbb1\u8afe\ufe8a\u100e\u0f80\u1fc0\u17e0\u13c0\u1ffc\u1802\u1f07\u23f9\uc3fd\u47f5\u5bf5\u33fe\uc18a\u013a\u0806\u1e01\u2434\u0e13\u1008\u7ffc\u83fc\u83fe\u8e79\u4c31\u25c5\u44bd\u83d9\u0818\u827e\u057d\u0905\u120a\u241c\u242c\u242c\u1234\u1214\ube34\u88f4\uff88\u1013\ufe00\ua500\u5f00\u00e0\u00d0\u00f8\u0014\u001a\n\n\u000b\r\t\u0036\u00d2\u008c\u0084\u0088\u00f8\u0806\u7840\u8d88\u1dfa\u0804\ud600\ud629\u0830\u78f8\u3c7c\u3c7c\u7c1c\u3c3c\u783c\u3838\u3c38\u78f8\u3c7c\u3c7c\u7c1c\u3c3c\u783c\u3838\u3c38\u3e3c\u0e0e\u0e0e\u1e0e\u070f\u0303\u3141\uef1f\u0810\u123f\u5332\u97db\u8f9d\u8482\u8484\u8182\u8080\u080a\u523c\u868d\ufaab\uc3bb\uecfb\u0828\u7fff\u7f7f\uffff\u7f7f\u7f7f\u7f7f\u7f7f\u3f7f\u3f3f\u3f3f\u3f3f\u3f3f\u1f3f\u0f0f\u0707\u0303\u0202\u0101\u0f01\u30d1\u080e\u33ff\uf67e\ud0f8\u8060\u4080\u4040\u0080\u0804\u0810\uff3c\u0830\uf3cf\udfb9\u0fbf\udecf\ufe7e\ufafa\uf4f2\uece4\ue8e8\ue8e8\ue8e8\ue4e8\ue4e4\uc8c4\uc4c8\uc3c2\uc2c4\ud2c6\ud2c2\ud1c1\ue1d1\uf1e5\ufdf9\ufffd\u0806\u0704\u0f0f\u3f1f\u101e\u0000\u2c00\u7c00\uff00\ufc80\uf878\ufc04\uea04\u19fc\u08fc\u38fa\u49fa\u4ff4\u3f0c\u07fc\u09fc\u11fe\u1079\u2131\u22c5\u25bd\u38df\u4400\u4400\u4400\u2400\u2400\u2200\u4600\uf800\u1010\u0000\u8000\u4000\ue000\ud000\ue800\uf400\ufc00\ue400\uc200\ua1fc\u9112\u9882\ua491\u83f1\uff39\u1010\u0000\u0001\u0001\u0000\u0003\f\u0033\u00df\u011f\u0f3f\u3f3f\u3da3\u78a4\u78f8\uf989\ufe7f\u1010\uffff\uc00b\u8f07\u8463\u800b\ua007\u8003\u8007\u8003\u8005\u8005\u8005\u8003\u808f\uefff\uffff\u100e\u03c0\u0240\u0240\u0240\u0240\u7e7e\u4002\u4002\u7e7e\u0240\u0240\u0240\u0240\u03c0\u0810\u1000\u0010\u0000\u4202\u0840\u5e18\uc3e7\u7ec3\u0810\u0000\u0000\u1010\u3c18\u2c3c\u6666\u4266\u3c66\u0810\u0000\u1000\u1000\u0010\u2020\u1000\u2810\u1828\u0806\u6e3c\u4747\u386e\u1010\u0000\u1ffc\u3006\u6ffa\u680a\u6d5a\u6552\u6552\u6552\u6d5a\u680a\u6ffa\u7006\u7ffc\u3ff8\u0000\u1010\u3e00\u47c0\u86e0\u8d78\uaec4\udf82\ubf82\uef11\u5ff1\u7f39\u3e19\u6249\u4e49\u676d\u3ffe\u54b8\u1010\u3e30\u47c8\u86c4\u8d42\uaec2\u5f92\ubf22\uef62\u5fe2\u63e2\u2ffe\u299c\u3b18\u4628\uba50\uc3e0";

	final int SPRITE_NONE = -1;
	final int SPRITE_LEGS_1 = 0;
	final int SPRITE_LEGS_2 = 1;
	final int SPRITE_LEGS_3 = 2;
	final int SPRITE_BODY_1 = 3;
	final int SPRITE_BODY_2 = 4;
	final int SPRITE_BODY_3 = 5;
	final int SPRITE_BODY_4 = 6;
	final int SPRITE_BODY_5 = 7;
	final int SPRITE_BODY_6 = 8;
	final int SPRITE_BODY_7 = 9;
	final int SPRITE_KNEELING = 10;
	final int SPRITE_WHIP_HANGING = 11;
	final int SPRITE_WHIP_BOWED = 12;
	final int SPRITE_WHIP_END = 13;
	final int SPRITE_WHIP_MIDDLE = 14;
	final int SPRITE_DRACULA_1 = 15;
	final int SPRITE_DRACULA_2 = 16;
	final int SPRITE_DRACULA_3 = 17;
	final int SPRITE_DRACULA_4 = 18;
	final int SPRITE_DRACULA_5 = 19;
	final int SPRITE_DRACULA_6 = 20;
	final int SPRITE_DRACULA_7 = 21;
	final int SPRITE_DRACULA_8 = 22;
	final int SPRITE_PLAYER_HURT = 23;
	final int SPRITE_DEAD_1 = 24;
	final int SPRITE_DEAD_2 = 25;
	final int SPRITE_BLOCK = 26;
	final int SPRITE_CROSS = 27;
	final int SPRITE_FLAME_1 = 28;
	final int SPRITE_FLAME_2 = 29;
	final int SPRITE_FLAME_3 = 30;
	final int SPRITE_FIREBALL = 31;
	final int SPRITE_TRIPLE = 32;
	final int SPRITE_IGOR_1 = 33;
	final int SPRITE_IGOR_2 = 34;

	final int SPRITES = 35;

	int i;
	int j;
	int k;
	int x;
	int y;
	int z;

	int gameState = GAME_STATE_ENTRY;
	int counter = 0;
	int playerX = 208;
	float playerFx = playerX;
	float playerVy = 0;
	int playerY = GROUND_Y;
	float playerFy = playerY;
	int playerWalkIndex = 0;
	int playerWhipping = 0;
	int playerPower = 16;
	int playerX1 = 0;
	int playerY1 = 0;
	int playerX2 = 0;
	int playerY2 = 0;
	int whipX1 = 0;
	int whipY1 = 0;
	int whipX2 = 0;
	int whipY2 = 0;
	int playerDead = 0;
	int playerStunned = 0;
	boolean playerWalking = false;
	boolean playerReversed = true;
	boolean playerKneeling = false;
	boolean playerJumping = false;
	boolean playerThrowing = false;
	boolean whipReleased = true;
	boolean jumpReleased = true;
	boolean subweaponReleased = true;
	boolean playerHurt = false;

	int headY = 50;
	int draculaX = 216;
	int draculaPower = 32;
	int draculaState = DRACULA_STATE_FADE_IN;
	int draculaCounter = 0xFF;
	boolean draculaReversed = true;
	boolean draculaOpened = false;
	boolean firstFadeIn = true;
	boolean draculaRight = true;

	BufferedImage offscreenImage;
	Graphics2D offscreenGraphics;
	AffineTransform affineTransform = new AffineTransform();
	BufferedImage[][] alphaSprites = new BufferedImage[256][SPRITES];
	BufferedImage[] sprites = alphaSprites[255];
	BufferedImage[] sprites2 = null;
	ArrayList<float[]> queue = new ArrayList<float[]>();
	ArrayList<float[]> crosses = new ArrayList<float[]>();
	Random random = new Random();
	float[] head = new float[32];

	// keys
	private boolean[] a = new boolean[32768];

	long nextFrameStartTime;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	public a() {
		super(true);
		offscreenImage = new BufferedImage(256, 208, 1);
		offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();

		// decompress sprites
		for (z = 0; z < 256; z++) {
			for (i = k = 0; i < SPRITES; i++) {
				j = S.charAt(k++);
				int width = j >> 8;
				int height = j & 0xFF;
				alphaSprites[z][i] = new BufferedImage(width, height, 2);
				for (y = 0; y < height; y++) {
					int[] pixels = new int[16];
					if (width != 8 || (y & 1) == 0) {
						j = S.charAt(k++);
					}
					for (x = 0; x < width; x++) {
						if ((j & 1) == 1) {
							pixels[x] = z << 24;
						}
						j >>= 1;
					}
					alphaSprites[z][i].setRGB(0, y, width, 1, pixels, 0, width);
				}
			}
		}

		nextFrameStartTime = System.nanoTime();
	}

	@Override
	public void paintComponent(Graphics g) {

		// burn off extra cycles
		if (nextFrameStartTime - System.nanoTime() > 0) {
			return;
		}

		do {
			nextFrameStartTime += 16666667;

			// -- update starts ----------------------------------------------------

			counter++;
			if (!a[VK_JUMP]) {
				jumpReleased = true;
			}
			if (!a[VK_WHIP]) {
				whipReleased = true;
			}
			if (!a[VK_SUBWEAPON]) {
				subweaponReleased = true;
			}

			if (gameState == GAME_STATE_RISING) {
				// make head rise
				if ((counter & 1) == 1 && --headY == -10) {
					gameState = GAME_STATE_FIGHTING;
				}
			} else if (gameState == GAME_STATE_FIGHTING) {

				// update dracula
				if (draculaState == DRACULA_STATE_FADE_IN) {
					draculaCounter -= 8;
					if (draculaCounter < 8) {
						if (draculaPower == 0 && playerPower > 0) {
							draculaState = DRACULA_STATE_DEAD;
							draculaCounter = 0;
						} else {
							draculaState = DRACULA_STATE_PAUSED;
							draculaCounter = 24;
							firstFadeIn = false;

							// head is an invisible region used for collision detection only
							queue.add(head);
							head[OBJ_X] = draculaX;
							head[OBJ_Y] = 128;
							head[OBJ_X1] = draculaReversed ? -2 : -6;
							head[OBJ_Y1] = -10;
							head[OBJ_X2] = head[OBJ_X1] + 8;
							head[OBJ_SPRITE] = SPRITE_NONE;
							head[OBJ_COLLIDES] = 1;
							head[OBJ_TYPE] = TYPE_DRACULA_HEAD;
						}
					}
				} else if (draculaState == DRACULA_STATE_PAUSED) {
					if (--draculaCounter == 0) {
						draculaState = DRACULA_STATE_FIRING;
						draculaOpened = true;
						draculaCounter = 64;

						// dracula creates fireball
						if (draculaPower >= 22 || (draculaPower > 10 && draculaPower < 16) || (draculaPower < 6)) {
							float[] fireball = new float[32];
							queue.add(fireball);
							fireball[OBJ_X] = draculaX;
							fireball[OBJ_Y] = 156 + (random.nextInt(2) << 3);
							fireball[OBJ_DX] = -3;
							fireball[OBJ_COLLIDES] = 1;
							fireball[OBJ_X1] = -4;
							fireball[OBJ_Y1] = 0;
							fireball[OBJ_X2] = 3;
							fireball[OBJ_Y2] = 5;
							fireball[OBJ_SPRITE] = SPRITE_FIREBALL;
							fireball[OBJ_TYPE] = TYPE_FIREBALL;
							fireball[OBJ_VX] = FIREBALL_SPEED;
							if (draculaReversed) {
								fireball[OBJ_REVERSED] = 1;
								fireball[OBJ_VX] *= -1;
							}
						}

						if (draculaPower > 10 && draculaPower < 22) {
							// dracula creates igors
							j = 8 + (random.nextInt(8) << 4);
							for (i = 0; i < 2; i++) {
								float[] igor = new float[32];
								queue.add(igor);
								igor[OBJ_X] = j + (i << 7);
								igor[OBJ_Y] = 64;
								igor[OBJ_DX] = -8;
								igor[OBJ_X1] = -8;
								igor[OBJ_X2] = 7;
								igor[OBJ_Y2] = 15;
								igor[OBJ_COLLIDES] = 1;
								igor[OBJ_SPRITE] = SPRITE_IGOR_1;
								igor[OBJ_TYPE] = TYPE_IGOR;
							}
						} else if (draculaPower <= 10) {
							// dracula drops bricks
							j = random.nextInt(4);
							for (i = 0; i < 4; i++) {
								float[] brick = new float[32];
								queue.add(brick);
								brick[OBJ_X] = (j + (i << 2)) << 4;
								brick[OBJ_Y] = 32;
								brick[OBJ_X2] = 15;
								brick[OBJ_Y2] = 15;
								brick[OBJ_COLLIDES] = 1;
								brick[OBJ_SPRITE] = SPRITE_BLOCK;
								brick[OBJ_TYPE] = TYPE_BRICK;
							}
						}
					}
				} else if (draculaState == DRACULA_STATE_FIRING) {
					if (--draculaCounter == 0) {
						draculaState = DRACULA_STATE_HIDDEN;
						draculaCounter = 128;
						queue.remove(head);
					}
				} else if (draculaState == DRACULA_STATE_HIDDEN) {
					if (--draculaCounter == 0) {
						draculaState = DRACULA_STATE_FADE_IN;
						draculaOpened = false;
						draculaCounter = 0xFF;
						if (draculaPower == 0 && playerPower > 0) {
							draculaReversed = false;
							draculaX = 132;
							crosses.clear();
							queue.clear();
						} else {
							draculaRight = !draculaRight;
							draculaX = 40 + (random.nextInt(5) << 4);
							if (draculaRight) {
								draculaX += 112;
							}
							draculaReversed = draculaX + 8 > playerX;
						}
					}
				} else {
					if (draculaCounter < 511) {
						draculaCounter++;

						// dracula fades out and flames as he perishes
						if ((draculaCounter & 7) == 7) {
							float[] flame = new float[32];
							queue.add(flame);
							flame[OBJ_X] = 120 + random.nextInt(12);
							flame[OBJ_Y] = 128 + random.nextInt(32);
							flame[OBJ_SPRITE] = SPRITE_FLAME_1;
							flame[OBJ_TYPE] = TYPE_FLAME;
						}
					} else if (queue.size() - crosses.size() == 0) {
						// game freezes after defeating dracula
						continue;
					}
				}
			} else {
				if (playerX < 80) {
					// when the player walks to the left of the screen, the fight begins
					gameState = GAME_STATE_RISING;
				}
			}

			// update player
			if (playerJumping) {
				playerVy += GRAVITY;
				playerFy += playerVy;
				playerY = (int) playerFy;
				if (playerFy >= GROUND_Y) {
					playerJumping = false;
					playerFy = playerY = GROUND_Y;
				}
			} else {
				if (jumpReleased && a[VK_JUMP] && !a[VK_DOWN] && playerWhipping == 0 && playerDead == 0) {
					playerFy = playerY - 7;
					jumpReleased = false;
					playerJumping = true;
					playerVy = PLAYER_JUMP_SPEED;
				}
			}

			if (playerDead > 0) {
				if (++playerDead > 96) {
					// reset game
					gameState = GAME_STATE_ENTRY;
					playerX = 208;
					playerFx = playerX;
					playerVy = 0;
					playerY = GROUND_Y;
					playerFy = playerY;
					playerWalkIndex = 0;
					playerWhipping = 0;
					playerPower = 16;
					playerStunned = 0;
					playerWalking = false;
					playerReversed = true;
					playerKneeling = false;
					playerJumping = false;
					playerThrowing = false;
					playerHurt = false;

					headY = 50;
					draculaX = 216;
					draculaPower = 32;
					draculaState = DRACULA_STATE_FADE_IN;
					draculaCounter = 0xFF;
					draculaReversed = true;
					draculaOpened = false;
					firstFadeIn = true;
					draculaRight = true;

					crosses.clear();
					queue.clear();
				}
				if (playerDead == 128) {
					playerDead = 0;
				}
			} else if (playerHurt) {
				if (playerReversed) {
					if (playerX < 240) {
						playerFx += PLAYER_WALK_SPEED;
					}
				} else {
					if (playerX > 0) {
						playerFx -= PLAYER_WALK_SPEED;
					}
				}
				playerX = (int) playerFx;
				if (!playerJumping) {
					if (playerPower <= 0) {
						playerDead++;
					} else {
						playerWhipping = 0;
						playerKneeling = false;
						playerHurt = false;
						playerStunned = 64;
					}
				}
			} else {

				if (playerStunned > 0) {
					playerStunned--;
				}

				if (playerWhipping > 0) {
					if (--playerWhipping < WHIP_THROW && playerThrowing) {
						// player throws cross
						playerWhipping = 0;
						float[] cross = new float[32];
						queue.add(cross);
						crosses.add(cross);
						if (playerReversed) {
							cross[OBJ_X] = playerX;
							cross[OBJ_VX] = -CROSS_SPEED;
						} else {
							cross[OBJ_X] = playerX + 16;
							cross[OBJ_VX] = CROSS_SPEED;
						}
						if (cross[OBJ_X] <= 20 || cross[OBJ_X] >= 234) {
							cross[OBJ_STATE] = CROSS_STATE_REVERSED;
						}
						cross[OBJ_Y] = playerY + (playerKneeling ? 16 : 8);
						cross[OBJ_DX] = -8;
						cross[OBJ_DY] = -7;
						cross[OBJ_COLLIDES] = 1;
						cross[OBJ_X1] = -2;
						cross[OBJ_Y1] = -7;
						cross[OBJ_X2] = 2;
						cross[OBJ_Y2] = 7;
						cross[OBJ_SPRITE] = SPRITE_CROSS;
					}
				} else if (whipReleased && a[VK_WHIP]) {
					whipReleased = false;
					playerThrowing = false;
					playerWhipping = WHIP_HANGING;
				}

				playerWalking = false;
				playerKneeling = false;
				if (a[VK_DOWN] && playerY == GROUND_Y) {
					playerKneeling = true;
				} else if (a[VK_LEFT]) {
					if (playerX > 0 && !(playerWhipping > 0 && playerY == GROUND_Y)) {
						playerReversed = true;
						playerWalking = true;
						playerFx -= PLAYER_WALK_SPEED;
					}
				} else if (a[VK_RIGHT]) {
					if (playerX < 240 && !(playerWhipping > 0 && playerY == GROUND_Y)) {
						playerReversed = false;
						playerWalking = true;
						playerFx += PLAYER_WALK_SPEED;
					}
				}
				playerX = (int) playerFx;
				if (playerWalking) {
					if ((counter & 7) == 0) {
						if (++playerWalkIndex == 4) {
							playerWalkIndex = 0;
						}
					}
				} else {
					playerWalkIndex = 0;
				}

				if (crosses.size() < 3 && subweaponReleased && a[VK_SUBWEAPON] && playerWhipping == 0) {
					// player starts to throw cross
					subweaponReleased = false;
					whipReleased = false;
					playerThrowing = true;
					playerWhipping = WHIP_HANGING;
				}

				// compute player hit boundaries
				playerX1 = playerX + 2;
				playerY1 = playerY + (playerKneeling || playerVy < 0 ? 8 : 0);
				playerX2 = playerX + 13;
				playerY2 = playerY + 30;

				if (playerWhipping > 0 && playerWhipping <= WHIP_EXTENDED && !playerThrowing) {
					// compute whip hit boundaries
					whipX1 = playerX + (playerReversed ? -45 : 16);
					whipY1 = playerY1 + 10;
					whipX2 = whipX1 + 44;
					whipY2 = whipY1 + 6;
				}
			}

			// test for player-dracula collision
			if (playerStunned == 0 && draculaPower > 0 && !playerHurt && draculaState != DRACULA_STATE_FADE_IN && draculaState != DRACULA_STATE_HIDDEN) {
				i = draculaX + (draculaReversed ? -8 : (draculaOpened ? -24 : -16));
				if (playerX2 >= i && playerX1 <= i + (draculaOpened ? 32 : 24) && playerY2 >= 128 && playerY1 <= 176) {
					playerPower -= 2;
					playerHurt = true;
					playerJumping = true;
					playerVy = PLAYER_JUMP_SPEED;
				}
			}

			// update objects
			o: for (i = queue.size() - 1; i >= 0; i--) {
				float[] object = queue.get(i);
				if (object[OBJ_TYPE] == TYPE_CROSS) {
					// move cross
					object[OBJ_ANGLE] += object[OBJ_VX] * CROSS_RATIO;
					object[OBJ_X] += object[OBJ_VX];
					if (object[OBJ_STATE] == CROSS_STATE_FORWARD) {
						if ((object[OBJ_VX] < 0 && object[OBJ_X] <= 20) || (object[OBJ_VX] > 0 && object[OBJ_X] >= 234)) {
							object[OBJ_STATE] = CROSS_STATE_REVERSING;
						}
					} else if (object[OBJ_STATE] == CROSS_STATE_REVERSING) {
						if (object[OBJ_X] <= 20) {
							object[OBJ_VX] += CROSS_ACCELERATION;
						} else if (object[OBJ_X] >= 234) {
							object[OBJ_VX] -= CROSS_ACCELERATION;
						} else {
							object[OBJ_STATE] = CROSS_STATE_REVERSED;
						}
					} else {
						if (object[OBJ_X] < -16 || object[OBJ_X] > 256) {
							queue.remove(i);
							crosses.remove(object);
							continue;
						}
					}
				} else if (object[OBJ_TYPE] == TYPE_FIREBALL) {
					object[OBJ_X] += object[OBJ_VX];
					if (object[OBJ_X] < -8 || object[OBJ_X] > 256) {
						queue.remove(i);
						continue;
					}
				} else if (object[OBJ_TYPE] == TYPE_FLAME) {
					if ((counter & 7) == 7) {
						if (++object[OBJ_COUNTER] > 8) {
							queue.remove(i);
							continue;
						}
						if (++object[OBJ_SPRITE] > SPRITE_FLAME_3) {
							object[OBJ_SPRITE] = SPRITE_FLAME_1;
						}
					}
					object[OBJ_VY] += FLAME_ACCELERATION;
					object[OBJ_Y] += object[OBJ_VY];
					object[OBJ_X] += object[OBJ_VX];
				} else if (object[OBJ_TYPE] == TYPE_BRICK) {
					object[OBJ_VY] += GRAVITY;
					object[OBJ_Y] += object[OBJ_VY];
					if (object[OBJ_Y] >= 160) {
						// brick hits ground
						queue.remove(i);
						float[] flame = new float[32];
						queue.add(flame);
						flame[OBJ_X] = object[OBJ_X] + 4;
						flame[OBJ_Y] = object[OBJ_Y] - 4;
						flame[OBJ_SPRITE] = SPRITE_FLAME_1;
						flame[OBJ_TYPE] = TYPE_FLAME;
						continue;
					}
				} else if (object[OBJ_TYPE] == TYPE_IGOR) {
					object[OBJ_VY] += GRAVITY;
					object[OBJ_Y] += object[OBJ_VY];
					if (object[OBJ_Y] >= 160) {
						object[OBJ_Y] = 160;
					}
					if (object[OBJ_Y] == 160) {
						if ((counter & 31) == 0) {
							object[OBJ_SPRITE] = object[OBJ_SPRITE] == SPRITE_IGOR_1 ? SPRITE_IGOR_2 : SPRITE_IGOR_1;
						}
						object[OBJ_REVERSED] = object[OBJ_X] > playerX ? 1 : 0;
						if (object[OBJ_COUNTER]-- == 0) {
							object[OBJ_COUNTER] = 32 + random.nextInt(64);
							object[OBJ_VY] = random.nextBoolean() ? IGOR_JUMP_SPEED_1 : IGOR_JUMP_SPEED_2;
							object[OBJ_VX] = (object[OBJ_X] > playerX) ? -IGOR_SPEED : IGOR_SPEED;
						}
					} else {
						object[OBJ_X] += object[OBJ_VX];
					}
				}

				if (!playerHurt && draculaPower > 0) {
					if (object[OBJ_COLLIDES] == 1) {
						// test for object-player collision
						if (playerX2 >= object[OBJ_X] + object[OBJ_X1] && playerX1 <= object[OBJ_X] + object[OBJ_X2] && playerY2 >= object[OBJ_Y] + object[OBJ_Y1]
								&& playerY1 <= object[OBJ_Y] + object[OBJ_Y2]) {
							if (object[OBJ_TYPE] == TYPE_CROSS) {
								if (object[OBJ_STATE] == CROSS_STATE_REVERSED) {
									crosses.remove(object);
									queue.remove(i);
								}
							} else if (playerStunned == 0 && (object[OBJ_TYPE] == TYPE_FIREBALL || object[OBJ_TYPE] == TYPE_BRICK || object[OBJ_TYPE] == TYPE_IGOR)) {
								queue.remove(i);
								float[] flame = new float[32];
								queue.add(flame);
								flame[OBJ_X] = object[OBJ_X] + object[OBJ_X1] + ((object[OBJ_X2] - object[OBJ_X1] - 8) / 2);
								flame[OBJ_Y] = object[OBJ_Y] + object[OBJ_Y1] + ((object[OBJ_Y2] - object[OBJ_Y1] - 24) / 2);
								flame[OBJ_SPRITE] = SPRITE_FLAME_1;
								flame[OBJ_TYPE] = TYPE_FLAME;

								playerPower -= 2;
								playerHurt = true;
								playerJumping = true;
								playerVy = PLAYER_JUMP_SPEED;
								continue;
							}
						}

						// test for object-whip collision
						if (playerWhipping > 0 && playerWhipping <= WHIP_EXTENDED && !playerThrowing && whipX2 >= object[OBJ_X] + object[OBJ_X1] && whipX1 <= object[OBJ_X] + object[OBJ_X2]
								&& whipY2 >= object[OBJ_Y] + object[OBJ_Y1] && whipY1 <= object[OBJ_Y] + object[OBJ_Y2]) {
							if (object[OBJ_TYPE] == TYPE_FIREBALL || object[OBJ_TYPE] == TYPE_DRACULA_HEAD || object[OBJ_TYPE] == TYPE_IGOR) {
								queue.remove(i);
								float[] flame = new float[32];
								queue.add(flame);
								flame[OBJ_X] = object[OBJ_X] + object[OBJ_X1] + ((object[OBJ_X2] - object[OBJ_X1] - 8) / 2);
								flame[OBJ_Y] = object[OBJ_Y] + object[OBJ_Y1] + ((object[OBJ_Y2] - object[OBJ_Y1] - 24) / 2);
								flame[OBJ_SPRITE] = SPRITE_FLAME_1;
								flame[OBJ_TYPE] = TYPE_FLAME;
								if (object[OBJ_TYPE] == TYPE_DRACULA_HEAD) {
									draculaPower--;
								}
								continue;
							}
						}

						// test for object-cross collision
						if (object[OBJ_TYPE] == TYPE_FIREBALL || object[OBJ_TYPE] == TYPE_DRACULA_HEAD || object[OBJ_TYPE] == TYPE_IGOR) {
							for (j = 0; j < crosses.size(); j++) {
								float[] cross = crosses.get(j);
								if (cross[OBJ_X] + cross[OBJ_X2] >= object[OBJ_X] + object[OBJ_X1] && cross[OBJ_X] + cross[OBJ_X1] <= object[OBJ_X] + object[OBJ_X2]
										&& cross[OBJ_Y] + cross[OBJ_Y2] >= object[OBJ_Y] + object[OBJ_Y1] && cross[OBJ_Y] + cross[OBJ_Y1] <= object[OBJ_Y] + object[OBJ_Y2]) {
									queue.remove(i);
									float[] flame = new float[32];
									queue.add(flame);
									flame[OBJ_X] = object[OBJ_X] + object[OBJ_X1] + ((object[OBJ_X2] - object[OBJ_X1] - 8) / 2);
									flame[OBJ_Y] = object[OBJ_Y] + object[OBJ_Y1] + ((object[OBJ_Y2] - object[OBJ_Y1] - 24) / 2);
									flame[OBJ_SPRITE] = SPRITE_FLAME_1;
									flame[OBJ_TYPE] = TYPE_FLAME;
									if (object[OBJ_TYPE] == TYPE_DRACULA_HEAD) {
										draculaPower--;
									}
									continue o;
								}
							}
						}
					}
				}
			}

			// -- update ends ------------------------------------------------------

		} while (nextFrameStartTime < System.nanoTime());

		// -- render starts ------------------------------------------------------

		// clear frame
		offscreenGraphics.setColor(Color.WHITE);
		offscreenGraphics.fillRect(0, 0, 256, 240);
		offscreenGraphics.setColor(Color.BLACK);

		if (gameState != GAME_STATE_ENTRY && draculaState != DRACULA_STATE_HIDDEN) {
			// draw dracula
			offscreenGraphics.translate(draculaX, 128);
			if (draculaReversed) {
				offscreenGraphics.scale(-1, 1);
			}

			offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_2], 0, 0, null);
			offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_3], -6, -10, null);
			offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_5], -8, 0, null);
			if (draculaOpened) {
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_4], -8, 8, null);
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_7], -24, 0, null);
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_8], -16, 0, null);
				offscreenGraphics.fillRect(-16, 6, 8, 42);
			} else {
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_1], -16, 0, null);
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_6], -8, 44, null);
			}
			if (draculaReversed) {
				offscreenGraphics.translate(1, 0);
			}
			offscreenGraphics.drawLine(7, 16, 7, 47);
			offscreenGraphics.drawLine(0, 47, 7, 47);
			if (draculaOpened) {
				offscreenGraphics.drawLine(-13, 0, -9, 0);
			}

			if (draculaState == DRACULA_STATE_FADE_IN || draculaState == DRACULA_STATE_DEAD) {
				offscreenGraphics.setColor(new Color(0xFFFFFF | ((draculaState == DRACULA_STATE_FADE_IN ? draculaCounter : (draculaCounter >> 1)) << 24), true));
				offscreenGraphics.fillRect(-20, -10, 32, 60);
				offscreenGraphics.setColor(Color.BLACK);
			}

			if (gameState == GAME_STATE_RISING || firstFadeIn) {
				// draw floating head
				offscreenGraphics.drawImage(sprites[SPRITE_DRACULA_3], -7, headY, null);
			}

			offscreenGraphics.setTransform(affineTransform);
		}

		// draw subweapon box
		offscreenGraphics.drawRect(128, 8, 31, 21);
		offscreenGraphics.drawRect(129, 9, 29, 19);
		offscreenGraphics.rotate(1.1f);
		offscreenGraphics.drawImage(sprites[SPRITE_CROSS], 74, -127, null);
		offscreenGraphics.setTransform(affineTransform);

		// draw power bars
		for (i = 0; i < 16; i++) {
			if (playerPower > i) {
				offscreenGraphics.fillRect(56 + (i << 2), 9, 3, 6);
			} else {
				offscreenGraphics.drawRect(56 + (i << 2), 9, 2, 5);
			}
			if (((draculaPower + 1) >> 1) > i) {
				offscreenGraphics.fillRect(56 + (i << 2), 17, 3, 6);
			} else {
				offscreenGraphics.drawRect(56 + (i << 2), 17, 2, 5);
			}
		}

		// draw triple
		offscreenGraphics.drawImage(sprites[SPRITE_TRIPLE], 208, 8, null);

		// draw floor
		for (i = 0; i < 16; i++) {
			offscreenGraphics.drawImage(sprites[SPRITE_BLOCK], i << 4, 176, null);
			offscreenGraphics.drawImage(sprites[SPRITE_BLOCK], i << 4, 192, null);
		}

		// draw player
		offscreenGraphics.translate(playerX + 8, playerY);
		if (playerReversed) {
			offscreenGraphics.scale(-1, 1);
		}

		// player is faded when stunned briefly after a hit
		sprites2 = alphaSprites[(playerStunned == 0) ? 255 : 127];

		if (playerDead < 96 && playerDead > 8) {
			offscreenGraphics.drawImage(sprites[SPRITE_DEAD_1], -16, 15, null);
			offscreenGraphics.drawImage(sprites[SPRITE_DEAD_2], 0, 15, null);
		} else if (playerDead < 96 && playerDead > 0) {
			offscreenGraphics.drawImage(sprites[SPRITE_BODY_1], -8, 7, null);
			offscreenGraphics.drawImage(sprites[SPRITE_KNEELING], -8, 23, null);
		} else if (playerHurt) {
			offscreenGraphics.drawImage(sprites[SPRITE_PLAYER_HURT], -8, 0, null);
		} else if (playerWhipping > 0) {
			if (playerKneeling || (playerJumping && playerVy < 0)) {
				offscreenGraphics.drawImage(sprites2[SPRITE_KNEELING], -8, 23, null);
				offscreenGraphics.translate(0, 7);
			} else {
				offscreenGraphics.drawImage(sprites2[SPRITE_LEGS_1], -8, 16, null);
			}
			if (playerWhipping > WHIP_BOWED) {
				if (!playerThrowing) {
					offscreenGraphics.drawImage(sprites[SPRITE_WHIP_HANGING], -24, 7, null);
				}
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_4], -16, 1, null);
			} else if (playerWhipping > WHIP_EXTENDED) {
				if (!playerThrowing) {
					offscreenGraphics.drawImage(sprites[SPRITE_WHIP_BOWED], -24, 4, null);
				}
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_5], -8, 0, null);
			} else {
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_6], -8, 2, null);
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_7], 8, 10, null);
				if (!playerThrowing) {
					// draw extended whip
					for (i = 0; i < 4; i++) {
						offscreenGraphics.drawImage(sprites[SPRITE_WHIP_MIDDLE], 12 + (i << 3), 12, null);
					}
					offscreenGraphics.drawImage(sprites[SPRITE_WHIP_END], 44, 10, null);
				}
			}
			if (playerKneeling) {
				offscreenGraphics.translate(0, -7);
			}
		} else {
			if (playerKneeling || (playerJumping && playerVy < 0)) {
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_1], -8, 7, null);
				offscreenGraphics.drawImage(sprites2[SPRITE_KNEELING], -8, 23, null);
			} else {
				i = playerJumping ? 0 : (playerWalkIndex == 3 ? 1 : playerWalkIndex);
				offscreenGraphics.drawImage(sprites2[SPRITE_BODY_1 + i], -8, 0, null);
				offscreenGraphics.drawImage(sprites2[SPRITE_LEGS_1 + i], -8, 16, null);
			}
		}
		offscreenGraphics.setTransform(affineTransform);

		// draw objects
		for (i = 0; i < queue.size(); i++) {
			float[] object = queue.get(i);
			if (object[OBJ_SPRITE] >= 0) {
				offscreenGraphics.translate((int) object[OBJ_X], (int) object[OBJ_Y]);

				if (object[OBJ_REVERSED] == 1) {
					offscreenGraphics.scale(-1, 1);
				}
				if (object[OBJ_ANGLE] != 0) {
					offscreenGraphics.rotate(object[OBJ_ANGLE]);
				}
				offscreenGraphics.drawImage(sprites[(int) object[OBJ_SPRITE]], (int) object[OBJ_DX], (int) object[OBJ_DY], null);
				offscreenGraphics.setTransform(affineTransform);
			}
		}

		// draw fade
		if (playerDead > 64) {
			offscreenGraphics.setColor(new Color(((playerDead >= 96) ? (127 - playerDead) : (playerDead - 64)) << 27, true));
			offscreenGraphics.fillRect(0, 0, 256, 208);
		}

		// -- render ends --------------------------------------------------------

		// show the hidden buffer
		g.drawImage(offscreenImage, 0, 0, WIDTH, HEIGHT, null);

	}

	@Override
	public void processAWTEvent(AWTEvent e) {
		if (e instanceof KeyEvent) {
			KeyEvent keyEvent = (KeyEvent) e;
			final int VK_LEFT = 0x25;
			final int VK_RIGHT = 0x27;
			final int VK_DOWN = 0x28;
			final int VK_WHIP = 0x53; // S
			final int VK_SUBWEAPON = 0x41; // A
			final int VK_JUMP = 1;

			int k = keyEvent.getKeyCode();
			if (k > 0) {
				a[(k == VK_LEFT || k == VK_RIGHT || k == VK_DOWN || k == VK_WHIP || k == VK_SUBWEAPON) ? k : VK_JUMP] = keyEvent.getID() != 402;
			}
		}
	}

	// to run in window, uncomment below
	/*public static void main(String[] args) throws Throwable {
	  javax.swing.JFrame frame = new javax.swing.JFrame("Castlevania 4K");
	  frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	  a applet = new a();
	  applet.setPreferredSize(new java.awt.Dimension(512, 416));
	  frame.add(applet, java.awt.BorderLayout.CENTER);
	  frame.setResizable(false);
	  frame.pack();
	  frame.setLocationRelativeTo(null);
	  frame.setVisible(true);
	  Thread.sleep(250);
	  applet.start();
	}*/
}
