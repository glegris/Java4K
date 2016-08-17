package java4k;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Launcher {

	private static final Class[] GAME_CLASSES = { java4k.apohockey4k.W.class, java4k.apoone4k.A.class, java4k.boing4k.a.class, java4k.boxbot4k.B.class, java4k.castlevania4k.a.class,
			java4k.crackattack4k.a.class, java4k.demonattack4k.a.class, java4k.di4klo.A.class, java4k.diez.Z.class, java4k.dord.a.class, java4k.doubledragon4k.a.class, java4k.flap4kanabalt.V.class,
			java4k.fzero4k.M.class, java4k.gradius4k.a.class, java4k.greenballs.G.class, java4k.i4kopter.I4Kopter.class, java4k.inthedark4k.A.class, java4k.jackal4k.a.class,
			java4k.junglehunt4k.a.class, java4k.keystonekapers4k.a.class, java4k.laserpinball.a.class, java4k.legendofzelda4k.a.class, java4k.magewars4k.M.class, java4k.mcjob.a.class,
			java4k.myprecious.R.class, java4k.mysterymash.M.class, java4k.on.O.class, java4k.outrun4k.a.class, java4k.pinball4k.a.class, java4k.pitfall4k.a.class, java4k.porta4k.P.class,
			java4k.rainbowroad.a.class, java4k.s23.A.class, java4k.scramble.G.class, java4k.spacedevastation.A.class, java4k.supermarioland4k.a.class, java4k.thebattleforhoth4k.a.class,
			java4k.wolfenstein4k.a.class, java4k.yoshiscoins4k.a.class };

	private static final DefaultListModel<GameInfo> gameListModel = new DefaultListModel<GameInfo>();
	private JTextArea textArea;
	
	public Launcher() throws IOException {
		parseGameInfos();
	}

	private void startUI() {
		JList list = new JList(gameListModel);
		list.setCellRenderer(new GameListRenderer());
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 1) {
					int index = list.locationToIndex(evt.getPoint());
					GameInfo gameInfo = gameListModel.elementAt(index);
					textArea.setText(gameInfo.description);
					textArea.setCaretPosition(0);
				} else if (evt.getClickCount() == 2) {
					// Double-click detected
					int index = list.locationToIndex(evt.getPoint());
					GameInfo gameInfo = gameListModel.elementAt(index);

					try {
						//						Method startMethod = gameInfo.gameClass.getMethod("main", String[].class); //stringArray.getClass()
						//						Object instance = gameInfo.gameClass.newInstance();
						//						String[] params = null;
						//						startMethod.invoke(instance, (Object) params);

						Game game = (Game) gameInfo.gameClass.newInstance();
						startGame(game, gameInfo);

					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} /*catch (InvocationTargetException e) {
						e.printStackTrace();
						} catch (NoSuchMethodException e) {
						e.printStackTrace();
						}*/
				}
			}
		});

		JScrollPane listScroll = new JScrollPane(list);
		listScroll.setPreferredSize(new Dimension(350, 400));

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane textScroll = new JScrollPane(textArea);
		textScroll.setPreferredSize(new Dimension(400, 400));

		JFrame frame = new JFrame("Java 4K games");
		frame.setLayout(new FlowLayout());
		frame.add(listScroll);
		frame.add(textScroll);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void startGame(final Game game, GameInfo info) {
		javax.swing.JFrame frame = new javax.swing.JFrame(info.name);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				game.stop();
			}
		});
		frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = game.getPanel();
		frame.add(panel, java.awt.BorderLayout.CENTER);

		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		panel.requestFocusInWindow();

		game.start();
	}

	private void parseGameInfos() throws IOException {

		for (int i = 0; i < GAME_CLASSES.length; i++) {
			Class gameClass = GAME_CLASSES[i];
			BufferedReader in = new BufferedReader(new InputStreamReader(gameClass.getResourceAsStream("readme.txt")));
			GameInfo gameInfo = new GameInfo();
			gameInfo.gameClass = gameClass;
			gameInfo.name = in.readLine().trim();
			in.readLine(); // Skip next line

			StringBuffer textBuffer = new StringBuffer();
			String s;
			while ((s = in.readLine()) != null) {
				textBuffer.append(s);
				textBuffer.append("\n");
			}
			gameInfo.description = textBuffer.toString();

			//			StringBuffer descriptionBuffer = new StringBuffer();
			//			StringBuffer instructionsBuffer = new StringBuffer();
			//			String s;
			//			while(!(s = in.readLine()).contains("Description")) {
			//				in.readLine();
			//			}
			//			while((s = in.readLine()).contains("Description")) {
			//				in.readLine();
			//			}
			//			while(!(s = in.readLine()).contains("Instructions")) {
			//				descriptionBuffer.append(in.readLine());
			//			}
			//			while((s = in.readLine()).contains("Instructions")) {
			//				in.readLine();
			//			}
			//			while((s = in.readLine()) != null) {
			//				instructionsBuffer.append(in.readLine());
			//			}

			// Load screenshots
			BufferedImage image;
			String className = gameClass.getSimpleName();
			InputStream is = gameClass.getResourceAsStream(className + ".png");
			if (is == null) {
				is = gameClass.getResourceAsStream(className + ".jpg");
			}
			if (is == null) {
				is = gameClass.getResourceAsStream(className + ".gif");
			}
			if (is != null) {
				image = ImageIO.read(is);
			} else {
				image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB_PRE);
			}
			gameInfo.image = image;

			gameListModel.addElement(gameInfo);
		}

	}

	private class GameInfo {
		Class gameClass;
		BufferedImage image;
		String name;
		String description;
		//String instructions;
	}

	private class GameListRenderer extends DefaultListCellRenderer {

		Font font = new Font("helvitica", Font.BOLD, 12);

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText(((GameInfo) value).name);
			label.setIcon(new ImageIcon(((GameInfo) value).image));
			label.setHorizontalTextPosition(JLabel.RIGHT);
			label.setFont(font);
			return label;
		}
	}

	public static void main(String[] args) {

		try {
			final Launcher launcher = new Launcher();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					launcher.startUI();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}
