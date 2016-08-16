package java4k.s23;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class A extends Applet implements Runnable {
	static boolean keys[] = new boolean[65536];
	final static double PI = 3.1415;
	final static int enemynum = 80;
	final static int bulletnum = 8000;
	final static int itemnum = 8000;
	final static int grazenum = 2000;

	public void start() {
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		new Thread(this).start();
	}

	public void run() {
		boolean newGame = true;
		for (;;) {
			if (newGame) {
				l();
				newGame = false;
			}

			if (keys[KeyEvent.VK_R])
				newGame = true;

			if (!isActive())
				return;
		}
	}

	public void l() {
		setSize(480, 480); //delete

		Random rndLvl = new Random(23), rndItemStar = new Random(1337);

		BufferedImage screen = new BufferedImage(480, 480, BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletGraphics = getGraphics();

		//Variables
		//---------

		int playerX = 225, playerY = 400, shotY = -30, playerSpeed = 1, lastShot = -1380, lives = 3, lastDeath = -120, score = 0, multiplier = 1, nextEnemy = 0, nextEnemyDiff = 180, i, j, stari;
		double t1, t2;

		//x - y - state - shootdelay
		int[][] enemy = new int[enemynum][4];
		int[] enemyStack = new int[enemynum];
		int enemyStacki = enemynum;

		double[][] enemyB = new double[enemynum][9];

		//bullet: x - y - angle1 - speed1 - delay1 - homing/angle2 - speed2 - delay2
		double[][] bullet = new double[bulletnum][8];
		//state - smooth - grazed
		int[][] ibullet = new int[bulletnum][3];
		int[] bulletStack = new int[bulletnum];
		int bulletStacki = bulletnum;

		//x - y - xspeed - yspeed - xacc - yacc
		double[][] item = new double[itemnum][6];
		int itemStack[] = new int[itemnum];
		int itemStacki = itemnum;

		int item2[] = { 0, 490, 600, 1 };

		//x - y - xspeed - yspeed - state
		double[][] graze = new double[grazenum][5];
		int[] grazeStack = new int[grazenum];
		int grazeStacki = grazenum;

		double star[][] = new double[75][2];

		for (i = 0; i < itemnum; ++i) {
			if (i < enemynum) {
				enemy[i][2] = 0;
				enemyStack[i] = i;
			}
			if (i < bulletnum) {
				ibullet[i][0] = 0;
				bulletStack[i] = i;
			}
			if (i < grazenum) {
				graze[i][4] = 0;
				grazeStack[i] = i;
			}
			item[i][1] = -10;
			itemStack[i] = i;
		}

		//Graphics
		//--------

		Graphics g2;
		BufferedImage gplayer = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
		BufferedImage genemy = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
		BufferedImage gitem = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		BufferedImage gbullet = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
		BufferedImage gbg = new BufferedImage(480, 480, BufferedImage.TYPE_INT_RGB);

		Color bgCol[] = { new Color(64, 20, 64), new Color(160, 60, 160), new Color(112, 112, 140) };

		//Player and Enemy
		long playerG = 35466898305473L, enemyG = 562949815469064L;
		for (i = 6; i >= 0; --i)
			for (j = 6; j >= 0; --j) {
				g2 = gplayer.getGraphics();
				g2.setColor(new Color(128, 0, 16));
				if (playerG % 2 == 1)
					g2.fillRect(4 * j, 4 * i, 4, 4);
				playerG /= 2;

				g2.setColor(new Color(16, 128, 64));
				g2.fillRect(12, 8, 4, 4);

				g2 = genemy.getGraphics();
				g2.setColor(new Color(16, 128, 64));
				if (enemyG % 2 == 1)
					g2.fillRect(4 * j, 4 * i, 4, 4);
				enemyG /= 2;
			}

		//Items
		g2 = gitem.getGraphics();
		g2.setColor(Color.blue);
		g2.fillRect(0, 0, 10, 10);

		//Bullet
		g2 = gbullet.getGraphics();
		for (i = 0; i < 10; i++) {
			g2.setColor(Color.getHSBColor(0f, 0.625f - i / 20f, 0.75f + i / 40f));
			g2.fillOval(i, i, 20 - 2 * i, 20 - 2 * i);
		}

		//Stars
		for (i = 0; i < 75; ++i) {
			star[i][0] = rndItemStar.nextInt(480);
			star[i][1] = rndItemStar.nextInt(480);
		}

		g2 = gbg.getGraphics();
		g2.setColor(Color.black);
		g2.fillRect(0, 0, 480, 480);

		for (stari = 700; stari > 0; --stari) {
			t2 = rndItemStar.nextDouble() * 320 - 60;
			t1 = t2 * t2 * 0.0034;

			i = (int) t2 + 60;
			t1 += (rndItemStar.nextInt(i) - i / 2) / 1.5;

			g2.setColor(Color.getHSBColor(0.9f, 0.4f, 0.5f + i / 640f));
			g2.fillRect((int) t1 + 160, (int) t2 + 220, 1, 1);
		}

		for (stari = 1200; stari > 0; --stari) {
			i = rndItemStar.nextInt(300);
			int d = i - 150;
			if (d < 0)
				d *= -1;
			d = 180 - d;

			j = i + rndItemStar.nextInt(d) - d / 2;
			g2.setColor(bgCol[rndItemStar.nextInt(2)]);
			if (rndItemStar.nextInt(2) == 0)
				g2.setColor(Color.getHSBColor(0.8f, 0.2f, d / 200f)); //200 = 180 + something
			g2.fillRect(i + 50, j + 50, 1, 1);

			if (rndItemStar.nextInt(3) == 0) {
				d /= 3;
				j = i + rndItemStar.nextInt(d) - d / 2;
				g2.setColor(bgCol[2]);
				g2.fillRect(i + 50, j + 50, 1, 1);
			}
		}

		for (stari = 480; stari > 0; --stari) {
			i = rndItemStar.nextInt(100);
			j = rndItemStar.nextInt(100);
			int d = (i - 50) * (i - 50) + (j - 50) * (j - 50);
			g2.setColor(Color.getHSBColor(0.8f, 0.2f, 1 - d / 6000f)); //6000 = 5000 + something
			g2.fillRect(i + 140, j + 130, 1, 1);
			g2.fillRect(rndItemStar.nextInt(480), rndItemStar.nextInt(480), 1, 1);
		}

		//Main Loop
		//---------

		int fps = 0, fpsout = 0, frame = 0;
		long lastfpst, lastfpsoutt = System.nanoTime();
		while (lives >= 0) {
			lastfpst = System.nanoTime();

			//Control

			playerSpeed = 2;
			if (keys[KeyEvent.VK_Y] || keys[KeyEvent.VK_Z])
				playerSpeed = 4;
			if (keys[KeyEvent.VK_SHIFT])
				playerSpeed = 1;

			if (keys[KeyEvent.VK_ESCAPE])
				lives = -1;
			if (keys[KeyEvent.VK_UP] && playerY > 2)
				playerY -= playerSpeed;
			if (keys[KeyEvent.VK_DOWN] && playerY < 448)
				playerY += playerSpeed;
			if (keys[KeyEvent.VK_LEFT] && playerX > 2)
				playerX -= playerSpeed;
			if (keys[KeyEvent.VK_RIGHT] && playerX < 448)
				playerX += playerSpeed;

			//Shoot
			if ((keys[KeyEvent.VK_X] || keys[KeyEvent.VK_SPACE]) && frame - lastShot >= 1380) {
				lastShot = frame;
				shotY = playerY;
				multiplier = 1;
			}

			if (shotY > -30)
				shotY -= 4;

			//Items
			for (i = 0; i < itemnum; ++i) {
				if (item[i][1] > -1) {
					item[i][0] += item[i][2];
					item[i][1] += item[i][3];

					int dir = item[i][2] > 0.0625 ? -1 : 1;
					item[i][2] += dir * item[i][4];
					if (item[i][3] < 5)
						item[i][3] += item[i][5];

					if (item[i][1] > 480) {
						item[i][1] = -10;
						itemStack[itemStacki++] = i;
					}
					if (item[i][0] > playerX - 5 && item[i][0] < playerX + 25 && item[i][1] > playerY - 5 && item[i][1] < playerY + 25) {
						score += 20;
						item[i][1] = -10;
						itemStack[itemStacki++] = i;
					}
				}
			}

			//Item2
			if (item2[1] <= 480) {
				item2[1]++;
				if (item2[1] > 480)
					item2[3] = 1;
			}
			if (item2[2] == frame) {
				item2[0] = rndItemStar.nextInt(450);
				item2[1] = -30;
				item2[2] += 900;
			}
			if (playerX + 18 > item2[0] && playerX < item2[0] + 18 && playerY + 18 > item2[1] && playerY < item2[1] + 18) {
				score += item2[3] * 500;
				if (item2[3] < 32)
					item2[3] *= 2;
				item2[1] = 490;
			}

			//Level Generator
			if (frame >= nextEnemy) {
				if (enemyStacki > 0)
					enemyStacki--;
				int ei = enemyStack[enemyStacki];
				enemy[ei][0] = rndLvl.nextInt(400) + 25;
				enemy[ei][1] = -30;
				enemy[ei][2] = 1;

				nextEnemy = frame + nextEnemyDiff + rndLvl.nextInt(60 + nextEnemyDiff);
				if (nextEnemyDiff >= 2)
					nextEnemyDiff -= 2;

				enemyB[ei][0] = rndLvl.nextInt(100) + 50;
				t1 = enemyB[ei][1] = rndLvl.nextInt(6);
				enemyB[ei][2] = 60 + rndLvl.nextInt(4) * 20;
				enemyB[ei][3] = 5;
				enemyB[ei][4] = 1.5;
				enemyB[ei][5] = 20;
				enemyB[ei][6] = rndLvl.nextInt(2);
				enemyB[ei][7] = 1.5;
				enemyB[ei][8] = 60 * (rndLvl.nextInt(2) + 1);

				if (t1 == 2)
					enemyB[ei][2] = 25 * rndLvl.nextInt(5) + 100;
				if (t1 == 1 || t1 == 2) {
					enemyB[ei][5] = 10;
					enemyB[ei][6] = 0;
					enemyB[ei][7] = 0;
					enemyB[ei][8] = -1;
				}
			}

			//Enemies
			for (i = 0; i < enemynum; ++i) {
				if (enemy[i][2] > 0) {
					if (enemy[i][1] > shotY && enemy[i][1] < shotY + 20 && shotY > -30) {
						enemy[i][2] = 0;
						enemyStack[enemyStacki++] = i;

						score += 100 * multiplier;
						if (frame - lastDeath > 300 && multiplier < 64)
							multiplier *= 2;

						for (j = 0; j < 100; ++j) {
							if (itemStacki > 0)
								itemStacki--;
							int ii = itemStack[itemStacki];
							item[ii][0] = enemy[i][0];
							item[ii][1] = enemy[i][1];
							item[ii][2] = rndItemStar.nextInt(100) / 40.0 - 1.25;
							item[ii][3] = rndItemStar.nextInt(100) / 50.0 + 0.5;
							item[ii][4] = rndItemStar.nextInt(10) * 0.002;
							item[ii][5] = rndItemStar.nextInt(10) * 0.0005 + 0.01;
						}
					}

					//Movement
					if (enemy[i][2] == 1) {
						enemy[i][1]++;
						if (enemy[i][1] > enemyB[i][0]) {
							enemy[i][2]++;
							enemy[i][3] = 0;
						}
					}

					//Bullet firing
					if (enemy[i][2] == 2 && --enemy[i][3] <= 0) {
						enemy[i][3] = 500;
						for (j = 0; j < enemyB[i][2]; ++j) {
							if (bulletStacki > 0)
								bulletStacki--;
							int bi = bulletStack[bulletStacki];
							ibullet[bi][0] = 1;
							ibullet[bi][2] = 0;
							bullet[bi][0] = enemy[i][0];
							bullet[bi][1] = enemy[i][1];

							bullet[bi][3] = enemyB[i][4];
							bullet[bi][5] = enemyB[i][6];
							bullet[bi][6] = enemyB[i][7];
							bullet[bi][7] = enemyB[i][8];

							stari = (int) (enemyB[i][2] / enemyB[i][3]);

							//PI*0.04 = 2*PI*0.02
							if (enemyB[i][1] == 0 || enemyB[i][1] == 4 || enemyB[i][1] == 5) {
								bullet[bi][2] = j * 2 * PI * (enemyB[i][3] / enemyB[i][2]);
								bullet[bi][4] = enemyB[i][5] * (j / stari);

								if (enemyB[i][1] == 4)
									bullet[bi][2] -= (j / stari) * PI * 0.04;
								if (enemyB[i][1] == 5) {
									bullet[bi][6] = enemyB[i][7] * (1 + 0.2 * j / stari);
									bullet[bi][7] = enemyB[i][5] * (enemyB[i][3] - 1) - bullet[bi][4];
								}
							}
							if (enemyB[i][1] == 1) {
								bullet[bi][2] = (j / enemyB[i][3]) * PI * 0.04 + (j % enemyB[i][3]) * 2 * PI / enemyB[i][3];
								bullet[bi][4] = enemyB[i][5] * (j / enemyB[i][3]);
							}
							if (enemyB[i][1] == 2) {
								int wavenum = 5 * (int) enemyB[i][3];
								bullet[bi][2] = (j / wavenum) * PI * 0.04 + (j % enemyB[i][3]) * 2 * PI / enemyB[i][3];
								bullet[bi][4] = enemyB[i][5] * (((j % wavenum) / enemyB[i][3]) + (j / wavenum) * (enemyB[i][3] + 2));
							}
							if (enemyB[i][1] == 3) {
								bullet[bi][2] = (-PI / 8) - ((j / 2) % 5) * (PI / 20);
								if (j % 2 == 1)
									bullet[bi][2] = -PI - bullet[bi][2];
								bullet[bi][4] = enemyB[i][5] * (j / 10);
								bullet[bi][5] = 1;
							}

						}
					}
				}
			}

			//Bullet movement
			for (i = 0; i < bulletnum; ++i) {
				if (ibullet[i][0] > 0) {
					if (bullet[i][1] > shotY && bullet[i][1] < shotY + 20) {
						ibullet[i][0] = 0;
						bulletStack[bulletStacki++] = i;
					}

					if (ibullet[i][0] == 1 && --bullet[i][4] <= 0) {
						ibullet[i][0]++;
						ibullet[i][1] = 30;
						while (bullet[i][2] > PI)
							bullet[i][2] -= 2 * PI;
					}

					if (ibullet[i][0] == 2 && --bullet[i][7] == -1) {
						ibullet[i][0]++;
						if (bullet[i][5] == 1) {
							t1 = playerX - bullet[i][0] + 5;
							t2 = playerY - bullet[i][1] + 5;
							bullet[i][5] = Math.acos(t1 / Math.sqrt(t1 * t1 + t2 * t2)); //dot = t1*1 + t2*0
							if (t2 > 0)
								bullet[i][5] *= -1;
						} else
							bullet[i][5] = bullet[i][2];
						bullet[i][5] -= bullet[i][2];
					}

					if (ibullet[i][0] == 2 || ibullet[i][0] == 3) {
						if (ibullet[i][0] == 3 && ibullet[i][1] > 0)
							ibullet[i][1]--;
						t1 = bullet[i][2] + bullet[i][5] * (30 - ibullet[i][1]) / 30.0;
						t2 = ibullet[i][1] / 30.0 * bullet[i][3] + (30 - ibullet[i][1]) / 30.0 * bullet[i][6];
						bullet[i][0] += Math.cos(t1) * t2;
						bullet[i][1] -= Math.sin(t1) * t2;

						if (bullet[i][0] < -20 || bullet[i][0] > 480 || bullet[i][1] < -20 || bullet[i][1] > 480) {
							ibullet[i][0] = 0;
							bulletStack[bulletStacki++] = i;
						}

						t1 = (playerX + 4 - bullet[i][0]) * (playerX + 4 - bullet[i][0]) + (playerY - bullet[i][1]) * (playerY - bullet[i][1]);
						if (t1 < 100 && frame - lastDeath > 120) {
							playerX = 225;
							playerY = 400;
							lives--;
							lastDeath = frame;
						}
						if (t1 < 1600 && ibullet[i][2] == 0) {
							ibullet[i][2] = 1;
							score += 20;

							if (grazeStacki > 0)
								grazeStacki--;
							int gi = grazeStack[grazeStacki];
							graze[gi][0] = playerX + 16;
							graze[gi][1] = playerY + 12;
							graze[gi][2] = 2 * rndItemStar.nextDouble() - 1;
							graze[gi][3] = 2 * rndItemStar.nextDouble() - 1;
							graze[gi][4] = 60;
						}
					}
				}
			}

			//Graze
			for (i = 0; i < grazenum; ++i)
				if (graze[i][4] > 0) {
					graze[i][0] += graze[i][2];
					graze[i][1] += graze[i][3];
					if (--graze[i][4] <= 0)
						grazeStack[grazeStacki++] = i;
				}

			//Blitting

			g.drawImage(gbg, 0, 0, null);

			//Stars
			for (i = 1, stari = 0; i <= 5; ++i)
				for (j = 0; j < i * 5; ++j, ++stari) {
					star[stari][1] += (26 - i * i) * 0.01;
					if (star[stari][1] > 480) {
						star[stari][0] = rndItemStar.nextInt(480);
						star[stari][1] = -rndItemStar.nextInt(30) - 10;
					}
					g.setColor(Color.white);
					g.fillRect((int) star[stari][0], (int) star[stari][1], (6 - i), (6 - i));
				}

			if (item2[1] <= 480) {
				g.setColor(Color.blue);
				g.fillRect(item2[0], item2[1], 30, 30);
			}
			for (i = 0; i < enemynum; ++i)
				if (enemy[i][2] > 0)
					g.drawImage(genemy, enemy[i][0], enemy[i][1], null);
			if (frame - lastDeath > 120 || (frame / 10) % 3 > 0)
				g.drawImage(gplayer, playerX, playerY, null);
			for (i = 0; i < itemnum; ++i)
				if (item[i][1] > -1)
					g.drawImage(gitem, (int) item[i][0], (int) item[i][1], null);
			for (i = 0; i < bulletnum; ++i)
				if (ibullet[i][0] > 1)
					g.drawImage(gbullet, (int) bullet[i][0], (int) bullet[i][1], null);
			g.setColor(Color.pink);
			for (i = 0; i < grazenum; ++i)
				if (graze[i][4] > 0)
					g.fillRect((int) graze[i][0], (int) graze[i][1], 2, 2);
			if (shotY > -30)
				g.fillRect(0, shotY, 480, 20);

			int cd = (1439 - frame + lastShot) / 60;
			if (cd < 0)
				cd = 0;
			g.setColor(Color.white);
			g.drawString("Score: " + String.valueOf(score), 5, 15);
			g.drawString("Lives: " + String.valueOf(lives), 5, 30);
			g.drawString("Next Bomb: " + String.valueOf(cd), 5, 45);
			g.drawString("FPS: " + String.valueOf(fpsout), 430, 475);

			appletGraphics.drawImage(screen, 0, 0, null);

			do {
				Thread.yield();
			} while (System.nanoTime() - lastfpst < 16600000L);

			fps++;
			frame++;
			if (System.nanoTime() - lastfpsoutt >= 1000000000L) {
				fpsout = fps;
				fps = 0;
				lastfpsoutt += 1000000000L;
			}

			if (!isActive())
				return;
		}

		g.drawString("GAME OVER", 200, 220);
		g.drawString("Press 'R' to Restart", 190, 240);
		appletGraphics.drawImage(screen, 0, 0, null);
	}

	public void processKeyEvent(KeyEvent e) {
		keys[e.getKeyCode()] = (e.getID() == KeyEvent.KEY_PRESSED);
	}
}