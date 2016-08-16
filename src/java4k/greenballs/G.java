package java4k.greenballs;

/*
 * Attac4k of the Greenballs
 * Copyright (C) 2012 Jens St‰‰f
 *
 * Attac4k of the Greenballs is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class G extends Applet implements Runnable {
	// SIZE
	final static int WINDOW_WIDTH = 702;
	final static int WINDOW_HEIGHT = 480;
	final static int MAP_WIDTH = 22;
	final static int MAP_HEIGHT = 15;
	final static int TILE_SIZE = 32;

	// TILES
	final static char TILE_SPACE = '0';
	final static char TILE_WALL = '1';
	final static char TILE_LADDER = '2';

	// DIRECTION
	final static int DIRECTION_NONE = -1;
	final static int DIRECTION_LEFT = 0;
	final static int DIRECTION_RIGHT = 1;

	// BOOLEAN
	final static int NO = 0;
	final static int YES = 1;

	// MISC. CONSTANTS
	final static int MAX_NUMBER_OF_ACTIVE_BULLETS = 4;
	final static int MAX_NUMBER_OF_ENEMIES = 7;
	final static int MAX_NUMBER_OF_PARTICLES = 150;
	final static int TIME_BETWEEN_BULLETS = 19;

	// LEVELS
	final static String levels = "111111111111111111111110000000000000000000011000000000000000000001100112100000011211000110000200000000020000011000020000000002000001100002000000000111211110000200000000000020011112111000000000002001100200000000000000200110020000000001211110011001111120000020000001100000002000002000000110000000200000200000011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001100000000000000000000110000012111111210000011000000200000020000111100000020000002000000110011211000000200000011000020000021111120001111002000002000002000110000200000200000200011000011121111110020001100000002000000002000110000000200000000200011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001100111211112000000000110000020000200112110011000002000020000200001100211111002000020000110020000000200002000011002000000020000200001100200001211112110000110020000020000200000011002000002000020000001100111211100001112100110000020000000000200011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001111100000000000000000110000000000000000011111000211000000000000001100020000000000111000110002000000000000000011000111121112112100001100000002000200200000110000000200020020000011000002110002011112001100000200000200000200110000020000020000020011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001100111121001120001121110000002000002000002011000000200000200000201100002111111110002111110000200000000000200011000020000000000020001100002000000012111100110000200000000200000011000020000000011210001100002000000000020000110000200000000002000011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001102111120000000111000110200002000000000000011020011112110000000001102000000200000000000111112100020000000000011000200002001112110001100020000200000200000110002000020000020011111001112111000002000001100000200000001111200110000020000000000020011111111111111111111111111111111111111111111110000000000000000000011000000001110000000001100011100000000111000110000000000000000000011000000000000000000001112100000011121111000110200000000002000000011020001211100200000001102000020000020000000110200002000002000000011020021111111112000001102002000000000200000110200200000000020000011111111111111111111111111111111111111111111110000000000000000000011001120000000001112001100002000000000000200111121100000000000111111002000000000000000001100111200000000012100110000020000000000200011000001111111120020001100000000000002002000110211110000021111100011020000000002000000001111121111111112111121110002000000000200002011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001100000000100000000000111001112110011111210011000000200000000020001100000020000000002000111111111000000000200011000100000012111110001100010000000200000000110000001211111110000011112100020000001001211100200002000000100020110020000200000000002011111111111111111111111111111111111111111111110000000000000000000011000000000000000000001100000000000000000000110011120000000021110011000002001111002000001100000200000000200000110000020000000020000011111001112112111001111100000000200200000000110000000020020000000011001121111001111211001100002000000000020000110000200000000002000011111111111111111111111";

	// PLAYER CONTROLS
	static int PLAYER_MOVING_LEFT = NO;
	static int PLAYER_MOVING_RIGHT = NO;
	static int PLAYER_MOVING_UP = NO;
	static int PLAYER_MOVING_DOWN = NO;
	static int BUTTON_FIRE_PRESSED = NO;

	// SPEED
	final static int PLAYER_SPEED = 2;
	final static int BULLET_SPEED = 4;
	final static int ENEMY_SPEED = 2;

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.enableEvents(AWTEvent.KEY_EVENT_MASK);
		BufferedImage screen = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletGraphics = getGraphics();
		Random random = new Random();

		/* VARIABLES */
		// Images
		BufferedImage mapImage1 = null;
		BufferedImage mapImage2 = null;
		BufferedImage bgImage1 = null;
		BufferedImage bgImage2 = null;

		// Player
		int player_x = 0;
		int player_y = 0;
		int player_falling = NO;
		int player_direction = DIRECTION_RIGHT;
		int player_climbing = NO;
		int player_bulletCounter = 0;

		// Bullets
		int bullet_x[] = new int[MAX_NUMBER_OF_ACTIVE_BULLETS];
		int bullet_y[] = new int[MAX_NUMBER_OF_ACTIVE_BULLETS];
		int bullet_direction[] = new int[MAX_NUMBER_OF_ACTIVE_BULLETS];
		int numberOfActiveBullets = 0;

		// Enemies
		int numberOfActiveEnemies = 0;
		int enemy_x[] = new int[MAX_NUMBER_OF_ENEMIES];
		int enemy_y[] = new int[MAX_NUMBER_OF_ENEMIES];
		int enemy_health[] = new int[MAX_NUMBER_OF_ENEMIES];
		int enemy_direction[] = new int[MAX_NUMBER_OF_ENEMIES];
		int enemy_climbing[] = new int[MAX_NUMBER_OF_ENEMIES];

		// Animation
		int balls_height = 0;
		int balls_direction_isUp = YES;

		// Particles
		int particle_x[] = new int[MAX_NUMBER_OF_PARTICLES];
		int particle_y[] = new int[MAX_NUMBER_OF_PARTICLES];
		float particle_xVelocity[] = new float[MAX_NUMBER_OF_PARTICLES];
		float particle_yVelocity[] = new float[MAX_NUMBER_OF_PARTICLES];
		int particle_life[] = new int[MAX_NUMBER_OF_PARTICLES];
		int activeParticles = 0;

		// Level state
		int levelLoaded = NO;
		int levelCompleted = YES;
		int levelIndex = 0;
		int killedEnemies = 0;
		int draw_textTimer = 0;
		String currentLevel = "";

		// Level graphics positions
		int mapX = 0;
		int bgX = 0;

		// General
		int i;
		int x;
		int y;
		int a;

		// Walls
		BufferedImage wallImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics wallGraphics = wallImage.getGraphics();
		for (x = 0; x < TILE_SIZE; x++) {
			for (y = 0; y < TILE_SIZE; y++) {
				int randomNumber = random.nextInt(100) + 80;
				wallGraphics.setColor(new Color(randomNumber, randomNumber, randomNumber));
				wallGraphics.fillRect(x, y, 1, 1);
			}
		}

		// Ladders
		BufferedImage ladderImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics ladderGfx = ladderImage.getGraphics();
		ladderGfx.setColor(Color.BLACK);
		ladderGfx.fillRect(0, 0, 2, TILE_SIZE);
		ladderGfx.fillRect(30, 0, 2, TILE_SIZE);
		ladderGfx.fillRect(0, 0, TILE_SIZE, 2);
		ladderGfx.fillRect(0, 15, TILE_SIZE, 2);

		// Loading level-box
		BufferedImage loadBoxImage = new BufferedImage(200, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics loadBoxGfx = loadBoxImage.getGraphics();
		for (i = 0; i < 40; i++) {
			loadBoxGfx.setColor(new Color(255 - (i * 3), 255 - (i * 3), 255 - (i * 3)));
			loadBoxGfx.drawLine(0, i, 200, i);
		}
		loadBoxGfx.setColor(Color.BLACK);
		loadBoxGfx.drawRect(0, 0, 199, 39);
		loadBoxGfx.drawRect(1, 1, 197, 37);
		loadBoxGfx.drawString("Level", 77, 25);

		// Show the start screen
		while (BUTTON_FIRE_PRESSED == NO) {
			// Draw background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

			g.setColor(Color.WHITE);
			g.drawString("Attac4k of the Greenballs", 260, 30);
			g.drawString("Jens St‰‰f 2012", 293, 50);
			g.drawString("Press space to start", 276, 220);

			// Draw the entire results on the screen.
			appletGraphics.drawImage(screen, 0, 0, null);

			Thread.yield();
		}

		// Reset the fire button
		BUTTON_FIRE_PRESSED = NO;

		// Game loop.
		long nextFrameStartTime = System.nanoTime();
		while (true) {
			// New level
			if (levelCompleted == YES) {
				levelIndex++;
				levelCompleted = NO;
				killedEnemies = 0;
				player_climbing = NO;
				player_falling = NO;
				numberOfActiveBullets = 0;
				player_bulletCounter = 0;
				draw_textTimer = 200;
				levelLoaded = NO;
				mapX = WINDOW_WIDTH;
				bgX = -702;

				if (levelIndex == 20)
					levelIndex = 1;

				if (levelIndex < 10)
					currentLevel = levels.substring(((levelIndex - 1) * 330), ((levelIndex - 1) * 330) + 330);
				else
					currentLevel = levels.substring(((levelIndex - 10) * 330), ((levelIndex - 10) * 330) + 330);

				numberOfActiveEnemies = (levelIndex / 3) + 1;

				// Init enemies
				for (i = 0; i < numberOfActiveEnemies; i++)
					enemy_health[i] = 0;

				// Render background
				x = random.nextInt(7) + 2;
				i = random.nextInt(7) + 2;
				a = random.nextInt(7) + 2;
				bgImage2 = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics backgroundGfx = bgImage2.getGraphics();
				for (y = 0; y < WINDOW_HEIGHT; y++) {
					backgroundGfx.setColor(new Color(255 - (y / x), 255 - (y / i), 255 - (y / a)));
					backgroundGfx.drawLine(0, y, WINDOW_WIDTH, y);
				}

				// Render map
				mapImage2 = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				Graphics gfx = mapImage2.getGraphics();
				gfx.setColor(Color.BLACK);
				for (y = 0; y < MAP_HEIGHT; y++) {
					for (x = 0; x < MAP_WIDTH; x++) {
						if (currentLevel.charAt((y * MAP_WIDTH) + x) == TILE_WALL) {
							gfx.drawImage(wallImage, x * TILE_SIZE, y * TILE_SIZE, null);

							if (y != 0 && currentLevel.charAt(((y - 1) * MAP_WIDTH) + x) != TILE_WALL)
								gfx.drawLine(x * TILE_SIZE, y * TILE_SIZE, x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE); // Upper
							// line
							if (y != MAP_HEIGHT - 1 && currentLevel.charAt(((y + 1) * MAP_WIDTH) + x) != TILE_WALL)
								gfx.drawLine(x * TILE_SIZE, y * TILE_SIZE + TILE_SIZE, x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE + TILE_SIZE); // Lower
							// line
							if (x != 0 && currentLevel.charAt((y * MAP_WIDTH) + (x - 1)) == TILE_SPACE)
								gfx.drawLine(x * TILE_SIZE, y * TILE_SIZE, x * TILE_SIZE, y * TILE_SIZE + TILE_SIZE); // Left
							// line
							if (x != MAP_WIDTH - 1 && currentLevel.charAt((y * MAP_WIDTH) + (x + 1)) == TILE_SPACE)
								gfx.drawLine(x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE, x * TILE_SIZE + TILE_SIZE, y * TILE_SIZE + TILE_SIZE); // Right
							// line
						} else if (currentLevel.charAt((y * MAP_WIDTH) + x) == TILE_LADDER) {
							gfx.drawImage(ladderImage, x * TILE_SIZE, y * TILE_SIZE, null);
						}
					}
				}

				gfx.setColor(Color.BLACK);
				gfx.fillRect(0, 458, WINDOW_WIDTH, 40);
				gfx.setColor(Color.WHITE);
				gfx.drawLine(0, 458, WINDOW_WIDTH, 458);

				for (i = 0; i < MAX_NUMBER_OF_ACTIVE_BULLETS; i++)
					bullet_direction[i] = DIRECTION_NONE;

				nextFrameStartTime = System.nanoTime();
			}

			// Level is loaded
			if (levelLoaded == NO) {
				mapX -= 5;
				bgX += 5;

				if (player_x > 352)
					player_x -= 4;
				else if (player_x < 352)
					player_x += 4;
				if (player_y > 416)
					player_y -= 4;
				else if (player_y < 416)
					player_y += 4;

				if (mapX <= 0) {
					mapX = 0;
					mapImage1 = mapImage2;
					bgImage1 = bgImage2;
					levelLoaded = YES;
					player_x = 352;
					player_y = 416;

					// Init enemies
					for (i = 0; i < numberOfActiveEnemies; i++) {
						enemy_x[i] = ((i + 1) * 2) * 32;
						enemy_y[i] = 32;
						enemy_health[i] = 5;
						enemy_direction[i] = random.nextInt(2);
						enemy_climbing[i] = NO;
					}
				}
			}

			do {
				nextFrameStartTime += 16666667;

				// Time between bullets, to prevent rapid fire
				if (player_bulletCounter < TIME_BETWEEN_BULLETS)
					player_bulletCounter++;

				// Decrease the "Loading level-text"
				if (draw_textTimer > 0)
					draw_textTimer--;

				// Input
				if (levelLoaded == YES) {
					if (PLAYER_MOVING_LEFT == YES && player_climbing == NO && player_falling == NO) {
						if (currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32)) == TILE_SPACE || currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32)) == TILE_LADDER)
							player_x -= PLAYER_SPEED;
						player_direction = DIRECTION_LEFT;
					} else if (PLAYER_MOVING_RIGHT == 1 && player_climbing == NO && player_falling == NO) {
						if (currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_SPACE
								|| currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_LADDER)
							player_x += PLAYER_SPEED;
						player_direction = DIRECTION_RIGHT;
					}
					if (PLAYER_MOVING_UP == YES) {
						if ((currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32)) == TILE_LADDER || currentLevel.charAt(((player_y / 32) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_LADDER)
								|| (player_climbing == YES && (currentLevel.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32)) == TILE_LADDER || currentLevel
										.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_LADDER))) {
							player_y -= PLAYER_SPEED;
							player_climbing = YES;
						} else
							player_climbing = NO;
					} else if (PLAYER_MOVING_DOWN == YES) {
						if (currentLevel.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32)) == TILE_LADDER
								|| currentLevel.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_LADDER) {
							player_y += PLAYER_SPEED;
							player_climbing = YES;
						} else
							player_climbing = NO;
					}
					if (BUTTON_FIRE_PRESSED == YES && player_climbing == NO && player_bulletCounter >= TIME_BETWEEN_BULLETS) {
						if (numberOfActiveBullets < MAX_NUMBER_OF_ACTIVE_BULLETS) {
							for (i = 0; i < MAX_NUMBER_OF_ACTIVE_BULLETS; i++) {
								if (bullet_direction[i] == DIRECTION_NONE) {
									bullet_x[i] = player_x;
									bullet_y[i] = player_y;
									bullet_direction[i] = player_direction;
									numberOfActiveBullets++;
									break;
								}
							}
						}

						player_bulletCounter = 0;
					}
				}

				// Do the ball jumping animation
				if (balls_direction_isUp == YES) {
					balls_height++;

					if (balls_height >= 10)
						balls_direction_isUp = NO;
				} else {
					balls_height--;

					if (balls_height <= -10)
						balls_direction_isUp = YES;
				}

				// Update particles
				for (i = activeParticles - 1; i > -1; i--) {
					particle_x[i] += particle_xVelocity[i];
					particle_y[i] += particle_yVelocity[i];
					particle_life[i]--;

					if (particle_life[i] < 1)
						activeParticles--;

					particle_yVelocity[i] += Float.valueOf(particle_life[i]) / 500;
				}

				// Update bullets
				for (i = 0; i < MAX_NUMBER_OF_ACTIVE_BULLETS; i++) {
					if (bullet_direction[i] != DIRECTION_NONE) {
						if (bullet_direction[i] == DIRECTION_RIGHT)
							bullet_x[i] += BULLET_SPEED;
						else
							bullet_x[i] -= BULLET_SPEED;

						if (bullet_x[i] < 0 || bullet_x[i] > WINDOW_WIDTH) {
							numberOfActiveBullets--;
							bullet_direction[i] = DIRECTION_NONE;
						}
					}
				}

				// Update enemies
				for (i = 0; i < numberOfActiveEnemies; i++) {
					// Find the player
					if (player_y == enemy_y[i]) {
						if (player_x > enemy_x[i])
							enemy_direction[i] = DIRECTION_RIGHT;
						else
							enemy_direction[i] = DIRECTION_LEFT;
					}
					// Move the enemies
					if (enemy_health[i] > 0) {
						if (currentLevel.charAt(((enemy_y[i] / 32 + 1) * MAP_WIDTH) + (enemy_x[i] / 32 + 1)) == TILE_SPACE
								&& currentLevel.charAt(((enemy_y[i] / 32 + 1) * MAP_WIDTH) + (enemy_x[i] / 32)) == TILE_SPACE) {
							enemy_y[i] += ENEMY_SPEED;
							enemy_climbing[i] = NO;
						} else if (player_y != enemy_y[i] && currentLevel.charAt(((enemy_y[i] / 32) * MAP_WIDTH) + (enemy_x[i] / 32)) == TILE_LADDER && random.nextInt(17) == 1) {
							enemy_climbing[i] = YES;
						} else if (enemy_climbing[i] == YES) {
							enemy_y[i] -= ENEMY_SPEED;
						} else if (enemy_direction[i] == DIRECTION_RIGHT) {
							if (currentLevel.charAt(((enemy_y[i] / 32) * MAP_WIDTH) + (enemy_x[i] / 32 + 1)) == TILE_SPACE
									|| currentLevel.charAt(((enemy_y[i] / 32) * MAP_WIDTH) + (enemy_x[i] / 32 + 1)) == TILE_LADDER)
								enemy_x[i] += ENEMY_SPEED;
							else
								enemy_direction[i] = DIRECTION_LEFT;
						} else {
							if (currentLevel.charAt(((enemy_y[i] / 32) * MAP_WIDTH) + (enemy_x[i] / 32)) == TILE_SPACE
									|| currentLevel.charAt(((enemy_y[i] / 32) * MAP_WIDTH) + (enemy_x[i] / 32)) == TILE_LADDER)
								enemy_x[i] -= ENEMY_SPEED;
							else
								enemy_direction[i] = DIRECTION_RIGHT;
						}

						// Collision with player
						if (((enemy_x[i] >= player_x && enemy_x[i] <= player_x + TILE_SIZE) || (enemy_x[i] <= player_x && enemy_x[i] >= player_x - TILE_SIZE))
								&& ((enemy_y[i] >= player_y && enemy_y[i] <= player_y + TILE_SIZE) || (enemy_y[i] <= player_y && enemy_y[i] >= player_y - TILE_SIZE))) {
							levelIndex--;
							levelCompleted = YES;

							// Create particles
							for (y = 0; y < 20; y++) {
								if (activeParticles < MAX_NUMBER_OF_PARTICLES) {
									particle_x[activeParticles] = player_x;
									particle_y[activeParticles] = player_y;
									particle_xVelocity[activeParticles] = random.nextInt(6) - 3;
									particle_yVelocity[activeParticles] = random.nextInt(6) - 3;

									if (particle_xVelocity[activeParticles] == 0)
										particle_xVelocity[activeParticles] = 3;
									if (particle_yVelocity[activeParticles] == 0)
										particle_yVelocity[activeParticles] = -3;

									particle_life[activeParticles] = 100;
									activeParticles++;
								}
							}

							// Remove all enemies so the player cannot
							// collide
							// with them muptiple times
							numberOfActiveEnemies = 0;

							break;
						}
						// Collision with bullets
						for (x = 0; x < MAX_NUMBER_OF_ACTIVE_BULLETS; x++) {
							if (bullet_direction[x] != DIRECTION_NONE
									&& ((enemy_x[i] >= bullet_x[x] && enemy_x[i] <= bullet_x[x] + TILE_SIZE) || (enemy_x[i] <= bullet_x[x] && enemy_x[i] >= bullet_x[x] - TILE_SIZE))
									&& ((enemy_y[i] >= bullet_y[x] && enemy_y[i] <= bullet_y[x] + TILE_SIZE) || (enemy_y[i] <= bullet_y[x] && enemy_y[i] >= bullet_y[x] - TILE_SIZE))) {
								enemy_health[i]--;
								bullet_direction[x] = DIRECTION_NONE;
								numberOfActiveBullets--;

								if (enemy_health[i] == 0) {
									killedEnemies++;

									if (killedEnemies == numberOfActiveEnemies)
										levelCompleted = YES;
								}

								// Create particles
								for (y = 0; y < 20; y++) {
									if (activeParticles < MAX_NUMBER_OF_PARTICLES) {
										particle_x[activeParticles] = bullet_x[x];
										particle_y[activeParticles] = bullet_y[x];
										particle_xVelocity[activeParticles] = random.nextInt(6) - 3;
										particle_yVelocity[activeParticles] = random.nextInt(6) - 3;

										if (particle_xVelocity[activeParticles] == 0)
											particle_xVelocity[activeParticles] = 3;
										if (particle_yVelocity[activeParticles] == 0)
											particle_yVelocity[activeParticles] = -3;

										particle_life[activeParticles] = 100;
										activeParticles++;
									}
								}
							}
						}
					}
				}

				// Is player falling?
				if (player_falling == YES)
					player_y += 2;
				if (currentLevel.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32 + 1)) == TILE_SPACE
						&& currentLevel.charAt(((player_y / 32 + 1) * MAP_WIDTH) + (player_x / 32)) == TILE_SPACE)
					player_falling = YES;
				else
					player_falling = NO;

			} while (nextFrameStartTime < System.nanoTime());

			// Draw background
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

			// Draw level
			if (levelLoaded == NO) {
				g.drawImage(bgImage1, bgX + WINDOW_WIDTH, 0, null);
				g.drawImage(bgImage2, bgX, 0, null);
				g.drawImage(mapImage1, mapX - WINDOW_WIDTH, 0, null);
				g.drawImage(mapImage2, mapX, 0, null);
			} else {
				g.drawImage(bgImage1, 0, 0, null);
				g.drawImage(mapImage1, 0, 0, null);
			}

			// Draw the player
			g.setColor(new Color(185, 0, 185));
			g.fillOval(player_x + (balls_height / 2), player_y - balls_height, 32 - balls_height, 32 + balls_height);

			g.setColor(new Color(225, 0, 225));
			g.fillOval(player_x + (balls_height / 2), player_y - balls_height, 28 - balls_height, 28 + balls_height);

			g.setColor(Color.BLACK);
			g.drawOval(player_x + (balls_height / 2), player_y - balls_height, 32 - balls_height, 32 + balls_height);

			g.setColor(new Color(255, 0, 255));
			g.fillOval(player_x + (balls_height / 2) + 3, player_y - balls_height + 3, 16 - balls_height, 16 + balls_height);

			// Draw enemies
			for (i = 0; i < numberOfActiveEnemies; i++) {
				if (enemy_health[i] > 0) {
					g.setColor(new Color(0, 10 - (enemy_health[i] * -35), 0));
					g.fillOval(enemy_x[i] + (balls_height / 2), enemy_y[i] - balls_height, 32 - balls_height, 32 + balls_height);

					g.setColor(new Color(0, 50 - (enemy_health[i] * -35), 0));
					g.fillOval(enemy_x[i] + (balls_height / 2), enemy_y[i] - balls_height, 28 - balls_height, 28 + balls_height);

					g.setColor(Color.BLACK);
					g.drawOval(enemy_x[i] + (balls_height / 2), enemy_y[i] - balls_height, 32 - balls_height, 32 + balls_height);

					g.setColor(new Color(0, 80 - (enemy_health[i] * -35), 0));
					g.fillOval(enemy_x[i] + (balls_height / 2) + 3, enemy_y[i] - balls_height + 3, 16 - balls_height, 16 + balls_height);
				}
			}

			// Draw particles
			for (i = 0; i < activeParticles; i++) {
				g.setColor(new Color(0, 55 + (particle_life[i] * 2), 0));
				g.fillRect(particle_x[i], particle_y[i], 3, 3);
			}

			// Draw text
			g.setColor(Color.WHITE);
			g.drawString("Level:", 5, 474);
			g.drawString(String.valueOf(levelIndex), 43, 474);
			g.drawString("Bullets:", 590, 474);

			// Draw the bullets
			y = 0;
			for (i = 0; i < MAX_NUMBER_OF_ACTIVE_BULLETS; i++) {
				if (bullet_direction[i] != DIRECTION_NONE) {
					for (x = 0; x < 16; x++) {
						g.setColor(new Color(255 - random.nextInt(255), 255 - random.nextInt(255), 255 - random.nextInt(255)));
						g.fillRect(bullet_x[i] + x * 2, bullet_y[i] + 9 + random.nextInt(6) - 6, 4, 2);
					}
				} else {
					g.setColor(Color.WHITE);
					g.fillOval(645 + 12 * y, 465, 9, 9);
					y++;
				}
			}

			// Draw "next level"-text if a new level is starting
			if (draw_textTimer > 0) {
				g.setColor(Color.BLACK);
				g.drawImage(loadBoxImage, 265, 230, null);
				g.drawString(String.valueOf(levelIndex), 379, 255);
			}

			// Draw the entire results on the screen.
			appletGraphics.drawImage(screen, 0, 0, null);

			// Burn off extra cycles
			while (nextFrameStartTime - System.nanoTime() > 0) {
				Thread.yield();
			}
		}
	}

	public void processKeyEvent(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (e.getID() == KeyEvent.KEY_PRESSED) {
			if (keyCode == KeyEvent.VK_SPACE)
				BUTTON_FIRE_PRESSED = YES;
			else if (keyCode == KeyEvent.VK_LEFT)
				PLAYER_MOVING_LEFT = YES;
			else if (keyCode == KeyEvent.VK_RIGHT)
				PLAYER_MOVING_RIGHT = YES;
			else if (keyCode == KeyEvent.VK_UP)
				PLAYER_MOVING_UP = YES;
			else if (keyCode == KeyEvent.VK_DOWN)
				PLAYER_MOVING_DOWN = YES;
		} else if (e.getID() == KeyEvent.KEY_RELEASED) {
			if (keyCode == KeyEvent.VK_SPACE)
				BUTTON_FIRE_PRESSED = NO;
			else if (keyCode == KeyEvent.VK_LEFT)
				PLAYER_MOVING_LEFT = NO;
			else if (keyCode == KeyEvent.VK_RIGHT)
				PLAYER_MOVING_RIGHT = NO;
			else if (keyCode == KeyEvent.VK_UP)
				PLAYER_MOVING_UP = NO;
			else if (keyCode == KeyEvent.VK_DOWN)
				PLAYER_MOVING_DOWN = NO;
		}
	}
}