package java4k.mysterymash;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Mystery Mash-up game.
 *
 * Starts as Pong, then adds elements from these games:
 *  * Pong
 *       http://www.youtube.com/watch?v=LPkUvfL8T1I
 *  * Break-Out
 *       Atari:
 *       http://www.youtube.com/watch?v=Up-a5x3coC0
 *       Arcade:
 *       http://www.youtube.com/watch?v=IpVIufJ4qoU
 *  * Space Invaders
 *       http://www.youtube.com/watch?v=437Ld_rKM2s
 *  * Tetris
 *  * Cave shooter (walls move in - what's the right game for a tunnel dodging
 *      game; is this the right game name for this play element?)
 *      Scramble
 *      http://www.youtube.com/watch?v=kjIbk1Lpr4I
 *      Vanguard
 *      http://www.youtube.com/watch?v=-Kf-2Z0wBlc
 *  * Missile Command
 *      http://www.youtube.com/watch?v=1uAzPCtoEGY
 *  * Maybe Asteroids
 *  * Maybe Galaga (sucks up your paddle; Tetris already fulfils this
 *    gameplay mechanic)
 *
 * Pong: Requires ball, collision detection, AI for opponent
 * Break-Out: Requires add and remove item logic
 * Space Invaders: Requires image of alien, or definition of "blocks"
 *    layout. Requires Alien movement logic
 * Asteroids: either requires line-drawing or image of asteroid, and
 *    asteroid movement.
 * Tetris: requires stacking logic and shape maintenance.
 * Missile Command: requires line drawing and circles, and mouse position for
 *    clicks?  Explosion would destroy destroyable objects.
 *
 *
 * SIZEDO
 *   Constructing game:
 *      * See fixme sections
 *   Final cleanup:
 *      * Switch to String-packed data.
 *      * Change calculations in constants to a constant value.
 *
 * @author Matt Albrecht
 */
public class M extends Applet implements Runnable {
	/**
	 * SIZEDO
	 * Load the OBJECT_TYPE array.  For now, hard code the values.  As this
	 * gets closer to being finished, replace the invocation of this function
	 * with a string decoding structure.
	 */
	private static final void loadTypeArray(int[] obj) {

		//private static final int OBJECT_TYPE_WALL_INDEX = 0;
		// WALL does not have initialization data.  It will be specially
		// configured on its own.  Setup with the Array class should skip
		// this record.  Because it is populated with zeros, we need to
		// be careful with initialization.
		// Because it will be "hard-coded" to be the first set of objects in
		// the array, but it won't actually be initialized with the rest,
		// special care needs to be done to make sure its image pointers
		// are correctly setup.

		// Breakout & invaders cause dents?  No.  It would mean
		// having a TPADDLE with lines cut through it and not fall.
		// That may be an acceptable "bug", because if the invader shoots,
		// it can create the same cut-through line if the invader is in the
		// middle of the TPADDLE.

		//private static final int OBJECT_TYPE_BREAKOUT_INDEX = 2;
		int p = OBJECT_TYPE_BREAKOUT_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_DESTROYABLE | FLAGMSK_STOPS_TETRIS | FLAGMSK_REFLECTS_BALL;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_BREAKOUT_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = BREAKOUT_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = BREAKOUT_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = BREAKOUT_BLOCK_V_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = BREAKOUT_BLOCK_H_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = BREAKOUT_BLOCK_V_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = BREAKOUT_BLOCK_H_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = BREAKOUT_BLOCK_V_SPACING + BREAKOUT_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = BREAKOUT_BLOCK_H_SPACING + BREAKOUT_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX] = 0xff;
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0xff;

		//    private static final int OBJECT_TYPE_INVADER1_INDEX = 3;
		p = OBJECT_TYPE_INVADER1_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_DESTROYABLE | FLAGMSK_STOPS_TETRIS | FLAGMSK_REFLECTS_BALL | FLAGMSK_CAUSES_DENTS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_INVADER1_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = INVADER_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = INVADER_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = INVADER1_BLOCK_V_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = INVADER_BLOCK_H_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = INVADER1_BLOCK_V_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = INVADER1_BLOCK_H_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = INVADER_BLOCK_V_SPACING;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = INVADER_BLOCK_H_SPACING;

		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x69; // 01101001
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x6d; // 01101101

		// .xx.   // 0110
		// xx.x   // 1101
		// .xx.   // 0110
		// x..x   // 1001

		//    private static final int OBJECT_TYPE_INVADER2_INDEX = 4;
		p = OBJECT_TYPE_INVADER2_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_DESTROYABLE | FLAGMSK_STOPS_TETRIS | FLAGMSK_REFLECTS_BALL;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_INVADER2_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = INVADER_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = INVADER_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = INVADER2_BLOCK_V_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = INVADER_BLOCK_H_COUNT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = INVADER2_BLOCK_V_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = INVADER2_BLOCK_H_OFFSET;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = INVADER_BLOCK_V_SPACING;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = INVADER_BLOCK_H_SPACING;

		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x66; // 01100110
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x6b; // 01101011

		// .xx.   // 0110
		// x.xx   // 1011
		// .xx.   // 0110
		// .xx.   // 0110

		//private static final int OBJECT_TYPE_NPADDLE_INDEX = 5;
		p = OBJECT_TYPE_NPADDLE_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_REFLECTS_BALL;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_NPADDLE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = NPADDLE_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = NPADDLE_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = NPADDLE_BLOCK_Y;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = NPADDLE_BLOCK_STARTX;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX] = 0xff;

		//private static final int OBJECT_TYPE_SPADDLE_INDEX = 6;
		p = OBJECT_TYPE_SPADDLE_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_REFLECTS_BALL | FLAGMSK_DENTABLE | FLAGMSK_STOPS_TETRIS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_SPADDLE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = SPADDLE_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = SPADDLE_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = SPADDLE_BLOCK_Y;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = SPADDLE_BLOCK_START_X;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX] = 0xff;

		//private static final int OBJECT_TYPE_BALL_INDEX = 7;
		p = OBJECT_TYPE_BALL_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = FLAGMSK_CAUSES_DENTS | FLAGMSK_CAUSES_DESTROY;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_BALL_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = BALL_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = BALL_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 1;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = BALL_BLOCK_START_Y;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = BALL_BLOCK_START_X;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX] = 0x1;

		//private static final int OBJECT_TYPE_ENEMYFIRE_INDEX = 14;
		p = OBJECT_TYPE_ENEMYFIRE_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = ENEMYFIRE_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_ENEMYFIRE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = ENEMYFIRE_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = ENEMYFIRE_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX] = 0x3;

		//private static final int OBJECT_TYPE_TETRIS_LHOOK_INDEX = 15;
		p = OBJECT_TYPE_TETRIS_LHOOK_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = TETRIS_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_TETRIS_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = TETRIS_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = TETRIS_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		//    ........  // 00000000
		//    ........  // 00000000
		//    ......XX  // 11000000
		//    ......XX  // 11000000
		//    ......XX  // 11000000
		//    ......XX  // 11000000
		//    ....XXXX  // 11110000
		//    ....XXXX  // 11110000

		//private static final int OBJECT_TYPE_TETRIS_RHOOK_INDEX = 16;
		p = OBJECT_TYPE_TETRIS_RHOOK_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = TETRIS_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_TETRIS_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = TETRIS_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = TETRIS_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		//    //    ........   00000000
		//    //    ........   00000000
		//    //    XX......   00000011
		//    //    XX......   00000011
		//    //    XX......   00000011
		//    //    XX......   00000011
		//    //    XXXX....   00001111
		//    //    XXXX....   00001111

		//private static final int OBJECT_TYPE_TETRIS_BAR_INDEX = 17;
		p = OBJECT_TYPE_TETRIS_BAR_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = TETRIS_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_TETRIS_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = TETRIS_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = TETRIS_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0xff; // 11111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0xff; // 11111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    XXXXXXXX  11111111
		//    //    XXXXXXXX  11111111

		//private static final int OBJECT_TYPE_TETRIS_BOX_INDEX = 18;
		p = OBJECT_TYPE_TETRIS_BOX_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = TETRIS_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_TETRIS_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = TETRIS_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = TETRIS_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    ........  00000000
		//    //    XXXX....  00001111
		//    //    XXXX....  00001111
		//    //    XXXX....  00001111
		//    //    XXXX....  00001111

		//private static final int OBJECT_TYPE_TETRIS_BUMP_INDEX = 19;
		p = OBJECT_TYPE_TETRIS_BUMP_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = TETRIS_FLAGS; // never actually used - can be anything
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_TETRIS_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = TETRIS_BLOCK_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = TETRIS_BLOCK_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x0c; // 00001100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x0c; // 00001100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		//    // ........  00000000
		//    // ........  00000000
		//    // ........  00000000
		//    // ........  00000000
		//    // ..xx....  00001100
		//    // ..xx....  00001100
		//    // xxxxxx..  00111111
		//    // xxxxxx..  00111111

		//private static final int OBJECT_TYPE_MISSILE_HEAD_INDEX = 12;
		p = OBJECT_TYPE_MISSILE_HEAD_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = MISSILE_HEAD_FLAGS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_MISSILE_HEAD_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = MISSILE_HEAD_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = MISSILE_HEAD_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x01; // 00000001

		//private static final int OBJECT_TYPE_MISSILE_EXPLODE_1_INDEX = 8;
		p = OBJECT_TYPE_MISSILE_EXPLODE_1_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = MISSILE_EXPLODE_FLAGS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_MISSILE_EXPLODE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = MISSILE_EXPLODE_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = MISSILE_EXPLODE_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 8] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 9] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 10] = 0xe0; // 11100000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 11] = 0x07; // 00000111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 12] = 0xe0; // 11100000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 13] = 0x07; // 00000111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 14] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 15] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 16] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 17] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 18] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 19] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 20] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 21] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 22] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 23] = 0x00; // 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ......xxxx...... 00000011 11000000
		// .....xxxxxx..... 00000111 11100000
		// .....xxxxxx..... 00000111 11100000
		// ......xxxx...... 00000011 11000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000

		//private static final int OBJECT_TYPE_MISSILE_EXPLODE_2_INDEX = 8;
		p = OBJECT_TYPE_MISSILE_EXPLODE_2_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = MISSILE_EXPLODE_FLAGS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_MISSILE_EXPLODE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = MISSILE_EXPLODE_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = MISSILE_EXPLODE_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 8] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 9] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 10] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 11] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 12] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 13] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 14] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 15] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 16] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 17] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 18] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 19] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 20] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 21] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 22] = 0x00; // 00000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 23] = 0x00; // 00000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000
		// ......xxxx...... 00000011 11000000
		// ....xxxxxxxx.... 00001111 11110000
		// ...xxxxxxxxxx... 00011111 11111000
		// ...xxxxxxxxxx... 00011111 11111000
		// ...xxxxxxxxxx... 00011111 11111000
		// ...xxxxxxxxxx... 00011111 11111000
		// ....xxxxxxxx.... 00001111 11110000
		// ......xxxx...... 00000011 11000000
		// ................ 00000000 00000000
		// ................ 00000000 00000000

		//private static final int OBJECT_TYPE_MISSILE_EXPLODE_3_INDEX = 8;
		p = OBJECT_TYPE_MISSILE_EXPLODE_3_POS;
		obj[p + OBJECT_TYPE_RECORD_FLAG_INDEX] = MISSILE_EXPLODE_FLAGS;
		obj[p + OBJECT_TYPE_RECORD_COLOR_INDEX] = PALETTE_MISSILE_EXPLODE_POS;
		obj[p + OBJECT_TYPE_RECORD_WIDTH_INDEX] = MISSILE_EXPLODE_WIDTH;
		obj[p + OBJECT_TYPE_RECORD_HEIGHT_INDEX] = MISSILE_EXPLODE_HEIGHT;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX] = 0;
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX] = 0; // garbage value - can be anything
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 0] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 1] = 0x03; // 00000011
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 2] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 3] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 4] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 5] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 6] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 7] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 8] = 0xfc; // 11111100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 9] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 10] = 0xfc; // 11111100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 11] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 12] = 0xfc; // 11111100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 13] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 14] = 0xfc; // 11111100
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 15] = 0x3f; // 00111111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 16] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 17] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 18] = 0xf8; // 11111000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 19] = 0x1f; // 00011111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 20] = 0xf0; // 11110000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 21] = 0x0f; // 00001111
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 22] = 0xc0; // 11000000
		obj[p + OBJECT_TYPE_RECORD_IMAGE_INDEX + 23] = 0x03; // 00000011
		// ......xxxx...... 00000011 11000000
		// ....xxxxxxxx.... 00001111 11110000
		// ...xxxxxxxxxx... 00011111 11111000
		// ...xxxxxxxxxx... 00011111 11111000
		// ..xxxxxxxxxxxx.. 00111111 11111100
		// ..xxxxxxxxxxxx.. 00111111 11111100
		// ..xxxxxxxxxxxx.. 00111111 11111100
		// ..xxxxxxxxxxxx.. 00111111 11111100
		// ...xxxxxxxxxx... 00011111 11111000
		// ...xxxxxxxxxx... 00011111 11111000
		// ....xxxxxxxx.... 00001111 11110000
		// ......xxxx...... 00000011 11000000

		/*
		    // Is this object destroyable by a destroy-cause object?
		    private static final int FLAGMSK_DESTROYABLE = 0x0001;
		    
		    // If the object collides with an object, does it remove part of its
		    // image?
		    private static final int FLAGMSK_CAUSES_DENTS = 0x0002;
		    
		    // If the object collides with the ball, does it dent the object?
		    private static final int FLAGMSK_CAUSES_DESTROY = 0x0004;
		    
		    // If the object collides with a dent-cause object, does it dent the object?
		    private static final int FLAGMSK_DENTABLE = 0x0008;
		    
		    // Does the object fall until it lands on top of an object?
		    private static final int FLAGMSK_REFLECTS_BALL = 0x0010;
		    
		    // Does the object stop falling objects?  If this is set, it also
		    // reflects the ball.
		    private static final int FLAGMSK_STOPS_TETRIS = 0x0020;
		    
		    // Is this one of the tetris objects?
		    private static final int FLAGMSK_IS_TETRIS = 0x0040;
		*/

	}

	private int mousePos;
	// Encoded as (x | (y <<< 9)), and is in display resolution, not
	// render resolution.
	private boolean mouseClick = false;

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		while (!isActive()) {
			Thread.yield();
		}

		// Define all variables
		int x1 = 0, x2, x3, x4, x5, x6, x7;
		int y1 = 0, y2 = 0, y3 = 0, y4, y5, y6, y7;
		int p0, p1, p2, p3, p4, p5, p6;
		boolean b1, b2, b3, b4;

		// Initialize Constants

		BufferedImage image = new BufferedImage(RENDER_WIDTH, RENDER_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics ogr = image.getGraphics();

		// definition about each object type
		int[] OBJECT_TYPES = new int[OBJECT_TYPE_SIZE];
		// SIZEDO replace this hard-coded setting with loading from a string.
		loadTypeArray(OBJECT_TYPES);

		// Extra initialization needed outside the string loading
		// It's needed because the values are out-of-range, in addition
		// to these types being skipped in the string setup.
		OBJECT_TYPES[WALL_TYPE_HEIGHT_POS] = WALL_BLOCK_HEIGHT;
		OBJECT_TYPES[WALL_TYPE_WIDTH_POS] = WALL_IMAGE_BLOCK_WIDTH;

		// Load the color values
		Color[] COLORS = new Color[PALETTE_COUNT];

		// x1 ALLOCATE
		// y1 ALLOCATE
		// y2 ALLOCATE
		// y3 ALLOCATE
		/*x1 = 0;*/
		/*y1 = 0;*/
		/*y2 = 0;*/
		/*y3 = 0;*/
		while (true) {
			COLORS[x1++] = new Color(y1 << PALETTE_COMPONENT_SHL, y2 << PALETTE_COMPONENT_SHL, y3 << PALETTE_COMPONENT_SHL);
			if (++y1 >= PALETTE_COMPONENT_MAX) {
				y1 = 0;
				if (++y2 >= PALETTE_COMPONENT_MAX) {
					y2 = 0;
					if (++y3 >= PALETTE_COMPONENT_MAX) {
						break;
					}
				}
			}
		}
		// x1 FREE
		// y1 FREE
		// y2 FREE
		// y3 FREE

		// ================================================================
		// ================================================================
		// initialize game

		int playerScore = 0;
		int computerScore = 0;
		int playerScoreColor = PALETTE_SCORE_NEUTRAL_POS;
		int computerScoreColor = PALETTE_SCORE_NEUTRAL_POS;

		startLife: while (true) {
			// ============================================================
			// ============================================================
			// initialize level
			long lastTime = System.nanoTime();
			int frameCount = 0;
			int reflectCount = 0;
			int wallCount = 0;
			int wallLeft = MIN_WALL_LEFT;
			int wallRight = MAX_WALL_RIGHT;

			boolean ballReflect = false;

			// sticky == attached to top of paddle, moves with the paddle,
			// and is released with a click
			boolean ballSticky = true;

			// ============================================================
			// Object instance list + the images for each instance.
			int[] OBJECT_INST = new int[OBJECT_INST_SIZE];

			// Setup initial objects:
			//   Use the prepopulate attributes on the type to construct
			//   the static objects.

			// p0 ALLOCATE
			// p1 ALLOCATE
			p0 = WALL_OBJECT_INST_START_POS;
			// p0 = object instance pointer
			p1 = OBJECT_INST_IMAGE_BASE_START_POS;
			// p1 = image instance pointer

			// ------------------------------------------------------------
			// Populate walls first.  They're a special case, and always
			// start at the beginning.
			// y1 ALLOCATE
			y1 = WALL_START_Y;
			// y1 = y position of current wall item
			// x1 ALLOCATE
			for (x1 = 0; x1 < WALL_TOTAL_COUNT; x1++) {
				// x1 = object instance
				OBJECT_INST[p0 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_YES;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_IMAGE_POS_INDEX] = p1;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_X_INDEX] = WALL_START_X;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_Y_INDEX] = y1;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_NOOP;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_FLAG_INDEX] = WALL_FLAGS;
				OBJECT_INST[p0 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_WALL_INDEX;

				// x2 ALLOCATE
				x2 = (x1 % WALL_PALETTE_DELTA) + PALETTE_WALL_START;
				OBJECT_INST[p1 + WALL_IMAGE_LBLOCK_INDEX] = x2;
				OBJECT_INST[p1 + WALL_IMAGE_RBLOCK_INDEX] = x2;
				// x2 FREE
				// Don't set this to 0 - it's already 0.
				//OBJECT_INST[p1 + WALL_IMAGE_DATA_LBREAK_IPOS] = 0;
				OBJECT_INST[p1 + WALL_IMAGE_DATA_RBREAK_IPOS] = RENDER_WIDTH;

				y1 -= WALL_SEGMENT_HEIGHT;
				p0 += OBJECT_INST_RECORD_SIZE;
				p1 += OBJECT_INST_IMAGE_SIZE;
			}

			// x1 FREE
			// y1 FREE

			// x4 ALLOC
			x4 = OBJECT_TYPE_BREAKOUT_POS;
			// x4 = OBJECT_TYPE position
			// x3 ALLOC
			for (x3 = OBJECT_TYPE_BREAKOUT_INDEX; x3 < OBJECT_TYPE_COUNT; x3++) {
				// x3 = OBJECT_TYPE index
				// y1 ALLOC
				y1 = OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX];
				// y1 = y position
				// y2 ALLOC
				for (y2 = 0; y2 < OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX]; y2++) {
					// y2 = V COUNT position
					// x1 ALLOC
					x1 = OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX];
					// x1 = x position
					// x2 ALLOC
					for (x2 = 0; x2 < OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX]; x2++) {
						// x2 = H COUNT position

						OBJECT_INST[p0 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_NO;
						OBJECT_INST[p0 + OBJECT_INST_RECORD_IMAGE_POS_INDEX] = p1;
						OBJECT_INST[p0 + OBJECT_INST_RECORD_X_INDEX] = x1 * GR_BLOCK_WIDTH;
						OBJECT_INST[p0 + OBJECT_INST_RECORD_Y_INDEX] = y1 * GR_BLOCK_HEIGHT;
						OBJECT_INST[p0 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
						OBJECT_INST[p0 + OBJECT_INST_RECORD_FLAG_INDEX] = OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_FLAG_INDEX];
						OBJECT_INST[p0 + OBJECT_INST_RECORD_TYPE_INDEX] = x3;

						x1 += OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX];
						p0 += OBJECT_INST_RECORD_SIZE;
						p1 += OBJECT_INST_IMAGE_SIZE;
					}
					// x2 FREE
					// x1 FREE
					// y2 FREE
					y1 += OBJECT_TYPES[x4 + OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX];
				}

				x4 += OBJECT_TYPE_RECORD_SIZE;
			}
			// y1 FREE
			// x3 FREE
			// x4 FREE

			// All the objects that need assignment are assigned.
			// Now we need to ensure that all unallocated object records
			// have a valid image pointer.
			// y1 ALLOC
			p0 += OBJECT_INST_RECORD_IMAGE_POS_INDEX;
			// p0 = pointer in OBJECT_INST to record's image position
			while (p0 < OBJECT_INST_IMAGE_START_POS) {
				OBJECT_INST[p0] = p1;

				p0 += OBJECT_INST_RECORD_SIZE;
				p1 += OBJECT_INST_IMAGE_SIZE;
			}
			// x1 ALLOC

			// p1 FREE
			// p0 FREE
			// set a default DX / DY motion for the ball.
			OBJECT_INST[OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS] + BALL_IMAGE_DATA_DX_IPOS] = BALL_START_DX;
			OBJECT_INST[OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS] + BALL_IMAGE_DATA_DY_IPOS] = BALL_START_DY;

			// special objects need to be visible at start
			OBJECT_INST[NPADDLE_OBJECT_INST_VISIBLE_POS] = VISIBLE_YES;
			OBJECT_INST[SPADDLE_OBJECT_INST_VISIBLE_POS] = VISIBLE_YES;
			OBJECT_INST[BALL_OBJECT_INST_VISIBLE_POS] = VISIBLE_YES;

			int objectCount = BALL_OBJECT_INST_START_INDEX + 1;

			loopGame: while (true) {
				Graphics sg = getGraphics();
				// ========================================================
				// ========================================================
				// Shared processing between started and not started games

				// Input check: mouse click is used by many different
				// areas.  To ensure that the click is registered, we
				// mark it as handled by setting it to false, and keeping
				// a loop-local variable to the original value.
				// This also means we can ignore mouse release actions,
				// an added bonus because then we won't miss mouse clicks.
				boolean mousePressed = this.mouseClick;
				this.mouseClick = false;
				if (mousePressed && frameCount > 0) {
					ballSticky = false;
					OBJECT_INST[BALL_OBJECT_INST_Y_POS] -= BALL_HEIGHT;
				}
				frameCount++;

				// ========================================================
				// Common rendering

				// --------------------------------------------------------
				// Fill the background
				ogr.setColor(COLORS[PALETTE_BACKGROUND_POS]);
				ogr.fillRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);

				// --------------------------------------------------------
				// Draw net
				// x1 ALLOCATE
				ogr.setColor(COLORS[PALETTE_NET_POS]);
				for (x1 = 0; x1 < RENDER_WIDTH; x1 += DR_NET_SEP) {
					ogr.fillRect(x1, DR_NET_Y, DR_NET_WIDTH, DR_NET_HEIGHT);
				}
				// x1 FREE

				// ====================================================
				// process next frame in the game

				// --------------------------------------------------------
				// Specialized logic for next "special" event to occur.

				if (ballReflect && (reflectCount & REFLECT_SPECIAL_BITS) == SPECIAL_COMPUTE_EQUALS) {
					ballReflect = false;
					// p2 ALLOC
					p2 = BASE_EVENT_MOD + playerScore + computerScore + (reflectCount >> REFLECT_SPECIAL_SHR);
					if (p2 > MAX_EVENT_MOD) {
						p2 = MAX_EVENT_MOD;
					}

					// y4 ALLOC
					y4 = 0;
					// y4 = increment across non-break case statements

					switch (frameCount % p2) {
					//case 0:
					// do nothing
					//    break;
					case 1:
						y4 += BREAKOUT_BLOCK_V_COUNT / 2;
					case 2:
						// ------------------------------------------------------------
						// Add line of breakout

						// x1 ALLOC
						x1 = BREAKOUT_OBJECT_INST_START_POS + ((frameCount >>> 3) % (y4 + (BREAKOUT_BLOCK_V_COUNT / 2))) * BREAKOUT_ROW_OBJECT_INST_SIZE + OBJECT_INST_RECORD_VISIBLE_INDEX;
						// x4 ALLOC
						for (x4 = 0; x4 < BREAKOUT_BLOCK_H_COUNT; x4++, x1 += OBJECT_INST_RECORD_SIZE) {
							OBJECT_INST[x1] = VISIBLE_YES;
						}
						// x4 FREE
						// x1 FREE

						break;
					case 3:
						// ------------------------------------------------------------
						// Add walls
						wallCount += WALL_ADD_COUNT_1;
					case 4:
						// ------------------------------------------------------------
						// Add more walls
						wallCount += WALL_ADD_COUNT_2;
						break;

					case 5:
						// -----------------------------------------------------------
						// Add tetris piece
						y4 = (frameCount >>> 3) % TETRIS_TYPE_COUNT;

						// x1 ALLOC
						x1 = objectCount * OBJECT_INST_RECORD_SIZE;
						// x1 = new object position
						objectCount++;
						// x4 ALLOC
						x4 = OBJECT_INST[OBJECT_INST[NPADDLE_OBJECT_INST_IMAGE_POS_POS] + NPADDLE_IMAGE_DATA_WALL_RIGHT_IPOS] - TETRIS_WIDTH
								- OBJECT_INST[OBJECT_INST[NPADDLE_OBJECT_INST_IMAGE_POS_POS] + NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS];
						// x4 = x position of the object, which must be between the
						//      walls
						x4 = (((frameCount * objectCount * reflectCount) >>> 3) % x4) + OBJECT_INST[OBJECT_INST[NPADDLE_OBJECT_INST_IMAGE_POS_POS] + NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS];
						OBJECT_INST[x1 + OBJECT_INST_RECORD_X_INDEX] = x4;
						// x4 FREE
						OBJECT_INST[x1 + OBJECT_INST_RECORD_Y_INDEX] = TETRIS_START_Y;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_FLAG_INDEX] = TETRIS_FLAGS;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_TETRIS_LHOOK_INDEX + y4;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_YES;
						// x1 FREE
						break;

					case 6:
						// -----------------------------------------------------------
						// Add a line of Invaders
						y4++;
					case 7:
						// -----------------------------------------------------------
						// Add a line of Invaders
						// x1 ALLOC
						x1 = INVADER_OBJECT_INST_START_POS + (y4 * INVADER_BLOCK_H_COUNT * OBJECT_INST_RECORD_SIZE) + OBJECT_INST_RECORD_VISIBLE_INDEX;
						// x1 = object inst pos for row to add
						// x4 ALLOC
						for (x4 = 0; x4 < INVADER_BLOCK_H_COUNT; x4++, x1 += OBJECT_INST_RECORD_SIZE) {
							OBJECT_INST[x1] = VISIBLE_YES;
						}
						// x4 FREE
						// x1 FREE
						break;
					case 8:
					case 9:
						// -----------------------------------------------------------
						// Add missile

						// x1 ALLOC
						x1 = objectCount * OBJECT_INST_RECORD_SIZE;
						// x1 = new object position
						objectCount++;

						OBJECT_INST[x1 + OBJECT_INST_RECORD_Y_INDEX] = MISSILE_START_Y;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_FLAG_INDEX] = MISSILE_HEAD_FLAGS;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_MISSILE_HEAD_INDEX;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_YES;

						// Set Current position
						// p0 ALLOC
						p0 = OBJECT_INST[x1 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
						//y1 ALLOC
						y1 = (((frameCount * objectCount * reflectCount) >>> 3) % MISSILE_START_X_COUNT) * MISSILE_START_X_MULT;
						OBJECT_INST[x1 + OBJECT_INST_RECORD_X_INDEX] = y1;
						// x1 FREE
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_PATH_START_X_IPOS] = y1;
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_PATH_START_Y_IPOS] = MISSILE_START_Y;
						y1 <<= MISSILE_MOVEMENT_ENCODE_SHL;

						OBJECT_INST[p0 + MISSILE_HEAD_DATA_X_IPOS] = y1;
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_Y_IPOS] = MISSILE_START_Y_ENCODED;

						// Calculate the path of the missile
						// x1 ALLOC
						x1 = ((((MISSILE_RANDOM_MULT * frameCount * objectCount) + reflectCount) % MISSILE_START_X_COUNT) * MISSILE_START_X_MULT) << MISSILE_MOVEMENT_ENCODE_SHL;
						if (y1 == x1) {
							x1++;
						}

						OBJECT_INST[p0 + MISSILE_HEAD_DATA_DX_IPOS] = RENDER_HEIGHT / (x1 - y1);
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_DY_IPOS] = 1 << MISSILE_MOVEMENT_ENCODE_SHL;
						//x1 FREE
						//y1 FREE

						break;
					}
					// y4 FREE
					// p2 FREE
				}

				if (wallCount > 0) {
					if (wallCount % GR_BLOCK_HEIGHT == 0) {
						// x1 ALLOC
						x1 = wallRight - wallLeft;
						if (wallCount < (GR_SCREEN_BLOCK_WIDTH - x1) * GR_BLOCK_HEIGHT) {
							wallLeft--;
							wallRight++;
						} else if (x1 > WALL_MOVEMENT_1) {
							wallLeft++;
							wallRight--;
						} else if (x1 > WALL_MOVEMENT_2) {
							if ((((frameCount >> 6) + reflectCount) & 1) == 0) {
								wallLeft--;
								wallRight--;
							} else {
								wallLeft++;
								wallRight++;
							}
						} else {
							wallLeft--;
							wallRight++;
						}
						if (wallLeft < MIN_WALL_LEFT) {
							wallLeft = MIN_WALL_LEFT;
						}
						if (wallLeft > MAX_WALL_LEFT) {
							wallLeft = MAX_WALL_LEFT;
						}
						if (wallRight > MAX_WALL_RIGHT) {
							wallRight = MAX_WALL_RIGHT;
						}
						if (wallRight < MIN_WALL_RIGHT) {
							wallRight = MIN_WALL_RIGHT;
						}
						// x1 FREE
					}
					wallCount--;
				} else {
					wallLeft = MIN_WALL_LEFT;
					wallRight = MAX_WALL_RIGHT;
				}

				// Change the missile explosion color
				// x1 ALLOC
				x1 = ((frameCount >> 2) % PALETTE_MISSILE_EXPLODE_DELTA) + PALETTE_MISSILE_EXPLODE_BASE_POS;
				OBJECT_TYPES[MISSILE_EXPLODE_1_TYPE_COLOR_POS] = x1;
				OBJECT_TYPES[MISSILE_EXPLODE_2_TYPE_COLOR_POS] = x1;
				OBJECT_TYPES[MISSILE_EXPLODE_3_TYPE_COLOR_POS] = x1;
				// x1 FREE

				// ========================================================
				// ========================================================
				// Render object list and perform intersection checks
				//    it would be nice to have this outside the gamestate loop,
				//    but that would mean adding checks for life (so the life meter
				//    doesn't expand exponentially, and we don't keep resetting our game
				//    state).

				// p3 ALLOC
				p3 = 0;
				// p3 = OBJECT_INST index
				// x2 ALLOC
				for (x2 = 0; x2 < objectCount; x2++, p3 += OBJECT_INST_RECORD_SIZE) {
					// x2 = object index
					if (OBJECT_INST[p3 + OBJECT_INST_RECORD_VISIBLE_INDEX] != VISIBLE_YES) {
						continue;
					}

					// p1 ALLOC
					p1 = OBJECT_INST[p3 + OBJECT_INST_RECORD_FLAG_INDEX];
					// p1 = OBJECT_TYPES flags
					// x3 ALLOC
					x3 = OBJECT_INST[p3 + OBJECT_INST_RECORD_TYPE_INDEX];
					// x3 = OBJECT_TYPE index

					// y3 ALLOC
					y3 = x3 * OBJECT_TYPE_RECORD_SIZE;
					// y3 = OBJECT_TYPE pointer

					// p0 ALLOC
					p0 = OBJECT_INST[p3 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
					// p0 = image pointer

					// Movement calculations
					int nextXPos = OBJECT_INST[p3 + OBJECT_INST_RECORD_X_INDEX];
					int nextYPos = OBJECT_INST[p3 + OBJECT_INST_RECORD_Y_INDEX];

					// ================================================================
					// Copy object image if needed
					if (OBJECT_INST[p3 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] == NEEDSCOPY_COPY) {
						OBJECT_INST[p3 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_NOOP;

						//y4 ALLOC
						y4 = y3 + OBJECT_TYPE_RECORD_IMAGE_INDEX;
						// y4 = source index (packed)
						//x1 ALLOC
						x1 = OBJECT_TYPES[y4];
						// x1 = source image packed data
						//x4 ALLOC
						x4 = 0;
						// x4 = bit count

						//y1 ALLOC
						for (y1 = 0; y1 < OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_HEIGHT_INDEX] * OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_WIDTH_INDEX]; y1++) {
							// y1 = output image position
							//y5 ALLOC
							y5 = OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_COLOR_INDEX];
							if (x3 == OBJECT_TYPE_BREAKOUT_INDEX) {
								// Breakout has a color based on row
								y5 = ((OBJECT_INST[p3 + OBJECT_INST_RECORD_Y_INDEX]) % PALETTE_BREAKOUT_MOD) + PALETTE_BREAKOUT_POS;
							}
							if ((x1 & 1) == 0) {
								y5 = PALETTE_BACKGROUND_POS;
							}
							OBJECT_INST[p0 + y1] = y5;
							//y5 FREE
							x1 >>>= 1;
							x4++;
							if (x4 >= IMAGE_BITS_PER_POS) {
								x4 = 0;
								y4++;
								x1 = OBJECT_TYPES[y4];
							}
						}
						//y1 FREE
						//x4 FREE
						//x1 FREE
						//y4 FREE
					}

					// ================================================================
					// Perform action on object
					switch (x3) {

					case OBJECT_TYPE_WALL_INDEX:
						// -------------------------------------------------------
						// Move wall down

						nextYPos++;
						// if wall > RENDER_HEIGHT + GR_BLOCK_HEIGHT
						// then move it to top.
						if (nextYPos >= WALL_START_Y) {
							// Adjust the new wall position based on a possible
							// missed y position (it happens on a rare occasion).
							nextYPos = OBJECT_INST[p3 + OBJECT_INST_RECORD_Y_INDEX] = WALL_END_Y + (nextYPos - WALL_START_Y);

							OBJECT_INST[p0 + WALL_IMAGE_DATA_LBREAK_IPOS] = wallLeft * GR_BLOCK_WIDTH;
							OBJECT_INST[p0 + WALL_IMAGE_DATA_RBREAK_IPOS] = wallRight * GR_BLOCK_WIDTH;

							// x1 ALLOC
							for (x1 = 1; x1 < wallLeft; x1++) {
								OBJECT_INST[p0 + x1] = OBJECT_INST[p0];
							}
							for (; x1 <= wallRight; x1++) {
								// The middle should already be 0, but there are cases where
								// this doesn't happen correctly.
								OBJECT_INST[p0 + x1] = PALETTE_BACKGROUND_POS;
							}
							for (; x1 <= GR_SCREEN_BLOCK_WIDTH; x1++) {
								OBJECT_INST[p0 + x1] = OBJECT_INST[p0];
							}

							// x1 FREE
						}

						// There are various checks we do on the walls when comparing
						// them against other objects at the same y position, to
						// ensure data validity
						// p2 ALLOC
						p2 = nextYPos / GR_BLOCK_HEIGHT;
						// p1 = block position of wall
						// x1 ALLOC
						x1 = OBJECT_INST[p0 + WALL_IMAGE_DATA_LBREAK_IPOS];
						// x1 = left end position
						// y1 ALLOC
						y1 = OBJECT_INST[p0 + WALL_IMAGE_DATA_RBREAK_IPOS];
						// y1 = right start position

						// If the wall is at the NPADDLE y level, then set the
						// NPADDLE left/right wall boundaries.  This works because
						// NPADDLE always is moved after the walls.
						if (p2 == NPADDLE_BLOCK_Y) {
							OBJECT_INST[OBJECT_INST[NPADDLE_OBJECT_INST_IMAGE_POS_POS] + NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS] = x1;
							OBJECT_INST[OBJECT_INST[NPADDLE_OBJECT_INST_IMAGE_POS_POS] + NPADDLE_IMAGE_DATA_WALL_RIGHT_IPOS] = y1;
						}
						if (p2 == OBJECT_INST[BALL_OBJECT_INST_Y_POS] / GR_BLOCK_HEIGHT) {
							// ensure the ball is within the wall bounds
							// x4 ALLOC
							x4 = OBJECT_INST[BALL_OBJECT_INST_X_POS];
							// x4 = ball x position (in blocks)
							// y4 ALLOC
							y4 = OBJECT_INST[OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS] + BALL_IMAGE_DATA_DX_IPOS];
							// x5 ALLOC
							x5 = x1;
							// x5 = x position the ball will move to
							// b1 ALLOC
							b1 = false;
							// set to true if the ball needs to move
							if (x4 < x1) {
								b1 = true;
								if (y4 < 0) {
									y4 = 0 - y4;
								}
							}
							if (x4 > y1 - GR_BLOCK_WIDTH) {
								b1 = true;
								x5 = y1 - GR_BLOCK_WIDTH;
								if (y4 > 0) {
									y4 = 0 - y4;
								}
							}

							if (b1) {
								// move the ball to inside the wall
								OBJECT_INST[BALL_OBJECT_INST_X_POS] = x5;
								OBJECT_INST[BALL_IMAGE_DATA_X_IPOS + OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS]] = x5 << BALL_MOVEMENT_ENCODE_SHL;
								// adjust the x movement direction so it's going
								// the right way.
								OBJECT_INST[BALL_IMAGE_DATA_DX_IPOS + OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS]] = y4;
							}
							// b1 FREE
							// x5 FREE
							// y4 FREE
							// x4 FREE
						}
						// x1 FREE
						// y1 FREE
						// p2 FREE
						break;

					case OBJECT_TYPE_BREAKOUT_INDEX:
						// Do nothing; here because of all the tetris types, which
						// are summed up in the "default" case.
						break;

					case OBJECT_TYPE_INVADER1_INDEX:
					case OBJECT_TYPE_INVADER2_INDEX:
						// -------------------------------------------------------
						// Adjust the invader

						if ((frameCount % INVADER_CHANGE_IMAGE_MOD) == 0) {
							OBJECT_INST[p3 + OBJECT_INST_RECORD_TYPE_INDEX] = (OBJECT_TYPE_INVADER2_INDEX - x3) + OBJECT_TYPE_INVADER1_INDEX;
							OBJECT_INST[p3 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
						}

						// y1 ALLOC
						y1 = (frameCount >>> INVADER_FRAMES_PER_MOVE_SHR) % INVADER_FRAME_MOVE_MOD;
						// y1 = current movement state

						// check if initial position was ever set
						if (OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS] == 0) {
							OBJECT_INST[p0 + INVADER_DATA_BASE_X_IPOS] = nextXPos;
							OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS] = nextYPos;
							OBJECT_INST[p0 + INVADER_DATA_FIRE_IPOS] = x2 << 2;
						}

						// Movement: search from highest to lowest index
						if (y1 > INVADER_UP_MOVEMENT_START) {
							// perform up movement
							nextXPos = OBJECT_INST[p0 + INVADER_DATA_BASE_X_IPOS];
							nextYPos = OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS] + (INVADER_FRAME_MOVE_MOD - y1);
						} else if (y1 > INVADER_RIGHT_MOVEMENT_START) {
							// perform right movement
							nextXPos = OBJECT_INST[p0 + INVADER_DATA_BASE_X_IPOS] + y1 - INVADER_RIGHT_MOVEMENT_START - INVADER_MAX_H_MOVEMENT;
							nextYPos = OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS] + INVADER_MAX_V_MOVEMENT;
						} else if (y1 > INVADER_DOWN_MOVEMENT_START) {
							// perform down movement
							nextXPos = OBJECT_INST[p0 + INVADER_DATA_BASE_X_IPOS] - INVADER_MAX_H_MOVEMENT;
							nextYPos = OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS] + (y1 - INVADER_DOWN_MOVEMENT_START);
						} else {
							// perform left movement
							nextXPos = OBJECT_INST[p0 + INVADER_DATA_BASE_X_IPOS] - y1;
							nextYPos = OBJECT_INST[p0 + INVADER_DATA_BASE_Y_IPOS];
						}
						// y1 FREE

						// Fire a bullet check

						if ((OBJECT_INST[p0 + INVADER_DATA_FIRE_IPOS] + frameCount * reflectCount + nextXPos) % INVADER_FIRE_FREQUENCY == 0 && objectCount < MAX_OBJECT_INST_RECORDS) {
							OBJECT_INST[p0 + INVADER_DATA_FIRE_IPOS] += frameCount >> 3;
							// y1 ALLOC
							y1 = objectCount * OBJECT_INST_RECORD_SIZE;
							// y1 = bullet position
							objectCount++;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_X_INDEX] = nextXPos + INVADER_BULLET_XDELTA;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_Y_INDEX] = nextYPos + ENEMYFIRE_HEIGHT;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_FLAG_INDEX] = ENEMYFIRE_FLAGS;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_ENEMYFIRE_INDEX;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_YES;
							// y1 FREE
						}

						break;

					case OBJECT_TYPE_NPADDLE_INDEX:
						// -------------------------------------------------------
						// move the NPADDLE

						// Move the paddle based on speed & ball location
						// y1 ALLOC
						y1 = OBJECT_INST[BALL_OBJECT_INST_X_POS] - nextXPos;
						// y1 = delta for paddle movement
						// x4 ALLOC
						x4 = NPADDLE_BASE_SPEED + ((playerScore - computerScore) >> 1);
						// x4 = maximum movement rate for paddle; it adjusts
						//  based on how well the player is doing.
						if (x4 < NPADDLE_MIN_SPEED) {
							x4 = NPADDLE_MIN_SPEED;
						}
						if (x4 > NPADDLE_MAX_SPEED) {
							x4 = NPADDLE_MAX_SPEED;
						}
						if (y1 > x4) {
							y1 = x4;
						}
						if (y1 < 0 - x4) {
							y1 = 0 - x4;
						}
						nextXPos += y1;
						// y1 FREE
						// x4 FREE

						// adjust position based on wall limits
						if (nextXPos < OBJECT_INST[p0 + NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS]) {
							nextXPos = OBJECT_INST[p0 + NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS];
						}
						if (nextXPos + NPADDLE_WIDTH >= OBJECT_INST[p0 + NPADDLE_IMAGE_DATA_WALL_RIGHT_IPOS]) {
							nextXPos = OBJECT_INST[p0 + NPADDLE_IMAGE_DATA_WALL_RIGHT_IPOS] - NPADDLE_WIDTH;
						}
						break;

					case OBJECT_TYPE_SPADDLE_INDEX:
						// --------------------------------------------------------
						// Move the paddle

						// If the paddle has been dented on the sides, this
						// will prevent the player from moving the now-dented paddle
						// all the way to the sides.  Fixing this logic is a bit
						// complicated, involving looping through the blocks in the
						// paddle to find the edges.

						// x1 ALLOC
						x1 = SPADDLE_BLOCK_WIDTH;
						// x1 = left-most paddle block
						// x4 ALLOC
						x4 = 0;
						// x4 = right-most paddle block
						// p2 ALLOC
						for (p2 = 0; p2 < SPADDLE_BLOCK_WIDTH; p2++) {
							if (OBJECT_INST[p0 + p2] != PALETTE_BACKGROUND_POS) {
								if (p2 < x1) {
									x1 = p2;
								}
								if (p2 > x4) {
									x4 = p2;
								}
							}
						}
						x1 *= GR_BLOCK_WIDTH;
						x4 *= GR_BLOCK_WIDTH;
						// p2 FREE

						// Keep the mouse centered on the paddle
						// y1 ALLOC
						y1 = (this.mousePos & MOUSE_X_MASK) - x1 - ((x4 - x1) >> 1);
						// y1 = paddle render x position

						if (y1 + x1 < SPADDLE_MIN_X) {
							y1 = SPADDLE_MIN_X - x1;
						} else if (y1 + x4 > SPADDLE_MAX_X) {
							y1 = SPADDLE_MAX_X - x4;
						}

						// x4 FREE
						// x1 FREE
						nextXPos = y1;

						// --------------------------------------------------------
						// stickiness of ball: put ball on the SPADDLE if the
						// ball is sticky
						if (ballSticky) {
							OBJECT_INST[BALL_OBJECT_INST_X_POS] = y1 + BALL_STICKY_X_OFFSET;
							OBJECT_INST[BALL_OBJECT_INST_Y_POS] = BALL_STICKY_Y;
							OBJECT_INST[OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS] + BALL_IMAGE_DATA_X_IPOS] = (y1 + BALL_STICKY_X_OFFSET) << BALL_MOVEMENT_ENCODE_SHL;
							OBJECT_INST[OBJECT_INST[BALL_OBJECT_INST_IMAGE_POS_POS] + BALL_IMAGE_DATA_Y_IPOS] = BALL_STICKY_Y << BALL_MOVEMENT_ENCODE_SHL;
						}
						// y1 FREE
						break;

					case OBJECT_TYPE_BALL_INDEX:
						// -------------------------------------------------------
						if (ballSticky) {
							// ball is stuck to the paddle; do not perform
							// collision detection.  Set the flags to sticky
							// state.
							p1 = BALL_STICKY_FLAGS;
						} else {
							nextXPos = (OBJECT_INST[p0 + BALL_IMAGE_DATA_X_IPOS] + OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS]) >> BALL_MOVEMENT_DECODE_SHR;
							nextYPos = (OBJECT_INST[p0 + BALL_IMAGE_DATA_Y_IPOS] + OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS]) >> BALL_MOVEMENT_DECODE_SHR;
						}

						break;

					case OBJECT_TYPE_MISSILE_HEAD_INDEX:
						// x1 ALLOC
						x1 = DISPLAY_WIDTH;
						// x1 = distance x position from missile head
						// y1 ALLOC
						y1 = DISPLAY_HEIGHT;
						// y1 = distance y position from missile head.

						// Find next position in movement
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_X_IPOS] += OBJECT_INST[p0 + MISSILE_HEAD_DATA_DX_IPOS];
						// x4 ALLOC
						x4 = (OBJECT_INST[p0 + MISSILE_HEAD_DATA_X_IPOS]) >> MISSILE_MOVEMENT_DECODE_SHR;
						OBJECT_INST[p0 + MISSILE_HEAD_DATA_Y_IPOS] += OBJECT_INST[p0 + MISSILE_HEAD_DATA_DY_IPOS];
						// y4 ALLOC
						y4 = (OBJECT_INST[p0 + MISSILE_HEAD_DATA_Y_IPOS]) >> MISSILE_MOVEMENT_DECODE_SHR;

						if (mousePressed) {
							// Check if the mouse pressed close to the missile head.
							x1 = (this.mousePos & MOUSE_X_MASK) - nextXPos;
							y1 = (this.mousePos >>> MOUSE_Y_SHR) - nextYPos;
						}
						nextXPos = x4;
						nextYPos = y4;
						// x4 FREE
						// y4 FREE

						// Alternate the color
						if (((frameCount >> 3) & 1) == 0) {
							OBJECT_INST[p0] = (OBJECT_INST[p0] == PALETTE_MISSILE_HEAD_POS) ? PALETTE_MISSILE_HEAD2_POS : PALETTE_MISSILE_HEAD_POS;
						}

						if (((x1 < MISSILE_MAX_DIST_X && x1 > MISSILE_MIN_DIST_X && y1 < MISSILE_MAX_DIST_Y && y1 > MISSILE_MIN_DIST_Y) || nextYPos > MISSILE_HEAD_EXPLODE_Y || nextXPos < 0 || nextXPos > RENDER_WIDTH)
								&& objectCount < MAX_OBJECT_INST_RECORDS) {
							// x1 FREE
							// y1 FREE
							// explode the missile
							OBJECT_INST[p3 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_DESTROY;

							// y1 ALLOC
							y1 = objectCount * OBJECT_INST_RECORD_SIZE;
							// y1 = new object position
							objectCount++;
							// Don't need to set coordinates; these are set in the per-object test
							OBJECT_INST[y1 + OBJECT_INST_RECORD_X_INDEX] = nextXPos - MISSILE_EXPLODE_MID_X;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_Y_INDEX] = nextXPos - MISSILE_EXPLODE_MID_Y;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_FLAG_INDEX] = MISSILE_EXPLODE_FLAGS;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_MISSILE_EXPLODE_1_INDEX;
							OBJECT_INST[y1 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_YES;
							y1 = OBJECT_INST[y1 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
							// y1 = image position of new object
							OBJECT_INST[y1 + MISSILE_EXPLODE_DATA_ALIVECOUNT_IPOS] = MISSILE_EXPLODE_LIFESPAN;
							OBJECT_INST[y1 + MISSILE_EXPLODE_DATA_BASE_X_IPOS] = nextXPos - MISSILE_EXPLODE_MID_X;
							OBJECT_INST[y1 + MISSILE_EXPLODE_DATA_BASE_Y_IPOS] = nextYPos + MISSILE_EXPLODE_MID_Y;
						}
						// y1 FREE

						break;

					case OBJECT_TYPE_MISSILE_EXPLODE_1_INDEX:
					case OBJECT_TYPE_MISSILE_EXPLODE_2_INDEX:
					case OBJECT_TYPE_MISSILE_EXPLODE_3_INDEX:

						// count down to detonation
						// y1 ALLOC
						y1 = --OBJECT_INST[p0 + MISSILE_EXPLODE_DATA_ALIVECOUNT_IPOS];
						if (y1 <= 0) {
							OBJECT_INST[p3 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_DESTROY;
							// don't break.  It doesn't hurt anything to
							// continue to the shake.
						} else {
							// Increment the frame if needed, but always mark as
							// needs copy to allow for changing the image color.
							y1 = -1 + (MISSILE_EXPLODE_LIFESPAN - y1 + MISSILE_EXPLODE_SWITCH_IMAGE_DURATION) / MISSILE_EXPLODE_SWITCH_IMAGE_DURATION;
							OBJECT_INST[p3 + OBJECT_INST_RECORD_TYPE_INDEX] = OBJECT_TYPE_MISSILE_EXPLODE_1_INDEX + y1;
							OBJECT_INST[p3 + OBJECT_INST_RECORD_NEEDSCOPY_INDEX] = NEEDSCOPY_COPY;
						}
						// y1 FREE
						nextXPos = OBJECT_INST[p0 + MISSILE_EXPLODE_DATA_BASE_X_IPOS];
						nextYPos = OBJECT_INST[p0 + MISSILE_EXPLODE_DATA_BASE_Y_IPOS];

						break;

					case OBJECT_TYPE_ENEMYFIRE_INDEX:
						// -------------------------------------------------------
						nextYPos += ENEMYFIRE_MOVEMENT_Y;

						break;

					/*
					case OBJECT_TYPE_TETRIS_LHOOK_INDEX:
					case OBJECT_TYPE_TETRIS_RHOOK_INDEX:
					case OBJECT_TYPE_TETRIS_BAR_INDEX:
					case OBJECT_TYPE_TETRIS_BOX_INDEX:
					case OBJECT_TYPE_TETRIS_BUMP_INDEX:
					*/
					default:
						// -------------------------------------------------------

						if (mousePressed && OBJECT_INST[p0 + TETRIS_IMAGE_DATA_IS_STICKY_IPOS] == 0) {
							// Rotate the image by 90 degrees to a temporary space
							// if the mouse was pressed and the piece isn't "stuck"
							// to something else.

							// x1 ALLOC
							for (x1 = 0; x1 < TETRIS_IMAGE_SIZE; x1++) {
								// x4 ALLOC
								x4 = 56 - ((x1 & 7) << 3) + (x1 >> 3);
								/* 4x4
								x4 = 12 - ((x1 & 3) << 2) + (x1 >> 2);
								*/
								OBJECT_INST[p0 + TETRIS_IMAGE_TEMP_DATA_IPOS + x1] = OBJECT_INST[p0 + x4];
								// x4 FREE
							}
							// Move the rotated image back into the image
							for (x1 = 0; x1 < TETRIS_IMAGE_SIZE; x1++) {
								OBJECT_INST[p0 + x1] = OBJECT_INST[p0 + TETRIS_IMAGE_TEMP_DATA_IPOS + x1];
							}
							// x1 FREE
						}

						if (frameCount % TETRIS_MOVEMENT_MOD == 0) {
							nextYPos += TETRIS_MOVEMENT_RATE;
						}

						break;
					}

					// ================================================================
					// Intersect object to stop fall & dent objects & destroy objects
					// This needs to be done over many loops to cover these conditions:
					//   * object moving from OBJECT_INST[x2 + OBJECT_TYPE_?_INDEX] to
					//     next?Pos
					//   * object being not aligned with the blocks.
					if ((p1 & COLLIDES_FLAGS) != 0) {

						// Traverse the direction of movement, pixel by pixel, checking
						// for intersection with objects along the way.  Sum up the
						// # of pixels intersected along each side (N, E, S, W, but
						// really, with the direction, it can be only 2 sides max,
						// along the x & y axes).  Use <= and >= for the side sums to
						// determine which way to bounce (== because, if they are ==,
						// then both axes should reflect).

						// The optimization is to move to nearest grid collision of the next object,
						// but if we need to check for a collision on *every* object, then the
						// only real solution is to progress pixel by pixel.

						// We need to track each corner for movement, to ensure
						// these cases are covered (x = moving object, o = collide-with object,
						// M = point of collision):
						//
						// Angle of
						// motion:   1.  oo   2. xx    3.  oo
						//    _         xMo      xMo       oMx
						//    /|        xx        oo        xx
						//   /      (And likewise for other directions of movement)
						//
						// Really, only 3 of the 4 corners need to be checked, but that may be
						// to many if statements for the benefit of just testing the 4th corner.
						// This can be dealt with by means of either 2 inner loops (to get the
						// 0/1 value for x,y of the points), or 1 inner loop + bit checking (0-3,
						// checking bits 0 & 1 for the 0/1 value).  These 0/1 values can be used for
						// multiplying an adder to the x/y value.

						// Keep sum for ALL objects intersected along the movement.
						// If 1 or more intersections occurred at a pixel movement, then
						// break the collision loop.

						// Calculation for overlap depends upon direction of motion:
						//  X Axis:
						//    xdir >= 0:
						//        overlapX = cornerX - withX
						//    xdir < 0:
						//        overlapX = (withX + GR_BLOCK_WIDTH) - cornerX
						//    Generalization:
						//        overlapX = xdir * (cornerX - withX - (GR_BLOCK_WIDTH * isNegative))
						//   Y Axis:
						//    ydir > 0:
						//        overlapY = cornerY - (withY - GR_BLOCK_HEIGHT)
						//    ydir <= 0:
						//        overlapY = withY - cornerY
						//    Generalization:
						//        overlapY = ydir * (cornerY - withY + (GR_BLOCK_HEIGHT * isPositive))

						// Initialize movement

						int xpos = OBJECT_INST[p3 + OBJECT_INST_RECORD_X_INDEX];
						int xdelta = nextXPos - xpos;
						int xdir = 0;
						int withXOverlapSubt = GR_BLOCK_WIDTH;
						if (xdelta > 0) {
							xdir++;
							withXOverlapSubt = 0;
						}
						if (xdelta < 0) {
							// decr to not include a number, specifically the -1
							// constant pool value.
							xdir--;

							// xdelta = absolute value
							xdelta = 0 - xdelta;

							// withXoverlapAdd = addition to withX to determine accurate
							// block overlap
						}

						int ypos = OBJECT_INST[p3 + OBJECT_INST_RECORD_Y_INDEX];
						int ydelta = nextYPos - ypos;
						int ydir = 0;
						int withYOverlapAdd = GR_BLOCK_HEIGHT;
						if (ydelta > 0) {
							ydir++;
						}
						if (ydelta < 0) {
							// decr to not include a number, specifically the -1
							// constant pool value.
							ydir--;

							// ydelta = absolute value
							ydelta = 0 - ydelta;
							withYOverlapAdd = 0;
						}
						int movementError = xdelta - ydelta;

						int sourceWidth = OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_WIDTH_INDEX];
						int sourceHeight = OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_HEIGHT_INDEX];

						int maxP2 = objectCount * OBJECT_INST_RECORD_SIZE;

						movementLoop: while (true) {
							boolean collision = false;
							boolean ballReflectNow = false;
							int collisionXPixels = 0;
							int collisionYPixels = 0;
							int highestTetrisPos = DISPLAY_HEIGHT;
							int collisionPaddle = 0;

							// --------------------------------------------------------------------
							// perform collision check at (xpos, ypos)

							// p2 ALLOC
							withObjectLoop: for (p2 = 0; p2 < maxP2; p2 += OBJECT_INST_RECORD_SIZE) {
								// p2 = collide-with OBJECT_INST position

								// Discover collision type
								//y1 ALLOC
								y1 = OBJECT_INST[p2 + OBJECT_INST_RECORD_FLAG_INDEX];
								// y1 = collide-with flags
								//b1 ALLOC
								b1 = ((p1 & FLAGMSK_CAUSES_DESTROY) != 0) && ((y1 & FLAGMSK_DESTROYABLE) != 0);
								// b1 = destroy collision
								//b2 ALLOC
								b2 = ((p1 & FLAGMSK_CAUSES_DENTS) != 0) && ((y1 & FLAGMSK_DENTABLE) != 0) && !((x3 == OBJECT_TYPE_BALL_INDEX) && (p2 == SPADDLE_OBJECT_INST_START_POS))
										&& !((x3 == OBJECT_TYPE_WALL_INDEX) && ((y1 & FLAGMSK_IS_TETRIS) != 0));
								// b2 = dent collision, requires special logic for
								// ignoring ball + spaddle dent.
								//b3 ALLOC
								b3 = (x3 == OBJECT_TYPE_BALL_INDEX) && ((y1 & FLAGMSK_REFLECTS_BALL) != 0);
								// b3 = ball reflect
								//b4 ALLOC
								b4 = ((p1 & FLAGMSK_IS_TETRIS) != 0) && ((y1 & FLAGMSK_STOPS_TETRIS) != 0);
								// b4 = tetris stop
								//y1 FREE

								// Calculate whether the source image intersects this image
								// on a boundary cross location.  We know that the moving object
								// (source) will only collide with the stationary object if it
								// passes through the boundary of the stationary object.  We still
								// need to move through all pixels, because each object is aligned
								// differently, but we know for a specific stationary object
								// whether the boundary is being crossed or not.

								// The one edge case for this is if an object suddenly appears
								// on top of the other.  That's not necessarily a bad thing, and
								// will not adversely affect gameplay... Except for the Wall issue,
								// but those wall extensions appear off-screen.

								// x1 ALLOC
								x1 = OBJECT_INST[p2 + OBJECT_INST_RECORD_X_INDEX] /*+ withXPlane */;
								// x1 = with object base x location
								// y1 ALLOC
								y1 = OBJECT_INST[p2 + OBJECT_INST_RECORD_Y_INDEX] /*+ withYPlane*/;
								// y1 = with object base y location

								// Are the objects different, visible, and a combination
								// of flags that cause a collision?  Detection of
								// whether the rectangles intersect or not is indirectly
								// performed with the for loops below.
								if (p2 != p3 && OBJECT_INST[p2 + OBJECT_INST_RECORD_VISIBLE_INDEX] == VISIBLE_YES && (b1 || b2 || b3 || b4)) {
									// x4 FREE
									// y4 FREE
									// At this point, we know the collisions are "compatible", the
									// "with" object is visible, the two objects are not the same, and
									// they are breaking the collision boundary.

									// Now, check the 4 corners of each active block in the
									// source image, and map them to blocks on the with image;
									// if it's a collision, react accordingly.

									// Setup calculations for source image loop
									int withType = OBJECT_INST[p2 + OBJECT_INST_RECORD_TYPE_INDEX];
									int withTypePos = withType * OBJECT_TYPE_RECORD_SIZE;
									// p4 ALLOC
									p4 = OBJECT_INST[p2 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
									// p4 = with image pointer
									int withWidth = OBJECT_TYPES[withTypePos + OBJECT_TYPE_RECORD_WIDTH_INDEX];
									int withHeight = OBJECT_TYPES[withTypePos + OBJECT_TYPE_RECORD_HEIGHT_INDEX];
									int withX2 = x1 + withWidth * GR_BLOCK_WIDTH;
									int withY2 = y1 - withHeight * GR_BLOCK_HEIGHT;

									// p5 ALLOC
									p5 = p0;
									// p5 = current source pointer
									for (int sourceY = 0; sourceY < sourceHeight; sourceY++) {
										// y5 ALLOC
										y5 = ypos + sourceY * GR_BLOCK_WIDTH;
										for (int sourceX = 0; sourceX < sourceWidth; sourceX++, p5++) {
											// x5 ALLOC
											x5 = xpos + sourceX * GR_BLOCK_WIDTH;
											if (OBJECT_INST[p5] != PALETTE_BACKGROUND_POS) {
												// Source has a visible block.  Iterate across its 4 corners.
												// This is done by looping across a single int, and using the
												// first 2 bits to indicate which corner it's at.
												for (int corner = 0; corner < 4; corner++) {
													// x6 ALLOC
													x6 = x5 + ((corner & 1) * (GR_BLOCK_WIDTH - 1));
													// y6 ALLOC
													y6 = y5 - (((corner >> 1) & 1) * (GR_BLOCK_HEIGHT - 1));
													// subtract corner, because y starts at bottom,
													// and next corner is "up", which is negative.

													// Map this pixel to the with image
													// to discover if there is an active pixel
													// in the with image, along with
													// discovering the number of overlapping
													// pixels.

													// use the "overlap" variables temporarily to
													// calculate the intersecting block
													int overlapX = (x6 - x1) / GR_BLOCK_WIDTH;
													if (overlapX >= withWidth || overlapX < 0) {
														continue;
													}
													//p6 ALLOC
													p6 = p4 + overlapX;
													// p6 = mapped with image position

													int overlapY = (y1 - y6) / GR_BLOCK_HEIGHT;
													if (overlapY >= withHeight || overlapY < 0) {
														continue;
													}
													p6 += overlapY * withWidth;

													if (x6 >= x1 && x6 < withX2 && y6 <= y1 && y6 > withY2 && OBJECT_INST[p6] != PALETTE_BACKGROUND_POS) {
														collision = true;

														// Calculate actual overlap
														overlapX = xdir * (x6 - x1 - (overlapX * GR_BLOCK_WIDTH) - withXOverlapSubt);
														overlapY = (0 - ydir) * (y1 - (overlapY * GR_BLOCK_HEIGHT) - y6 - withYOverlapAdd);

														// ---------------------------------------
														// Multiple collision types can occur
														// within the same object.

														if (b1) {
															// destroy object
															// y7 ALLOC
															y7 = VISIBLE_DESTROY;
															if (p2 < OBJECT_INST_FIRST_DESTROYABLE_POS) {
																y7 = VISIBLE_NO;
															}
															OBJECT_INST[p2 + OBJECT_INST_RECORD_VISIBLE_INDEX] = y7;
															// y7 FREE

															if (x3 == OBJECT_TYPE_ENEMYFIRE_INDEX) {
																// On destroying something, enemy fire
																// also is destroyed
																OBJECT_INST[p3 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_DESTROY;
															}

															// Don't stop looking at this object -
															// it can reflect.
														}
														if (b2) {
															// cause dent; keep searching
															// this and all other objects.
															OBJECT_INST[p6] = PALETTE_BACKGROUND_POS;
														}
														if (b3) {
															// reflect ball motion + keep searching
															ballReflect = true;
															ballReflectNow = true;
															if (p2 == NPADDLE_OBJECT_INST_START_POS) {
																collisionPaddle = -1;
															}
															if (p2 == SPADDLE_OBJECT_INST_START_POS) {
																collisionPaddle = 1;
															}
															collisionXPixels += overlapX;
															collisionYPixels += overlapY;

															// Ensure that the next position is
															// set correctly
															nextXPos = xpos;
															nextYPos = ypos;
														}
														if (b4) {
															// Stop tetris
															// tetri only move down (increase y),
															// so use the smallest y position to
															// set the stop position.
															if (y6 < highestTetrisPos) {
																highestTetrisPos = y6;
															}
														}
														// p6 FREE
													}
													// y6 FREE
													// x6 FREE
												}
											}
											// x5 FREE
										}
										// y5 FREE
									}

									// p5 FREE

									// p4 FREE
								}

								// y1 FREE
								// x1 FREE
							}
							// p2 FREE

							// --------------------------------------------------------------------
							// Post-Movement loop
							if (collision) {
								if (highestTetrisPos < nextYPos) {
									nextYPos = highestTetrisPos;
									OBJECT_INST[p0 + TETRIS_IMAGE_DATA_IS_STICKY_IPOS] = 1;
								}
								if (ballReflectNow) {
									// determine whether the ball made
									// an X or Y contact.  If the offsets
									// are equal, do both.
									//y1 ALLOC
									y1 = OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS];
									// Cheat on the reflection: if it's a paddle hit, ALWAYS
									// reflect in the right direction.  This makes the game a bit
									// more interesting.
									if ((collisionPaddle < 0 && y1 < 0) || // spaddle + decreasing dy
											(collisionPaddle > 0 && y1 > 0) || // npaddle + increasing dy
											(collisionPaddle == 0 && collisionXPixels >= collisionYPixels) // not a paddle hit but a y reflect all the same
									) {
										// y reflect
										// add the x adjustment when the
										// rest of the reflect is working right
										if (collisionXPixels > collisionYPixels) {
											OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] += (GR_BLOCK_WIDTH / 2) - collisionXPixels;
										}
										if (collisionPaddle != 0) {
											y1 += ydir;
											OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] += xdir;
										}
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] = 0 - y1;
									}
									//y1 FREE
									if (collisionYPixels > collisionXPixels) {
										// x reflect
										// add the y adjustment when the
										// rest of the reflect is working right
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] += (GR_BLOCK_HEIGHT / 2) - collisionYPixels;
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] *= -1;
									}

									// Speed limiter
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] > BALL_MAX_X_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] = BALL_MAX_X_SPEED;
									}
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] < 0 - BALL_MAX_X_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] = 0 - BALL_MAX_X_SPEED;
									}
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] > 0 - BALL_MIN_X_SPEED && OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] < BALL_MIN_X_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS] = BALL_MIN_X_SPEED * xdir;
									}
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] > BALL_MAX_Y_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] = BALL_MAX_Y_SPEED;
									}
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] < 0 - BALL_MAX_Y_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] = 0 - BALL_MAX_Y_SPEED;
									}
									if (OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] > 0 - BALL_MIN_Y_SPEED && OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] < BALL_MIN_Y_SPEED) {
										OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS] = BALL_MIN_Y_SPEED * ydir;
									}

									reflectCount++;
								}

								// collisions cause a break in the collision test.
								break movementLoop;
							}
							if (((p1 & FLAGMSK_IS_TETRIS) != 0) && highestTetrisPos != nextYPos) {
								// tetris piece that's not sticky
								OBJECT_INST[p0 + TETRIS_IMAGE_DATA_IS_STICKY_IPOS] = 0;
							}
							if (xpos == nextXPos && ypos == nextYPos) {
								break movementLoop;
							}
							// Perform movement calculations for next loop
							// y1 ALLOC
							y1 = movementError << 1;
							// y1 = movement calculation adjustment
							if (y1 > -ydelta) {
								movementError -= ydelta;
								xpos += xdir;
							}
							if (y1 < xdelta) {
								movementError += xdelta;
								ypos += ydir;
							}
							// y1 FREE
						}

					}

					// ================================================================
					// Finalize object movement

					if (x3 == OBJECT_TYPE_BALL_INDEX) {
						if (nextYPos < BALL_N_BOUND) {
							// player scored a point
							playerScore++;
							computerScoreColor = PALETTE_SCORE_NEUTRAL_POS;
							playerScoreColor = PALETTE_SCORE_SCORED_POS;
							continue startLife;
						}
						if (nextYPos > BALL_S_BOUND) {
							// computer scored a point
							computerScore++;
							computerScoreColor = PALETTE_SCORE_SCORED_POS;
							playerScoreColor = PALETTE_SCORE_NEUTRAL_POS;
							continue startLife;
						}
						OBJECT_INST[p0 + BALL_IMAGE_DATA_X_IPOS] += OBJECT_INST[p0 + BALL_IMAGE_DATA_DX_IPOS];
						OBJECT_INST[p0 + BALL_IMAGE_DATA_Y_IPOS] += OBJECT_INST[p0 + BALL_IMAGE_DATA_DY_IPOS];
						nextXPos = OBJECT_INST[p0 + BALL_IMAGE_DATA_X_IPOS] >> BALL_MOVEMENT_DECODE_SHR;
						nextYPos = OBJECT_INST[p0 + BALL_IMAGE_DATA_Y_IPOS] >> BALL_MOVEMENT_DECODE_SHR;
					} else if (nextYPos - (OBJECT_TYPES[x3 + OBJECT_TYPE_RECORD_HEIGHT_INDEX] * GR_BLOCK_HEIGHT) > RENDER_HEIGHT) {
						OBJECT_INST[p3 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_DESTROY;
					}
					OBJECT_INST[p3 + OBJECT_INST_RECORD_X_INDEX] = nextXPos;
					OBJECT_INST[p3 + OBJECT_INST_RECORD_Y_INDEX] = nextYPos;

					// p0 FREE

					// y3 FREE
					// x3 FREE
					// p1 FREE
				}
				// x2 FREE
				// p3 FREE

				// ================================================================
				// ================================================================
				// Draw object and destroy object loop
				for (p0 = 0; p0 < objectCount * OBJECT_INST_RECORD_SIZE; p0 += OBJECT_INST_RECORD_SIZE) {
					// y3 ALLOC
					y3 = OBJECT_INST[p0 + OBJECT_INST_RECORD_TYPE_INDEX] * OBJECT_TYPE_RECORD_SIZE;
					// y3 = OBJECT_TYPES position

					if (OBJECT_INST[p0 + OBJECT_INST_RECORD_VISIBLE_INDEX] == VISIBLE_YES) {
						// ===============================================================
						// Draw object
						// p3 ALLOC
						p3 = OBJECT_INST[p0 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
						// p3 = OBJECT_INST image position

						if (y3 == OBJECT_TYPE_MISSILE_HEAD_POS) {
							// Draw missile path
							ogr.setColor(COLORS[PALETTE_MISSILE_PATH_POS]);
							ogr.drawLine(OBJECT_INST[p3 + MISSILE_HEAD_DATA_PATH_START_X_IPOS], OBJECT_INST[p3 + MISSILE_HEAD_DATA_PATH_START_Y_IPOS],
									OBJECT_INST[p0 + OBJECT_INST_RECORD_X_INDEX] + 2, OBJECT_INST[p0 + OBJECT_INST_RECORD_Y_INDEX] - 2);
						}

						// b1 ALLOC
						b1 = p0 <= BALL_OBJECT_INST_START_POS && p0 != SPADDLE_OBJECT_INST_START_POS;
						// b1 = any stuff to draw?
						// It will ignore all "constant" objects (never destroyed)

						// y4 ALLOC
						y4 = OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_HEIGHT_INDEX] * GR_BLOCK_HEIGHT;
						// x4 ALLOC
						x4 = OBJECT_TYPES[y3 + OBJECT_TYPE_RECORD_WIDTH_INDEX] * GR_BLOCK_WIDTH;

						// start y at GR_BLOCK_HEIGHT because we start the rendering
						// at the bottom, and GUI rectangles mark the y at the top.
						// y1 ALLOC
						for (y1 = GR_BLOCK_HEIGHT; y1 <= y4; y1 += GR_BLOCK_HEIGHT) {
							// y1 = relative y position
							// x1 ALLOC
							for (x1 = 0; x1 < x4; x1 += GR_BLOCK_WIDTH) {
								// x1 = relative x position
								if (OBJECT_INST[p3] != PALETTE_BACKGROUND_POS) {
									b1 = true;
									ogr.setColor(COLORS[OBJECT_INST[p3]]);
									ogr.fillRect(x1 + OBJECT_INST[p0 + OBJECT_INST_RECORD_X_INDEX], OBJECT_INST[p0 + OBJECT_INST_RECORD_Y_INDEX] - y1, GR_BLOCK_WIDTH, GR_BLOCK_HEIGHT);
								}
								p3++;
							}
							// x1 FREE
						}
						// y1 FREE
						if (!b1) {
							// did not draw, so destroy the object
							OBJECT_INST[p0 + OBJECT_INST_RECORD_VISIBLE_INDEX] = VISIBLE_DESTROY;
						}
						// x4 FREE
						// y4 FREE
						// b1 FREE
						// p3 FREE
					}

					if (OBJECT_INST[p0 + OBJECT_INST_RECORD_VISIBLE_INDEX] == VISIBLE_DESTROY) {
						// ================================================================
						// Destroy object

						// If the object being removed is SPADDLE, then this counts as
						// a death
						// SIZEDO this is a minor place for space decrease - remove the
						// check on gameState, and move the destroy/draw block up into
						// the gameState case.
						if (p0 == SPADDLE_OBJECT_INST_START_POS) {
							// computer scored a point
							computerScore++;
							computerScoreColor = PALETTE_SCORE_SCORED_POS;
							playerScoreColor = PALETTE_SCORE_NEUTRAL_POS;
							continue startLife;
						}

						// p3 ALLOC
						p3 = OBJECT_INST[p0 + OBJECT_INST_RECORD_IMAGE_POS_INDEX];
						// p3 = OBJECT_INST image position

						// remove the count of object; do this first, because we'll be
						// only moving the (minus current) count down.
						objectCount--;
						// Move all data from the next object up to the end of the
						// object list.
						// y1 ALLOC
						for (y1 = p0; y1 < objectCount * OBJECT_INST_RECORD_SIZE; y1++) {
							// y1 = index into OBJECT_INST to copy
							OBJECT_INST[y1] = OBJECT_INST[y1 + OBJECT_INST_RECORD_SIZE];
						}

						// Ensure the image pointer is saved
						OBJECT_INST[y1 + OBJECT_INST_RECORD_IMAGE_POS_INDEX] = p3;
						// y1 FREE
						// p3 FREE

						// Decrement p0, because the next loop will rerun on the
						// same index, which is the next object.
						p0 -= OBJECT_INST_RECORD_VISIBLE_INDEX;
					}
					// b1 FREE
				}
				// y3 FREE
				// p0 FREE

				// --------------------------------------------------------
				// Draw scores
				ogr.setColor(COLORS[computerScoreColor]);
				ogr.drawString(Integer.toString(computerScore), SCORE_COMP_X, SCORE_COMP_Y);
				ogr.setColor(COLORS[playerScoreColor]);
				ogr.drawString(Integer.toString(playerScore), SCORE_PLAYER_X, SCORE_PLAYER_Y);

				// ========================================================
				// ========================================================
				// Render the frame and wait for the next frame
				sg.drawImage(image, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, // destination
						0, 0, RENDER_WIDTH, RENDER_HEIGHT, // source
						null);

				// ========================================================
				// Wait for the end of the frame
				do {
					Thread.yield();
				} while (System.nanoTime() - lastTime < 0);
				if (!isActive()) {
					// if we are nice citizens, we would properly stop our
					// audio usage,  But we're in 4k, so we don't.
					//outputLine.drain();
					//outputLine.close();
					return;
				}
				lastTime += FRAME_WAIT_TIME;
			}
		}
	}

	public boolean handleEvent(Event e) {
		// Process the key and mouse events.
		// Note that this can miss key and mouse events if they happen faster
		// than the frame processing can accept them.  The "fix" is to only
		// mark mouse press, and clear that flag when the click is handled
		// in the code, rather than handling mouse_up events.

		//boolean down = false;

		switch (e.id) {
		// all mouse events require updating the mouse position
		//case Event.MOUSE_DOWN:
		//    down = true;
		//    // fall through
		case Event.MOUSE_UP:
			//this.mouseClick = down;
			this.mouseClick = true;
			// fall through to pick up x/y position
		case Event.MOUSE_MOVE:
		case Event.MOUSE_DRAG:
			this.mousePos = (e.x >>> ENCODE_MOUSE_X_SHR)

			// Shift the y right to remove the lower bits that
			// are insignificant due to screen scaling.
			// SIZEDO test if this is less space than a mask
					+ ((e.y >>> ENCODE_MOUSE_Y_SHR) << ENCODE_MOUSE_Y_SHL);
		}
		return false;
	}

	// RENDERING CONSTANTS
	private static final int DISPLAY_WIDTH = 408;
	private static final int DISPLAY_HEIGHT = 512;
	private static final int RENDER_SCALING_FACTOR = 2; // 1 - double resolution
	private static final int RENDER_SHR_FACTOR = 1; // 0 - double resolution
	private static final int RENDER_WIDTH = DISPLAY_WIDTH / RENDER_SCALING_FACTOR;
	private static final int RENDER_HEIGHT = DISPLAY_HEIGHT / RENDER_SCALING_FACTOR;

	//private static final int RENDER_PIXEL_COUNT = RENDER_WIDTH * RENDER_HEIGHT;
	private static final int FRAMES_PER_SECOND = 30;
	private static final long NANOS_PER_SECOND = 1000000000;
	private static final long FRAME_WAIT_TIME = NANOS_PER_SECOND / FRAMES_PER_SECOND;
	private static final int ENCODE_MOUSE_X_SHR = RENDER_SHR_FACTOR;
	private static final int ENCODE_MOUSE_Y_SHL = 8; // 12 - double resolution
	private static final int ENCODE_MOUSE_Y_SHR = RENDER_SHR_FACTOR;
	// RENDER_WIDTH is 204, which is < 256.
	// NOTE: because of this masking, the actual X may be much further
	// out, but ONLY if the display screen is bigger than what we dictate
	// (thus, when testing in an applet viewer, which you can stretch
	// the frame size, the x may suddenly jump down to 0.
	private static final int MOUSE_X_MASK = 0xff; //0x7ff - double resolution
	private static final int MOUSE_Y_SHR = ENCODE_MOUSE_Y_SHL;

	private static final int PALETTE_COMPONENT_MAX = 4;
	private static final int PALETTE_COMPONENT_SHL = 6;
	private static final int PALETTE_COUNT = PALETTE_COMPONENT_MAX * PALETTE_COMPONENT_MAX * PALETTE_COMPONENT_MAX;

	// Color mapping is based upon the computation of the color array
	private static final int PALETTE_BACKGROUND_POS = 0;
	private static final int PALETTE_SCORE_NEUTRAL_POS = 63;
	private static final int PALETTE_SCORE_SCORED_POS = 14;
	private static final int PALETTE_NET_POS = 63;
	private static final int PALETTE_BREAKOUT_POS = 20;
	private static final int PALETTE_BREAKOUT_MOD = PALETTE_COUNT - PALETTE_BREAKOUT_POS - 1;
	private static final int PALETTE_INVADER1_POS = 14;
	private static final int PALETTE_INVADER2_POS = PALETTE_INVADER1_POS;
	private static final int PALETTE_NPADDLE_POS = 63;
	private static final int PALETTE_SPADDLE_POS = 63;
	private static final int PALETTE_BALL_POS = 63;
	private static final int PALETTE_ENEMYFIRE_POS = 3;
	private static final int PALETTE_TETRIS_POS = 11;
	private static final int PALETTE_MISSILE_HEAD_POS = 5;
	private static final int PALETTE_MISSILE_HEAD2_POS = 63;
	private static final int PALETTE_MISSILE_PATH_POS = 3;
	private static final int PALETTE_MISSILE_EXPLODE_POS = 63;
	private static final int PALETTE_MISSILE_EXPLODE_BASE_POS = 58;
	private static final int PALETTE_MISSILE_EXPLODE_DELTA = PALETTE_MISSILE_EXPLODE_POS - PALETTE_MISSILE_EXPLODE_BASE_POS;

	// Generic definition of graphical objects
	private static final int GR_BLOCK_WIDTH = 4;
	private static final int GR_BLOCK_HEIGHT = 4;
	private static final int GR_SCREEN_BLOCK_WIDTH = RENDER_WIDTH / GR_BLOCK_WIDTH;
	private static final int GR_SCREEN_BLOCK_HEIGHT = RENDER_HEIGHT / GR_BLOCK_HEIGHT;

	private static final int GR_PADDLE_BLOCK_WIDTH = 8;

	private static final int DR_NET_WIDTH = 5;
	private static final int DR_NET_HEIGHT = 2;
	private static final int DR_NET_SPACING = 6;
	private static final int DR_NET_SEP = DR_NET_WIDTH + DR_NET_SPACING;
	private static final int DR_NET_Y = RENDER_HEIGHT / 2;

	private static final int SCORE_PLAYER_X = GR_BLOCK_WIDTH;
	private static final int SCORE_PLAYER_Y = DR_NET_Y + GR_BLOCK_HEIGHT * 4;
	private static final int SCORE_COMP_X = GR_BLOCK_WIDTH;
	private static final int SCORE_COMP_Y = DR_NET_Y - GR_BLOCK_HEIGHT - 2;

	private static final int BASE_EVENT_MOD = 1;
	// start game by only showing walls and breakout
	private static final int MAX_EVENT_MOD = 10;

	private static final int REFLECT_SPECIAL_BITS = 0x03;
	private static final int SPECIAL_COMPUTE_EQUALS = 3;
	private static final int REFLECT_SPECIAL_SHR = 3;

	// GAME OBJECT CONSTANTS
	//     INDEX: relative index or position
	//     POS: absolute position in the array.

	private static final int OBJECT_TYPE_WALL_INDEX = 0;

	private static final int OBJECT_TYPE_BREAKOUT_INDEX = 1;
	private static final int OBJECT_TYPE_INVADER1_INDEX = 2;
	private static final int OBJECT_TYPE_INVADER2_INDEX = 3;
	private static final int OBJECT_TYPE_NPADDLE_INDEX = 4;
	private static final int OBJECT_TYPE_SPADDLE_INDEX = 5;
	private static final int OBJECT_TYPE_BALL_INDEX = 6;

	private static final int OBJECT_TYPE_MISSILE_HEAD_INDEX = 7;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_1_INDEX = 8;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_2_INDEX = 9;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_3_INDEX = 10;
	private static final int OBJECT_TYPE_ENEMYFIRE_INDEX = 11;

	// tetris objects: one for each type
	private static final int OBJECT_TYPE_TETRIS_LHOOK_INDEX = 12;
	//    X
	//    X
	//   XX

	private static final int OBJECT_TYPE_TETRIS_RHOOK_INDEX = 13;
	//    X
	//    X
	//    XX

	private static final int OBJECT_TYPE_TETRIS_BAR_INDEX = 14;
	//    XXXX

	private static final int OBJECT_TYPE_TETRIS_BOX_INDEX = 15;
	//    XX
	//    XX

	private static final int OBJECT_TYPE_TETRIS_BUMP_INDEX = 16;
	//     X
	//    XXX

	private static final int OBJECT_TYPE_COUNT = 17;

	// -----------------------------------------------------------------------
	// OBJECT_TYPE list pointers and record indices

	// OBJECT_TYPE is a list that contains one record for each object type.
	// The construction of instances in OBJECT_LIST comes directly from
	// OBJECT_TYPE.

	// Images are packed with 1 bit == pixel, 8 bits per index.

	// Each record in the OBJECT_TYPE array.
	// FUTURE ME: Keeping this small helps reduce
	// the string data size, but reducing 1 byte here for the added complexity
	// of harder decoding saves only 13 bytes (uncompressed) data, and more
	// bytecode for a single special case.  Reduce this size (bit packing) only
	// as a last resort. Also note that adding a few bytes here can create
	// a centralized loop object that can compress lots of special case logic
	// down to a smaller bytecode.
	private static final int OBJECT_TYPE_RECORD_FLAG_INDEX = 0;
	private static final int OBJECT_TYPE_RECORD_COLOR_INDEX = 1;
	private static final int OBJECT_TYPE_RECORD_WIDTH_INDEX = 2;
	private static final int OBJECT_TYPE_RECORD_HEIGHT_INDEX = 3;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_VCOUNT_INDEX = 4;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_HCOUNT_INDEX = 5;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_VINDENT_INDEX = 6;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_HINDENT_INDEX = 7;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_VSPACE_INDEX = 8;
	private static final int OBJECT_TYPE_RECORD_PREPOPULATE_HSPACE_INDEX = 9;
	private static final int OBJECT_TYPE_RECORD_IMAGE_INDEX = 10;

	private static final int IMAGE_BITS_PER_POS = 8;
	private static final int MISSILE_EXPLODE_WIDTH = 16; // biggest image dimensions
	private static final int MISSILE_EXPLODE_HEIGHT = 12;
	private static final int OBJECT_TYPE_RECORD_MAX_IMAGE_SIZE = (MISSILE_EXPLODE_WIDTH * MISSILE_EXPLODE_WIDTH) / IMAGE_BITS_PER_POS;
	// max # of bytes that can contain the image
	// 8x8 is biggest.

	private static final int OBJECT_TYPE_RECORD_IMAGE_SIZE_UNPACKED = OBJECT_TYPE_RECORD_MAX_IMAGE_SIZE * IMAGE_BITS_PER_POS;

	private static final int OBJECT_TYPE_RECORD_SIZE = OBJECT_TYPE_RECORD_IMAGE_INDEX + OBJECT_TYPE_RECORD_MAX_IMAGE_SIZE;
	private static final int OBJECT_TYPE_SIZE = OBJECT_TYPE_RECORD_SIZE * OBJECT_TYPE_COUNT + 1;

	// Positions of the types in the OBJECT_TYPE array.

	private static final int OBJECT_TYPE_WALL_POS = 0;
	private static final int OBJECT_TYPE_BREAKOUT_POS = OBJECT_TYPE_WALL_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_INVADER1_POS = OBJECT_TYPE_BREAKOUT_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_INVADER2_POS = OBJECT_TYPE_INVADER1_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_NPADDLE_POS = OBJECT_TYPE_INVADER2_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_SPADDLE_POS = OBJECT_TYPE_NPADDLE_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_BALL_POS = OBJECT_TYPE_SPADDLE_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_MISSILE_HEAD_POS = OBJECT_TYPE_BALL_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_1_POS = OBJECT_TYPE_MISSILE_HEAD_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_2_POS = OBJECT_TYPE_MISSILE_EXPLODE_1_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_MISSILE_EXPLODE_3_POS = OBJECT_TYPE_MISSILE_EXPLODE_2_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_ENEMYFIRE_POS = OBJECT_TYPE_MISSILE_EXPLODE_3_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_TETRIS_LHOOK_POS = OBJECT_TYPE_ENEMYFIRE_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_TETRIS_RHOOK_POS = OBJECT_TYPE_TETRIS_LHOOK_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_TETRIS_BAR_POS = OBJECT_TYPE_TETRIS_RHOOK_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_TETRIS_BOX_POS = OBJECT_TYPE_TETRIS_BAR_POS + OBJECT_TYPE_RECORD_SIZE;
	private static final int OBJECT_TYPE_TETRIS_BUMP_POS = OBJECT_TYPE_TETRIS_BOX_POS + OBJECT_TYPE_RECORD_SIZE;

	// -----------------------------------------------------------------------
	// OBJECT_INST list pointers and record indices

	// OBJECT_INST array does double duty.  The first part of the list contains
	// all object instances.  The second half contains the images for those
	// instances.  Because the object array is pre-allocated with image pointers
	// to the image space, the image location doesn't need to be recalculated.
	// This also allows the tetris paddle object to be the last image and take
	// up a large amount of space.
	// Image is copied into the instance data so that it can be destroyed
	// independent of the other similar objects

	// Because the first objects are static, we can create constant pointers
	// to them.

	// Image data has some extra unused data at the end (beyond height * width)
	// so this can be used by types for extra data.

	// Image definition goes from left to right, bottom to top, so Y indicates
	// the BOTTOM of the image.
	private static final int OBJECT_INST_RECORD_VISIBLE_INDEX = 0;
	private static final int OBJECT_INST_RECORD_IMAGE_POS_INDEX = 1;
	private static final int OBJECT_INST_RECORD_X_INDEX = 2;
	private static final int OBJECT_INST_RECORD_Y_INDEX = 3;
	private static final int OBJECT_INST_RECORD_NEEDSCOPY_INDEX = 4;
	private static final int OBJECT_INST_RECORD_FLAG_INDEX = 5;
	private static final int OBJECT_INST_RECORD_TYPE_INDEX = 6;
	private static final int OBJECT_INST_RECORD_SIZE = 7;

	// A descent number; we need 256 objects for the walls, 30 objects for
	// breakout, 20 objects for invaders.  This leaves us with a good number.
	private static final int OBJECT_INST_RECORD_COUNT = 800;
	private static final int MAX_OBJECT_INST_RECORDS = OBJECT_INST_RECORD_COUNT - 1;

	private static final int OBJECT_INST_IMAGE_START_POS = OBJECT_INST_RECORD_COUNT * OBJECT_INST_RECORD_SIZE;

	private static final int OBJECT_INST_IMAGE_SIZE = RENDER_WIDTH * 2; // MUST be bigger than 8x8x2+1 = 128, which this is.
	//OBJECT_TYPE_RECORD_IMAGE_SIZE_UNPACKED;

	private static final int OBJECT_INST_IMAGE_BASE_START_POS = OBJECT_INST_IMAGE_START_POS;

	private static final int OBJECT_INST_IMAGEPART_SIZE = (OBJECT_INST_IMAGE_SIZE * (OBJECT_INST_RECORD_COUNT));
	// all the objects

	private static final int OBJECT_INST_SIZE = OBJECT_INST_IMAGE_START_POS + OBJECT_INST_IMAGEPART_SIZE;

	// Needs Copy value
	private static final int NEEDSCOPY_NOOP = 0;
	private static final int NEEDSCOPY_COPY = 1;

	// Visible value
	private static final int VISIBLE_NO = 0;
	private static final int VISIBLE_YES = 1;
	private static final int VISIBLE_DESTROY = 2;

	// GAME OBJECT FLAGS
	//    Flags are shr by the given value, and masked with 1 to determine
	//    if they're active.  This needs to be checked for size against
	//    a straight mask != 0 (straight mask can introduce the number into
	//    the constant pool; if the number is used in other places, that will
	//    actually be smaller).
	//    The differences are FLAGSHR_ for the shift count, and FLAGMSK_ for
	//    the mask value.
	//    Two types of destruction: totally destroy (DESTORY) or remove
	//    a piece of the block (DENT).

	// WALL - ball bounces off it, can't be destroyed or damaged.  Dents things
	// it touches, but doesn't destroy things.  Tetris stops falling on it.
	// Moves (with special bottom-of-screen logic).
	// BREAKOUT - ball bounces off it, can be destroyed, not dentable,
	// Doesn't destroy.  Dents objects.  Tetris stops falling on it.
	// Doesn't move.
	// GALAGA & INVADER? - ball bounces off it, can be destroyed, not dentable,
	// does not destroy objects. Dents objects it touches.  Moves (special
	// logic to change motion at certain intervals).  Does not stop Tetris?
	// NPADDLE - ball bounces off it.  Can't be destroyed or dented.  All
	// things pass through it.  Doesn't harm other things.  Should hanging
	// Tetris be dented by it?  No.  They just cover up the NPADDLE.
	// Special AI logic for movement (so don't consider it as "moves")
	// SPADDLE - ball bounces off it.  Not destroyable, but dentable by
	// all things that dent EXCEPT the ball, does not harm other things,
	// Tetris blocks hang on it - special logic here to put those hanging
	// tetris pieces into a new object so they move with the paddle.
	// User moves it (so don't consider it as "moves")
	// TPADDLE - ball bounces off it.  Not destroyable, is dentable, doesn't
	// harm other objects.  Tetris objects hang on it.
	// Moves with the user, so don't consider it as "moves".
	// BALL - destroys and dents things (EXCEPT the SPADDLE).  Moves (movement
	// is altered by interaction on a special case).  Does not stop Tetris.
	// MISSILE PATH & TRACTORBEAM - nothing hangs on it, can't be destroyed,
	// doesn't harm things.  Moves with special rules (each one has different
	// movement rules).
	// CAPTURED PADDLE - Acts just like SPADDLE, except that tetris objects
	// don't move with it.  Movement pattern matches the owning GALAGA
	// (so "moves").
	// MISSILE HEAD - destroys and dents things (special detonate logic),
	// can't be destroyed or dented, tetris pass through it.  Moves.
	// ENEMY FIRE & MISSILE EXPLODE - destroys and dents things, can't be
	// destroyed or dented, tetris "pass" through it (are actually destroyed by
	// it).  Enemy Fire moves, explode doesn't.
	// TETRIS_* - Ball bounces off it, not destroyable, dentable, doesn't harm
	// other things. special logic for being stopped by things.  Moves by blocks.
	// Stops tetris blocks

	// Motion seems custom enough for each object that it should be done without
	// flags, and instead on a case-by-case basis.

	// Single objects with special logic - flags aren't necessary for these,
	// because it's a type index check.
	//    TETRIS - things stop its fall
	//    SPADDLE - ball doesn't dent it, but it's dentable
	// Common behaviors
	//    ball bounces off it (x7)
	//    destroyable (x2) - could be special case, especially since
	//        destroyable means not dentable.
	//    dentable (x4)
	//    destroys (x4)
	//    dents (x7)
	//    catches tetris (x4 or x5)

	// Is this object destroyable by a destroy-cause object?
	private static final int FLAGMSK_DESTROYABLE = 0x0001;
	private static final int FLAGSHR_DESTROYABLE = 0;

	// If the object collides with an object, does it remove part of its
	// image?
	private static final int FLAGMSK_CAUSES_DENTS = 0x0002;
	private static final int FLAGSHR_CAUSES_DENTS = 1;

	// If the object collides with the ball, does it dent the object?
	private static final int FLAGMSK_CAUSES_DESTROY = 0x0004;
	private static final int FLAGSHR_CAUSES_DESTROY = 2;

	// If the object collides with a dent-cause object, does it dent the object?
	private static final int FLAGMSK_DENTABLE = 0x0008;
	private static final int FLAGSHR_DENTABLE = 3;

	// Does the object reflect the ball?
	private static final int FLAGMSK_REFLECTS_BALL = 0x0010;
	private static final int FLAGSHR_REFLECTS_BALL = 4;

	// Does the object stop falling objects?  If this is set, it also
	// reflects the ball.
	private static final int FLAGMSK_STOPS_TETRIS = 0x0020;
	private static final int FLAGSHR_STOPS_TETRIS = 5;

	// Is this one of the tetris objects?  Used for checking if the object
	// needs to join TPADDLE
	private static final int FLAGMSK_IS_TETRIS = 0x0040;
	private static final int FLAGSHR_IS_TETRIS = 6;

	// Pre-computed mask combinations
	private static final int WALL_FLAGS = FLAGMSK_REFLECTS_BALL | FLAGMSK_CAUSES_DENTS;
	private static final int BALL_STICKY_FLAGS = 0;
	private static final int COLLIDES_FLAGS = FLAGMSK_CAUSES_DENTS | FLAGMSK_CAUSES_DESTROY | FLAGMSK_IS_TETRIS;

	// ------------------------------------------------------------------------
	// WALL OBJECT CONSTANTS
	//   A wall is a single row, with a break in the middle.  For the non-tunnel
	//   mode, it's off-screen right at the edges to keep the ball inside the
	//   game field.
	private static final int WALL_BLOCK_HEIGHT = 1;
	private static final int WALL_SEGMENT_HEIGHT = WALL_BLOCK_HEIGHT * GR_BLOCK_HEIGHT;
	private static final int WALL_SEGMENT_MIN_BLOCK_WIDTH = 1;
	private static final int WALL_SEGMENT_MIN_WIDTH = WALL_SEGMENT_MIN_BLOCK_WIDTH * GR_BLOCK_WIDTH;
	private static final int WALL_IMAGE_BLOCK_WIDTH = GR_SCREEN_BLOCK_WIDTH + 2;
	// + 2 for the blocks off the screen.
	private static final int WALL_IMAGE_LBLOCK_INDEX = 0;
	private static final int WALL_IMAGE_RBLOCK_INDEX = WALL_IMAGE_BLOCK_WIDTH - 1;
	private static final int WALL_SEGMENT_MAX_BLOCK_WIDTH = WALL_IMAGE_BLOCK_WIDTH - GR_PADDLE_BLOCK_WIDTH - 2;
	private static final int WALL_VISIBLE_COUNT = RENDER_HEIGHT / WALL_SEGMENT_HEIGHT;
	// the wall moving can stretch for 3 times the visible height.
	private static final int WALL_TOTAL_COUNT = WALL_VISIBLE_COUNT + 1;
	private static final int WALL_START_Y = RENDER_HEIGHT + WALL_SEGMENT_HEIGHT;
	// images start at the bottom
	private static final int WALL_END_Y = WALL_START_Y - (WALL_TOTAL_COUNT * WALL_SEGMENT_HEIGHT);
	private static final int WALL_START_X = 0 - WALL_SEGMENT_MIN_WIDTH;
	private static final int MIN_WALL_LEFT = 1;
	private static final int MAX_WALL_LEFT = GR_SCREEN_BLOCK_WIDTH - GR_PADDLE_BLOCK_WIDTH;
	private static final int MIN_WALL_RIGHT = GR_PADDLE_BLOCK_WIDTH;
	private static final int MAX_WALL_RIGHT = GR_SCREEN_BLOCK_WIDTH;

	private static final int WALL_MOVEMENT_1 = GR_SCREEN_BLOCK_WIDTH >> 1;
	private static final int WALL_MOVEMENT_2 = GR_PADDLE_BLOCK_WIDTH << 1;

	private static final int WALL_ADD_COUNT_1 = 12 * GR_BLOCK_HEIGHT;
	private static final int WALL_ADD_COUNT_2 = 24 * GR_BLOCK_HEIGHT;

	// Positions in the image data section that contains extra data for the
	// wall segment
	private static final int WALL_IMAGE_DATA_START_IPOS = WALL_IMAGE_BLOCK_WIDTH * WALL_BLOCK_HEIGHT;
	private static final int WALL_IMAGE_DATA_LBREAK_INDEX = 0;
	private static final int WALL_IMAGE_DATA_LBREAK_IPOS = WALL_IMAGE_DATA_START_IPOS + WALL_IMAGE_DATA_LBREAK_INDEX;
	private static final int WALL_IMAGE_DATA_RBREAK_INDEX = 1;
	private static final int WALL_IMAGE_DATA_RBREAK_IPOS = WALL_IMAGE_DATA_START_IPOS + WALL_IMAGE_DATA_RBREAK_INDEX;

	private static final int PALETTE_WALL_START = 48;
	private static final int PALETTE_WALL_END = 60;
	private static final int WALL_PALETTE_DELTA = (PALETTE_WALL_END - PALETTE_WALL_START);

	private static final int WALL_TYPE_WIDTH_POS = OBJECT_TYPE_WALL_POS + OBJECT_TYPE_RECORD_WIDTH_INDEX;
	private static final int WALL_TYPE_HEIGHT_POS = OBJECT_TYPE_WALL_POS + OBJECT_TYPE_RECORD_HEIGHT_INDEX;

	private static final int WALL_OBJECT_INST_START_INDEX = 0;
	private static final int WALL_OBJECT_INST_START_POS = WALL_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;

	// ------------------------------------------------------------------------
	// BREAKOUT OBJECT CONSTANTS
	private static final int BREAKOUT_BLOCK_HEIGHT = 2;
	private static final int BREAKOUT_BLOCK_WIDTH = 8;

	private static final int BREAKOUT_BLOCK_H_SPACING = 1;
	private static final int BREAKOUT_BLOCK_V_SPACING = 1;

	private static final int BREAKOUT_BLOCK_ACTUAL_WIDTH = BREAKOUT_BLOCK_WIDTH + BREAKOUT_BLOCK_H_SPACING;
	private static final int BREAKOUT_BLOCK_ACTUAL_HEIGHT = BREAKOUT_BLOCK_HEIGHT + BREAKOUT_BLOCK_V_SPACING;

	private static final int BREAKOUT_BLOCK_H_COUNT = GR_SCREEN_BLOCK_WIDTH / BREAKOUT_BLOCK_ACTUAL_WIDTH;
	private static final int BREAKOUT_BLOCK_V_COUNT = GR_SCREEN_BLOCK_HEIGHT / 5;

	private static final int BREAKCOUT_COUNT = BREAKOUT_BLOCK_H_COUNT * BREAKOUT_BLOCK_V_COUNT;

	private static final int BREAKOUT_ROW_OBJECT_INST_SIZE = BREAKOUT_BLOCK_H_COUNT * OBJECT_INST_RECORD_SIZE;

	// starts below the NPADDLE
	private static final int BREAKOUT_BLOCK_V_OFFSET = 6;

	private static final int BREAKOUT_BLOCK_H_OFFSET = (GR_SCREEN_BLOCK_WIDTH - (BREAKOUT_BLOCK_ACTUAL_WIDTH * BREAKOUT_BLOCK_H_COUNT)) / 2;

	private static final int BREAKOUT_OBJECT_INST_START_INDEX = WALL_OBJECT_INST_START_INDEX + WALL_TOTAL_COUNT;
	private static final int BREAKOUT_OBJECT_INST_START_POS = BREAKOUT_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;

	// ------------------------------------------------------------------------
	// INVADER CONSTANTS
	private static final int INVADER_BLOCK_HEIGHT = 4;
	private static final int INVADER_BLOCK_WIDTH = 4;
	private static final int INVADER_BLOCK_H_SPACING = 4 + INVADER_BLOCK_WIDTH;
	private static final int INVADER_BLOCK_V_SPACING = 4 + INVADER_BLOCK_HEIGHT;
	private static final int INVADER_BLOCK_H_COUNT = 6;
	private static final int INVADER1_BLOCK_V_COUNT = 1;
	private static final int INVADER1_BLOCK_V_OFFSET = 20; // A Guess
	private static final int INVADER1_BLOCK_H_OFFSET = INVADER_BLOCK_WIDTH;

	private static final int INVADER2_BLOCK_V_COUNT = 1;
	private static final int INVADER2_BLOCK_V_OFFSET = INVADER1_BLOCK_V_OFFSET + ((INVADER_BLOCK_V_SPACING + INVADER_BLOCK_HEIGHT) * INVADER1_BLOCK_V_COUNT);
	private static final int INVADER2_BLOCK_H_OFFSET = INVADER1_BLOCK_H_OFFSET + GR_BLOCK_WIDTH;

	private static final int INVADER_CHANGE_IMAGE_MOD = FRAMES_PER_SECOND;

	private static final int INVADER_FRAMES_PER_MOVE = 4;
	private static final int INVADER_FRAMES_PER_MOVE_SHR = 2;
	private static final int INVADER_MAX_H_MOVEMENT = 12;
	private static final int INVADER_MAX_V_MOVEMENT = 4;
	private static final int INVADER_HL_MOVEMENT_START = 0;
	private static final int INVADER_DOWN_MOVEMENT_START = INVADER_HL_MOVEMENT_START + INVADER_MAX_H_MOVEMENT;
	private static final int INVADER_RIGHT_MOVEMENT_START = INVADER_DOWN_MOVEMENT_START + INVADER_MAX_V_MOVEMENT;
	private static final int INVADER_UP_MOVEMENT_START = INVADER_RIGHT_MOVEMENT_START + INVADER_MAX_H_MOVEMENT;
	private static final int INVADER_FRAME_MOVE_MOD = INVADER_UP_MOVEMENT_START + INVADER_MAX_V_MOVEMENT;

	private static final int INVADER_BULLET_XDELTA = (INVADER_BLOCK_WIDTH * GR_BLOCK_WIDTH) / 2;
	private static final int INVADER_FIRE_FREQUENCY = RENDER_HEIGHT;

	private static final int INVADER_OBJECT_INST_START_INDEX = BREAKOUT_OBJECT_INST_START_INDEX + BREAKCOUT_COUNT;
	private static final int INVADER_OBJECT_INST_START_POS = INVADER_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;

	private static final int INVADER_IMAGE_DATA_START_IPOS = INVADER_BLOCK_HEIGHT * INVADER_BLOCK_WIDTH;
	private static final int INVADER_DATA_BASE_X_IPOS = INVADER_IMAGE_DATA_START_IPOS;
	private static final int INVADER_DATA_BASE_Y_IPOS = INVADER_DATA_BASE_X_IPOS + 1;
	private static final int INVADER_DATA_FIRE_IPOS = INVADER_DATA_BASE_Y_IPOS + 1;

	private static final int INVADER_COUNT = (INVADER_BLOCK_H_COUNT * INVADER1_BLOCK_V_COUNT) + (INVADER_BLOCK_H_COUNT * INVADER2_BLOCK_V_COUNT);

	// ------------------------------------------------------------------------
	// NPADDLE CONSTANTS
	private static final int NPADDLE_BASE_SPEED = 3;
	private static final int NPADDLE_MIN_SPEED = 2;
	private static final int NPADDLE_MAX_SPEED = 8;
	private static final int NPADDLE_BLOCK_HEIGHT = 1;
	private static final int NPADDLE_BLOCK_WIDTH = GR_PADDLE_BLOCK_WIDTH;
	private static final int NPADDLE_WIDTH = NPADDLE_BLOCK_WIDTH * GR_BLOCK_WIDTH;
	private static final int NPADDLE_BLOCK_STARTX = (GR_SCREEN_BLOCK_WIDTH - NPADDLE_BLOCK_WIDTH) / 2;
	private static final int NPADDLE_BLOCK_Y = 1;
	private static final int NPADDLE_Y = NPADDLE_BLOCK_Y * NPADDLE_BLOCK_HEIGHT;

	private static final int NPADDLE_COUNT = 1;

	private static final int NPADDLE_OBJECT_INST_START_INDEX = INVADER_OBJECT_INST_START_INDEX + INVADER_COUNT;
	private static final int NPADDLE_OBJECT_INST_START_POS = NPADDLE_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;
	private static final int NPADDLE_OBJECT_INST_VISIBLE_POS = NPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_VISIBLE_INDEX;
	private static final int NPADDLE_OBJECT_INST_X_POS = NPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_X_INDEX;
	private static final int NPADDLE_OBJECT_INST_Y_POS = NPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_Y_INDEX;

	private static final int NPADDLE_OBJECT_INST_IMAGE_POS_POS = NPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_IMAGE_POS_INDEX;

	private static final int NPADDLE_IMAGE_DATA_START_IPOS = NPADDLE_BLOCK_HEIGHT * NPADDLE_BLOCK_WIDTH;
	private static final int NPADDLE_IMAGE_DATA_WALL_LEFT_IPOS = NPADDLE_IMAGE_DATA_START_IPOS; // left X position of the wall on the
	// same Y position as the NPADDLE.
	private static final int NPADDLE_IMAGE_DATA_WALL_RIGHT_IPOS = NPADDLE_IMAGE_DATA_START_IPOS + 1; // right X position of the wall on the
	// same Y position as the NPADDLE.

	// ------------------------------------------------------------------------
	// SPADDLE CONSTANTS
	private static final int SPADDLE_BLOCK_HEIGHT = 1;
	private static final int SPADDLE_BLOCK_WIDTH = GR_PADDLE_BLOCK_WIDTH;
	private static final int SPADDLE_WIDTH = SPADDLE_BLOCK_WIDTH * GR_BLOCK_WIDTH;
	private static final int SPADDLE_BLOCK_START_X = (GR_SCREEN_BLOCK_WIDTH - SPADDLE_BLOCK_WIDTH) / 2;
	private static final int SPADDLE_BLOCK_Y = GR_SCREEN_BLOCK_HEIGHT;
	private static final int SPADDLE_Y = SPADDLE_BLOCK_Y * GR_BLOCK_HEIGHT;
	private static final int SPADDLE_MIN_X = 0;
	private static final int SPADDLE_MAX_X = RENDER_WIDTH - GR_BLOCK_WIDTH;
	private static final int SPADDLE_MOUSE_OFFSET = SPADDLE_WIDTH / 2;
	private static final int SPADDLE_COUNT = 1;

	private static final int SPADDLE_OBJECT_INST_START_INDEX = NPADDLE_OBJECT_INST_START_INDEX + NPADDLE_COUNT;
	private static final int SPADDLE_OBJECT_INST_START_POS = SPADDLE_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;
	private static final int SPADDLE_OBJECT_INST_VISIBLE_POS = SPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_VISIBLE_INDEX;
	private static final int SPADDLE_OBJECT_INST_X_POS = SPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_X_INDEX;
	private static final int SPADDLE_OBJECT_INST_Y_POS = SPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_Y_INDEX;
	//private static final int SPADDLE_OBJECT_INST_IMAGE_POS_POS =
	//    SPADDLE_OBJECT_INST_START_POS + OBJECT_INST_RECORD_IMAGE_POS_INDEX;

	// Positions in the image data section that contains extra data for the
	// wall segment
	private static final int SPADDLE_IMAGE_DATA_START_IPOS = SPADDLE_BLOCK_WIDTH * SPADDLE_BLOCK_HEIGHT;

	// ------------------------------------------------------------------------
	// BALL CONSTANTS
	private static final int BALL_BLOCK_HEIGHT = 1;
	private static final int BALL_BLOCK_WIDTH = 1;
	private static final int BALL_HEIGHT = BALL_BLOCK_HEIGHT * GR_BLOCK_HEIGHT;

	// These start values don't actually matter, because with the "sticky" flag,
	// the ball starts on the paddle.
	private static final int BALL_BLOCK_START_X = SPADDLE_BLOCK_START_X + 2;
	private static final int BALL_BLOCK_START_Y = SPADDLE_BLOCK_Y - 1;
	private static final int BALL_STICKY_Y = SPADDLE_Y - 1;
	private static final int BALL_STICKY_X_OFFSET = GR_BLOCK_WIDTH;
	private static final int BALL_Y = BALL_BLOCK_START_Y * BALL_BLOCK_HEIGHT;

	private static final int BALL_N_BOUND = 0;
	private static final int BALL_S_BOUND = RENDER_HEIGHT + GR_BLOCK_HEIGHT;

	// movement is a SHL of the actual pixel movement, to allow
	// for decimal points
	private static final int BALL_MOVEMENT_DECODE_SHR = 4;
	private static final int BALL_MOVEMENT_ENCODE_SHL = 4;

	private static final int BALL_START_DX = 1 << BALL_MOVEMENT_ENCODE_SHL;
	private static final int BALL_START_DY = -3 << BALL_MOVEMENT_ENCODE_SHL;

	private static final int BALL_MIN_X_SPEED = 1 << BALL_MOVEMENT_ENCODE_SHL;
	private static final int BALL_MIN_Y_SPEED = (1 << BALL_MOVEMENT_ENCODE_SHL) + (3 << (BALL_MOVEMENT_ENCODE_SHL - 1));
	private static final int BALL_MAX_X_SPEED = (GR_BLOCK_WIDTH << BALL_MOVEMENT_ENCODE_SHL);
	private static final int BALL_MAX_Y_SPEED = (GR_BLOCK_HEIGHT << BALL_MOVEMENT_ENCODE_SHL) + (GR_BLOCK_HEIGHT << (BALL_MOVEMENT_ENCODE_SHL - 1));

	private static final int BALL_COUNT = 1;

	private static final int BALL_OBJECT_INST_START_INDEX = SPADDLE_OBJECT_INST_START_INDEX + SPADDLE_COUNT;
	private static final int BALL_OBJECT_INST_START_POS = BALL_OBJECT_INST_START_INDEX * OBJECT_INST_RECORD_SIZE;
	private static final int BALL_OBJECT_INST_VISIBLE_POS = BALL_OBJECT_INST_START_POS + OBJECT_INST_RECORD_VISIBLE_INDEX;
	private static final int BALL_OBJECT_INST_X_POS = BALL_OBJECT_INST_START_POS + OBJECT_INST_RECORD_X_INDEX;
	private static final int BALL_OBJECT_INST_Y_POS = BALL_OBJECT_INST_START_POS + OBJECT_INST_RECORD_Y_INDEX;

	private static final int BALL_OBJECT_INST_IMAGE_POS_POS = BALL_OBJECT_INST_START_POS + OBJECT_INST_RECORD_IMAGE_POS_INDEX;
	private static final int BALL_IMAGE_DATA_START_IPOS = BALL_BLOCK_HEIGHT * BALL_BLOCK_WIDTH;
	private static final int BALL_IMAGE_DATA_DX_IPOS = BALL_IMAGE_DATA_START_IPOS;
	private static final int BALL_IMAGE_DATA_DY_IPOS = BALL_IMAGE_DATA_DX_IPOS + 1;
	private static final int BALL_IMAGE_DATA_X_IPOS = BALL_IMAGE_DATA_DY_IPOS + 1;
	private static final int BALL_IMAGE_DATA_Y_IPOS = BALL_IMAGE_DATA_X_IPOS + 1;

	// -----------------------------------------------------------------------

	private static final int OBJECT_INST_FIRST_DESTROYABLE_INDEX = BALL_OBJECT_INST_START_INDEX + BALL_COUNT;
	private static final int OBJECT_INST_FIRST_DESTROYABLE_POS = OBJECT_INST_FIRST_DESTROYABLE_INDEX * OBJECT_INST_RECORD_SIZE;

	// -----------------------------------------------------------------------
	// Enemy Fire
	private static final int ENEMYFIRE_BLOCK_HEIGHT = 2;
	private static final int ENEMYFIRE_BLOCK_WIDTH = 1;
	private static final int ENEMYFIRE_HEIGHT = ENEMYFIRE_BLOCK_HEIGHT * GR_BLOCK_HEIGHT;
	private static final int ENEMYFIRE_FLAGS = FLAGMSK_CAUSES_DENTS | FLAGMSK_CAUSES_DESTROY | FLAGMSK_DESTROYABLE;
	//| FLAGMSK_DESTROYABLE | FLAGMSK_REFLECTS_BALL;

	private static final int ENEMYFIRE_MOVEMENT_Y = 3;

	// -----------------------------------------------------------------------
	// Tetris
	private static final int TETRIS_BLOCK_HEIGHT = 8;
	private static final int TETRIS_BLOCK_WIDTH = 8;
	private static final int TETRIS_WIDTH = TETRIS_BLOCK_WIDTH * GR_BLOCK_WIDTH;
	private static final int TETRIS_FLAGS = FLAGMSK_IS_TETRIS | FLAGMSK_DENTABLE | FLAGMSK_REFLECTS_BALL | FLAGMSK_STOPS_TETRIS;
	private static final int TETRIS_START_Y = NPADDLE_Y + (NPADDLE_BLOCK_HEIGHT * GR_BLOCK_HEIGHT);
	private static final int TETRIS_IMAGE_SIZE = TETRIS_BLOCK_HEIGHT * TETRIS_BLOCK_WIDTH;
	private static final int TETRIS_TYPE_COUNT = 5;

	private static final int TETRIS_MOVEMENT_MOD = FRAMES_PER_SECOND;
	private static final int TETRIS_MOVEMENT_RATE = GR_BLOCK_HEIGHT * 2;

	private static final int TETRIS_IMAGE_DATA_START_IPOS = TETRIS_IMAGE_SIZE;
	private static final int TETRIS_IMAGE_DATA_IS_STICKY_IPOS = TETRIS_IMAGE_DATA_START_IPOS;
	private static final int TETRIS_IMAGE_TEMP_DATA_IPOS = TETRIS_IMAGE_DATA_IS_STICKY_IPOS + 1;

	// -----------------------------------------------------------------------
	// Missile Head

	private static final int MISSILE_HEAD_FLAGS = 0;
	private static final int MISSILE_HEAD_WIDTH = 1;
	private static final int MISSILE_HEAD_HEIGHT = 1;
	private static final int MISSILE_HEAD_IMAGE_SIZE = MISSILE_HEAD_WIDTH * MISSILE_HEAD_HEIGHT;

	private static final int MISSILE_HEAD_EXPLODE_Y = SPADDLE_Y - (GR_BLOCK_HEIGHT * 2);

	private static final int MISSILE_MIN_DIST_X = -10;
	private static final int MISSILE_MIN_DIST_Y = -10;
	private static final int MISSILE_MAX_DIST_X = 10;
	private static final int MISSILE_MAX_DIST_Y = 10;

	private static final int MISSILE_START_Y = 0;
	private static final int MISSILE_START_Y_ENCODED = 0;
	private static final int MISSILE_START_X_COUNT = 6;
	private static final int MISSILE_START_X_MULT = RENDER_WIDTH / MISSILE_START_X_COUNT;
	private static final int MISSILE_START_X_MIDDLE = RENDER_WIDTH / 2;

	// movement is a SHL of the actual pixel movement, to allow
	// for decimal points
	private static final int MISSILE_MOVEMENT_DECODE_SHR = 8;
	private static final int MISSILE_MOVEMENT_ENCODE_SHL = 8;

	private static final int MISSILE_MAX_DX = 1 << MISSILE_MOVEMENT_ENCODE_SHL;
	private static final int MISSILE_MAX_DY = 1 << MISSILE_MOVEMENT_ENCODE_SHL;
	private static final int MISSILE_MIN_DX = 4;
	private static final int MISSILE_MIN_DY = 4;

	private static final int MISSILE_DX_MOD = MISSILE_MAX_DX - MISSILE_MIN_DX;
	private static final int MISSILE_DX_ADD = MISSILE_MIN_DX;
	private static final int MISSILE_DY_MOD = MISSILE_MAX_DY - MISSILE_MIN_DY;
	private static final int MISSILE_DY_ADD = MISSILE_MIN_DY;

	private static final int MISSILE_RANDOM_MULT = 7;

	private static final int MISSILE_HEAD_IMAGE_DATA_START_IPOS = MISSILE_HEAD_IMAGE_SIZE;
	private static final int MISSILE_HEAD_DATA_PATH_START_X_IPOS = MISSILE_HEAD_IMAGE_DATA_START_IPOS;
	private static final int MISSILE_HEAD_DATA_PATH_START_Y_IPOS = MISSILE_HEAD_DATA_PATH_START_X_IPOS + 1;
	private static final int MISSILE_HEAD_DATA_X_IPOS = MISSILE_HEAD_DATA_PATH_START_Y_IPOS + 1;
	private static final int MISSILE_HEAD_DATA_Y_IPOS = MISSILE_HEAD_DATA_X_IPOS + 1;
	private static final int MISSILE_HEAD_DATA_DX_IPOS = MISSILE_HEAD_DATA_Y_IPOS + 1;
	private static final int MISSILE_HEAD_DATA_DY_IPOS = MISSILE_HEAD_DATA_DX_IPOS + 1;

	// -----------------------------------------------------------------------
	// Missile Explode

	private static final int MISSILE_EXPLODE_TYPE_COUNT = 3;

	private static final int MISSILE_EXPLODE_FLAGS = FLAGMSK_CAUSES_DENTS | FLAGMSK_CAUSES_DESTROY;

	private static final int MISSILE_EXPLODE_MID_X = (MISSILE_EXPLODE_WIDTH * GR_BLOCK_WIDTH) / 2;
	private static final int MISSILE_EXPLODE_MID_Y = (MISSILE_EXPLODE_HEIGHT * GR_BLOCK_HEIGHT) / 2;

	private static final int MISSILE_EXPLODE_IMAGE_SIZE = MISSILE_EXPLODE_WIDTH * MISSILE_EXPLODE_HEIGHT;

	private static final int MISSILE_EXPLODE_SWITCH_IMAGE_DURATION = FRAMES_PER_SECOND / 2;

	private static final int MISSILE_EXPLODE_LIFESPAN = MISSILE_EXPLODE_SWITCH_IMAGE_DURATION * MISSILE_EXPLODE_TYPE_COUNT;

	private static final int MISSILE_EXPLODE_IMAGE_DATA_START_IPOS = MISSILE_EXPLODE_IMAGE_SIZE;
	private static final int MISSILE_EXPLODE_DATA_ALIVECOUNT_IPOS = MISSILE_EXPLODE_IMAGE_DATA_START_IPOS;
	private static final int MISSILE_EXPLODE_DATA_BASE_X_IPOS = MISSILE_EXPLODE_DATA_ALIVECOUNT_IPOS + 1;
	private static final int MISSILE_EXPLODE_DATA_BASE_Y_IPOS = MISSILE_EXPLODE_DATA_BASE_X_IPOS + 1;

	private static final int MISSILE_EXPLODE_1_TYPE_COLOR_POS = OBJECT_TYPE_MISSILE_EXPLODE_1_POS + OBJECT_TYPE_RECORD_COLOR_INDEX;
	private static final int MISSILE_EXPLODE_2_TYPE_COLOR_POS = OBJECT_TYPE_MISSILE_EXPLODE_2_POS + OBJECT_TYPE_RECORD_COLOR_INDEX;
	private static final int MISSILE_EXPLODE_3_TYPE_COLOR_POS = OBJECT_TYPE_MISSILE_EXPLODE_3_POS + OBJECT_TYPE_RECORD_COLOR_INDEX;
}
