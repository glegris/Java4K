package java4k.inthedark4k;
/* 
 * In The Dark 4K
 * By Gef
 * 
 * 
 * The story
 * ---------
 * After being captured by the ghosts of last year,
 * our sorcerer has been locked in a jail in the basements of the witch’s castle.
 * After several months, he decides to escape. He can still shoot fireballs.
 * And, because of the darkness, he uses a spell which produces a light above him,
 * but it runs out over time. To revive it, he needs to kill creatures.
 * So pay attention to his intensity…
 * 
 * To go upstairs through the 5 levels, he has to collect the coins scattered all
 * over each one.
 * 
 * The UI
 * ------
 *  - green bar is for life level
 *  - white bar is for light intensity
 *  - on the right, the number of coins to collect in the current level
 *  
 * Game Controls
 * -------------
 *  - Move : Arrow keys
 *  - Jump : SPACE
 *  - Fire : W
 *  - Pause : P
 *  - Start/continue : ENTER
 * 
 * The player can move freely, but when you start shooting,
 * the shooting direction locks until you release the fire key.
 * 
 * Technical notes
 * ---------------
 * For speed and best compression, lots of variables are transform to fixed ints (24:8) :
 *  - Storage : y = x * 256 (<<8)
 *  - Reading : x = y / 256 (>>8)
 * 
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class A extends Applet implements Runnable {

	final boolean keys[] = new boolean[65535];

	public A() {
		new Thread(this).start();
	}

	private void addEntity(ArrayList<int[]> entities, int type, int x, int y, int z, int dirX, int dirY, int dirZ, int model, int angle, int life, int fx, int fy, int fz, int animSpeed) {
		entities.add(
				new int[] {
					// TYPE
					type,
					// X, Y, Z
					x << 8, y << 8, z << 8,
					// DIRX, DIRY, DIRZ
					dirX << 8, dirY << 8, dirZ << 8,
					// MODEL, ANGLE
					model, angle << 8,
					// TOUCH_GROUND
					0,
					// LIFE
					life,
					// FORCEX, FORCEY, FORCEZ
					fx << 8, fy << 8, fz << 8,
					// ANIMATION_SPEED
					animSpeed << 8
				}
			);
	}

	@Override
	public void run() {
		final boolean DEBUG = false;
		
		// Rendering constants
		final int SCREEN_WIDTH = 672;
		final int SCREEN_HEIGHT = 600;
		final int WORLD_SIZE_X = 1024;
		final int WORLD_SIZE_Y = 29;
		final int WORLD_SIZE_Z = 1024;
		final int FRUSTUM_SIZE = 84;

		final int BLOCK_X = 4;
		final int BLOCK_Y = 3;

		final int LIGHT_DISTANCE = 84;
		final int PCF_SHADOW = 10;

		// Entity types
		final int ENTITY_PLAYER_TYPE = 0;
		final int ENTITY_ENENY_TYPE = 1;
		final int ENTITY_BULLET_TYPE = 2;
		final int ENTITY_OBJECT_TYPE = 3;
		final int ENTITY_BLOCK_TYPE = 4;

		// Model angles
		final int FRONT_ANGLE = 0;
		final int LEFT_ANGLE = 1;
		final int BACK_ANGLE = 2;
		final int RIGHT_ANGLE = 3;

		// Motion constants
		final int GRAVITY = 64000;		// 250<<8;
		final int PLAYER_SPEED = 15360;	// 60<<8;
		final int PLAYER_JUMP = 25600;	// 100<<8;
		final int OBJECT_JUMP = 38400;	// 150<<8;
		final int BULLET_SPEED = 100;	// will be transformed in fixed int by addEntity(...)
		final int ENEMY_SPEED = 6400;	// 25<<8;
		final int EXPLOSION_RADIUS = 10;
		final int EXPLOSION_RADIUS_FORCE = 10;
		final int STEP_HEIGHT = 5;
		final int MIN_MOVING_DISTANCE = 6553600;	// (10<<8)*(10<<8) Delta used to verify if entity is moving;

		// Entity attributes
		final int TYPE = 0;
		final int X = 1;
		final int Y = 2;
		final int Z = 3;
		final int DIRX = 4;
		final int DIRY = 5;
		final int DIRZ = 6;
		final int MODEL = 7;
		final int ANGLE = 8;
		final int TOUCH_GROUND = 9;
		final int LIFE = 10;
		final int FORCEX = 11;
		final int FORCEY = 12;
		final int FORCEZ = 13;
		final int ANIMATION_SPEED = 14;

		// Block types
		final int BLOCK_TRANSPARENT = 0;
		final int BLOCK_EPHEMERAL = 1;
		final int BLOCK_DECOR = 2;
		final int BLOCK_ENTITY = 3;
		final int BLOCK_SOLID = 4;
		final int BLOCK_TYPE_SHIFT = 4;
		final int BLOCK_VALUE_BITMASK = 15;

		// Game status
		final int GENERATE_MAP = 0;
		final int PLAYING = 1;
		final int PAUSE = 2;
		final int NEXT_LEVEL = 3;
		final int YOU_LOOSE = 4;
		final int YOU_WIN = 5;

		// Keyboard values
		final int KEY_ENTER = 10;
		final int KEY_SPACE = 32;
		final int KEY_P = 112;
		final int KEY_W = 119;
		final int KEY_UP = 1004;
		final int KEY_DOWN = 1005;
		final int KEY_LEFT = 1006;
		final int KEY_RIGHT = 1007;

		// Entity models
		final int MODEL_BUZZ_ENEMY = 2;
		final int MODEL_BLOB_ENEMY = 3;
		final int MODEL_HEART = 4;
		final int MODEL_GROUND = 5;
		final int MODEL_WALL = 6;
		final int MODEL_COIN = 7;

		// Maze generation
		final int CELL_SIZE = 64;
		final int MIN_WALL_SIZE = 4;

		// Color panel
		final String STRING_COLORS =
				"\u0080\u00FF\u0000" +
				"\u00EF\u00E4\u00B0" +
				"\u0000\u0040\u0000" +
				"\u00FF\u00FF\u00FF" +
				"\u00FF\u00FF\u0000" +
				"\u00FF\u0080\u0000" +
				"\u0000\u00FF\u0000" +
				"\u00FF\u0000\u0000" +
				"\u00FF\u0080\u00FF" +
				"\u0080\u0080\u0080" +
				"\u00FF\u0000\u00FF" +
				"\u00C0\u00C0\u00C0";

		// Levels
		final String STRING_LEVELS =
				"\u0004\u0004\u001C\u001C" +
				"\u0002\u0003\u0005\u0000" +
				"\u0000\u0000\u000E\u0001" +
				"\u0006\u0001\f\u0000" +
				"\n\u0003\u000B\u0001" +
				"\u0005" +

				"\u0006\u0003\u00DC\u001C" +
				"\u0002\u0007\u0001" +
				"\u0000\f\u0004" +
				"\u0006\u000B\t" +
				"\n\u0005\u0000" +
				"\u0006\t\u0000" +
				"\n\u0003\u0001" +
				"\u0006" +

				"\u0007\u0007\u011C\u001C" +
				"\u0000\u0006\u0007\u0003\u0007\u0005\u0000" +
				"\u0006\t\u000E\u0003\r\n\u0005" +
				"\u000E\u0007\t\u0000\n\u0007\r" +
				"\f\f\u0000\u0000\u0000\f\f" +
				"\u000E\u000B\u0005\u0000\u0006\u000B\r" +
				"\n\u0005\u000E\u0003\r\u0006\t" +
				"\u0000\n\u000B\u0003\u000B\t\u0000" +
				"\u000C" +

				"\n\u0005\u009C\u001C" +
				"\u0006\u0001\u0000\u0000\u0000" +
				"\n\u0005\u0004\u0006\u0005" +
				"\u0006\t\n\t\f" +
				"\f\u0000\u0006\u0005\f" +
				"\u000E\u0003\r\u000E\r" +
				"\b\u0000\n\t\f" +
				"\u0000\u0004\u0000\u0004\f" +
				"\u0006\r\u0006\u000B\t" +
				"\b\f\u000E\u0001\u0000" +
				"\u0000\n\t\u0000\u0000" +
				"\u000C" +

				"\n\n\u015C\u011C" +
				"\u0000\u0000\u0004\u0006\u0001\u0000\u0000\u0004\u0000\u0000" +
				"\u0000\u0000\u000E\u000B\u0001\u0000\u0004\u000E\u0001\u0000" +
				"\u0002\u0005\u000E\u0007\u0003\u0003\u000B\u000F\u0007\u0001" +
				"\u0006\t\f\f\u0000\u0000\u0002\r\b\u0000" +
				"\n\u0003\r\f\u0004\u0004\u0000\f\u0000\u0000" +
				"\u0000\u0006\t\b\n\u000B\u0003\r\u0006\u0005" +
				"\u0006\u000B\u0003\u0003\u0001\u0000\u0000\u000E\t\b" +
				"\u000E\u0005\u0000\u0004\u0004\u0004\u0004\f\u0000\u0000" +
				"\f\n\u0003\u000B\u000F\u000B\r\u000E\u0001\u0004" +
				"\n\u0003\u0001\u0000\b\u0000\n\u000B\u0003\t" +
				"\u0026";

		// Models
		final String STRING_OBJECTS =
				// Player
				"\u000B\u000B\u000B\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0033\u0033\u0033\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0034\u0031\u0031\u0033\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0031\u0000\u0031\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0032\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0033\u0033\u0033\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0031\u0031\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
				// Bullet
				"\u0003\u0003\u0004\u0000\u0015\u0000\u0000\u0015\u0016\u0016\u0015\u0000\u0015\u0000\u0000\u0015\u0015\u0015\u0000\u0015\u0015\u0016\u0016\u0015\u0016\u0015\u0000\u0000\u0015\u0000\u0000\u0015\u0016\u0015\u0000\u0000\u0015\u0000\u0000" +
				// Buzz
				"\u0007\b\u0005\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0036\u0000\u0032\u0000\u0000\u0000\u0032\u0032\u0000\u0000\u0036\u0036\u0000\u0000\u0000\u0037\u0036\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0036\u0036\u0000\u0000\u0000\u0036\u0036\u0000\u0000\u0000\u0036\u0036\u0038\u0000\u0036\u0036\u0036\u0038\u0000\u0000\u0036\u0036\u0038\u0000\u0038\u0038\u0038\u0000\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0036\u0000\u0032\u0000\u0000\u0000\u0032\u0032\u0000\u0000\u0036\u0036\u0000\u0000\u0000\u0037\u0036\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0036\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0032\u0000\u0000\u0000\u0000\u0000" +
				// Blob
				"\u0006\u0006\u0007\u0000\u0000\u0000\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0000\u0000\u0000\u0000\u0000\u0039\u0000\u0000\u0000\u0000\u0000\u0000\u0039\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0039\u0039\u0000\u0000\u0039\u0000\u0039\u0039\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0034\u0039\u0039\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0039\u0039\u0039\u0039\u0039\u0000\u0000\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0000\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0039\u0039\u0039\u0000\u0039\u0039\u0039\u0039\u0039\u0000\u0000\u0039\u0039\u0039\u0039\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0034\u0039\u0039\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0039\u0039\u0039\u0000\u0000\u0000\u0000\u0039\u0039\u0000\u0000\u0000\u0000\u0000\u0039\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" +
				// Heart
				"\u0007\u0006\u0003\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000\u0000\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0000\u0038\u0038\u0000\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0038\u0038\u0000\u0038\u0038\u0000\u0000\u0000" +
				// Ground
				"\b\u0001\b\u002A\u002A\u002A\u0000\u0000\u002A\u002A\u002A\u002A\u002A\u0000\u002A\u002A\u0000\u002A\u002A\u002A\u0000\u002A\u002A\u002A\u002A\u0000\u002A\u0000\u002A\u002A\u002A\u002A\u002A\u002A\u0000\u0000\u002A\u002A\u0000\u002A\u002A\u002A\u0000\u002A\u0000\u002A\u002A\u002A\u002A\u0000\u002A\u002A\u002A\u0000\u002A\u002A\u0000\u002A\u002A\u0000\u002A\u002A\u0000\u0000\u002A\u002A\u002A" +
				// Wall
				"\f\f\u0001\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\u0000\u0000\u0000\u0000\u0000\"\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\"\"\"\u0000\u0000\u0000\"\"\u0000\u0000\u0000\"\"\"\"\u0000\u0000\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\u0000\u0000\u0000\u0000\u0000\"\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"\"\"\"\u0000\u0000\u0000\"\"\u0000\u0000\u0000\"\"\"\"\u0000\u0000\"\"\"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\"" +
				// Coin
				"\u0005\u0005\u0002\u0000\u0000\u0000\u0035\u0000\u0035\u0000\u0035\u0000\u0000\u0000\u0035\u0035\u0035\u0035\u0035\u0035\u0035\u0000\u0035\u0000\u0035\u0035\u0035\u0035\u0035\u0035\u0035\u0000\u0035\u0000\u0035\u0035\u0035\u0035\u0035\u0035\u0035\u0000\u0035\u0000\u0000\u0000\u0035\u0000\u0035\u0000\u0035\u0000\u0000";

		// Level map
		final byte[][][] box = new byte[WORLD_SIZE_X][WORLD_SIZE_Y][WORLD_SIZE_Z];
		// Models array
		final byte[][][][][] models = new byte[8][][][][];
		// Color array
		final int[] colors = new int[LIGHT_DISTANCE * 16];
		// Maze array
		byte[][] maze = null;

		// Screen manipulation
		final BufferedImage screen = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		final int[] pixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
		final Graphics gfx = screen.getGraphics();
		Graphics appletGraphics = null;
		
		// entities collection
		ArrayList<int[]> entities = null;

		// Other variables
		int map_size_x = 0;
		int map_size_z = 0;
		int tick = 0;
		int fps = 0;
		int acc = 0;
		int mazeWidth = 0;
		int mazeHeight = 0;
		int level = 1;
		int nbEnemies = 0;
		int nbEnemiesToPop = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int frustumX = 0;
		int frustumZ = 0;
		int playerLife = 0;
		int playerX = 0;
		int playerZ = 0;
		int gameState = 0;
		int lightStrength = 0;
		int nbCoins = 0;
		long lastFrame = 0;
		long nextBullet = 0;
		long nextLightVariation = 0;

		/*******************
		 * Initialisations *
		 *******************/
		
		if (DEBUG) {
			setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
			requestFocus();
		}

		// Color panel
		k = LIGHT_DISTANCE;
		char[] c = STRING_COLORS.toCharArray();
		for (i = 0; i < c.length; i += 3) {
			for (j = LIGHT_DISTANCE - 1; j >= 0; j--) {
				float coef = 1 - 1 / ((j / (float) (LIGHT_DISTANCE - 1) + 1) * (j / (float) (LIGHT_DISTANCE - 1) + 1));
				colors[k++] = ((int) (c[i] * coef) << 16) + ((int) (c[i + 1] * coef) << 8) + (int) (c[i + 2] * coef);
			}
		}

		// Models collection
		c = STRING_OBJECTS.toCharArray();
		int n = 0;
		for (l = 0; l < c.length;) {

			// Model dimensions
			int x = c[l];
			int y = c[l + 1];
			int z = c[l + 2];
			l += 3;

			// Initialise the 8 models arrays : 4 standards & 4 variants
			models[n] = new byte[][][][] {
				// 4 standards
				// Front
				new byte[x][y][z],
				// Left
				new byte[z][y][x],
				// Back
				new byte[x][y][z],
				// Right
				new byte[z][y][x],
				// 4 variants used for animation
				// Front
				new byte[x][y][z],
				// Left
				new byte[z][y][x],
				// Back
				new byte[x][y][z],
				// Right
				new byte[z][y][x]
			};

			// Filling of models arrays
			for (i = 0; i < x; i++) {
				for (j = 0; j < y; j++) {
					for (k = 0; k < z; k++, l++) {
						byte[][][][] model = models[n];
						int x1 = x - i - 1;
						int z1 = z - k - 1;
						// Front
						model[FRONT_ANGLE][i][j][k] =
						model[FRONT_ANGLE + 4][x1][j][k] =
						// Back
						model[BACK_ANGLE][x1][j][z1] =
						model[BACK_ANGLE + 4][i][j][z1] =
						// Right
						model[LEFT_ANGLE][k][j][x1] =
						model[LEFT_ANGLE + 4][k][j][i] =
						// Left
						model[RIGHT_ANGLE][z1][j][i] =
						model[RIGHT_ANGLE + 4][z1][j][x1] = (byte) c[l];
					}
				}
			}
			n++;
		}

		/*************
		 * Game loop *
		 *************/
		
		while (true) {
			long now = System.nanoTime();

			gfx.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			if (gameState == GENERATE_MAP) {

				// Loading of the next Level
				
				// Reset some variables
				if (playerLife<5) playerLife = 5;
				lightStrength = LIGHT_DISTANCE-1;
				nbCoins = 0;
				nbEnemies = 0;
				gameState = NEXT_LEVEL;

				c = STRING_LEVELS.toCharArray();

				// Loop to find the current level
				l = 1;
				k = 0;
				while (l < level) {
					k += (c[k] * c[k + 1] + 5);
					l++;
				}

				mazeWidth = c[k];
				mazeHeight = c[k + 1];

				playerX = c[k + 2];
				playerZ = c[k + 3];

				// Initialise entities list with just the player
				entities = new ArrayList<int[]>();
				addEntity(
						entities,
						// TYPE
						ENTITY_PLAYER_TYPE,
						// X, Y, Z
						playerX, 18, playerZ,
						// DIRX, DIRY, DIRZ
						0, 0, 0,
						// MODEL, ANGLE
						0, 2,
						// LIFE
						playerLife,
						// FORCEX, FORCEY, FORCEZ
						0, 0, 0,
						// ANIMATION_SPEED
						10);

				// Reading of the maze
				maze = new byte[mazeWidth][mazeHeight];

				for (i = 0; i < mazeWidth; i++) {
					for (j = 0; j < mazeHeight; j++, k++) {
						byte cell = maze[i][j] = (byte) c[k + 4];

						// If the current cell is not filled, add a coin in it
						if (cell > 0 && (i != playerX / CELL_SIZE || j != playerZ / CELL_SIZE)) {
							nbCoins++;
							addEntity(
									entities,
									// TYPE
									ENTITY_OBJECT_TYPE,
									// X, Y, Z
									i * CELL_SIZE + CELL_SIZE / 2, 15, j * CELL_SIZE + CELL_SIZE / 2,
									// DIRX, DIRY, DIRZ
									0, 0, 0,
									// MODEL, ANGLE
									MODEL_COIN, 0,
									// LIFE
									1,
									// FORCEX, FORCEY, FORCEZ
									0, 0, 0,
									// ANIMATION_SPEED
									0);

						}

					}
				}
				nbEnemiesToPop = c[k+4];

				// Creation of the voxelbox based on the maze
				map_size_x = mazeWidth * CELL_SIZE;
				map_size_z = mazeHeight * CELL_SIZE;

				for (i = 0; i < map_size_x; i++) {
					for (j = 0; j < map_size_z; j++) {

						byte cell = maze[i / CELL_SIZE][j / CELL_SIZE];

						boolean murNord = (cell & 1) == 0;
						boolean murSud = (cell & 2) == 0;
						boolean murEst = (cell & 4) == 0;
						boolean murOuest = (cell & 8) == 0;

						int x1 = murOuest ? MIN_WALL_SIZE : 0;
						int x2 = murEst ? CELL_SIZE - MIN_WALL_SIZE : CELL_SIZE;
						int z1 = murNord ? MIN_WALL_SIZE : 0;
						int z2 = murSud ? CELL_SIZE - MIN_WALL_SIZE : CELL_SIZE;
						
						for (int y = WORLD_SIZE_Y - 1; y >= 0; y--) {
							// Default block = unbreakable wall
							byte block = ((BLOCK_SOLID << BLOCK_TYPE_SHIFT) + 10);
							// If cell is open
							if (cell > 0) {
								// Default block = breakable wall
								block = ((BLOCK_DECOR << BLOCK_TYPE_SHIFT) + 12);
								if (y <= 5) {
									//              /              Ground                \   /                Tile                  \
									block = y < 4 ? (BLOCK_DECOR << BLOCK_TYPE_SHIFT) + 10 : models[MODEL_GROUND][0][i % 8][0][j % 8];
								} else {
									// Determine walls
									int x = i - (i / CELL_SIZE) * CELL_SIZE;
									int z = j - (j / CELL_SIZE) * CELL_SIZE;
									if (x >= x1 && x < x2 && z >= z1 && z < z2) {
										block = 0;
										if ((murNord && z == z1) || (murSud && z == z2 - 1))
											block = models[MODEL_WALL][0][i % 12][y % 12][0];
										if ((murOuest && x == x1) || (murEst && x == x2 - 1))
											block = models[MODEL_WALL][1][0][y % 12][j % 12];
									}
								}
							}

							box[i + 1][y][j + 1] = block;
						}
					}
				}

			}

			// Calculate time elapsed since the last frame
			float timeElapsed = (int)(now - lastFrame) / 1000000000f;
			if (timeElapsed > 0.1f) {
				timeElapsed = 0.1f;
			}
			
			if (DEBUG) {
				// Fps counter
				acc += now - lastFrame;
				tick++;
				if (acc >= 1000000000L) {
					acc -= 1000000000L;
					fps = tick;
					tick = 0;
				}
			}

			lastFrame = now;

			// Friction for this frame
			float friction = 0.85f * (1 - timeElapsed);

			// Loop through the entities list
			for (int e = entities.size() - 1; e >= 0; e--) {
				int[] entity = entities.get(e);

				int entityType = entity[TYPE];
				
				// Store some variables
				byte[][][] model = null;
				
				int x = entity[X] >> 8;
				int y = entity[Y] >> 8;
				int z = entity[Z] >> 8;

				int dx = entity[DIRX];
				int dy = entity[DIRY];
				int dz = entity[DIRZ];

				boolean firing = false;

				// Model & Dimensions
				int sx = 1;
				int sy = 1;
				int sz = 1;

				if (entityType < ENTITY_BLOCK_TYPE) {
					model = models[entity[MODEL]][entity[ANGLE] >> 8];
					sx = model.length;
					sy = model[0].length;
					sz = model[0][0].length;
				}

				// Clear entity at current position
				for (i = x; i < x + sx; i++) {
					for (j = y; j < y + sy; j++) {
						for (k = z; k < z + sz; k++) {
							if ((box[i][j][k] >> BLOCK_TYPE_SHIFT) > BLOCK_TRANSPARENT) {
								box[i][j][k] = 0;
							}
						}
					}
				}

				// Player related modifications
				if (entityType == ENTITY_PLAYER_TYPE) {
					dx = 0;
					dz = 0;
					playerX = x + 5;
					playerZ = z + 6;

					if (gameState == PLAYING) {
						if (keys[KEY_UP]) {
							dz = PLAYER_SPEED;
						}
						if (keys[KEY_DOWN]) {
							dz = -PLAYER_SPEED;
						}
						if (keys[KEY_LEFT]) {
							dx = -PLAYER_SPEED;
						}
						if (keys[KEY_RIGHT]) {
							dx = PLAYER_SPEED;
						}

						if (keys[KEY_SPACE] && entity[TOUCH_GROUND] == 1) {
							dy = PLAYER_JUMP;
						}
						if (keys[KEY_P]) {
							gameState = PAUSE;
						}

						firing = keys[KEY_W];

						// Light Variation
						if (now >= nextLightVariation) {
							lightStrength--;
							nextLightVariation = now + 1000000000L;
						}
					}
				}

				// Enemy direction
				if (entityType == ENTITY_ENENY_TYPE) {
					dx = playerX - x;
					dz = playerZ - z;
					int sqrt = (ENEMY_SPEED<<8) / ((int) Math.sqrt(dx * dx + dz * dz) + 1);
					dx = (dx * sqrt)>>8;
					dz = (dz * sqrt)>>8;
				}

				// Object jump
				if (entityType == ENTITY_OBJECT_TYPE && entity[TOUCH_GROUND] == 1) {
					dy += OBJECT_JUMP;
				}

				// Friction & Gravity
				if (entityType != ENTITY_BULLET_TYPE) {
					// Friction
					dx *= friction;
					dy *= friction;
					dz *= friction;

					// Gravity
					dy -= GRAVITY * timeElapsed;
				}

				// Determine variant & direction for the model
				if (entityType < ENTITY_OBJECT_TYPE) {
					i = j = entity[ANGLE] >> 8;
					 
					if (dz * dz > dx * dx) {
						if (dz > 0) {
							i = BACK_ANGLE;
						}
						if (dz < 0) {
							i = FRONT_ANGLE;
						}
					} else {
						if (dx > 0) {
							i = RIGHT_ANGLE;
						}
						if (dx < 0) {
							i = LEFT_ANGLE;
						}
					}
					
					entity[ANGLE] += (int)(entity[ANIMATION_SPEED]*timeElapsed);
					if (j != (entity[ANGLE] >> 8)) {
						entity[ANGLE] = ((j + 4) & 7) << 8;
					}
					if (!(firing || i == ((entity[ANGLE] >> 8) & 3))) {
						entity[ANGLE] = i << 8;
					}
				}

				// Friction on external force
				entity[FORCEX] *= friction;
				entity[FORCEY] *= friction;
				entity[FORCEZ] *= friction;

				// Add external force
				dx += entity[FORCEX];
				dy += entity[FORCEY];
				dz += entity[FORCEZ];

				// Save new direction
				entity[DIRX] = dx;
				entity[DIRY] = dy;
				entity[DIRZ] = dz;

				// Move for this frame
				dx *= timeElapsed;
				dy *= timeElapsed;
				dz *= timeElapsed;

				// New model & dimensions
				int decal = 0;
				if (entityType < ENTITY_BLOCK_TYPE) {
					model = models[entity[MODEL]][entity[ANGLE] >> 8];
					sx = model.length;
					sy = model[0].length;
					sz = model[0][0].length;
					// This variable is used to prevent collisions with the decor when the model as different width or depth
					decal = -3;
				}

				/**
				 * Collision test explanations
				 * 
				 * On Y axis
				 * if entity moves down, test is done on blocks that are the lowest on Y in the entity and goes up while a block collide
				 * if entity moves up, test does the same but in the other direction 
				 * 
				 * On X axis
				 * The principle is the same as for the Y axis, with two differences :
				 *  - a entity can climb few blocks (STEP_HEIGHT)
				 *  - a collision occurs if the hole in the wall is not large enough (test delta between ceil and floor)
				 */
				
				// Collisions variables
				boolean collide = false;
				int cx = -1;
				int cy = -1;
				int cz = -1;

				entity[TOUCH_GROUND] = 0;

				// Collision test on Y axis
				// Down
				if (dy <= 0) {
					for (j = 0; j >= (dy >> 8) && !collide; j--) {
						int j2 = y + j - 1;
						for (i = x + sx - 1; i >= x && !collide; i--) {
							for (k = z + sz - 1; k >= z && !collide; k--) {
								int blockType = box[i][j2][k] >> BLOCK_TYPE_SHIFT;
								if (blockType > BLOCK_EPHEMERAL && blockType != BLOCK_ENTITY) {
									entity[TOUCH_GROUND] = 1;
									cx = i;
									cy = j2;
									cz = k;
									j++;
									collide = true;
								}
							}
						}
					}
				} else {
					// Up
					for (j = 0; j <= (dy >> 8) && !collide; j++) {
						int j2 = y + j + sy;
						for (i = x + sx - 1; i >= x && !collide; i--) {
							for (k = z + sz - 1; k >= z && !collide; k--) {
								if (j2 >= WORLD_SIZE_Y - 1 || (box[i][j2][k] >> BLOCK_TYPE_SHIFT) > BLOCK_EPHEMERAL) {
									cx = i;
									cy = j2;
									cz = k;
									j--;
									collide = true;
								}
							}
						}
					}
				}

				if (collide) {
					if (entityType == ENTITY_BLOCK_TYPE) {
						entity[DIRY] *= -1;
					}
					dy = j<<8;
				}

				// Save new Y position
				entity[Y] += dy;
				y = entity[Y] >> 8;

				if (gameState == PLAYING || entityType > ENTITY_OBJECT_TYPE) {
					// Collision test on X axis
					if (dx != 0) {
						collide = false;

						// Determine the X direction
						int x1 = decal;
						int x2 = (dx >> 8) + 1;
						int step = 1;

						if (dx < 0) {
							x1 = 0;
							x2 = (dx >> 8) - 1;
							step = -1;
						}

						// Loop through the X distance to move
						for (i = x1; i != x2 && !collide; i += step) {
							int i2 = 0;
							if (step < 0) {
								i2 = x + i - 1;
								if (i2 <= 0) {
									cx = i2;
									collide = true;
									break;
								}
							} else {
								i2 = x + sx + i;
								if (i2 <= 0)
									continue;
								if (i2 >= map_size_x + 1) {
									cx = i2;
									collide = true;
									break;
								}
							}
							int yFloor = y - 1;
							int yCeil = 100;
							int blockType = 0;

							for (j = y + sy * 2 - 1; j >= y; j--) {
								if (j < WORLD_SIZE_Y) {
									for (k = z + sz - 1; k >= z; k--) {
										blockType = box[i2][j][k] >> BLOCK_TYPE_SHIFT;
										if (blockType > BLOCK_EPHEMERAL) {
											if (blockType == BLOCK_DECOR) {
												if (j >= y + sy) {
													yCeil = j;
													continue;
												} else {
													yFloor = j;
													j = 0;
													break;
												}
											} else {
												yFloor = j;
												yCeil = 100;
												j = 0;
												break;
											}
										}
									}
								} else {
									yCeil = j;
								}
							}
							if (blockType > 0) {
								if (blockType == BLOCK_DECOR && entityType < ENTITY_BULLET_TYPE && yFloor <= y + STEP_HEIGHT && yCeil >= yFloor + sy) {
									entity[Y] = (y = yFloor + 1)<<8;
								} else {
									cx = i2;
									cy = yFloor;
									cz = k;
									i -= step;
									collide = true;
								}
							}
						}

						if (collide) {
							if (entityType == ENTITY_BLOCK_TYPE) {
								entity[DIRX] *= -1;
							}
							dx = i<<8;
						}

						// Save new X position
						entity[X] += dx;
						x = entity[X] >> 8;
					}

					// Collision test on Z axis
					if (dz != 0) {
						collide = false;

						int z1 = decal;
						int z2 = (dz >> 8) + 1;
						int step = 1;

						if (dz < 0) {
							z1 = 0;
							z2 = (dz >> 8) - 1;
							step = -1;
						}

						// Loop through the Z distance to move
						for (k = z1; k != z2 && !collide; k += step) {
							int k2 = 0;

							if (step < 0) {
								k2 = z + k - 1;
								if (k2 <= 0) {
									cz = k2;
									collide = true;
									break;
								}
							} else {
								k2 = z + sz + k;
								if (k2 <= 0)
									continue;
								if (k2 >= map_size_z + 1) {
									cz = k2;
									collide = true;
									break;
								}
							}

							int yFloor = y - 1;
							int yCeil = 100;
							int blockType = 0;

							for (j = y + sy * 2 - 1; j >= y; j--) {
								if (j < WORLD_SIZE_Y) {
									for (i = x + sx - 1; i >= x; i--) {
										blockType = box[i][j][k2] >> BLOCK_TYPE_SHIFT;
										if (blockType > BLOCK_EPHEMERAL) {
											if (blockType == BLOCK_DECOR) {
												if (j >= y + sy) {
													yCeil = j;
													continue;
												} else {
													yFloor = j;
													j = 0;
													break;
												}
											} else {
												yFloor = j;
												yCeil = 100;
												j = 0;
												break;
											}
										}
									}
								} else {
									yCeil = j;
								}
							}
							if (blockType > 0) {
								if (blockType == BLOCK_DECOR && entityType < ENTITY_BULLET_TYPE && yFloor <= y + STEP_HEIGHT && yCeil >= yFloor + sy) {
									entity[Y] = (y = yFloor + 1)<<8;
								} else {
									cx = i;
									cy = yFloor;
									cz = k2;
									k -= step;
									collide = true;
								}
							}
						}

						if (collide) {
							if (entityType == ENTITY_BLOCK_TYPE) {
								entity[DIRZ] *= -1;
							}
							dz = k<<8;
						}

						// Save new Z position
						entity[Z] += dz;
						z = entity[Z] >> 8;
					}

					// If there's a collision and the entity is not a block or a object
					if (entityType < ENTITY_OBJECT_TYPE) {
						if (cx > 0 && cz > 0) {

							// Collision with a another entity ?
							collide = false;
							for (l = entities.size() - 1; l >= 0 && !collide; l--) {
								int[] ent = entities.get(l);

								// If the other entity can collide with the reference entity
								if (ent[TYPE] < entityType || ent[TYPE] == ENTITY_OBJECT_TYPE) {
									byte[][][] model1 = models[ent[MODEL]][ent[ANGLE] >> 8];
									int sx1 = model1.length;
									int sy1 = model1[0].length;
									int sz1 = model1[0][0].length;

									int x1 = ent[X] >> 8;
									int y1 = ent[Y] >> 8;
									int z1 = ent[Z] >> 8;

									// If it's a collision with the player or a enemy
									if (cx >= x1 && cx < x1 + sx1 && cy >= y1 && cy < y1 + sy1 && cz >= z1 && cz < z1 + sz1) {
										if (ent[TYPE] < ENTITY_BULLET_TYPE) {
											// If the other entity has life level of 1, it explodes
											if (ent[LIFE] == 1) {
												for (i = 0; i < sx1; i++) {
													for (j = 0; j < sy1; j++) {
														for (k = 0; k < sz1; k++) {
															byte block = model1[i][j][k];
															if (block > 0) {
																addEntity(
																		entities,
																		// TYPE
																		ENTITY_BLOCK_TYPE,
																		// X, Y, Z
																		x1 + i, y1 + j, z1 + k,
																		// DIRX, DIRY, DIRZ
																		entity[DIRX] >> 8, entity[DIRY] >> 8, entity[DIRZ] >> 8,
																		// MODEL, ANGLE,
																		(block & BLOCK_VALUE_BITMASK) + (BLOCK_EPHEMERAL << BLOCK_TYPE_SHIFT), 0,
																		// LIFE
																		1,
																		// FORCEX, FORCEY, FORCEZ
																		(i - (sx1 >> 1)) * 5, (8 + j - (sy1 >> 1)) * 5, (k - (sz1 >> 1)) * 5,
																		// ANIMATION_SPEED
																		0);
															}
														}
													}
												}
											}
											collide = true;

											// Apply a external force
											ent[LIFE]--;
											ent[FORCEX] = 50 * (ent[X] - entity[X]);
											ent[FORCEZ] = 50 * (ent[Z] - entity[Z]);
										}

										// Collision between player and a object
										if (entityType == ENTITY_PLAYER_TYPE) {
											if (ent[MODEL] == MODEL_HEART) {
												entity[LIFE]++;
											}
											if (ent[MODEL] == MODEL_COIN) {
												nbCoins--;
												if (nbCoins==0) {
													gameState = GENERATE_MAP;
													level++;
													if (level == 6) {
														gameState = YOU_WIN;
													}
												}
											}
											ent[LIFE] = 0;
										}
									}
								}
							}

							// Collision between a bullet and the decor
							if (entityType == ENTITY_BULLET_TYPE && !collide) {
								for (i = -EXPLOSION_RADIUS; i < EXPLOSION_RADIUS; i++) {
									int x1 = cx + i - 1;
									if (x1 > 0) {
										for (j = -EXPLOSION_RADIUS; j < EXPLOSION_RADIUS; j++) {
											int y1 = cy + j + 1;
											if (y1 > 1 && y1 < WORLD_SIZE_Y) {
												for (k = -EXPLOSION_RADIUS; k < EXPLOSION_RADIUS; k++) {
													int z1 = cz + k - 1;
													if (z1 > 0) {
														byte block = box[x1][y1][z1];
														if (block > 0 && (block >> BLOCK_TYPE_SHIFT <= BLOCK_DECOR)) {
															int d = (i * i) + (j * j) + (k * k);
															if (d < EXPLOSION_RADIUS * EXPLOSION_RADIUS - 1) {
																if (d > 90*EXPLOSION_RADIUS * EXPLOSION_RADIUS / 100) {
																	addEntity(
																			entities,
																			// TYPE
																			ENTITY_BLOCK_TYPE,
																			// X, Y, Z
																			x1, y1, z1,
																			// DIRX, DIRY, DIRZ
																			EXPLOSION_RADIUS_FORCE * i, EXPLOSION_RADIUS_FORCE * j, EXPLOSION_RADIUS_FORCE * k,
																			// MODEL, ANGLE
																			(BLOCK_EPHEMERAL << BLOCK_TYPE_SHIFT) + (block & BLOCK_VALUE_BITMASK), 0,
																			// LIFE
																			1,
																			// FORCEX, FORCEY, FORCEZ
																			0, 0, 0,
																			// ANIMATION_SPEED
																			0);
																}
																box[x1][y1][z1] = 0;
															}
														}
													}
												}
											}
										}
									}
								}
							}

						}
						// If bullet collision outside the voxelbox, the bullet dies
						if ((cx != -1 || cz != -1) && entityType == ENTITY_BULLET_TYPE) {
							entity[LIFE] = 0;
						}
					}
				}

				// Draw the entity into the voxelbox if its life > 0
				if (entity[LIFE] > 0) {
					// Not block entity
					if (entityType < ENTITY_BLOCK_TYPE) {
						for (i = 0; i < sx; i++) {
							for (j = 0; j < sy; j++) {
								for (k = 0; k < sz; k++) {
									if (box[x + i][y + j][z + k] == 0) {
										box[x + i][y + j][z + k] = model[i][j][k];
									}
								}
							}
						}
					} else {
					// Block entity
						if (entity[TOUCH_GROUND] == 1 && ((entity[DIRX] * entity[DIRX] + entity[DIRY] * entity[DIRY] + entity[DIRZ] * entity[DIRZ] < MIN_MOVING_DISTANCE))) {
							entity[LIFE] = 0;
						}
						if (box[x][y][z] >> BLOCK_TYPE_SHIFT == 0) {
							byte block = (byte) entity[MODEL];
							if (entity[LIFE] == 0)
								block = (byte) ((BLOCK_DECOR << BLOCK_TYPE_SHIFT) + block & BLOCK_VALUE_BITMASK);

							box[x][y][z] = block;
						}
					}

					// Launch a bullet in the direction of the entity if Firing is true
					if (firing && now > nextBullet) {
						j = (entity[ANGLE] >> 8) & 3;
						i = 0;
						k = 0;
						if (j == RIGHT_ANGLE) {
							i = 1;
						}
						if (j == LEFT_ANGLE) {
							i = -1;
						}
						if (j == BACK_ANGLE) {
							k = 1;
						}
						if (j == FRONT_ANGLE) {
							k = -1;
						}

						addEntity(
								entities,
								// TYPE
								ENTITY_BULLET_TYPE,
								// X, Y, Z
								x + (1 + i) * 4, y + 3, z + (1 + k) * 4,
								// DIRX, DIRY, DIRZ
								i * BULLET_SPEED, 0, k * BULLET_SPEED,
								// MODEL, ANGLE
								1, j,
								// LIFE
								1,
								// FORCEX, FORCEY, FORCEZ
								0, 0, 0,
								// ANIMATION_SPEED
								10);
						nextBullet = now + 320000000;
					}

				}

				// If entity dies
				if (entity[LIFE] <= 0) {
					// If entity is a enemy
					if (entityType == ENTITY_ENENY_TYPE) {
						// randomly pop a heart
						if (((int) now & 127) > 115) {
							addEntity(
									entities,
									// TYPE
									ENTITY_OBJECT_TYPE,
									// X, Y, Z
									x, y, z,
									// DIRX, DIRY, DIRZ
									0, 0, 0,
									// MODEL, ANGLE
									MODEL_HEART, 0,
									// LIFE
									1,
									// FORCEX, FORCEY, FORCEZ
									0, 48, 0,
									// ANIMATION_SPEED
									0);
						}
						nbEnemies--;
						// Increase light strength
						lightStrength += 2;
						if (lightStrength >= LIGHT_DISTANCE) lightStrength = LIGHT_DISTANCE-1;
					}
					entities.remove(entity);
				}

				// Store player attributes
				if (entityType == ENTITY_PLAYER_TYPE) {
					playerLife = entity[LIFE];

					playerX = x + 5;
					playerZ = z + 6;
				}
			}

			// Determine frustum position relatively to player
			frustumX = playerX;
			frustumZ = playerZ;
			if (frustumX <= FRUSTUM_SIZE) {
				frustumX = FRUSTUM_SIZE + 1;
			}
			if (frustumX >= map_size_x - FRUSTUM_SIZE) {
				frustumX = map_size_x - FRUSTUM_SIZE + 1;
			}
			if (frustumZ <= FRUSTUM_SIZE) {
				frustumZ = FRUSTUM_SIZE + 1;
			}
			if (frustumZ >= map_size_z - FRUSTUM_SIZE) {
				frustumZ = map_size_z - FRUSTUM_SIZE + 1;
			}

			
			// Pop enemy if needed
			if (nbEnemies < nbEnemiesToPop) {
				i = MODEL_BUZZ_ENEMY + ((int) now & 1);
				j = 2;
				k = 20;
				if (i == MODEL_BLOB_ENEMY) {
					j = 1;
					k = 10;
				}
				int x = (int) (Math.random() * mazeWidth);
				int z = (int) (Math.random() * mazeHeight);
				if ((x != (int) (playerX / CELL_SIZE) || z != (int) (playerZ / CELL_SIZE)) && maze[x][z] != 0) {
					addEntity(
							entities,
							// TYPE
							ENTITY_ENENY_TYPE,
							// X, Y, Z
							x * CELL_SIZE + (CELL_SIZE >> 1), 20, z * CELL_SIZE + (CELL_SIZE >> 1),
							// DIRX, DIRY, DIRZ
							0, 0, -1,
							// MODEL, ANGLE
							i, 0,
							// LIFE
							j,
							// FORCEX, FORCEY, FORCEZ
							0, 0, 0,
							// ANIMATION_SPEED
							k);
					nbEnemies++;
				}
			}
			
			// Verify if the player looses
			if (playerLife <= 0 || lightStrength <= 14) {
				playerLife = 0;
				gameState = YOU_LOOSE;
			}

			// Rendering by column
			for (int x = (FRUSTUM_SIZE << 1) - 1; x >= 0; x--) {
				// List of blocks for this column
				ArrayList<Integer> blocksList = new ArrayList<Integer>();

				// Loop from down to maximum visible blocks (frustumSizeZ + frustumSizeY)
				for (i = 1; i < WORLD_SIZE_Y + (FRUSTUM_SIZE << 1); i++) {
					// Z block position
					int z = i - WORLD_SIZE_Y;
					if (z < 1) {
						z = 1;
					}
					// Y block position
					int y = i - z - 2;
					// First block is at the bottom of the frustum (near us)
					z += frustumZ - FRUSTUM_SIZE;
					// Loop through the blocks on the same view ray
					for (; z < frustumZ + FRUSTUM_SIZE && y > 0; z++, y--) {
						byte block = box[x + frustumX - FRUSTUM_SIZE][y][z];
						// Stop when a block is not empty
						if (block > 0) {
							// (z << 6) - y) used to sort the blocks by Z and Y position
							blocksList.add((((z << 6) - y) << 12) + (i << 4) + (block & BLOCK_VALUE_BITMASK));
							break;
						}
					}
				}

				// Sort the visible blocks list by the Z and Y position => orderer by Z rendering position
				Collections.sort(blocksList);

				// Loop through the visible blocks order by Y rendering position
				for (j = blocksList.size() - 1; j >= 0; j--) {
					int value = blocksList.get(j);
					// block color index
					int color = (value & BLOCK_VALUE_BITMASK) * LIGHT_DISTANCE;

					// Initialise distance to max value
					int z = LIGHT_DISTANCE - 1;

					// LightSource position in voxelBox
					int x1 = playerX << 7;
					int y1 = 4096;	// 32 << 7
					int z1 = playerZ << 7;

					// Block position in voxelBox
					int x2 = x + frustumX - FRUSTUM_SIZE;
					int y2 = -(value >> 12) & 0x3F;
					int z2 = (value >> 18);

					// Ray from lightSource to block
					int dx = (x2 << 7) - x1;
					int dy = (y2 << 7) - y1;
					int dz = (z2 << 7) - z1;

					// Accumulate shadow produced by neighbours
					int pcf = 0;

					// Distance between lightSource and block
					int len = dx * dx + dy * dy + dz * dz;
					
					// If block is in light
					if (len < (lightStrength*lightStrength) << 14) {

						// Normalise ray
						len = ((int) Math.sqrt(len) >> 7) + 1;
						dx /= len;
						dy /= len;
						dz /= len;

						// Make the start point in the middle of the cell
						x1 += 64;
						y1 += 128;	// One cell above for artifacts
						z1 += 64;

						// Initialise the distance to 0
						z = 0;

						// Loop on the ray till distance over or block hit
						while (z < len - 1) {
							int y3 = y1 >> 7;
							if (y3 < WORLD_SIZE_Y - 1) {
								int x3 = x1 >> 7;
								int z3 = z1 >> 7;
								if (box[x3][y3][z3] != 0) {
									z = LIGHT_DISTANCE - 1;
								}
								// Approximate PCF shadow (not very conventional, but result is not too bad)
								if (box[x3 - 1][y3][z3] != 0) pcf += PCF_SHADOW;
								if (box[x3 + 1][y3][z3] != 0) pcf += PCF_SHADOW;
								if (box[x3][y3][z3 - 1] != 0) pcf += PCF_SHADOW;
								if (box[x3][y3][z3 + 1] != 0) pcf += PCF_SHADOW;
							}

							x1 += dx;
							y1 += dy;
							z1 += dz;
							z++;
						}
						
						// Calculate final lighting
						z += LIGHT_DISTANCE - lightStrength + pcf;
	
						// Approximate ambient occlusion
						y2++;
						if (box[x2][y2][z2] == 0) {
							if (box[x2 - 1][y2][z2] != 0) z += PCF_SHADOW;
							if (box[x2][y2][z2 - 1] != 0) z += PCF_SHADOW;
							if (box[x2][y2][z2 + 1] != 0) z += PCF_SHADOW;
							if (box[x2 + 1][y2][z2] != 0) z += PCF_SHADOW;
						}
						
						// Fit distance in authorised range
						if (z>=LIGHT_DISTANCE) z = LIGHT_DISTANCE-1;
						
						// Calculate front face lighting
						int fz = (z + LIGHT_DISTANCE) >> 1;
	
						// Draw the block
						l = x * BLOCK_X + (SCREEN_HEIGHT - ((value >> 4) & 0xFF) * BLOCK_Y) * SCREEN_WIDTH;
						for (i = l + BLOCK_X - 1; i >= l; i--) {
							pixels[i] =
							pixels[i + SCREEN_WIDTH] =
							pixels[i + 2 * SCREEN_WIDTH] = colors[color + z];
							pixels[i + 3 * SCREEN_WIDTH] =
							pixels[i + 4 * SCREEN_WIDTH] =
							pixels[i + 5 * SCREEN_WIDTH] = colors[color + fz];
						}
					}
				}
			}

			// UI
			gfx.setColor(Color.green);

			// Player life
			gfx.fillRect(10, 10, 14 * playerLife, 10);

			gfx.setColor(Color.white);
			
			// Player light
			gfx.fillRect(10, 30, lightStrength-14, 10);
			
			// Coins left
			gfx.drawString("Coins x", 606, 20);
			gfx.drawString(String.valueOf(nbCoins), 650, 20);

			// Messages and gameState
			if (DEBUG) {
				gfx.drawString("FPS : " + String.valueOf(fps), 10, 60);
				gfx.drawString("Entites : " + String.valueOf(entities.size()), 10, 80);
				gfx.drawString("Enemies : " + String.valueOf(nbEnemies), 10, 100);
			}

			if (gameState > PLAYING) {

				gfx.clearRect(0, 278, SCREEN_WIDTH, 50);

				if (gameState != YOU_WIN) {
					gfx.drawString("ENTER to go", 300, 315);
					if (keys[KEY_ENTER]) {
						gameState = PLAYING;
						if (playerLife <= 0) {
							gameState = GENERATE_MAP;
						}
					}
				}
				gfx.setColor(Color.orange);
				if (gameState == PAUSE) {
					gfx.drawString("Pause", 318, 296);
				}
				if (gameState == YOU_WIN) {
					gfx.drawString("CONGRATULATIONS !", 275, 308);
				}
				if (gameState == YOU_LOOSE) {
					gfx.drawString("You died", 310, 296);
				}
				if (gameState == NEXT_LEVEL) {
					gfx.drawString("Level", 315, 296);
					gfx.drawString(String.valueOf(level), 350, 296);
				}
			}

			if (appletGraphics == null)
				appletGraphics = getGraphics();
			else
				appletGraphics.drawImage(screen, 0, 0, null);

		}
	}

	@Override
	public boolean handleEvent(Event e) {
		return keys[e.key] = (e.id == Event.KEY_PRESS || e.id == Event.KEY_ACTION);
	}
}