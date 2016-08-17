package java4k.diez;

/*
 * Zombie 4k Version 1.1
 *
 * Copyright 2012, Alan Waddington
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * The names of its contributors may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java4k.GamePanel;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Z extends GamePanel {

	//
	// Graphics Engine Constants
	//

	private final static int SCREENHEIGHT = 400; // Screen height
	private final static int SCREENWIDTH = 600; // Screen width
	private final static float SCREENDEPTH = 693f; // Eyepoint to screen
	private final static int DEPTH = 750; // Distance to draw
	private final static float DEPTHRATIO = 1.5f; // Non-linear sampling

	private final static int MAPSIZE = 0x100; // Map size
	private final static int TILETYPES = 5; // Tile types
	private final static int TILESIZE = 0x100; // Tile size
	private final static int ROADGRIDSIZE = 16; // Road grid size
	private final static float PLAYERHEIGHT = 1.2f; // Player Height

	private final static int GRASS = 0; // Grass Tile
	private final static int FOREST = 1; // Forest Tile
	private final static int ROAD = 2; // Road Tile
	private final static int BUILDING = 3; // Building Outside
	private final static int BUILDINGINSIDE = 4; // Building Inside

	private final static float DEGREES = 18000f / (float) Math.PI;// Angle
	private final static float ROTATERATE = 1e-9f; // Player rotate speed
	private final static float TRANSLATERATE = 2e-7f; // Player speed

	private final static int ENEMIES = 64; // Number of Zombies
	private final static int SUPPLIES = 32; // Number of Boxes
	private final static int ENTITIES = ENEMIES + SUPPLIES;

	private final static int ENTITYTYPES = 2; // Zombies or Boxes
	private final static int ENTITYZOMBIE = 0; // Zombie Type
	private final static int ENTITYBOX = 1; // Box Type

	private final static int ENTITYSIZE = 7; // Size of entity tile
	private final static int ENTITYCOLOURS = 6; // Max cols in column
	private final static float ENTITYDEPTH = 9e6f; // Draw dist squared
	private final static float ENTITYCOLLISION = 400f; // Collide dist squared
	private final static float ENTITYRADIUS = 32f / TILESIZE * 32f / TILESIZE / 2f * 1e6f; // Radius squared
	private final static float ENTITYCENTRE = ENTITYSIZE / 2 + 0.5f;//Rotation pt
	private final static int ENTITYSCALE = 2; // Map to entity  scale

	private final static int ZOMBIEDEPTH = 3; // Character depth
	private final static int ZOMBIEWIDTH = 7; // Character width
	private final static int ZOMBIEHEIGHT = 15; // Character height

	private final static int DEAD = 0; // Entity is not drawn
	private final static int IDLE = 1; // Entity is static
	private final static int ATTACK = 2; // Entity is attacking

	private final static float SUPPLIESBOOST = 10f; // Resource bar boost %
	private final static float BLOODLOSS = 50f; // B&W on blood loss %
	private final static float WATERLOSS = 25f; // Dehydration bloodloss
	private final static float STARVATION = 10f; // Slow on starvation
	private final static float SUPPLYUSAGE = 5e-10f;// Supply usage rate
	private final static float ZOMBIEDAMAGE = 2e-8f; // Zombie damage rate
	private final static float ATTACKRANGE = 5e3f; // Attack range squared
	private final static float SPAWNOUTSIDE = 0.01f; // Probability of spawn
	private final static long DEADTIME = 0x100000000L; // Player dead

	private final static float DETECTRANGE = 5e4f; // Zombie idles
	private final static float FORGETRANGE = 5e6f; // Zombie attacks

	private final static int SOUNDS = 6; // Number of sounds
	private final static float RATE = 16000f;// Music sample rate
	private final static float AMPLITUDE = 16000f; // Music amplitude

	private final static int PLAYERATTACKSOUND = 0; // Sounds
	private final static int BOXFOUNDSOUND = 1;
	private final static int ZOMBIEALERTSOUND = 2;
	private final static int ZOMBIEATTACKSOUND = 3;
	private final static int AMBIENTSOUND1 = 4;
	private final static int AMBIENTSOUND2 = 5;

	//
	// Graphics Engine Class Variables
	//

	float[] sin = new float[36000]; // Precalculate trig
	float[] cos = new float[36000]; // functions
	float[] rayAngleFix = new float[SCREENWIDTH]; // Perspective
	float[] cosRayAngleFix = new float[SCREENWIDTH]; // Fix
	float[] zMap = new float[DEPTH]; // Non-linear z function

	float[] heightX = new float[MAPSIZE * TILESIZE];// Terrain height map
	float[] heightZ = new float[MAPSIZE * TILESIZE];

	float[][][] tileHeight = new float[TILETYPES][TILESIZE][TILESIZE]; // Tile height maps
	int[][][] tileColour = new int[TILETYPES][TILESIZE][TILESIZE]; // Tile height maps

	int[][] map = new int[MAPSIZE][MAPSIZE]; // World map
	float[] gauge = new float[3]; // HUD gauges
	byte[][] audioData = new byte[SOUNDS][]; // Array of 16 bit sound samples
	Clip[] audio = new Clip[SOUNDS]; // Clips to play the above sounds

	//
	// Game Engine Class Variables
	//

	boolean keyboard[] = new boolean[0x10000]; // Keyboard map

	// Generic variables
	int i, j, k = 0, m, n = 0;
	float x = 0, y, z = 0, r;
	float dx, dz;

	int buildings;
	int[] buildingX;
	int[] buildingZ;

	// Frame time
	long time; // Current time
	long lastTime; // Time at the start of the last frame
	long deltaTime; // Delta frame time
	long deadTime = 0; // Time player is dead

	// Player
	float playerX = 0, playerY, playerZ = 0; // Player coordinates
	float playerA = 0, sinPlayerA, cosPlayerA; // Player rotational angle
	boolean attacking, lastAttacking = false; // Player Attack
	int blood = 0; // Blood on axe

	// World map positions
	int px, pz; // Player position on current tile
	int ex = 0, ez = 0; // Entity position on current tile
	int tx, tz; // Renderer column on current tile

	// Rendering
	int c; // Entity colour RRGGBB
	int tileType; // Terrain type of current tile
	float rx, rz; // x & z rotated into player frame
	boolean draw; // Enable terrain drawing
	boolean outside; // Player is outside a building
	boolean drawEntity; // There is an Entity to draw

	// Ray casting
	float depth; // Distance into screen
	int ray; // Ray number
	float rayAngle; // Horizontal raycasting angle
	float cosRayAngle; // Cosine of raycasting angle
	float sinRayAngle; // Sine of raycasting angle

	// Screen drawing
	int dst; // Pointer into screen image
	int sy, lsy, msy; // Screen y pointer
	float[] heightY = new float[DEPTH]; // Precalculated baseline heights
	float[] heightYT = new float[DEPTH]; // Precalculated terrain heights
	int[] heightSY = new int[DEPTH]; // Precalculated screen heights

	// Object culling variables
	int[] drawList1 = new int[ENTITIES];
	int[] drawList2 = new int[ENTITIES];
	int drawCount1, drawCount2;

	// Entities
	int[] entityS = new int[ENTITIES]; // Entity State
	float[] entityA = new float[ENTITIES]; // Entity Angle
	float[] entityX = new float[ENTITIES]; // Entity X coordinate
	float[] entityZ = new float[ENTITIES]; // Entity Z coordinate
	int[] entityLTX = new int[ENTITIES]; // Entity Tile X coordinate
	int[] entityLTZ = new int[ENTITIES]; // Entity Tile Z coordinate
	float[] entityWX = new float[ENTITIES]; // Zombie X waypoint
	float[] entityWZ = new float[ENTITIES]; // Zombie Z waypoint
	// Create entities
	int[][][][] entityHeight = new int[ENTITYSIZE][ENTITYSIZE][ENTITYCOLOURS][ENTITYTYPES];
	int[][][][] entityColour = new int[ENTITYSIZE][ENTITYSIZE][ENTITYCOLOURS][ENTITYTYPES];
	int[][][] entityColours = new int[ENTITYSIZE][ENTITYSIZE][ENTITYTYPES];

	// Sound effects
	boolean[] sound = new boolean[SOUNDS]; // Sounds to be played
	int[] audioLoop = new int[SOUNDS]; // Loop feature workaround

	BufferedImage screen;
	int[] screenData;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SCREENWIDTH, SCREENHEIGHT);
	}

	public Z() {
		super(true);

		// Create screen image (double buffer)
		screen = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
		screenData = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();

		// Precalculate trigonometry
		for (i = 0; i < 36000; i++) { // Sin & Cos lookup
			cos[i] = (float) Math.cos(i / DEGREES);
			sin[(i + 9000) % 36000] = cos[i];
		}
		for (i = 0; i < SCREENWIDTH; i++) { // Renderer spherical aberation fix
			rayAngleFix[i] = (float) Math.atan2((SCREENWIDTH / 2 - i), SCREENDEPTH);
			cosRayAngleFix[i] = (float) Math.cos(rayAngleFix[i]);
		}
		for (i = 0; i < DEPTH; i++)
			// Non-linear depth sampling intervals
			zMap[i] = DEPTH * DEPTHRATIO / (DEPTH * DEPTHRATIO - i) - 1f;

		heightSY[0] = SCREENHEIGHT - 1; // Start of terrain heights

		//
		// Create Sound Effects
		//

		for (i = 0; i < SOUNDS; i++) {
			// Select sample length
			switch (i) {
			case PLAYERATTACKSOUND:
				r = 0.1f;
				break;
			case ZOMBIEATTACKSOUND:
			case AMBIENTSOUND2:
				r = 0.25f;
				break;
			default:
				r = 1f;
			}
			audioData[i] = new byte[2 * (int) (r * RATE)];
			for (j = 0; j < r * RATE; j++) {
				x = j / (r * RATE); // Ramp
				y = 2 * x - 1f;
				z = (float) Math.PI * j / RATE;
				// Create each individual sound
				switch (i) {
				case PLAYERATTACKSOUND: // Short Noise
					k = (int) (AMPLITUDE / 5f * Math.random());
					break;
				case BOXFOUNDSOUND: // Ping
					k = (int) (AMPLITUDE / 5f * (1f - x) * Math.cos(500 * z));
					break;
				case ZOMBIEALERTSOUND: // Groan
					k = (int) (AMPLITUDE / 2f * Math.cos((300 - 20 * y * y) * z));
					break;
				case ZOMBIEATTACKSOUND: // Munch
					k = (int) (AMPLITUDE / 2f * Math.cos((500 - 200 * x) * z) * (Math.cos(100 * z)));
					break;
				case AMBIENTSOUND1: // Low groan
					k = (int) (AMPLITUDE / 2f * Math.cos((250 - 20 * y * y) * z));
					break;
				default: // Twitter
					k = (int) (AMPLITUDE / 20f * x * Math.cos((500 + 500 * x) * z));
				}
				// Apply attack and decay to avoid clicks
				if (j < 1000)
					k *= j / 1000f;
				if (r * RATE - j < 1000)
					k *= (r * RATE - j) / 1000f;
				// Store audio data
				audioData[i][2 * j + 1] = (byte) (k & 0xff);
				audioData[i][2 * j] = (byte) ((k >> 8) & 0xff);
			}
		}

		// Initialise sound system
		try {
			AudioFormat audioFormat = new AudioFormat(RATE, 16, 1, true, true);
			DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
			for (i = 0; i < SOUNDS; i++) {
				audio[i] = (Clip) AudioSystem.getLine(info);
				audio[i].open(audioFormat, audioData[i], 0, audioData[i].length);
			}
		} catch (Exception e) {
		}

		//
		// Create Game Map Tiles
		//

		// Create terrain height map in increments of 0.01 units
		for (i = 0; i < 0x10000; i++) {
			x = 2f * (float) Math.PI / 0x10000 * i;
			heightX[i] = (float) (1.0 - Math.cos(x) - 0.2 * Math.cos(x * 5f)) / 2f;
			heightZ[i] = (float) (1.0 - Math.cos(x) - 0.2 * Math.cos(x * 7f)) / 2f;
		}

		// Create tiles
		for (i = 0; i < TILETYPES; i++) {
			for (j = 0; j < TILESIZE; j++) {
				for (k = 0; k < TILESIZE; k++) {
					// Pyramid shape for trees and roofs
					x = 0.5f - j / (float) TILESIZE;
					if (x < 0)
						x = -x;
					z = 0.5f - k / (float) TILESIZE;
					if (z < 0)
						z = -z;
					// Create each tile type
					switch (i) {
					case GRASS:
					case FOREST:
						tileHeight[i][j][k] = (float) (Math.random() - 0.5);
						tileColour[i][j][k] = 0x000100 * (16 + (3 * j + 17 * k) % 32);
						if (i == FOREST) {
							y = 100f * ((0.5f - x) * (0.5f - z) - 0.15f);
							if (y > 0) {
								tileHeight[i][j][k] = y;
								tileColour[i][j][k] = 0x010100 + 0x000100 * (int) (15.0 * Math.random());
							}
						}
						break;
					case ROAD:
						tileColour[i][j][k] = 0x202020 + 0x010101 * (int) (15.0 * Math.random());
						break;
					case BUILDING:
					case BUILDINGINSIDE:
						tileHeight[i][j][k] = (3f * (1f - x) * (1f - z) + 1f);// Wall
						tileColour[i][j][k] = 0x404040 + (j > k ? 0x040404 : 0) + (j < TILESIZE - k - 1 ? 0x020202 : 0);
						if ((k > TILESIZE / 2 - 10 && k < TILESIZE / 2 + 10) && ((i == BUILDING && (j < 10 || j >= TILESIZE - 10)) || (i == BUILDINGINSIDE && (j >= 10 && j < TILESIZE - 10)))) {
							tileHeight[i][j][k] = 1.6f; // Door
							tileColour[i][j][k] = 0x010000;
						}
						if ((j > TILESIZE / 2 - 10 && j < TILESIZE / 2 + 10) && ((i == BUILDING && (k < 10 || k >= TILESIZE - 10)) || (i == BUILDINGINSIDE && (k >= 10 && k < TILESIZE - 10)))) {
							tileHeight[i][j][k] = 1.6f; // Door
							tileColour[i][j][k] = 0x010000;
						}
						if (j > 20 && j < TILESIZE - 20 && k > 20 && k < TILESIZE - 20)
							if (i == BUILDING) {
								tileColour[i][j][k] = 0x402020; // Roof
							} else { // Inside
								tileHeight[i][j][k] = 0;
								tileColour[i][j][k] = 0x401010 + 0x010101 * (int) (10.0 * Math.random());//Floor
							}
					}
				}
			}
		}

		// Zombie is entity 0
		// Looking from the front, each zombie is 15 cells tall by 7 wide
		// Each zombie is 3 cells deep, each cell being one of 4 colours
		// 0=Transparent; 1=Clothes; 2=Face/Hands; 3=Hair/Vest/Shoes

		//     Front     Side
		//  E --333--    333
		//  D --222--    322
		//  C -12221-    112
		//  B 1133311    113
		//  A 1133311    113
		//  9 1133311    113
		//  8 1133311    113
		//  7 1111111    111
		//  6 2111112    121
		//  5 -11-11-    11-
		//  4 -11-11-    11-
		//  3 -11-11-    11-
		//  2 -11-11-    11-
		//  1 -11-11-    11-
		//  0 -33-33-    333

		//          Col  000000000000000111111111111111222222222222222
		//          Row  0123456789ABCDE0123456789ABCDE0123456789ABCDE
		// Colour Front  ---------------3--------------3-----113333223  Bit0-1
		//       Middle  ------211111---3111111111111--311111111111123  Bit2-3
		//         Back  ---------------3111111111111--311111111111133  Bit4-5
		String zombie = "000000844444000oDDDDDDDDDDDD00oDDDDDEEGGGGFjo"
		//          Col  333333333333333  - Centre Column
		//          Row  0123456789ABCDE
		// Colour Front  ------113333223 Bit0-1
		//       Middle  ------111111123 Bit2-3
		//         Back  ------111111133 Bit4-5
				+ "000000EEGGGGFjo"

				//          Col  444444444444444555555555555555666666666666666
				//          Row  0123456789ABCDE0123456789ABCDE0123456789ABCDE
				// Colour Front  3-----1133332233----------------------------- Bit0-1
				//       Middle  3111111111111233111111111111--------211111--- Bit2-3
				//         Back  3111111111111333111111111111----------------- Bit4-5
				+ "oDDDDDEEGGGGFjooDDDDDDDDDDDD00000000844444000";

		// Convert colour map to height map
		for (i = 0; i < ZOMBIEDEPTH; i++)
			for (j = 0; j < ZOMBIEWIDTH; j++) {
				n = -1; // Start with no colours
				c = -1;
				for (k = 0; k < ZOMBIEHEIGHT; k++) {
					// Get colour
					m = ((zombie.charAt(k + ZOMBIEHEIGHT * j) - 48) >> (2 * i)) & 3;
					if (c != m) {
						n++; // Next block
						entityColour[j][i + 2][n][ENTITYZOMBIE] = c = m;// Set colour
					}
					entityHeight[j][i + 2][n][ENTITYZOMBIE] = k + 1; // Set Height
				}
				entityColours[j][i + 2][ENTITYZOMBIE] = n + (c > 0 ? 1 : 0);
			}

		// Box is entity 1
		// All boxes are 7 x 7 x 5 high
		// Edges are in colour 1; Side panels are in colour 2; Top in colour 3
		for (i = 0; i < ENTITYSIZE; i++)
			for (j = 0; j < ENTITYSIZE; j++) {
				// Top and bottom
				for (k = 0; k < 3; k += 2) {
					entityColour[j][i][k][ENTITYBOX] = (i == 0 || i == 6 || j == 0 || j == 6) ? 1 : 3;
					entityHeight[j][i][k][ENTITYBOX] = (k == 0) ? 1 : 5;
				}
				// Sides
				entityColour[j][i][1][ENTITYBOX] = ((i == 0 || i == 6) && (j == 0 || j == 6)) ? 1 : 2;
				entityHeight[j][i][1][ENTITYBOX] = 4;
				entityColours[j][i][ENTITYBOX] = 3;
			}

		//
		// Draw Map
		//

		// Draw roads and buildings
		for (i = ROADGRIDSIZE; i < MAPSIZE - ROADGRIDSIZE; i += MAPSIZE / ROADGRIDSIZE)
			for (j = ROADGRIDSIZE; j < MAPSIZE - ROADGRIDSIZE; j += MAPSIZE / ROADGRIDSIZE) {
				draw = Math.random() > 0.9; // Village
				if (Math.random() > 0.5 || draw) {
					for (k = 0; k <= ROADGRIDSIZE; k++)
						map[i + k][j] = ROAD;
					if (Math.random() > 0.5 || draw)
						map[i + 1][j - 1] = BUILDING;
					if (Math.random() > 0.5 || draw)
						map[i + 1][j + 1] = BUILDING;
				}
				if (Math.random() > 0.5 || draw) {
					for (k = 0; k <= ROADGRIDSIZE; k++)
						map[i][j + k] = ROAD;
					if (Math.random() > 0.5 || draw)
						map[i - 1][j + 1] = BUILDING;
					if (Math.random() > 0.5 || draw)
						map[i + 1][j + 1] = BUILDING;
				}
				if (draw)
					for (k = 1; k < ROADGRIDSIZE; k += 2) {
						map[i + k][j - 1] = BUILDING;
						map[i + k][j + 1] = BUILDING;
						map[i - 1][j + k] = BUILDING;
						map[i + 1][j + k] = BUILDING;
					}
			}

		// Draw trees and count buildings
		buildings = 0;
		for (i = 0; i < MAPSIZE; i++)
			for (j = 0; j < MAPSIZE; j++) {
				if (map[i][j] == GRASS && Math.random() > 0.5 && !(i == 0x80 && j == 0x80)) // Start square not a tree
					map[i][j] = FOREST;
				if (map[i][j] == BUILDING)
					buildings++;
			}

		// Make a list of building locations
		buildingX = new int[buildings];
		buildingZ = new int[buildings];
		buildings = 0;
		for (i = 0; i < MAPSIZE; i++)
			for (j = 0; j < MAPSIZE; j++)
				if (map[i][j] == BUILDING) {
					buildingX[buildings] = i;
					buildingZ[buildings] = j;
					buildings++;
				}

		time = System.nanoTime();

	}

	@Override
	public void paintComponent(Graphics gs) {

		lastTime = time;
		time = System.nanoTime();
		deltaTime = time - lastTime;

		//
		// Game Start
		//

		// Restart if any of the gauges are empty
		if (deadTime >= 0) {
			deadTime -= deltaTime;
			playerX = playerZ = 0x8080; // Start at centre
			playerA = 0; // Face fowards
			blood = 0; // No blood on axe
			for (i = 0; i < 3; i++)
				gauge[i] = 100f; // Full gauges
			for (i = 0; i < ENTITIES; i++)
				entityS[i] = DEAD; // Despawn everything
		}

		//
		// Player Movement
		//

		// Rotate player
		if (keyboard[Event.LEFT] || keyboard['a']) // Rotate Left
			playerA += ROTATERATE * deltaTime;
		if (keyboard[Event.RIGHT] || keyboard['d'])// Rotate Right
			playerA -= ROTATERATE * deltaTime;
		playerA = (playerA + 2f * (float) Math.PI) % (2f * (float) Math.PI);
		sinPlayerA = sin[(int) (DEGREES * playerA)];
		cosPlayerA = cos[(int) (DEGREES * playerA)];

		// Translate player
		rx = playerX;
		rz = playerZ;
		r = deltaTime * TRANSLATERATE / (gauge[1] < STARVATION ? 2 : 1); // Slow
		if (keyboard[Event.UP] || keyboard['w']) { // Move forward
			rx += -r * sinPlayerA;
			rz += -r * cosPlayerA;
		}
		if (keyboard[Event.DOWN] || keyboard['s']) { // Move backward
			rx += r * sinPlayerA;
			rz += r * cosPlayerA;
		}

		// Detect player attack
		sound[PLAYERATTACKSOUND] = attacking = (draw = keyboard[' ']) && !lastAttacking;
		lastAttacking = draw;

		//
		// Player / Map Collisions
		//

		// Determine the terrain tile
		px = (int) rx & 0xffff;
		pz = (int) rz & 0xffff;
		tx = px & 0xff;
		tz = pz & 0xff;
		tileType = map[px >> 8][pz >> 8];
		playerY = 50f * (heightX[px] * heightZ[pz] - 0.1f);
		outside = true;
		if (playerY > -1) {
			if (tileType == FOREST) { // Move if no tree collision
				if (tx < 0x60 || tx > 0xA0 || tz < 0x60 || tz > 0xA0) {
					playerX = rx;
					playerZ = rz;
				}
			} else if (tileType == BUILDING) { // Move if no wall collision
				outside = false;
				if ((tx > 0x70 && tx < 0x90) || (tz > 0x70 && tz < 0x90) || (tx > 0x14 && tx < 0xEC && tz > 0x14 && tz < 0xEC)) {
					playerX = rx;
					playerZ = rz;
				}
			} else { // Move unconditionally
				playerX = rx;
				playerZ = rz;
			}
		}

		// Recalculate player position
		px = (int) playerX & 0xffff;
		pz = (int) playerZ & 0xffff;
		playerY = 50f * (tileType == BUILDING ? (heightX[(px & 0xff00) + 0x80] // Level inside building
				* heightZ[(pz & 0xff00) + 0x80] - 0.1f) : (heightX[px] * heightZ[pz] - 0.1f));

		//
		// Collision Detection, Respawn and Entity on Screen Detection
		//

		drawCount1 = 0;
		for (i = 1; i < SOUNDS; i++)
			sound[i] = false;
		for (i = 0; i < ENTITIES; i++) {
			draw = drawEntity = false;
			if (entityS[i] > DEAD) {
				dx = entityX[i] - playerX;
				dz = entityZ[i] - playerZ;
				r = dx * dx + dz * dz;
				// Detect if entity is within draw range and in view
				if (r < ENTITYDEPTH) {
					draw = true; // In range
					if (dx * sinPlayerA + dz * cosPlayerA < 0) { // In view
						drawEntity = true;
						drawList1[drawCount1++] = i;
					}
				}
				// Player attack
				if (i < ENEMIES) {
					if (r < ATTACKRANGE && attacking && drawEntity) {
						attacking = false; // Kill zombie
						entityS[i] = DEAD;
						if (blood < 100)
							blood++;
					} else { // Zombie attack
						// Attack moding
						if (r < DETECTRANGE && entityS[i] != ATTACK) {
							entityS[i] = ATTACK;
							sound[ZOMBIEALERTSOUND] = true; // Hunted
						}
						if (r > FORGETRANGE)
							entityS[i] = IDLE;
						if (entityS[i] == ATTACK) {
							if ((int) ((time / 1e9) % 4) == 0)
								sound[ZOMBIEALERTSOUND] = true; // Hunted
							// Determine direction to player
							tx = (int) entityX[i] & 0xff00;
							tz = (int) entityZ[i] & 0xff00;
							rx = entityX[i] - entityWX[i];
							rz = entityZ[i] - entityWZ[i];
							if ((px & 0xff00) == tx && (pz & 0xff00) == tz) {
								entityWX[i] = playerX;
								entityWZ[i] = playerZ;
							} else {
								// Update waypoint when tile changes
								if (tx != entityLTX[i] || tz != entityLTZ[i]) {
									// If the tile is forest any we enter
									// from the north or south, we can only
									// go to east or west.(and vice versa).
									// Otherwise route the middle of the
									// edge of the tile that routes most
									// directly to the player
									if (map[tx >> 8][tz >> 8] == FOREST ? ((int) entityWX[i] & 0xff) == 0x80 : dx * dx > dz * dz ^ Math.random() < 0.25) {
										if (dx > 0) { // Route east/west
											entityWX[i] = tx;
											entityWZ[i] = tz + 0x80;
										} else {
											entityWX[i] = tx + 0x100;
											entityWZ[i] = tz + 0x80;
										}
									} else {
										if (dz > 0) { // Route north/south
											entityWX[i] = tx + 0x80;
											entityWZ[i] = tz;
										} else {
											entityWX[i] = tx + 0x80;
											entityWZ[i] = tz + 0x100;
										}
									}
								}
							}
							// Rotate zombie towards waypoint iteratively
							for (j = 10; j > 0; j--) {
								x = sin[(int) (DEGREES * entityA[i])];
								z = cos[(int) (DEGREES * entityA[i])];
								y = rx * z - rz * x;
								if (y > 0)
									entityA[i] -= deltaTime * ROTATERATE * j / 10f;
								else
									entityA[i] += deltaTime * ROTATERATE * j / 10f;
								entityA[i] = (entityA[i] + 2f * (float) Math.PI) % (2f * (float) Math.PI);
							}
							// Move forward
							if (r > ENTITYCOLLISION) {
								entityLTX[i] = (int) entityX[i] & 0xff00;
								entityLTZ[i] = (int) entityZ[i] & 0xff00;
								entityX[i] += deltaTime * TRANSLATERATE * x / 2;
								entityZ[i] += deltaTime * TRANSLATERATE * z / 2;
							}
						}
						// Add random wobble
						entityX[i] += deltaTime * TRANSLATERATE * (Math.random() - 0.5f) / 10f;
						entityZ[i] += deltaTime * TRANSLATERATE * (Math.random() - 0.5f) / 10f;
						entityA[i] += deltaTime * ROTATERATE * (Math.random() - 0.5f);
						entityA[i] = (entityA[i] + 2f * (float) Math.PI) % (2f * (float) Math.PI);
					}
				}
				// Player/Entity collision
				if (r < ENTITYCOLLISION) {
					if (i < ENEMIES) { // Enemy collision
						sound[ZOMBIEATTACKSOUND] = true; // Eaten
						gauge[2] -= deltaTime * ZOMBIEDAMAGE;
					} else { // Box collision
						j = (i & 3) % 3;
						entityS[i] = DEAD;
						sound[BOXFOUNDSOUND] = true; // Box found
						gauge[j] += SUPPLIESBOOST;
						if (gauge[j] > 100f)
							gauge[j] = 100f;
					}
				}
			}
			// Respawn entities that are out of draw range
			if (!draw && i >= ENEMIES) { // Respawn boxes
				// Firstly attempt to respawn box in a nearby house
				n = (int) (buildings * (float) Math.random());
				x = TILESIZE * (buildingX[n] + 0.1f + 0.8f * (float) Math.random());
				z = TILESIZE * (buildingZ[n] + 0.1f + 0.8f * (float) Math.random());
				dx = x - playerX;
				dz = z - playerZ;
				if (dx * dx + dz * dz < ENTITYDEPTH) { // Spawn in house
					entityX[i] = x;
					entityZ[i] = z;
					entityLTX[i] = (int) entityX[i] & 0xff00;
					entityLTZ[i] = (int) entityZ[i] & 0xff00;
					entityS[i] = IDLE;
					draw = true;
				}
				// Secondly, if unsuccessful, occassionally spawn outside
				if (Math.random() > SPAWNOUTSIDE)
					draw = true;
			}
			if (!draw) { // Respawn zombie or box just out of draw range
				// This can ocassionally spawn inside a tree or house wall
				ray = (int) (36000 * Math.random());
				r = TILESIZE * (3f * (float) Math.random() + 6f);
				entityX[i] = playerX + r * sin[ray];
				entityZ[i] = playerZ + r * cos[ray];
				entityS[i] = IDLE;
				if (i < ENEMIES)
					entityA[i] = (int) (36000 * Math.random());
			}
		}

		//
		//  Draw 3D View
		//

		// Render each column of display using ray casting
		for (ray = 0; ray < SCREENWIDTH; ray += 2) {
			rayAngle = playerA + rayAngleFix[ray];
			rayAngle = (rayAngle + 2f * (float) Math.PI) % (2f * (float) Math.PI);
			cosRayAngle = cos[(int) (DEGREES * rayAngle)] / cosRayAngleFix[ray];
			sinRayAngle = sin[(int) (DEGREES * rayAngle)] / cosRayAngleFix[ray];

			// Pre-calculate which entities intersect ray
			drawCount2 = 0;
			for (i = 0; i < drawCount1; i++) {
				// Rotate into player's coordinate frame
				x = entityX[drawList1[i]] - playerX;
				z = entityZ[drawList1[i]] - playerZ;
				r = x * cosRayAngle - z * sinRayAngle;
				if (r * r < ENTITYRADIUS)
					drawList2[drawCount2++] = drawList1[i];
			}

			// Pre-calculate screen y coordinates for terrain billboards
			for (i = 1; i < DEPTH; i++) {
				// Ray cast to point (x,z) in player coordinates
				depth = zMap[i];
				x = playerX - sinRayAngle * depth * 1000f;
				z = playerZ - cosRayAngle * depth * 1000f;

				// Determine the terrain tile
				tx = (int) x & 0xffff;
				tz = (int) z & 0xffff;
				tileType = map[tx >> 8][tz >> 8];
				if (tileType == BUILDING && (px & 0xff00) == (tx & 0xff00) && (pz & 0xff00) == (tz & 0xff00))
					tileType = BUILDINGINSIDE;

				// Calculate base ground height
				y = 50f * (tileType == BUILDINGINSIDE ? (heightX[(tx & 0xff00) + 0x80] // Level inside building
						* heightZ[(tz & 0xff00) + 0x80] - 0.1f) : (heightX[tx] * heightZ[tz] - 0.1f));
				heightY[i] = y;

				// Add Tile Height if above or just under water
				if (y > -1f)
					y += tileHeight[tileType][tx & 0xff][tz & 0xff];
				// If underwater, then the height is the water surface
				if (y < 0f)
					y = 0f;
				heightYT[i] = y;

				// Calculate screen position for the top of the terrain
				sy = SCREENHEIGHT / 2 - (int) (20f * (y - playerY - PLAYERHEIGHT) / depth);
				if (sy < 0)
					sy = 0;
				// If the height is less than foreground hills, set it to
				// the maximum height of the foreground.
				if (sy > heightSY[i - 1])
					sy = heightSY[i - 1];
				heightSY[i] = sy;
			}

			// Render column drawing from back to front
			msy = SCREENHEIGHT - 1; // Track horizon
			for (i = DEPTH - 1; i > 0; i--) {
				// Ray cast to point (x,z) in player coordinates
				depth = zMap[i];
				x = playerX - sinRayAngle * depth * 1000f;
				z = playerZ - cosRayAngle * depth * 1000f;

				// Determine the terrain tile
				tx = (int) x & 0xffff;
				tz = (int) z & 0xffff;
				tileType = map[tx >> 8][tz >> 8];
				if (tileType == BUILDING && (px & 0xff00) == (tx & 0xff00) && (pz & 0xff00) == (tz & 0xff00))
					tileType = BUILDINGINSIDE;

				//
				// Draw Terrain
				//

				// Get tile colour
				if (heightYT[i] <= 0) // Water
					c = 0x000020 + 0x010101 * (int) ((0x20 * i) / DEPTH);
				else { // Terrain
					c = tileColour[tileType][tx & 0xff][tz & 0xff];
					// Modulate with base location to give a bit of variety
					if ((tileType == GRASS || tileType == FOREST) && ((tx & 0x100) == 0) ^ ((tz & 0x100) == 0))
						c += 0x100800;
				}
				c = (c / 2) & 0x7f7f7f; // Scale to give a darker scene

				if (gauge[2] < BLOODLOSS) // Black & white on high bloodloss
					c = 0x010101 * (((c >> 16) & 0xff) + ((c >> 8) & 0xff) + (c & 0xff));

				// Inhibit drawing outside world when in a building
				draw = outside || tileType == BUILDINGINSIDE;

				// Draw terrain
				sy = heightSY[i];
				if (draw && sy < heightSY[i - 1]) {
					dst = ray + heightSY[i - 1] * SCREENWIDTH;
					for (k = heightSY[i - 1]; k > sy; k--) {
						screenData[dst] = screenData[dst + 1] = c;
						dst -= SCREENWIDTH;
					}
				}

				// Check for drawable entity at this location
				drawEntity = false;
				for (j = 0; j < drawCount2; j++) {
					k = drawList2[j];
					dx = entityX[k] - x;
					dz = entityZ[k] - z;

					// Draw if we are within the entity's base radius
					if (dx * dx + dz * dz < ENTITYRADIUS) {
						m = (int) (DEGREES * entityA[k]);

						// Adjust origin to be at centre of entity
						dx = dx / ENTITYSCALE;
						dz = dz / ENTITYSCALE;

						// Rotate entity coordinates
						ex = (int) (dx * cos[m] - dz * sin[m] + ENTITYCENTRE + 1) - 1;
						if (ex < 0 || ex >= ENTITYSIZE)
							continue;
						ez = (int) (dx * sin[m] + dz * cos[m] + ENTITYCENTRE + 1) - 1;
						if (ez < 0 || ez >= ENTITYSIZE)
							continue;

						// Check for non-zero height for first colour
						n = k < ENEMIES ? ENTITYZOMBIE : ENTITYBOX;
						if (entityColours[ex][ez][n] > 0) {
							drawEntity = true;
							break; // Quit on first entity
						}
					}
				}

				//
				// Draw Entity
				//

				if (drawEntity) {
					// Repeat for each block of colour in the column
					lsy = heightSY[i - 1];
					for (j = 0; j < entityColours[ex][ez][n]; j++) {
						// Define object colour and height
						y = heightY[i] + 0.045f * (float) entityHeight[ex][ez][j][n] * ENTITYSCALE;
						m = entityColour[ex][ez][j][n];
						if (n == ENTITYZOMBIE)
							switch (m) { // Zombie
							case 0:
								c = 0x303050; // Blue sky fill in
								break;
							case 1: // Clothes
								c = 0x101020 + 0x080808 * (k & 3);
								break;
							case 2: // Face and hands
								c = 0x203008;
								break;
							default: // Hair and vest
								c = 0x100808;
							}
						else
							switch (m) { // Box
							case 0:
								c = 0x303050; // Blue sky fill in
								break;
							case 1: // Edges
								c = 0;
								break;
							case 2: // Sides
								c = 0x100404;
								break;
							default: // Top
								c = 0x000040 << (8 * ((k & 3) % 3));
							}
						if (gauge[2] < BLOODLOSS) // Black and white
							c = 0x010101 * (((c >> 16) & 0xff) + ((c >> 8) & 0xff) + (c & 0xff));

						// Calculate screen position
						sy = SCREENHEIGHT / 2 - (int) (20f * (y - playerY - PLAYERHEIGHT) / depth);
						if (sy < 0)
							sy = 0;
						if (sy > SCREENHEIGHT - 1)
							sy = SCREENHEIGHT - 1;

						// Fill in sky if skylined
						if (m == 0)
							lsy = msy;

						// Draw Entity
						if (draw && sy < lsy) {
							dst = ray + lsy * SCREENWIDTH;
							for (m = lsy; m > sy; m--) {
								screenData[dst] = screenData[dst + 1] = c;
								dst -= SCREENWIDTH;
							}
						}
						lsy = sy;
					}
				}
				// Keep track of where to start drawing sky
				// Take account of entities which may be skylined
				if (draw && sy < msy)
					msy = sy;
			}

			// Draw sky if outside, building roof if inside
			if (outside) { // Sky
				m = 0x202040;
				n = 0x010101;
			} else { // Roof
				m = 0x100808;
				n = 0;
			}
			if (gauge[2] < BLOODLOSS) { // Black and white on high bloodloss
				m = outside ? 0xC0C0C0 : 0x101010;
				n = 0;
			}
			dst = ray + msy * SCREENWIDTH;
			for (j = msy; j >= 0; j--) {
				k = j > 380 ? 380 : j;
				screenData[dst] = screenData[dst + 1] = m + n * (k / 12);
				dst -= SCREENWIDTH;
			}
		}

		//
		// Overlay
		//

		// Update gauges
		if (playerX != 0x8080 || playerZ != 0x8080) {
			gauge[0] -= deltaTime * SUPPLYUSAGE;
			gauge[1] -= deltaTime * SUPPLYUSAGE / 2;
			gauge[2] -= gauge[0] < WATERLOSS ? deltaTime * SUPPLYUSAGE * 2 : 0;
		}

		// Draw gauges
		for (i = 0; i < 3; i++) {
			if (gauge[i] < 0) {
				if (deadTime <= 0)
					deadTime = DEADTIME;
				gauge[i] = 0; // Dead
			}
			for (j = 0; j < 100; j++) {
				c = j < gauge[i] ? (0xff << (8 * i)) : 0;
				for (k = 0; k < 8; k++) {
					screenData[SCREENWIDTH * (10 + 10 * i + k) + 10 + j] = c;
				}

			}
		}

		// Draw axe blade
		draw = gauge[2] < BLOODLOSS; // Black and white on high bloodloss
		for (i = 0; i < 150; i++) {
			for (j = 0; j < i; j++) {
				n = -80 + i + (keyboard[' '] ? 0 : 100) + SCREENWIDTH / 2;
				m = 40 + j + (keyboard[' '] ? 350 : 16);
				if (m < SCREENHEIGHT)
					screenData[SCREENWIDTH * m + n] = draw ? 0 : 0x040208 + 0x010101 * (int) ((150 - i + j) / 10) + 0x010000 * ((300 - i + j) * blood / 500);
				else
					break;
			}
		}
		// Draw axe shaft
		for (i = 0; i < SCREENWIDTH / 2; i++) {
			n = SCREENWIDTH / 2 + i + (keyboard[' '] ? 0 : 100);
			if (n >= SCREENWIDTH)
				break;
			for (j = 0; j < 32 + i / 10; j++) {
				m = i - j + (keyboard[' '] ? 350 : 16);
				if (m >= 0 && m < SCREENHEIGHT && j < 2 * i) {
					screenData[SCREENWIDTH * m + n] = draw ? 0 : 0x040208 + 0x010101 * j + 0x010000 * ((300 - i + j) * blood / 1000);
				}
			}
		}

		// Ambient sounds
		if ((int) ((time / 1e9) % 9) == 0)
			sound[AMBIENTSOUND1] = true;
		if ((int) ((time / 1e9) % 25) == 0)
			sound[AMBIENTSOUND2] = true;

		// Play Sound Effects
		for (i = 0; i < SOUNDS; i++) {
			if (sound[i] && !audio[i].isActive()) {
				audio[i].loop(audioLoop[i]);
				audioLoop[i] = 1;
			}
		}

		//
		// Draw Screen
		//

		if (deadTime < 0 && gs != null) {
			gs.drawImage(screen, 0, 0, null);
		}
		Thread.yield();

	}

	@Override
	public void processAWTEvent(AWTEvent awtEvent) {
		if (awtEvent instanceof KeyEvent) {
			KeyEvent e = (KeyEvent) awtEvent;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				keyboard[e.getKeyChar()] = true;
			} else if (e.getID() == KeyEvent.KEY_RELEASED) {
				keyboard[e.getKeyChar()] = false;
			}
		}
	}
}
