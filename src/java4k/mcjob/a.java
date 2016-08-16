package java4k.mcjob;

/*
 * McJob
 * Copyright (C) 2012 meatfighter.com
 *
 * This file is part of McJob.
 *
 * McJob is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * McJob is distributed in the hope that it will be useful,
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

		final int SPRITE_DIGIT_0 = 0;
		final int SPRITE_DIGIT_1 = 1;
		final int SPRITE_DIGIT_2 = 2;
		final int SPRITE_DIGIT_3 = 3;
		final int SPRITE_DIGIT_4 = 4;
		final int SPRITE_DIGIT_5 = 5;
		final int SPRITE_DIGIT_6 = 6;
		final int SPRITE_DIGIT_7 = 7;
		final int SPRITE_DIGIT_8 = 8;
		final int SPRITE_DIGIT_9 = 9;
		final int SPRITE_LETTER_L = 10;
		final int SPRITE_LETTER_E = 11;
		final int SPRITE_LETTER_V = 12;
		final int SPRITE_LETTER_G = 13;
		final int SPRITE_LETTER_A = 14;
		final int SPRITE_LETTER_M = 15;
		final int SPRITE_LETTER_R = 16;
		final int SPRITE_EXTRA_LIFE = 17;
		final int SPRITE_BURGER = 18;
		final int SPRITE_DRINK = 19;
		final int SPRITE_FRIES = 20;
		final int SPRITE_CHICKEN = 21;
		final int SPRITE_COIN = 22;
		final int SPRITE_FLOOR_TILE = 23;
		final int SPRITE_CUSTOMER = 24;
		final int SPRITE_BUBBLE_QUOTE = 25;
		final int SPRITE_PLAYER_STANDING = 26;
		final int SPRITE_PLAYER_HOLDING = 27;
		final int SPRITE_TRAY = 28;
		final int SPRITE_TITLE = 29;

		final int POINTS_PER_EXTRA_LIFE = 50000;

		final int POINTS_X = 0;
		final int POINTS_Y = 1;
		final int POINTS_VALUE = 2;
		final int POINTS_COUNTER = 3;

		final int SLIDER_STATE_DOWN = 0;
		final int SLIDER_STATE_LEFT = 1;
		final int SLIDER_STATE_RIGHT = 2;
		final int SLIDER_STATE_UP = 3;

		final int SLIDER_X = 0;
		final int SLIDER_Y = 1;
		final int SLIDER_TRAY = 2;
		final int SLIDER_HOLDING = 3;
		final int SLIDER_COLUMN = 4;
		final int SLIDER_STATE = 5;

		final int CUSTOMER_X = 0;
		final int CUSTOMER_Y = 1;
		final int CUSTOMER_SPRITE_INDEX = 2;
		final int CUSTOMER_WALK_INDEX = 3;
		final int CUSTOMER_ITEM = 4;
		final int CUSTOMER_BUBBLE_COUNTER = 5;
		final int CUSTOMER_COLUMN = 6;

		final int VK_LEFT = 0x25;
		final int VK_RIGHT = 0x27;
		final int VK_UP = 0x26;
		final int VK_DOWN = 0x28;
		final int VK_START = 0x0a;
		final int VK_W = 0x57;
		final int VK_S = 0x53;
		final int VK_A = 0x41;
		final int VK_D = 0x44;
		final int VK_PAUSE = 0x50;

		final int COLOR_TRANSPARENT = 0x00000000;
		final int COLOR_RED = 0xFFFF0000;
		final int COLOR_ORANGE = 0xFFFFB800;
		final int COLOR_YELLOW = 0xFFFFFF00;
		final int COLOR_GREEN = 0xFF68B800;
		final int COLOR_BROWN = 0xFFDE6800;
		final int COLOR_VANILLA = 0xFFFFFFDE;
		final int COLOR_GRAY = 0xFFB8B897;
		final int COLOR_BLACK = 0xFF000000;
		final int COLOR_WHITE = 0xFFFFFFFF;
		final int COLOR_FLESH = 0xFFFFD9AD;
		final int COLOR_DARK_GRAY = 0xFF5B5B4C;
		final int COLOR_BLUE = 0xFF0064A2;
		final int COLOR_DARK_BLUE = 0xFF154B71;
		final int COLOR_DARK_BROWN = 0xFF9E4900;

		final int CLR_TRANSPARENT = 0;
		final int CLR_RED = 1;
		final int CLR_ORANGE = 2;
		final int CLR_YELLOW = 3;
		final int CLR_GREEN = 4;
		final int CLR_BROWN = 5;
		final int CLR_VANILLA = 6;
		final int CLR_GRAY = 7;
		final int CLR_BLACK = 8;
		final int CLR_WHITE = 9;
		final int CLR_FLESH = 10;
		final int CLR_DARK_GRAY = 11;
		final int CLR_BLUE = 12;
		final int CLR_DARK_BLUE = 13;
		final int CLR_DARK_BROWN = 14;

		final int[] COLORS = { COLOR_TRANSPARENT, COLOR_RED, COLOR_ORANGE, COLOR_YELLOW, COLOR_GREEN, COLOR_BROWN, COLOR_VANILLA, COLOR_GRAY, COLOR_BLACK, COLOR_WHITE, COLOR_FLESH, COLOR_DARK_GRAY,
				COLOR_BLUE, COLOR_DARK_BLUE, COLOR_DARK_BROWN, };

		final String S = "aaiiiiaaaijjjjiaijjiijjiijjiijjiijjiijjiijjiijjiaijjjjiaa" + "aiiiiaaaaaiiaaaaaijjiaaaijjjiaaaaijjiaaaaijjiaaaaijjiaaaijjjjiaaaiii"
				+ "iaaaaiiiiaaaijjjjiaijjiijjiaiiijjiaaaijjiaaaijjiiiaijjjjjjiaiiiiiiaa" + "aiiiiiaaijjjjjiaaiijjiaaaijjjiaaiiiijjiijjiijjiaijjjjiaaaiiiiaaaaaai"
				+ "iaaaaaijjiaaaijjjiaaijijjiaijjijjiaijjjjjjiaiiijjiaaaaaiiaaiiiiiiiai" + "jjjjjiaijjiiiiaijjjjjiaiiiiijjiiiiiijjiijjjjjiaiiiiiiaaaaaiiiaaaaijj"
				+ "jiaaijjiiaaijjjjjiaijjiijjiijjiijjiaijjjjiaaaiiiiaaiiiiiiiiijjjjjjii" + "jjiijjiiiiijjiaaaijjiaaaaijjiaaaaijjiaaaaiiiiaaaaiiiiaaaijjjjiaijjii"
				+ "jjiaijjjjiaijjiijjiijjiijjiaijjjjiaaaiiiiaaaaiiiiaaaijjjjiaijjiijjii" + "jjiijjiaijjjjjiaaiijjiaaijjjiaaaaiiiaaaaiiaaaaaijjiaaaaijjiaaaaijjia"
				+ "aaaijjiiiaaijjjjjiaijjjjjiaaiiiiiaaaiiiiiaaijjjjjiaijjiiiaaijjjiaaai" + "jjiiiaaijjjjjiaijjjjjiaaiiiiiaaaiiaiiaaijjijjiaijjijjiaijjijjiaaijjj"
				+ "iaaaijjjiaaaaijiaaaaaaiaaaaaaiiiaaaaijjjiaaijjiijiaijjiiiaaijjijjiai" + "jjiijiaaijjjiaaaaiiiaaaaaiiiaaaaijjjiaaijjijjiaijjijjiaijjjjjiaijjjj"
				+ "jiaijjijjiaaiiaiiaaaiiaiiaaijjijjiaijjjjjiaijjjjjiaijijijiaijijijiai" + "jiiijiaaiaaaiaaaiiiiiaaijjjjiiaijjijjiaijjjjjiaijjjjiaaijjijjiaijjij"
				+ "jiaaiiaiiiaiiiiiiiiaiggggiaaiggggiafiiiiiifififfifiikikkikiaikkkkiaa" + "aiiiiaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacccccccca"
				+ "aaaaaccccccccccccaaaccccccccccccccaaccccccccccccccaaccccccccccccccaa" + "bebbbbbbbebbeeeaeeeeeeeeeeeeeeaeeeffffffffeeeeaabffffffffffffbaeebbb"
				+ "eeebbbbbefeeeeebbbbbeeffefeaeeeefffffefeeeaaaaccccccccceaaaaaahhhhhh" + "hhhhhaaaahgfffffffffghaaaahghggggggghaaaaahghhbbbggghaaaaahghhbbbggg"
				+ "haaaaahghhbbbggghaaaaahghhbbbggghaaaaaahghhgggghaaaaaaahghhgggghaaaa" + "aaahghhgggghaaaaaaahghhgggghaaaaaaahghhhggghaaaaaaahghhhggghaaaaaaah"
				+ "ghhhhgghaaaaaaaahggggghaaaaaaaaahhhhhhhaaaaaaaaaacaaaaaaaaaaaaaaadaa" + "aacaaaaaaaaadcacaadacaaaacacadddcadadaaaadadddccdccddaaaacddcccdcdcd"
				+ "daaaccccddccdcdcccaabbbcdcddcdcbbbaabbbccdcccdcbbbaabbbbcdcdccbbbbaa" + "abbbbccccbbbbaaaabbbbbbbbbbbbaaaabbbbbbbbbbbbaaaaabbbbbbbbbbaaaaaabb"
				+ "bbbbbbbbaaaaaabbbbbbbbbbaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaffaaaaaaaaaaaaafdcfaaaaaaaaaaffcccfaaaaafffffccc"
				+ "cfaaafffccccccccfaaafcdccccccccfaaaafdddccccccfaaaafcddccccccfaaaaaf" + "ddcccccccfaaaaafcdddccccfaaaaaaafcdccccfaaaaaaaaafcccffaaaaaaaaaaaff"
				+ "faaaaaaaaaaaaaaaiiiiiaaaaaaaaaiidddddiiaaaaaaiddddfddddiaaaaiddddfff" + "dddfiaaaidddfdfdfdddiiaiddddfdfdddddfiaidddddfffddddfiaiddddddfdfddd"
				+ "fiaiddddddfdfdddfiaiddddfdfdfdddfiaaiddddfffddddiiaaidddddfddddfiaaa" + "iiddddddddfiiaaaaiiifffffiiiaaaaaaiiiiiiiiiaaaaaaaaaiiiiiiaaaaaooooo"
				+ "oooofffffoooffoffoofffffffofffffffofffffffofffffffofffffffofffffffof" + "ffffffofffffffofffffffofffffffooffoffooofffffoooooooooooooooooooofff"
				+ "ffoooffoffoofffffffofffffffofffffffofffffffofffffffofffffffofffffffo" + "fffffffofffffffofffffffooffoffooofffffoooooooooaaaiiiiiiaaaaaibbffbb"
				+ "iaaaibbfbbfbbiaaibfbbbbffiaaifbbbbbbfiaaifbbbbbbfiaaifbbbbbbfiaikhff" + "ffffhkiikhhhhhhhhkiaihhhhhhhhiaaaihiiiihiaaaaiibbbbiiaaaibbbbbbbiaaa"
				+ "ibbibbbibiaaibbibbbikiaaikkiiiibiaaaaibbiiiiiaaaaibbibbbiaaaaaiiibbb" + "iaaaaaaaaiiiaaaaaiiiiiiiiiiiiiiiiaaaijjjjjjjjjjjjjjjjiaijjjjjjjjjjjj"
				+ "jjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjii" + "jjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjj"
				+ "jjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjj" + "jjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiijjjj"
				+ "jjjjjjjjjjjjjjiijjjjjjjjjjjjjjjjjjiaijjjjjjjjjjjjjjjjiaaaiiiiiiiijjj" + "jjiiiaaaaaaaaaaaaijjjiaaaaaaaaaaaaaaaijjiaaaaaaaaaaaaaaaijjiaaaaaaaa"
				+ "aaaaaaaiiiiaaaaaaaaaaaiiiiiiiiiiiiiiiiiiiiiaaaaaaigggggggggggggggggg" + "giaaaaaaigggggggggggggggggggiaaaaaaigggggggggggggggggggiaaaaaaaiggig"
				+ "gigggggiggiggiaaaaaaaaiggiggigggggiggiggiaaaaaaaaiggiggigggggiggiggi" + "aaaaaaaaiggiggigggggiggiggiaaaaaaafiggiggigggggiggiggiaaaaaafffiiigg"
				+ "igggggiggiiiffaaaaaififffiiiiiiiiiiiffffiaaaaaaiiffffffffffffffffffi" + "aaaaaaikffkkiikfffkiikffikiaaaaaaikffkkiikffkkiikkfikiaaaaaaaiikkkii"
				+ "kkkkkiikkfiiaaaaaaaaaikkkiikkkkkiikkkiaaaaaaaaaaiikkkkkkkkkkkkkiiaaa" + "aaaaaaiggikkkkkkkkkkkiggiaaaaaaiiggggikkkkkkkkkiggggiiaaaigggggggiii"
				+ "iiiiiigggggggiaaigggggggggficifgggggggggiaigggiiiggffcicicffggiiiggg" + "iiggikkiifccciiicccfiikkiggiiggikkkifccfgigfccfikkkiggiiggikkkifccfg"
				+ "ggfccfikkkiggiaigikkiggffgggggffggikkigiaaaiiiigggggggggggggggiiiiaa" + "aaaaiiiiiiiiiiiiiiiiiiiiaaaaaaikkkkkigggggggikkkkkiaaaaaikkkkiiggggg"
				+ "ggggiikkkkiaaaiiiiiigggggggggggggiiiiiiaaigggiiiggigggggiggiiigggiai" + "ggggigiggigggggiggigiggggiigggiggiggigggggiggiggigggiigggiggiggigggg"
				+ "giggiggigggiigggiggiggigggggiggiggigggiigggiiiiggigggggiggiiiigggiig" + "ggifffiiiiiiiiiiifffigggiigggifffffffffffffffffigggiigggiffkkiikfffk"
				+ "iikffiigggiigggiffkkiikffkkiikkfiigggiigggiikkkiikkkkkiikkfiigggiigg" + "ggikkkiikkkkkiikkkiggggiaigggiikkkkkkkkkkkkkiigggiaaigggggikkkkkkkkk"
				+ "kkigggggiaaaigggggikkkkkkkkkigggggiaaaaaigggggiiiiiiiiigggggiaaaaaaa" + "iggggggficifggggggiaaaaaaaaaigggffcicicffgggiaaaaaaaaaaiggfccciiiccc"
				+ "fggiaaaaaaaaaaiggfccfgigfccfggiaaaaaaaaaaiggfccfgggfccfggiaaaaaaaaaa" + "igggffgggggffgggiaaaaaaaaaaigggggggggggggggiaaaaaaannnnnnnnnnnnnnnna"
				+ "aannnnnnnnnnnnnnnnnnannnmmmmmmmmmmmmmmnnnnnmmmmmmmmmmmmmmmmnnnmmmmmm" + "mmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmm"
				+ "mmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmm" + "mmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmm"
				+ "mmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmm" + "nnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmmmmmmmmmmmmmmnnmmmmmm"
				+ "mmmmmmmmmmmmnnnmmmmmmmmmmmmmmmmnnnnnmmmmmmmmmmmmmmnnnannnnnnnnnnnnnn" + "nnnnaaannnnnnnnnnnnnnnnaaaaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacccccccccccccccccccccccccaaa" + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacccccccccccccccccccgcccccccccccaaaaaa"
				+ "aaaaaaaaaaaaaaaaaaaaaaacccccccccccccccgccccccccccccccccccccaaaaaaaaa" + "aaaaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccaaaaaaaaaaaa"
				+ "aaaaaaaaacccccccccgccccccccccccccccccccccccccccccggcaaaaaaaaaaaaaaaa" + "aaaccccccccccccccccccccccgccccccccccccccccccccccaaaaaaaaaaaaaaaaaccc"
				+ "cccccccccccccccccccccccccccgccccccccccccccccaaaaaaaaaaaaaaaacccccccc" + "ccccccccgccccccccccccccccccccccccccccccccaaaaaaaaaaaaacccccccccccccc"
				+ "ccccccccccccccccccccccccccccccccccccaaaaaaaaaaaacccccccccccccccccccc" + "cccccccccccccccccccccccgccccccccaaaaaaaaaaaccccccccccccccccccccccccc"
				+ "ccccccccccccccccccccccccccccaaaaaaaaaccccccccccccccccccccccccccccccc" + "ccccccccccccccccccccccccaaaaaaaacccccccccccccccccccccccccccccccccccg"
				+ "cccccccccccccccccccaaaaaaaaccccccccgcccccccccccccccccccccccccccccccc" + "ccccccccccccccaaaaaaaacccccccccccccccccccccccccgcccccccccccccccccccc"
				+ "cccccccccaaaaaaaaccccccccccccccccccccccccccccccccccccccccccccccccccc" + "cccaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccccccccccccccaa"
				+ "aaaaaaaacccccccccccccccccccccccccccccccccccccccccgcccccccfffaaaaaaaa" + "aaaffffcccccccccccccccccccccccccccccccccccccccccffffffffaeeaaaaaafff"
				+ "fffffffffffffffffffffffffffffffffffffffffffffffffffeeeaaaaaaffffffff" + "ffffffffffffffffffffffffffffffffffffffffffffffeeaaaaaaafffffffffffff"
				+ "ffffffffffffffffffffffffffffffffffffffffeeeeeaaeeafffiiifffffiiiffff" + "ffffffffffffiiffffffffffffiiffffffeeeeeeaaeeaaaibbbifffibbbiffffffff"
				+ "ffffffibbiffffffffffibbifeeceeeceeaaaaeeeeibbbifffibbbiffffffffffddd" + "dibbiefffffffffibbieeeeeeeeeeaaaaaaacibbbbifibbbbidffiiiieedddddibbi"
				+ "eeeiiiieeeibbiiiiccccceeaaaaaaccibbbbidibbbbidfibbbbiedddddibbieeibb" + "bbieeibbibbbiccccceeaaaaaccibbibidibibbieibbbbbbieddddibbicibbbbbbie"
				+ "ibbbbbbbiccccaaaaaaaceibbibbibbibbiibbbiibbieecddibbiibbbiibbbiibbbi" + "ibbbicccaaaaaaeeeibbibbibbibbiibbicciiccccccibbiibbiccibbiibbiccibbi"
				+ "ccaaaaaeeeecibbibbibbibbiibbiccecciicccibbiibbiccibbiibbiccibbiccaaa" + "aaaaaacibbiibbbiibbiibbicciiibbiccibbiibbiccibbiibbiccibbicaaaaaaaaa"
				+ "aaibbiibbbiibbiibbbiibbibbbiibbbiibbbiibbbiibbbiibbbifaaaaaaaaaffibb" + "iibbbiibbicibbbbbbiibbbbbbiccibbbbbbicibbbbbbbifffaaaaaaaaffibbicibi"
				+ "cibbiccibbbbiccibbbbiccccibbbbiccibbibbbiffffaaaaaaaffffiiffficcciic" + "ccciiiicccciiiicccccciiiicdddiifiiifffffaaaaaaafffffffffffffffcccccc"
				+ "ccccccccccccccccfffffdfffffffffffffaaaaaaaafffffffffffffffffdddffddf" + "fffffffffffffffffffffffffffffddaaaeeeefffffffffffffffffffffddfffffff"
				+ "fffffffffffffffffffffffddddaaeeeeeeefeeddfffffffffffffffffffffffffff" + "ffffffffffffffffdddddddaaeeeeeeeeddddeffffffffffffffffffffffffffffff"
				+ "fffffffdddddddddddaeeeeeeeeedddddffffffffffffffffffffffffffffffeefff" + "eedddddddddddaeeeeeeeccdddddcddddddddddddddddddddddddddddeeeefceeeee"
				+ "eccddddddaaaeeeecceedceedddddddddddddddddddddddddddeeeeecceeeeeccccc" + "ddddaaaeeeeccceeeeeedddddddddddddddddddeeeeceeeeeeeecccceeccccaaaaaa"
				+ "aaaaaecccceeeeeedddddddddddcccccccceeeeccceeceecccceeccccaaaaaaaaaaa" + "accccceeeeecddddddccccccccccccceeecccccceeccceeecccaaaaaaaaaaaaaaacc"
				+ "cccccccdddcccccccccccccccceeeecccccccccceecccaaaaaaaaaaaaaaaaacccccc" + "cccccccccccccccccccccceeeccccccccccccccaaaaaaaaaaaaaaaaaaaaccccccccc"
				+ "cccccccccccccccccceeccccccccccccaaaaaaaaaaaaaaaaaaaaaaaacccccccccccc" + "cccccccccccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccc"
				+ "ccccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccc" + "ccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacccccccccaaaaaaaa" + "aaaaaaaaaaaaaaaaaaaaaa";

		Random random = new Random();
		BufferedImage image = new BufferedImage(256, 300, BufferedImage.TYPE_INT_RGB);
		BufferedImage[] sprites = new BufferedImage[30];
		BufferedImage[][] customerSprites = new BufferedImage[8][2];
		Graphics2D g = (Graphics2D) image.getGraphics();
		Graphics2D g2 = null;

		ArrayList<int[]> customers = new ArrayList<int[]>();
		ArrayList<int[]> sliders = new ArrayList<int[]>();
		ArrayList<int[]> points = new ArrayList<int[]>();

		int i;
		int j;
		int k;
		int m;
		int x;
		int y;
		int z;

		int lives = 7;
		int level = 1;
		int playerX = 34;
		int playerHolding = -1;

		int walkCounter = 0;
		int walkRate = 0;
		int customerCount = 0;
		int spawnDelay = 0;
		int timer = 0;
		int loseLife = 0;
		int beatLevel = 0;
		int gameOver = 0;
		int playerRunning = 0;
		int score = 0;

		int[] customer = null;
		int[] slider = null;
		int[] point = null;
		boolean paused = false;
		boolean showTitle = true;
		boolean keysReleased = true;
		boolean[] columnBlinking = new boolean[4];
		float gameOverScale = 0;

		final Color[] CLRS = new Color[15];
		for (i = 0; i < 15; i++) {
			CLRS[i] = new Color(COLORS[i], true);
		}
		Color darkGray = CLRS[CLR_DARK_GRAY];

		// decompress sprites
		for (m = i = 0; i < 30; i++) {
			k = z = 8;
			if (i >= SPRITE_BURGER && i < SPRITE_CUSTOMER) {
				k = z = 16;
			} else if (i == SPRITE_CUSTOMER) {
				k = 12;
				z = 20;
			} else if (i == SPRITE_BUBBLE_QUOTE) {
				k = 20;
				z = 24;
			} else if (i == SPRITE_TRAY) {
				k = 20;
				z = 26;
			} else if (i == SPRITE_TITLE) {
				k = 63;
				z = 55;
			} else if (i >= SPRITE_PLAYER_STANDING) {
				k = z = 27;
			}

			sprites[i] = new BufferedImage(k, z, BufferedImage.TYPE_INT_ARGB_PRE);
			for (y = 0; y < z; y++) {
				for (x = 0; x < k; x++) {
					sprites[i].setRGB(x, y, COLORS[S.charAt(m++) - 'a']);
				}
			}
		}

		// create customer sprites
		for (i = 0; i < 8; i++) {
			customerSprites[i][0] = new BufferedImage(12, 20, BufferedImage.TYPE_INT_ARGB_PRE);
			customerSprites[i][1] = new BufferedImage(12, 20, BufferedImage.TYPE_INT_ARGB_PRE);
			for (y = 0; y < 20; y++) {
				for (x = 0; x < 12; x++) {
					j = sprites[SPRITE_CUSTOMER].getRGB(x, y);
					if (j == COLOR_RED) {
						j = Color.HSBtoRGB(i / 8f, 1, 1);
					}
					customerSprites[i][0].setRGB(x, y, j);
					customerSprites[i][1].setRGB(11 - x, y, j);
				}
			}
		}

		long nextFrameStartTime = System.nanoTime();
		while (true) {

			do {
				nextFrameStartTime += 16666667;

				// -- update starts ----------------------------------------------------

				if (!(a[VK_UP] || a[VK_DOWN] || a[VK_LEFT] || a[VK_RIGHT] || a[VK_START] || a[VK_PAUSE])) {
					keysReleased = true;
				}

				if (keysReleased && a[VK_PAUSE]) {
					keysReleased = false;
					paused = !paused;
				}
				if (paused) {
					// game paused
					continue;
				}

				if (showTitle || gameOver > 0) {
					gameOverScale = (float) (1 + 0.01f * (1 + Math.cos(.03f * gameOver)) * gameOver);
					if (showTitle || --gameOver == 0) {
						gameOver = 1;
						if (keysReleased && (a[VK_UP] || a[VK_DOWN] || a[VK_LEFT] || a[VK_RIGHT] || a[VK_START])) {

							// reset game

							keysReleased = false;
							showTitle = false;

							gameOver = 0;
							lives = 7;
							level = 1;
							playerX = 34;
							playerHolding = -1;

							walkCounter = 0;
							walkRate = 0;
							customerCount = 0;
							spawnDelay = 0;
							timer = 0;
							loseLife = 0;
							beatLevel = 0;
							playerRunning = 0;
							score = 0;

							columnBlinking = new boolean[4];

							customers.clear();
							sliders.clear();
							points.clear();
						}
					}
					continue;
				}

				// update timer
				timer++;

				// update lose life flash
				darkGray = CLRS[CLR_DARK_GRAY];
				if (beatLevel == 0 && customers.size() == 0 && sliders.size() == 0 && points.size() == 0 && customerCount >= 32 + (level << 3)) {
					beatLevel = 300;
				}
				if (loseLife > 0) {
					float m1 = (float) ((1 - Math.cos(0.196f * loseLife)) / 2);
					float m2 = 1 - m1;

					darkGray = new Color((int) (0x5B * m2 + 0xFF * m1), (int) (0x5B * m2), (int) (0x4C * m2));

					if (((--loseLife) & 31) == 0) {
						if (lives == 0) {

							// GAME OVER

							gameOver = 600;
						} else {
							--lives;
						}
					}
				} else if (beatLevel > 0) {
					if (--beatLevel == 0) {
						level++;
						if (++lives > 7) {
							lives = 7;
						}
						customerCount = 0;
					}
					playerHolding = -1;
					darkGray = Color.getHSBColor(beatLevel / 60f, 1, 1);
				}

				// update floating points
				for (i = points.size() - 1; i >= 0; i--) {
					point = points.get(i);
					if (--point[POINTS_COUNTER] == 0) {
						points.remove(i);
					} else if ((timer & 1) == 0) {
						point[POINTS_Y]--;
					}
				}

				// update customers
				for (i = 0; i < 4; i++) {
					columnBlinking[i] = false;
				}
				if (++walkCounter >= walkRate) {
					walkCounter = 0;
				}
				for (i = customers.size() - 1; i >= 0; i--) {
					customer = customers.get(i);
					if (customer[CUSTOMER_Y] < 130) {
						columnBlinking[customer[CUSTOMER_COLUMN]] = true;
					}
					if (walkCounter == 0) {
						customer[CUSTOMER_WALK_INDEX] ^= 1;
						if (--customer[CUSTOMER_Y] < 80) {
							// player failed to service a customer in time
							customers.remove(i);
							loseLife += 32;
							break;
						}
					}
					if (++customer[CUSTOMER_BUBBLE_COUNTER] == 256) {
						customer[CUSTOMER_BUBBLE_COUNTER] = 0;
					}
				}

				// create customers
				if (--spawnDelay < 0 && customerCount < 32 + (level << 3)) {
					customerCount++;

					// walk rate increases as level progresses
					i = 16 - (level >> 1);
					if (i < 8) {
						i = 8;
					}
					float fraction = customerCount / (float) (32 + (level << 1));
					walkRate = (int) (4 * fraction + i * (1 - fraction));
					if (walkRate < 4) {
						walkRate = 4;
					}
					spawnDelay = walkRate * 20 + random.nextInt(walkRate * 20);
					customer = new int[16];
					customer[CUSTOMER_COLUMN] = random.nextInt(4);
					customer[CUSTOMER_X] = customer[CUSTOMER_COLUMN] << 6;
					customer[CUSTOMER_Y] = 300;
					customer[CUSTOMER_SPRITE_INDEX] = random.nextInt(8);
					customer[CUSTOMER_WALK_INDEX] = random.nextInt(2);
					customer[CUSTOMER_ITEM] = random.nextInt(4);
					customers.add(customer);
				}

				// update queue
				for (i = sliders.size() - 1; i >= 0; i--) {
					slider = sliders.get(i);
					if (slider[SLIDER_STATE] == SLIDER_STATE_DOWN) {
						slider[SLIDER_Y] += 4;
						if (slider[SLIDER_Y] > 300) {
							sliders.remove(i);
							if (slider[SLIDER_HOLDING] >= 0) {
								// player send the wrong tray and no customer collected it
								loseLife += 32;
							}
						} else {

							// test for collsion with customer
							for (j = customers.size() - 1; j >= 0; j--) {
								customer = customers.get(j);
								k = customer[CUSTOMER_Y] - slider[SLIDER_Y] - 3;
								if (customer[CUSTOMER_ITEM] == slider[SLIDER_HOLDING] && customer[CUSTOMER_COLUMN] == slider[SLIDER_COLUMN] && k <= 5 && k >= -5) {
									slider[SLIDER_Y] = customer[CUSTOMER_Y] + 3;
									slider[SLIDER_STATE]++;
									k = 100 + 3 * customer[CUSTOMER_Y];
									if (lives < 7 && (score + k) / POINTS_PER_EXTRA_LIFE != score / POINTS_PER_EXTRA_LIFE) {
										lives++;
									}
									score += k;
									points.add(point = new int[16]);
									point[POINTS_X] = (customer[CUSTOMER_COLUMN] << 6) + 20;
									point[POINTS_Y] = customer[CUSTOMER_Y] + 6;
									point[POINTS_VALUE] = k;
									point[POINTS_COUNTER] = 64;
									customers.remove(j);
									break;
								}
							}
						}
					} else if (slider[SLIDER_STATE] == SLIDER_STATE_LEFT) {
						if (slider[SLIDER_X] > (slider[SLIDER_COLUMN] << 6) + 6) {
							slider[SLIDER_X] -= 8;
						} else {
							slider[SLIDER_STATE]++;
							slider[SLIDER_HOLDING] = -1;
						}
					} else if (slider[SLIDER_STATE] == SLIDER_STATE_RIGHT) {
						if (slider[SLIDER_X] < (slider[SLIDER_COLUMN] << 6) + 32) {
							slider[SLIDER_X] += 8;
						} else {

							slider[SLIDER_STATE] = SLIDER_STATE_DOWN;

							if (random.nextInt(7) < level) {
								slider[SLIDER_STATE] = SLIDER_STATE_UP;
							} else if (random.nextInt(7) > level) {
								// create coin
								x = slider[SLIDER_X] + 2;
								y = slider[SLIDER_Y] - 4;
								z = slider[SLIDER_COLUMN];
								sliders.add(slider = new int[16]);
								slider[SLIDER_X] = x;
								slider[SLIDER_Y] = y;
								slider[SLIDER_TRAY] = 0;
								slider[SLIDER_COLUMN] = z;
								slider[SLIDER_STATE] = SLIDER_STATE_UP;
							}
						}
					} else {

						if (--slider[SLIDER_Y] <= 64) {
							sliders.remove(i);
							if (slider[SLIDER_COLUMN] == (playerX - 34) >> 6) {
								// player collects empty tray or coin at top
								points.add(point = new int[16]);
								point[POINTS_X] = playerX + 17;
								point[POINTS_Y] = 60;
								point[POINTS_COUNTER] = 64;
								k = point[POINTS_VALUE] = slider[SLIDER_TRAY] == 1 ? 100 : 500;
								if (lives < 7 && (score + k) / POINTS_PER_EXTRA_LIFE != score / POINTS_PER_EXTRA_LIFE) {
									lives++;
								}
								score += k;
							} else if (slider[SLIDER_TRAY] == 1) {
								// player failed to catch returned tray
								loseLife += 32;
							}
						}

						// test for collision with other slider
						for (j = sliders.size() - 1; j > i; j--) {
							customer = sliders.get(j);
							k = customer[CUSTOMER_Y] - slider[SLIDER_Y] - 3;
							if (slider[SLIDER_COLUMN] == customer[SLIDER_COLUMN] && customer[SLIDER_STATE] == SLIDER_STATE_DOWN && k <= 8 && k >= -8) {
								sliders.remove(i);
								// player collects empty tray or coin by collision with tray
								points.add(point = new int[16]);
								point[POINTS_X] = slider[SLIDER_X] + 12;
								point[POINTS_Y] = slider[SLIDER_Y] + 8;
								point[POINTS_COUNTER] = 64;
								k = point[POINTS_VALUE] = slider[SLIDER_TRAY] == 1 ? 200 : 800;
								if (lives < 7 && (score + k) / POINTS_PER_EXTRA_LIFE != score / POINTS_PER_EXTRA_LIFE) {
									lives++;
								}
								score += k;
								break;
							}
						}
					}
				}

				// update player
				if (playerRunning > 0) {
					playerRunning -= 16;
					playerX -= 16;
				} else if (playerRunning < 0) {
					playerRunning += 16;
					playerX += 16;
				} else if (keysReleased) {
					if (a[VK_LEFT]) {
						keysReleased = false;
						if (playerX > 34) {
							playerRunning = 64;
						}
					} else if (a[VK_RIGHT]) {
						keysReleased = false;
						if (playerX < 224) {
							playerRunning = -64;
						}
					} else if (beatLevel == 0 && a[VK_UP]) {
						keysReleased = false;
						if (playerHolding < 0) {
							playerHolding = (playerX - 34) >> 6;
						} else if (playerX == (playerHolding << 6) + 34) {
							playerHolding = -1;
						}
					} else if (beatLevel == 0 && a[VK_DOWN]) {
						keysReleased = false;
						if (playerHolding >= 0) {
							// create tray with food on it
							sliders.add(slider = new int[16]);
							slider[SLIDER_X] = playerX + 4;
							slider[SLIDER_Y] = 64;
							slider[SLIDER_TRAY] = 1;
							slider[SLIDER_HOLDING] = playerHolding;
							slider[SLIDER_COLUMN] = (playerX - 34) >> 6;
							playerHolding = -1;
						}
					}
				}

				// -- update ends ------------------------------------------------------

			} while (nextFrameStartTime < System.nanoTime());

			// -- render starts ------------------------------------------------------

			if (showTitle) {
				// draw title
				g.setColor(CLRS[CLR_BLACK]);
				g.fillRect(0, 0, 256, 300);
				g.drawImage(sprites[SPRITE_TITLE], 65, 95, 126, 110, null);
			} else {

				// draw background
				for (y = 96; y < 300; y += 16) {
					for (i = 0; i < 256; i += 64) {
						g.drawImage(sprites[SPRITE_FLOOR_TILE], i, y, null);
						g.drawImage(sprites[SPRITE_FLOOR_TILE], i + 16, y, null);
					}
				}

				g.setColor(darkGray);
				g.fillRect(0, 0, 256, 64);

				for (i = 0; i < 4; i++) {
					g.setColor(CLRS[columnBlinking[i] && (timer & 4) == 0 ? CLR_WHITE : CLR_GRAY]);
					g.fillRect(i << 6, 81, 32, 15);
				}

				g.setColor(CLRS[CLR_BLACK]);
				g.drawRect(0, 64, 255, 16);
				g.drawRect(0, 80, 255, 3);
				g.drawRect(0, 80, 255, 16);
				for (i = 0; i < 4; i++) {
					g.drawRect(32 + (i << 6), 80, 31, 235);
				}
				g.drawRect(28, 14, 224, 19);
				g.fillRect(28, 14, 224, 16);

				g.setColor(CLRS[CLR_VANILLA]);
				g.fillRect(1, 65, 254, 15);
				for (i = 0; i < 4; i++) {
					g.fillRect(33 + (i << 6), 80, 30, 235);
				}

				// draw menu
				for (i = 0; i < 4; i++) {
					if (i != playerHolding) {
						g.drawImage(sprites[SPRITE_BURGER + i], 40 + (i << 6), 16, null);
					}
				}

				// draw held food
				if (playerHolding >= 0) {
					g.drawImage(sprites[SPRITE_BURGER + playerHolding], playerX + 6, 21, null);
				}

				// draw objects
				for (i = sliders.size() - 1; i >= 0; i--) {
					slider = sliders.get(i);
					g.drawImage(sprites[slider[SLIDER_TRAY] == 1 ? SPRITE_TRAY : SPRITE_COIN], slider[SLIDER_X], slider[SLIDER_Y], null);
					if (slider[SLIDER_TRAY] == 1 && slider[SLIDER_HOLDING] >= 0) {
						g.drawImage(sprites[SPRITE_BURGER + slider[SLIDER_HOLDING]], slider[SLIDER_X] + 2, slider[SLIDER_Y] + 4, null);
					}
				}

				// draw player
				g.drawImage(sprites[playerHolding < 0 ? SPRITE_PLAYER_STANDING : SPRITE_PLAYER_HOLDING], playerX, 37, null);

				// draw customers
				for (i = 0; i < customers.size(); i++) {
					customer = customers.get(i);
					g.drawImage(customerSprites[customer[CUSTOMER_SPRITE_INDEX]][customer[CUSTOMER_WALK_INDEX]], customer[CUSTOMER_X] + 10, customer[CUSTOMER_Y], null);
					if (customer[CUSTOMER_Y] < 279 && customer[CUSTOMER_BUBBLE_COUNTER] < 128) {
						g.drawImage(sprites[SPRITE_BUBBLE_QUOTE], customer[CUSTOMER_X] + 6, customer[CUSTOMER_Y] - 25, null);
						g.drawImage(sprites[SPRITE_BURGER + customer[CUSTOMER_ITEM]], customer[CUSTOMER_X] + 8, customer[CUSTOMER_Y] - 23, null);
					}
				}

				// print LEVEL
				g.drawImage(sprites[SPRITE_LETTER_L], 3, 3, null);
				g.drawImage(sprites[SPRITE_LETTER_E], 11, 3, null);
				g.drawImage(sprites[SPRITE_LETTER_V], 19, 3, null);
				g.drawImage(sprites[SPRITE_LETTER_E], 27, 3, null);
				g.drawImage(sprites[SPRITE_LETTER_L], 35, 3, null);

				// draw extra lives
				for (i = 0; i < lives; i++) {
					g.drawImage(sprites[SPRITE_EXTRA_LIFE], 84 + (i << 4), 3, null);
				}

				// draw float points, level number and score
				for (i = points.size() + 1; i >= 0; i--) {
					j = score;
					x = 245;
					y = 3;
					if (i == points.size()) {
						j = level;
						x = j < 10 ? 47 : 55;
					} else if (i < points.size()) {
						point = points.get(i);
						x = point[POINTS_X];
						y = point[POINTS_Y];
						j = point[POINTS_VALUE];
					}
					do {
						g.drawImage(sprites[SPRITE_DIGIT_0 + (j % 10)], x, y, null);
						x -= 8;
						j /= 10;
					} while (j > 0);
				}

				if (gameOver > 0) {
					// draw bouncing GAME OVER
					g.drawImage(sprites[SPRITE_LETTER_G], (int) (128 - 36 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_A], (int) (128 - 28 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_M], (int) (128 - 20 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_E], (int) (128 - 12 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_DIGIT_0], (int) (128 + 4 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_V], (int) (128 + 12 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_E], (int) (128 + 20 * gameOverScale), 146, null);
					g.drawImage(sprites[SPRITE_LETTER_R], (int) (128 + 28 * gameOverScale), 146, null);
				}
			}

			// -- render ends --------------------------------------------------------

			// show the hidden buffer
			if (g2 == null) {
				g2 = (Graphics2D) getGraphics();
				requestFocus();
			} else {
				g2.drawImage(image, 0, 0, 512, 600, null);
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
		final int VK_START = 0x0a;
		final int VK_W = 0x57;
		final int VK_S = 0x53;
		final int VK_A = 0x41;
		final int VK_D = 0x44;
		final int VK_PAUSE = 0x50; // press p for pause

		int k = keyEvent.getKeyCode();
		if (k > 0) {
			k = k == VK_W ? VK_UP : k == VK_D ? VK_RIGHT : k == VK_A ? VK_LEFT : k == VK_S ? VK_DOWN : k;
			a[(k >= VK_LEFT && k <= VK_DOWN) || k == VK_PAUSE ? k : VK_START] = keyEvent.getID() != 402;
		}
	}

	// to run in window, uncomment below
	/*public static void main(String[] args) throws Throwable {
	  javax.swing.JFrame frame = new javax.swing.JFrame("McJob");
	  frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	  a applet = new a();
	  applet.setPreferredSize(new java.awt.Dimension(512, 600));
	  frame.add(applet, java.awt.BorderLayout.CENTER);
	  frame.setResizable(false);
	  frame.pack();
	  frame.setLocationRelativeTo(null);
	  frame.setVisible(true);
	  Thread.sleep(250);
	  applet.start();
	}*/
}
