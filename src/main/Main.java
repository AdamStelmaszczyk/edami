package main;

import java.io.IOException;

import scorer.Scorer;
import structures.Clusters;
import structures.Points;
import visualizer.Visualizer;
import algorithms.ClusteringAlgorithm;
import algorithms.Dbscan;
import algorithms.Denclue;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		double eps = 0.9;
		int minPts = 5;

		if (args.length > 0)
		{
			eps = Double.parseDouble(args[0]);
		}
		if (args.length > 1)
		{
			minPts = Integer.parseInt(args[1]);
		}

		final Points input = PointsReader.getPoints(System.in);

		System.out.println("Standard deviations of parameters:");
		for (int i = 0; i < input.getDimenstion(); i++)
		{
			System.out.println(input.getStandardDeviation(i));
		}
		System.out.println();

		Visualizer.showClusters("Perfect clustering", input.getPerfectClusters());

		testAlgorithm(input, new Dbscan(input, eps, minPts));
		testAlgorithm(input, new Denclue(input));
	}

	private static void testAlgorithm(Points input, ClusteringAlgorithm algorithm)
	{
		final Clusters clusters = algorithm.getClusters();
		final Scorer scorer = new Scorer(input, clusters);

		System.out.println(algorithm.toString());
		System.out.println(clusters.toString());
		System.out.println(scorer.toString());
				
		Visualizer.showClusters(algorithm.toString(), clusters);
	}

}
