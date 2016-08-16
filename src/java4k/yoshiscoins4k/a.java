package java4k.yoshiscoins4k;

/*
 * Yoshi's Coins 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Yoshi's Coins 4K.
 *
 * Yoshi's Coins 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Yoshi's Coins 4K is distributed in the hope that it will be useful,
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
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class a extends Applet implements Runnable {

	private int[] a = new int[3];

	private static final int STONE_EMPTY = 0;
	private static final int STONE_X = 1;
	private static final int STONE_O = -1;

	private static final int NODE_INTERMEDIATE = 0;
	private static final int NODE_X_WINS = 1;
	private static final int NODE_O_WINS = 2;
	private static final int NODE_TIE = 3;

	private static final int INFINITY = 1000000;

	private static final int[][] scans = { { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 } };
	private static final int[][] W = new int[6][7];

	private static final BufferedImage I = new BufferedImage(182, 232, 1);

	@Override
	public void start() {
		new Thread(this).start();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(I, 0, 0, 364, 464, null);
	}

	// NegaMax
	//
	// board[y : 8][x : 7]
	// rows board[0] -- board[5] contain the stones
	// row board[6] contains stack pointers
	//     the stack pointers point to the next available space
	//     stacks are intially empty with pointer set to row y = 5
	//     a stack pointer of y = -1 indicates the stack is full
	// row board[7][x > 0] valid only immediately after a call to makeMove()
	//     board[7][0] contains the heuristic value of the board
	//     board[7][1] contains the node type
	//     (board[7][2],board[7][3])-(board[7][4],board[7][5]) win end points
	private void n(int[][] board, int[][] sorts, int depth, int stone, int alpha, int beta, int maxDepth, int[][] permutations, int[] result) {

		int max = -2 * INFINITY;
		int bestColumn = -1;
		int freeColumns = 0;
		for (int i = 0; i < 7; i++) {
			sorts[depth][i] = 0;
			if (board[6][i] >= 0) {
				freeColumns++;
			}
		}
		for (int i = 0; i < 7; i++) {
			int largestValue = -1;
			int largestColumn = -1;
			for (int j = 0; j < 7; j++) {
				int column = permutations[depth][j];
				if (sorts[depth][column] == 0 && board[6][column] >= 0 && largestValue < W[board[6][column]][column]) {
					largestValue = W[board[6][column]][column];
					largestColumn = column;
				}
			}
			if (largestColumn < 0) {
				break;
			}
			sorts[depth][largestColumn] = 1;

			m(board, largestColumn, stone);
			int x = 0;
			if (board[7][1] == NODE_INTERMEDIATE) {
				if (depth == maxDepth && freeColumns > 1) {
					x = stone * board[7][0];
				} else {
					n(board, sorts, depth + 1, -stone, -beta, -alpha, maxDepth, permutations, result);
					x = -result[0];
				}
			} else if (board[7][1] == NODE_TIE) {
				x = 0;
			} else {
				x = INFINITY - (depth << 5);
				board[++board[6][largestColumn]][largestColumn] = STONE_EMPTY;
				board[7][0] -= stone * W[board[6][largestColumn]][largestColumn];

				for (int j = 0; j < 7; j++) {
					if (sorts[depth][j] == 0 && j != largestColumn && board[6][j] >= 0) {
						m(board, j, stone);
						if (board[7][1] == NODE_INTERMEDIATE || board[7][1] == NODE_TIE) {
							x--;
						}
						board[++board[6][j]][j] = STONE_EMPTY;
						board[7][0] -= stone * W[board[6][j]][j];
					}
				}

				max = x;
				bestColumn = largestColumn;
				break;
			}
			board[++board[6][largestColumn]][largestColumn] = STONE_EMPTY;
			board[7][0] -= stone * W[board[6][largestColumn]][largestColumn];

			if (x > max) {
				max = x;
				bestColumn = largestColumn;
			}

			if (x > alpha) {
				alpha = x;
			}

			if (alpha >= beta) {
				break;
			}
		}

		result[0] = max;
		result[1] = bestColumn;
	}

	// makeMove
	private void m(int[][] board, int column, int stone) {

		int y = board[6][column]--;

		board[7][0] += stone * W[y][column];
		board[y][column] = stone;

		for (int i = 0; i < 4; i++) {
			int vx = scans[i][0];
			int vy = scans[i][1];
			int count = 0;

			for (int j = 0; j < 2; j++) {
				int m = j == 0 ? -1 : 1;
				board[7][2 + (j << 1)] = column;
				board[7][3 + (j << 1)] = y;
				for (int k = 1; k < 4; k++) {
					int X = column + m * k * vx;
					int Y = y + m * k * vy;
					if (X < 0 || Y < 0 || X > 6 || Y > 5 || board[Y][X] != stone) {
						break;
					}
					count++;
					board[7][2 + (j << 1)] = X;
					board[7][3 + (j << 1)] = Y;
				}
			}

			if (count > 2) {
				board[7][1] = stone == STONE_X ? NODE_X_WINS : NODE_O_WINS;
				return;
			}
		}

		for (int i = 0; i < 7; i++) {
			if (board[6][i] >= 0) {
				board[7][1] = NODE_INTERMEDIATE;
				return;
			}
		}

		board[7][1] = NODE_TIE;
	}

	public void run() {

		final float GRAVITY = 0.5f;
		final float COIN_JUMP_VY = -0.8f;
		final float COIN_JUMP_GRAVITY = 0.05f;

		final int MOUSE_X = 0;
		final int MOUSE_PRESSED = 1;

		final int STATE_X_CHOOSING = 0;
		final int STATE_COIN_DROPPING = 1;
		final int STATE_WAIT_FOR_PAINT = 2;
		final int STATE_O_CHOOSING = 3;
		final int STATE_EVALUATE_MOVE = 4;
		final int STATE_BOUNCING = 5;
		final int STATE_TIE = 6;
		final int STATE_O_PLAYS_FIRST = 7;

		final int COIN_X = 0;
		final int COIN_Y = 1;
		final int COIN_VY = 2;
		final int COIN_DELAY = 3;
		final int COIN_SPRITE = 4;
		final int COIN_FLOOR_Y = 5;

		final int SPRITE_EGG = 16;
		final int SPRITE_BODY = 17;
		final int SPRITE_HEAD = 18;
		final int SPRITE_BLINK_1 = 19;
		final int SPRITE_BLINK_2 = 20;
		final int SPRITE_LOSE = 21;
		final int SPRITE_STARE = 22;
		final int SPRITE_WIN = 23;

		final String S = "\u7543\ua864\udb85\uffff\u0fff\uc0ff\ubc3f\uab0f\uabcf\uaac3\u6af3\u9ab0\ud6bc\ue2ac\u31a8\uf8a8\ub8a8\ua4a8\u51a0\u06a3\u6a83\uaa8f\uaa0f\ua83f\u00ff\u0fff\uffff\u0000\u2ffc\uaaaf\uaaaa\ua91a\ua1e6\u86b9\u0aaf\u2b1b\u6112\uac0e\u1b29\uc714\ub113\uac0e\u6b2a\u0614\u4090\ua44a\ua40a\uaaaa\u1aa4\u0000\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2aa\uc2aa\ucaaa\u0aa8\u0aa4\u0aa0\u0a91\u0a82\u0a86\u0a81\u0690\uc2a4\uc1aa\uf06a\uf01a\ufc01\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uab0f\uaacf\u55c3\uf9f3\ua6b0\u9abc\u6aac\u6aa8\u9aa8\udaa8\ub6a8\u66a0\u05a3\u4183\uaa8f\uaa0f\ua83f\u00ff\u0fff\uffff\u0000\u2ffc\ua96b\ua69a\ua7da\u9be6\u5af9\uaaab\ua69a\ua28a\u628b\u6aab\uaaab\ua96a\u5006\u0150\u1aa4\uaaaa\uaaaa\uaaaa\uaaaa\u1aa4\u0000\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2aa\uc215\uca1a\u0a46\u0a91\u0aa4\u0aa4\u0aa1\u0a91\u0a85\u0645\uc200\uc101\uf0aa\uf01a\ufc01\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\u6b0f\u8acf\uf2c3\uf9b3\uf4b0\ub27c\ua32c\u6128\u0428\u1868\u44a8\u81a0\u86a3\u6a83\u6a8f\u1a0f\u843f\u00ff\u0fff\uffff\u0000\u2ffc\uaaaa\u9006\u06e4\u2bf9\u6afa\u9aa6\ua55a\uaaaa\uaaaa\u9400\u41a9\u0667\u1a33\u1abe\u08bd\u0602\u4068\u0801\u89a9\u49a8\u0000\u0000\uffff\ufff0\uff02\ufc2a\uf0a8\uf2a1\uc286\uca5a\u090a\u0866\u0872\u08a2\u0852\u0845\u0914\u0605\uc241\uc190\uf06a\uf019\ufc00\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uab0f\u17cf\uf9c3\u69b3\u16b0\u0abc\u06ac\uf9a8\ub8a8\ua1a8\u02a8\u0aa0\u8aa3\u6a83\u6a8f\u090f\u103f\u00ff\u0fff\uffff\u0000\u2ffc\ua55b\u9bf6\u6919\ua6a4\ua11b\ua090\u8414\u11e7\u04e1\u6862\u1aaa\u1865\u6800\ua900\u6a95\u06aa\u0000\u6219\ub240\u62cb\u0040\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2a9\uc2a6\ucaa6\u0a9a\u0a4a\u09a9\u08a8\u0890\u0829\u0a26\u061a\uc280\uc180\uf0a9\uf024\ufc01\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uab0f\u6bcf\uc9c3\uf0b3\ub470\u6e1c\u198c\u0e68\u68a8\u24a8\u1868\u8a20\u4463\u1183\u028f\ua20f\ub43f\u80ff\u0fff\uffff\u0000\u2ffc\ua667\ua1d2\ua2a2\u4010\u6fcb\ubaaa\ue451\u9204\u85a4\u40a0\u54b1\u8aba\u56ae\u2249\u11a4\u0040\u0a0d\u46e8\uea1a\u28f1\u0050\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2aa\uc2a4\uc811\u0886\u006f\u021a\u01a6\u009c\u0488\u01a0\u028a\uc590\uc0a4\uf050\uf00b\ufc02\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uab0f\u5bcf\uf9c3\u28f3\u12b0\u0ab8\uaaa8\u5aa8\ue6a8\ue6a8\u9aa8\u0aa0\u2aa3\uaa83\uaa8f\uaa0f\ua83f\u00ff\u0fff\uffff\u0000\u2ffc\ua55b\u9bf6\u6916\ua690\ua06a\u8640\u1454\u01e2\u28e0\u2861\u1aa7\u862a\ua802\ua800\u6a04\u0aa8\u42a1\ua196\u8010\u18a1\u0154\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2a8\uc2a9\ucaa2\u0a92\u0a49\u0a14\u0a10\u0a46\u0a88\u0a89\u0692\uc2a8\uc1a4\uf069\uf01a\ufc06\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uab0f\u6acf\u1ac3\ub6f3\uc6b0\u1dbc\u2dac\u19a8\u80a8\u68a8\u10a8\u01a0\u46a3\u4a83\u1a8f\u6a0f\ua83f\u00ff\u0fff\uffff\u0000\u2ffc\uaaaf\uaaaa\u9006\u73c9\ub7df\u1be4\uaabf\ua41b\u9be6\u86f2\u85a2\u5005\u0000\u5145\u628e\u5aaa\u0555\u0000\uaaaa\u1aa4\u0000\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2a9\uc2a1\uca86\u0a46\u0a2d\u0a2c\u0a14\u0a01\u0a05\u0a00\u0640\uc291\uc1a0\uf064\uf019\ufc01\uff00\ufff0\uffff\uffff\u0fff\uc0ff\ubc3f\uaf0f\u1bcf\ua6c3\ubdf3\u3cb0\u6cbc\ua2ac\u4aa8\uaaa8\u56a8\ufca8\u9ca0\u64a3\u9283\u0a8f\u6a0f\ua83f\u00ff\u0fff\uffff\u0000\u2ffc\uaaaf\uaaaa\u4001\ubffa\u5006\u27dc\u528e\u1558\u4001\u56aa\u0000\ua1ca\u6188\u8183\ud187\u2188\u0551\u4000\uaaaa\u1aa4\u0000\u0000\uffff\ufff0\uff02\ufc2a\uf0aa\uf2a1\uc29a\uca18\u0a18\u0a04\u0a41\u0a90\u0aa4\u0a95\u0a7e\u062b\uc214\uc245\uf1a0\uf02a\ufc01\uff00\ufff0\uffff\uffff\u3fff\u83ff\ua8ff\ua93f\ua50f\ua54f\u654f\u5a53\u5aa3\u6aa3\ua94f\ua54f\u950f\u90ff\u0fff\uffff\ufffc\uffc1\uff15\ufc56\ufcaa\uf2aa\uf2a5\uca95\uca95\uc565\uf15a\uf15a\ufc5a\uff0a\ufff0\uffff\uc3ff\u28ff\u0a3f\u5083\u5414\u9518\u9528\u5523\u54a3\u000f\u154f\u154f\u5553\u5553\u0003\u2943\u2953\u2a50\u2a55\u2a85\u2aa1\u2a86\u0a82\uca85\uf2a1\uf2a8\ufc0a\uf010\uc554\uc554\uc000\u30ff\u453f\ua94f\u8a4f\u8a8f\uaa8f\u2a13\u4064\u5a94\u5aa4\u5aa4\u4aa4\u4294\u4a53\u2a8f\u003f\ufffc\ufff1\uffc6\uffc8\uffc8\ufc0a\uf152\uc694\u1215\u1555\u1555\u1555\u1555\uc555\uf155\ufc00\u30ff\u453f\u154f\u150f\u008f\uaa8f\u2a13\u4064\u5a94\u5aa4\u5aa4\u4aa4\u4294\u4a53\u2a8f\u003f\ufffc\ufff1\uffc5\uffc1\uffc8\ufc0a\uf152\uc694\u1215\u1555\u1555\u1555\u1555\uc555\uf155\ufc00\u30ff\u453f\u154f\u154f\u154f\u154f\u1513\u4064\u5a94\u5aa4\u5aa4\u4aa4\u4294\u4a53\u2a8f\u003f\ufffc\ufff1\uffc5\uffc5\uffc5\ufc05\uf151\uc694\u1215\u1555\u1555\u1555\u1555\uc555\uf155\ufc00\uc0ff\u2a3f\uaa8f\uaa8f\ua28f\uaa8f\u2a13\u4054\u5a94\u56a4\u56a4\u56a4\u56a3\u5a83\u094f\u003f\uffc0\uff2a\ufcaa\ufcaa\ufca8\ufc02\uf154\uca55\uc145\u1555\u1555\u1555\u1555\uc555\uf054\uff00\uc0ff\u14ff\ua93f\u8a3f\u8a3f\u020f\ua453\ua513\u4544\u5548\u5548\u5548\u5540\u5523\u540f\u00ff\uff03\ufc14\ufc6a\ufca2\ufca2\uf080\uc515\uc455\u1151\u2155\u2155\u2155\uc155\ufc55\ufc15\uff00\u0c3f\ua24f\uaa93\u20a3\ua8a3\u2aa3\u4a54\u55a4\u56a8\u56a8\u54a8\u4024\u00a4\u0090\u0243\u0a0f\u283f\u80ff\u0fff\uffff\ufffc\ufff2\ufc00\uf154\uc695\uc215\u1555\u1555\u1555\uc555\uf055\ufc00\uffc8\uffc8\uffc8\uffc8\ufff2\ufffc";

		final int[] palette = { 0xB88818, 0xE0C028, 0xF8F040, 0xFFFFFF, 0xB71919, 0xDD2727, 0xF74040, 0xFF7575, };

		int x;
		int y;
		int z;
		int i;
		int j;
		int k;

		final AffineTransform affineTransform = new AffineTransform();
		int[][] sorts = null;
		int[][] permutations = null;
		int[] pixels = new int[8];
		BufferedImage[] sprites = new BufferedImage[24];
		ArrayList<Integer> values = new ArrayList<Integer>();
		Random random = new Random();
		int[] result = null;
		int[][] board = null;
		int[][] spriteBoard = null;
		int state = 0;
		int coinX = 0;
		float coinY = 0;
		float coinVy = 0;
		int targetY = 0;
		int coinSprite = 0;
		int move = 0;
		int moveStone = 0;
		int yoshiX = 0;
		float[][] bouncingCoins = null;
		boolean fading = true;
		int fadeRadius = 0;
		int fadeDirection = -4;
		int level = 1;
		boolean advanceLevel = false;
		boolean xPlaysFirst = true;
		boolean mouseReleased = true;
		int stareCountdown = 0;
		boolean staring = false;
		int blinkCountdown = 0;
		int blinkIndex = 0;

		// create background image
		BufferedImage backgroundImage = new BufferedImage(182, 232, 1);
		Graphics2D g = (Graphics2D) backgroundImage.getGraphics();
		for (y = 0; y < 232; y++) {
			for (x = 0; x < 7; x++) {
				float percent = y / 231f;
				g.setColor(new Color(Color.HSBtoRGB(0.58f, 0.88f - 0.29f * percent, (y <= 75 || (x & 1) == 1 ? 1 : 0.9f) * (0.7f + 0.3f * percent))));
				g.fillRect(x * 26, y, 26, 1);
			}
		}

		// extract ways-to-win table
		for (y = 0; y < 3; y++) {
			for (x = 0; x < 4; x++) {
				W[y][x] = W[5 - y][x] = W[5 - y][6 - x] = W[y][6 - x] = 0x0F & (S.charAt(y) >> (x << 2));
			}
		}

		// decompress the coin sprites    
		for (z = 0; z < 2; z++) {
			for (i = 0; i < 8; i++) {
				sprites[(z << 3) + i] = new BufferedImage(24, 24, 2);
				for (j = 0; j < 3; j++) {
					for (y = 0; y < 24; y++) {
						for (x = 0; x < 8; x++) {
							float dx = x + (j << 3) - 11.5f;
							float dy = y - 11.5f;
							k = (int) (dx * dx + dy * dy);
							pixels[x] = (k >= 144) ? 0 : (0xFF000000 | palette[(z << 2) + (0x03 & (S.charAt(3 + y + j * 24 + i * 72) >> (x << 1)))]);
						}
						sprites[(z << 3) + i].setRGB(j << 3, y, 8, 1, pixels, 0, 8);
					}
				}
			}
		}

		// decompress the Yoshi sprites
		for (i = 0; i < 8; i++) {
			z = i == 7 ? 19 : 16;
			sprites[16 + i] = new BufferedImage(16, z, 2);
			for (j = 0; j < 2; j++) {
				for (y = 0; y < z; y++) {
					for (x = 0; x < 8; x++) {
						k = 0x03 & (S.charAt(579 + y + j * z + (i << 5)) >> (x << 1));
						pixels[x] = (k == 0) ? 0xFF000000 : (k == 1) ? 0xFF00FF00 : (k == 2) ? 0xFFFFFFFF : 0;
					}
					sprites[16 + i].setRGB(j << 3, y, 8, 1, pixels, 0, 8);
				}
			}
		}

		g = (Graphics2D) I.getGraphics();
		Graphics2D g2 = null;

		long nextFrameStartTime = System.nanoTime();
		while (true) {

			do {
				nextFrameStartTime += 16666667;

				// -- update starts ----------------------------------------------------

				if (a[MOUSE_PRESSED] == 0) {
					mouseReleased = true;
				}

				// update Yoshi staring
				if (--stareCountdown < 0) {
					staring = !staring;
					stareCountdown = staring ? 64 : (5 + random.nextInt(10)) << 6;
					if (!staring) {
						blinkIndex = 0;
						blinkCountdown = (2 + random.nextInt(4)) << 6;
					}
				}

				// update Yoshi blinking
				if (--blinkCountdown < 0) {
					if (++blinkIndex == 4) {
						blinkIndex = 0;
						blinkCountdown = (2 + random.nextInt(4)) << 6;
					} else {
						blinkCountdown = 4;
					}
				}

				if (state == STATE_X_CHOOSING) {
					coinX = a[MOUSE_X] - 12;
					coinY = 51;
					if (mouseReleased && a[MOUSE_PRESSED] == 1) {
						mouseReleased = false;
						move = a[MOUSE_X] / 26;
						moveStone = STONE_X;
						if (move >= 0 && move < 7 && board[6][move] >= 0) {
							coinX = 1 + move * 26;
							coinVy = 0;
							targetY = 77 + board[6][move] * 26;
							state = STATE_COIN_DROPPING;
						}
					}
				} else if (state == STATE_COIN_DROPPING) {
					coinVy += GRAVITY;
					coinY += coinVy;
					if (coinY >= targetY) {
						coinY = targetY;
						state = STATE_WAIT_FOR_PAINT;
					}
				} else if (state == STATE_EVALUATE_MOVE) {
					spriteBoard[board[6][move]][move] = coinSprite + 1;
					m(board, move, moveStone);
					if (board[7][1] != NODE_INTERMEDIATE) {
						if (board[7][1] != NODE_TIE) {
							x = board[7][2];
							y = board[7][3];
							for (i = 0; i < 4; i++) {
								bouncingCoins[i][COIN_X] = 1 + x * 26;
								bouncingCoins[i][COIN_FLOOR_Y] = bouncingCoins[i][COIN_Y] = 77 + y * 26;
								bouncingCoins[i][COIN_VY] = COIN_JUMP_VY;
								bouncingCoins[i][COIN_DELAY] = i << 2;
								bouncingCoins[i][COIN_SPRITE] = spriteBoard[y][x] - 1;
								spriteBoard[y][x] = 0;
								if (board[7][4] < x) {
									x--;
								} else if (board[7][4] > x) {
									x++;
								}
								if (board[7][5] < y) {
									y--;
								} else if (board[7][5] > y) {
									y++;
								}
							}
							state = STATE_BOUNCING;
							advanceLevel = moveStone == STONE_X;
						} else {
							state = STATE_TIE;
						}
					} else {
						stareCountdown = staring ? 64 : (5 + random.nextInt(10)) << 6;
						state = moveStone == STONE_X ? STATE_O_CHOOSING : STATE_X_CHOOSING;
						coinSprite = random.nextInt(8);
						if (state == STATE_O_CHOOSING) {
							coinSprite += 8;
						} else {
							coinY = 51;
							coinX = a[MOUSE_X] - 12;
						}
					}
				} else if (state == STATE_O_CHOOSING) {
					// apply NegaMax
					n(board, sorts, 0, STONE_O, -INFINITY, INFINITY, level + 3, permutations, result);
					move = result[1];
					moveStone = STONE_O;
					coinY = 16;
					coinX = 1 + move * 26;
					yoshiX = coinX + 13;
					coinVy = 0;
					targetY = 77 + board[6][move] * 26;
					state = STATE_COIN_DROPPING;
					nextFrameStartTime = System.nanoTime();
				} else if (state == STATE_BOUNCING) {
					for (i = 0; i < 4; i++) {
						if (bouncingCoins[i][COIN_DELAY] > 0) {
							bouncingCoins[i][COIN_DELAY]--;
						} else {
							bouncingCoins[i][COIN_VY] += COIN_JUMP_GRAVITY;
							bouncingCoins[i][COIN_Y] += bouncingCoins[i][COIN_VY];
							if (bouncingCoins[i][COIN_VY] > 0 && bouncingCoins[i][COIN_Y] >= bouncingCoins[i][COIN_FLOOR_Y]) {
								bouncingCoins[i][COIN_Y] = bouncingCoins[i][COIN_FLOOR_Y];
								bouncingCoins[i][COIN_VY] = COIN_JUMP_VY;
							}
						}
					}
				}

				if (fading) {
					fadeRadius += fadeDirection;
					if (fadeRadius <= 0) {

						if (advanceLevel && level == 7) {
							// do not advance beyond level 7
							fadeDirection = 0;
							continue;
						}

						fadeDirection = 4;

						// --- reset game begins ----------

						if (advanceLevel) {
							level++;
						}
						xPlaysFirst = !xPlaysFirst;

						sorts = new int[64][7];
						permutations = new int[64][7];
						result = new int[2];
						board = new int[8][7];
						spriteBoard = new int[8][7];
						state = xPlaysFirst ? STATE_X_CHOOSING : STATE_O_PLAYS_FIRST;
						yoshiX = 92;
						bouncingCoins = new float[4][32];
						advanceLevel = false;
						stareCountdown = (5 + random.nextInt(10)) << 6;
						staring = false;

						coinSprite = random.nextInt(8);
						if (state == STATE_O_PLAYS_FIRST) {
							coinSprite += 8;
						} else {
							coinY = 51;
							coinX = a[MOUSE_X] - 12;
						}

						// intialize board
						for (x = 0; x < 7; x++) {
							board[6][x] = 5;
						}

						// initialize permutations
						for (i = 0; i < 64; i++) {
							for (j = 6; j >= 0; j--) {
								values.add(j);
							}
							for (j = 6; j >= 0; j--) {
								permutations[i][j] = values.remove(random.nextInt(j + 1));
							}
						}

						nextFrameStartTime = System.nanoTime();

						// --- reset game ends ------------

					} else if (fadeRadius > 116) {
						fading = false;
					}
				} else if ((state == STATE_BOUNCING || state == STATE_TIE) && mouseReleased && a[MOUSE_PRESSED] == 1) {
					mouseReleased = false;
					fading = true;
					fadeRadius = 116;
					fadeDirection = -4;
				} else if (state == STATE_O_PLAYS_FIRST) {
					if (!fading) {
						state = STATE_O_CHOOSING;
					}
				}

				// -- update ends ------------------------------------------------------

			} while (nextFrameStartTime < System.nanoTime());

			// -- render starts ------------------------------------------------------

			// draw background
			g.drawImage(backgroundImage, 0, 0, null);

			// draw coins
			for (y = 0; y < 6; y++) {
				for (x = 0; x < 7; x++) {
					if (spriteBoard[y][x] > 0) {
						g.drawImage(sprites[spriteBoard[y][x] - 1], 1 + x * 26, 77 + y * 26, null);
					}
				}
			}

			// draw eggs
			for (i = 0; i < level; i++) {
				g.drawImage(sprites[SPRITE_EGG], 5 + i * 26, 0, null);
			}

			// draw Yoshi
			g.translate(yoshiX, 0);
			if (yoshiX > 100) {
				g.scale(-1, 1);
				g.translate(2, 0);
			}
			g.drawImage(sprites[SPRITE_BODY], -13, 33, null);
			g.drawImage(sprites[(state == STATE_X_CHOOSING && staring) ? SPRITE_STARE : state == STATE_TIE || (state == STATE_BOUNCING && moveStone == STONE_X) ? SPRITE_LOSE
					: state == STATE_BOUNCING ? SPRITE_WIN : blinkIndex == 3 ? SPRITE_BLINK_1 : SPRITE_HEAD + blinkIndex], -6, 17, null);
			g.setTransform(affineTransform);

			if (state == STATE_X_CHOOSING || state == STATE_COIN_DROPPING || state == STATE_WAIT_FOR_PAINT) {
				// draw moving coin
				g.drawImage(sprites[coinSprite], coinX, (int) coinY, null);
			}

			if (state == STATE_BOUNCING) {
				// draw bouncing coins
				for (i = 0; i < 4; i++) {
					g.drawImage(sprites[(int) bouncingCoins[i][COIN_SPRITE]], (int) bouncingCoins[i][COIN_X], (int) bouncingCoins[i][COIN_Y], null);
				}
			}

			if (fading) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 182, 116 - fadeRadius);
				g.fillRect(0, 116 + fadeRadius, 182, 116 - fadeRadius);
				for (y = 116 - fadeRadius; y < 116 + fadeRadius; y++) {
					x = 116 - y;
					z = (int) Math.sqrt(fadeRadius * fadeRadius - x * x);
					g.fillRect(0, y, 91 - z, 1);
					g.fillRect(91 + z, y, 91 - z, 1);
				}
			}

			if (state == STATE_WAIT_FOR_PAINT) {
				// make sure a paint occurs before applying NegaMax
				state = STATE_EVALUATE_MOVE;
			}

			// -- render ends --------------------------------------------------------

			// show the hidden buffer
			if (g2 == null) {
				g2 = (Graphics2D) getGraphics();
				requestFocus();
			} else {
				paint(g2);
			}

			// burn off extra cycles
			while (nextFrameStartTime - System.nanoTime() > 0) {
				Thread.yield();
			}
		}
	}

	@Override
	public boolean handleEvent(Event e) {
		a[0] = e.x >> 1;
		a[1] = e.id == 501 ? 1 : 0;
		return false;
	}

	// to run in window, uncomment below
	/*public static void main(String[] args) throws Throwable {
	  javax.swing.JFrame frame = new javax.swing.JFrame("Yoshi's Coins 4K");
	  frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	  a applet = new a();
	  applet.setPreferredSize(new java.awt.Dimension(364, 464));
	  frame.add(applet, java.awt.BorderLayout.CENTER);
	  frame.setResizable(false);
	  frame.pack();
	  frame.setLocationRelativeTo(null);
	  frame.setVisible(true);
	  Thread.sleep(250);
	  applet.start();
	}*/
}
