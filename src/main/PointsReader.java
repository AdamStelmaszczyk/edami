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
			final StringTokenizer tokenizer = new StringTokenizer(line);
			final int numberOfParams = tokenizer.countTokens() - 1;
			final double params[] = new double[numberOfParams];
			for (int i = 0; i < numberOfParams; i++)
			{
				params[i] = Double.parseDouble(tokenizer.nextToken());
			}
			final int classId = Integer.parseInt(tokenizer.nextToken());
			final Point point = new Point(params, classId);
			points.add(point);
		}
		return points;
	}
}
