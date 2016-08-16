package java4k.boxbot4k;

/*
 * BoxBot4k
 *
 * Copyright (c) 2009-2010 Gabor Bata
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public final class B extends JApplet implements KeyListener, MouseListener, Runnable {

	private static final int OUTER_FLOOR = 0;
	private static final int FLOOR = 1;
	private static final int GOAL = 2;
	//private static final int WALL = 3;
	private static final int PLAYER = 4;
	private static final int BOX = 5;
	private static final int BOX_ON_GOAL = 6;
	private static final int PLAYER_SHADOW = 7;
	private static final int SHADOW = 8;
	private static final int SHADOW_SHIFT = 4;

	private static final int TABLE_WIDTH = 14;
	private static final int TABLE_HEIGHT = 12;
	private static final int BLOCK_SIZE = 32;

	private static final String LEVEL_DATA = "                                             ####          #\"!#          #!!#####      #%!"
			+ "!%!!#      #!$!!!\"#      ########                                                         "
			+ "                                  ####          #!!###        #!!%$#        #!&\"!#        "
			+ "##!!!#         #####                                                                ####  "
			+ "        #!!#          #!!###        #!!%!#        ##&#\"#        #!!$!#        #!!!!#      "
			+ "  #!!###        ####                                                                  ####"
			+ "          #!!#          #!!#          #!$###        #!%%!#        #\"!!\"#        ######    "
			+ "                                                                          #####         #!"
			+ "!!#         #\"#%##        #!!$!#        #\"!%!#        ###!!#          ####                "
			+ "                                                                           ########      #"
			+ "!!!!!!#      #!\"&&%$#      #!!!!!!#      #####!!#          ####                           "
			+ "                                                    ####        ###!!#        #\"%%\"#      "
			+ "  #!#!!#        #!$!!#        ###!!#          ####                                        "
			+ "                         ####        ###!!#        #!!!!#        #!!!\"###      ###!#$\"#   "
			+ "     #!%%!#        #!!%!#        #\"!###        ####                                       "
			+ "                          ######       ##!!!!#       #!%##!##      #!!!!!!#      ##!!\"$\"# "
			+ "      #%#!###       #!!!#         #####                                                   "
			+ "              ####         ##!!####      #!!!!!!#      #!\"\"#%!#      ###!!%!#        ###$!"
			+ "#          ####                                                                 #####     "
			+ "    #!!!#     #####!#!##    #!!#!%!!!#    #!!!%!!#!#    #!\"\"!#!!!#    ###$!#####      #!!#"
			+ "          ####                                                                  #####     "
			+ "    #!!!###     ###$%\"!!#     #!!&#!!!#     #!!!%\"!##     #####!!#          ####          "
			+ "                                                                   ####          #!!####  "
			+ "     #!\"!!!#       #!$%%!#       ##!!###        #\"!#          #!!#          ####          "
			+ "                                                        #####        ##!!\"#        #!%#\"# "
			+ "       #!!!!##       ##!#!!#        #%!$!#        #!!!##        #####                     "
			+ "                                              #####         #!!!#      ####!!!#      #\"&!!"
			+ "!##      #!%#!!#       #$!#!!#       #######                                              "
			+ "                                ####          #\"$#          #!!###        #!%!!#        #!"
			+ "%\"!#        #!!###        ####                                                            "
			+ "                     #####       ###!!!#       #!%!#!##      #!#!!\"!#      #!\"!!#!#      #"
			+ "#%#\"%!#       #$!!###       #####                                                         "
			+ "        #######       #!!!!\"#       #!###\"#      ##!#!!!#      #!!#!%!#      #!!$!%##     "
			+ " #!!#!!#       #######                                                                ####"
			+ "        ###!!####     #!!!!#$\"#     #!!!!%%\"#     #!!##!%\"#     #####!!!#         #####   "
			+ "                                                                          #####         #!"
			+ "$!###       #!#!!!#      ##%\"&#!#      #!%\"&!!#      #!#!!###      #!!!!#        ######   "
			+ "                                               ####          #!!#          #%$#          #"
			+ "\"%###       ##\"%!!##      #!\"#!!!##     #!!!!!!!#     #####!!!#         #####             "
			+ "                                   ####          #!!###        #!$!!#        #\"!#%##      "
			+ " ##\"%!!#       #!%\"#!#       #!#!!!#       #!!!!##       ###!!#          ####             "
			+ "                                     #######      ##!!#!!###    #!%!#!%!!#    #!!\"$\"!!!#  "
			+ "  ##%!#!%###     #!\"!\"!#       #######                                                    "
			+ "                                       ####          #!!#####      #!!\"&\"!#      #!%!%!$# "
			+ "     #!!!####      #####                                                                  "
			+ "             #######       #\"!\"!\"#       #!!!#!#       ##!!%!#        #!%%##        #!!$# "
			+ "        #####                                                                             "
			+ "  ####          #!!##        ##!!!##       #!$%%!#       #!#!#!#       #\"\"!!!#       #####"
			+ "##                                                                               ####     "
			+ "     #!\"#        ###!!##       #!%%!!#       #!!!!!#       ##%#!##        #\"$\"#         ##"
			+ "###                                                                  #####         #!!!#  "
			+ "     ###%#!##      #!!%$%!#      #!#!!!!#      #\"\"\"!###      ######                       "
			+ "                                                      ######        #!!!!#        #!!\"%###"
			+ "##    ##&\"%!!!!#     #$\"%#!!!#     ##!!#####      ####                                    "
			+ "                                                          #######      ##!\"!!!#      #!%&%"
			+ "#!#      #!#$!!!#      #!!\"!###      ######                                               "
			+ "                                             #######      ##\"\"\"\"$##     #!!%!#%!#     #!!%"
			+ "!!%!#     #!!##!!!#     #########                                                         "
			+ "                                    ####       ####!$#       #!!%%!#       #!\"!%!#       #"
			+ "#\"\"!##        #####                                                                       "
			+ "       ########      #!\"$\"!!#      #!%!%!!#      ##&#&###      #!!!!!#       #!!!!!#      "
			+ " #!!####       ####                                                                  #####"
			+ "##       #!\"!!!#       #!!%\"\"#       ###%&###      #!%$!!!#      #!!!!!!#      ########   "
			+ "                                                                           ####        ###"
			+ "!!#        #!!!!##       #!#!\"\"#       #!%%%$#       ###!#!#         #!!\"#         #####  "
			+ "                                                                             #####       #"
			+ "##!!!#       #!%%%$#       #\"\"\"!##       #!!!!#        ######                             "
			+ "                                                   #####        ##!!!#        #!$!!#      "
			+ " ##!%!##       #!%&!#        #!#\"!#        #!!\"##        #####                            "
			+ "                                       ####          #!!#        ###%!##       #!!%!!#    "
			+ "   #$#&\"!##      #!!&\"!!#      ###!!!!#        ######                                     "
			+ "              #####       ###$!!#       #\"!%!\"#       #!!%!##      ###!###      ##!%!!#   "
			+ "    #!!!!!#       #\"!!###       #####                                                     "
			+ "              ####          #!!##         #$\"!#         #\"\"%##        #!%%!#        #!!!!#"
			+ "        ######                                                                            "
			+ "   ####          #!!###      ###!\"!!#      #$%%%!!#      #!!!#\"!#      ###\"!!##        ###"
			+ "##                                                                              ####      "
			+ "    #!!#          #!!####       #!\"\"&!#      ##!%%$!#      #!!!####      #!!!#         ###"
			+ "##                                                                #####         #!!!###   "
			+ "    #!!\"%!###     ##!\"#!%!#      #!\"#!%$#      ##\"!!%!#       #!!####       ####          "
			+ "                                                      ####          #!!#####      #!!!!$!#"
			+ "#     #!\"&\"%&!#     ##!!#%!!#      ##!!!###       #####                                   "
			+ "                                              ####      #####!!#      #!!!\"\"%##     #!%$#!"
			+ "%!#     ##%\"\"!!!#      #!!##!!#      ########                                             "
			+ "                                #####         #!!!#         #!#%##       ##\"\"$!#       #!&"
			+ "\"%!#       #!#!%##       #!!!!#        ######                                             "
			+ "                  ###           #\"#####       #\"\"\"!!#       #!#!#!###     #!!!%%!$#     ##"
			+ "#!!%%!#       ###!!!#         #####                                                       "
			+ "       ########      #!!##!\"#     ##!!!%!!#     #!!%#%!\"#     #!!!#$!##     #\"!##!!#      "
			+ "########                                                                              ####"
			+ "#         #!!!#        ##!!!###      #!%%%%!#      #!\"\"\"\"!#      ##!$!###       #####     "
			+ "                                                                        ########      #\"\"!"
			+ "$\"\"#      #!!%%%!#      ####!####     #!!!!!!!#     #!!!!!%!#     #!!######     ####      " + "                              ";

	// 16 x 16, 3-color images
	private static final String IMAGE_DATA = "     !!!!!\"          !\"!\"!\"          !!!!!\"          !\"\"\"!\"          !!!!!\"          \"\"\"\"\""
			+ "\"       !\"!!!!!!!\"!\"    !\"!!!!!!!\"!\"    !\"!\"\"\"\"\"!\"!\"    !\"!\"\"\"\"\"!\"!\"    \"\"!!!!!!!\"\"\"      "
			+ "\"\"\"\"\"\"\"\"         !\"  !\"          !\"  !\"        !!!\"  !!!\"      \"\"\"\"  \"\"\"\"                 "
			+ " \" !!\"\"!!!!!!\"\"!!\" !!  !!!!!!  !!\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"   \"   \"   \"   \" !!\" !!\" !!\" !!\" !!\" !!\""
			+ " !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\" !!\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"  "
			+ "             \" !!\"\"!!!!!!\"\"!!\" !!  !!!!!!  !!\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"        !!!!!!!! \"\"\"\"\"\" !   "
			+ "   ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      !      "
			+ "  !!!!!!!!!!!!!!!!        !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" "
			+ "!      ! \"\"\"\"\"\" !      ! \"\"\"\"\"\" !!!!!!!!         !!!!!!!!!  !!! !!!       \"!!! \"!!       \""
			+ "\"!!  \"!       \"\"\"!   \" \"\"\"\"\"\"\"\"\" !   \" !!!  !!!! !   \"!!! \"!!!  \"!   \"!!  \"!!   \"!   \"!   "
			+ "\"!   \"\"!  \"\"!   \"!  \"\"\"! \"\"\"!   \" \"\"\"\"  \"\"\" !   \" !!!!!!!!! !   \"!!!       \"!  \"\"!!       " + "\"\"! \"\"\"!       \"\"\" \"\"\"  \"\"\"\"\"\"\"\"\" ";

	private static final String STATUS_TEXT = "LEVEL: %s     MOVES: %s     PUSHES: %s";
	private static final String COMPLETED_TEXT = "LEVEL COMPLETED! PRESS ENTER TO CONTINUE.";

	private static final String GAME_TITLE = "BOXBOT4K";
	private static final String GAME_CONTROLS = "GAME CONTROLS:";
	private static final String GAME_CONTROL_ARROWS = "ARROW KEYS - MOVE";
	private static final String GAME_CONTROL_RESTART = "BACKSPACE - RESTART LEVEL";
	private static final String GAME_CONTROL_LEVEL = "PGUP/PGDN - NEXT/PREVIOUS LEVEL";
	private static final String GAME_CONTROL_START = "PRESS ANY KEY TO START.";
	private static final String GAME_COPYRIGHT = "(C) 2010 GABOR BATA";

	private static final int LEVEL_NUM = 50; //LEVEL_DATA.length() / (TABLE_WIDTH * TABLE_HEIGHT);
	private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
	private static final Color FONT_COLOR = new Color(0x60ffffff, true);

	// colors for images
	// 0 bot
	// 1 box
	// 2 floor
	// 3 wall
	private static final int[][] BLOCK_GFX_CONFIG = { { 0xff21285c, 0xff19204c, 0xff303378, 2 }, { 0xff5c3c20, 0xff4c3018, 0xff785830, 2 }, { 0xff74060c, 0xff640305, 0xff8e1618, 2 },
			{ 0xffa29b79, 0xffcec491, 0xff7f7543, 3 }, { 0x00000000, 0xffc0c0c0, 0xff808080, 0 }, { 0xffc4a864, 0xffa88448, 0xff6f4e2a, 1 }, { 0xffc47865, 0xffa85449, 0xff6f2c2B, 1 },
			{ 0x00000000, 0x50000000, 0x50000000, 0 }, { 0x50000000, 0x50000000, 0x50000000, 1 } };

	private static final Image[] BLOCK_GFX = new Image[9];

	static {
		int[] imageData = new int[BLOCK_SIZE / 2 * BLOCK_SIZE / 2];
		for (int i = 0; i < BLOCK_GFX.length; i++) {
			getData(IMAGE_DATA, BLOCK_GFX_CONFIG[i][3], imageData);
			Image image = new BufferedImage(BLOCK_SIZE, BLOCK_SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
			for (int x = 0; x < BLOCK_SIZE / 2; x++) {
				for (int y = 0; y < BLOCK_SIZE / 2; y++) {
					g.setBackground(new Color(BLOCK_GFX_CONFIG[i][imageData[BLOCK_SIZE / 2 * y + x]], true));
					g.clearRect(x * 2, y * 2, 2, 2);
				}
			}
			BLOCK_GFX[i] = image;
		}
	}

	private Image screenBuffer;
	private JComponent canvas;
	private int[] table;
	private int playerX;
	private int playerY;
	private int level;
	private int moves;
	private int pushes;
	private boolean levelSolved;
	private boolean keyPress;
	private boolean gameStarted;

	@Override
	public void init() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@Override
	public void run() {
		screenBuffer = new BufferedImage(TABLE_WIDTH * BLOCK_SIZE, TABLE_HEIGHT * BLOCK_SIZE, BufferedImage.TYPE_INT_RGB);

		table = new int[TABLE_WIDTH * TABLE_HEIGHT];
		level = 0;
		loadLevel();

		canvas = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintTableGraphics(g);
			}
		};

		getContentPane().add(canvas);
		addMouseListener(this);
		addKeyListener(this);
	}

	private void paintTableGraphics(Graphics g) {
		Graphics buffer = screenBuffer.getGraphics();
		for (int i = 0; i < 3; i++) { // 3 step: draw floor, draw shadows, draw other elements
			for (int x = 0; x < TABLE_WIDTH; x++) {
				for (int y = 0; y < TABLE_HEIGHT; y++) {
					if (!gameStarted) {
						buffer.drawImage(BLOCK_GFX[OUTER_FLOOR], x * BLOCK_SIZE, y * BLOCK_SIZE, null);
					} else if (i == 0 && table[TABLE_WIDTH * y + x] <= 2) { // draw floor
						buffer.drawImage(BLOCK_GFX[table[TABLE_WIDTH * y + x]], x * BLOCK_SIZE, y * BLOCK_SIZE, null);
					} else if (i == 1 && table[TABLE_WIDTH * y + x] > 2) { // draw shadow
						buffer.drawImage(BLOCK_GFX[SHADOW], x * BLOCK_SIZE + SHADOW_SHIFT, y * BLOCK_SIZE + SHADOW_SHIFT, null);
					} else if (i == 2 && table[TABLE_WIDTH * y + x] > 2) { // draw other
						buffer.drawImage(BLOCK_GFX[table[TABLE_WIDTH * y + x]], x * BLOCK_SIZE, y * BLOCK_SIZE, null);
					}
				}
			}
			if (i == 1 && gameStarted) { // draw player shadow
				buffer.drawImage(BLOCK_GFX[PLAYER_SHADOW], playerX * BLOCK_SIZE + SHADOW_SHIFT, playerY * BLOCK_SIZE + SHADOW_SHIFT, null);
			} else if (i == 2 && gameStarted) { // draw player
				buffer.drawImage(BLOCK_GFX[PLAYER], playerX * BLOCK_SIZE, playerY * BLOCK_SIZE, null);
			}
		}

		buffer.setFont(FONT);
		buffer.setColor(FONT_COLOR);

		if (!gameStarted) {
			int shift = (TABLE_HEIGHT * BLOCK_SIZE - buffer.getFontMetrics().getHeight() * 9) / 2;
			buffer.drawString(GAME_TITLE, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_TITLE) / 2, buffer.getFontMetrics().getHeight() + shift);
			buffer.drawString(GAME_CONTROLS, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_CONTROLS) / 2, buffer.getFontMetrics().getHeight() * 3 + shift);
			buffer.drawString(GAME_CONTROL_ARROWS, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_CONTROL_ARROWS) / 2, buffer.getFontMetrics().getHeight() * 4 + shift);
			buffer.drawString(GAME_CONTROL_RESTART, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_CONTROL_RESTART) / 2, buffer.getFontMetrics().getHeight() * 5 + shift);
			buffer.drawString(GAME_CONTROL_LEVEL, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_CONTROL_LEVEL) / 2, buffer.getFontMetrics().getHeight() * 6 + shift);
			buffer.drawString(GAME_CONTROL_START, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_CONTROL_START) / 2, buffer.getFontMetrics().getHeight() * 8 + shift);
			buffer.drawString(GAME_COPYRIGHT, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(GAME_COPYRIGHT) / 2, TABLE_HEIGHT * BLOCK_SIZE - BLOCK_SIZE / 4);
		} else {
			if (levelSolved) {
				buffer.drawString(COMPLETED_TEXT, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(COMPLETED_TEXT) / 2, buffer.getFontMetrics().getHeight());
			}
			String text = String.format(STATUS_TEXT, String.valueOf(level + 1), String.valueOf(moves), String.valueOf(pushes));
			buffer.drawString(text, TABLE_WIDTH * BLOCK_SIZE / 2 - buffer.getFontMetrics().stringWidth(text) / 2, TABLE_HEIGHT * BLOCK_SIZE - BLOCK_SIZE / 4);
		}

		g.drawImage(screenBuffer, 0, 0, null);
	}

	private void loadLevel() {
		if (level < 0) {
			level = LEVEL_NUM - 1;
		} else if (level > LEVEL_NUM - 1) {
			level = 0;
		}
		moves = 0;
		pushes = 0;
		levelSolved = false;
		getData(LEVEL_DATA, level, table);
		for (int x = 0; x < TABLE_WIDTH; x++) {
			for (int y = 0; y < TABLE_HEIGHT; y++) {
				if (table[TABLE_WIDTH * y + x] == PLAYER) {
					table[TABLE_WIDTH * y + x] = FLOOR;
					playerX = x;
					playerY = y;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (keyPress) {
			return;
		}
		keyPress = true;

		if (!gameStarted) {
			gameStarted = true;
			canvas.repaint();
			return;
		}

		int dirX = 0;
		int dirY = 0;

		switch (event.getKeyCode()) {
		case KeyEvent.VK_UP:
			dirY = -1;
			break;
		case KeyEvent.VK_DOWN:
			dirY = 1;
			break;
		case KeyEvent.VK_LEFT:
			dirX = -1;
			break;
		case KeyEvent.VK_RIGHT:
			dirX = 1;
			break;
		case KeyEvent.VK_PAGE_UP:
			level++;
			loadLevel();
			break;
		case KeyEvent.VK_PAGE_DOWN:
			level--;
			loadLevel();
			break;
		case KeyEvent.VK_BACK_SPACE:
			loadLevel();
			break;
		case KeyEvent.VK_ENTER:
			if (levelSolved) {
				level++;
				loadLevel();
			}
			break;
		}

		if ((dirX != 0 || dirY != 0) && !levelSolved) {
			int firstStep = TABLE_WIDTH * (playerY + dirY) + playerX + dirX;
			int secondStep = TABLE_WIDTH * (playerY + dirY * 2) + playerX + dirX * 2;
			if (table[firstStep] == FLOOR || table[firstStep] == GOAL) {
				playerX += dirX;
				playerY += dirY;
				moves++;
			} else if ((table[firstStep] == BOX || table[firstStep] == BOX_ON_GOAL) && (table[secondStep] == FLOOR || table[secondStep] == GOAL)) {
				table[firstStep] = table[firstStep] == BOX ? FLOOR : GOAL;
				table[secondStep] = table[secondStep] == FLOOR ? BOX : BOX_ON_GOAL;
				playerX += dirX;
				playerY += dirY;
				moves++;
				pushes++;
			}
		}

		boolean boxesOnGoal = true;
		for (int i = 0; i < table.length; i++) {
			if (table[i] == BOX) {
				boxesOnGoal = false;
			}
		}
		levelSolved = boxesOnGoal;

		canvas.repaint();
	}

	@Override
	public void keyReleased(KeyEvent event) {
		keyPress = false;
	}

	@Override
	public void keyTyped(KeyEvent event) {
		// not used
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// not used
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// only to workaround linux keyboard input focus problem...
		requestFocus();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// not used
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// not used
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// not used
	}

	/**
	 * Get data from ASCII string.
	 *
	 * @param input the input string
	 * @param index the index of data
	 * @param output the array to be filled
	 */
	private static void getData(String input, int index, int[] output) {
		int counter = 0;
		while (counter < output.length) {
			output[counter] = input.charAt(index * output.length + counter) - 0x20;
			counter++;
		}
	}

}
