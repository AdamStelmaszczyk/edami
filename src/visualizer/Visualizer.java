package visualizer;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import structures.Clusters;
import structures.Point;

public class Visualizer
{
	private final static int WIDTH = 600;
	private final static int HEIGHT = 600;
	private final static int MAX_X = 25;
	private final static int MAX_Y = 25;

	public static void showClusters(final String title, final Clusters clusters)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final JFrame frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(WIDTH, HEIGHT);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.add(new JPanel()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						for (int i = 0; i < clusters.size(); i++)
						{
							for (final Point p : clusters.get(i))
							{
								final int x = getWindowX(p.params[0]);
								final int y = getWindowY(p.params[5]);
								g.drawString(String.valueOf(i + 1), x, y);
							}
						}
					}
				});
			}
		});
	}

	private static int getWindowX(double value)
	{
		return (int) (WIDTH * (value / MAX_X));
	}

	private static int getWindowY(double value)
	{
		return (int) (HEIGHT * (value / MAX_Y));
	}
}
