package java4k.magewars4k;

/*
 * Out Run 4K
 * Copyright (C) 2011 meatfighter.com
 *
 * This file is part of Out Run 4K.
 *
 * Out Run 4K is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Out Run 4K is distributed in the hope that it will be useful,
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
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class M extends Applet implements Runnable {

  // keys
  private boolean[] a = new boolean[32768];

  @Override
  public void start() {
    enableEvents(8);
    new Thread(this).start();
  }

  public void run() {

    final int SEGMENTS = 4096;
    final int ROAD = 0;
    final int HILL = 1;
    final int CHECKPOINT_SPRITE = 11;    
    final int TELEPHONE_POLE_SPRITE = 12;
    final int SNOWMAN_SPRITE = 13;
    final int TREE_SPRITE = 14;
    final float I256 = 0.00390625f;
    final float ACCELERATION = 0.003f;
    final float CAR_SPEED = 0.05f;

    final int OBJ_TIME = 0;
    final int OBJ_X = 1;
    final int OBJ_DT = 2;
    final int OBJ_SPRITE = 3;
    final int OBJ_SPRITE_X = 4;
    final int OBJ_SPRITE_Y = 5;
    final int OBJ_SPRITE_WIDTH = 6;
    final int OBJ_SPRITE_HEIGHT = 7;
    final int OBJ_CAR = 8;
    final int OBJ_SEEKER = 9;
    final int OBJ_HALF_WIDTH = 10;
    final int OBJ_HEIGHT = 11;
    final int OBJ_CHECKPOINT = 12;
    final int OBJ_SEEKER_SPEED = 13;

    final int ROAD_HEIGHT = 0;
    final int ROAD_K = 1;
    final int ROAD_OFFSET = 2;
    final int ROAD_CLIP = 3;
    final int ROAD_Y = 4;

    final int VK_LEFT = 0x25;
    final int VK_RIGHT = 0x27;
    final int VK_GAS = 0x44;
    final int VK_START = '\n';

    int x;
    int y;
    int z;
    int i;
    int j;
    int k;
    int time = 50;
    int timeIncrementor = 0;
    float mag;
    float nextCarTime = 32;
    boolean wonBlink = false;
    boolean startReleased = true;
    boolean raceStarted = false;

    final int[] colors = {
      0xE3BD7C, // sand
      0x6AC730, // grass
      0xFF8A68, // rock
      0xD7C6B4, // rock 2
      0xF1F0F5, // snow
    };

    BufferedImage image = new BufferedImage(256, 256, 1);
    BufferedImage[] sprites = new BufferedImage[64];
    Graphics2D g = (Graphics2D)image.getGraphics();
    Graphics2D g2 = null;
    Graphics2D g3 = null;
    Graphics2D g4 = null;
    ArrayList<float[]> queue = new ArrayList<float[]>();
    ArrayList[] renderList = new ArrayList[8192];
    float[][] roadInfos = new float[8192][16];
    float[] player = new float[32];
    Random random = new Random(37);

    Color[] backgroundColors = new Color[10];
    for(i = 0; i < 10; i++) {
      int color = colors[i >> 1];
      if ((i & 1) == 0) {
        backgroundColors[i] = new Color(color);
      } else {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color >> 16, 0xFF & (color >> 8), 0xFF & color, hsv);
        backgroundColors[i] = new Color(Color.HSBtoRGB(hsv[0], hsv[1],
          hsv[2] - .03f));
      }
    }

    // create player
    queue.add(player);
    player[OBJ_SPRITE] = 0;
    player[OBJ_TIME] = 16;
    player[OBJ_HALF_WIDTH] = 42;
    player[OBJ_HEIGHT] = 54;

    for(i = 0; i < 8192; i++) {
      renderList[i] = new ArrayList<float[]>();
    }

    // generate road and hill segments
    float[][][] segments = new float[2][SEGMENTS][3];
    float slope;
    float nextSlope;

    for(k = 0; k < 2; k++) {
      slope = 0;
      nextSlope = 0.3f;

      for(i = 1; i < SEGMENTS; i++) {

        float[] lastSegment = segments[k][i - 1];
        float[] segment = segments[k][i];

        j = random.nextInt(17);
        if (k == ROAD) {
          float ds = nextSlope - slope;
          if (ds < 0) {
            ds = -ds;
          }
          if (ds > 0.025f) {
            if (slope < nextSlope) {
              slope += 0.025f;
            } else {
              slope -= 0.025f;
            }
          }
          if (j == 0) {
            if (ds <= 0.025f) {
              if (nextSlope != 0) {
                nextSlope = 0;
              } else {
                nextSlope = random.nextBoolean() ? 0.4f : -0.4f;
              }
            }
          }
        } else {
          if (lastSegment[2] < -7.25f) {
            slope = nextSlope;
          } else if (lastSegment[2] > 7.25f) {
            slope = -nextSlope;
          } if (j == 0) {
            slope = nextSlope;
          } else if (j == 1) {
            slope = -nextSlope;
          } else if (j == 2) {
            slope = 0;
            nextSlope = 0.3f * random.nextFloat();
          }
        }

        segment[2] = lastSegment[0] + lastSegment[1] + lastSegment[2];
        segment[1] = 2 * lastSegment[0] + lastSegment[1];
        segment[0] = (slope - segment[1]) / 2;
      }
    }

    // generate tree sprites
    for(k = 0; k < 3; k++) {
      if (k == 2) {
        random = new Random(22);
      }
      sprites[TREE_SPRITE + k] = new BufferedImage(128, 128, 2);
      g3 = (Graphics2D)sprites[TREE_SPRITE + k].getGraphics();
      ArrayList<AffineTransform> transforms = new ArrayList<AffineTransform>();
      AffineTransform transform = g3.getTransform();
      transform.translate(64, 128);
      transforms.add(transform);
      while(transforms.size() > 0) {
        transform = transforms.remove(0);
        mag = (float)transform.getScaleX() + (float)transform.getScaleY();
        mag *= 0.1f;
        float hue = 0.3f - mag;
        if (hue < 0.175f) {
          hue = 0.175f;
        }
        if (k == 0) {
          // green tree
          g3.setColor(new Color(Color.HSBtoRGB(hue, 0.5f + hue, 0.7f - mag)));
        } else if (k == 1) {
          // autom tree
          g3.setColor(new Color(Color.HSBtoRGB(
              hue - 0.2f, 0.5f + hue, 0.7f - mag)));
        } else {
          // flowering tree
          float s = 0.495f - hue * 1.9f;
          if (s < -0.05f) {
            s = -0.05f;
          }
          g3.setColor(new Color(Color.HSBtoRGB(
             hue, 0.495f - hue * 1.9f, 0.8f - mag)));
        }
        g3.setTransform(transform);
        g3.fillRect(-10, -40, 20, 40);
        if (mag > 0.03f) {
          for(i = 0; i < 5; i++) {
            AffineTransform transform2 = (AffineTransform)transform.clone();
            transforms.add(transform2);
            transform2.translate(0, -30);
            transform2.scale(0.7f + 0.1f * random.nextFloat(),
                0.7f + 0.1f * random.nextFloat());
            mag = 0.5f + 0.5f * random.nextFloat();
            if (random.nextBoolean()) {
              mag = -mag;
            }
            transform2.rotate(mag);
          }
        }
      }
    }

    // generate snowman sprite
    sprites[SNOWMAN_SPRITE] = new BufferedImage(256, 256, 2);
    g4 = (Graphics2D)sprites[SNOWMAN_SPRITE].getGraphics();
    AffineTransform transform = g4.getTransform();
    for(x = 2; x >= 0; x--) {
      for(i = 0; i < 16; i++) {
        k = i << 2;
        j = 195 + k;
        g4.setColor(new Color(j, j, j));
        g4.fillOval((i << 1) + 64, (i << 1) + 128, 127 - k, 127 - k);
      }
      g4.translate(24, -40);
      g4.scale(0.8f, 0.8f);
    }
    g4.setColor(Color.BLACK);
    g4.fillOval(92, 250, 25, 25);
    g4.fillOval(142, 250, 25, 25);
    g4.setTransform(transform);

    // generate telephone pole sprite
    sprites[TELEPHONE_POLE_SPRITE] = new BufferedImage(84, 256, 2);
    g3 = (Graphics2D)sprites[TELEPHONE_POLE_SPRITE].getGraphics();
    for(i = 0; i < 16; i++) {
      Color color = new Color(Color.HSBtoRGB(24f / 360, 0.69f,
        (74 - (i << 1)) / 100f));
      g3.setColor(color);
      y = 4 + (i >> 1);
      g3.drawLine(34 + i, 0, 34 + i, 255);
      g3.drawLine(0, y, 83, y);
      g3.drawLine(16, y, 42, y + 26);
      g3.drawLine(68, y, 42, y + 26);

      // add arms to snowman
      g4.setColor(color);
      g4.drawLine(0, 96 + i, 48, 64 + i);
      g4.drawLine(48, 64 + i, 90, 96 + i);
      g4.drawLine(255, 96 + i, 207, 64 + i);
      g4.drawLine(207, 64 + i, 165, 96 + i);
    }

    // generate road bitmap
    int[] pixels = new int[256 << 8];
    BufferedImage[] roadImages = new BufferedImage[256];
    for(y = 0; y < 256; y++) {      
      for(x = 0; x < 256; x++) {
        float angle = 8 * 6.28f * x / 243f;
        mag = 0.5f + 0.5f * (float)Math.sin(angle);
        i = 0x72 + (int)((0x93 - 0x72) * mag);
        j = 0x68 + (int)((0x8A - 0x68) * mag);
        k = 0x67 + (int)((0x83 - 0x67) * mag);
        float scale = y < 128 ? 0.95f : 1f;
        scale -= 0.05f * random.nextFloat();
        i = (int)(i * scale);
        j = (int)(j * scale);
        k = (int)(k * scale);
        pixels[x] = (i << 16) | (j << 8) | k;
      }
      for(i = 0; i < 5; i++) {
        if (i == 0 || i == 4 || y < 128) {
          for(x = 0; x < 4; x++) {
            pixels[x + i * 62 + 2] = i == 2 ? 0xF5C549 : 0xE7DED5;
          }
        }
      }
      roadImages[y] = new BufferedImage(256, 1, 1);
      roadImages[y].setRGB(0, 0, 256, 1, pixels, 0, 256);
    }

    // generate sky image
    BufferedImage skyImage = new BufferedImage(512, 512, 1);
    g3 = (Graphics2D)skyImage.getGraphics();
    for(y = 0; y < 512; y++) {
      mag = y / 512f;
      i = 0x6B + (int)((0xB7 - 0x6B) * mag);
      j = 0x92 + (int)((0xD4 - 0x92) * mag);
      k = 0xD3 + (int)((0xF2 - 0xD3) * mag);
      for(x = 0; x < 512; x++) {
        pixels[x] = (i << 16) | (j << 8) | k;
      }
      skyImage.setRGB(0, y, 512, 1, pixels, 0, 512);
    }    
    for(i = 0; i < 2048; i++) {
      x = random.nextInt(512);
      mag = random.nextFloat();
      y = (int)(mag * mag * mag * mag * 512) - 16;
      g3.setColor(new Color(0x0AFFFFFF, true));
      g3.fillOval(x, y, 100, 20);
    }

    // generate car sprites
    String S = "\u03c0\u0000\u5ff0\u0000\u5ff0\u0000\ufbe4\u0000\u0aac\u0000\u0aad\u0000\u0aa3\u0000\u0aa3\u0000\u0aa3\u0000\u0283\u0000\u0e83\uf000\u3aaf\ufc00\ueaaa\ufc00\uaaab\ubc00\ufeaa\u8000\uaaaa\u8000\uaaaa\u5000\u555a\u9000\u5555\u9400\u5555\u5400\u5555\u5500\u5555\u5550\u5555\u5550\u5555\u5554\u5555\u5554\u5555\u5554\u5555\u5554\u5555\u5554\u5555\u5554\uaa95\u5557\uaaa9\u5557\uaaaa\u9557\uaaaa\ua955\u5555\u55fd\u555f\ufead\u555a\uaaa5\u955a\uaaa5\u955a\uaaa5\u555a\ua957\u5555\u57f7\u5555\u57f7\ufff5\u57f7\uffd5\u57f7\uaad5\u57f7\uffd5\u55f4\ufb55\u55f4\ufaff\uf5f4\ufaea\ufff4\uaaee\ufff4\ufff3\ufffc\uffc0\u3ffc\u0000\u3ffc\u0000\u3ffc\u0000\u3ff0";    
    for(k = 0; k < 10; k++) {

      mag = 0.628f * (k + 6);
      x = (int)(256f * (0.5f + 0.5f * (float)Math.sin(mag)));
      y = (int)(256f * (0.5f + 0.5f * (float)Math.sin(mag + 2.09f)));
      z = (int)(256f * (0.5f + 0.5f * (float)Math.sin(mag + 4.19f)));

      int color1 = 0xFF000000 | (x << 16) | (y << 8) | z;
      int color2 = 0xFF000000 | ((x >> 1) << 16) | ((y >> 1) << 8) | (z >> 1);
      int color3 = 0xFF000000 | (((x + 768) >> 2) << 16)
          | (((y + 768) >> 2) << 8) | ((z + 768) >> 2);

      for(y = 0; y < 54; y++) {
        i = (S.charAt(y << 1) << 16) | S.charAt((y << 1) + 1);
        for(x = 0; x < 16; x++) {
          j = i & 3;
          pixels[(y << 5) | x] = pixels[(y << 5) | (31 - x)]
              = j == 0 ? 0 : j == 1 ? (y < 35 ? color1 : color2) : j == 2
                  ? (y < 11 ? 0xFFB86838 : y < 24 ? 0xFF682808 :
                    y < 34 ? color3 :
                    y < 40 ? 0xFFF00020 :
                    0xFFB8B8B8) : 0xFF000000;
          i >>= 2;
        }
      }
      BufferedImage carImage = new BufferedImage(32, 54, 2);
      carImage.setRGB(0, 0, 32, 54, pixels, 0, 32);

      sprites[k] = new BufferedImage(84, 54, 2);
      g3 = (Graphics2D)sprites[k].getGraphics();
      g3.drawImage(carImage, 0, 0, 84, 54, null);
    }

    // generate check point sprite
    sprites[CHECKPOINT_SPRITE] = new BufferedImage(710, 256, 2);
    g3 = (Graphics2D)sprites[CHECKPOINT_SPRITE].getGraphics();
    for(z = 0; z < 8; z++) {
      int h = z << 5;
      for(x = 0; x < 22; x++) {
        if (z == 0 || x == 0 || x == 21) {
          k = x << 5;
          for(i = 0; i < 3; i++) {
            Color color = Color.WHITE;
            for(j = 0; j < 5; j++) {
              g3.setColor(color);
              color = color.darker();
              y = j + (i << 4);
              g3.drawLine(k, y + h, k + 32, y + h);
              g3.drawLine(k + y, h, k + y, h + 32);
              if (i == 0) {
                g3.drawLine(k, y + h, k + 32, y + h + 32);
                g3.drawLine(k + 32, y + h, k, y + h + 32);
              }
            }
          }
        }
      }
    }

    long nextFrameStartTime = System.nanoTime();
    while(true) {

      do {
        nextFrameStartTime += 16666667;

        // -- update starts ----------------------------------------------------

        if (!a[VK_START]) {
          startReleased = true;
        }

        if (startReleased && a[VK_START]) {
          // reset the race
          startReleased = false;
          raceStarted = true;
          time = 50;
          timeIncrementor = 0;
          nextCarTime = 32;
          wonBlink = false;
          queue.clear();
          player = new float[32];
          queue.add(player);
          player[OBJ_SPRITE] = 0;
          player[OBJ_TIME] = 16;
          player[OBJ_HALF_WIDTH] = 42;
          player[OBJ_HEIGHT] = 54;
          random = new Random();

          // create checkpoints
          for(i = 0; i < 6; i++) {
            float[] checkpoint = new float[32];
            queue.add(checkpoint);
            checkpoint[OBJ_SPRITE] = CHECKPOINT_SPRITE;
            checkpoint[OBJ_TIME] = 18 + 400 * i;
            checkpoint[OBJ_HALF_WIDTH] = 352;
            checkpoint[OBJ_HEIGHT] = 256;
            checkpoint[OBJ_CHECKPOINT] = 1;
          }
        }

        // update remaining time
        if (raceStarted) {
          if (player[OBJ_TIME] < 2018) {
            if (++timeIncrementor == 60) {
              timeIncrementor = 0;
              time--;
            }
          } else {
            if (++timeIncrementor > 8) {
              timeIncrementor = 0;
              wonBlink = !wonBlink;
            }
          }
        }

        // update player car
        if (raceStarted && time > 0 && player[OBJ_TIME] < 2018
            && a[VK_GAS] 
            && player[OBJ_DT] < (player[OBJ_X] > 1.25f
                || player[OBJ_X] < -1.25f ? 0.05f : 0.15f)) {
          player[OBJ_DT] += ACCELERATION;
        } else {
          if (player[OBJ_DT] > ACCELERATION) {
            player[OBJ_DT] -= ACCELERATION;
          } else if (player[OBJ_DT] < -ACCELERATION) {
            player[OBJ_DT] += ACCELERATION;
          } else {
            player[OBJ_DT] = 0;
          }
        }

        int t = (int)player[OBJ_TIME];
        player[OBJ_TIME] += player[OBJ_DT];
        if (t != (int)player[OBJ_TIME]) {
          i = ((int)player[OBJ_TIME] - 18);
          j = i / 400;
          k = i - j * 400;
          if (k > 100 && k < 300) {            
            if (j == 0) {
              if ((i & 3) == 0) {
                // add telephone pole
                float[] pole = new float[32];
                queue.add(pole);
                pole[OBJ_TIME] = player[OBJ_TIME] + 32;
                pole[OBJ_SPRITE] = TELEPHONE_POLE_SPRITE;
                pole[OBJ_HALF_WIDTH] = 42;
                pole[OBJ_HEIGHT] = 256;
                pole[OBJ_X] = 1.4f;
              }
            } else {
              // add tree
              float[] tree = new float[32];
              queue.add(tree);
              tree[OBJ_TIME] = player[OBJ_TIME] + 32;
              tree[OBJ_SPRITE] = j == 4 ? SNOWMAN_SPRITE 
                  : TREE_SPRITE + j - 1;
              tree[OBJ_HALF_WIDTH] = 256;
              tree[OBJ_HEIGHT] = 512;
              tree[OBJ_X] = random.nextBoolean() ? -1.4f : 1.4f;
            }
          }
        }

        player[OBJ_X] += 67 * player[OBJ_DT] * player[OBJ_DT]
            * segments[ROAD][(int)player[OBJ_TIME]][0];
        if (a[VK_LEFT]) {
          player[OBJ_X] -= player[OBJ_DT] * 0.167f;
        } else if (a[VK_RIGHT]) {
          player[OBJ_X] += player[OBJ_DT] * 0.167f;
        }

        if (player[OBJ_TIME] > nextCarTime) {

          int segment = (int)((player[OBJ_TIME] - 18) / 400);

          // add cars
          x = 1;
          j = 1;
          if (random.nextInt(1 + segment) == 0) {
            if (random.nextBoolean()) {
              j += random.nextInt(7);
            }
            if (random.nextBoolean()) {
              x += random.nextInt(3);
            }
          }
          k = random.nextInt(4);
          for(y = 0; y < x; y++) {
            mag = 0.6f * ((y + k) % 4) - 0.9f;
            for(i = 0; i < j; i++) {
              float[] car = new float[32];
              queue.add(car);
              car[OBJ_SPRITE] = 1 + random.nextInt(7);
              car[OBJ_X] = mag;
              car[OBJ_TIME] = 32 + player[OBJ_TIME] + i;
              car[OBJ_CAR] = 1;
              car[OBJ_HALF_WIDTH] = 42;
              car[OBJ_HEIGHT] = 54;
              if (x == 1 && j == 1 
                  && random.nextInt(6 - segment) == 0) {
                car[OBJ_SEEKER] = 1;
                car[OBJ_SEEKER_SPEED] = 0.01f + segment * 0.001f;
              }
            }
          }

          nextCarTime = 16 + player[OBJ_TIME] + random.nextInt(16 - segment);
        }

        // update other cars
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] car = queue.get(i);
          if (car[OBJ_CAR] == 1) {
            car[OBJ_TIME] += CAR_SPEED;

            // test for collision with player
            if (player[OBJ_DT] > 0
                && player[OBJ_TIME] < car[OBJ_TIME]
                && player[OBJ_TIME] > car[OBJ_TIME] - 0.16f) {
              float dx = car[OBJ_X] - player[OBJ_X];
              if (dx < 0) {
                dx = -dx;
              }
              if (dx < 0.328125f) {
                // collision occurred
                player[OBJ_DT] = -player[OBJ_DT];
                car[OBJ_SEEKER] = 0;
              }
            }

            // seeker cars intentionally move directly in front of the player
            if (car[OBJ_SEEKER] == 1
                && player[OBJ_X] < 1.25f && player[OBJ_X] > -1.25f) {
              if (car[OBJ_X] < player[OBJ_X] - car[OBJ_SEEKER_SPEED]) {
                car[OBJ_X] += car[OBJ_SEEKER_SPEED];
              } else if (car[OBJ_X] > player[OBJ_X] + car[OBJ_SEEKER_SPEED]) {
                car[OBJ_X] -= car[OBJ_SEEKER_SPEED];
              }
            }
          } else if (car[OBJ_CHECKPOINT] == 1) {
            if (car[OBJ_TIME] <= player[OBJ_TIME]) {
              time = 50;
            }
          }
          if (car[OBJ_TIME] < player[OBJ_TIME] - 8) {
            queue.remove(i);
          }
        }

        // -- update ends ------------------------------------------------------

      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------


      // Find player coordinates in space (playerX is within the segment)
      float playerTime = player[OBJ_TIME];
      float playerX = playerTime - (int)playerTime;
      float[] road = segments[ROAD][(int)playerTime];
      float[] hill = segments[HILL][(int)playerTime];
      float playerY = (road[0] * playerX + road[1]) * playerX + road[2];
      float playerZ = (hill[0] * playerX + hill[1]) * playerX + hill[2] + 0.5f;

      // direction of road at player point
      float vx = 1;
      float vy = 2 * road[0] * playerX + road[1];
      mag = (float)Math.sqrt(vx * vx + vy * vy);
      vx /= mag;
      vy /= mag;
      
      // draw sky
      i = (int)(playerZ * 16) - 128;
      if (i > 0) {
        i = 0;
      }
      if (i < -255) {
        i = -255;
      }
      g.drawImage(skyImage, (int)(vy * 256) - 128, i, null);

      // draw road
      int height = 256;
      for(i = 0; i < 32; i++) {
        road = segments[ROAD][(int)playerTime + i];
        hill = segments[HILL][(int)playerTime + i];
        for(j = 0; j < 256; j++) {
          float tx = I256 * j;

          // coordinates of road point relative to player
          float py = (road[0] * tx + road[1]) * tx + road[2] - playerY;
          float pz = (hill[0] * tx + hill[1]) * tx + hill[2] - playerZ;
          float px = tx - playerX + i;

          // rotate point around z-axis using road direction as basis vector
          float qx = vy * px - vx * py - player[OBJ_X];
          float qy = vx * px - vy * py;

          float K = 256 / (1 + qy);
          int Y = 128 - (int)(K * pz);

          // only draw a strip of road if it is not blocked by nearer hill
          if (Y < height) {
            height = Y;

            int X1 = 128 + (int)(K * (qx - 1.25f));
            int X2 = 128 + (int)(K * (qx + 1.25f));

            k = (((int)playerTime + i - 18) / 400) << 1;
            if (k > 8) {
              k = 8;
            }
            g.setColor((tx - (int)tx) < 0.5f ? 
                backgroundColors[k] : backgroundColors[k + 1]);
            g.fillRect(0, Y, 256, 1);

            g.drawImage(roadImages[(int)((tx - (int)tx) * 256)],
                    X1, Y, X2 - X1 + 1, 1, null);
          }

          // save information about the road for rendering sprites on next pass
          float[] roadInfo = roadInfos[(i << 8) + j];
          roadInfo[ROAD_HEIGHT] = pz;
          roadInfo[ROAD_K] = K;
          roadInfo[ROAD_OFFSET] = qx;
          roadInfo[ROAD_CLIP] = height;
          roadInfo[ROAD_Y] = Y;
        }
      }

      if (raceStarted) {
        // order and position sprites
        for(i = 8191; i >= 0; i--) {
          renderList[i].clear();
        }
        for(i = queue.size() - 1; i >= 0; i--) {
          float[] object = queue.get(i);
          int index = (int)(256 * (object[OBJ_TIME] - (int)playerTime));
          if (index >= 0 && index < 8192) {
            float[] roadInfo = roadInfos[index];
            renderList[index].add(object);
            float K = roadInfo[ROAD_K];
            float W = object[OBJ_HALF_WIDTH] * I256;
            int X1 = 128 + (int)(K * (roadInfo[ROAD_OFFSET] + object[OBJ_X] - W));
            int X2 = 128 + (int)(K * (roadInfo[ROAD_OFFSET] + object[OBJ_X] + W));
            int Y1 = 128 - (int)(K * (roadInfo[ROAD_HEIGHT]
                + object[OBJ_HEIGHT] * I256));
            object[OBJ_SPRITE_X] = X1;
            object[OBJ_SPRITE_Y] = Y1;
            object[OBJ_SPRITE_WIDTH] = X2 - X1 + 1;
            object[OBJ_SPRITE_HEIGHT] = (int)roadInfo[ROAD_Y] - Y1 + 1;
          }
        }

        // draw sprites
        for(i = 8191; i >= 0; i--) {
          ArrayList<float[]> spriteList = renderList[i];
          if (spriteList.size() > 0) {
            g.setClip(0, 0, 256, (int)roadInfos[i][ROAD_CLIP]);
            for(j = spriteList.size() - 1; j >= 0; j--) {
              float[] sprite = spriteList.get(j);
              g.drawImage(sprites[(int)sprite[OBJ_SPRITE]],
                  (int)sprite[OBJ_SPRITE_X],
                  (int)sprite[OBJ_SPRITE_Y],
                  (int)sprite[OBJ_SPRITE_WIDTH],
                  (int)sprite[OBJ_SPRITE_HEIGHT], null);
            }
          }
        }
        g.setClip(0, 0, 256, 256);

        // draw time bar
        g.setColor(Color.MAGENTA);
        for(i = 0; i < time; i++) {
          x = 127 - (i << 1);
          y = 129 + (i << 1);
          g.drawLine(x, 10, x, 18);
          g.drawLine(y, 10, y, 18);
        }

        // draw progress bar
        y = (int)(0.1f * (player[OBJ_TIME] - 18));
        g.setColor(Color.RED);
        g.drawLine(28, 4, 228, 4);
        g.setColor(wonBlink ? Color.YELLOW : Color.GREEN);
        g.drawLine(28, 4, 28 + y, 4);
        for(i = 0; i < 6; i++) {
          x = 40 * i;
          g.setColor(x <= y ? (wonBlink ? Color.YELLOW : Color.GREEN)
              : Color.RED);
          x += 28;
          g.drawLine(x, 2, x, 6);
        }
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
    final int VK_LEFT = 0x25;
    final int VK_RIGHT = 0x27;
    final int VK_GAS = 0x44;
    final int VK_START = '\n';
    final int VK_A = 0x41;
    final int VK_D = 0x44;

    int k = keyEvent.getKeyCode();
    if (k > 0) {
      if (k == VK_D) {
        k = VK_RIGHT;
      } else if (k == VK_A) {
        k = VK_LEFT;
      }
      a[(k == VK_LEFT || k == VK_RIGHT || k == VK_START) ? k : VK_GAS]
          = keyEvent.getID() != 402;
    }
  }

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame("Out Run 4K");
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
