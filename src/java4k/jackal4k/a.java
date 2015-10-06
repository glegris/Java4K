package java4k.jackal4k;
/*
 * Jackal 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Jackal 4K.
 *
 * Jackal 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jackal 4K is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.applet.Applet;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
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

  // map:
  //  0 - space
  //  1 - rock
  //  2 - gun
  //  3 - tank
  //  4 - hidden gun

  // queue:
  //  0 - remove flag
  //  1 - type:
  //        0 = player
  //        1 = player bullet
  //        2 = enemy bullet
  //        3 = man
  //        4 = gun
  //        5 = tank
  //        6 = explosion
  //        7 = boss
  //        8 = dead man
  //        9 = rolling rock
  //       10 = hidden gun
  //       11 = mine
  //  2 - sprite index
  //  3 - x
  //  4 - y
  //  5 - rotates
  //  6 - center x
  //  7 - center y
  //  8 - angle
  //  9 - target angle
  // 10 - scale
  // 11 - shoot delay
  // 12 - hits remaining

  // 13 - state
  //    0 - seeking
  //    1 - paused
  // 14 - vx
  // 15 - vy

  // bullet
  //  8 - vx
  //  9 - vy
  // 10 - time out

  // explosion
  // 11 - size

  public void run() {
    
    int[][][] stages = new int[6][80][16];
    ArrayList<float[]> queue = new ArrayList<float[]>();    
    int x = 0;
    int y = 0;
    int value = 0;
    int i = 0;
    int j = 0;
    int cameraY = 0;
    int spawnY = 0;
    int stageIndex = 0;
    int[][] stage = null;
    Random random = new Random();
    int playerDead = 1;
    float[] player = null;
    boolean fireworks = false;
    int bossSpawn = 0;
    int color0 = 0;
    int color1 = 0;

    // -- decompress the sprites starts ----------------------------------------
    String s = "\u10f0\u262a\uc8c4\u3214\uc8a1\u152a\u2529\u5116\ua8d1\u262a\uc8c4\u3214\uc8a1\u152a\ua9a5\u1651\u5128\u1088\u0240\u0fb8\u0f7c\u38ff\u788f\u788e\u709e\u715e\u6e2f\uadb7\u4c73\ufe9e\u3f0c\u1f5e\u0ffc\u0f38\u106d\uff0c\ufffe\u3802\u2002\u2001\u2039\u2001\ue781\uff41\u9041\u0c41\u037f\u017f\u0141\u017f\u8201\u1810\ufffc\uc36f\u07c3\u7b7a\u7a7a\uf646\ufefe\u0ef2\u0bfb\u0a0b\u0a0b\u00f4\u18b6\uffee\u6f23\u3fa3\u2727\u233f\ue323\u6763\u7c7e\u4040\u4040\u4040\uc040\u14a0\uffde\ue50d\u1d35\u9d9d\u1d1d\u65f5\u4545\u794d\u11f1\u1ff3\u0eb4\ub468\u0284\u0202\uce02\u6428\u1c4c\uf01a\u06ff\u2418\u4242\u1824\u1053\u271b\u273f\u837f\ubea3\u5a42\u3a5a\u3b1a\u007e\u1053\u4e36\u4e7e\u86f6\u7cc6\ub484\ub8b4\ubebc\u007c\u10e0\u0604\u1903\u2925\ue67c\ubbae\u7979\u8cd8\u44c8\u08b4\u423c\ua581\u8199\u7cc2\u106d\u18e0\u0204\u0102\u0101\u0101\u0201\u0402\ue018\u106d\u18e0\ue204\udf3a\u172f\u1713\u02ff\u0402\ue018\u106d\u38e0\u2edc\u1316\u2515\ua325\uaee7\ufc7e\ue0f8\u5555\u4001\u4001\u4041\u4401\u4001\u4101\u4001\u5415\u5695\u0000\u0000\u000c\u0000\u0000\u4155\u0000\u0800\u0000\u5030\u5400\u5500\u5540\u5550\u0000\u00c0\u0555\u0955\u0015\u0c05\u0000\u0000\u5541\u0000\u0308\u0000\u0550\u0000\u0000\u0000\u5555\u4821\u4001\u0000\u0280\u0000\u0000\u5415\u0800\u0000\u0080\u1400\u0008\u3000\u0014\u0000\u0000\u0040\u0000\u0000\u0200\u0550\u0000\u0000\u4155\u0c15\u0015\u0015\u5355\u0000\u2000\u0000\u5550\u400c\u4800\u4005\u4141\u4001\u4001\u5005\u5555\u0001\u0001\u5005\u5005\u5005\u8005\u00c1\u2000\u0008\u1050\u1040\u0140\u0580\u0000\u0000\u5451\u5451\u5451\u545d\u5751\u5451\u5451\u5451\u0000\u0000\u1554\u0200\u4000\u4000\u0005\u0005\u0c31\u4000\u400c\u0000\u0155\u4000\u0000\u0000\u5555\u4551\u4141\u4001\u4001\u4821\u4001\u4281\u5031\u5401\u5401\u5401\u5305\u4015\u0055\ua555\u0000\u3000\u0000\u0000\u5559\u0000\u0140\u0550\u1554\u0550\u2148\u0000\u3000\u0000\u0040\u1750\u1454\u044c\u0000\u1050\u1414\u0000\u4001\u5005\u5555\u9966\u0000\u0000\u0000\u0000\u0000\u0000\u0000\ua955\u0155\u0c00\u0000\u0140\u5825\u5005\u3000\u0000\u1445\u1445\u1445\u0000\u6559\u0000\u0000\u0010\u1400\u2400\u0510\u001c\u00d0\u001c\u0015\u5155\u0030\u5155\u5155\u4001\u4001\u5555\u0000\u0000\uaaaa\u0000\u0000\u0000\u0000\u0000\u300c\u0140\u0550\u1554\u0000\u5415\u5825\u5415\u5415\u4001\u4001\u4002\u4001\u4002\u4001\u4001\u0000\u4551\u0300\u0000\u155c\u1990\u0000\u0000\u0555\u3002\u0002\u0002\u5415\u4001\u4001\u4001";
    int[] pixels = new int[1024];
    BufferedImage[] sprites = new BufferedImage[21];
    for(i = 0; i < 21; i++) {
      int width = (i == 2) ? 32 : (i < 7 || i > 12 ? 16 : 8);
      color1 = i == 0  ? 0xFF994E00             
             : i == 16 ? 0xFFADADAD
             : i == 17 ? 0xFFABABAB
             : i == 18 ? 0xFF008B00
             : i == 19 ? 0xFF626262
             : i == 20 ? 0xFF000000
             : 0xFF000000;
      if (i == 12) {
        j -= 5;
      } else if (i > 15) {
        j = 0;
        width = 16;
      }
      value = s.charAt(j++);      
      int height = value >> 8;
      int color = value & 0xFF;
      color0 = i == 12 ? 0xFF666666
             : i == 16 ? 0xFF6B6D00
             : i == 17 ? 0xFF626262
             : i == 18 ? 0xFF004545
             : i == 19 ? 0xFF562F00
             : i == 20 ? 0xFF626262
             : 0xFF000000 | ((color & 0xE0) << 16)
                  | ((color & 0x1C) << 11) | ((color & 0x03) << 6);

      sprites[i] = new BufferedImage(width, i == 2 ? 32 : height, 2);

      for(y = 0; y < height; y++) {
        value = s.charAt(j);
        if (i < 3 || i > 15) {
          j++;
        } else if ((y&1) == 1) {
          j++;
          value >>= 8;
        } else {
          value &= 0xFF;
        }
        boolean inside = i == 0 || i > 15;
        for(x = 0; x < ((i < 3 || i > 15) ? 16 : 8); x++, value >>= 1) {
          if (i > 0 && i < 16) {
            if (((value&1) == 1)) {
              inside = true;
            }
            if (value == 0 && ((i > 6 && i < 13) || i == 1)) {
              inside = false;
            }
          }  
          int p = pixels[x + y * width]
              = inside ? (((value&1) == 0) ? color0 : color1) : 0;
          if (width > 8 && i > 1 && i < 16) {
            pixels[i == 6 ? (height - y) * width - x - 1
                : (y + 1) * width - x - 1] = p;
          }
          if (i == 2) {
            pixels[(32 - y) * 32 - x - 1] = pixels[x + (31 - y) * 32] = p;
          }
        }
      }
      sprites[i].setRGB(0, 0, width, i == 2 ? 32 : height, pixels, 0, width);
    }
    // -- decompress the sprites ends ------------------------------------------

    // -- decompress stages begins ---------------------------------------------
    for(i = 0, j = 159; i < 6; i++) {
      int[][] map = stages[i];
      for(y = 0; y < 40; y++) {
        value = s.charAt(j++);
        for(x = 0; x < 8; x++, value >>= 2) {
          int m = value & 3;
          if (m == 1) {
            // add 4 rocks
            map[y*2][x*2 + 1] = map[y*2 + 1][x*2 + 1]
                = map[y*2 + 1][x*2] = map[y*2][x*2] = 1;
          } else if (m == 2 && y >= 16 && y < 24) {
            map[y*2][x*2] = 4; 
          } else {
            map[y*2][x*2] = m;
          } 
        }
      }
    }
    // -- decompress stages ends -----------------------------------------------

    BufferedImage image = new BufferedImage(256, 256, 1);
    Graphics2D g = (Graphics2D)image.getGraphics();
    Graphics2D g2 = null;
    AffineTransform defaultTransform = g.getTransform();
    
    long nextFrameStartTime = System.nanoTime();    
    while(true) {

      do {
        nextFrameStartTime += 16666667;

        // -- update starts ----------------------------------------------------

        if (playerDead > 0 && --playerDead == 0) {
          // reset the stage after player death
          cameraY = 1008;
          spawnY = 79;
          stage = stages[stageIndex];
          sprites[0] = sprites[stageIndex > 0 ? 15 + stageIndex : 0];
          fireworks = false;
          player = new float[]
              // to test out a boss uncomment below
              // { 0, 0, 3, 64, 130, 1, 8, 12, 3.14f, 3.14f, 1, 0 };
              { 0, 0, 3, 128, 1216, 1, 8, 12, 3.14f, 3.14f, 1, 0 };
          queue.clear();
          queue.add(player);
        } else if (fireworks) {
          // create explosion
          queue.add(new float[] { 0, 6, 6,
              random.nextInt(256), random.nextInt(256),
              1, 8, 7, 0, 16, 0.1f});
        }

        // scan hidden region above to spawn enemies
        int nextSpawnY = (cameraY >> 4) - 2;
        while(nextSpawnY < spawnY && spawnY > 0) {
          for(x = 0; x < 16; x++) {
            int enemy = stage[spawnY][x];
            if (enemy == 0) {
              // randomly create man or mine
              if (spawnY > 16 && x > 0 && x < 15
                  && random.nextInt(64) == 0 && spawnY < 64) {
                if (random.nextInt(8) == 0) {
                  // create mine
                  queue.add(new float[] { 0, 11, 11,
                      x * 16 + 9, spawnY * 16 + 9,
                      0, 4, 4, 0, 0, 1, 0, 1, 0, 0, 0 });
                } else {
                  // create man
                  queue.add(new float[] { 0, 3, 8,
                      x * 16 + 9, spawnY * 16 + 9,
                      0, 4, 7, 0, 0, 1, 0, 1, 0, 0, 0 });
                }
              }
            } else if (enemy == 2) {
              // create gun
              queue.add(new float[] { 0, 4, 4,
                  (x + 1) * 16, (spawnY + 1) * 16, 1, 8, 8, 0, 0, 1, 0, 1,
                  0, 0, (x + 1) * 16, (spawnY + 1) * 16 });
            } else if (enemy == 3) {
              // create tank
              queue.add(new float[] { 0, 5, 5,
                  x * 16 + 9, spawnY * 16 + 9,
                  1, 8, 10, 0, 0, 1, 0, 1, 0, 0, 0 });
            } else if (enemy == 4) { 
              // create hidden gun
              queue.add(new float[] { 0, 10, 13,
                  (x + 1) * 16, (spawnY + 1) * 16, 1, 8, 8, 0, 0, 1, 180, 1,
                  0, 0, 0, 0 });
            } else if (spawnY > 16 && spawnY < 64
                && (x&1) == 0 && (spawnY&1) == 0
                && random.nextInt(20 - stageIndex * 2) == 0) {
              // create rolling rock
              queue.add(new float[] { 0, 9, 1,
                  x * 16 + 16, spawnY * 16 + 16,
                  1, 8, 8, 0, 0, 1, 0, 1, 0, 0, 0 });
            }
          }
          spawnY--;
        }

        // process queue
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] object = queue.get(i);
          if (object[0] != 0) {
            queue.remove(i);
            continue;
          }
          switch((int)object[1]) {
            case 0: // player
              if (a[0x26] && stage[((int)object[4] - 12 >> 4)]
                    [((int)object[3] >> 4)] != 1
                    && (int)object[4] - 12 > 0) { // up
                object[9] = 3.14f;
                object[4] -= 0.75f;
              } else if (a[0x28] && stage[((int)object[4] + 11 >> 4)]
                  [((int)object[3] >> 4)] != 1
                  && (int)object[4] < cameraY + 245) { // down
                object[9] = 0;
                object[4] += 0.75f;
              } else if (a[0x25] && stage[((int)object[4] >> 4)]
                  [((int)object[3] - 12 >> 4)] != 1
                  && (int)object[3] - 12 > 0) { // left
                object[9] = 1.57f;
                object[3] -= 0.75f;
              } else if (a[0x27] && stage[((int)object[4] >> 4)]
                  [((int)object[3] + 11 >> 4)] != 1
                  && (int)object[3] + 11 < 255) { // right
                object[9] = 4.71f;
                object[3] += 0.75f;
              }
              if (a[0x20] && object[11] < 1) { // shoot (non-arrow key)
                queue.add(new float[] { 0, 1, 7,
                    object[3], object[4], 0, 4, 3,
                    -2 * (float)Math.sin(object[9]),
                    2 * (float)Math.cos(object[9]), 60, 0, 1});
                object[11] = 1;
              }
              break;
            case 1: // player bullet
            case 2: // enemy bullet
              object[3] += object[8];
              object[4] += object[9];
              if (object[10] > 2 && object[3] > 2 && object[3] < 253
                  && object[4] < 1277 && object[4] > 0) {
                object[10]--;
                if (stage[((int)object[4] >> 4)]
                    [((int)object[3] >> 4)] == 1) {
                  object[0] = 1;
                  if (object[1] == 1) {
                    player[11] = 0;
                  }
                }

                if (object[1] == 1) {
                  // check if player bullet hits an enemy
                  for(j = queue.size() - 1; j >= 0; j--) {
                    float[] obj = queue.get(j);
                    if (obj[0] == 0) {
                      if (object[1] == 1 && (
                          (obj[1] == 11 && obj[2] > 0)
                          || obj[1] == 10 || obj[1] == 9
                          || obj[1] == 7
                          || (obj[1] >= 3 && obj[1] <= 5))) {
                        float dx = obj[3] - object[3];
                        float dy = obj[4] - object[4];
                        float radius = obj[1] == 3 ? (int)obj[10] * 4 + 4
                            : (int)obj[10] * 8 + 8;
                        if (dx * dx + dy * dy < radius * radius) {
                          object[0] = 1;
                          player[11] = 0;
                          if (obj[1] != 10
                              || (obj[11] > 15 && obj[11] < 90)) {
                            if (--obj[12] < 1) {

                              if (obj[1] == 3) {
                                // create dead man
                                obj[1] = 8;
                                obj[2] = 10;
                              } else {
                                obj[0] = 1;

                                if (obj[1] == 7) {
                                  if (stageIndex < 5) {
                                    // advance stage
                                    stageIndex++;
                                    playerDead = 300;
                                  } else {
                                    playerDead = 32768;
                                  }
                                  for(int k = queue.size() - 1; k >= 0; k--) {
                                    queue.get(k)[0] = 1;
                                  }
                                  player[0] = 0;
                                  fireworks = true;
                                }

                                // create exploding enemy
                                queue.add(new float[] { 0, 6, 6, obj[3], obj[4],
                                    1, 8, 7, 0, 16, 0.1f});
                              }
                            } else {
                              // create bullet exploding on enemy
                              queue.add(new float[] { 0, 6, 6,
                                  object[3], object[4], 1, 8, 7, 0, 16, 0.1f});
                            }
                          }
                        }
                      }
                    }
                  }
                } else if (player[0] == 0) {
                  float vx = player[3] - object[3];
                  float vy = player[4] - object[4];
                  if (vx * vx + vy * vy < 128) {
                    object[0] = 1;
                    player[0] = 1;
                    playerDead = 120;
                    // create exploding player
                    queue.add(new float[] { 0, 6, 6, player[3], player[4],
                        1, 8, 7, 0, 16, 0.1f});
                  }
                }
              } else {
                object[0] = 1;
                if (object[1] == 1) {
                  player[11] = 0;
                }
              }
              break;
            case 4: // gun
              if (object[11] < 165) {
                object[9] = (float)Math.atan2(player[4] - object[4],
                    player[3] - object[3]) - 1.57f;
                object[3] = object[15];
                object[4] = object[16];
              } else {
                float deflection = 4 * (float)Math.sin(
                    0.209f * (object[11] - 165));
                object[3] = object[15] - object[13] * deflection;
                object[4] = object[16] - object[14] * deflection;
              }
              if (object[11] > 0) {
                object[11]--;
              } else {
                object[11] = 180;
                float vx = player[3] - object[3];
                float vy = player[4] - object[4];
                float mag = (float)Math.sqrt(vx * vx + vy * vy);

                // guns shoots enemy bullet
                queue.add(new float[] { 0, 2, 7,
                    object[3], object[4], 0, 4, 3,
                    object[13] = vx / mag,
                    object[14] = vy / mag, 120, 0, 1});
                object[15] = object[3];
                object[16] = object[4];
              }
              break;
            case 10: // hidden gun
              if (object[11] >= 15 && object[11] < 75) {
                if (object[11] == 74) {
                  object[9] = (float)Math.atan2(
                      player[4] - object[4],
                      player[3] - object[3]) - 1.57f;
                } else if (object[11] == 60) {
                  // hidden gun shoots 5 bullets
                  float angle = object[9] + 0.785f;
                  for(int z = 0; z < 5; z++, angle += 0.314f) {
                    queue.add(new float[] { 0, 2, 7,
                        object[3], object[4], 0, 4, 3,
                        (float)Math.cos(angle),
                        (float)Math.sin(angle), 120, 0, 1});
                  }
                }
                object[2] = 15;
              } else if (object[11] < 15
                  || (object[11] >= 75 && object[11] < 90)) {
                object[2] = 14;
                object[8] = 0;
                object[9] = 0;
              } else {
                object[8] = 0;
                object[9] = 0;
                object[2] = 13;
              }
              if (--object[11] == 0) {
                object[11] = 240;
              }
              break;
            case 3: // man
            case 5: // tank
            case 7: // boss
              if (object[13] < 1) {
                // seeking

                float nextX = object[3] + object[14];
                float nextY = object[4] + object[15];

                boolean safeMove = true;
                for(int z = -1; z <= 1; z++) {
                  for(int b = -1; b <= 1; b++) {
                    int X = ((int)nextX + z * 8) >> 4;
                    int Y = ((int)nextY + b * 8) >> 4;
                    if (X < 0 || X > 15 || Y < 0 || Y > 78
                        || stage[Y][X] == 1) {
                      safeMove = false;
                      break;
                    }
                  }
                }

                if (--object[11] > 0 && (object[1] == 7 || safeMove)) {
                  // actually moving
                  object[3] = nextX;
                  object[4] = nextY;
                  if ((object[2] == 8 || object[2] == 9)
                     && ((int)object[11] & 7) == 0) {
                    // man's legs move
                    object[2] = object[2] > 8 ? 8 : 9;
                  }
                } else {
                  // either bumped into a wall or seeking time expired
                  object[13] = 1;
                  object[11] = object[1] == 7 ? 2 : 120;
                  float vx = player[3] - object[3];
                  float vy = player[4] - object[4];
                  float mag = (float)Math.sqrt(vx * vx + vy * vy);
                  vx /= mag;
                  vy /= mag;
                  // fire bullet
                  queue.add(new float[] { 0, 2, 7,
                      object[3], object[4], 0,
                      4, 3, vx, vy, 120, 0, 1});
                }
              } else if (--object[11] < 1) {
                // paused expired

                float vx = player[3] - object[3];
                float vy = player[4] - object[4];
                float mag = (float)Math.sqrt(vx * vx + vy * vy);
                vx /= mag;
                vy /= mag;

                object[13] = 0;
                object[11] = object[1] == 7 ? 60 : 120;
                if (random.nextBoolean()) {
                  object[14] = vx * 0.5f;
                  object[15] = vy * 0.5f;
                } else {
                  mag = 6.28f * random.nextFloat();
                  object[14] = 0.5f * (float)Math.cos(mag);
                  object[15] = 0.5f * (float)Math.sin(mag);
                }
                if (object[1] == 3) {
                  object[14] *= 0.5f;
                  object[15] *= 0.5f;
                  object[11] *= 2;
                }
                object[9] = (float)Math.atan2(object[15], object[14]) - 1.57f;
              }
              break;
            case 6: // explosion
              object[10] += 0.5f;
              object[3]-=2;
              object[4]-=2;
              if (object[10] > 3) {
                object[0] = 1;
              }
              break;
            case 9: {// rolling rock
              float vx = player[3] - object[3];
              float vy = player[4] - object[4];
              float mag = vx * vx + vy * vy;
              if (object[11] == 0) {
                if (mag < 8192) {
                  object[9] = 128;
                  object[11] = 128;
                  mag = (float)Math.sqrt(mag);
                  object[14] = vx * 0.5f / mag;
                  object[15] = vy * 0.5f / mag;                  
                }
              } else {
                if (object[11] > 1) {                  
                  object[11]--;
                  object[3] += object[14];
                  object[4] += object[15];
                } else {
                  object[9] = object[8];
                }
              }
              
              break;
            }
            case 11: {// mine
              float vx = player[3] - object[3];
              float vy = player[4] - object[4];
              float mag = vx * vx + vy * vy;
              if (object[11]-- == 0) {
                object[11] = 2;
                object[2] = mag > 8192 ? -1 : object[2] == 11 ? 12 : 11;
              }
              break;
            }
          }

          // boss can spawn enemies
          if (object[1] == 7) {
            if (stageIndex > 1 && --bossSpawn < 0) {
              bossSpawn = 360 + random.nextInt(512);

              if ((stageIndex == 5 && random.nextBoolean())
                  || stageIndex == 2 || stageIndex == 4) {
                // create tank
                queue.add(new float[] { 0, 5, 5,
                    64 + random.nextInt(128), 255,
                    1, 8, 10, 0, 0, 1, 60, 1, 0, 0, -0.5f });
              } else {
                // create man
                queue.add(new float[] { 0, 3, 8,
                    64 + random.nextInt(128), 255, 0, 4, 7, 0, 0,
                    1, 180, 1, 0, 0, -0.25f });
              }
            }
          }

          // check if player collides with enemy
          if ((object[1] >= 3 && object[1] <= 5) || object[1] == 7
              || object[1] == 9 || object[1] == 10 || object[1] == 11) {
            float vx = player[3] - object[3];
            float vy = player[4] - object[4];
            vx = vx * vx + vy * vy;
            vy = 12 + 10 * object[10];
            if (object[1] == 3 && vx < 64) {
              // create dead man by running over him
              object[1] = 8;
              object[2] = 10;
            } else if (object[1] != 3 && vx < (object[1] == 11 ? 128 :
                vy * vy)
                && player[0] < 1) {
              // player explodes as a result of crashing into enemy
              player[0] = 1;
              playerDead = 120;
              queue.add(new float[] { 0, 6, 6, player[3], player[4],
                  1, 8, 7, 0, 16, 0.1f});

              // enemy also explodes unless it is a boss
              if (object[1] != 7) {
                object[0] = 1;
                queue.add(new float[] { 0, 6, 6, object[3], object[4],
                    1, 8, 7, 0, 16, 0.1f});
              }
            }
          }

          // rotate approaching target angle
          if (object[5] != 0) {
            if (Math.abs(object[8] - object[9]) > 0.1f) {
              if (object[8] < object[9]) {
                object[8] += 0.1f;
              } else {
                object[8] -= 0.1f;
              }
            } else {
              object[8] = object[9];
            }
          }
        }

        // camera tracks player
        int nextCameraY = (int)player[4] - 128;
        if (nextCameraY < 0) {
          nextCameraY = 0;
        }
        if (nextCameraY < cameraY) {
          cameraY = nextCameraY;
          if (cameraY == 0) {
            // create boss

            bossSpawn = 0;
            boolean b = stageIndex != 1;
            float scale = 2 + (b ? 0.5f * stageIndex : stageIndex);
            queue.add(new float[] { 0, 7,
                  stageIndex == 5 ? 15 :
                  stageIndex == 4 ? 6 :stageIndex == 3 ? 4
                  : (b ? (stageIndex == 2 ? 1 : 5) : 8),
                128, -96, 
                b ? 1 : 0,
                (b ? 8 : 4) * scale,
                (b ? (stageIndex == 5 ? 8 : stageIndex == 4 ? 7 : 10) : 7)
                    * scale, 0, 0,
                scale,
                256, 5, 0, 0, 0.5f });
          }
        }
        // -- update ends ------------------------------------------------------
        
      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------

      // render sand
      for(y = 0; y < 17; y++) {
        for(x = 0; x < 16; x++) {
          g.drawImage(sprites[0], x * 16, y * 16 - (cameraY & 15), null);          
        }
      }
      
      // render rocks and gun platforms
      for(y = -1; y < 17; y++) {
        for(x = 0; x < 16; x++) {
          j = y + (cameraY >> 4);
          if (j >= 0) {
            j = stage[j][x];
            if (j == 1 || j == 2) {
              g.drawImage(sprites[j], x * 16, y * 16 - (cameraY & 15), null);
            }
          }
        }
      }

      // render sprite queue
      for(i = queue.size() - 1; i >= 0; i--) {
        float[] object = queue.get(i);
        if (object[2] >= 0) {
          g.translate((int)(object[3] - object[6]),
              (int)(object[4] - object[7]) - cameraY);
          if (object[5] != 0 || object[1] == 7) {            
            g.rotate(object[8], object[6], object[7]);
            g.scale(object[10], object[10]);
          }
          g.drawImage(sprites[(int)object[2]], 0, 0, null);
          g.setTransform(defaultTransform);
        }

        // to track sprite centers, uncomment below
        //g.setColor(java.awt.Color.WHITE);
        //g.drawRect((int)object[3] - 1, (int)object[4] - 1 - cameraY, 3, 3);
      }

      // -- render ends --------------------------------------------------------

      // show the hidden buffer
      if (g2 == null) {
        g2 = (Graphics2D)getGraphics();
        requestFocus();
      } else {
        g2.drawImage(image, 0, 0, 512, 512, null);
      }

      // burn off extra cycles
      while(nextFrameStartTime - System.nanoTime() > 0) {
        Thread.yield();
      }      
    }
  }

  @Override
  public void processKeyEvent(KeyEvent keyEvent) {
    int k = keyEvent.getKeyCode();
    if (k > 0) {
      if (k == 0x57) { // W
        k = 0x26; // up
      } else if (k == 0x53) { // S
        k = 0x28; // down
      } else if (k == 0x41) { // A
        k = 0x25; // left
      } else if (k == 0x44) { // D
        k = 0x27; // right
      }
      a[k >= 0x25 && k <= 0x28 ? k : 0x20] = keyEvent.getID() != 402;
    }
  }

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame("Jackal 4K");
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    a applet = new a();    
    applet.setPreferredSize(new java.awt.Dimension(512, 512));
    frame.add(applet, java.awt.BorderLayout.CENTER);
    frame.setResizable(false);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    Thread.sleep(250);
    applet.start();
  }*/
}
