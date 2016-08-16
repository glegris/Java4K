package java4k.flap4kanabalt;

/*
 * Copyright (c) 2005-2014 Dirk Aporius <dirk.aporius@gmail.com>
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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

public class V extends Applet implements Runnable {

	@Override
	public void start() {
		new Thread(this).start();
	}

	private boolean down;

	private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	private static int[] toInt = new int[128];

	static {
		for (int i = 0; i < ALPHABET.length; i++) {
			toInt[ALPHABET[i]] = i;
		}
	}

	/**
	 * Translates the specified Base64 string into a byte array.
	 *
	 * @param s the Base64 string (not null)
	 * @return the byte array (not null)
	 */
	public static byte[] decode(String s) {
		int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
		byte[] buffer = new byte[s.length() * 3 / 4 - delta];
		int mask = 0xFF;
		int index = 0;
		for (int i = 0; i < s.length(); i += 4) {
			int c0 = toInt[s.charAt(i)];
			int c1 = toInt[s.charAt(i + 1)];
			buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c2 = toInt[s.charAt(i + 2)];
			buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
			if (index >= buffer.length) {
				return buffer;
			}
			int c3 = toInt[s.charAt(i + 3)];
			buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
		}
		return buffer;
	}

	@Override
	public void run() {

		BufferedImage iBackground = new BufferedImage(320, 205, BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g = iBackground.createGraphics();
		try {
			//			g.drawImage(ImageIO.read(V.class.getResource("a.png")), 0, 0, 320, 205, 0, 0, 120, 77, null);

			//			g.drawImage(ImageIO.read(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(
			//					"R0lGODlheABNAPEAAIaGlrCwv2RqfQAAACwAAAAAeABNAEEC/4yPqct94KKcNIILFd670uwtEBga5MSlale2yJpgMfesNsMJthoIwV3bCXe6odEmSCqXyo2OCW1Gl4Bk9Ym9aq3cbHd7RKam5LDKqjWrnbliUU2OM9dsN/3ejg/l/D4RjOfUN0hYaPi2goYmNHd2+AhJGPZ0gSgYiZlZ6MKpcfEzEgo6+lkaynL62SmCt6rh+hOLIkR6EkTHmXIbyMvbZBSFASXcGPjbm8g3Jnd5jPz3XNksiecVjbSoDJx5NbznZnlNfcmoySxmh2y+vs4IuMweL49ZCa44j48Ju2rroNrSD5aMBjD2dYhmAtTAfSZaMZTF758FIB8cGgzoicUDiP8ThzTEYfHitZEk/1HcVTIlnJUqgbXspkfYFi80y3y5SXOmzpsqs02BV6ZeMJI/n4UzBLSoUGctw/n6dchYFWkvqS5dZEaTMXBVk8bERk/pHy49B8GkAjYf0zpWq6l9qy1ZPaNw63pL9I4IWq9287lMl6evYD/b5joajLgRFXs2l15NbPehC4ySOVa+PKurRsz+MOuSlSF0QtAKD9KQvHn0SYECJZamPDkka1cjMvPwEAg1a9goT78Aeeeha3+pF3bcUQM4Ls7Ki3P83NBMczXMWd3eCKLgayPTpaPWDN5jbI3he0r3cX3GSKfi4rb1iL48m6bup07C+rjpS8KPIUPBon/ONEOdtddUb1AizYH2GSiTggB2cRcx/j2izmH8SRgUgWvRVWAvRxXCVzEEasaeWxCCWE1e+uG1xjBIpUhWV4iImBU3bhlW1YEZbkNPigzud0+A5fR4hzXlXZjWfzemQZRWeoU1IDQIljROhCHOg86UJk6oFjY4PsUlPmL86GGYWGa5IJhmsqPXl0kmsyabY2r5poVxEommU3dCVhiTge1Zl0xzpikgoF3+RSeGhooJ05+LsbeoYGdpKChakcZTAAA7"
			//					))), 0, 0, 320, 205, 0, 0, 120, 77, null);

			//			g.drawImage(ImageIO.read(new ByteArrayInputStream((new BASE64Decoder()).decodeBuffer(
			//					"R0lGODlheABNAPEAAIaGlrCwv2RqfQAAACwAAAAAeABNAEEC/4yPqct94KKcNIILFd670uwtEBga5MSlale2yJpgMfesNsMJthoIwV3bCXe6odEmSCqXyo2OCW1Gl4Bk9Ym9aq3cbHd7RKam5LDKqjWrnbliUU2OM9dsN/3ejg/l/D4RjOfUN0hYaPi2goYmNHd2+AhJGPZ0gSgYiZlZ6MKpcfEzEgo6+lkaynL62SmCt6rh+hOLIkR6EkTHmXIbyMvbZBSFASXcGPjbm8g3Jnd5jPz3XNksiecVjbSoDJx5NbznZnlNfcmoySxmh2y+vs4IuMweL49ZCa44j48Ju2rroNrSD5aMBjD2dYhmAtTAfSZaMZTF758FIB8cGgzoicUDiP8ThzTEYfHitZEk/1HcVTIlnJUqgbXspkfYFi80y3y5SXOmzpsqs02BV6ZeMJI/n4UzBLSoUGctw/n6dchYFWkvqS5dZEaTMXBVk8bERk/pHy49B8GkAjYf0zpWq6l9qy1ZPaNw63pL9I4IWq9287lMl6evYD/b5joajLgRFXs2l15NbPehC4ySOVa+PKurRsz+MOuSlSF0QtAKD9KQvHn0SYECJZamPDkka1cjMvPwEAg1a9goT78Aeeeha3+pF3bcUQM4Ls7Ki3P83NBMczXMWd3eCKLgayPTpaPWDN5jbI3he0r3cX3GSKfi4rb1iL48m6bup07C+rjpS8KPIUPBon/ONEOdtddUb1AizYH2GSiTggB2cRcx/j2izmH8SRgUgWvRVWAvRxXCVzEEasaeWxCCWE1e+uG1xjBIpUhWV4iImBU3bhlW1YEZbkNPigzud0+A5fR4hzXlXZjWfzemQZRWeoU1IDQIljROhCHOg86UJk6oFjY4PsUlPmL86GGYWGa5IJhmsqPXl0kmsyabY2r5poVxEommU3dCVhiTge1Zl0xzpikgoF3+RSeGhooJ05+LsbeoYGdpKChakcZTAAA7"
			//					))), 0, 0, 320, 205, 0, 0, 120, 77, null);

			g.drawImage(
					ImageIO.read(new ByteArrayInputStream(
							decode("R0lGODlheABNAPEAAIaGlrCwv2RqfQAAACwAAAAAeABNAEEC/4yPqct94KKcNIILFd670uwtEBga5MSlale2yJpgMfesNsMJthoIwV3bCXe6odEmSCqXyo2OCW1Gl4Bk9Ym9aq3cbHd7RKam5LDKqjWrnbliUU2OM9dsN/3ejg/l/D4RjOfUN0hYaPi2goYmNHd2+AhJGPZ0gSgYiZlZ6MKpcfEzEgo6+lkaynL62SmCt6rh+hOLIkR6EkTHmXIbyMvbZBSFASXcGPjbm8g3Jnd5jPz3XNksiecVjbSoDJx5NbznZnlNfcmoySxmh2y+vs4IuMweL49ZCa44j48Ju2rroNrSD5aMBjD2dYhmAtTAfSZaMZTF758FIB8cGgzoicUDiP8ThzTEYfHitZEk/1HcVTIlnJUqgbXspkfYFi80y3y5SXOmzpsqs02BV6ZeMJI/n4UzBLSoUGctw/n6dchYFWkvqS5dZEaTMXBVk8bERk/pHy49B8GkAjYf0zpWq6l9qy1ZPaNw63pL9I4IWq9287lMl6evYD/b5joajLgRFXs2l15NbPehC4ySOVa+PKurRsz+MOuSlSF0QtAKD9KQvHn0SYECJZamPDkka1cjMvPwEAg1a9goT78Aeeeha3+pF3bcUQM4Ls7Ki3P83NBMczXMWd3eCKLgayPTpaPWDN5jbI3he0r3cX3GSKfi4rb1iL48m6bup07C+rjpS8KPIUPBon/ONEOdtddUb1AizYH2GSiTggB2cRcx/j2izmH8SRgUgWvRVWAvRxXCVzEEasaeWxCCWE1e+uG1xjBIpUhWV4iImBU3bhlW1YEZbkNPigzud0+A5fR4hzXlXZjWfzemQZRWeoU1IDQIljROhCHOg86UJk6oFjY4PsUlPmL86GGYWGa5IJhmsqPXl0kmsyabY2r5poVxEommU3dCVhiTge1Zl0xzpikgoF3+RSeGhooJ05+LsbeoYGdpKChakcZTAAA7"))),
					0, 0, 320, 205, 0, 0, 120, 77, null);
		} catch (Exception e1) {
		}

		// Graphische Grundlagen für das Double Buffering
		BufferedImage screen = new BufferedImage(320, 480, BufferedImage.TYPE_INT_RGB);
		g = screen.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D appletGraphics = (Graphics2D) getGraphics();

		// Variablen zum Zeitmessen und genau Timen wann geupdatet werden soll
		long lastTime = System.nanoTime();
		long think = 10000000L;

		String s = "";
		int x = 0;
		int curFrame = 0;
		int frameTime = 0;
		int i = 0;
		int j = 0;
		int score = 0;
		int maxScore = 0;
		boolean bCurDown = false;
		boolean bStart = false;
		boolean bWin = false;

		final float CHANGE_DIF = 1.2f;

		final Color downColor = new Color(100, 106, 125);
		final Color windowColor = new Color(176, 176, 191);
		final Color darkColor = new Color(53, 53, 61);
		final Color brightColor = new Color(230, 230, 230);

		float playerY = 240;
		float playerX = 60;
		float curVecY = 0;
		float curDif = 0;

		int[] level = new int[4];
		level[0] = level[1] = 400;

		// Game loop.
		while (true) {
			long now = System.nanoTime();
			long delta = now - lastTime;
			think += delta;

			// Update / think
			// Wenn 10 ms vergangen sind, dann denke nach
			while (think >= 10000000L) {
				think -= 10000000L;

				if (this.down) {
					if (!bStart) {
						playerX = 60;
						playerY = 240;

						this.down = false;
						bCurDown = false;

						x = 400;
						curVecY = -1f;
						curDif = 0;
						score = 0;
						level = new int[2048];
						for (i = 0; i < level.length; i += 4) {
							level[i] = x;
							level[i + 1] = (int) (Math.random() * 300) + 30;
							level[i + 2] = (int) (Math.random() * 50) + 70;
							level[i + 3] = (int) (Math.random() * 5) + 78;

							x += 165 + level[i + 2];
						}
						bStart = true;
						bWin = false;
					}
					if (bWin) {
						bWin = false;
						bStart = false;
						this.down = false;

						playerX = 60;
						playerY = 240;
						curDif = 0;
						if (maxScore < score) {
							maxScore = score;
						}
					}
				}

				if (!bWin) {
					curDif += CHANGE_DIF;

					frameTime += 10;
					if (frameTime >= 200) {
						frameTime -= 200;
						curFrame += 1;
						if (curFrame > 3) {
							curFrame = 0;
						}
					}
				}

				if ((!bStart) && (curDif >= 0)) {
					curDif -= 10;
				}
				if (bStart) {
					playerY += curVecY;
					curVecY += 0.051f;
					if (bWin) {
						if (playerY + curFrame + 6 >= 460) {
							curVecY = 0;
							playerY = 460 - 6 - curFrame;
						}
					}
					if (playerY >= 460) {
						bWin = true;
					}
					if (playerY - 6 < 0) {
						playerY = 6;
						if (curVecY < 0) {
							curVecY = 0;
						}
					}

					for (i = 0; i < level.length; i += 4) {
						if (curDif + playerX + 6 < level[i] - level[i + 2] / 2) {
							break;
						}
						if (curDif > level[i] + level[i + 2] / 2) {
							continue;
						}
						if ((curDif + playerX - CHANGE_DIF < level[i]) && (curDif + playerX >= level[i]) && (!bWin)) {
							score += 1;
						}
						if ((curDif + playerX + 6 > level[i] - level[i + 2] / 2) && (curDif + playerX - 6 < level[i] + level[i + 2] / 2)) {
							if ((playerY - curFrame - 6 < level[i + 1]) || (playerY - curFrame + 6 > level[i + 1] + level[i + 3])) {
								bWin = true;
								this.down = false;
							}
						}
					}

					// do action
					if ((this.down) && (!bCurDown)) {
						bCurDown = true;
						curVecY = -2.45f;
					}
				}

				if ((!this.down) && (bCurDown)) {
					bCurDown = false;
				}
			}

			lastTime = now;

			// Renderabschnitt
			// Hintergrund malen
			g.setColor(windowColor);
			g.fillRect(0, 0, 320, 140);
			g.setColor(downColor);
			g.fillRect(0, 320, 320, 140);
			g.drawImage(iBackground, 0, 138, null);

			g.setColor(darkColor);

			if (!bStart) {
				g.setFont(g.getFont().deriveFont(25f).deriveFont(1));
				s = "Flap4kanabalt";
				g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, 30);

				g.setFont(g.getFont().deriveFont(20f).deriveFont(1));
				s = "Tap to fly";
				g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, 100);

				g.setColor(windowColor);
				s = "a 4k mix of flappy bird";
				g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, 380);

				s = "and Canabalt";
				g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, 410);
			}

			g.setColor(darkColor);
			g.fillRect(0, 460, 320, 20);

			for (i = 0; i < level.length; i += 4) {
				if (curDif + 320 < level[i] - level[i + 2] / 2) {
					break;
				}
				if (curDif > level[i] + level[i + 2] / 2) {
					continue;
				}
				g.setColor(darkColor);
				g.fillRect((int) (level[i] - curDif - level[i + 2] / 2), 0, level[i + 2], level[i + 1]);
				g.fillRect((int) (level[i] - curDif - level[i + 2] / 2), level[i + 1] + level[i + 3], level[i + 2], 460 - level[i + 1] - level[i + 3]);

				g.setColor(windowColor);
				for (x = level[i] - level[i + 2] / 2; x < level[i] + level[i + 2] / 2 - 14; x += 16) {
					for (j = 0; j < level[i + 1] - 16; j += 16) {
						g.fillRect((int) (x - curDif) + 4, j + 4, 8, 8);
					}
				}

				for (x = level[i] - level[i + 2] / 2; x < level[i] + level[i + 2] / 2 - 14; x += 16) {
					for (j = level[i + 1] + level[i + 3]; j < 440; j += 16) {
						g.fillRect((int) (x - curDif) + 4, j + 4, 8, 8);
					}
				}
			}

			g.setColor(windowColor);
			for (i = 0; i < 500; i += 10) {
				for (j = 0; j < 20; j += 10) {
					g.fillRect((int) (-curDif + 100) % 10 + i, 465 + j, 5, 5);
				}
			}

			g.setColor(brightColor);
			if (curFrame == 0) {
				g.fillRect((int) (playerX - 14), (int) (playerY - 12), 4, 8);
				g.fillRect((int) (playerX - 10), (int) (playerY - 8), 4, 8);
				g.fillRect((int) (playerX - 6), (int) (playerY - 4), 12, 12);
				g.fillRect((int) (playerX + 6), (int) (playerY - 8), 4, 8);
				g.fillRect((int) (playerX + 10), (int) (playerY - 12), 4, 8);
			}

			if ((curFrame == 1) || (curFrame == 3)) {
				g.fillRect((int) (playerX - 14), (int) (playerY - 5), 28, 6);
				g.fillRect((int) (playerX - 6), (int) (playerY + 1), 12, 6);
			}

			if (curFrame == 2) {
				g.fillRect((int) (playerX - 14), (int) (playerY + 6), 4, 8);
				g.fillRect((int) (playerX - 10), (int) (playerY + 2), 4, 8);
				g.fillRect((int) (playerX - 6), (int) (playerY - 6), 12, 12);
				g.fillRect((int) (playerX + 6), (int) (playerY + 2), 4, 8);
				g.fillRect((int) (playerX + 10), (int) (playerY + 6), 4, 8);
			}

			g.setFont(g.getFont().deriveFont(16f).deriveFont(1));
			s = "best: " + maxScore;
			g.setColor(windowColor);
			g.fillRect(306 - g.getFontMetrics().stringWidth(s), 2, g.getFontMetrics().stringWidth(s) + 8, 20);
			g.setColor(darkColor);
			g.drawRect(306 - g.getFontMetrics().stringWidth(s), 2, g.getFontMetrics().stringWidth(s) + 8, 20);
			g.drawString(s, 310 - g.getFontMetrics().stringWidth(s), 18);

			if (bStart) {
				g.setFont(g.getFont().deriveFont(35f).deriveFont(1));
				if (bWin) {
					s = "Score: " + score;
					x = g.getFontMetrics().stringWidth(s);
					g.setColor(windowColor);
					g.fillRect(150 - x / 2, 210, x + 20, 60);
					g.setColor(darkColor);
					g.drawRect(150 - x / 2, 210, x + 20, 60);
					g.drawString(s, 160 - x / 2, 257);
				} else {
					if ((!bWin) && (bStart)) {
						s = "" + score;
						g.drawString(s, 160 - g.getFontMetrics().stringWidth(s) / 2, 60);
					}
				}
			}

			// Render das Ganze auf den Bildschirm
			appletGraphics.drawImage(screen, 0, 0, null);

			try {
				Thread.sleep(4);
			} catch (Exception e) {
				/** nicht schön aber selten */
			}
			;

			if (!isActive()) {
				return;
			}
		}
	}

	@Override
	public boolean handleEvent(Event e) {
		switch (e.id) {
		case Event.KEY_PRESS:
		case Event.MOUSE_DOWN:
			down = true;
			break;
		case Event.MOUSE_UP:
		case Event.KEY_RELEASE:
			down = false;
			break;

		}
		return false;
	}
}