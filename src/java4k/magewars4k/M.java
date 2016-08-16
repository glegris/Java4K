package java4k.magewars4k;

/*
 * MageWars 4k
 *
 * Copyright 2010, Alan Waddington
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

/* Minor bugfix 22/02/11 */

import java.applet.Applet;
import java.awt.Cursor;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
// Network Support
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//import java.net.NetworkInterface;

public class M extends Applet implements Runnable {

	//
	// LAN Play Constants
	//

	private final static int PLAYERS = 4; // Max concurrent players
	private final static int ENTITIES = 21; // No. of entities in game = player+8fireballs + 6enemy + 6fireballs
	private final static int VARIABLES = 8; // Variables per entity
	private final static int IDSIZE = 4; // Process ID Size

	// Network parameters
	private final static int PORT = 6789; // Server port
	private final static String GROUP = "228.5.6.7";// Multicast group (IP)
	private final static int RATE = 100000000; // Transmision interval (uS)
	private final static long TIMEOUT = 1000000000; // Player dropout time (uS)

	// Allocation of Network Data Variables
	// Player           = LANdata[x][0][]
	// Enemies          = LANdata[x][1][] to LANdata[x][ENEMIES][]
	// Player Fireballs = LANdata[x][ENEMIES+1][ENEMIES+PLAYERFIREBALLS]
	// Enemy Fireballs  = LANdata[x][ENEMIES+PLAYERFIREBALLS+1][] to LANdata[x][ENTITIES-1]

	// Allocation of Entity Parameters
	// LANdata[x][x][0] = Position X
	// LANdata[x][x][1] = Position Y
	// LANdata[x][x][2] = Position Z
	// LANdata[x][x][3] = Angle Y
	// LANdata[x][x][4] = Type     (0 indicates entity is invalid)
	// LANdata[x][x][5] = Velocity X
	// LANdata[x][x][6] = Velocity Y
	// LANdata[x][x][7] = Velocity Z

	//
	// Graphics Engine Constants
	//

	private final static int SCREENHEIGHT = 400; // Screen height
	private final static int SCREENWIDTH = 600; // Screen width
	private final static float SCREENDEPTH = 693f; // Eyepoint to screen
	private final static int DEPTH = 750; // Distance to draw
	private final static float DEPTHRATIO = 1.3f; // Non-linear sampling
	private final static float ENTITYDEPTH = 9f; // Distance to draw squared
	private final static float PLAYERHEIGHT = 2f; // Player Height
	private final static int TILETYPES = 4; // Number of tile types
	private final static int TILESIZE = 1024; // Size of map tile
	private final static int ENTITYSIZE = 8; // Size of entity tile
	private final static int MAPSIZE = 7; // Size of world map

	private final static int DAPPLE = 32; // Colour dapple period
	private final static float ENTITYRADIUS = 32f / TILESIZE * 32f / TILESIZE / 2f; // Radius squared
	private final static float COLLISIONRADIUS = 48f / TILESIZE * 48f / TILESIZE / 2f; // Radius squared
	private final static float CENTRE = ENTITYSIZE / 2 - 0.5f; // Centre of Entity
	private final static int ENTITYSCALE = 4; // 4 map tile cuboids map to one entity tile cuboid

	private final static int PLAIN = 0; // Map tile types
	private final static int GRASS = 1;
	private final static int MOUNTAIN = 2;
	private final static int CASTLE = 3;

	//
	// Game Engine Constants
	//

	// Scaling for network data
	private final static float SCALEXYZ = TILESIZE * 100f; //Position
	private final static float DEGREES = 18000f / (float) Math.PI;//Angle

	private final static float ROTATERATE = 1e-9f;// Player rotate speed
	private final static float TRANSLATERATE = 4e-10f;// Player speed

	private final static long FIREBALLTIMEOUT1 = 2000000000l;//Fireball Timeout
	private final static long FIREBALLTIMEOUT2 = 10000000l; // Short Timeout
	private final static int ENEMIES = 6; // No of Enemies
	private final static int PLAYERFIREBALLS = 8; // No of Player Fireballs

	private final static int PLAYERHEALTH = 100; // Player Health
	private final static int ENEMYHEALTH = 20; // Enemy Health

	private final static float TOOCLOSE = 100f / TILESIZE; // Enemy AI
	private final static float TOOFAR = 400f / TILESIZE; // Enemy AI

	private final static int X = 0; // Position X
	private final static int Y = 1; // Position Y
	private final static int Z = 2; // Position Z
	private final static int A = 3; // Angle Y axis
	private final static int T = 4; // Entity type
	private final static int VX = 5; // Velocity X
	private final static int VY = 6; // Velocity Y
	private final static int VZ = 7; // Velocity Z

	//
	// LAN Play Class Variables
	//

	// UDP Data Messages
	private MulticastSocket socket; // Server Socket
	private DatagramPacket txPacket, rxPacket; // UDP Datagrams
	private byte[] txMsg = new byte[IDSIZE + 4 * ENTITIES * VARIABLES];
	private byte[] rxMsg = new byte[IDSIZE + 4 * ENTITIES * VARIABLES];
	private InetAddress group; // Multicast IP Address

	// Database of currently connected players
	private InetAddress[] address = new InetAddress[PLAYERS];
	private int[] port = new int[PLAYERS];
	private long[] timestamp = new long[PLAYERS];
	private long[] process = new long[PLAYERS];
	private volatile int[][][] LANdata = new int[PLAYERS][ENTITIES][VARIABLES];
	private volatile int[][][] LANbuffer = new int[PLAYERS][ENTITIES][VARIABLES];

	// Process data
	private int processID = (int) ((System.nanoTime() >> 16) & 0xffffffff);
	private boolean receiveThread = false; // Thread usage flag

	//
	// Graphics Engine Class Variables
	//

	private float[] sin = new float[36000]; // Precalculate trig
	private float[] cos = new float[36000]; // functions
	private float[] rayAngleFix = new float[SCREENWIDTH]; // Perspective
	private float[] cosRayAngleFix = new float[SCREENWIDTH]; // Fix
	private float[] zMap = new float[DEPTH]; // Non-linear z function
	private float[][][] tiles = new float[TILETYPES][TILESIZE][TILESIZE]; // Tile height maps
	private int[][] map = new int[MAPSIZE][MAPSIZE]; // World map

	//
	// Game Engine Class Variables
	//

	private float[][][] localData = new float[PLAYERS][ENTITIES][VARIABLES];
	private long[] fireballTimeout = new long[ENEMIES + PLAYERFIREBALLS];
	private boolean[] castle = new boolean[4];

	private boolean keyboard[] = new boolean[0x10000]; // Keyboard map
	private boolean mouse; // Mouse button
	private volatile int mouseX, mouseY; // Mouse coords

	@Override
	public void start() {
		new Thread(this).start(); // Start receive thread
	}

	public void run() {

		// Generic Variables
		int i, j, k, m;
		int x1 = 0, y1, z1 = 0;
		float x, y, z;
		float dx, dy, dz;
		long time;

		// Run one of two threads
		while (!isActive())
			Thread.yield();
		if (receiveThread) {
			//
			// Packet Receive Thread
			//

			int[] LANvars;
			do {
				try {
					socket.receive(rxPacket); // Blocks until packet received
					InetAddress pa = rxPacket.getAddress();
					j = rxPacket.getPort(); // Port Number
					// Discard record for this player
					k = ((rxMsg[3] & 0xff) << 24) + ((rxMsg[2] & 0xff) << 16) + ((rxMsg[1] & 0xff) << 8) + (rxMsg[0] & 0xff);
					//if (NetworkInterface.getByInetAddress(pa)!=null && k==processID)
					if (k == processID)
						continue;
					time = System.nanoTime();
					// Find existing record
					x1 = 0; // Find existing record and put index in x1
					y1 = 0; // Find a free record and put the index in y1
					for (i = 1; i < PLAYERS; i++) {
						if (address[i] != null && address[i].equals(pa) && port[i] == j && process[i] == k && timestamp[i] + TIMEOUT >= time)
							x1 = i; // Existing record Found
						if (timestamp[i] + TIMEOUT < time)
							y1 = i; // Free record found
					}
					// Create new record if necessary
					if (x1 == 0) { // No existing record
						x1 = y1; // so use a free record
						// Create new record
						if (x1 != 0) {
							// Write address information
							address[x1] = InetAddress.getByAddress(pa.getAddress());
							port[x1] = j; // Record port
							process[x1] = k; // Record process id
						}
					}
					// Provided there is room, update record
					if (x1 != 0) {
						// We are either updating the existing record or
						// creating a new record in a time expired slot.
						// Slots are only declared free after twice the
						// TIMEOUT to minimise the risk of processing a
						// record which is in the middle of being reassigned

						// Copy payload data into shared data area
						// Assemble in separate array for thread safety
						for (i = 0; i < ENTITIES; i++) {
							LANvars = LANbuffer[x1][i];
							for (j = 0; j < VARIABLES; j++) {
								k = IDSIZE + 4 * (i * VARIABLES + j);
								LANvars[j] = ((rxMsg[k + 3] & 0xff) << 24) + ((rxMsg[k + 2] & 0xff) << 16) + ((rxMsg[k + 1] & 0xff) << 8) + (rxMsg[k] & 0xff);
							}
							LANbuffer[x1][i] = LANdata[x1][i];
							LANdata[x1][i] = LANvars; // Atomic operation
						}

						// Update timestamp last, as if this is the first
						// time this record is written, we want to ensure
						// it is valid before being processed.
						timestamp[x1] = time;
					}
				} catch (Exception e) {
				}
			} while (isActive());
			return;
		}

		//
		// Game Thread
		//

		//
		// Initialise LAN Play
		//

		try {
			// Open socket
			socket = new MulticastSocket(PORT);
			group = InetAddress.getByName(GROUP);
			socket.joinGroup(group);
			// Initialise Transmit packet
			txMsg[0] = (byte) (processID & 0xff);
			txMsg[1] = (byte) ((processID >> 8) & 0xff);
			txMsg[2] = (byte) ((processID >> 16) & 0xff);
			txMsg[3] = (byte) ((processID >> 24) & 0xff);
		} catch (Exception e) {
		}
		txPacket = new DatagramPacket(txMsg, txMsg.length, group, PORT);
		rxPacket = new DatagramPacket(rxMsg, rxMsg.length, group, PORT);

		// Start receive thread
		receiveThread = true;
		new Thread(this).start();
		long sendTime = 0;

		//
		//  Initialise Graphics Engine
		//

		// Object culling variables
		float[][] drawList1 = new float[PLAYERS * (ENEMIES + 1)][];
		float[][] drawList2 = new float[PLAYERS * (ENEMIES + 1)][];
		int drawCount1, drawCount2;

		// Object pointers
		int[] LANentity; // LANdata pointers
		float[] entity1 = null, entity2; // LocalData pointers

		// Entity Colours
		int[] cMap = new int[8]; // Entity type to colour map
		int colour; // Entity colour RRGGBB

		// Renderer Variables
		int tileType; // Terrain type of current tile
		int dst; // Pointer into screen image
		int sx, sy; // Screen x and y pointers
		float rx, rz; // x & z rotated into player frame
		float radius, depth; // Object radius & distance
		float rayAngle = 0, cosRayAngle, sinRayAngle; // Raycasting variables
		int ray, angle;

		// Map Creation
		float m1, m2; // Mountain heights
		float p1, p2; // Plains heights

		// Create screen image (double buffer)
		BufferedImage screen = new BufferedImage(SCREENWIDTH, SCREENHEIGHT, BufferedImage.TYPE_INT_RGB);
		int[] screenData = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		float[] zBuffer = new float[SCREENHEIGHT * SCREENWIDTH];
		Graphics gs = getGraphics();

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

		// Type to Colour Mapping
		for (i = 0; i < 8; i++) {
			cMap[i] = 0x3fc000 * (i & 4) + 0x7f80 * (i & 2) + 0xff * (i & 1);
		}

		// Define World Map
		// 0 = plain
		// 1 = grass effect on plain (This is not used directly on map)
		// 2 = mountain
		// 3 = castle
		for (i = 0; i < MAPSIZE; i++) {
			for (j = 0; j < MAPSIZE; j++) {
				if ((i == 3 && (j <= 2 || j >= 4)) || ((i <= 2 || i >= 4) && j == 3))
					map[i][j] = MOUNTAIN; // Some mountains
				if (((i == 1 || i == MAPSIZE - 2)) && (j == 1 || j == MAPSIZE - 2))
					map[i][j] = CASTLE; // 4 Castles
			}
		}

		// Define Map Tiles
		for (i = 0; i < TILESIZE; i++) {
			p1 = (float) Math.cos(i / (float) TILESIZE * 2 * (float) Math.PI);
			m1 = (2f * i) / TILESIZE - 1f;
			if (m1 < 0)
				m1 = -m1;
			m1 = 3f * (1f - m1);
			for (j = 0; j < TILESIZE; j++) {
				p2 = (float) Math.cos(j / (float) TILESIZE * 2 * (float) Math.PI);
				m2 = (2f * j) / TILESIZE - 1f;
				if (m2 < 0)
					m2 = -m2;
				m2 = 3f * (1f - m2);
				// Rolling Plain
				y = 2f;
				if (p1 < y)
					y = p1; // Valley N-S
				if (p2 < y)
					y = p2; // Valley W-E
				tiles[PLAIN][i][j] = y;
				// Grass
				tiles[GRASS][i][j] = y + 0.2f * (float) Math.random();
				// Mountain
				z = m1 + m2;
				tiles[MOUNTAIN][i][j] = (y > z) ? y : z; // Allow grass at edges
				// Castle
				z = 3f; // Walls
				if (y < -1.75f / 2f)
					z = -1f; // Entrance
				if (p1 < 1.9f / 2f && p2 < 1.9f / 2f)
					z = -1f; // Courtyard
				if (y > 1f / 2f)
					z = 10f; // Towers
				tiles[CASTLE][i][j] = z;
			}
		}

		//  Define Wizard Object
		int[][] entityHeight = new int[ENTITYSIZE][ENTITYSIZE];
		int[][] entityColour = new int[ENTITYSIZE][ENTITYSIZE];

		//    Define as a base64 number with 4 bits height and 2 bits colour
		//               0000000011111111222222223333333344444444555555556666666677777777
		//    Colour     0000000000000000000000000001100000022000000000000020020000000030
		//    Height     0000000000666600067777600679976006799760056776500454454004444470";
		String wizard = "000000000066660006777760067II760067YY7600567765004U44U40044444g0";
		for (i = 0; i < ENTITYSIZE; i++) {
			for (j = 0; j < ENTITYSIZE; j++) {
				k = wizard.charAt(i + ENTITYSIZE * j) - 48;
				entityHeight[i][j] = k & 15;
				entityColour[i][j] = k / 16;
			}
		}

		//
		// Initialise Game Engine
		//

		// Player
		float playerX = 0, playerY = 0, playerZ = 0; // Player coordinates
		float angleY = 0, sinAngleY, cosAngleY; // Player rotational angle

		// Map
		int mapX, mapZ; // Current map tile
		int tileX, tileZ; // Position within map tile

		// Sundries
		int[] health = new int[ENEMIES + 1];
		LANdata[0][0][T] = 7; // Players are white
		boolean allowSpawn = false;

		//
		// Game Loop
		//

		// Get the focus and select the cross-hair cursor
		requestFocus();
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		// Time
		long lastTime; // Time at the start of the last frame
		long LANdeltaTime; // Delta time from LANdata timestamp
		time = System.nanoTime();
		//        long printTime = time;

		do {
			lastTime = time;
			time = System.nanoTime();
			long deltaTime = time - lastTime;
			/*            if (printTime+1000000000<time) {
			                printTime = time;
			                System.out.println("Frame:"+deltaTime/1000000);
			            }
			*/
			//
			// Game Over Logic
			//

			if (health[0] == 0) { // Reset player to start
				health[0] = PLAYERHEALTH;
				playerX = playerZ = 3.5f;
				angleY = 0.5f;
				for (i = 0; i < 4; i++)
					castle[i] = false; // Surrender all castles
			}

			//
			// LAN Transmit data
			//

			if (time > sendTime) {
				sendTime = time + RATE;

				// Send Entity data
				for (i = 0; i < ENTITIES; i++) {
					for (j = 0; j < VARIABLES; j++) {
						k = IDSIZE + 4 * (i * VARIABLES + j);
						m = LANdata[0][i][j];
						txMsg[k + 3] = (byte) ((m >> 24) & 0xff);
						txMsg[k + 2] = (byte) ((m >> 16) & 0xff);
						txMsg[k + 1] = (byte) ((m >> 8) & 0xff);
						txMsg[k] = (byte) (m & 0xff);
					}
				}
				try {
					socket.send(txPacket);
				} catch (Exception e) {
				}
			}

			//
			// Game
			//

			//
			// Estimate all entity positions
			//

			for (i = 0; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = 0; j < ENTITIES; j++) {
						LANentity = LANdata[i][j];
						entity1 = localData[i][j];
						if ((entity1[T] = LANentity[T]) != 0) { // Valid Entity
							LANdeltaTime = time - timestamp[i];
							for (k = X; k <= Z; k++) {
								// Estimate position based on last known position and velocity
								entity1[k] = (LANentity[k] + LANdeltaTime * TRANSLATERATE * LANentity[VX + k]) / SCALEXYZ;
							}
							entity1[A] = LANentity[A];
						}
					}
				}
			}

			//
			// Collision Detect: Local Player Fireballs against Local and Remote Enemies and Remote Players
			//                   Local Enemy Fireballs against Local and Remote Players
			//

			for (i = 0; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = 0; j <= ENEMIES; j++) {
						entity1 = localData[i][j];
						if (entity1[T] != 0) { // Valid Entity
							// Entity1 = Local & Remote Enemies & Players
							for (k = ENEMIES + 1; k < ENTITIES; k++) {
								// Local Player fireballs do not damage Local Player
								if (i == 0 && j == 0 && k <= ENEMIES + PLAYERFIREBALLS)
									continue;
								// Local Enemy fireballs do not damage enemies
								if (j != 0 && k > ENEMIES + PLAYERFIREBALLS)
									continue;
								entity2 = localData[0][k]; // Local Fireball
								if (entity2[T] != 0) { // Valid Local Fireball
									// Entity2 = Local Fireball
									dx = entity1[X] - entity2[X];
									dz = entity1[Z] - entity2[Z];
									// Check end points
									if (dx * dx + dz * dz < COLLISIONRADIUS && entity2[Y] < entity1[Y] + 2f) {
										// Collision
										fireballTimeout[k - ENEMIES - 1] = time + FIREBALLTIMEOUT2;
										// Damage the local player or entity
										if (i == 0) {
											if (health[j] > 0)
												health[j]--;
											if (health[j] == 0 && j > 0)
												LANdata[0][j][T] = 0;
										}
									}
								}
							}
						}
					}
				}
			}

			//
			// Collision Detect: Remote Fireballs against Local Player & Entities
			//

			for (i = 1; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = ENEMIES + 1; j < ENTITIES; j++) {
						entity2 = localData[i][j];
						if (entity2[T] != 0) { // Valid Remote Fireball
							// Entity1 = Remote Fireballs
							for (k = 0; k <= ENEMIES; k++) {
								// Remote Enemy fireballs do not damage Local Enemies
								if (j > ENEMIES + PLAYERFIREBALLS && k != 0)
									continue;
								entity1 = localData[0][k]; // Local Entity
								if (entity1[T] != 0) { // Valid Local Entity
									// Entity2 = Local Player and Local Entities
									dx = entity1[X] - entity2[X];
									dz = entity1[Z] - entity2[Z];
									// Check end points
									if (dx * dx + dz * dz < COLLISIONRADIUS && entity2[Y] < entity1[Y] + 2f) {
										// Collision
										// Damage the local player or entity
										if (health[k] > 0)
											health[k]--;
										if (health[k] == 0 && k > 0)
											LANdata[0][k][T] = 0;
									}
								}
							}
						}
					}
				}
			}

			//
			// Collision detect local fireballs against terrain
			//
			for (i = ENEMIES + 1; i < ENTITIES; i++) {
				entity1 = localData[0][i];
				// Height Look Up
				mapX = (int) (entity1[X] + 1000) - 1000;
				mapZ = (int) (entity1[Z] + 1000) - 1000;
				tileX = (int) (TILESIZE * (entity1[X] - mapX));
				tileZ = (int) (TILESIZE * (entity1[Z] - mapZ));

				// Determine which height map to use
				tileType = MOUNTAIN;
				if (mapX >= 0 && mapX < MAPSIZE && mapZ >= 0 && mapZ < MAPSIZE)
					tileType = map[mapX][mapZ];

				// Find new fireball height
				if (tiles[tileType][tileX][tileZ] > entity1[Y]) {
					// Delete fireball
					LANdata[0][i][T] = 0;
				}
			}

			//
			// Player Movement: Set Player Velocity Vector and Angle
			//

			// Rotate player
			if (keyboard[Event.LEFT] || keyboard['a']) // Rotate Left
				angleY += ROTATERATE * deltaTime;
			if (keyboard[Event.RIGHT] || keyboard['d']) // Rotate Right
				angleY -= ROTATERATE * deltaTime;
			angleY = (angleY + 2f * (float) Math.PI) % (2f * (float) Math.PI);
			sinAngleY = sin[(int) (DEGREES * angleY)];
			cosAngleY = cos[(int) (DEGREES * angleY)];

			// Translate player
			rx = 0;
			rz = 0;
			if (keyboard[Event.UP] || keyboard['w']) { // Move Forward
				rx = -sinAngleY;
				rz = -cosAngleY;
			}
			if (keyboard[Event.DOWN] || keyboard['s']) { // Move Backward

				rx = sinAngleY;
				rz = cosAngleY;
			}

			// Determine player map square and location in that square
			x = playerX + TRANSLATERATE * deltaTime * rx;
			z = playerZ + TRANSLATERATE * deltaTime * rz;
			mapX = (int) (x + 1000) - 1000;
			mapZ = (int) (z + 1000) - 1000;
			tileX = (int) (TILESIZE * (x - mapX));
			tileZ = (int) (TILESIZE * (z - mapZ));

			// Determine which height map to use
			tileType = MOUNTAIN;
			if (mapX >= 0 && mapX < MAPSIZE && mapZ >= 0 && mapZ < MAPSIZE)
				tileType = map[mapX][mapZ];

			// Find new player height
			y = tiles[tileType][tileX][tileZ];//+ PLAYERHEIGHT;

			if (y >= 2) {
				rx = rz = 0;
			} else {
				playerX = x;
				playerY = y;
				playerZ = z;
			}
			LANentity = LANdata[0][0];
			LANentity[X] = (int) (SCALEXYZ * playerX);
			LANentity[Y] = (int) (SCALEXYZ * playerY);
			LANentity[Z] = (int) (SCALEXYZ * playerZ);
			LANentity[A] = (int) (DEGREES * angleY);
			LANentity[VX] = (int) (SCALEXYZ * rx);
			LANentity[VZ] = (int) (SCALEXYZ * rz);

			//
			// Entity Movement AI: Set Entity Velocity Vector and Angle
			//

			// Local Entities attack local player.
			// Remote entities no not attack local player but the player
			// is not immune from their fireballs.
			entity2 = localData[0][0]; // Use time corrected player coords
			for (j = 1; j <= ENEMIES; j++) {
				entity1 = localData[0][j];
				if (entity1[T] != 0) { // Valid Enemy
					dx = entity1[X] - entity2[X];
					dy = entity1[Y] - entity2[Y];
					dz = entity1[Z] - entity2[Z];
					depth = dx * dx + dz * dz;

					// Adjust enemy destination coordinates to prevent huddling
					k = ((entity1[X] - 3.5f) * (entity1[Z] - 3.5f) > 0) ? -1 : 1;
					x = dx + (j - (ENEMIES + 1) / 2f) * 0.1f * k;
					z = dz + (j - (ENEMIES + 1) / 2f) * 0.1f;

					// Determine velocity vector to destination
					// This requires an ARCTAN which is a slow operation
					angle = (int) (Math.atan2(x, z) * DEGREES);
					angle = (angle + 36000) % 36000;
					x = sin[angle]; // Unit Vector toward destination
					z = cos[angle]; // This is used for enemy movement

					// Determine angle to player
					// This requires an ARCTAN which is a slow operation
					dy = (dy - 1f) / dx;
					angle = (int) (Math.atan2(dx, dz) * DEGREES);
					angle = (angle + 36000) % 36000;
					dx = sin[angle]; // Unit Vector toward player
					dy *= dx;
					dz = cos[angle]; // This is used for enemy fireballs

					// Determine relative angle to player
					angle = (int) entity1[A] - angle; // This is used to control
					angle = (angle + 36000) % 36000; // enemy rotation

					// Enemy movement and firing AI
					LANdata[0][j][VX] = 0; // Default action is to
					LANdata[0][j][VZ] = 0; // stay in the same place

					// If we are too close to the player, turn and run
					if (depth < TOOCLOSE) {
						// Move away from the player
						LANdata[0][j][VX] = +(int) (x * SCALEXYZ);
						LANdata[0][j][VZ] = +(int) (z * SCALEXYZ);
						// Rotate enemy to face away from player
						if (angle > 19000) {
							LANdata[0][j][A] -= (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						} else if (angle < 17000) {
							LANdata[0][j][A] += (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						}
						LANdata[0][j][A] = (LANdata[0][j][A] + 36000) % 36000;

						// If we are too far from the player, face and close in
					} else if (depth > TOOFAR) {
						// Move towards the player
						LANdata[0][j][VX] = -(int) (x * SCALEXYZ);
						LANdata[0][j][VZ] = -(int) (z * SCALEXYZ);
						// Rotate enemy to face player
						if (angle > 1000 && angle < 18000) {
							LANdata[0][j][A] -= (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						} else if (angle < 35000 && angle > 18000) {
							LANdata[0][j][A] += (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						}
						LANdata[0][j][A] = (LANdata[0][j][A] + 36000) % 36000;

						// Otherwise stand our ground
					} else {
						// Rotate enemy to face player
						if (angle > 1000 && angle < 18000) {
							LANdata[0][j][A] -= (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						} else if (angle < 35000 && angle > 18000) {
							LANdata[0][j][A] += (int) (ROTATERATE * 10f * deltaTime * DEGREES);
						}
						LANdata[0][j][A] = (LANdata[0][j][A] + 36000) % 36000;

						// Fire at player if a fireball is available and
						// the enemy is facing the player
						if (localData[0][j + ENEMIES + PLAYERFIREBALLS][T] == 0 && (angle < 1000 || angle > 35000)) {

							// Set the fireball start location about halfway up the enemy
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][X] = LANdata[0][j][X];
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][Y] = LANdata[0][j][Y] + (int) SCALEXYZ;
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][Z] = LANdata[0][j][Z];

							// Set the fireball velocity equal to unit vector towards the player
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][VX] = -(int) (dx * SCALEXYZ);
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][VY] = -(int) (dy * SCALEXYZ);
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][VZ] = -(int) (dz * SCALEXYZ);

							// Set Fireball timeout
							LANdata[0][j + ENEMIES + PLAYERFIREBALLS][T] = LANdata[0][j][T];
							fireballTimeout[j + PLAYERFIREBALLS - 1] = time + FIREBALLTIMEOUT1;
						}
					}
				}
			}

			//
			// Player Fire
			//
			if (mouse) {
				for (i = ENEMIES + 1; i <= ENEMIES + PLAYERFIREBALLS; i++) {
					if (localData[0][i][T] == 0) {
						// Found an inactive fireball
						mouse = false;
						if (mouseX >= 00 && mouseX < SCREENWIDTH) {
							// Set Fireball Position to Player Position
							LANdata[0][i][X] = LANdata[0][0][X]; // Set position
							LANdata[0][i][Y] = LANdata[0][0][Y] + (int) (1.5f * SCALEXYZ);
							LANdata[0][i][Z] = LANdata[0][0][Z];

							// Set Fireball Velocity to vector based on mouse position
							rayAngle = angleY + rayAngleFix[mouseX];
							rayAngle = (rayAngle + 2f * (float) Math.PI) % (2f * (float) Math.PI);
							cosRayAngle = cos[(int) (DEGREES * rayAngle)];
							sinRayAngle = sin[(int) (DEGREES * rayAngle)];

							LANdata[0][i][VX] = -(int) (sinRayAngle * 2f * SCALEXYZ);
							// 40f=2*20f
							LANdata[0][i][VY] = -(int) (40f * ((float) (mouseY - SCREENHEIGHT / 2) / SCREENHEIGHT) * SCALEXYZ);
							LANdata[0][i][VZ] = -(int) (cosRayAngle * 2f * SCALEXYZ);

							// Set Fireball timeout
							LANdata[0][i][T] = 7;
							fireballTimeout[i - ENEMIES - 1] = time + FIREBALLTIMEOUT1;
						}
						break;
					}
				}
			}

			//
			// Delete expired fireballs
			//

			for (i = ENEMIES + 1; i < ENTITIES; i++) {
				if (localData[0][i][T] != 0) { // Fireball Active
					if (fireballTimeout[i - ENEMIES - 1] < time) {
						LANdata[0][i][T] = 0;
					}
				}
			}

			//
			// Move entities
			//

			for (i = 1; i < ENTITIES; i++) {
				if (localData[0][i][T] != 0) { // Entity Active
					LANentity = LANdata[0][i];
					for (j = X; j <= Z; j++) {
						LANentity[j] += deltaTime * TRANSLATERATE * LANentity[j + VX];
					}
				}
			}

			//
			// Adjust Enemy heights to match terrain
			//

			for (i = 1; i <= ENEMIES; i++) {
				x = localData[0][i][X];
				z = localData[0][i][Z];
				mapX = (int) (x + 1000) - 1000;
				mapZ = (int) (z + 1000) - 1000;
				tileX = (int) (TILESIZE * (x - mapX));
				tileZ = (int) (TILESIZE * (z - mapZ));

				// Determine which height map to use
				tileType = MOUNTAIN;
				if (mapX >= 0 && mapX < MAPSIZE && mapZ >= 0 && mapZ < MAPSIZE)
					tileType = map[mapX][mapZ];

				// Find new enemy height
				y = tiles[tileType][tileX][tileZ];
				LANdata[0][i][Y] = (int) (y * SCALEXYZ);
			}

			// Update timestamp for local data
			timestamp[0] = time;

			//
			// Castle ownership logic
			//

			// Determine whether there are any enemies in the castle sector
			boolean found = false;
			for (i = 0; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = 1; j <= ENEMIES; j++) {
						entity1 = localData[i][j];
						if (entity1[T] > 0) {
							if ((playerX > 3.5f) == (entity1[X] > 3.5f) && (playerZ > 3.5f) == (entity1[Z] > 3.5f))
								found = true;
							break;
						}
					}
				}
			}
			if (!found) {
				// If there are no enemies allow change of castle ownership
				for (i = 0; i < PLAYERS; i++) {
					if (timestamp[i] + TIMEOUT > time) { // Valid player
						j = (int) localData[i][0][X];
						k = (int) localData[i][0][Z];
						if ((j == 1 || j == 5) && (k == 1 || k == 5)) {
							castle[(j - 1) / 4 + 2 * ((k - 1) / 4)] = (i == 0);
						}
					}
				}
			}

			//
			// Spawn enemies
			//

			// If player is in a quarter they don't own, then spawn
			if ((int) playerX == 3 && (int) playerZ == 3) {
				for (i = 1; i <= ENEMIES; i++)
					LANdata[0][i][T] = 0;
				allowSpawn = true; // Player Retreated so allow respawn
			} else {
				j = playerX < 3.5f ? 1 : 5;
				k = playerZ < 3.5f ? 1 : 5;
				if (allowSpawn && !castle[(j - 1) / 4 + 2 * ((k - 1) / 4)]) {
					// Spawn all enemies
					for (i = 1; i <= ENEMIES; i++) {
						LANdata[0][i][X] = (int) ((j + 0.5f) * SCALEXYZ);
						LANdata[0][i][Z] = (int) ((k + 0.5f) * SCALEXYZ);
						LANdata[0][i][T] = (i % 6) + 1;
						health[i] = ENEMYHEALTH;
					}
				}
				allowSpawn = false; // No respawning once battle commenced
			}

			//
			//  Draw 3D View
			//

			// Pre-calculate which entities are likely to be on screen
			drawCount1 = 0;
			for (i = 0; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = (i == 0) ? 1 : 0; j <= ENEMIES; j++) {
						entity1 = localData[i][j];
						if (entity1[T] != 0f) { // Valid entity
							// Don't draw entities more than 3 tiles away
							// Don't draw entities behind the camera
							dx = entity1[X] - playerX;
							dz = entity1[Z] - playerZ;
							if (dx * dx + dz * dz < ENTITYDEPTH && dx * sinAngleY + dz * cosAngleY < 0)
								drawList1[drawCount1++] = entity1;
						}
					}
				}
			}

			for (ray = 0; ray < SCREENWIDTH; ray += 2) {
				rayAngle = angleY + rayAngleFix[ray];
				rayAngle = (rayAngle + 2f * (float) Math.PI) % (2f * (float) Math.PI);
				cosRayAngle = cos[(int) (DEGREES * rayAngle)] / cosRayAngleFix[ray];
				sinRayAngle = sin[(int) (DEGREES * rayAngle)] / cosRayAngleFix[ray];

				// Pre-calculate which entities intersect ray
				drawCount2 = 0;
				for (i = 0; i < drawCount1; i++) {
					// Rotate into player's coordinate frame
					dx = drawList1[i][X] - playerX;
					dz = drawList1[i][Z] - playerZ;
					radius = dx * cosRayAngle - dz * sinRayAngle;

					if (radius * radius < ENTITYRADIUS)
						drawList2[drawCount2++] = drawList1[i];
				}

				int lsy = SCREENHEIGHT - 1;
				// Time critical loop!
				for (i = 1; i < DEPTH; i++) {
					depth = zMap[i];
					x = playerX - sinRayAngle * depth;
					z = playerZ - cosRayAngle * depth;

					// Get height 'y' at distance 'p' on column 'ray'
					mapX = (int) (x + 1000) - 1000;
					mapZ = (int) (z + 1000) - 1000;
					tileX = (int) (TILESIZE * (x - mapX));
					tileZ = (int) (TILESIZE * (z - mapZ));

					// Determine which height map to use
					tileType = MOUNTAIN;
					if (mapX >= 0 && mapX < MAPSIZE && mapZ >= 0 && mapZ < MAPSIZE)
						tileType = map[mapX][mapZ];

					// Find terrain height
					y = tiles[tileType][tileX][tileZ];

					boolean drawObject = false; // No drawable objects

					// Check for drawable object at this location
					// ****Ultra time critical loop****
					for (j = 0; j < drawCount2; j++) {
						entity1 = drawList2[j];
						dx = entity1[X] - x;
						dz = entity1[Z] - z;
						// Draw if we are within the entity's base radius
						if (dx * dx + dz * dz < ENTITYRADIUS) {
							angle = (int) entity1[A];

							// Adjust origin to be at centre of entity
							dx = dx * TILESIZE / ENTITYSCALE;
							dz = dz * TILESIZE / ENTITYSCALE;

							// Rotate entity coordinates
							x1 = (int) (dx * cos[angle] - dz * sin[angle] + CENTRE);
							if (x1 < 0 || x1 >= ENTITYSIZE)
								continue;
							z1 = (int) (dx * sin[angle] + dz * cos[angle] + CENTRE);
							if (z1 < 0 || z1 >= ENTITYSIZE)
								continue;

							// Check for non-zero height
							if (entityHeight[x1][z1] > 0) {
								drawObject = true;
								break; // Quit on first object
							}
						}
					}

					colour = 0x000200; // Default to green
					if (drawObject) {
						// Define object colour and height
						y += (float) entityHeight[x1][z1] / ENTITYSCALE;
						switch (entityColour[x1][z1]) {
						case 0:
							colour = (cMap[(int) entity1[T]] & 0x007f7f7f) + 0x080808 * (x1 + z1);
							break;
						case 1:
							colour = 0;
							break;
						case 2:
							colour = 0x800000 + 0x080808 * (x1 + z1);
							break;
						default:
							colour = 0x801010;
						}
					} else {
						// Define Ground Colour
						switch (tileType) {
						case PLAIN:
							//case GRASS:
							y = tiles[GRASS][tileX][tileZ];
							break;
						case MOUNTAIN:
							if (tiles[PLAIN][tileX][tileZ] < y)
								colour = 0x020201;
							else
								y = tiles[GRASS][tileX][tileZ];
							break;
						default: // CASTLE
							if (mapX == 1) {
								if (mapZ == 1)
									colour = 0x000001; // Blue
								else
									colour = 0x010000; // Red
							} else {
								if (mapZ == 1)
									colour = 0x000100; // Green
								else
									colour = 0x010100; // Yellow
							}

							// Colour dappling
							if (((tileX / DAPPLE) & 1) == ((tileZ / DAPPLE) & 1))
								colour += 0x010101;
						}
						// Colour variation with height and distance
						colour *= (int) (16 * ((4f + (y < 2f ? y / 1f : 2f)) / (1f + depth))) & 255;
					}

					// Draw ground

					// Calculate screen position
					sy = SCREENHEIGHT / 2 - (int) (20f * (y - playerY - PLAYERHEIGHT) / depth);
					if (sy < 0)
						sy = 0;
					if (sy > SCREENHEIGHT - 1)
						sy = SCREENHEIGHT - 1;

					if (sy < lsy) {
						dst = ray + lsy * SCREENWIDTH;
						for (j = lsy; j > sy; j--) {
							screenData[dst] = screenData[dst + 1] = colour;
							zBuffer[dst] = zBuffer[dst + 1] = -depth;
							dst -= SCREENWIDTH;
						}
						lsy = sy;
					}
				}

				// Draw sky
				dst = ray + lsy * SCREENWIDTH;
				for (j = lsy; j >= 0; j--) {
					screenData[dst] = screenData[dst + 1] = ((health[0] < 10) ? 0x400040 : 0x200080) + 0x010101 * (j / 3);
					zBuffer[dst] = zBuffer[dst + 1] = -Float.MAX_VALUE;
					dst -= SCREENWIDTH;
				}
			}

			// Draw fireballs
			for (i = 0; i < PLAYERS; i++) {
				if (timestamp[i] + TIMEOUT > time) { // Valid player
					for (j = ENEMIES + 1; j < ENTITIES; j++) {
						entity1 = localData[i][j];
						if (entity1[T] != 0) { // Valid Entity

							// Rotate into player's coordinate frame
							dx = entity1[X] - playerX;
							dz = entity1[Z] - playerZ;
							rx = dx * cosAngleY - dz * sinAngleY;
							rz = dx * sinAngleY + dz * cosAngleY;

							// Draw fireball if in front of player
							if (dx * dx + dz * dz < ENTITYDEPTH && rz < 0) {
								sx = SCREENWIDTH / 2 - (int) (SCREENDEPTH * rx / rz);
								sy = SCREENHEIGHT / 2 + (int) (20f * (entity1[Y] - playerY - PLAYERHEIGHT) / rz);
								// Limit number of points drawn to 10,000 maximum
								drawCount1 = (int) (50 / (rz * rz));
								if (drawCount1 > 50000)
									drawCount1 = 50000;
								colour = cMap[(int) entity1[T]];
								for (k = 0; k < drawCount1; k++) {
									radius = 5f * (float) Math.random() / rz;
									angle = (int) (36000 * Math.random());
									x1 = sx + (int) (radius * sin[angle]);
									y1 = sy + (int) (radius * cos[angle]);
									if (x1 > 0 && x1 < SCREENWIDTH && y1 >= 0 && y1 < SCREENHEIGHT) {
										dst = x1 + SCREENWIDTH * y1;
										if (zBuffer[dst] < rz)
											screenData[dst] = colour;
									}
								}
							}
						}
					}
				}
			}

			//
			// Overlay
			//

			for (i = 10; i < 20; i++) { // Draw 10 rows
				for (j = 0; j < health[0]; j++) { // Draw health bar
					screenData[10 + j + SCREENWIDTH * i] = 0x00ff00;
				}
				for (k = 0; k < 4; k++) { // Draw castles owned
					colour = 0xff << (8 * k);
					if (k == 3)
						colour = 0xffff00;
					if (!castle[k])
						colour = 0;
					for (j = 0; j < 10; j++) {
						screenData[120 + k * 20 + j + SCREENWIDTH * i] = colour;
					}
				}
			}

			//
			// Draw Screen
			//

			if (gs != null) {
				gs.drawImage(screen, 0, 0, null);
			}
			Thread.yield();
		} while (isActive());

		// Tidy up
		//gs.dispose();
		socket.close();
	}

	/** Process Keyboard and Mouse Events */
	@Override
	public boolean handleEvent(Event e) {
		switch ((e.id - 1) | 1) {
		case java.awt.Event.KEY_PRESS:
		case java.awt.Event.KEY_ACTION:
			keyboard[e.key] = (e.id & 1) == 1;
			return true;
		case java.awt.Event.MOUSE_DOWN:
			mouse = (e.id & 1) == 1;
			mouseX = e.x;
			mouseY = e.y;
			return true;
		default:
		}
		return false;
	}
}
