package java4k.supermarioland4k;

/*
 * Super Mario Land 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Super Mario Land 4K.
 *
 * Super Mario Land 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Super Mario Land 4K is distributed in the hope that it will be useful,
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

    final int VK_LEFT = 0x25;
    final int VK_RIGHT = 0x27;
    final int VK_JUMP = 0x44;

    final float GRAVITY = 0.18f;
    final float MARIO_ACCELERATION = 0.2f;
    final float MARIO_MAX_SPEED = 1.5f;
    final float MARIO_JUMP_SPEED = -2f;
    final float MARIO_BOUNCE_SPEED = -2f;
    final int MARIO_JUMP_EXTENSION_SLOW = 15;
    final int MARIO_JUMP_EXTENSION_FAST = 20;
    final float DYING_MARIO_GRAVITY = 0.06f;
    final float DYING_MARIO_JUMP_SPEED = -2f;
    final float CHIBIBO_SPEED = 0.3f;
    final float NOKOBON_SPEED = 0.3f;

    final int FADE_SPEED = 8;

    final int OBJ_X = 0;
    final int OBJ_Y = 1;
    final int OBJ_SPRITE = 2;
    final int OBJ_FLIPPED = 3;
    final int OBJ_SPRITE_INDEX = 4;
    final int OBJ_SPRITE_COUNTER = 5;
    final int OBJ_X1 = 6;
    final int OBJ_X2 = 7;
    final int OBJ_Y1 = 8;
    final int OBJ_Y2 = 9;
    final int OBJ_TYPE = 10;
    final int OBJ_VX = 11;
    final int OBJ_VY = 12;
    final int OBJ_SUPPORTED = 13;
    final int OBJ_OBSTRUCTED = 14;
    final int OBJ_DIRECTION = 15;
    final int OBJ_SQUASHED = 16;
    final int OBJ_MIRRORED = 17;
    final int OBJ_WEIGHTLESS = 18;
    final int OBJ_NOT_SQUASHABLE = 19;
    final int OBJ_BEHIND_TILES = 20;
    final int OBJ_COUNTER = 21;

    final int SPRITE_COUNT = 34;

    final int SPRITE_COIN = 0;
    final int SPRITE_DOOR_TOP = 1;
    final int SPRITE_DOOR_BOTTOM = 2;
    final int SPRITE_PIPE_TOP_LEFT = 3;
    final int SPRITE_PIPE_MIDDLE_LEFT = 4;
    final int SPRITE_PIPE_TOP_RIGHT = 5;
    final int SPRITE_PIPE_MIDDLE_RIGHT = 6;
    final int SPRITE_PLATFORM = 7;
    final int SPRITE_WALL = 8;
    final int SPRITE_MARIO_0 = 9;
    final int SPRITE_MARIO_1 = 10;
    final int SPRITE_MARIO_2 = 11;
    final int SPRITE_MARIO_3 = 12;
    final int SPRITE_MARIO_4 = 13; // converted into SPRITE_MARIO_2
    final int SPRITE_MARIO_HEAD = 13;
    final int SPRITE_NOKOBON_0 = 14;
    final int SPRITE_NOKOBON_1 = 15;
    final int SPRITE_CHIBIBO = 16;
    final int SPRITE_SQUASHED_CHIBIBO = 17;
    final int SPRITE_MARIO_DEAD = 18;
    final int SPRITE_BOMB_0 = 19;
    final int SPRITE_BOMB_1 = 20;
    final int SPRITE_EXPLOSION_0 = 21;
    final int SPRITE_EXPLOSION_1 = 22;
    final int SPRITE_PAKKUN_FLOWER_0 = 23;
    final int SPRITE_PAKKUN_FLOWER_1 = 24;
    final int SPRITE_GIRA_0 = 25;
    final int SPRITE_GIRA_1 = 26;
    final int SPRITE_TREE = 27;
    final int SPRITE_FIGHTER_FLY_0 = 28;
    final int SPRITE_FIGHTER_FLY_1 = 29;
    final int SPRITE_BUNBUN_0 = 30;
    final int SPRITE_BUNBUN_1 = 31;
    final int SPRITE_SPEAR = 32;
    final int SPRITE_DAISY = 33;
    final int SPRITE_PYRAMID = 34;

    final int TYPE_MARIO = 0;
    final int TYPE_CHIBIBO = 1;
    final int TYPE_NOKOBON = 2;
    final int TYPE_EXPLOSION = 3;
    final int TYPE_PAKKUN_FLOWER = 4;
    final int TYPE_GIRA = 5;
    final int TYPE_FIGHTER_FLY = 6;
    final int TYPE_BUNBUN = 7;
    final int TYPE_SPEAR = 8;

    final int ORIENTATION_ORIGINAL = 0;
    final int ORIENTATION_FLIPPED = 1;
    final int ORIENTATION_MIRRORED = 2;

    final int MAP_EMPTY = 0;
    final int MAP_COIN = 1;
    final int MAP_DOOR_TOP = 2;
    final int MAP_DOOR_BOTTOM = 3;
    final int MAP_PIPE_TOP_LEFT = 4;
    final int MAP_PIPE_MIDDLE_LEFT = 5;
    final int MAP_PIPE_TOP_RIGHT = 6;
    final int MAP_PIPE_MIDDLE_RIGHT = 7;
    final int MAP_PLATFORM = 8;
    final int MAP_WALL = 9;

    final int MAP_FLIPPED_EMPTY = 16;
    final int MAP_FLIPPED_COIN = 17;
    final int MAP_FLIPPED_DOOR_TOP = 18;
    final int MAP_FLIPPED_DOOR_BOTTOM = 19;
    final int MAP_FLIPPED_PIPE_TOP_LEFT = 20;
    final int MAP_FLIPPED_PIPE_MIDDLE_LEFT = 21;
    final int MAP_FLIPPED_PIPE_TOP_RIGHT = 22;
    final int MAP_FLIPPED_PIPE_MIDDLE_RIGHT = 23;
    final int MAP_FLIPPED_PLATFORM = 24;
    final int MAP_FLIPPED_WALL = 25;

    final int MAP_MASK = 15;
    final int MAP_EMPTIES = 3;

    final int PAKKUN_FLOWER_SLEEPING = 64;
    final int PAKKUN_FLOWER_RISING = 80;
    final int PAKKUN_FLOWER_CHOMPING = 176;
    final int PAKKUN_FLOWER_SINKING = 191;

    final int PAKKUN_FLOWER_DISTANCE = 20;

    final String S = "\u0808\ufc0f\uf2a3\ucae8\uc8f8\uc8f8\uca28\uf2a3\ufc0f\u0808\u0000\uffc0\uaaf0\u56bc\u01ac\u006c\u006c\u006c\u0808\u006c\u006c\u006c\u006c\u006c\u006c\u006c\uaaac\u0808\u0000\uaaa8\uaaa8\uaaa8\uaaa8\u0000\uaaa3\uaaa3\u0808\uaaa3\uaaa3\uaaa3\uaaa3\u000f\uaaa3\uaaa3\uaaa3\u0808\u0000\u015a\u015a\u015a\u015a\u0000\uc16a\uc16a\u0808\uc16a\uc16a\uc16a\uc16a\uf000\uc16a\uc16a\uc16a\u0808\uc003\u0aa8\u0808\u0a88\u0a88\u02a8\u0000\uc003\u0808\u0000\ufffc\uaaac\ua8ec\ua82c\uaaac\u0000\u0000\u1006\ufff4\u10ff\uff04\u103f\ufe0d\u72bf\ufe95\u56bf\uffc3\uc3ff\uff03\uc0ff\u1007\ufff1\u00ff\ufa81\u00af\ufa07\u03af\ufcf5\u55ff\ufc15\u553f\ufc17\uf43f\uffff\uc0ff\u1007\ufffc\u40ff\ufff0\u403f\uffdd\ue83f\uffd5\u597f\ufff0\u55ff\uffc0\u03ff\ufffc\u03ff\u1006\ufeb1\u00ff\ufea0\u00bf\uffd5\u55af\ufff5\u553f\ufffc\u350f\ufff0\u3fcf\u1006\uffff\uf03f\ufffc\u000f\uffff\u8a03\ufffe\ua820\ufffe\u0aa0\uffff\uaaaf\u080b\uffc3\uff3c\uff30\uff30\uff3c\ufec0\uc083\u308f\u00bf\u82bf\u280f\u080c\uffc3\uff3c\uff30\uff30\uff3c\ufec0\uc08f\u308f\u0083\u82bf\uea3f\u030f\u0808\uf00f\uc003\u0c30\u0c30\u0000\ufaaf\uca83\uc30f\u0805\uf00f\uc003\u0c30\ufaaf\u0ff0\u080b\u0fff\u03bf\u88af\ua02f\u083f\ua8ff\u90ff\u15cf\u740f\u540f\u57ff\u0806\uf00f\uc0c3\uc033\uc033\uc003\uf00f\u0806\ufaaf\ueaeb\ueabb\ueabb\ueaab\ufaaf\u0808\uc3ff\ufcff\ua3cf\uf8cf\u2e3f\ufbbc\u3cb3\u0f8f\u0806\ucfff\uffff\u3f3f\ub3ff\u2fff\uc8cf\u0810\ufc3f\uf28f\uca23\ucaa3\u2aa0\u22a8\u2aa8\u0a88\u2aa8\u0aa0\uc283\uf00f\u3c3c\u0c30\uc003\uf00f\u0810\u3ffc\u3ffc\u0ff0\u0ff0\u23c8\u23c8\u23c8\u23c8\u2008\u2828\ucaa3\uf00f\u3c3c\u0c30\uc003\uf00f\u1006\ufffc\uc00f\ufc3f\u7cf3\uf00c\u40cc\ufc3c\u4000\ufffc\u4003\ufffc\uc00f\u1006\uf0fc\uc00f\ucf3f\u7cf3\u3cfc\u40cc\ucf3c\u4000\uf0fc\u4003\ufffc\uc00f\u1006\uff03\uf03f\ufcfc\ucfcf\uf3f0\u00f3\uf3cf\u0cf3\uff3f\u3f3f\uff3f\u3f3f\u080e\uff0f\ufcf3\uf3f3\u33a3\u8383\u3c0f\u8cff\u00f0\uccc0\ucc0f\u03ff\u8fff\u800f\u3f03\u080b\u3fc3\u833c\u3cfc\u8ce8\u0003\uccff\ucc0f\u0303\u8fff\u00ff\uf03f\u1010\uf03f\uffff\uc3cf\uffff\ucff3\uc00f\ucbf3\u23f3\ucabc\u63c8\uc2ac\u63c8\uf00c\u6828\uffc3\u1aa0\uf000\u02a8\uc1df\u0c03\ucdd7\u0fff\ucdf4\u33cf\uf000\ufcc3\u0000\u003c\uf3fc\ufcc3\uffff\uffcf\u100f\uffff\uc00f\uffff\u23f3\ufffc\u63c8\uc00c\u63c8\u3ff0\u6828\u3fff\u1a80\u0ea0\u023f\uf00f\u0003\ucdd7\u0fff\ucdf4\u33ff\uf000\ufccf\ucff3\ufcc3\u0000\u003c\ucff3\ufcc3\uffff\uffcf\u080f\ufc0f\ufc8f\ufc8f\ufc8f\ufc8f\ufc8f\ufc8f\ufc8f\ufc8f\ufc8f\uc000\ucffc\uf3f3\ufccf\uff3f\u1010\uffc0\u0fff\uff00\u03ff\ufc00\u80ff\ufc0a\ua8ff\uf008\u88ff\uf00a\ua8ff\uc00a\u2bff\uc000\u8fff\uc036\ua7ef\uf0d5\u56af\uffe9\u5aff\uff6a\uafff\ufd55\u5fff\uf555\u57ff\ud555\u57ff\u5555\u57ff";

    int i;
    int j;
    int k;
    int x;
    int y;
    int z;
    int level = 0;
    int cameraX = 0;
    int enemiesX = 0;
    int jumpCounter = 0;
    boolean marioDied = false;
    boolean jumpReleased = true;
    int giraCountdown = 1;
    int bunbunCountdown = 1;
    int fadeDelta = FADE_SPEED;
    int fadeIntensity = 255;    
    int[][] map = null;
    int[][] enemies = null;
    Graphics2D g2 = null;
    Random random = null;
    float[] mario = null;
    
    int[] pixels = new int[64];
    ArrayList<float[]> queue = new ArrayList<float[]>();
    ArrayList<float[]>[] backgroundPlanes = new ArrayList[4];
    BufferedImage[][] sprites = new BufferedImage[3][64];
    BufferedImage image = new BufferedImage(160, 144, 1);
    Graphics2D g = (Graphics2D)image.getGraphics();
        
    Color BLACK = new Color(0x000000);
    Color LIGHT_GRAY = new Color(0xA8A8A8);
    Color WHITE = new Color(0xF8F8F8);

    // decompress the sprites    
    for(i = 0, k = 0; i < SPRITE_COUNT; i++) {
      j = S.charAt(k++);
      int width = j >> 8;
      int height = j & 0xFF;
      int height2 = width == 8 && height < 9 ? 8 : 16;
      sprites[0][i] = new BufferedImage(width, height2, 2);
      for(y = 0; y < height; y++) {
        long value = S.charAt(k++);
        if (width == 16) {
          value <<= 16;
          value |= S.charAt(k++);
        }
        for(x = 0; x < width; x++) {
          z = ((int)value) & 3;
          pixels[x] = z == 0 ? 0xFF000000 : z == 1 ? 0xFF606060
              : z == 2 ? 0xFFA8A8A8 : 0;
          value >>= 2;
        }
        sprites[0][i].setRGB(
            0, y + height2 - height, width, 1, pixels, 0, width);
      }
    }

    // attach Mario's head
    g2 = (Graphics2D)sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_0]
        .getGraphics();
    g2.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_HEAD], 3, -6, null);
    g2 = (Graphics2D)sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_1]
        .getGraphics();
    g2.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_HEAD], 4, -7, null);
    g2 = (Graphics2D)sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_2]
        .getGraphics();
    g2.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_HEAD], 3, -7, null);
    g2 = (Graphics2D)sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_3]
        .getGraphics();
    g2.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_HEAD], 3, -6, null);

    // create flipped and mirrored versions of the sprites
    for(i = 0; i < SPRITE_COUNT; i++) {
      x = sprites[ORIENTATION_ORIGINAL][i].getWidth();
      y = sprites[ORIENTATION_ORIGINAL][i].getHeight();
      sprites[ORIENTATION_FLIPPED][i] = new BufferedImage(x, y, 2);
      sprites[ORIENTATION_MIRRORED][i] = new BufferedImage(x << 1, y, 2);
      g2 = (Graphics2D)sprites[ORIENTATION_FLIPPED][i].getGraphics();
      g2.drawImage(sprites[ORIENTATION_ORIGINAL][i],
          0, 0, x, y,
          x, 0, 0, y,
          null);
      g2 = (Graphics2D)sprites[ORIENTATION_MIRRORED][i].getGraphics();
      g2.drawImage(sprites[ORIENTATION_ORIGINAL][i], 0, 0, null);
      g2.drawImage(sprites[ORIENTATION_ORIGINAL][i],
          x, 0, x << 1, y,
          x, 0, 0, y,
          null);
    }

    // create pyramid sprite
    sprites[0][SPRITE_PYRAMID] = new BufferedImage(144, 72, 2);
    g2 = (Graphics2D)sprites[0][SPRITE_PYRAMID].getGraphics();
    g2.setColor(BLACK);
    g2.drawLine(0, 71, 71, 0);
    g2.drawLine(72, 0, 143, 71);
    g2.setColor(WHITE);
    for(i = 0; i < 71; i++) {
      g2.drawLine(71 - i, i + 1, 72 + i, i + 1);
    }
    g2 = null;

    long nextFrameStartTime = System.nanoTime();
    while(true) {

      do {
        nextFrameStartTime += 16666667;

        // -- update starts ----------------------------------------------------

        // update fade transition
        if (fadeDelta != 0) {
          fadeIntensity += fadeDelta;
          if (fadeDelta < 0) {
            if (fadeIntensity <= 0) {
              fadeIntensity = 0;
              fadeDelta = 0;
            }
          } else {
            if (fadeIntensity >= 255) {
              fadeIntensity = 255;
              fadeDelta = -fadeDelta;

              // visible map Y is between 16 and 31 (inclusive)
              // Y: 0--15 is the sky
              // Y: 32--47 is underground
              map = new int[48][512];
              enemies = new int[48][512];
              queue.clear();

              // -- create level start -----------------------------------------
              random = new Random(4 + (level == 4 ? 16 : level));

              // initialize background planes
              for(i = 0; i < 4; i++) {
                backgroundPlanes[i] = new ArrayList<float[]>();
                k = random.nextInt(80);
                for(j = 0; j < 4; j++) {
                  float[] object = new float[32];
                  backgroundPlanes[i].add(object);
                  object[OBJ_X] = k;
                  object[OBJ_Y] = 72 + (random.nextInt(4) << 3);
                  k += ((i == 3) ? 32 : 160) + random.nextInt(80);
                }
              }

              for(x = 0; x < 340; x++) {
                if (x < 170 - 22 * level || x >= 170 + 22 * level) {
                  map[30][x] = map[31][x] = MAP_PLATFORM;
                }
              }
              for(x = 0; x < 48; x++) {
                map[x][318] = MAP_WALL;
                map[x][319] = MAP_FLIPPED_WALL;
              }
              map[28][318] = MAP_DOOR_TOP;
              map[28][319] = MAP_FLIPPED_DOOR_TOP;
              map[29][318] = MAP_DOOR_BOTTOM;
              map[29][319] = MAP_FLIPPED_DOOR_BOTTOM;
              i = 30;
              j = 2;
              x = 32;
              outter: while(x < 300) {
                boolean addCoins = random.nextInt(5) == 0;
                for(k = 0; k < j; k++) {
                  map[i][x + k] = MAP_PLATFORM;
                  if (addCoins) {
                    if (map[i - 2][x + k] == MAP_EMPTY) {
                      map[i - 1 - (k & 1)][x + k] = MAP_COIN;
                    }
                  }
                }
                if (x > 40 && x < 300 && random.nextInt(3) == 1) {
                  k = random.nextInt(level == 0 ? 2 : 3);
                  if (k == 0) {
                    enemies[i - 1][x + j - 5] = TYPE_CHIBIBO;
                  } else if (k == 1) {
                    enemies[i - 2][x + j - 5] = TYPE_NOKOBON;
                  } else {
                    enemies[i - 2][x + j - 5] = TYPE_FIGHTER_FLY;
                  }
                }
                int pipeX = 0;
                if (random.nextInt(3) == 0 && i > 21) {
                  k = i - 2 - random.nextInt(3);
                  z = x + 1 + ((j > 2) ? random.nextInt(j - 2) : 0);
                  pipeX = z + 3;
                  map[k][z] = MAP_PIPE_TOP_LEFT;
                  map[k][z + 1] = MAP_PIPE_TOP_RIGHT;
                  for(y = k + 1; y < i; y++) {
                    map[y][z] = MAP_PIPE_MIDDLE_LEFT;
                    map[y][z + 1] = MAP_PIPE_MIDDLE_RIGHT;
                  }
                  if (level > 0) {
                    enemies[k][z] = TYPE_PAKKUN_FLOWER;
                  }
                }
                if (random.nextInt(5) == 0) {
                  x += j + random.nextInt(8);
                } else {
                  x += 2 + random.nextInt(j);
                }
                if (x < pipeX) {
                  x = pipeX;
                }
                j = 3 + random.nextInt(8);
                do {
                  if (random.nextBoolean()) {
                    z = i - (3 + random.nextInt(2));
                  } else {
                    z = i + 2 + random.nextInt(8);
                  }
                } while(z < 20 || z > 30);
                i = z;
              }

              // remove enemies that ended up inside of solid blocks
              for(x = 0; x < 340; x++) {
                for(y = 16; y < 32; y++) {
                  if (enemies[y][x] != 0
                      && enemies[y][x] != TYPE_PAKKUN_FLOWER) {
                    for(i = 0; i < 5; i++) {
                      if (map[y][i + x] != MAP_EMPTY) {
                        enemies[y][x] = 0;
                      }
                    }
                  }
                }
              }

              // create Mario
              mario = new float[32];
              queue.add(mario);
              mario[OBJ_X] = 176;
              mario[OBJ_Y] = 224;
              mario[OBJ_SPRITE] = SPRITE_MARIO_0;
              mario[OBJ_X1] = 4;
              mario[OBJ_Y1] = 4;
              mario[OBJ_X2] = 11;
              mario[OBJ_Y2] = 15;
              mario[OBJ_SUPPORTED] = 1;

              marioDied = false;
              cameraX = 160;
              enemiesX = 0;

              // -- create level end -------------------------------------------

              // since level creation takes a while, reset the game timer
              nextFrameStartTime = System.nanoTime();
            }
          }
          continue;
        }

        // update dying Mario
        if (marioDied) {
          mario[OBJ_VY] += DYING_MARIO_GRAVITY;
          mario[OBJ_Y] += mario[OBJ_VY];
          if (mario[OBJ_Y] > 320) {
            // reset level
            fadeDelta = FADE_SPEED;
          }
          continue;
        }

        // update Mario
        boolean marioWalking = false;
        if (a[VK_LEFT]) {
          if (mario[OBJ_VX] > -MARIO_MAX_SPEED) {
            mario[OBJ_VX] -= MARIO_ACCELERATION;
          }
          mario[OBJ_FLIPPED] = 1;
          marioWalking = true;
        } else if (a[VK_RIGHT]) {
          if (mario[OBJ_VX] < MARIO_MAX_SPEED) {
            mario[OBJ_VX] += MARIO_ACCELERATION;
          }
          mario[OBJ_FLIPPED] = 0;
          marioWalking = true;
        }

        if (marioWalking) {
          // Mario walks
           mario[OBJ_SPRITE] = SPRITE_MARIO_1 + mario[OBJ_SPRITE_INDEX];
           if (mario[OBJ_SPRITE] == SPRITE_MARIO_4) {
             mario[OBJ_SPRITE] = SPRITE_MARIO_2;
           }
           if (++mario[OBJ_SPRITE_COUNTER] == 4) {
             mario[OBJ_SPRITE_COUNTER] = 0;
             if (++mario[OBJ_SPRITE_INDEX] == 4) {
               mario[OBJ_SPRITE_INDEX] = 0;
             }
           }
        } else {
          // Mario stands there
          mario[OBJ_SPRITE] = SPRITE_MARIO_0;
          mario[OBJ_SPRITE_INDEX] = 0;
          mario[OBJ_SPRITE_COUNTER] = 0;

          // Mario slows down
          if (mario[OBJ_VX] < -MARIO_ACCELERATION) {
            mario[OBJ_VX] += MARIO_ACCELERATION;
          } else if (mario[OBJ_VX] > MARIO_ACCELERATION) {
            mario[OBJ_VX] -= MARIO_ACCELERATION;
          } else {
            mario[OBJ_VX] = 0;
          }
        }

        if (!a[VK_JUMP]) {
          jumpCounter = 0;
        }
        if (mario[OBJ_SUPPORTED] == 1) {
          if (jumpReleased) {
            if (a[VK_JUMP]) {
              mario[OBJ_VY] = MARIO_JUMP_SPEED;
              jumpReleased = false;
              jumpCounter = (mario[OBJ_VX] < 0 ? -mario[OBJ_VX] : mario[OBJ_VX]) 
                  >= MARIO_MAX_SPEED ? MARIO_JUMP_EXTENSION_FAST
                      : MARIO_JUMP_EXTENSION_SLOW;
            }
          } else {
            if (!a[VK_JUMP]) {
              jumpReleased = true;
            }
          }
        } else {
          // Mario poses as he flies through the air
          mario[OBJ_SPRITE] = SPRITE_MARIO_1;
          if (--jumpCounter > 0 && a[VK_JUMP]) {
            mario[OBJ_VY] = MARIO_JUMP_SPEED;
          }
        }

        // scan for coins
        if (map[((int)(mario[OBJ_Y] + mario[OBJ_Y1])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X1])) >> 3] == MAP_COIN) {
          map[((int)(mario[OBJ_Y] + mario[OBJ_Y1])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X1])) >> 3] = MAP_EMPTY;
        }
        if (map[((int)(mario[OBJ_Y] + mario[OBJ_Y2])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X1])) >> 3] == MAP_COIN) {
          map[((int)(mario[OBJ_Y] + mario[OBJ_Y2])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X1])) >> 3] = MAP_EMPTY;
        }
        if (map[((int)(mario[OBJ_Y] + mario[OBJ_Y1])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X2])) >> 3] == MAP_COIN) {
          map[((int)(mario[OBJ_Y] + mario[OBJ_Y1])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X2])) >> 3] = MAP_EMPTY;
        }
        if (map[((int)(mario[OBJ_Y] + mario[OBJ_Y2])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X2])) >> 3] == MAP_COIN) {
          map[((int)(mario[OBJ_Y] + mario[OBJ_Y2])) >> 3]
              [((int)(mario[OBJ_X] + mario[OBJ_X2])) >> 3] = MAP_EMPTY;
        }

        // update queue
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] object = queue.get(i);

          if (object[OBJ_SQUASHED] == 0) {

            // apply gravity
            if (object[OBJ_WEIGHTLESS] == 0) {
              object[OBJ_VY] += GRAVITY;
              object[OBJ_SUPPORTED] = 0;
              if (object[OBJ_VY] > 0) {
                // blocks only affect objects in downwards direction
                // scan all points in range established by VY
                for(y = (int)object[OBJ_Y];
                    y <= (int)(object[OBJ_Y] + object[OBJ_VY]); y++) {
                  x = (y + (int)object[OBJ_Y2]) >> 3;
                  z = (y + (int)object[OBJ_Y2] + 1) >> 3;
                  if (((map[z][((int)object[OBJ_X] + (int)object[OBJ_X1]) >> 3]
                        & MAP_MASK) > MAP_EMPTIES
                      && ((map[x][((int)object[OBJ_X]
                          + (int)object[OBJ_X1]) >> 3]) & MAP_MASK)
                              <= MAP_EMPTIES)
                      || ((map[z][((int)object[OBJ_X]
                          + (int)object[OBJ_X2]) >> 3] & MAP_MASK) > MAP_EMPTIES
                              && (map[x][((int)object[OBJ_X]
                                  + (int)object[OBJ_X2]) >> 3]
                                      & MAP_MASK) <= MAP_EMPTIES)) {
                    // object is supported
                    object[OBJ_Y] = y;
                    object[OBJ_VY] = 0;
                    object[OBJ_SUPPORTED] = 1;
                    break;
                  }
                }
              }
              object[OBJ_Y] += object[OBJ_VY];
            }

            // move in X direction
            outter: {
              object[OBJ_OBSTRUCTED] = 0;
              for(x = (int)object[OBJ_X];
                  x != (int)(object[OBJ_X] + object[OBJ_VX]);
                      x += (object[OBJ_VX] < 0) ? -1 : 1) {

                // test for wall collision
                z = ((object[OBJ_VX] < 0)
                    ? (x + (int)object[OBJ_X1]) - 1
                    : (x + (int)object[OBJ_X2]) + 1) >> 3;
                k = ((object[OBJ_VX] < 0)
                    ? (x + (int)object[OBJ_X1])
                    : (x + (int)object[OBJ_X2])) >> 3;

                for(j = 0; j < 3; j++) {
                  y = ((int)object[OBJ_Y] + (int)object[OBJ_Y1]
                      + ((j * ((int)object[OBJ_Y2]
                          - (int)object[OBJ_Y1])) >> 1)) >> 3;
                  if (((map[y][z] & MAP_MASK) > MAP_EMPTIES
                      && (map[y][k] & MAP_MASK) <= MAP_EMPTIES)) {
                    // object obstructed by bricks in X direction
                    object[OBJ_X] = x;
                    object[OBJ_VX] = 0;
                    object[OBJ_OBSTRUCTED] = 1;
                    break outter;
                  }
                }

                // test for enemy-enemy collision
                if (object != mario) {
                  k = (object[OBJ_VX] < 0) ? x - 1 : x + 1;
                  for(j = queue.size() - 1; j >= 0; j--) {
                    if (j != i) {
                      float[] obj = queue.get(j);
                      if (obj != mario
                          && obj[OBJ_SQUASHED] == 0
                          && obj[OBJ_X] + obj[OBJ_X1]
                              <= k + object[OBJ_X2]
                          && obj[OBJ_X] + obj[OBJ_X2]
                              >= k + object[OBJ_X1]
                          && obj[OBJ_Y] + obj[OBJ_Y1]
                              <= object[OBJ_Y] + object[OBJ_Y2]
                          && obj[OBJ_Y] + obj[OBJ_Y2]
                              >= object[OBJ_Y] + object[OBJ_Y1]) {

                        // object obstructed by enemy in X direction
                        object[OBJ_X] = x;
                        object[OBJ_VX] = 0;
                        object[OBJ_OBSTRUCTED] = 1;
                        break outter;
                      }
                    }
                  }
                }
              }
              object[OBJ_X] += object[OBJ_VX];

              // test for enemy-Mario collision
              if (mario != object
                  && mario[OBJ_X] + mario[OBJ_X1]
                      <= object[OBJ_X] + object[OBJ_X2]
                  && mario[OBJ_X] + mario[OBJ_X2]
                      >= object[OBJ_X] + object[OBJ_X1]
                  && mario[OBJ_Y] + mario[OBJ_Y1]
                      <= object[OBJ_Y] + object[OBJ_Y2]
                  && mario[OBJ_Y] + mario[OBJ_Y2]
                      >= object[OBJ_Y] + object[OBJ_Y1]) {

                if (mario[OBJ_VY] == 0 || object[OBJ_NOT_SQUASHABLE] == 1
                    || ((object[OBJ_TYPE] == TYPE_GIRA
                        || object[OBJ_TYPE] == TYPE_BUNBUN)
                            && mario[OBJ_VY] <= 0)) {
                  // Enemy killed Mario
                  marioDied = true;
                  mario[OBJ_MIRRORED] = 1;
                  mario[OBJ_VY] = DYING_MARIO_JUMP_SPEED;
                  mario[OBJ_SPRITE] = SPRITE_MARIO_DEAD;
                } else if (mario[OBJ_VY] > 0) {
                  // Mario squashed enemy
                  mario[OBJ_VY] = MARIO_BOUNCE_SPEED;
                  object[OBJ_SQUASHED] = 1;
                  object[OBJ_SPRITE_COUNTER] = 0;
                  object[OBJ_VX] = 0;
                } 
              }
            }
          }

          // update enemies
          if (object[OBJ_TYPE] == TYPE_CHIBIBO) {

            // update chibibo

            if (object[OBJ_SQUASHED] == 1) {
              object[OBJ_SPRITE] = SPRITE_SQUASHED_CHIBIBO;
              if (++object[OBJ_SPRITE_COUNTER] == 40) {
                queue.remove(i);
                continue;
              }
            } else {
              if (++object[OBJ_SPRITE_COUNTER] == 8) {
                object[OBJ_SPRITE_COUNTER] = 0;
                object[OBJ_FLIPPED] = (int)object[OBJ_FLIPPED] ^ 1;
              }
              if (object[OBJ_OBSTRUCTED] == 1) {
                object[OBJ_DIRECTION] = (int)object[OBJ_DIRECTION] ^ 1;
                object[OBJ_VX] = (object[OBJ_DIRECTION] == 1)
                    ? CHIBIBO_SPEED : -CHIBIBO_SPEED;
              }
            }
          } else if (object[OBJ_TYPE] == TYPE_NOKOBON) {

            // update nokobon

            if (object[OBJ_SQUASHED] == 1) {

              // update bomb

              if (object[OBJ_SPRITE_COUNTER] == 0) {
                object[OBJ_Y] += 8;
                object[OBJ_WEIGHTLESS] = 1;
              }
              object[OBJ_SPRITE] = SPRITE_BOMB_0 
                  + ((object[OBJ_SPRITE_COUNTER] < 15) 
                      ? 0
                      : ((((int)object[OBJ_SPRITE_COUNTER]) >> 2) & 1));
              if (++object[OBJ_SPRITE_COUNTER] > 50) {

                // bomb explodes

                object[OBJ_TYPE] = TYPE_EXPLOSION;
                object[OBJ_MIRRORED] = 1;
                object[OBJ_X] -= 4;
                object[OBJ_X1] = 0;
                object[OBJ_Y1] = 0;
                object[OBJ_X2] = 15;
                object[OBJ_Y2] = 7;
                object[OBJ_SQUASHED] = 0;
                object[OBJ_NOT_SQUASHABLE] = 1;
              }
            } else {
              if (++object[OBJ_SPRITE_COUNTER] == 8) {
                object[OBJ_SPRITE_COUNTER] = 0;
                object[OBJ_SPRITE] = object[OBJ_SPRITE] == SPRITE_NOKOBON_0
                    ? SPRITE_NOKOBON_1 : SPRITE_NOKOBON_0;
              }
              if (object[OBJ_OBSTRUCTED] == 1 || (object[OBJ_SUPPORTED] == 1
                  && (map[(16 + (int)object[OBJ_Y]) >> 3]
                      [((int)(object[OBJ_X] + (object[OBJ_VX] < 0 ? -1 : 8)))
                          >> 3] & MAP_MASK) <= MAP_EMPTIES)) {
                object[OBJ_DIRECTION] = (int)object[OBJ_DIRECTION] ^ 1;
                object[OBJ_FLIPPED] = object[OBJ_DIRECTION];
                if (object[OBJ_DIRECTION] == 1) {
                  object[OBJ_VX] = NOKOBON_SPEED;
                } else {
                  object[OBJ_VX] = -NOKOBON_SPEED;
                }
              }
            }
          } else if (object[OBJ_TYPE] == TYPE_EXPLOSION) {

            // update explosion

            object[OBJ_SPRITE] = SPRITE_EXPLOSION_0
                  + ((((int)object[OBJ_SPRITE_COUNTER]) >> 2) & 1);
            if (++object[OBJ_SPRITE_COUNTER] > 100) {
              queue.remove(i);
              continue;
            }
          } else if (object[OBJ_TYPE] == TYPE_PAKKUN_FLOWER) {

            // update pakkun flower

            if (++object[OBJ_SPRITE_COUNTER] <= PAKKUN_FLOWER_SLEEPING) {
              if (object[OBJ_SPRITE_COUNTER] == PAKKUN_FLOWER_SLEEPING) {
                k = (int)mario[OBJ_X] - (int)object[OBJ_X] + 4;
                if (k < 0) {
                  k = -k;
                }
                if (k < PAKKUN_FLOWER_DISTANCE) {
                  object[OBJ_SPRITE_COUNTER] = 0;
                }
              }
            } else if (object[OBJ_SPRITE_COUNTER] < PAKKUN_FLOWER_RISING) {
              object[OBJ_Y]--;
            } else if (object[OBJ_SPRITE_COUNTER] < PAKKUN_FLOWER_CHOMPING) {
              object[OBJ_SPRITE] = SPRITE_PAKKUN_FLOWER_0
                  + ((((int)object[OBJ_SPRITE_COUNTER]) >> 4) & 1);
            } else if (object[OBJ_SPRITE_COUNTER] < PAKKUN_FLOWER_SINKING) {
              object[OBJ_Y]++;
            } else {
              object[OBJ_SPRITE_COUNTER] = 0;
            }
          } else if (object[OBJ_TYPE] == TYPE_GIRA) {

            // update gira

            if (object[OBJ_SQUASHED] == 1) {
              object[OBJ_VY] += DYING_MARIO_GRAVITY;
              object[OBJ_Y] += object[OBJ_VY];
            } else {
              object[OBJ_X]--;
            }

            if (++object[OBJ_SPRITE_COUNTER] == 8) {
              object[OBJ_SPRITE_COUNTER] = 0;
              object[OBJ_SPRITE] = object[OBJ_SPRITE] != SPRITE_GIRA_0
                  ? SPRITE_GIRA_0 : SPRITE_GIRA_1;
            }
          } else if (object[OBJ_TYPE] == TYPE_FIGHTER_FLY) {

            // update fighter fly

            if (object[OBJ_SQUASHED] == 1) {
              object[OBJ_VY] += DYING_MARIO_GRAVITY;
              object[OBJ_Y] += object[OBJ_VY];
              object[OBJ_WEIGHTLESS] = 1;
            } else {
              if (object[OBJ_SUPPORTED] == 1 && ++object[OBJ_COUNTER] == 60) {
                object[OBJ_VY] = -2.5f;
                object[OBJ_COUNTER] = 0;
              }
              object[OBJ_VX] = object[OBJ_VY] == 0
                  ? 0 : (object[OBJ_X] > mario[OBJ_X]) ? -1 : 1;

              if (++object[OBJ_SPRITE_COUNTER] == 16) {
                object[OBJ_SPRITE_COUNTER] = 0;
                object[OBJ_SPRITE] = object[OBJ_SPRITE] != SPRITE_FIGHTER_FLY_0
                    ? SPRITE_FIGHTER_FLY_0 : SPRITE_FIGHTER_FLY_1;
              }
            }
          } else if (object[OBJ_TYPE] == TYPE_BUNBUN) {

            // update bunbun

            if (object[OBJ_SQUASHED] == 1) {
              object[OBJ_VY] += DYING_MARIO_GRAVITY;
              object[OBJ_Y] += object[OBJ_VY];
            } else {              
              if (++object[OBJ_COUNTER] < 40) {
                object[OBJ_X]--;
              }
              if (object[OBJ_COUNTER] == 40) {
                float[] spear = new float[32];
                queue.add(spear);
                spear[OBJ_X] = object[OBJ_X] + 4;
                spear[OBJ_Y] = object[OBJ_Y];
                spear[OBJ_X1] = 2;
                spear[OBJ_Y1] = 1;
                spear[OBJ_X2] = 4;
                spear[OBJ_Y2] = 15;
                spear[OBJ_SPRITE] = SPRITE_SPEAR;
                spear[OBJ_WEIGHTLESS] = 1;
                spear[OBJ_NOT_SQUASHABLE] = 1;
                spear[OBJ_TYPE] = TYPE_SPEAR;
              } else if (object[OBJ_COUNTER] == 60) {
                object[OBJ_COUNTER] = 0;
              }

              if (++object[OBJ_SPRITE_COUNTER] == 8) {
                object[OBJ_SPRITE_COUNTER] = 0;
                object[OBJ_SPRITE] = object[OBJ_SPRITE] != SPRITE_BUNBUN_0
                    ? SPRITE_BUNBUN_0 : SPRITE_BUNBUN_1;
              }
            }
          } else if (object[OBJ_TYPE] == TYPE_SPEAR) {

            // update spear

            object[OBJ_Y]++;
          }

          // remove out of bounds enemies
          if (object[OBJ_X] < cameraX - 80 || object[OBJ_Y] > 320) {
            queue.remove(i);

            if (object == mario) {
              // reset level
              fadeDelta = FADE_SPEED;
            }
          }
        }

        // left side of the screen acts as a wall
        if (mario[OBJ_X] < cameraX - 3) {
          mario[OBJ_X] = cameraX - 3;
        }

        // update camera
        i = (int)mario[OBJ_X] - 72;
        if (i > cameraX) {
          cameraX = i;
        }
        if (cameraX > 2400) {
          cameraX = 2400;
        }

        // generate enemies
        i = (cameraX + 160) >> 3;
        while(enemiesX <= i) {
          for(y = 16; y < 32; y++) {
            if (enemies[y][enemiesX] == TYPE_CHIBIBO) {
              for(j = random.nextInt(2) + 1; j >= 0; j--) {

                // create chibibo

                float[] chibibo = new float[32];
                queue.add(chibibo);
                chibibo[OBJ_X] = (enemiesX << 3) + j * 12;
                chibibo[OBJ_Y] = y << 3;
                chibibo[OBJ_VX] = -CHIBIBO_SPEED;
                chibibo[OBJ_X1] = 1;
                chibibo[OBJ_X2] = 6;
                chibibo[OBJ_Y2] = 7;
                chibibo[OBJ_SPRITE] = SPRITE_CHIBIBO;
                chibibo[OBJ_TYPE] = TYPE_CHIBIBO;
              }
            } else if (enemies[y][enemiesX] == TYPE_NOKOBON) {
              for(j = random.nextInt(2) + 1; j >= 0; j--) {

                // create nokobon

                float[] nokobon = new float[32];
                queue.add(nokobon);
                nokobon[OBJ_X] = (enemiesX << 3) + j * 12;
                nokobon[OBJ_Y] = y << 3;
                nokobon[OBJ_VX] = -NOKOBON_SPEED;
                nokobon[OBJ_Y1] = 10;
                nokobon[OBJ_X2] = 8;
                nokobon[OBJ_Y2] = 15;
                nokobon[OBJ_SPRITE] = SPRITE_NOKOBON_0;
                nokobon[OBJ_TYPE] = TYPE_NOKOBON;
              }
            } else if (enemies[y][enemiesX] == TYPE_PAKKUN_FLOWER) {

              // create pakkun flower

              float[] pakkunFlower = new float[32];
              queue.add(pakkunFlower);
              pakkunFlower[OBJ_X] = (enemiesX << 3) + 4;
              pakkunFlower[OBJ_Y] = y << 3;
              pakkunFlower[OBJ_X2] = 7;
              pakkunFlower[OBJ_Y2] = 15;
              pakkunFlower[OBJ_SPRITE] = SPRITE_PAKKUN_FLOWER_0;
              pakkunFlower[OBJ_WEIGHTLESS] = 1;
              pakkunFlower[OBJ_NOT_SQUASHABLE] = 1;
              pakkunFlower[OBJ_BEHIND_TILES] = 1;
              pakkunFlower[OBJ_TYPE] = TYPE_PAKKUN_FLOWER;
            } else if (enemies[y][enemiesX] == TYPE_FIGHTER_FLY) {

              // create fighter fly

              float[] fighterFly = new float[32];
              queue.add(fighterFly);
              fighterFly[OBJ_X] = enemiesX << 3;
              fighterFly[OBJ_Y] = y << 3;
              fighterFly[OBJ_Y1] = 5;
              fighterFly[OBJ_X2] = 15;
              fighterFly[OBJ_Y2] = 15;
              fighterFly[OBJ_SPRITE] = SPRITE_FIGHTER_FLY_0;
              fighterFly[OBJ_MIRRORED] = 1;
              fighterFly[OBJ_TYPE] = TYPE_FIGHTER_FLY;
            }
          }
          enemiesX++;
        }

        if ((level == 2 || level == 4) && cameraX < 2320) {
          if (--giraCountdown == 0) {

            // create gira

            giraCountdown = 180;
            float[] gira = new float[32];
            queue.add(gira);
            gira[OBJ_X] = cameraX + 160;
            gira[OBJ_Y] = ((15 + random.nextInt(14)) << 3) - 1;
            gira[OBJ_X2] = 8;
            gira[OBJ_Y1] = 10;
            gira[OBJ_Y2] = 15;
            gira[OBJ_SPRITE] = SPRITE_GIRA_0;
            gira[OBJ_WEIGHTLESS] = 1;
            gira[OBJ_TYPE] = TYPE_GIRA;
          }
        }

        if (level > 2 && cameraX < 2320) {
          if (--bunbunCountdown == 0) {

            // create bunbun

            bunbunCountdown = 360 + random.nextInt(360);
            float[] bunbun = new float[32];
            queue.add(bunbun);
            bunbun[OBJ_X] = cameraX + 160;
            bunbun[OBJ_Y] = mario[OBJ_Y] - ((2 + random.nextInt(5)) << 3);
            bunbun[OBJ_X2] = 15;
            bunbun[OBJ_Y2] = 15;
            bunbun[OBJ_SPRITE] = SPRITE_BUNBUN_0;
            bunbun[OBJ_WEIGHTLESS] = 1;
            bunbun[OBJ_TYPE] = TYPE_BUNBUN;

            if (bunbun[OBJ_Y] < 144) {
              bunbun[OBJ_Y] = 144;
            }
          }
        }

        // check if mario reached end of level
        if (mario[OBJ_X] >= 2544) {
          // advance level
          level++;
          fadeDelta = FADE_SPEED;
        }

        // -- update ends ------------------------------------------------------

      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------

      // clear frame
      g.setColor(WHITE);
      g.fillRect(0, 0, 160, 144);

      if (level == 5) {
        // draw ending
        g.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_MARIO_0],
            67, 64, null);
        g.drawImage(sprites[ORIENTATION_ORIGINAL][SPRITE_DAISY], 78, 64, null);
      } else {

        // draw sky
        g.setColor(LIGHT_GRAY);
        g.fillRect(0, 16, 160, 9);
        g.drawLine(0, 30, 160, 30);
        g.drawLine(0, 28, 160, 28);
        g.drawLine(0, 26, 160, 26);        

        // draw background planes
        g.setColor(BLACK);
        for(i = 0; i < 4; i++) {
          for(j = 0; j < 4; j++) {
            float[] object = backgroundPlanes[i].get(j);
            k = ((int)object[OBJ_X]) - (cameraX >> (4 - i));
            g.drawImage(sprites[0][i == 3 ? SPRITE_TREE : SPRITE_PYRAMID],
                k, (int)object[OBJ_Y], null);
            if (i == 3) {
              g.drawLine(k + 7, 16 + (int)object[OBJ_Y], k + 7, 143);
            }
            if (k + ((i == 3) ? 16 : 144) < 0) {
              object[OBJ_X] += ((160 + random.nextInt(40)) << (4 - i));
              object[OBJ_Y] = 72 + (random.nextInt(4) << 3);
            }
          }
        }

        // draw sprites behind of tiles
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] object = queue.get(i);
          if (object[OBJ_BEHIND_TILES] == 1) {
            g.drawImage(sprites[object[OBJ_MIRRORED] == 0
                    ? (int)object[OBJ_FLIPPED]
                    : ORIENTATION_MIRRORED][(int)object[OBJ_SPRITE]],
                ((int)object[OBJ_X]) - cameraX,
                ((int)object[OBJ_Y]) - 112, null);
          }
        }

        // draw blocks
        int mapOffset = cameraX >> 3;
        int drawOffset = cameraX & 7;
        for(y = 0; y < 16; y++) {
          for(x = 0; x < 21; x++) {
            i = map[y + 16][x + mapOffset];
            j = i & MAP_MASK;
            if (j > MAP_EMPTY) {
              g.drawImage(sprites[i >> 4][j - 1],
                  (x << 3) - drawOffset, 16 + (y << 3), null);
            }
          }
        }

        // draw sprites in front of tiles
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] object = queue.get(i);
          if (object[OBJ_BEHIND_TILES] == 0) {
            g.drawImage(sprites[object[OBJ_MIRRORED] == 0
                    ? (int)object[OBJ_FLIPPED]
                    : ORIENTATION_MIRRORED][(int)object[OBJ_SPRITE]],
                ((int)object[OBJ_X]) - cameraX,
                ((int)object[OBJ_Y]) - 112, null);
          }
        }

        // darken image during fading
        if (fadeDelta != 0) {
          g.setColor(new Color(fadeIntensity << 24, true));
          g.fillRect(0, 0, 160, 144);
        }
      }

      // -- render ends --------------------------------------------------------

      // show the hidden buffer
      if (g2 != null) {
        g2.drawImage(image, 0, 0, 640, 576, null);
      } else {
        g2 = (Graphics2D)getGraphics();
        requestFocus();
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
    final int VK_DOWN = 0x28;
    final int VK_JUMP = 0x44;
    final int VK_A = 0x41;
    final int VK_D = 0x44;
    final int VK_S = 0x53;

    int k = keyEvent.getKeyCode();
    if (k > 0) {
      if (k == VK_A) {
        k = VK_LEFT;
      } else if (k == VK_D) {
        k = VK_RIGHT;
      }
      a[(k == VK_LEFT || k == VK_RIGHT || k == VK_DOWN || k == VK_S)
          ? k : VK_JUMP] = keyEvent.getID() != 402;
    }
  }

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame("Super Mario Land 4K");
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    a applet = new a();
    applet.setPreferredSize(new java.awt.Dimension(640, 576));
    frame.add(applet, java.awt.BorderLayout.CENTER);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    Thread.sleep(250);
    applet.start();
  }*/
}
