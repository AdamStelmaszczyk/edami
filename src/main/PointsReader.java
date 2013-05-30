package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import structures.Point;
import structures.Points;

public class PointsReader
{
	public static Points getPoints(InputStream in) throws IOException
	{
		final Points points = new Points();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null)
		{
			final Point point = new Point();
			final StringTokenizer tokenizer = new StringTokenizer(line);
			while (tokenizer.hasMoreTokens())
			{
				point.add(Double.parseDouble(tokenizer.nextToken()));
			}
			points.add(point);
		}
		return points;
	}
}
