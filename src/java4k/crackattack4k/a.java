/*
 * Crack Attack 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Crack Attack 4K.
 *
 * Crack Attack 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crack Attack 4K is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package java4k.crackattack4k;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Event;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class a extends Applet implements Runnable {

  // keys
  private int[] a = new int[4];

  @Override
  public void start() {
    new Thread(this).start();
  }

  public void run() {

    final float SPEED_UP_RATE = 0.00016f;

    final int Z0 = -256;
    final int Z1 = 0;
    final int DZ = 4;

    final int FALL_DELAY = 16;
    final int EXPLODE_DELAY = 40;

    final int CHAIN_X = 0;
    final int CHAIN_Y = 1;
    final int CHAIN_SIZE = 2;
    final int CHAIN_WIDTH = 3;
    final int CHAIN_COUNT = 4;

    final int TILE_EMPTY = -1;
    final int TILE_RED = 0;
    final int TILE_YELLOW = 1;
    final int TILE_GREEN = 2;
    final int TILE_CYAN = 3;
    final int TILE_PURPLE = 4;
    final int TILE_HIDDEN = 5;

    final int SPRITE_FACE_1 = 30;
    final int SPRITE_FACE_2 = 31;
    final int SPRITE_FACE_3 = 32;
    final int SPRITE_FACE_4 = 33;
    final int SPRITE_FACE_5 = 34;

    final int SPRITE_DIGIT_0 = 40;
    final int SPRITE_DIGIT_1 = 41;
    final int SPRITE_DIGIT_2 = 42;
    final int SPRITE_DIGIT_3 = 43;
    final int SPRITE_DIGIT_4 = 44;
    final int SPRITE_DIGIT_5 = 45;
    final int SPRITE_DIGIT_6 = 46;
    final int SPRITE_DIGIT_7 = 47;
    final int SPRITE_DIGIT_8 = 48;
    final int SPRITE_DIGIT_9 = 49;
    final int SPRITE_HYPHEN = 50;
    final int SPRITE_CURSOR_1 = 51;
    final int SPRITE_CURSOR_2 = 52;
    final int SPRITE_CURSOR_3 = 53;

    final int SPRITE_UPSIDE_DOWN_DIGIT_0 = 60;
    final int SPRITE_CURSOR_4 = 71;
    final int SPRITE_CURSOR_5 = 72;
    final int SPRITE_CURSOR_6 = 73;

    final int MOUSE_X = 0;
    final int MOUSE_Y = 1;
    final int MOUSE_DOWN = 2;
    final int MOUSE_BUTTON = 3;

    final int FADE_NONE = 0;
    final int FADE_OUT = 1;
    final int FADE_IN = 2;

    final int OVER_NONE = 0;
    final int OVER_UP = 1;
    final int OVER_DOWN = 2;
    final int OVER_START = 3;

    final String S = "\u0000\u3000\ufc78\ufcfc\uf0f8\uc0e0\u0080\u0000\u8000\u8080\uc0c0\uf8fc\ue0f0\u70e0\u1030\u0000\u0000\ue000\uf8f0\uf8f8\uf8f8\uf0f8\u00e0\u0000\u0000\u8000\uc080\ue0c0\uf0e0\uf8f0\u00f8\u0000\u0000\u8000\ue0c0\uf8f0\uf0f8\uc0e0\u0080\u0000\u0555\u5554\u16aa\uaab5\u1955\u556d\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1555\u5559\u1955\u5555\u0555\u5554\u0000\u0000\u0000\u0000\u0aa8\u2aaa\uafea\ubf3a\ubcfa\ub3f8\uafc0\uaa00\u0800\u4800\u4800\u2000\u8000\u0000\u8000\ua000\ua000\ua800\uafc0\ubffa\ubcfa\ubff8\uafe0\u2a80\u4a80\u4080\u20a0\u8020\u0028\b\u0000\uaa80\uaaa0\uaaa8\uafe8\ubff8\ubcf8\ubff8\uafe8\uaaa8\u0aa8\u48a8\u4828\u4800\ua000\u0000\u8000\u8000\ua000\ua000\ua800\uafc0\ubff0\ubcf0\ubff0\u8fc0\u22a0\u48a0\u4828\u0808\ua008\u0000\u8000\ua000\ua800\uaa00\uafc0\ubff0\ubcf8\ubffa\uafea\u0aa8\u4aa0\u4a80\u4a00\u2800\ua000\u8000\u0550\u1aa4\u6aa9\u6559\u6559\u6aa9\u1aa4\u0550\u0000\u1010\u6564\u6aa9\u6aa9\u6554\u1000\u0000\u1010\u6464\u6969\u6a59\u6699\u65a9\u6464\u1010\u0400\u1904\u6959\u6599\u65a9\u6aa9\u1a59\u0504\u0500\u1a40\u1a90\u1964\u6aa9\u6aa9\u1954\u0400\u5555\u65a9\u65a9\u6599\u6599\u6a99\u1a55\u0500\u0540\u1a90\u6aa4\u65a9\u6599\u6a99\u1a44\u0500\u0055\u0069\u5569\u6a59\u6a99\u55a9\u0069\u0015\u0510\u1a64\u6aa9\u6599\u6599\u6aa9\u1a64\u0510\u0050\u11a4\u66a9\u6659\u6a59\u1aa9\u06a4\u0150\u0000\u0100\u0640\u0640\u0640\u0640\u0100\u0000\u1550\u6aa4\u6aa9\u55a9\u0069\u0069\u0069\u0054\u0054\u0069\u0069\u0069\u55a9\u6aa9\u6aa4\u1550\u0054\u0069\u0069\u0069\u15a9\u6aa5\u6aa9\u15a9\u0069\u0069\u0069\u0054";

    final Color borderColor1 = new Color(0xE08800);
    final Color borderColor2 = new Color(0x703000);
    final Color borderColor3 = new Color(0x202020);

    int i;
    int j;
    int k;
    int x;
    int y;
    int z;

    int counter = 0;

    int cameraY = 0;
    int cameraY2 = 0;

    int tileX = 0;
    int tileY = 0;

    int cursorX = 0;
    int cursorY = 0;

    int swapTileLeft = 0;
    int swapTileRight = 0;
    int swapX = 0;
    int swapY = 0;
    int swapOffset = 0;
    int bonusTime = 0;
    int cameraYTarget = 0;
    int dangerBounce = 0;

    int limitTileY = 0;
    int limitLine = 0;

    int explodeID = 0;
    
    int dying = 0;
    int winning = 0;
    int level = 0;

    int countdown = 0;
    int countdownZ = 0;
    int fadeIntensity = 255;
    int fadeState = FADE_IN;

    int score = 0;
    int lastScore = 0;
    int lastLevel = 0;

    float timer = 0;
    float initialSpeed = 0;

    boolean cursorExpanded = false;
    boolean mouseReleased = true;
    boolean swapping = false;
    boolean drawFlash = false;
    boolean cameraBlocked = false;
    boolean resetLevel = false;

    boolean menuMode = true;
    int overButton = OVER_NONE;

    Random random = new Random();
    AffineTransform affineTransform = new AffineTransform();

    boolean[] dangerColumns = null;
    int[][] tiles = null;
    int[][] fallingTiles = null;
    int[][] bouncingTiles = null;
    int[][] explodingTiles = null;
    int[][] explodeIDs = null;
    int[] explodeCounts = null;
    int[] pixels = new int[256];
    ArrayList<int[]> explosions = new ArrayList<int[]>();
    ArrayList<int[]> chains = new ArrayList<int[]>();
    BufferedImage[] sprites = new BufferedImage[128];

    BufferedImage image = new BufferedImage(104, 200, 1);
    BufferedImage backgroundImage = new BufferedImage(104, 200, 1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    Graphics2D g2 = null;

    // decompress symbols, faces and tiles
    for(k = 0; k < 3; k++) {
      float saturation = k == 2 ? 0 : 1;
      float brightness = k == 1 ? 0.5f : 1;
      for(i = 0; i < 5; i++) {
        sprites[10 * k + i] = new BufferedImage(16, 16, 2);
        sprites[10 * k + i + 5] = new BufferedImage(16, 16, 2);
        sprites[SPRITE_FACE_1 + i] = new BufferedImage(16, 16, 2);
        for(y = 0; y < 16; y++) {

          float hue = (i < 4 ? i : -1) / 6f;

          // symbol
          for(x = 0; x < 8; x++) {
            pixels[x] = pixels[15 - x]
                = ((S.charAt((i << 3) + (y >> 1))
                    >> (((y & 1) << 3) + x)) & 1) 
                        * Color.HSBtoRGB(hue, saturation, brightness);
          }
          sprites[10 * k + i].setRGB(0, y, 16, 1, pixels, 0, 16);

          // faces
          for(x = 0; x < 8; x++) {
            j = (S.charAt(72 + (i << 4) + y) >> (x << 1)) & 3;
            pixels[x] = pixels[15 - x] = j == 3 ? 0xFFD0D0D0
                : j == 1 ? i == 0 ? 0xFFC0C000 : i == 4 ? 0xFF0038F8
                    : 0xFFF81800
                    : Color.HSBtoRGB(hue, 1, j == 0 ? 0.4f : 1);
          }
          sprites[SPRITE_FACE_1 + i].setRGB(0, y, 16, 1, pixels, 0, 16);

          // tile
          for(x = 0; x < 16; x++) {
            j = (S.charAt((x < 8 ? 41 : 40) + (y << 1)) >> ((x & 7) << 1)) & 3;
            if (k == 2) {
              j++;
            }
            pixels[x] = Color.HSBtoRGB(hue, saturation,
                brightness * (j == 0 ? 0 : j == 1 ? 0.4f : j == 2 ? 0.75f : 1));
          }
          sprites[10 * k + i + 5].setRGB(0, y, 16, 1, pixels, 0, 16);
        }
      }
    }

    // decompress digits, hypthen and cursor pieces
    for(i = 0; i < 14; i++) {
      j = i == 13 ? 12 : 8;
      sprites[SPRITE_DIGIT_0 + i] = new BufferedImage(j, 8, 2);
      sprites[SPRITE_UPSIDE_DOWN_DIGIT_0 + i] = new BufferedImage(j, 8, 2);
      for(x = 0; x < j; x++) {
        for(y = 0; y < 8; y++) {
          k = (S.charAt(152 + x + (i << 3)) >> (y << 1)) & 3;
          pixels[15 - y] = pixels[y]
              = k == 0 ? 0 : k == 1 ? 0xFF000000 : 0xFFFFFFFF;
        }
        sprites[SPRITE_DIGIT_0 + i].setRGB(x, 0, 1, 8, pixels, 0, 1);
        sprites[SPRITE_UPSIDE_DOWN_DIGIT_0 + i]
            .setRGB(x, 0, 1, 8, pixels, 8, 1);
      }
    }

    long nextFrameStartTime = System.nanoTime();
    while(true) {

      do {
        nextFrameStartTime += 16666667;

        // -- update starts ----------------------------------------------------

        if (resetLevel) {

          // reset level
          resetLevel = false;
          swapping = false;
          cameraBlocked = false;
          cameraY = 0;
          winning = 0;
          dying = 0;
          swapOffset = 0;
          bonusTime = 0;
          cameraYTarget = 0;
          dangerBounce = 0;
          explodeID = 0;
          countdown = 3;
          countdownZ = Z0;
          limitTileY = 18 + (level >> 1);
          initialSpeed = 0.01f + 0.0021f * level;
          overButton = OVER_NONE;

          dangerColumns = new boolean[6];
          tiles = new int[128][6];
          fallingTiles = new int[128][6];
          bouncingTiles = new int[128][6];
          explodingTiles = new int[128][6];
          explodeIDs = new int[128][6];
          explodeCounts = new int[4096];

          chains.clear();

          // create random tiles
          for(y = 0; y < 128; y++) {
            for(x = 0; x < 6; x++) {
              if (y < 2) {
                tiles[y][x] = TILE_EMPTY;
              } else if (x < 2) {
                do {
                  tiles[y][x] = random.nextInt(5);
                } while(tiles[y][x] == tiles[y - 1][x]
                    && tiles[y][x] == tiles[y - 2][x]);
              } else {
                do {
                  tiles[y][x] = random.nextInt(5);
                } while((tiles[y][x] == tiles[y - 1][x]
                        && tiles[y][x] == tiles[y - 2][x])
                    || (tiles[y][x] == tiles[y][x - 1]
                        && tiles[y][x] == tiles[y][x - 2]));
              }
            }
          }
          for(x = 0; x < 6; x++) {            
            for(y = 6 - (level % 5) + random.nextInt(3); y >= 0; y--) {
              tiles[y][x] = TILE_EMPTY;
            }
          }

          // create background image
          for(y = 0; y < 200; y++) {
            for(x = 0; x < 104; x++) {
              pixels[x] = Color.HSBtoRGB(
                  (0.0015f + 0.0005f * (level / 5)) * y - 0.15f
                      + 0.7f * (level / 5), 1, 0.5f);
            }
            backgroundImage.setRGB(0, y, 104, 1, pixels, 0, 104);
          }

          nextFrameStartTime = System.nanoTime();
        }

        if (fadeState == FADE_IN) {
          fadeIntensity -= 8;
          if (fadeIntensity <= 0) {
            fadeState = FADE_NONE;
          }
          continue;
        }

        if (menuMode) {

          if (fadeState == FADE_OUT) {
            fadeIntensity += 8;
            if (fadeIntensity >= 255) {
              fadeIntensity = 255;
              fadeState = FADE_IN;
              menuMode = false;              
              resetLevel = true;
              score = level >= lastLevel ? lastScore : 0;
            }
          } else if (fadeState == FADE_NONE) {
            x = a[MOUSE_X] / 3;
            y = a[MOUSE_Y] / 3;
            overButton = OVER_NONE;
            if (x >= 44 && x < 60) {
              if (y >= 62 && y < 78) {
                overButton = OVER_UP;
              }
              if (y >= 90 && y < 106) {
                overButton = OVER_DOWN;
              }
            }
            if (x >= 36 && x < 68 && y >= 150 && y < 166) {
              overButton = OVER_START;
            }

            if (a[MOUSE_DOWN] > 0) {
              if (mouseReleased) {
                mouseReleased = false;
                if (overButton == OVER_DOWN && level > 0) {
                  level--;
                } else if (overButton == OVER_UP && level < 44) {
                  level++;
                } else if (overButton == OVER_START) {
                  fadeState = FADE_OUT;
                  fadeIntensity = 0;
                }
              }
            } else {
              mouseReleased = true;
            }
          }

          continue;
        }

        if (winning > 0) {
          winning++;
          int[] explosion = new int[3];          
          if (winning < 256) {
            explosion[0] = random.nextInt(104);
            explosion[1] = random.nextInt(200);
            explosion[2] = -16;
            explosions.add(explosion);
          } else if (explosions.size() == 0) {
            fadeState = FADE_IN;
            fadeIntensity = 255;
            lastScore = score;
            lastLevel = level;
            if (level == 44) {
              // return to menu after level 9-5
              menuMode = true;
            } else {
              level++;
              resetLevel = true;
            }
          }
          for(i = explosions.size() - 1; i >= 0; i--) {
            explosion = explosions.get(i);
            if (explosion[2] < 0) {
              explosion[2]++;
            } else {
              explosions.remove(i);
            }
          }          
          continue;
        }

        if (dying > 0) {
          cameraY = cameraY2 + (int)((3 - 3f * dying / 128)
              * Math.sin(1.5f * dying));
          if (++dying == 128) {
            fadeIntensity = 255;
            fadeState = FADE_IN;
            menuMode = true;            
          }
          continue;
        }

        cameraBlocked = false;

        counter++;
        if ((counter & 15) == 0) {
          cursorExpanded = !cursorExpanded;
        }
        if ((counter & 1) == 0) {
          if (++limitLine == 6) {
            limitLine = 0;
          }
        }

        dangerBounce = ((counter << 1) & 31) / 3;
        dangerBounce = dangerBounce >= 7 ? 4 : dangerBounce < 4
            ? dangerBounce : 6 - dangerBounce;

        // position cursor
        x = (a[MOUSE_X] - 12) / 3;
        y = (a[MOUSE_Y] - 12) / 3;

        tileX = x < 0 ? 0 : x > 79 ? 4 : (x >> 4);
        tileY = (y + cameraY) >> 4;

        while((tileY << 4) < cameraY) {
          tileY++;
        }
        while((tileY << 4) > cameraY + 176) {
          tileY--;
        }

        cursorX = 2 + (tileX << 4);
        cursorY = 2 + (tileY << 4) - cameraY;

        if (countdown >= 0) {
          countdownZ += DZ;
          if (countdownZ >= Z1) {
            countdown--;
            countdownZ = Z0;
          }
          continue;
        }

        if (a[MOUSE_DOWN] > 0) {
          if (mouseReleased) {
            mouseReleased = false;
            if (a[MOUSE_BUTTON] == 1) {
              if (!swapping
                    && fallingTiles[tileY][tileX] == 0
                    && fallingTiles[tileY][tileX + 1] == 0
                    && explodingTiles[tileY][tileX] == 0
                    && explodingTiles[tileY][tileX + 1] == 0
                    && (tiles[tileY][tileX] >= 0
                        || tiles[tileY][tileX + 1] >= 0)) {
                swapping = true;
                swapOffset = 0;
                swapX = tileX;
                swapY = tileY;
                swapTileLeft = tiles[swapY][swapX];
                swapTileRight = tiles[swapY][swapX + 1];
                tiles[swapY][swapX] = TILE_HIDDEN;
                tiles[swapY][swapX + 1] = TILE_HIDDEN;
                bouncingTiles[swapY][swapX] = 0;
                bouncingTiles[swapY][swapX + 1] = 0;
              }
            } else {
              score++;
              cameraYTarget = (cameraY & ~15) + 16;
              bonusTime = 0;
            }
          } 
        } else {
          mouseReleased = true;
        }

        if (swapping) {
          // update swap
          swapOffset += 4;
          if (swapOffset == 16) {
            swapping = false;
            tiles[swapY][swapX + 1] = swapTileLeft;
            tiles[swapY][swapX] = swapTileRight;
            if (tiles[swapY + 1][swapX] == TILE_EMPTY) {
              fallingTiles[swapY][swapX] = FALL_DELAY;
            }
            if (tiles[swapY + 1][swapX + 1] == TILE_EMPTY) {
              fallingTiles[swapY][swapX + 1] = FALL_DELAY;
            }
            if (tiles[swapY][swapX] == TILE_EMPTY
                && tiles[swapY - 1][swapX] > TILE_EMPTY
                && tiles[swapY - 1][swapX] < TILE_HIDDEN) {
              fallingTiles[swapY - 1][swapX] = FALL_DELAY;
            }
            if (tiles[swapY][swapX + 1] == TILE_EMPTY
                && tiles[swapY - 1][swapX + 1] > TILE_EMPTY
                && tiles[swapY - 1][swapX + 1] < TILE_HIDDEN) {
              fallingTiles[swapY - 1][swapX + 1] = FALL_DELAY;
            }
          }
        }

        // scan for holes, danger columns, losing state and winning state
        // update falling, bouncing and exploding tiles
        // test for winning and losing
        i = 128;
        j = cameraY >> 4;
        k = -32;
        int cleared = 0;
        boolean fallingFound = false;
        for(x = 5; x >= 0; x--) {
          dangerColumns[x] = false;
          int holeFound = 0;
          for(y = 11; y >= 0; y--) {

            if (bouncingTiles[y + j][x] > 0) {
              bouncingTiles[y + j][x]--;
            }

            if (explodingTiles[y + j][x] > 0) {
              cameraBlocked = true;
              if (--explodingTiles[y + j][x] == 0) {
                cleared++;
                explodingTiles[y + j][x] = k;
                explodeCounts[explodeIDs[y + j][x] = explodeID]++;
                k -= 8;
              }
            } else if (explodingTiles[y + j][x] < 0) {
              cameraBlocked = true;
              if (++explodingTiles[y + j][x] == 0) {
                score += 10;
                tiles[y + j][x] = TILE_EMPTY;
                explodeCounts[explodeIDs[y + j][x]]--;
              }
            } else if (tiles[y + j][x] == TILE_EMPTY) {
              holeFound = explodeCounts[explodeIDs[y + j][x]] == 0 ? 1 : 0;
            } else if (tiles[y + j][x] != TILE_HIDDEN) {
              if (y + j < i) {
                i = y + j;
              }
              if (y < 2) {
                if (y == 0) {
                  // player lost
                  dying = 1;
                  cameraY2 = cameraY;
                  continue;
                }
                dangerColumns[x] = true;
              }
              if (holeFound > 0 && fallingTiles[y + j][x] == 0) {
                fallingTiles[y + j][x] = holeFound;
                fallingFound = true;
              } else if (fallingTiles[y + j][x] > 0) {
                holeFound = --fallingTiles[y + j][x];
                if (holeFound == 0) {
                  fallingFound = true;
                  if (tiles[y + j + 1][x] == TILE_EMPTY) {
                    fallingTiles[y + j + 1][x] = 1;
                    tiles[y + j + 1][x] = tiles[y + j][x];
                    tiles[y + j][x] = TILE_EMPTY;
                  } else {
                    bouncingTiles[y + j][x] = 6;
                  }
                }
              }
            }
          }
        }
        if (cleared > 0) {
          explodeID++;
        }
        if (i >= limitTileY) {
          bonusTime = 0;
          if (!(cameraBlocked || fallingFound || swapping)) {
            // player won
            winning = 1;
            score += 100;
            continue;
          }
        }

        // scan for horizontal combos
        cleared = 0;
        int clearedX = 0;
        int clearedY = 0;
        for(y = 11; y >= 0; y--) {
          i = 0;
          for(x = 4; x >= 0; x--) {
            if (tiles[y + j][x] == tiles[y + j][x + 1]
                && tiles[y + j][x] > TILE_EMPTY
                && tiles[y + j][x] < TILE_HIDDEN
                && explodingTiles[y + j][x] == 0
                && explodingTiles[y + j][x + 1] == 0
                && fallingTiles[y + j][x] == 0
                && fallingTiles[y + j][x + 1] == 0) {
              i++;
            } else {
              if (i >= 2) {                
                i++;
                if (cleared == 0) {
                  clearedX = x + 1;
                  clearedY = y + j;
                }
                cleared += i;
                while(i > 0) {
                  explodingTiles[y + j][x + i] = EXPLODE_DELAY;
                  fallingTiles[y + j][x + i] = 0;
                  bouncingTiles[y + j][x + i] = 0;
                  i--;
                }
              }
              i = 0;
            }
          }
          if (i >= 2) {
            i++;
            if (cleared == 0) {
              clearedX = x + 1;
              clearedY = y + j;
            }
            cleared += i;
            while(i > 0) {
              explodingTiles[y + j][x + i] = EXPLODE_DELAY;
              fallingTiles[y + j][x + i] = 0;
              bouncingTiles[y + j][x + i] = 0;
              i--;
            }
          }
        }

        // scan for vertical combos
        for(x = 5; x >= 0; x--) {
          i = 0;
          for(y = 10; y >= 0; y--) {
            if (tiles[y + j][x] == tiles[y + j + 1][x]
                && tiles[y + j][x] > TILE_EMPTY
                && tiles[y + j][x] < TILE_HIDDEN
                && (explodingTiles[y + j][x] == 0
                    || explodingTiles[y + j][x] == EXPLODE_DELAY)
                && (explodingTiles[y + j + 1][x] == 0
                    || explodingTiles[y + j + 1][x] == EXPLODE_DELAY)
                && fallingTiles[y + j][x] == 0
                && fallingTiles[y + j + 1][x] == 0) {
              i++;
            } else {
              if (i >= 2) {
                i++;
                if (cleared == 0) {
                  clearedX = x;
                  clearedY = y + j;
                }
                while(i > 0) {
                  if (explodingTiles[y + j + i][x] == 0) {
                    cleared++;
                  }
                  explodingTiles[y + j + i][x] = EXPLODE_DELAY;
                  fallingTiles[y + j + i][x] = 0;
                  bouncingTiles[y + j + i][x] = 0;
                  i--;
                }
              }
              i = 0;
            }
          }
          if (i >= 2) {
            i++;
            if (cleared == 0) {
              clearedX = x;
              clearedY = y + j;
            }
            while(i > 0) {
              if (explodingTiles[y + j + i][x] == 0) {
                cleared++;
              }
              explodingTiles[y + j + i][x] = EXPLODE_DELAY;
              fallingTiles[y + j + i][x] = 0;
              bouncingTiles[y + j + i][x] = 0;
              i--;
            }
          }
        }

        if (cleared > 3) {
          
          bonusTime += cleared << 5;

          score += 10 * (cleared - 2);

          // add chain
          int[] chain = new int[5];
          chain[CHAIN_Y] = (clearedY << 4) - cameraY + 8;
          chain[CHAIN_WIDTH] = cleared > 9 ? 24 : 16;
          chain[CHAIN_X] = (clearedX << 4) + (cleared > 9 ? 0 : 4);
          chain[CHAIN_SIZE] = cleared;
          if (chain[CHAIN_X] < 4) {
            chain[CHAIN_X] = 4;
          }
          if (chain[CHAIN_Y] < 4) {
            chain[CHAIN_X] = 4;
          }
          if (chain[CHAIN_X] + chain[CHAIN_WIDTH] > 100) {
            chain[CHAIN_X] = 100 - chain[CHAIN_WIDTH];
          }
          if (chain[CHAIN_Y] > 180) {
            chain[CHAIN_Y] = 180;
          }
          chains.add(chain);
        }

        // update chains
        for(i = chains.size() - 1; i >= 0; i--) {
          int[] chain = chains.get(i);
          if (++chain[CHAIN_COUNT] == 32) {
            chains.remove(i);
          }
        }

        if (!cameraBlocked) {

          if (bonusTime > 0) {
            bonusTime--;
          } else {

            // advance camera
            if (cameraYTarget > cameraY) {
              cameraY++;
            } else {
              timer += initialSpeed + cameraY * SPEED_UP_RATE;
              if (timer >= 1) {
                timer--;
                cameraY++;
              }
            }
          }
        }

        // -- update ends ------------------------------------------------------

      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------

      if (menuMode || tiles == null) {
        // clear background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 104, 200);
      } else {
        // draw background
        g.drawImage(backgroundImage, 0, 0, null);
      }

      // draw score
      if (score == 0) {
        g.drawImage(sprites[SPRITE_DIGIT_0], 48, 5, null);
      } else {
        j = 0;
        k = score;
        while(k > 0) {
          k /= 10;
          j++;
        }
        j = ((104 - (j << 3)) >> 1) + ((j - 1) << 3);
        k = score;
        while(k > 0) {
          g.drawImage(sprites[SPRITE_DIGIT_0 + (k % 10)], j, 5, null);
          k /= 10;
          j -= 8;
        }
      }

      if (menuMode || tiles == null) {

        // draw level
        g.drawImage(sprites[SPRITE_DIGIT_1 + (level / 5)], 40, 80, null);
        g.drawImage(sprites[SPRITE_HYPHEN], 48, 80, null);
        g.drawImage(sprites[SPRITE_DIGIT_1 + (level % 5)], 56, 80, null);

        // draw buttons
        g.drawImage(sprites[overButton == OVER_UP ? 28 : 8], 44, 62, null);
        g.drawImage(sprites[overButton == OVER_DOWN ? 28 : 8], 44, 90, null);
        g.drawImage(sprites[3], 
            44 + (overButton == OVER_UP && !mouseReleased ? 1 : 0),
            62 + (overButton == OVER_UP && !mouseReleased ? 1 : 0), null);
        g.drawImage(sprites[3], 
            44 + (overButton == OVER_DOWN && !mouseReleased ? 1 : 0),
            107 + (overButton == OVER_DOWN && !mouseReleased ? 1 : 0),
            16, -16, null);

        g.drawImage(sprites[overButton == OVER_START ? 27 : 7], 
            36, 150, 32, 16, null);
        g.drawImage(sprites[2], 
            36 + (overButton == OVER_START && !mouseReleased ? 1 : 0),
            150 + (overButton == OVER_START && !mouseReleased ? 1 : 0),
            32, 16, null);

      } else {

        // draw tiles
        drawFlash = !drawFlash;
        i = cameraY & 15;
        j = cameraY >> 4;
        for(y = 0; y < 13; y++) {
          for(x = 0; x < 6; x++) {
            k = tiles[y + j][x];
            if (k > TILE_EMPTY && k < TILE_HIDDEN) {
              z = k;
              if (dying > 0 || winning > 0) {
                z += SPRITE_FACE_1;
              } else {
                int e = explodingTiles[y + j][x];
                if (e != 0) {
                  if (e < 0) {
                    if (e >= -16) {
                      continue;
                    }
                    z += SPRITE_FACE_1;
                  }
                  if (drawFlash) {
                    k += 20;
                  }
                } else if (y == 12) {
                  k += 10;
                  z += 10;
                }
              }
              g.drawImage(sprites[k + 5],
                  4 + (x << 4), 4 + (y << 4) - i, null);

              int b = (dying > 0 || winning > 0) ? 0
                  : dangerColumns[x] ? dangerBounce : bouncingTiles[y + j][x];
              if (b >= 4) {
                g.drawImage(sprites[z],
                    2 + (x << 4),
                    1 + (y << 4) - i + 8, 20, 10, null);
              } else {
                g.drawImage(sprites[z],
                    4 + (x << 4),
                    4 + (y << 4) - i - b, null);
              }
            }
          }
        }

        if (dying == 0 && winning == 0) {
          // draw limit line
          g.setColor(Color.WHITE);
          g.fillRect(4 + (limitLine << 4),
              (limitTileY << 4) - cameraY + 4, 16, 1);
        }

        if (swapping) {
          // draw tiles being swapped

          if (swapTileLeft >= 0) {
            g.drawImage(sprites[swapTileLeft + 5],
                4 + (swapX << 4) + swapOffset,
                4 + (swapY << 4) - cameraY, null);
            g.drawImage(sprites[swapTileLeft],
                4 + (swapX << 4) + swapOffset,
                4 + (swapY << 4) - cameraY, null);
          }

          if (swapTileRight >= 0) {
            g.drawImage(sprites[swapTileRight + 5],
                20 + (swapX << 4) - swapOffset,
                4 + (swapY << 4) - cameraY, null);
            g.drawImage(sprites[swapTileRight],
                20 + (swapX << 4) - swapOffset,
                4 + (swapY << 4) - cameraY, null);
          }
        }

        // draw border
        g.setColor(borderColor1);
        g.drawRect(0, 0, 103, 199);
        g.setColor(borderColor2);
        g.drawRect(1, 1, 101, 197);
        g.drawRect(2, 2, 99, 195);
        g.setColor(borderColor3);
        g.drawRect(3, 3, 97, 193);

        if (countdown >= 0) {

          // draw level
          g.drawImage(sprites[SPRITE_DIGIT_1 + (level / 5)], 40, 80, null);
          g.drawImage(sprites[SPRITE_HYPHEN], 48, 80, null);
          g.drawImage(sprites[SPRITE_DIGIT_1 + (level % 5)], 56, 80, null);

          if (countdownZ > Z0) {
            // draw countdown
            g.translate(52, 100);
            float scale = Z0 / (float)(Z0 - countdownZ);
            g.scale(scale, scale);
            g.drawImage(sprites[SPRITE_DIGIT_0 + countdown], -4, -4, null);
            g.setTransform(affineTransform);
          }
        }

        if (dying > 0 || winning > 0) {
          // draw fade out
          i = dying + winning;
          g.setColor(new Color(0, 0, 0, (i >= 128) ? 255 : (i << 1)));
          g.fillRect(0, 0, 104, 200);
        }

        if (dying == 0) {
          // draw explosions
          i = cameraY & 15;
          j = cameraY >> 4;
          for(y = 0; y < 13; y++) {
            for(x = 0; x < 6; x++) {
              k = tiles[y + j][x];
              if (k > TILE_EMPTY && k < TILE_HIDDEN) {
                int e = explodingTiles[y + j][x];
                if (e < 0 && e >= -16) {
                  int f = 24 + e;
                  float scale = -.03125f * e;
                  for(int h = 0; h < 4; h++) {
                    g.translate(12 + (x << 4) - f * (h > 1 ? 1 : -1),
                        12 + (y << 4) - i - f * ((h & 1) == 1 ? 1 : -1));
                    g.rotate(e);
                    g.scale(scale, scale);
                    g.drawImage(sprites[k + 25], -8, -8, 16, 16, null);
                    g.setTransform(affineTransform);
                  }
                }
              }
            }
          }
          for(i = explosions.size() - 1; i >= 0; i--) {
            int[] explosion = explosions.get(i);
            int e = explosion[2];
            int f = 24 + e;
            float scale = -.03125f * e;
            for(int h = 0; h < 4; h++) {
              g.translate(explosion[0] - f * (h > 1 ? 1 : -1),
                  explosion[1] - i - f * ((h & 1) == 1 ? 1 : -1));
              g.rotate(e);
              g.scale(scale, scale);
              g.drawImage(sprites[25], -8, -8, 16, 16, null);
              g.setTransform(affineTransform);
            }
          }

          if (winning == 0 && countdown < 0) {
            // draw cursor
            y = cursorExpanded ? 1 : 0;
            g.drawImage(sprites[SPRITE_CURSOR_1], cursorX - y, cursorY - y, null);
            g.drawImage(sprites[SPRITE_CURSOR_4], cursorX - y,
                cursorY + 12 + y, null);
            g.drawImage(sprites[SPRITE_CURSOR_3], cursorX + 12, cursorY - y, null);
            g.drawImage(sprites[SPRITE_CURSOR_6], cursorX + 12,
                cursorY + 12 + y, null);
            g.drawImage(sprites[SPRITE_CURSOR_2], cursorX + 28 + y,
                cursorY - y, null);
            g.drawImage(sprites[SPRITE_CURSOR_5], cursorX + 28 + y,
                cursorY + 12 + y, null);

            // draw chain messages
            for(i = chains.size() - 1; i >= 0; i--) {
              int[] chain = chains.get(i);

              for(j = 0; j < 6; j++) {
                float angle = 1.05f * j + 0.1f * chain[CHAIN_COUNT];
                g.translate(chain[CHAIN_X] + (chain[CHAIN_WIDTH] >> 1)
                        + (int)(2 * (32 - chain[CHAIN_COUNT])
                            * Math.cos(angle)),
                    chain[CHAIN_Y] + 8
                        + (int)(2 * (32 - chain[CHAIN_COUNT])
                            * Math.sin(angle)));
                g.rotate(chain[CHAIN_COUNT]);
                float scale = 0.25f + chain[CHAIN_COUNT] / 128f;
                g.scale(scale, scale);
                g.drawImage(sprites[25], -8, -8, 16, 16, null);
                g.setTransform(affineTransform);
              }

              g.setColor(Color.RED);
              g.fillRect(chain[CHAIN_X], chain[CHAIN_Y],
                  chain[CHAIN_WIDTH], 16);
              g.setColor(Color.WHITE);
              g.drawRect(chain[CHAIN_X], chain[CHAIN_Y],
                  chain[CHAIN_WIDTH] - 1, 15);
              if (chain[CHAIN_SIZE] > 9) {
                g.drawImage(sprites[SPRITE_DIGIT_0 + chain[CHAIN_SIZE] / 10],
                    chain[CHAIN_X] + 4, chain[CHAIN_Y] + 4, null);
                g.drawImage(sprites[SPRITE_DIGIT_0 + chain[CHAIN_SIZE] % 10],
                    chain[CHAIN_X] + 12, chain[CHAIN_Y] + 4, null);
              } else {
                g.drawImage(sprites[SPRITE_DIGIT_0 + chain[CHAIN_SIZE]],
                    chain[CHAIN_X] + 4, chain[CHAIN_Y] + 4, null);
              }
            }
          }
        }
      }

      if (fadeState != FADE_NONE) {        
        // draw fade
        g.setColor(new Color(0, 0, 0, fadeIntensity));
        g.fillRect(0, 0, 104, 200);
      }

      // -- render ends --------------------------------------------------------

      // show the hidden buffer
      if (g2 == null) {
        g2 = (Graphics2D)getGraphics();
        requestFocus();
      } else {
        g2.drawImage(image, 0, 0, 312, 600, null);
      }

      // burn off extra cycles
      while(nextFrameStartTime - System.nanoTime() > 0) {
        Thread.yield();
      }
    }
  }

  @Override
  public boolean handleEvent(Event e) {

    final int MOUSE_X = 0;
    final int MOUSE_Y = 1;
    final int MOUSE_DOWN = 2;
    final int MOUSE_BUTTON = 3;

    a[MOUSE_X] = e.x;
    a[MOUSE_Y] = e.y;
    if (e.id == 501) {
      a[MOUSE_DOWN] = 1;
      a[MOUSE_BUTTON] = e.modifiers == 0 ? 1 : 0;
    } else {
      a[MOUSE_DOWN] = 0;
    }

    return false;
  }

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame("Crack Attack 4K");
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    a applet = new a();
    applet.setPreferredSize(new java.awt.Dimension(312, 600));
    frame.add(applet, java.awt.BorderLayout.CENTER);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    Thread.sleep(250);
    applet.start();
  }*/
}
