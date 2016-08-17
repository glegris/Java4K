package java4k.boing4k;

/*
 * Boing 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Boing 4K.
 *
 * Boing 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Boing 4K is distributed in the hope that it will be useful,
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
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import java4k.GamePanel;

public class a extends GamePanel implements Runnable {

	private static final int WIDTH = 512;
	private static final int HEIGHT = 512;

	// keys
	private boolean[] a = new boolean[32768];

	final int TILE_EMPTY = 0;
	final int TILE_SOLID = 1;
	final int TILE_QUESTION = 2;
	final int TILE_UP = 3;
	final int TILE_DOUBLE_UP = 4;
	final int TILE_EXCLAMATION = 5;
	final int TILE_PAUSE = 6;
	final int TILE_GREEN = 7;
	final int TILE_CHECKER = 8;
	final int TILE_RIGHT = 11;
	final int TILE_DOUBLE_RIGHT = 12;
	final int TILE_DOWN = 19;
	final int TILE_DOUBLE_DOWN = 20;
	final int TILE_LEFT = 27;
	final int TILE_DOUBLE_LEFT = 28;

	final int VK_LEFT = 0x25;
	final int VK_RIGHT = 0x27;
	final int VK_UP = 0x26;
	final int VK_DOWN = 0x28;

	final String S = "\u7c7c\u4428\u7cbc\uff7f\u5f9f\u57e7\ud5f9\uf57e\u555f\uff5f\uff5f\u555f\uff5f\uff5f\u556f\ufdbd\uf6f5\u5bd7\uaf5f\uf7ff\udbd5\u6f55\ubd7f\uf5ff\ud555\u9fff\u9fff\u9555\u9fff\u9fff\ue555\uf9ff\u7e7f\u5f95\ud7ea\u00b8\ub8f8\u0018\u5550\ufba5\u5555\ufee9\u56e9\u55e9\ue5e9\ufee9\u7ee9\u7ee9\ufee9\u7ee9\ufee9\u5555\ufba5\u5550\u0555\u5aef\u5555\u6bbf\u6b95\u6b55\u6b5b\u6b95\u6bb5\u6bbd\u6bbf\u6bbd\u6bbf\u5555\u5aef\u0555\ub700\uf718\u0018\u0000\u4000\ud000\ue400\ue900\u6b40\udb90\ud7a4\ud190\u5040\ud000\ud000\ud000\u4000\u0000\u0000\u0001\u0007\u001b\u007b\u01f9\u06f7\u1edb\u6e5b\u1d19\u0417\u001b\u001b\u001b\u0007\u0001\u0000\ub7b7\uf7f7\u0018\u0000\u4000\ud000\uf400\uf900\u7a40\u1ad0\u46e4\ud1d0\uf440\uf900\u7a40\u1ad0\u06e4\u01d0\u0040\u0001\u0006\u001e\u006e\u01ed\u07e4\u1fd1\u6f46\u1d1e\u046e\u01ed\u07e4\u1fd0\u6f40\u1d00\u0400\ub700\uf718\ua8e4\u5554\uaa94\u6a99\u5fd9\u5a9d\u5fd9\u5fdd\u5fdd\u7fdd\u7fdd\u7fd9\uaa9d\u7fd9\u6a99\uaa94\u5554\u1555\u16aa\u66a9\u67f5\u76a5\u67f5\u77f5\u77f5\u77fd\u77fd\u67fd\u76aa\u67fd\u66a9\u16aa\u1555\u00cc\u00ff\uccff\u0554\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u1ef9\u0554\u1550\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u7ee4\u1550\u2078\u00ff\u3800\uf000\u5f00\ua5c0\ufa70\uff9c\uff9c\uffe7\uffe7\uffe7\uffe7\uff9c\uff9c\ufa70\ua5c0\u5f00\uf000\u000f\u00f5\u035a\u0daf\u36ff\u36ff\udbeb\udbeb\udbff\udbff\u36ff\u36ff\u0daf\u035a\u00f5\u000f\uc6c6\uffff\uc6ff\ua955\u6e95\u5be5\u5bf9\u5bf9\u5bfe\u6ffe\ubffe\ufffe\ufffe\ufffe\u5ff9\u5ff9\uffe5\ufe95\ua955\u556a\u5695\u5955\u655f\u955f\u9555\u9555\u9555\u955a\u956f\u956f\u656f\u656f\u596f\u569b\u556a\u58c8\uffff\uf8ff\u0040\u0140\u0640\u0640\u0690\u0690\u0690\u06a4\u06a9\u46a9\u91a9\u91a4\ua690\uaa40\uaa40\u9500\ua900\uaa40\ua9d0\ua9f4\ua7f4\uaff4\ubfd0\u7f50\u95f4\uaa74\u6a90\uda90\uda40\ud640\ud900\u6a40\ua640\u9a40\u5a40\u1a40\u19d4\u19fd\u1ffd\u1ffd\u1ffd\u1fd4\u0540\ufeff\ufbfd\ueff7\ubfdf\uff7f\u7fff\udfbf\uf7ef\ufdfb\ufffe\ufeff\ufbfd\ueff7\ubdde\uf77b\udfef\u7fbf\uffff\ubf7f\u6fdf\udbb7\uf6ed\ufdfb\ufffe\uffff\uffff\u0f0f\u0f0f\u0f0f\u0f0f\uffff\uffff\uf0f0\uf0f0\uf0f0\uf0f0\uffff\uffff\u0707\u0707\u0707\u0707\uffff\ue0ff\ue0e0\ue0e0\ue0e0\uffe0\ufeff\ufbfd\ueff7\ubedf\ufb7d\ueff7\ubdde\uf67b\udbed\u6fb7\ubfdf\uff7f\u7eff\u3c3c\u3c3c\u783c\uf078\uf0f0\u3c78\u0f1e\u0f0f\u3c1e\ue376\ue3e3\u76e3\u1c1c\u1c1c\u6e3c\u87c7\u8787\u7cce\u3838\u3838\ue070\ue0e0\ue0e0\u3870\u061c\u0303\u0603\u180c\u6030\uc0c0\uc0c0\uffff\u0303\u1f1f\u1818\u3818\ucc68\u8386\u8181\u8181\uff81\u1010\u3e10\u4262\u80c3\u8080\uff80\u0101\u0301\u0602\u0c04\u1808\u80f0\u8080\u898f\u9199\u01f1\u0101\u0101\uffff\u00ff\uff00\uffff\u0000\uff00\uffff\u0000\u0000\u0000\uffff\u00ff\u0000\u3c00\u3c3c\u0000\u0000\u0f0f\u000f\u0000\u0000\uf0f0\u00f0\u0000\u0000\u0f00\u0f0f\u000f\u0000\u0606\u0000\u0000\u1818\u0000\u0000\uc000\u00c0\u0000\u0000\u1800\u0018\u0000\u0000\u0300\u0003\u0000\u0002\u0000\u0002\u0000\u0002\u0800\u0000\u6060\u0000\u0000\uff00\u0000\u0000\u0000\u3c3c\u0000\u0000\u0000\uffff\uffff\u0000\u0000\u0000\uffff\uffff\u0000\u0000\u0000\u0f0f\u0f0f\u0000\u0000\u0000\uf0f0\uf0f0\u0000\u0000\u0000\u00ff\u0000\u0000\uff00\u0000\u0000\u0000\u00ff\u0000\u0000\u0f00\u0000\u0000\u0000\u0006\u0000\u0000\uff00\u3c7e\u1818\u0000\u0000\u0000\u1818\u0000\u0000\u3c00\u243c\u2424\u003c\u0000\u0000\uff00\u0000\u0000\u0000\u3c7e\u1818\u3c18\uff7e\uff00\uff00\uff00\u7f00\u1f00\u0f00\u0700\u0300\u0100\u0100\u0200\u0400\u0800\u1000\u2000\u4000\u8000\u8080\u8080\u8080\u00ff\uff00\u0000\uffff\u0000\uffff\u0000\uc3c3\u0000\ue7e7\u1800\u1800\u0800\u0800\u0200\u0800\u2000\u0800\u2000\u8000\u2000\u0800\uff00\uffff\u8181\uff81\u8181\uff81\u2424\u103c\u1010\u011f\u0101\u0001\u0004\u00f0\uffff\uffff\uaaaa\uaaaa\uaaaa\u00fe\u7e00\u0000\u557f\u5555\u5555\u5555\u5555\u5555\u7f55\u0000\u3c3c\u3c3c\u003c\u7e00\u8100\u00ff\u7e00\u0000\u2838\u2828\u2828\u2828\u2828\u2828\u0038\u1c00\u1414\u1414\u1414\u1414\u1414\u1414\u1414\u001c\uff00\u0000\u0101\u0505\u1414\u1010\u1414\u0404\u1414\u1010\u1414\u0404\u1014\uff10\u0000\u2424\uff00\u00ff\u8100\u8181\uffff\uffff\u0000\uc6c6\uc6c6\uc6c6\ufec6\u0000\u223e\u2222\u2222\u2222\u2222\u2222\u2222\u2222\u003e\uff00\u0000\u00f0\u8000\u8080\u8888\u8088\u8080\u8880\u8888\u8080\u8080\u00ff\u0100\u1101\u1010\u1110\u0101\u1101\u1010\u1111\u0101\u1111\u1010\u1111\u0101\u1111\u1110\u0111\u1111\u0111\uff01\u0000\u007e\uff00\u0000\u0080\u9200\u0000\u0002\u9200\u8080\u8080\uffff\u0000\u0000\uffff\u1800\u663c\uc3c3\u3c66\u0018\u1800\u1818\u0018\u2400\u2424\u3c24\u0000\uff00\u00ff\u0000\u70e0\u1c38\u070e\u1c0e\u1018\u1010\u1010\u1010\u1010\uff10\ue7ff\uffe7\u0101\u0101\u0101\u0101\u0101\u0301\u0f07\u3f1f\uff7f\uffff\u40ff\u4040\u5040\u5050\u1410\u1414\u0414\u0e0e\u0e0e\u0e0e\u0e0e\uffff\uffff\u0000\u55aa\u55aa\u00aa\ua500\u0024\uffff\u0fff\u0f0f\u080f\u0808\u0808\uffff\u0000\uffff\u80ff\u8080\uff80\uffff\u0101\u4901\u4040\u4040\u0048\u0800\ufd08\u0400\u3000\u4000\u0108\u0101\u4055\u4040\u4040\u80c0\u8080\u10f0\u1010\u7c7c\u0000\ufe7c\ufefe\u10fe\u3810\u387c\uff10\uffff\u0000\uffff\u0101\u0407\u0404\u101c\u1010\u4070\u4040\u0040\uff00\uffff\u00ff\u5500\u5555\u0000\uffff\u00ff\u1818\u00ff\u00ff\u00ff\u3c7e\u1818\u1818\u6030\uc0c0\u3060\u7818\u0078\u0060\u00c0\ua080\ub8a0\ubab8\u0a0a\u020a\u0202\ue0ee\ue0e0\uc0e0\uc0c0\u80c0\u0080\u0080\u07bf\u0707\u0707\u0707\u00ff\uffff\u00ff\ua440\u1454\ue44a\u2a48\u2255\ud5c9\u1422\ua249\uf245\u90f0\uf090\ufff0\u8181\u9999\u8181\u0fff\u0909\u0909\u4949\u4f49\u4f40\u4040\u0f4f\u60ff\u6060\uf400\u0400\uffff\u00ff\u7e7e\u7e7e\u0000\uffff\u00ff\u0f00\u0f0f\u1e0f\u783c\uf0f0\uf0f0\u00f0\u9900\u9999\u9999\u9999\u0099\uff00\u00ff\u8900\u8991\u8991\u8991\u8991\u8991\u3c00\u3c3c\u003c\u3c00\u2424\u3c24\u2020\u2020\u80e0\u8080\u01ff\u0101\u0101\u0101\uffff\u00ff\u1800\u0018\uff00\ue7ff\ue0e7\uffe7\u00ff\uff00\u3c7e\u1818\u9999\u8181\u9999\u9999\u3c00\u7e00\uff00\u00ff\u0000\u0000\u0707\u0007\u0000\ue0e0\ue0e0\u0000\u0000\u0303\u0000\u0000\uff00\u0000\u0000\u0000\u00ff\u2800\u2828\u2828\u2828\u2828\u2828\u0028\u1400\u1414\u1414\u1414\u1414\u1414\u1414\u2222\u2222\u2222\u2222\u2222\u0000\u8888\u8888\u8888\u8888\u8888\u8888\u8888\u1100\u1111\u1111\u0000\u7f7f\u1c3e\u0808\uf808\u8080\u8080\u8381\u8985\ue191\u0101\u0101\u00ff\u0000\u0000\uff00\u0000\u0000\u0000\u000f\u0000\u0000\uf000\u0000\u0000\u0000\u00ff\u0000\u0000\uff00\uffff\u3c7e\u3c3c\u243c\ue724\u8181\u8181\u8181\ua500\u2400\u2400\ua500\u8100\uff00\u0000\u1818\u0000\u0000\uff00\uffff\u0000\u0000\uffff\u0000\u0000\u3c00\u2424\u003c\u0000\u00ff\u0000\uff00\u81ff\u8199\u81ff\u9999\ud581\uaa00\u5500\ufe00\uff80\u0101\u0700\uf707\uf0f7\u07f7\u00f7\ufefe\udc00\udfdc\ufc1f\u00fc\uf7f7\uf0f7\u07f7\uffff\u00ff\uffff\uff00\uffff\uff00\u3c00\u3c00\u243c\u2424\u183c\u1818\u1818\u0018\u1818\u1818\u3c18\u3c3c\uffff\uffff\u9400\u3754\u9555\uff00\u00ff\u0101\u0505\u1515\u5555\u5454\u5050\u5454\u5555\u5555\u007f\uffff\u0000\u0000\uff00\u8181\u8181\uff81\u0000\u0000\u0000\u0010\u0000";

	final int SPRITE_COUNT = 8;

	final float OY = 32;
	final float D = -256;

	final float GRAVITY = 0.4f;
	final float LOW_GRAVITY = 0.2f;
	final float HIGH_GRAVITY = 0.8f;
	final float JUMP_SPEED = 11.25f;
	final float V_ANGLE = 0.1122f;

	final float yy = 0.921061f;
	final float zy = 0.389418f;

	BufferedImage image = new BufferedImage(256, 256, 1);
	BufferedImage image2 = new BufferedImage(256, 85, 2);
	Graphics2D g = (Graphics2D) image.getGraphics();
	Graphics2D g3 = (Graphics2D) image.getGraphics();
	Graphics2D g2 = null;

	float playerX = 0;
	float playerY = 0;
	float playerZ = 0;
	float playerAngle = 0;
	float playerVx = 0;
	float playerVy = 0;
	float playerVz = 0;
	float playerVa = 0;
	float playerGravity = 0;
	float playerRestoreVx = 0;
	float playerRestoreVz = 0;
	int playerRestoreCount = 0;
	boolean playerFalling = false;
	boolean playerRestoring = false;

	boolean showingBoard = false;
	boolean beatLevel = false;

	boolean fading = true;
	boolean fadingOut = true;

	int x;
	int y;
	int z;
	int i;
	int j;
	int k;
	int cloudX = 0;
	int counter = 0;
	int time = 0;
	int timeCounter = 0;
	int level = 0;
	int beatLevelCounter = 0;
	int fade = 255;

	AffineTransform affineTransform = new AffineTransform();
	BufferedImage bunnySprite = new BufferedImage(16, 43, 2);
	int[][][] sprites = new int[64][16][16];
	int[][] floorMap = new int[60416][2];
	int[] pixels = new int[65536];
	int[][] sky = new int[256][256];
	int[][] world = null;

	Random random = null;

	int[] shadowMap = new int[60416];

	long nextFrameStartTime;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public void start() {
		new Thread(this).start();
	}

	public a() {
		super(true);
		
		// create floor map
		for (y = 0; y < 236; y++) {
			for (x = 0; x < 256; x++) {
				float Y = 107.5f - y;
				float py = OY + zy * D + Y * yy;
				float K = OY / (OY - py);
				floorMap[(y << 8) | x][0] = ((int) (1024 + (x - 127.5f) * K)) - 1024;
				floorMap[(y << 8) | x][1] = (int) ((Y * zy - yy * D) * K);
			}
		}

		// create shadow map
		for (y = 0; y < 256; y++) {
			for (x = 0; x < 256; x++) {
				i = (x - 129) >> 1;
				j = y - 153;
				if (i * i + j * j < 110) {
					shadowMap[(y << 8) | x] = 32;
				}
			}
		}

		// decompress tile sprites
		for (i = 0; i < SPRITE_COUNT; i++) {
			for (j = 0; j < 2; j++) {
				for (y = 0; y < 16; y++) {
					for (x = 0; x < 8; x++) {
						int color = 0x03 & S.charAt(3 + i * 35 + (j << 4) + y) >> (x << 1);
						sprites[i][15 - y][x + (j << 3)] = sprites[i + 8][x + (j << 3)][15 - y] = sprites[i + 16][y][x + (j << 3)] = sprites[i + 24][x + (j << 3)][y] = (color == 0) ? sprites[0][15 - y][x
								+ (j << 3)]
								: (color == 1) ? 0 : (color == 2) ? ((S.charAt(i * 35) << 8) | (S.charAt(i * 35 + 2) >> 8)) : (S.charAt(i * 35 + 1) << 8) | (S.charAt(i * 35 + 2) & 0xFF);
					}
				}
			}
		}

		// create dark versions of tiles for bunny shadow
		for (i = 0; i < 32; i++) {
			for (y = 0; y < 16; y++) {
				for (x = 0; x < 16; x++) {
					float[] hsb = new float[3];
					Color.RGBtoHSB(sprites[i][y][x] >> 16, 0xFF & (sprites[i][y][x] >> 8), 0xFF & sprites[i][y][x], hsb);
					sprites[i + 32][y][x] = Color.HSBtoRGB(hsb[0], hsb[1], 0.5f * hsb[2]);
				}
			}
		}

		// decompress bunny
		for (y = 0; y < 43; y++) {
			for (x = 0; x < 8; x++) {
				int color = 0x03 & S.charAt(283 + y) >> (x << 1);
				pixels[x] = pixels[15 - x] = (color == 0) ? 0 : 0xFF000000 | ((color == 1) ? 0 : (color == 2) ? ((S.charAt(280) << 8) | (S.charAt(282) >> 8)) : (S.charAt(281) << 8)
						| (S.charAt(282) & 0xFF));
			}
			bunnySprite.setRGB(0, y, 16, 1, pixels, 0, 16);
		}

		nextFrameStartTime = System.nanoTime();
	}

	@Override
	public void paintComponent(Graphics g2) {
		
		// burn off extra cycles
		if (nextFrameStartTime - System.nanoTime() > 0) {
			return;
		}

		do {
			nextFrameStartTime += 16666667;

			// -- update starts ----------------------------------------------------

			counter++;

			// move clouds
			if ((counter & 7) == 7) {
				cloudX++;
			}

			if (fading) {
				if (fadingOut) {
					fade += 8;
					if (fade >= 255) {
						fade = 255;
						fadingOut = false;
						time = 60;

						playerX = 56;
						playerY = -256;
						playerZ = 2048;
						playerAngle = 0;
						playerVx = 0;
						playerVy = 0;
						playerVz = 0;
						playerVa = 0;
						playerGravity = GRAVITY;
						playerRestoreVx = 0;
						playerRestoreVz = 0;
						playerRestoreCount = 0;
						playerFalling = false;
						playerRestoring = false;
						showingBoard = true;

						if (beatLevel) {
							if (level == 13) {
								// kill screen (14 playable levels in total)
								fade = 255;
								fading = true;
								fadingOut = true;
								continue;
							}
							beatLevel = false;
							level++;
						}

						random = new Random(level);

						// decompress level
						world = new int[128][8];
						for (y = 127; y >= 0; y--) {
							for (x = 0; x < 8; x++) {
								if (y > 123) {
									world[y][x] = TILE_CHECKER;
								} else if (y < 4) {
									world[y][x] = TILE_SOLID;
								} else if ((S.charAt(324 + (y >> 1) + 60 * level) >> (((y & 1) << 3) + x) & 1) == 1) {
									if (random.nextBoolean()) {
										z = TILE_SOLID;
									} else {
										z = random.nextInt(7) + 1;
										if (z == 3 || z == 4) {
											z += random.nextInt(4) << 3;
										}
									}
									world[y][x] = level == 3 ? (world[y + 1][x] == TILE_EMPTY ? TILE_DOUBLE_UP : TILE_SOLID) : level == 4 ? (world[y + 1][x] == TILE_EMPTY ? TILE_EXCLAMATION
											: TILE_SOLID) : level == 5 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : x < 4 ? TILE_RIGHT : TILE_LEFT)
											: level == 6 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : x < 4 ? TILE_DOUBLE_RIGHT : TILE_DOUBLE_LEFT)
													: level == 7 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : (x & 1) == 1 ? TILE_DOWN : TILE_DOUBLE_UP) : level == 8 ? (y & 3) == 0 ? TILE_QUESTION
															: TILE_SOLID : level == 9 ? (y & 3) == 0 ? TILE_SOLID : TILE_PAUSE : level == 10 ? z : level == 11 ? y < 36 ? TILE_GREEN
															: y < 68 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : x < 4 ? TILE_RIGHT : TILE_LEFT)
																	: y < 100 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : x < 4 ? TILE_DOUBLE_RIGHT : TILE_DOUBLE_LEFT) : TILE_EXCLAMATION
															: level == 12 ? y < 36 ? TILE_DOUBLE_UP : y < 68 ? TILE_EXCLAMATION : y < 100 ? TILE_GREEN : z
																	: level == 13 ? y < 36 ? x < 4 ? TILE_DOUBLE_RIGHT : TILE_DOUBLE_LEFT : y < 40 ? TILE_SOLID : y < 68 ? TILE_PAUSE
																			: y < 79 ? TILE_SOLID : y < 100 ? (world[y + 1][x] == TILE_EMPTY ? TILE_SOLID : x < 4 ? TILE_RIGHT : TILE_LEFT)
																					: TILE_GREEN : level == 2 ? TILE_GREEN : TILE_SOLID;
								}
							}
						}

						// create sky
						g3.setColor(new Color(0x3CBCFC));
						g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g3.fillRect(0, 0, 256, 256);
						for (j = 0; j < 4; j++) {
							k = random.nextInt(128);
							for (i = 0; i < 2; i++) {
								g3.setColor(i == 0 ? Color.LIGHT_GRAY : Color.WHITE);
								for (float p = 0; p < 70; p += 0.25f) {
									g3.fillOval(40 + k + (int) ((p + (i << 3)) * Math.cos(p) / 2), 20 + (j << 6) - (i << 2) + (int) (p * Math.sin(p) / 5), 8, 8);
								}
							}
						}
						for (y = 0; y < 256; y++) {
							image.getRGB(0, y, 256, 1, sky[y], 0, 256);
						}

						nextFrameStartTime = System.nanoTime();
					}
				} else {
					fade -= 8;
					if (fade <= 0) {
						fade = 0;
						fading = false;
					}
				}

				continue;
			} else if (beatLevel) {
				if (--beatLevelCounter == 0) {
					fading = true;
					fadingOut = true;
				} else {
					continue;
				}
			} else if (showingBoard) {
				playerZ -= 7;
				if (playerZ <= 0) {
					playerZ = 0;
					playerY = 256;
					showingBoard = false;
				}
			} else {

				if (playerZ > 0) {
					// update time
					if (++timeCounter == 60) {
						timeCounter = 0;
						time--;
					}

					if (time <= 0) {
						if (time <= -2) {
							fading = true;
							fadingOut = true;
						}
						continue;
					}
				}

				if (playerRestoring) {

					playerX += playerRestoreVx;
					playerZ += playerRestoreVz;

					if (--playerRestoreCount == 0) {
						playerRestoring = false;
						playerFalling = false;
						playerVx = 0;
						playerVy = 0;
						playerVz = 0;
						playerAngle = 0;
						playerY = 256;
						playerRestoreVx = playerX;
						playerRestoreVz = playerZ;
					}

				} else {

					if (playerFalling) {
						if (playerY < -256) {
							playerRestoreCount = 64;
							playerRestoreVx = (playerRestoreVx - playerX) / 64;
							playerRestoreVz = (playerRestoreVz - playerZ) / 64;
							playerRestoring = true;
						}
					} else {
						// update player X-Z plane position
						if (playerVz == 0) {
							if (a[VK_UP]) {
								playerZ++;
							} else if (a[VK_DOWN]) {
								playerZ--;
							}
						} else {
							playerZ += playerVz;
						}
						if (playerVx == 0) {
							if (a[VK_RIGHT]) {
								playerX++;
							} else if (a[VK_LEFT]) {
								playerX--;
							}
						} else {
							playerX += playerVx;
						}
					}

					// apply gravity
					playerVy -= playerGravity;
					playerY += playerVy;
					playerAngle += playerVa;
					if (!playerFalling && playerY <= 0) {

						// test if player is on solid ground
						i = 0;
						z = (int) (playerZ + 48) >> 4;
						if (z >= 0 && z < 128) {
							x = (int) playerX >> 4;
							if (x >= 0 && x < 8 && world[z][x] > 0) {
								i = world[z][x];
							} else {
								x = (int) (playerX - 4) >> 4;
								if (x >= 0 && x < 8 && world[z][x] > 0) {
									i = world[z][x];
								} else {
									x = (int) (playerX + 4) >> 4;
									if (x >= 0 && x < 8 && world[z][x] > 0) {
										i = world[z][x];
									}
								}
							}

							if (i > 0) {
								playerY = 0;
								playerVx = 0;
								playerVz = 0;
								playerAngle = 0;
								playerVa = 0;
								playerVy = JUMP_SPEED;
								playerGravity = GRAVITY;
								i = world[z][x];
								if (i == TILE_SOLID) {
									playerRestoreVx = (x << 4) + 8;
									playerRestoreVz = (z << 4) - 42;
								} else if (i == TILE_EXCLAMATION) {
									world[z][x] = TILE_EMPTY;
								} else if (i == TILE_UP) {
									playerVz = 1;
								} else if (i == TILE_DOWN) {
									playerVz = -1;
								} else if (i == TILE_LEFT) {
									playerVx = -0.5f;
									playerVa = -V_ANGLE;
								} else if (i == TILE_RIGHT) {
									playerVx = 0.5f;
									playerVa = V_ANGLE;
								} else if (i == TILE_DOUBLE_UP) {
									playerVz = 2;
								} else if (i == TILE_DOUBLE_DOWN) {
									playerVz = -2;
								} else if (i == TILE_DOUBLE_LEFT) {
									playerVx = -1.25f;
									playerVa = -V_ANGLE;
								} else if (i == TILE_DOUBLE_RIGHT) {
									playerVx = 1.25f;
									playerVa = V_ANGLE;
								} else if (i == TILE_QUESTION) {
									if (x < 4) {
										playerVx = 0.5f;
										playerVa = V_ANGLE;
									} else {
										playerVx = -0.5f;
										playerVa = -V_ANGLE;
									}
								} else if (i == TILE_PAUSE) {
									playerGravity = HIGH_GRAVITY;
								} else if (i == TILE_GREEN) {
									playerGravity = LOW_GRAVITY;
								} else if (i == TILE_CHECKER) {
									beatLevel = true;
									beatLevelCounter = 64;
								}
							} else {
								playerFalling = true;
							}
						} else {
							playerFalling = true;
						}
					}
				}
			}

			// -- update ends ------------------------------------------------------

		} while (nextFrameStartTime < System.nanoTime());

		// -- render starts ------------------------------------------------------

		// render floor
		int offsetX = (int) playerX;
		int offsetZ = (int) playerZ;
		for (z = 0; z < 65536; z++) {
			pixels[z] = sky[z >> 8][(z - cloudX) & 0xFF];
			if (z > 5120) {
				i = floorMap[z - 5120][0] + offsetX;
				if (i >= 0 && i < 128) {
					j = floorMap[z - 5120][1] + offsetZ;
					if (j >= 0 && j < 2048) {
						k = world[j >> 4][i >> 4];
						if (k > 0) {
							pixels[z] = sprites[k - 1 + (playerY < 0 ? 0 : shadowMap[z - 5120])][0x0F & j][0x0F & i];
						}
					}
				}
			}
		}
		image.setRGB(0, 0, 256, 256, pixels, 0, 256);

		// draw bunny
		g.translate(128, 134 - playerY);
		g.scale(2, 2);
		if (playerAngle != 0) {
			g.rotate(playerAngle);
		}
		g.drawImage(bunnySprite, -7, -21, null);
		g.setTransform(affineTransform);

		if (playerY < 0) {
			// render floor in front of bunny
			for (z = 0; z < 21760; z++) {
				pixels[z] = 0;
				i = floorMap[z + 38656][0] + offsetX;
				if (i >= 0 && i < 128) {
					j = floorMap[z + 38656][1] + offsetZ;
					if (j >= 0 && j < 2048) {
						k = world[j >> 4][i >> 4];
						if (k > 0) {
							pixels[z] = 0xFF000000 | sprites[k - 1][0x0F & j][0x0F & i];
						}
					}
				}
			}
			image2.setRGB(0, 0, 256, 85, pixels, 0, 256);
			g.drawImage(image2, 0, 171, null);
		}

		// draw time remaining
		g.setColor(Color.MAGENTA);
		for (i = 0; i < time; i++) {
			g.fillRect(128 - (i << 1), 246, 1, 8);
			g.fillRect(128 + (i << 1), 246, 1, 8);
		}

		// draw fade
		if (fading) {
			g.setColor(new Color(0, 0, 0, fade));
			g.fillRect(0, 0, 256, 256);
		}

		// -- render ends --------------------------------------------------------

		// show the hidden buffer
		g2.drawImage(image, 0, 0, 512, 512, null);
		
	}

	@Override
	public void processAWTEvent(AWTEvent awtEvent) {
		if (awtEvent instanceof KeyEvent) {
			KeyEvent keyEvent = (KeyEvent) awtEvent;
			final int VK_LEFT = 0x25;
			final int VK_RIGHT = 0x27;
			final int VK_UP = 0x26;
			final int VK_DOWN = 0x28;
			final int VK_W = 0x57;
			final int VK_S = 0x53;
			final int VK_A = 0x41;
			final int VK_D = 0x44;

			int k = keyEvent.getKeyCode();
			a[k == VK_W ? VK_UP : k == VK_D ? VK_RIGHT : k == VK_A ? VK_LEFT : k == VK_S ? VK_DOWN : k] = keyEvent.getID() != 402;
		}
	}

//	// to run in window, uncomment below
//	public static void main(String[] args) throws Throwable {
//		javax.swing.JFrame frame = new javax.swing.JFrame("Boing 4K");
//		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//		a applet = new a();
//		applet.setPreferredSize(new java.awt.Dimension(512, 512));
//		frame.add(applet, java.awt.BorderLayout.CENTER);
//		frame.setResizable(false);
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//		Thread.sleep(250);
//		applet.start();
//	}
}
