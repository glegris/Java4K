package java4k.rainbowroad;

/*
 * Rainbow Road
 * Copyright (C) 2013 meatfighter.com
 *
 * This file is part of Rainbow Road.
 *
 * Rainbow Road is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Rainbow Road is distributed in the hope that it will be useful,
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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class a extends Applet implements Runnable {

  // keys
  private boolean[] a = new boolean[65535];
  
  public a() {
    new Thread(this).start();
  }

  @Override
  public void run() {
    
    // Rainbow Road track from Mario Kart 64
    // 8 sprites (player, shell, mushroom, item box, fake item box, thunderbolt,
    //     star, banana peel)
    final String S = "\uff5b\u8dae\uff29\u6c32\u8f32\u7134\u76fe\uff14\u8d2a"
        + "\uff1c\u7688\uff16\u8a24\uff32\u91b4\u71f6\uff1e\u7428\u8b28\uff13"
        + "aaaaaaaaaaaabbaaaaaaaaaabbccaaaaaaaaabccccaaaaaaaabcccccaaaaaaabcccc"
        + "ccaaaaaabcccccccaaaaaabcccccccaaaaaabcccccccaaaaabdbccccccaaaabdddbb"
        + "ccccaaaabdddbbbbbbaaaaabdddbbbbbaaaaaabbddbbbbaaaaaaabdddbbbaaaaaaab"
        + "bddcccaaaaaabcceecccaaaaabccceecccaaaaabccceebbbaaaaabcccebeeeaaaaaa"
        + "bcbbeeeeaaaabbbebeeeeeaaabeeeeeeeeeeabbbbbebbbbbbbbeeeeebbbcccccbeee"
        + "eebbcbbbbbbbbbbbbccbccccbbbbbbbbcbbbbbbbbbbbbbcbbbbbbbbbbbbbccccccab"
        + "bbbbabbbbbbbaaaaaccaaaccccaacbcccaaccbccaccccbbacccbccaccbcccaabcccc"
        + "accbcccaaccbccffbbbbbaffffffaaaaffcaaffffcaffffccafffccfcccccffcffcc"
        + "ffffffccfcffcccccccbbbbaaaffbfaafffbfaaaffffccccccccggggggcggggbbcgg"
        + "gbbgcgggbbgcgggggbcgggggbcggggggcgggggbcgggggbcggggggccccccccccccccc"
        + "ggggggcgggggbcgggggbcggggggcgggggbcgggggbcgggbbgcgggbbgcggggbbcggggg"
        + "gcccccccaaggggaaaggggaagggggaaggggaaggggaaaggggggaggggggaaaagggaaaag"
        + "gaaaaggaaaaggaaaaagaaaaaaaaaaagaaaaaagaaaaaggaaaaagggggggggaggggbgaa"
        + "gggbgaaaggbgaagggggaagggggaggggaaaggaaaaaaaaaagaaaaaggaaaagggaaaaggg"
        + "aaaagbgaaaagbgaaaagbgaaaagggaaaaggggaagggaggggggaaggggaa";
    
    final int KEY_LEFT = 1006;
    final int KEY_RIGHT = 1007;
    final int KEY_X = 120;
    final int KEY_C = 99;
    
    final int ELEMENT_X = 0;
    final int ELEMENT_D = 1;
    final int ELEMENT_SPRITE = 2;
    final int ELEMENT_Z = 3;
    final int ELEMENT_VISIBLE = 4;
    final int ELEMENT_PROJECTED_X = 5;
    final int ELEMENT_PROJECTED_Y = 6;
    final int ELEMENT_PROJECTED_NX = 7;
    final int ELEMENT_PROJECTED_NY = 8; 
    final int ELEMENT_VD = 9;
    final int ELEMENT_VX = 10;
    final int ELEMENT_TIMER = 11;
    final int ELEMENT_ITEM_RANDOMIZER = 12;
    final int ELEMENT_ITEM = 13;
    final int ELEMENT_ITEM_COUNT = 14;
    final int ELEMENT_MUSHROOMING = 15;
    final int ELEMENT_STARING = 16;
    final int ELEMENT_EXPLODING = 17;
    final int ELEMENT_TINYING = 18;
    final int ELEMENT_BANANAING = 19;
    final int ELEMENT_ORBITING = 20;
    final int ELEMENT_PLAYER = 21;
    final int ELEMENT_FALLING = 22;
    final int ELEMENT_ITEM_TRIGGER = 23;
    
    final int SPRITE_PLAYER_0 = 0;
    final int SPRITE_PLAYER_1 = 1;
    final int SPRITE_PLAYER_2 = 2;
    final int SPRITE_PLAYER_3 = 3;
    final int SPRITE_PLAYER_4 = 4;
    final int SPRITE_PLAYER_5 = 5;
    final int SPRITE_PLAYER_6 = 6;
    final int SPRITE_PLAYER_7 = 7;
    final int SPRITE_RED_SHELL = 8;
    final int SPRITE_GREEN_SHELL = 9;
    final int SPRITE_BLUE_SHELL = 10;
    final int SPRITE_MUSHROOM = 11;
    final int SPRITE_ITEM_BOX = 12;    
    final int SPRITE_FAKE_ITEM_BOX = 13;
    final int SPRITE_THUNDERBOLT = 14;    
    final int SPRITE_STAR = 15; 
    final int SPRITE_BANANA = 16; 
    final int SPRITE_NONE = 17;
    
    final int GAME_STATE_ATTRACT_MODE = 0;
    final int GAME_STATE_PLAYING = 1;
    final int GAME_STATE_ENDING = 2;
    
    final int MAP_O = 0;
    final int MAP_U = 1;
    final int MAP_V = 2;
    final int MAP_W = 3;
    
    final int MAP_X = 0;
    final int MAP_Y = 1;
    final int MAP_Z = 2;
    
    final int SCREEN_WIDTH = 800;
    final int SCREEN_HEIGHT = 600;
    final int SCREEN_CENTER_X = SCREEN_WIDTH / 2;
    final int SCREEN_CENTER_Y = SCREEN_HEIGHT / 2;
    
    final int CAMERA_Y = 200;
    final int CAMERA_Z = 800;
    
    final int SCALE = 512;
    final int ROAD_COLORS = 8;
    final int PLAYERS = 8;
    final int HUMAN = 0;
    final int MAP_LENGTH = 707;
    final int SHELL_TIME_OUT = 2048;
    final float HUMAN_EDGE_X = 1.1f;
    final float ENEMY_EDGE_X = 0.95f;
    final float VX = 0.02f;
    final float AD = 0.001f;
    final float MAX_ENEMY_VD = 0.09f;
    final float MAX_VD = 0.1f;
    final float FAST_VD = 0.15f;
    final float SHELL_VD = 0.2f;
    final float ORBIT_VANG = 0.05f;
    final float ORBIT_RADIUS_X = 0.2f;
    final float ORBIT_RADIUS_D = 0.25f;
    final int GAME_RESET_DELAY = 1024;
    
    final Color[] roadColors = new Color[ROAD_COLORS];
    final float[][] players = new float[PLAYERS][]; 
    final ArrayList<float[]>[] shells = new ArrayList[PLAYERS];
    final float[][] onb = new float[4][3];
    final float[][] onb2 = new float[4][3];
    final float[][] onb3 = new float[4][3];
    final int[] polygonXs = new int[4];
    final int[] polygonYs = new int[4];
    final float[][] ps = new float[2][3];
    
    final ArrayList<float[]> elements = new ArrayList<float[]>();
    final BufferedImage[] sprites = new BufferedImage[17];
    final BufferedImage imageBuffer = new BufferedImage(
        800, 600, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = imageBuffer.createGraphics();
    final Font bigFont = g.getFont().deriveFont(Font.BOLD | Font.ITALIC, 90f);
    final Font smallFont = bigFont.deriveFont(45f);
    final AffineTransform defaultTransform = new AffineTransform();
    final AffineTransform transform = new AffineTransform();
    Graphics2D g2 = null;    
    
    int i;
    int j;
    int k;
    int p;
    int q;
    int r;    
    int band0;
    int band1;
    int rank = 0;
    int lap = 0;
    float bandOffset;
    float[] element;        
    boolean releasedC = true;
    boolean flash = true;
    int lightning = 0;
    int gameState = GAME_STATE_ENDING;
    int gameReset = 1;
    int startingLine = 0;
    
    // decompress map XZ's (all flat map)
    float[][][] map = new float[MAP_LENGTH][4][3]; // o, u, v, w vectors
    float vy = -1;
    float py = 1;    
    float px = 0;
    float vx = 0;       
    for(k = i = 0; i < 20; i++) {
      int c = S.charAt(i);
      q = c & 0xFF;
      if ((c >> 8) == 0xFF) {
        // line
        for(j = 0; j < q; j++) {
          px += vx;
          py += vy;
          
          float[][] m = map[k++];
          m[MAP_O][MAP_X] = px;
          m[MAP_O][MAP_Z] = py;          
        }
      } else {
        // arc
        r = (c >> 8) - 128; 
            
        float centerX = -r * vy;
        float centerY = r * vx;

        float X = -centerX;
        float Y = -centerY;

        centerX += px;
        centerY += py;

        int steps = (int)(r * q * 0.02464f);    
        if (steps < 0) {
          steps = -steps;
        }
        for(j = 1; j < steps; j++) {
          float cos = (float)Math.cos(j / (float)r);
          float sin = (float)Math.sin(j / (float)r);
          float X2 = X * cos - Y * sin;
          float Y2 = X * sin + Y * cos;

          vx = centerX + X2 - px;
          vy = centerY + Y2 - py;
          float mag = (float)Math.hypot(vx, vy);
          vx /= mag;
          vy /= mag;

          px = centerX + X2;
          py = centerY + Y2;
          
          float[][] m = map[k++];
          m[MAP_O][MAP_X] = px;
          m[MAP_O][MAP_Z] = py;           
        }
      }
    } 
    
    // add map Ys (elevations)
    for(i = 0; i < MAP_LENGTH; i++) {
      map[i][MAP_O][MAP_Y] = 5 
          * (((float)Math.cos(0.088871f * i - 0.6f)) - 1);
    }
    
    // compute map ONBs
    for(i = 0; i < MAP_LENGTH; i++) {
      float[][] m0 = map[i];
      float[][] m1 = map[(i + 1) % MAP_LENGTH];
      float x = m0[MAP_O][MAP_X] - m1[MAP_O][MAP_X];
      float y = m0[MAP_O][MAP_Y] - m1[MAP_O][MAP_Y];
      float z = m0[MAP_O][MAP_Z] - m1[MAP_O][MAP_Z];
      float mag = (float)Math.sqrt(x * x + y * y + z * z);
      x /= mag;
      y /= mag;
      z /= mag;
      m0[MAP_W][MAP_X] = x;
      m0[MAP_W][MAP_Y] = y;
      m0[MAP_W][MAP_Z] = z;
      m0[MAP_U][MAP_X] = z;
      m0[MAP_U][MAP_Z] = -x;
      m0[MAP_V][MAP_X] = -x * y;
      m0[MAP_V][MAP_Y] = x * x + z * z;
      m0[MAP_V][MAP_Z] = -y * z;
    }   
    
    // decompress the sprites
    for(i = 0; i < 17; i++) {
      if (i < 8) {
        sprites[i] = new BufferedImage(28, 30, BufferedImage.TYPE_INT_ARGB_PRE);
        p = Color.getHSBColor(i / 8f, 1, 1).hashCode();
        for(j = 0; j < 30; j++) {
          for(k = 0; k < 14; k++) {
            q = S.charAt(20 + j * 14 + k);
            if (q == 'a') {
              q = 0;
            } else if (q == 'b') {
              q = 0xFF000000;
            } else if (q == 'c') {
              q = p;
            } else if (q == 'd') {
              q = 0xffe0a888;
            } else {
              q = 0xff284848;
            }
            sprites[i].setRGB(k, j, q);
            sprites[i].setRGB(27 - k, j, q);
          }
        }
      } else {
        sprites[i] = new BufferedImage(14, 12, BufferedImage.TYPE_INT_ARGB_PRE);
        for(j = 0; j < 12; j++) {
          for(k = 0; k < 7; k++) {
            q = S.charAt((i < 11 ? 440 : (i * 84 - 400)) + j * 7 + k);
            if (q == 'a') {
              q = 0;
            } else if (q == 'b') {
              q = 0xFF000000;
            } else if (q == 'c') {
              q = i == 9 ? 0xFF00FF00 : i == 10 ? 0xFF7F00FF : 0xFFFF0000;
            } else if (q == 'f') {
              q = 0xFFFFFFFF;
            } else {
              q = 0xFFFFFF00;
            }
            sprites[i].setRGB(k, j, q);
            sprites[i].setRGB(13 - k, j, q);
          }
        }
      }
    }
    
    long nextFrameStartTime = 0;
    while(true) {

      do {
        nextFrameStartTime += 10000000; // 100 frames per second                              

        // -- update starts ----------------------------------------------------
                
        if (gameState == GAME_STATE_ENDING && --gameReset == 0) {
          // reset game (also happens on start up)
          gameState = GAME_STATE_ATTRACT_MODE;
          rank = 0;
          lap = 1;
          
          // initialize players  
          elements.clear();
          for(i = 0; i < PLAYERS; i++) {
            
            if (i < 7) {
              // initialize item boxes
              for(j = 0; j < 4; j++) {
                element = new float[32];
                elements.add(element);
                element[ELEMENT_X] = 0.75f - j * 0.5f;
                element[ELEMENT_D] = (i + 1) * MAP_LENGTH >> 3;
                element[ELEMENT_SPRITE] = SPRITE_ITEM_BOX;
              }
            }
            
            players[i] = new float[32];
            elements.add(players[i]);
            players[i][ELEMENT_X] = 0;
            players[i][ELEMENT_D] = i << 1;
            players[i][ELEMENT_SPRITE] = i;
            players[i][ELEMENT_ITEM] = SPRITE_NONE;
            shells[i] = new ArrayList<float[]>();
            
            // generate road colors
            roadColors[i] = new Color(
                Color.getHSBColor(i / (float)ROAD_COLORS, 1, 1).hashCode() 
                    & 0x80FFFFFF, true);
          } 
          nextFrameStartTime = System.nanoTime();
        }
        
        if (gameState == GAME_STATE_ATTRACT_MODE && a[KEY_X]) {
          gameState = GAME_STATE_PLAYING;
        } 
        if (gameState == GAME_STATE_PLAYING) {          
          if (lap == 4) {
            gameState = GAME_STATE_ENDING;
            gameReset = GAME_RESET_DELAY;
          }
        } else {
          continue;
        }
        
        // update flashing starting line
        startingLine = (startingLine + 1) & 15;
        
        // update other players
        lap = 1 + (int)(players[HUMAN][ELEMENT_D] / MAP_LENGTH);
        r = rank;
        rank = 1;        
        for(i = 0; i < 8; i++) {
          if (i != 0) {
            
            // compute rank
            if (players[i][ELEMENT_D] > players[HUMAN][ELEMENT_D]) {
              rank++;
            }
            
            if (players[i][ELEMENT_VD] < (players[i][ELEMENT_MUSHROOMING] > 0 
                  || players[i][ELEMENT_STARING] > 0 
                      ? FAST_VD : players[i][ELEMENT_TINYING] > 0 
                          ? MAX_ENEMY_VD / 2 : 
                    r < 4 ? MAX_VD : MAX_ENEMY_VD)
                && players[i][ELEMENT_BANANAING] == 0) {
              players[i][ELEMENT_VD] += AD;
            } else {
              players[i][ELEMENT_VD] -= AD;
            }
            if (players[i][ELEMENT_EXPLODING] > 0) {
              players[i][ELEMENT_VD] = 0;
            }
            players[i][ELEMENT_D] += players[i][ELEMENT_VD];
            players[i][ELEMENT_X] += players[i][ELEMENT_VX];
            if (players[i][ELEMENT_TIMER] == 0) {
              players[i][ELEMENT_TIMER] = 100 
                  + (int)(400 * (float)Math.random());
              players[i][ELEMENT_VX] 
                  = (((float)Math.random() * 2 * ENEMY_EDGE_X - ENEMY_EDGE_X) 
                      - players[i][ELEMENT_X]) / players[i][ELEMENT_TIMER];
            } else {
              players[i][ELEMENT_TIMER]--;
            }
          }        
          
          if (players[i][ELEMENT_ITEM_RANDOMIZER] > 0) {
            if (--players[i][ELEMENT_ITEM_RANDOMIZER] % 10 == 0) {
              // item randomly selected
              // human always gets mushroom if in 8th on lap 1,
              //     >= 6th on lap 2, >= 4th on lap 3
              players[i][ELEMENT_ITEM_COUNT] = 1;
              players[i][ELEMENT_ITEM] = 
                  i == HUMAN && r > 9 - (lap << 1)
                      && players[i][ELEMENT_ITEM_RANDOMIZER] == 0 
                          ? SPRITE_MUSHROOM 
                              : 8 + (int)(9 * (float)Math.random());  
              boolean rareItem = players[i][ELEMENT_ITEM] == SPRITE_BLUE_SHELL
                  || players[i][ELEMENT_ITEM] == SPRITE_THUNDERBOLT
                  || players[i][ELEMENT_ITEM] == SPRITE_STAR;
              if (players[i][ELEMENT_ITEM_RANDOMIZER] == 0
                  && (players[i][ELEMENT_ITEM] == SPRITE_ITEM_BOX
                      || (r == 1 && i == HUMAN 
                          && (rareItem 
                              || players[i][ELEMENT_ITEM] == SPRITE_MUSHROOM))
                      || (rareItem && (int)(7 * (float)Math.random()) != 3))) {
                // Fix the odds by making blue shells, thunderbolts 
                // and stars rare. Also, the human player cannot get any of 
                // those items or the mushroom if he is in 1st.
                players[i][ELEMENT_ITEM_RANDOMIZER] = 1;
              }      
              if (players[i][ELEMENT_ITEM] == SPRITE_MUSHROOM) {
                j = (int)(7 * (float)Math.random());
                players[i][ELEMENT_ITEM_COUNT] = j == 0 ? 10 : j == 1 ? 3 : 1;
              } else if (players[i][ELEMENT_ITEM] == SPRITE_BANANA) {
                players[i][ELEMENT_ITEM_COUNT] 
                    = 1 + 4 * (int)(2 * (float)Math.random());
              } else if (players[i][ELEMENT_ITEM] < SPRITE_ITEM_BOX
                  && players[i][ELEMENT_ITEM] != SPRITE_BLUE_SHELL) {
                players[i][ELEMENT_ITEM_COUNT] 
                    = 1 + 2 * (int)(2 * (float)Math.random());
              }
            }
          }
                    
          if (((i == 0 && releasedC && a[KEY_C]) 
              || (i > 0 && players[i][ELEMENT_ITEM_TRIGGER] == 1))
              && players[i][ELEMENT_EXPLODING] == 0
              && players[i][ELEMENT_FALLING] == 0) {          
            // player triggers carried-item (launches weapon)
            if (i == 0) {
              releasedC = false;
            }
            if (shells[i].size() > 0) {
              // player launches orbiting shell
              element = shells[i].remove(0);
              element[ELEMENT_ORBITING] = 0;
              element[ELEMENT_D] = players[i][ELEMENT_D] + 0.6f;
              element[ELEMENT_TIMER] = SHELL_TIME_OUT;
              if (element[ELEMENT_X] < -ENEMY_EDGE_X) {
                element[ELEMENT_X] = -ENEMY_EDGE_X;
              }
              if (element[ELEMENT_X] > ENEMY_EDGE_X) {
                element[ELEMENT_X] = ENEMY_EDGE_X;
              }
            } else if (players[i][ELEMENT_ITEM_RANDOMIZER] > 0) {
              players[i][ELEMENT_ITEM_RANDOMIZER] = 1;
            } else {
              j = (int)players[i][ELEMENT_ITEM];
              if (j != SPRITE_NONE) {

                if (j == SPRITE_MUSHROOM) {
                  // player uses mushroom (speed up player)
                  players[i][ELEMENT_MUSHROOMING] = 200;                
                } else if (j == SPRITE_STAR) {
                  // player uses star (speed up player and make invincible/bomb)
                  players[i][ELEMENT_STARING] = 800;              
                } else if (j == SPRITE_THUNDERBOLT) {
                  // player uses thunderbolt (shrink other players)
                  lightning = 50;
                  for(k = 0; k < PLAYERS; k++) {
                    if (k != i && players[k][ELEMENT_STARING] == 0) {
                      players[k][ELEMENT_TINYING] = 500;
                    }
                  }
                } else if (j < SPRITE_MUSHROOM
                    && players[i][ELEMENT_ITEM_COUNT] == 3) {
                  // create orbiting shells
                  players[i][ELEMENT_ITEM_COUNT] = 1;
                  for(k = 0; k < 3; k++) {
                    element = new float[32];
                    elements.add(element);
                    element[ELEMENT_X] = players[i][ELEMENT_X];
                    element[ELEMENT_D] = players[i][ELEMENT_D] + 0.5f;
                    element[ELEMENT_SPRITE] = j;
                    element[ELEMENT_TIMER] = k * 2.09f;
                    element[ELEMENT_PLAYER] = i;
                    element[ELEMENT_ORBITING] = 1;
                    shells[i].add(element);
                  }
                } else {              
                  element = new float[32];
                  elements.add(element);
                  element[ELEMENT_X] = players[i][ELEMENT_X];
                  element[ELEMENT_D] = players[i][ELEMENT_D] +
                          (j < SPRITE_MUSHROOM ? 0.6f : -0.5f);
                  element[ELEMENT_SPRITE] = j;
                  element[ELEMENT_TIMER] = SHELL_TIME_OUT;
                }

                if (--players[i][ELEMENT_ITEM_COUNT] == 0) {
                  players[i][ELEMENT_ITEM] = SPRITE_NONE;
                }
              }
            }
          }          
          
          if (players[i][ELEMENT_MUSHROOMING] > 0) {
            players[i][ELEMENT_MUSHROOMING]--;            
          }
          if (players[i][ELEMENT_STARING] > 0) {
            players[i][ELEMENT_STARING]--;            
          }
          if (players[i][ELEMENT_EXPLODING] > 0) {
            players[i][ELEMENT_EXPLODING]--;            
          }
          if (players[i][ELEMENT_TINYING] > 0) {
            players[i][ELEMENT_TINYING]--;
          }
          if (players[i][ELEMENT_FALLING] > 0) {
            if (players[i][ELEMENT_FALLING]++ > 50) {
              if (players[i][ELEMENT_X] > 2 * VX) {
                players[i][ELEMENT_X] -= VX;
              } else if (players[i][ELEMENT_X] < -2 * VX) {
                players[i][ELEMENT_X] += VX;
              } else {
                players[i][ELEMENT_FALLING] = 0;                
              }
            }            
          }
          if (players[i][ELEMENT_BANANAING] > 0) {
            players[i][ELEMENT_BANANAING]--;            
            players[i][ELEMENT_X] 
                += players[i][ELEMENT_VD] * 0.4f 
                    * (float)Math.cos(0.125f * players[i][ELEMENT_BANANAING]);
            if (i != HUMAN) {
              if (players[i][ELEMENT_X] < -ENEMY_EDGE_X) {
                players[i][ELEMENT_X] = -ENEMY_EDGE_X;
              }
              if (players[i][ELEMENT_X] > ENEMY_EDGE_X) {
                players[i][ELEMENT_X] = ENEMY_EDGE_X;              
              }
            }
          }
          if (players[i][ELEMENT_ITEM_TRIGGER]-- == 0) {
            players[i][ELEMENT_ITEM_TRIGGER] 
                = 99 + (int)(300 * (float)Math.random());
          }          
        }        
        
        float maxVd = MAX_VD;
        if (players[HUMAN][ELEMENT_MUSHROOMING] > 0
            || players[HUMAN][ELEMENT_STARING] > 0) {
          maxVd = FAST_VD;
        }
        if (players[HUMAN][ELEMENT_EXPLODING] > 0) {
          maxVd = 0;
          players[HUMAN][ELEMENT_VD] = 0;
        }
        if (players[HUMAN][ELEMENT_TINYING] > 0) {
          maxVd /= 2;
        }
        
        // player moves left and right
        if (players[HUMAN][ELEMENT_VD] > 0 
            && players[HUMAN][ELEMENT_FALLING] == 0) {
          if (a[KEY_LEFT]) {          
            players[HUMAN][ELEMENT_X] -= VX;
          } else if (a[KEY_RIGHT]) {
            players[HUMAN][ELEMENT_X] += VX;
          }
        }
        
        // player accelerates
        if (a[KEY_X] && players[HUMAN][ELEMENT_BANANAING] == 0) {
          if (players[HUMAN][ELEMENT_VD] < maxVd) {
            players[HUMAN][ELEMENT_VD] += AD;
          }
          if (players[HUMAN][ELEMENT_VD] > maxVd) {
            players[HUMAN][ELEMENT_VD] -= AD;
          }
        } else {
          if (players[HUMAN][ELEMENT_VD] > 0) {
            players[HUMAN][ELEMENT_VD] -= AD;
          }
          if (players[HUMAN][ELEMENT_VD] < 0) {
            players[HUMAN][ELEMENT_VD] = 0;
          }
        }
        
        // player advances
        players[HUMAN][ELEMENT_D] += players[HUMAN][ELEMENT_VD];
        
        // player affected by centrifugal force
        if (players[HUMAN][ELEMENT_FALLING] == 0) {
          band0 = ((int)players[HUMAN][ELEMENT_D]) % MAP_LENGTH;        
          band1 = (band0 + 1) % MAP_LENGTH;
          players[HUMAN][ELEMENT_X] 
              += ((map[band1][MAP_W][MAP_X] * map[band0][MAP_W][MAP_Z] 
                  - map[band0][MAP_W][MAP_X] * map[band1][MAP_W][MAP_Z]) 
                      * 0.25f * players[HUMAN][ELEMENT_VD] / MAX_VD)
                  * ((players[HUMAN][ELEMENT_MUSHROOMING] > 0 
                      || players[HUMAN][ELEMENT_STARING] > 0) ? 0.25f : 0.9f);
        }
        
        // player restricted to the track
        if ((players[HUMAN][ELEMENT_X] < -HUMAN_EDGE_X 
            || players[HUMAN][ELEMENT_X] > HUMAN_EDGE_X)
              && players[HUMAN][ELEMENT_FALLING] == 0) {
          players[HUMAN][ELEMENT_FALLING] = 1;
          players[HUMAN][ELEMENT_VD] = 0;
          elements.removeAll(shells[HUMAN]);
          shells[HUMAN].clear();
        } 
        
        // clear release key
        if (!(releasedC  || a[KEY_C])) {
          releasedC = true;
        }
        
        // update objects
        for(k = elements.size() - 1; k >= 0; k--) {
          element = elements.get(k);

          // test for collision between objects and players
          for(i = 0; i < 8; i++) {
            if (element != players[i] && players[i][ELEMENT_EXPLODING] == 0
                && element[ELEMENT_EXPLODING] == 0) {
              float dx = players[i][ELEMENT_X] - element[ELEMENT_X];
              float dd = players[i][ELEMENT_D] - element[ELEMENT_D];
              if (dx < 0) {
                dx = -dx;
              }
              if (dd < 0) {
                dd = -dd;
              }
              dd %= MAP_LENGTH;
              if (dx <= 0.2f && dd <= 0.4f) {                
                if (element[ELEMENT_SPRITE] == SPRITE_ITEM_BOX) {
                  if (players[i][ELEMENT_ITEM_RANDOMIZER] == 0
                      && players[i][ELEMENT_ITEM] == SPRITE_NONE) {
                    players[i][ELEMENT_ITEM_RANDOMIZER] = 300;
                  }
                } else if (element[ELEMENT_SPRITE] > SPRITE_PLAYER_7) {
                  // player hit banana, fake item box or shell                  
                  if (element[ELEMENT_SPRITE] == SPRITE_BANANA) {
                    if (players[i][ELEMENT_STARING] == 0) {
                      players[i][ELEMENT_BANANAING] = 100;
                    }
                    elements.remove(k);
                  } else if (element[ELEMENT_ORBITING] == 0 
                        || element[ELEMENT_PLAYER] != i) {
                    // make sure player is immune to his own orbiting shell
                    if (players[i][ELEMENT_STARING] == 0) {
                      players[i][ELEMENT_EXPLODING] = 200;
                    }
                    if (element[ELEMENT_ORBITING] == 1) {
                      shells[(int)element[ELEMENT_PLAYER]].remove(element);
                    }
                    if (element[ELEMENT_SPRITE] != SPRITE_BLUE_SHELL) {
                      elements.remove(k);
                    }
                  }       
                } else if (element[ELEMENT_STARING] > 0
                    || players[i][ELEMENT_TINYING] > 0) {
                  // player hit star-player and potentially explodes
                  if (players[i][ELEMENT_STARING] == 0) {
                    players[i][ELEMENT_EXPLODING] = 200;
                  }
                } else if (players[i][ELEMENT_STARING] == 0 
                    && element[ELEMENT_TINYING] == 0
                    && players[i][ELEMENT_D] < element[ELEMENT_D]) {
                  // player bumped into another player
                  players[i][ELEMENT_VD] *= 0.5f;
                }
              }
            }
          }
          
          // update shells
          if (element[ELEMENT_SPRITE] > SPRITE_PLAYER_7 
              && element[ELEMENT_SPRITE] < SPRITE_MUSHROOM) {
            
            if (element[ELEMENT_ORBITING] == 1) {
              // shell orbits around associated player
              element[ELEMENT_TIMER] += ORBIT_VANG;
              element[ELEMENT_X] 
                  = players[(int)element[ELEMENT_PLAYER]][ELEMENT_X]
                      + ORBIT_RADIUS_X 
                          * (float)Math.cos(element[ELEMENT_TIMER]);
              element[ELEMENT_D] 
                  = players[(int)element[ELEMENT_PLAYER]][ELEMENT_D]
                      + ORBIT_RADIUS_D 
                          * (float)Math.sin(element[ELEMENT_TIMER]);
            } else if (element[ELEMENT_TIMER] == 0) {
              // shells expire after a timeout
              elements.remove(k);
            } else {
              // move forward at fixed rate
              element[ELEMENT_D] += SHELL_VD;
              
              element[ELEMENT_TIMER]--;

              if (element[ELEMENT_SPRITE] == SPRITE_RED_SHELL) {
                // red shell home in on the opponent whose rank is one higher                
                float best = 1024;
                j = 0;
                for(i = 0; i < PLAYERS; i++) {
                  float dd = players[i][ELEMENT_D] - element[ELEMENT_D];                
                  if (dd >= 0 && dd < best) {
                    best = dd;
                    j = i;
                  }
                }
                if (element[ELEMENT_X] < players[j][ELEMENT_X]) {
                  element[ELEMENT_X] += VX;
                } else {
                  element[ELEMENT_X] -= VX;
                }
              } else if (element[ELEMENT_SPRITE] == SPRITE_BLUE_SHELL) { 
                // blue shell home in on the opponent whose rank is 1st
                j = 0;
                for(i = 0; i < PLAYERS; i++) {
                  if (players[i][ELEMENT_D] > players[j][ELEMENT_D]) {
                    j = i;
                  }
                }
                if (element[ELEMENT_X] < players[j][ELEMENT_X]) {
                  element[ELEMENT_X] += VX;
                } else {
                  element[ELEMENT_X] -= VX;
                }
              }
            }
          }
        }
        
        if (lightning > 0) {
          lightning--;
        }
               
        // -- update ends ------------------------------------------------------

      } while(nextFrameStartTime < System.nanoTime());

      // -- render starts ------------------------------------------------------

      // clear frame
      g.setColor(((lightning >> 1) & 1) == 1 ? Color.WHITE : Color.BLACK);
      g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);            

      bandOffset = players[HUMAN][ELEMENT_D] % 1;
      band0 = ((int)players[HUMAN][ELEMENT_D]) % MAP_LENGTH;        
      band1 = (band0 + 1) % MAP_LENGTH;

      // interpolate band end-points to create orthonormal basis vectors
      for(i = 0; i < 4; i++) {
        for(j = 0; j < 3; j++) {
          onb[i][j] = map[band0][i][j] 
              + bandOffset * (map[band1][i][j] - map[band0][i][j]);
        }
      }        

      // adjust for X
      for(i = 0; i < 3; i++) {
        onb[MAP_O][i] += players[HUMAN][ELEMENT_X] * onb[MAP_U][i];        
      }      
      
      // draw road
      for(k = MAP_LENGTH; k >= 0; k--) {
        int band = k % MAP_LENGTH;
        float[][] m = map[band];
        
        // loop over both end-points
        for(p = 0; p < 2; p++) {
          
          // compute end-point of the band relative to player-origin
          for(i = 0; i < 3; i++) {
            ps[0][i] = SCALE * (m[MAP_O][i] - onb[MAP_O][i] 
                + m[MAP_U][i] * ((p << 1) - 1)); 
            ps[1][i] = 0;
          }
          
          // apply inverse ONB (ONB transpose) to end-point
          for(i = 0; i < 3; i++) {
            for(j = 0; j < 3; j++) {
              ps[1][j] += onb[j + 1][i] * ps[0][i]; 
            }
          }
          
          // move point away from camera
          ps[1][MAP_Y] -= CAMERA_Y;
          ps[1][MAP_Z] -= CAMERA_Z;
          
          // project point from 3D to 2D          
          if (ps[1][MAP_Z] > CAMERA_Z) {
            polygonXs[0] = 0;
            polygonXs[1] = SCREEN_WIDTH;
            polygonYs[0] = 2 * SCREEN_HEIGHT;
            polygonYs[1] = 2 * SCREEN_HEIGHT;
            break;
          } else {
            float K = 1.5f * CAMERA_Z / (CAMERA_Z - ps[1][MAP_Z]);
            polygonXs[p] = SCREEN_CENTER_X + (int)(K * ps[1][MAP_X]);
            polygonYs[p] = SCREEN_CENTER_Y - (int)(K * ps[1][MAP_Y]);
          }
        }
                
        if (polygonYs[0] > -128
            && polygonYs[1] > -128
            && polygonYs[2] > -128
            && polygonYs[3] > -128) {
          // draw road band polygon
          g.setColor(roadColors[
              k == 0 ? (startingLine >> 1) : (band % ROAD_COLORS)]);
          g.fillPolygon(polygonXs, polygonYs, 4);
        }
        
        // shift end-points for next polygon
        polygonXs[2] = polygonXs[1];
        polygonYs[2] = polygonYs[1];
        polygonXs[3] = polygonXs[0];
        polygonYs[3] = polygonYs[0];
      }   
      
      // project game element sprites from 3D to 2D
      for(k = elements.size() - 1; k >= 0; k--) {
        
        element = elements.get(k);
        
        bandOffset = element[ELEMENT_D] % 1;
        band0 = ((int)element[ELEMENT_D]) % MAP_LENGTH;        
        band1 = (band0 + 1) % MAP_LENGTH;

        // interpolate band end-points to create orthonormal basis vectors
        for(i = 0; i < 4; i++) {
          for(j = 0; j < 3; j++) {
            onb2[i][j] = map[band0][i][j] 
                + bandOffset * (map[band1][i][j] - map[band0][i][j]);
            onb3[i][j] = 0;
          }
        }
        
        // adjust for X, center around player and scale
        for(i = 0; i < 3; i++) {
          onb2[MAP_O][i] += element[ELEMENT_X] * onb2[MAP_U][i]
              - onb[MAP_O][i];
          onb2[MAP_O][i] *= SCALE;
        }     
        
        // apply player ONB to element ONB
        for(i = 0; i < 4; i++) {                          
          for(j = 0; j < 3; j++) {                        
            for(p = 0; p < 3; p++) {                      
              onb3[i][j] += onb[j + 1][p] * onb2[i][p];
            }
          }
        }
        
        // move element away from camera
        onb3[MAP_O][MAP_Z] -= CAMERA_Z;
        onb3[MAP_O][MAP_Y] -= CAMERA_Y;        
        
        // project element
        element[ELEMENT_VISIBLE] = 0;
        float K = 1.5f * CAMERA_Z / (CAMERA_Z - onb3[MAP_O][MAP_Z]);
        if (K > 0) {
          element[ELEMENT_PROJECTED_X] = SCREEN_CENTER_X 
              + K * onb3[MAP_O][MAP_X];
          element[ELEMENT_PROJECTED_Y] = SCREEN_CENTER_Y 
              - K * onb3[MAP_O][MAP_Y];
          
          element[ELEMENT_Z] = onb3[MAP_O][MAP_Z];
          for(j = 0; j < 3; j++) {
            onb3[MAP_O][j] += onb3[MAP_V][j];
          }
          
          // capture normal vector
          float mag = K;
          K = 1.5f * CAMERA_Z / (CAMERA_Z - onb3[MAP_O][MAP_Z]);          
          if (K > 0) {            
            element[ELEMENT_PROJECTED_NX] = SCREEN_CENTER_X 
                + K * onb3[MAP_O][MAP_X] - element[ELEMENT_PROJECTED_X];
            element[ELEMENT_PROJECTED_NY] = SCREEN_CENTER_Y 
                - K * onb3[MAP_O][MAP_Y] - element[ELEMENT_PROJECTED_Y];
            mag /= (float)Math.hypot(
                element[ELEMENT_PROJECTED_NX], element[ELEMENT_PROJECTED_NY]);
                        
            element[ELEMENT_PROJECTED_NY] *= mag;
            element[ELEMENT_PROJECTED_NX] *= mag;            
            element[ELEMENT_VISIBLE] = 1;
          }
        }
      }
      
      // insertion sort elements by Z
      for(k = elements.size() - 1; k > 0; k--) {
        element = elements.get(k);
        p = k;
        for(j = k - 1; j >= 0; j--) {
          float[] element2 = elements.get(j);
          if (element2[ELEMENT_Z] < element[ELEMENT_Z]) {
            p = j;
          }
        }
        elements.set(k, elements.get(p));
        elements.set(p, element);
      }
      
      // draw element sprites
      flash = !flash;
      for(k = elements.size() - 1; k >= 0; k--) {
        element = elements.get(k);
        if (element[ELEMENT_VISIBLE] == 1) {        
          transform.setTransform(
              -element[ELEMENT_PROJECTED_NY], element[ELEMENT_PROJECTED_NX], 
              element[ELEMENT_PROJECTED_NX], -element[ELEMENT_PROJECTED_NY], 
              element[ELEMENT_PROJECTED_X], element[ELEMENT_PROJECTED_Y]);
          if (element[ELEMENT_EXPLODING] > 0) {
            // player hops in the air exploding
            transform.translate(0, (element[ELEMENT_EXPLODING] / 25 - 8) 
                * element[ELEMENT_EXPLODING]);
            transform.rotate(-0.063f * element[ELEMENT_EXPLODING]);            
          } else if (element[ELEMENT_FALLING] > 0) {
            // players falls off of track
            transform.translate(0, 
                0.5f * element[ELEMENT_FALLING] * element[ELEMENT_FALLING]);
          }
          if (element[ELEMENT_TINYING] > 0) {
            transform.scale(0.25f, 0.25f);
          }
          g.setTransform(transform);
          if (element[ELEMENT_SPRITE] < 8) {
            g.drawImage(sprites[element[ELEMENT_STARING] == 0 
                ? (int)element[ELEMENT_SPRITE] : (startingLine >> 1)], 
                    -56, -120, 112, 120, null);            
          } else {            
            g.drawImage(sprites[(int)element[ELEMENT_SPRITE]], 
                -28, -48, 56, 48, null);
          }
          g.setTransform(defaultTransform);
        } 
      }
      
      if (gameState != GAME_STATE_ATTRACT_MODE) {
        // display rank
        g.setFont(bigFont);
        g.setColor(lap == 4 && flash ? Color.MAGENTA : Color.YELLOW);
        g.drawString(String.format("%d%s", rank, 
            rank == 1 ? "st" : rank == 2 ? "nd" : rank == 3 ? "rd" : "th"),
            48, 512);  

        if (lap != 4) {
          // display lap
          g.setFont(smallFont);
          g.setColor(Color.MAGENTA);
          g.drawString(String.format("%d/3", lap), 48, 80);
        }

        // draw item
        i = (int)players[HUMAN][ELEMENT_ITEM];
        if (i != SPRITE_NONE) {      
          g.setColor(Color.BLACK);
          g.fillRect(304, 16, 192, 64);
          g.setColor(Color.WHITE);
          g.drawRect(304, 16, 192, 64);
          for(j = 3; j < players[HUMAN][ELEMENT_ITEM_COUNT]; j++) {
            g.drawImage(sprites[i], 280 + 15 * j, 24, 56, 48, null);
          }                    
          if (players[HUMAN][ELEMENT_ITEM_COUNT] > 1) {
            g.drawImage(sprites[i], 308, 24, 56, 48, null);                    
          }
          g.drawImage(sprites[i], 372, 24, 56, 48, null);
          if (players[HUMAN][ELEMENT_ITEM_COUNT] > 2) {
            g.drawImage(sprites[i], 436, 24, 56, 48, null);
          }          
        }
      }
      
      // -- render ends --------------------------------------------------------

      // show the hidden buffer
      if (g2 == null) {
        g2 = (Graphics2D)getGraphics();        
      } else {
        g2.drawImage(imageBuffer, 0, 0, null);
      }

      // burn off extra cycles
      while(nextFrameStartTime > System.nanoTime());      
    }
  }
  
  @Override
  public boolean handleEvent(Event e) { 
    return a[e.key] = e.id == 401 || e.id == 403;
  }  

  // to run in window, uncomment below
  /*public static void main(String[] args) throws Throwable {
    javax.swing.JFrame frame = new javax.swing.JFrame(
        "Rainbow Road");
    frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    a applet = new a();
    applet.setPreferredSize(new java.awt.Dimension(800, 600));
    frame.setContentPane(applet);
    frame.setResizable(false); 
    frame.pack();      
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    Thread.sleep(250);
    applet.start();
    applet.requestFocus();
  }*/
}
