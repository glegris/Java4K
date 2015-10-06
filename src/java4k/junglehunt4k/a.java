package java4k.junglehunt4k;
/*
 * Jungle Hunt 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Jungle Hunt 4K.
 *
 * Jungle Hunt 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jungle Hunt 4K is distributed in the hope that it will be useful,
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

    final int SPRITE_RUN_1 = 0;
    final int SPRITE_RUN_2 = 1;
    final int SPRITE_RUN_3 = 2;
    final int SPRITE_JUMP_1 = 3;
    final int SPRITE_JUMP_2 = 4;
    final int SPRITE_VINE_1 = 5;
    final int SPRITE_VINE_2 = 6;
    final int SPRITE_LEAVES_1 = 7;
    final int SPRITE_LEAVES_2 = 8;
    final int SPRITE_LEAVES_3 = 9;
    final int SPRITE_LEAVES_4 = 10;
    final int SPRITE_LEAVES_5 = 11;
    final int SPRITE_LEAVES_6 = 12;
    final int SPRITE_LEAVES_7 = 13;
    final int SPRITE_LEAVES_8 = 14;
    final int SPRITE_LEAVES_9 = 15;
    final int SPRITE_LEAVES_10 = 16;
    final int SPRITE_LEAVES_11 = 17;
    final int SPRITE_LEAVES_12 = 18;
    final int SPRITE_LEAVES_13 = 19;
    final int SPRITE_LEAVES_14 = 20;
    final int SPRITE_LEAVES_15 = 21;
    final int SPRITE_DUCK = 22;
    final int SPRITE_ROCK_1 = 23;
    final int SPRITE_ROCK_2 = 24;
    final int SPRITE_CRUSHED = 25;
    final int SPRITE_WOMAN = 26;
    final int SPRITE_CANNIBAL = 27;
    final int SPRITE_CLING_1 = 28;
    final int SPRITE_CLING_2 = 29;
    final int SPRITE_CLING_3 = 30;
    final int SPRITE_CROC_HEAD_1 = 31;
    final int SPRITE_CROC_HEAD_2 = 32;
    final int SPRITE_CROC_HEAD_3 = 33;
    final int SPRITE_CROC_TAIL_1 = 34;
    final int SPRITE_CROC_TAIL_2 = 35;
    final int SPRITE_CROC_TAIL_3 = 36;
    final int SPRITE_WATER_1 = 37;
    final int SPRITE_WATER_2 = 38;
    final int SPRITE_WATER_3 = 39;
    final int SPRITE_CORAL_1 = 40;
    final int SPRITE_CORAL_2 = 41;
    final int SPRITE_SWIM_BODY_1 = 42;
    final int SPRITE_SWIM_BODY_2 = 43;
    final int SPRITE_SWIM_BODY_3 = 44;
    final int SPRITE_SWIM_BODY_4 = 45;
    final int SPRITE_STAB_BODY = 46;
    final int SPRITE_DEAD_BODY_1 = 47;
    final int SPRITE_DEAD_BODY_2 = 48;
    final int SPRITE_SWIM_LEGS_1 = 49;
    final int SPRITE_SWIM_LEGS_2 = 50;
    final int SPRITE_SWIM_LEGS_3 = 51;
    final int SPRITE_SWIM_LEGS_4 = 52;
    final int SPRITE_STAB_LEGS = 53;
    final int SPRITE_DEAD_LEGS = 54;
    final int SPRITE_OXYGEN = 55;
    final int SPRITE_DIGIT_0 = 56;
    final int SPRITE_DIGIT_1 = 57;
    final int SPRITE_DIGIT_2 = 58;
    final int SPRITE_DIGIT_3 = 59;
    final int SPRITE_DIGIT_4 = 60;
    final int SPRITE_DIGIT_5 = 61;
    final int SPRITE_DIGIT_6 = 62;
    final int SPRITE_DIGIT_7 = 63;
    final int SPRITE_DIGIT_8 = 64;
    final int SPRITE_DIGIT_9 = 65;
    final int SPRITE_SMALL_DIGIT_0 = 66;
    final int SPRITE_SMALL_DIGIT_1 = 67;
    final int SPRITE_SMALL_DIGIT_2 = 68;
    final int SPRITE_SMALL_DIGIT_3 = 69;
    final int SPRITE_SMALL_DIGIT_4 = 70;
    final int SPRITE_SMALL_DIGIT_5 = 71;
    final int SPRITE_SMALL_DIGIT_6 = 72;
    final int SPRITE_SMALL_DIGIT_7 = 73;
    final int SPRITE_SMALL_DIGIT_8 = 74;
    final int SPRITE_SMALL_DIGIT_9 = 75;
    final int SPRITE_LIVES = 76;
    final int SPRITE_00 = 77;
    final int SPRITE_10 = 78;
    final int SPRITE_20 = 79;
    final int SPRITE_30 = 80;
    final int SPRITE_DIVING_1 = 81;
    final int SPRITE_DIVING_2 = 82;
    final int SPRITE_DIVING_3 = 83;

    final int SPRITE_COUNT = 84;
    final int SPRITE_DOUBLE_COUNT = SPRITE_COUNT * 2;

    final int VK_LEFT = 0x25;
    final int VK_RIGHT = 0x27;
    final int VK_UP = 0x26;
    final int VK_DOWN = 0x28;
    final int VK_JUMP = 0x42;
    final int VK_W = 0x57;
    final int VK_S = 0x53;
    final int VK_A = 0x41;
    final int VK_D = 0x44;

    final int MODE_VINES = 0;
    final int MODE_SWIMMING = 1;
    final int MODE_ROCKS = 2;
    final int MODE_CANNIBALS = 3;
    final int MODE_WOMAN = 4;
    final int MODE_WON = 5;

    final int SCREEN_WIDTH = 152;
    final int SCREEN_HEIGHT = 191;

    final int ROCK_MAX_Y_SMALL = 127;
    final int ROCK_MAX_Y_LARGE = 123;
    final int ROCK_Y_SMALL = 123;
    final int ROCK_Y_LARGE = 100;

    final int VINE_RADIUS = 0;
    final int VINE_INCREMENTING = 1;
    final int VINE_DIRECTION = 2;
    final int VINE_SPEED = 3;

    final int CROC_X = 0;
    final int CROC_Y = 1;
    final int CROC_CENTER_Y = 2;
    final int CROC_RADIUS = 3;
    final int CROC_ANGLE = 4;
    final int CROC_ANGLE_INC = 5;
    final int CROC_SPRITE_1 = 6;
    final int CROC_SPRITE_2 = 7;
    final int CROC_DELAY = 8;

    final int POINTS_VINE_CATCH = 100;
    final int POINTS_ADVANCED_LEVEL = 500;
    final int POINTS_ROCK_JUMP = 200;
    final int POINTS_ROCK_DUCK = 100;
    final int POINTS_ROCK_WALK = 50;

    final float GRAVITY = 0.04f;
    final float GRAVITY_2 = 0.12f;
    final float GRAVITY_3 = 0.065f;
    final float JUMP_SPEED = -1.1f;
    final float JUMP_SPEED_2 = -3.04f;
    final int CANNIBAL_JUMP_SPEED = -1;
    final int CAMERAX0 = 65457;

    final String S = "\u3800\u307c\u1838\u7230\ubefa\u00b4\u3830\u243c\u6424\u80c6\u3800\u307c\u1838\u7030\u7e72\u0078\u3830\u141c\u301c\u1850\u7c38\u3a30\uf21a\ubefe\ub0b0\u3e00\u223e\u2222\uc063\uc080\u0000\u7c38\u3830\u3218\ufc76\ub0b8\u3800\u263c\ue2e2\u0382\u3800\u307c\u1a3a\u7e72\u707c\u0070\u3e3c\u6222\u83c2\u0080\u2020\u7020\u5070\ud050\u8c98\u169c\u3415\u2326\u2021\u2020\u181c\u5010\u3c30\u4e6c\u89cb\u2889\u3c18\uc764\u098d\u1018\uf0bc\u00a0\udb7e\u058f\u6de7\u1038\ub019\u40e0\u77de\"\u5708\ufffd\ub3c1\uff1a\u774c\uffad\u9bc9\uff55\u459c\uff32\u6b42\uffee\ub513\uffff\u7450\uffdd\u8502\uffaf\u3903\ufffe\u1f0e\u1e0c\u787e\uf00e\u2c78\uc266\uc382\u1800\u3c3c\u7e7e\u7e7e\u3c7e\u183c\u1800\u3c3c\u3c3c\u183c\u0800\u4002\u3c18\u1818\u81ff\u663c\u1c00\u1d3e\u1819\u5abc\u1a1a\u3c18\u7e7e\u2424\u0664\u0702\uaa72\ufaaa\u7e52\u5672\u726a\u7072\ufc78\u4146\u3467\u3014\u7c38\u3a32\u121a\u3c3e\u3838\u3800\u1838\u9818\uf8d8\u00b0\ufa70\u7262\u6632\u787c\u7070\u7000\u3e78\u020e\u0202\u0302\uf870\u7160\u6633\u787c\u7070\u7000\u7170\u073f\u0800\uff17\u02ff\u0000\uc800\u1f37\ue21f\u0000\u4880\u1f37\u120f\u4020\u0000\uf8e0\u16fc\u0001\u0000\ufef7\u10f8\u0000\u0201\ufcfe\u10f0\u0000\u7830\ufffc\u0000\uc700\u0000\u3f07\u1410\ubf3e\ufbf1\ueeff\u0000\u6c40\ubdae\uffbe\u101c\ufe10\uf0fe\u0810\u0006\u0700\ufe08\uf6fe\u4020\u2040\u0200\ufe05\uf6fe\uc020\u0000\u0000\u4630\udebe\u00f0\u0000\u1e00\ufce0\uecfc\u4020\u0060\u0000\u0e00\uf610\ufefe\u0708\u0800\u2010\uec20\ufcfc\u3c20\u0000\u0100\u4f97\u8000\u0000\u2060\u1930\u0f03\u301c\u4060\u0000\u0000\u8f01\u002f\u0000\u2060\u1d30\u030f\u301c\u4060\u4000\u3fe0\u031f\u7623\u081c\u0800\u361c\u0f03\u711f\u0020\u0f06\u060f\u1c00\u6336\u6363\u1c36\u1800\u181c\u1818\u7e18\u3e00\u6063\u073c\u7f03\u3f00\u6060\u603e\u3f60\u3300\u3333\u307f\u3030\u3f00\u0303\u603f\u3f60\u3e00\u0303\u633f\u3e63\u7f00\u3060\u1818\u0c0c\u3e00\u6363\u633e\u3e63\u3e00\u6363\u607e\u3e60\u0700\u0505\u0705\u0200\u0203\u0702\u0700\u0604\u0701\u0700\u0604\u0704\u0500\u0705\u0404\u0700\u0301\u0704\u0700\u0701\u0705\u0700\u0604\u0103\u0700\u0705\u0705\u0700\u0705\u0704\u1504\u040e\u0a04\u8a71\u8a8a\u8a8a\u0071\u26c4\u2424\u2424\u00ce\u29c6\u2c28\u2122\u00cf\u29c6\u2428\u2928\u00c6\u5553\u9355\ub595\u94d5\u020e\u0e0a";

    final Color COLOR_SKY = new Color(0x185080);
    final int HEX_VINE = 0xC3903D;
    final Color COLOR_VINE = new Color(HEX_VINE);
    final int HEX_LEAVES_3 = 0x6D762B;
    final Color COLOR_DIRT = new Color(HEX_LEAVES_3);
    final int HEX_LEAVES_1 = 0x328432;
    final Color COLOR_LEAVES = new Color(HEX_LEAVES_1);
    final int HEX_WATER = 0x003064;
    final Color COLOR_WATER = new Color(HEX_WATER);
    final Color COLOR_BLACK = new Color(0);

    final int HEX_WHITE = 0xECECEC;
    final int HEX_GRAY = 0xC0C0C0;
    final int HEX_FLESH = 0xD65C5C;    
    final int HEX_ROCK = 0xBB9F47;
    final int HEX_PANTS = 0xC84848;
    final int HEX_HAIR = 0xD2D240;
    final int HEX_SHIRT = 0x87B754;
    final int HEX_DRESS = 0x545CD6;
    final int HEX_WOMAN_FLESH = 0xE46F6F;
    final int HEX_LEAVES_2 = 0x694D14;
    final int HEX_CORAL = 0xB83232;
    final int HEX_TIME = 0xD2B656;

    int i;
    int j;
    int k;
    int x;
    int y;
    int z;
    int u;
    int v;
    int w;
    int t;

    int counter = 0;
    int score = 0;
    int time = 5000;
    int lives = 4;
    int mode = MODE_VINES;
    int cameraX = CAMERAX0;
    int targetCameraX = cameraX;

    int playerX = cameraX + 144;
    int playerSprite = 0;
    int playerSprite2 = 0;
    int playerDead = 0;
    int playerDirection = -1;
    int playerVine = 819;
    int playerVineOffset = 64;
    int playerStabbing = 0;
    int playerOxygen = 0;

    int rockX = 0;
    int rockSprite = 0;
    int rockMaxY = 0;
    int cannibalDirection = 0;
    int remaining = 10;
    int crocSpawnX = 0;
    int level = 0;

    int[] vinePoints = new int[384];
    int[][] vines = new int[1024][4];
    int[][] crocs = null;

    boolean playerDucking = false;
    boolean jumpReleased = true;
    boolean panning = false;
    boolean firstMove = true;
    boolean bonusLifeAwarded = false;
    boolean rockBonusAwarded = false;
    boolean levelAdvanceRequest = false;

    float playerY = 0;
    float playerVy = 0;

    float rockY = 0;
    float rockVy = 0;

    Random random = new Random();

    BufferedImage[] sprites = new BufferedImage[SPRITE_DOUBLE_COUNT];
    BufferedImage image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, 1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    Graphics2D g2 = null;

    // initialize vines
    for(i = 0; i < 1024; i++) {
      vines[i][VINE_RADIUS] = 100 + random.nextInt(12700);
      vines[i][VINE_INCREMENTING] = random.nextInt(2);
      vines[i][VINE_DIRECTION] = random.nextInt(2);
      vines[i][VINE_SPEED] = random.nextInt(3);
    }

    // decompress sprites
    for(k = i = 0; i < SPRITE_COUNT; i++, k += z >> 1) {

      // height
      z = (i <= SPRITE_VINE_2 || i == SPRITE_CLING_1
              || i == SPRITE_CLING_2) ? 20
          : (i <= SPRITE_LEAVES_15 || i == SPRITE_OXYGEN
              || (i >= SPRITE_WATER_1 && i <= SPRITE_WATER_3)
              || (i >= SPRITE_DIVING_1 && i <= SPRITE_DIVING_3)) ? 4
          : (i == SPRITE_DUCK) ? 14
          : (i == SPRITE_ROCK_1 || i == SPRITE_CRUSHED) ? 12
          : (i == SPRITE_WOMAN) ? 18
          : (i == SPRITE_CANNIBAL) ? 22
          : (i == SPRITE_CLING_3) ? 16
          : (i >= SPRITE_SWIM_BODY_1 && i <= SPRITE_DEAD_LEGS) ? 10
          : (i >= SPRITE_SMALL_DIGIT_0 && i <= SPRITE_LIVES) ? 6
          : 8;

      sprites[i] = new BufferedImage(8, z, 2);
      sprites[i + SPRITE_COUNT] = new BufferedImage(8, z, 2); // reversed

      for(y = 0; y < z; y++) {
        j = y + (i == SPRITE_RUN_3 ? 1 : i == SPRITE_JUMP_1 ? -1 : 0);
        for(x = 0; x < 8; x++) {
          u = (((S.charAt(k + (y >> 1))
              >> (x + (((y & 1) == 0) ? 0 : 8))) & 1) == 1)
              ? 0xFF000000
              // colors
                  | (i <= SPRITE_JUMP_2 ? ((j < 3 || (j > 5 && j < 14))
                          ? HEX_WHITE : HEX_FLESH)
                      : i <= SPRITE_VINE_2 ? HEX_VINE
                      : i <= SPRITE_LEAVES_5 ? HEX_LEAVES_1
                      : i <= SPRITE_LEAVES_10 ? HEX_LEAVES_2
                      : i <= SPRITE_LEAVES_15 ? HEX_LEAVES_3
                      : i == SPRITE_DUCK ? ((y < 2 || (y > 3 && y < 8))
                          ? HEX_WHITE : HEX_FLESH)
                      : i <= SPRITE_ROCK_2 ? HEX_ROCK
                      : i == SPRITE_CRUSHED ? ((y < 4 || (y > 5 && y < 8))
                          ? HEX_FLESH : HEX_WHITE)
                      : i == SPRITE_WOMAN ? (y < 4 ? HEX_HAIR
                          : (y < 6 || y > 13) ? HEX_WOMAN_FLESH
                              : y < 10 ? HEX_SHIRT : HEX_DRESS)
                      : i == SPRITE_CANNIBAL ? (y < 6 ? HEX_GRAY 
                          : (y > 13 && y < 16) ? HEX_PANTS : HEX_ROCK)
                      : i <= SPRITE_CLING_3 ? ((y < 2 || (y > 5 && y < 14))
                          ? HEX_WHITE : HEX_FLESH)
                      : i <= SPRITE_CROC_TAIL_3 ? HEX_ROCK
                      : i <= SPRITE_WATER_3 ? HEX_WATER
                      : (i <= SPRITE_CORAL_2 || i == SPRITE_OXYGEN) ? HEX_CORAL
                      : i <= SPRITE_DEAD_BODY_2 ? HEX_WOMAN_FLESH
                      : i <= SPRITE_DEAD_LEGS ? HEX_WHITE
                      : i <= SPRITE_DIGIT_9 ? HEX_WOMAN_FLESH
                      : i <= SPRITE_LIVES ? HEX_TIME
                      : i <= SPRITE_30 ? HEX_ROCK
                      : HEX_GRAY) : 0;
          sprites[i].setRGB(x, y, u);
          sprites[i + SPRITE_COUNT].setRGB(7 - x, y, u);
        }        
      }
    }

    long nextFrameStartTime = System.nanoTime();
    while(true) {

      do {
        nextFrameStartTime += 16666667;

        // -- update starts ----------------------------------------------------

        counter++;
        if (playerDead == 0 && mode != MODE_WON && (counter & 63) == 0
            && !firstMove) {
          time -= 10;
        }

        if (!a[VK_JUMP]) {
          jumpReleased = true;
        }

        if (mode == MODE_VINES) {

          if (playerY > 256) {
            // lose a life
            lives--;
            playerVine++;
            playerVineOffset = 64;
            playerVy = 0;
          }

          int vineIndex = -1;
          j = cameraX / 80;
          k = cameraX % 80;
          w = 0;
          for(i = 0; i < 3; i++) {
            int[] vine = vines[i + j];
            if (i + j == playerVine) {
              vineIndex = i << 7;
              playerSprite = vine[VINE_RADIUS] > 1200 ? SPRITE_CLING_2
                  : vine[VINE_DIRECTION] == 1 ? SPRITE_CLING_1 : SPRITE_CLING_3;
            }

            // move the vine
            if ((counter & 3) == 3) {
              if (vine[VINE_INCREMENTING] == 1) {
                vine[VINE_RADIUS] *= 20 + vine[VINE_SPEED];
                vine[VINE_RADIUS] >>= 4;
                if (vine[VINE_RADIUS] > 1600) {
                  vine[VINE_RADIUS] = 1600;
                  vine[VINE_INCREMENTING] = 0;
                  vine[VINE_DIRECTION] ^= 1;
                }
              } else {
                vine[VINE_RADIUS] <<= 4;
                vine[VINE_RADIUS] /= 20 + vine[VINE_SPEED];
                if (vine[VINE_RADIUS] < 100) {
                  vine[VINE_RADIUS] = 100;
                  vine[VINE_INCREMENTING] = 1;
                }
              }
            }

            // apply midpoint circle algorithm to generate vine points
            int radius = vine[VINE_RADIUS];
            t = 1 - radius;
            u = 1;
            v = -2 * radius;
            z = x = 0;
            y = radius;

            while(true) {

              if (t >= 0) {
                if (++z == 64) {
                  break;
                }
                if (vine[VINE_DIRECTION] == 0) {
                  y--;
                } else {
                  y++;
                }
                v += 2;
                t += v;

                vinePoints[w++] = cameraX + i * 80 - k + y - radius + 53;
                vinePoints[w++] = 61 + x;
              }
              if (++z == 64) {
                break;
              }
              x++;
              u += 2;
              t += u;

              vinePoints[w++] = cameraX + i * 80 - k + y - radius + 53;
              vinePoints[w++] = 61 + x;
            }
          }

          if (playerVy == 0) {
            // player clings onto vine
            playerX = vinePoints[vineIndex + playerVineOffset] - 1;
            playerY = vinePoints[vineIndex + playerVineOffset + 1];

            if (jumpReleased && a[VK_JUMP]) {              
              jumpReleased = false;
              playerVy = JUMP_SPEED;
              playerVine--;
            } else if (remaining <= 0 && playerDead == 0) {

              // player advances to swimming stage
              remaining = 15 + (level << 1);
              mode = MODE_SWIMMING;
              cameraX = CAMERAX0;
              crocSpawnX = cameraX;
              playerX = cameraX + 133;
              playerY = 85;
              playerStabbing = 0;
              playerOxygen = 16;
              score += POINTS_ADVANCED_LEVEL;

              // initialize crocs
              crocs = new int[3][16];
              for(i = 0; i < 3; i++) {
                crocs[i][CROC_X] = cameraX << 1;
              }
            }

          } else {
            playerSprite = SPRITE_CLING_1;
            playerX -= 2;
            playerVy += GRAVITY_3 
                + (level == 1 ? 0.0025f : level > 1 ? 0.005f : 0);
            playerY += playerVy;

            if (vineIndex >= 0) {
              for(i = 0; i < 64; i++) {
                x = vinePoints[vineIndex + (i << 1)] + 1;
                y = vinePoints[vineIndex + (i << 1) + 1];
                if (playerX >= x && playerX <= x + 2
                    && (int)playerY == y) {
                  // player grabbed vine
                  playerVineOffset = i << 1;
                  playerVy = 0;
                  targetCameraX -= 80;
                  if (firstMove) {
                    // maintain the score until the first move of the next game
                    firstMove = false;
                    score = POINTS_VINE_CATCH;
                  } else {
                    score += POINTS_VINE_CATCH;
                  }
                  remaining--;
                  break;
                }
              }
            } 
          }

          if (cameraX > targetCameraX) {
            cameraX--;
          }

        } else if (mode == MODE_SWIMMING) {

          if ((counter & 1) == 1) {
            cameraX--;
          }

          if (playerDead > 0) {
            if (++playerDead > 128) {
              // lose a life
              // reset swimming stage
              playerDead = 0;
              remaining += 4;
              lives--;
              cameraX = CAMERAX0;
              crocSpawnX = cameraX;
              playerX = cameraX + 133;
              playerY = 85;
              playerStabbing = 0;
              playerOxygen = 16;

              // initialize crocs
              crocs = new int[3][16];
              for(i = 0; i < 3; i++) {
                crocs[i][CROC_X] = cameraX << 1;
              }
            } else {
              playerSprite = SPRITE_DEAD_BODY_1 + ((counter >> 4) & 1);
              playerSprite2 = SPRITE_DEAD_LEGS;
              if ((counter & 1) == 1) {
                playerX--;
              }
              if (playerY > 85) {
                playerY--;
              }
            }
          } else {

            if (playerY < 86) {
              playerOxygen = 16;
            } else if ((counter & 63) == 63 && --playerOxygen == 0) {
              playerDead = 1;
            }

            if (a[VK_UP]) {
              if (playerY > 85) {
                playerY--;
              }
            } else if (a[VK_DOWN]) {
              if (playerY < 155) {
                playerY++;
              }
            }

            if (a[VK_LEFT]) {
              playerX--;
            } else if (a[VK_RIGHT]) {
              playerX++;
            }

            if (playerStabbing > 0) {
              playerStabbing--;
              playerSprite = SPRITE_STAB_BODY;
              playerSprite2 = SPRITE_STAB_LEGS;
            } else {
              i = (counter >> 3) & 3;
              playerSprite = i + SPRITE_SWIM_BODY_1;
              playerSprite2 = i + SPRITE_SWIM_LEGS_1;

              if (jumpReleased && a[VK_JUMP]) {
                jumpReleased = false;
                playerStabbing = 64;
              }
            }
          }
          
          if (playerX > cameraX + 133) {
            playerX = cameraX + 133;
          } else if (playerX < cameraX + 81) {
            playerX = cameraX + 81;
          }

          // update crocs
          for(i = 0; i < 3; i++) {
            int[] croc = crocs[i];
            if (croc[CROC_DELAY] > 0) {
              if (--croc[CROC_DELAY] == 0) {
                croc[CROC_X] = cameraX + SCREEN_WIDTH;
              }
            } else {
              if (croc[CROC_X] >= cameraX + SCREEN_WIDTH) {
                if (crocSpawnX > cameraX) {
                  crocSpawnX = cameraX;
                }
                crocSpawnX -= 43 + random.nextInt(31);
                croc[CROC_X] = crocSpawnX;
                croc[CROC_CENTER_Y] = 85 + random.nextInt(70);
                croc[CROC_RADIUS] = 8 + random.nextInt(24);
                croc[CROC_ANGLE] = random.nextInt(1608);
                croc[CROC_ANGLE_INC] = 4 + random.nextInt(5 + level);

                if (playerDead == 0 && --remaining <= 0 && !a[VK_DOWN]) {

                  // player advances to rock stage
                  remaining = 15 + (level << 1);
                  mode = MODE_ROCKS;
                  cameraX = CAMERAX0;
                  playerX = cameraX + 133;
                  playerY = 115;
                  playerVy = 0;
                  rockX = cameraX + SCREEN_WIDTH;
                  playerDucking = false;
                  panning = false;
                  score += POINTS_ADVANCED_LEVEL;
                }
              }
              croc[CROC_ANGLE] += croc[CROC_ANGLE_INC];
              croc[CROC_Y] = (int)(croc[CROC_CENTER_Y]
                  + croc[CROC_RADIUS] * Math.sin(croc[CROC_ANGLE] / 256f));
              if (croc[CROC_Y] < 85) {
                croc[CROC_Y] = 85;
              } else if (croc[CROC_Y] > 155) {
                croc[CROC_Y] = 155;
              }

              croc[CROC_SPRITE_1] = SPRITE_CROC_TAIL_1
                      + ((croc[CROC_ANGLE] >> 6) % 3);
              croc[CROC_SPRITE_2] = SPRITE_CROC_HEAD_1
                      + ((croc[CROC_ANGLE] >> 8) % 3);

              if (playerDead == 0) {
                if (playerStabbing > 0
                    && playerX >= croc[CROC_X] + 2
                    && playerX <= croc[CROC_X] + 14
                    && playerY >= croc[CROC_Y] + 1
                    && playerY <= croc[CROC_Y] + 3) {
                  // player stabbed croc
                  j = playerX - croc[CROC_X];
                  if (j < 5) {
                    score += 100;
                    croc[CROC_SPRITE_1] = SPRITE_10;
                  } else if (j < 10) {
                    score += 200;
                    croc[CROC_SPRITE_1] = SPRITE_20;
                  } else {
                    score += 300;
                    croc[CROC_SPRITE_1] = SPRITE_30;
                  }
                  croc[CROC_SPRITE_2] = SPRITE_00;
                  croc[CROC_DELAY] = 32;
                } else if (playerX - 15 <= croc[CROC_X]
                    && playerX + 15 >= croc[CROC_X]
                    && playerY - 2 <= croc[CROC_Y]
                    && playerY + 7 >= croc[CROC_Y]) {
                  // croc killed player
                  playerDead = 1;
                }
              }
            }
          }

        } else if (mode == MODE_ROCKS) {

          if ((counter & 1) == 1) {
            rockX += playerDucking ? 2 : 1;
          }
          if (playerDead == 0 && rockX > cameraX + SCREEN_WIDTH) {
            if (--remaining == 0) {

              // player advances to cannibal stage

              remaining = 2 + level;
              mode = MODE_CANNIBALS;
              panning = true;
              rockY = 112;
              rockVy = 0;
              cannibalDirection = -1;
              rockX = cameraX - 72;              
              score += POINTS_ADVANCED_LEVEL;
            }
            rockX = cameraX - 8 - random.nextInt(64);
            rockBonusAwarded = false;
            if ((rockSprite = SPRITE_ROCK_1 + random.nextInt(2))
                == SPRITE_ROCK_1) {
              rockY = ROCK_Y_LARGE;
              rockMaxY = ROCK_MAX_Y_LARGE;
            } else {
              rockY = ROCK_Y_SMALL;
              rockMaxY = ROCK_MAX_Y_SMALL;
            }
            rockVy = 0;
          }

          rockVy += GRAVITY;
          rockY += rockVy;
          if (rockY >= rockMaxY) {
            rockY = (rockMaxY << 1) - rockY;
            rockVy = -rockVy;
          }

          if (playerDead > 0) {
            if (++playerDead > 128) {
              // lose a life
              // reset stage
              playerDead = 0;
              lives--;
              remaining += 2;
              cameraX = CAMERAX0;
              playerX = cameraX + 133;
              playerY = 115;
              playerVy = 0;
              rockX = cameraX + SCREEN_WIDTH;
              playerDucking = false;
              panning = false;
            } else {
              playerY = (int)playerY;
              if (playerY < 123) {
                playerY++;
              }
            }
          } else {

            if (playerVy == 0 && a[VK_DOWN]) {
              playerY = 121;
              playerSprite = SPRITE_DUCK;
              playerDucking = true;
            } else {
              if (playerDucking) {
                playerDucking = false;
                playerY = 115;
              }

              if ((counter & 1) == 1) {
                cameraX--;
              }

              if (a[VK_LEFT]) {
                playerX--;
              }

              if (playerX > cameraX + 143) {
                playerX = cameraX + 143;
              } else if (playerX < cameraX + 71) {
                playerX = cameraX + 71;
              }

              if (playerVy == 0) {
                if (jumpReleased && a[VK_JUMP]) {
                  jumpReleased = false;
                  playerVy = JUMP_SPEED;
                }
                if ((counter & 3) == 3 && playerSprite++ > SPRITE_RUN_3) {
                  playerSprite = SPRITE_RUN_1;
                }
              } else {
                playerVy += GRAVITY;
                playerY += playerVy;
                if (playerY >= 115) {
                  playerY = 115;
                  playerVy = 0;
                }
                playerSprite = (playerVy < 0) ? SPRITE_JUMP_2 : SPRITE_JUMP_1;
              }
            }

            i = rockSprite == SPRITE_ROCK_1 ? 11 : 7; // rock height
            j = 1 + (int)rockY; // rock y
            k = playerDucking ? 18 : 13; // player height
            x = 2 + (int)playerY; // player y

            // collision test between rock and player
            if (rockX + 8 > playerX && playerX + 8 > rockX
                && j + i > x && x + k > j) {
              playerDead = 1;
              playerY += playerDucking ? 2 : 8;
              playerSprite = SPRITE_CRUSHED;
            } else if (!rockBonusAwarded && rockX > playerX) {
              rockBonusAwarded = true;
              if (playerVy != 0) {
                score += POINTS_ROCK_JUMP;
              } else if (playerDucking) {
                score += POINTS_ROCK_DUCK;
              } else {
                score += POINTS_ROCK_WALK;
              }
            }
          }
        } else if (mode == MODE_WON) {

          playerSprite = (counter & 32) == 0 ? 0 : SPRITE_COUNT;
          rockSprite = SPRITE_WOMAN + (SPRITE_COUNT - playerSprite);

          if (time > 0) {
            time -= 10;
            score += 30;
            if (time <= 0) {
              // advance level
              levelAdvanceRequest = true;
            }
          }

        } else {

          // mode >= MODE_CANNIBAL

          rockSprite = (mode == MODE_WOMAN ? SPRITE_WOMAN : SPRITE_CANNIBAL)
              + ((counter & 32) == 0 ? 0 : SPRITE_COUNT);
          if (mode == MODE_CANNIBALS) {
            if ((counter & 1) == 1) {
              rockX += cannibalDirection;
            }
            rockVy += GRAVITY;
            rockY += rockVy;
            if (rockY >= 112) {
              rockY = 112;
              rockVy = CANNIBAL_JUMP_SPEED;
              cannibalDirection = -cannibalDirection;
            }
          } else {
            rockY = 117;
          }

          if (playerDead > 0) {
            if (++playerDead > 128) {
              // lose a life
              // reset stage
              lives--;
              playerDead = 0;
              playerX = cameraX + 143;
              playerY = 115;
              playerVy = 0;
              playerSprite = SPRITE_RUN_1;
            } else {
              playerY = (int)playerY;
              if (playerY < 123) {
                playerY++;
              }
            }
          } else if (panning) {
            playerDucking = false;
            if (playerY > 115) {
              playerY = 115;
            }
            if ((counter & 3) == 3 && playerSprite++ > SPRITE_RUN_3) {
              playerSprite = SPRITE_RUN_1;
            }
            if ((counter & 1) == 1) {
              cameraX--;
            }
            if (playerX > cameraX + 143) {
              playerX = cameraX + 143;
            }
            if (rockX >= cameraX + 72) {
              panning = false;
            }
          } else {
            i = 0;
            if (a[VK_LEFT]) {
              playerDirection = i = -1;
            } else if (a[VK_RIGHT]) {
              playerDirection = i = 1;
            }
            playerX += i;

            if (playerX > cameraX + 143) {
              playerX = cameraX + 143;
            } else {
              while (playerX < cameraX + 25) {
                if ((counter & 1) == 1) {
                  cameraX--;
                } else {
                  playerX++;
                }
              }
            }

            if (jumpReleased && a[VK_JUMP] && mode < MODE_WOMAN) {
              jumpReleased = false;
              playerVy = JUMP_SPEED_2;
            }

            if (playerVy == 0) {

              if (rockX > cameraX + SCREEN_WIDTH) {
                rockBonusAwarded = false;
                panning = true;
                rockY = 112;
                rockVy = 0;
                cannibalDirection = -1;
                rockX = cameraX - 72;
                if (--remaining == 0) {

                  // advance to ending stage

                  rockSprite = SPRITE_WOMAN;
                  mode = MODE_WOMAN;
                  score += POINTS_ADVANCED_LEVEL;
                }
              }

              if (i != 0) {
                if (playerSprite >= SPRITE_COUNT) {
                  playerSprite -= SPRITE_COUNT;
                }
                if ((counter & 3) == 3 && playerSprite++ > SPRITE_RUN_3) {
                  playerSprite = SPRITE_RUN_1;
                }
                if (i > 0) {
                  playerSprite += SPRITE_COUNT;
                }
              }
            } else {
              playerVy += GRAVITY_2;
              playerY += playerVy;
              if (playerY >= 115) {
                playerY = 115;
                playerVy = 0;
              }
              playerSprite = ((playerVy < 0) ? SPRITE_JUMP_2 : SPRITE_JUMP_1)
                  + (playerDirection == 1 ? SPRITE_COUNT : 0);
            }

            // collision test between cannibal and player
            if (rockX + 8 > playerX && playerX + 8 > rockX
                && rockY + 20 > playerY && playerY + 20 > rockY) {
              if (mode == MODE_WOMAN) {
                mode = MODE_WON;
              } else {
                playerDead = 1;
                playerY += playerDucking ? 2 : 8;
                playerSprite = SPRITE_CRUSHED;
              }
            } else if (!rockBonusAwarded && rockX > playerX) {
              rockBonusAwarded = true;
              score += POINTS_ROCK_JUMP;
            }
          }
        }

        if (lives < 0 || time <= 0) {
          // reset the entire game
          time = 5000;          
          mode = MODE_VINES;
          cameraX = CAMERAX0;
          targetCameraX = cameraX;
          playerX = cameraX + 144;
          playerVine = 819;
          playerVineOffset = 64;          
          playerVy = 0;
          if (levelAdvanceRequest) {
            levelAdvanceRequest = false;
            level++;
          } else {
            lives = 4;
            level = 0;
            firstMove = true;
            bonusLifeAwarded = false;
          }
          remaining = 10 + (level << 1);
        } else if (!firstMove && !bonusLifeAwarded && score >= 10000) {
          // gain bonus life after exceeding 10,000 points
          bonusLifeAwarded = true;
          lives++;
        }

        // -- update ends ------------------------------------------------------

      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------

      // draw sky
      g.setColor(COLOR_SKY);
      g.fillRect(0, 0, 152, 191);

      if (mode != MODE_SWIMMING) {

        // draw vine sprites
        j = (cameraX >> 4) % 15;
        for(i = 0; i < 11; i++, j++) {
          if (j > 14) {
            j = 0;
          }
          if (j < 4 || j == 5 || j == 8 || j == 10 || j == 13) {
            g.drawImage(sprites[SPRITE_VINE_1 + (j & 1)],
                (i << 4) - (cameraX & 15), 43, null);
          }
        }

        // draw leaves
        g.setColor(COLOR_LEAVES);
        g.fillRect(0, 0, 152, 43);
        j = cameraX << 1;
        for(i = 0; i < 6; i++) {
          g.drawImage(sprites[SPRITE_LEAVES_1 + ((i + (j >> 5)) % 5)],
              (i << 5) - (j & 31), 43, 32, 16, null);
        }
      }

      if (mode == MODE_VINES) {

        // draw leaves on ground (background layer)
        g.setColor(COLOR_DIRT);
        g.fillRect(0, 180, 152, 11);      
        j = cameraX;
        for(i = 0; i < 6; i++) {
          g.drawImage(sprites[SPRITE_LEAVES_11 + ((i + (j >> 5)) % 5)],
              (i << 5) - (j & 31), 164, 32, 16, null);
        }
        
        // draw moving vine
        g.setColor(COLOR_VINE);
        for(i = 0; i < 384; i += 2) {
          g.fillRect(vinePoints[i] - cameraX, vinePoints[i + 1], 1, 1);
        }

        // draw player
        g.drawImage(sprites[playerSprite],
            playerX - cameraX, (int)playerY, null);

        // draw leaves on ground (foreground layer)
        j = cameraX << 1;
        for(i = 0; i < 6; i++) {
          g.drawImage(sprites[SPRITE_LEAVES_6 + ((i + (j >> 5)) % 5)],
              (i << 5) - (j & 31), 177, 32, 16, null);
        }

      } else if (mode == MODE_SWIMMING) {

        // draw water
        g.setColor(COLOR_WATER);
        g.fillRect(0, 88, 152, 103);
        j = cameraX << 1;
        for(i = 0; i < 6; i++) {
          g.drawImage(sprites[SPRITE_WATER_1 + (((i + (j >> 5)) % 3))],
              (i << 5) - (j & 31), 80, 32, 8, null);
        }

        // draw coral
        j = cameraX;
        for(i = 0; i < 6; i++) {
          g.drawImage(sprites[SPRITE_CORAL_1 + (((i + (j >> 5)) & 1))],
              (i << 5) - (j & 31), 167, 32, 24, null);
        }

        // draw crocs
        for(i = 0; i < 3; i++) {
          int[] croc = crocs[i];
          g.drawImage(sprites[croc[CROC_SPRITE_1]],
              croc[CROC_X] - cameraX, croc[CROC_Y], null);
          g.drawImage(sprites[croc[CROC_SPRITE_2]],
              croc[CROC_X] - cameraX + 8, croc[CROC_Y], null);
        }

        // draw player
        g.drawImage(sprites[playerSprite], playerX - cameraX,
            (int)playerY, null);
        g.drawImage(sprites[playerSprite2], playerX - cameraX + 8,
            (int)playerY, null);

        // draw oxygen bar (HUD)
        g.setColor(COLOR_BLACK);
        g.fillRect(128, 68, 20, 4);
        for(i = 0; i < 5; i++) {
          g.drawImage(sprites[SPRITE_OXYGEN], 130 + i * 3, 68, null);
        }
        g.fillRect(130 + playerOxygen, 68, 16 - playerOxygen, 4);
        g.drawImage(sprites[SPRITE_DIVING_1], 128, 61, null);
        g.drawImage(sprites[SPRITE_DIVING_2], 136, 61, null);
        g.drawImage(sprites[SPRITE_DIVING_3], 144, 61, null);

      } else {

        // draw flat ground
        g.setColor(COLOR_DIRT);
        g.fillRect(0, 135, 152, 56);

        // draw rock
        g.drawImage(sprites[rockSprite], rockX - cameraX, (int)rockY, null);

        // draw player        
        g.drawImage(sprites[playerSprite
              + ((playerDead & 16) == 0 ? 0 : SPRITE_COUNT)],
            playerX - cameraX, (int)playerY, null);
      }

      // draw score
      j = score;
      x = 97;
      do {
        g.drawImage(sprites[SPRITE_DIGIT_0 + (j % 10)], x, 14, null);
        x -= 8;
        j /= 10;
      } while(j > 0);

      // draw time
      j = time;
      x = 135;
      do {
        g.drawImage(sprites[SPRITE_SMALL_DIGIT_0 + (j % 10)], x, 24, null);
        x -= 4;
        j /= 10;
      } while(j > 0);

      // draw lives
      g.drawImage(sprites[SPRITE_LIVES], 16, 30, null);
      g.drawImage(sprites[SPRITE_SMALL_DIGIT_0 + lives], 30, 30, null);

      // -- render ends --------------------------------------------------------

      // show the hidden buffer
      if (g2 == null) {
        g2 = (Graphics2D)getGraphics();
        requestFocus();
      } else {
        g2.drawImage(image, 0, 0, 608, 382, null);
      }

      // burn off extra cycles
      while(nextFrameStartTime - System.nanoTime() > 0) {
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
    final int VK_JUMP = 0x42;
    final int VK_W = 0x57;
    final int VK_S = 0x53;
    final int VK_A = 0x41;
    final int VK_D = 0x44;

    int k = keyEvent.getKeyCode();
    if (k > 0) {
      k = k == VK_W ? VK_UP : k == VK_D ? VK_RIGHT : k == VK_A ? VK_LEFT
          : k == VK_S ? VK_DOWN : k;
      a[(k >= VK_LEFT && k <= VK_DOWN) ? k : VK_JUMP]
          = keyEvent.getID() != 402;
    }
  }

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame("Jungle Hunt 4K");
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    a applet = new a();
    applet.setPreferredSize(new java.awt.Dimension(608, 382));
    frame.add(applet, java.awt.BorderLayout.CENTER);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    Thread.sleep(250);
    applet.start();
  }*/
}
