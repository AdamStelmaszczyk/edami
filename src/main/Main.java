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
		final Points input = PointsReader.getPoints(System.in);

		System.out.println("Standard deviations of parameters:");
		for (int i = 0; i < input.getDimenstion(); i++)
		{
			System.out.println(input.getStandardDeviation(i));
		}
		System.out.println();

		Visualizer.showClusters("Perfect clustering", input.getPerfectClusters());

		testAlgorithm(input, new Dbscan(input));
		testAlgorithm(input, new Denclue(input));
	}

	private static void testAlgorithm(Points input, ClusteringAlgorithm algorithm)
	{
		final long startTime = System.currentTimeMillis();
		final long startFreeMemory = Runtime.getRuntime().freeMemory();

		final Clusters clusters = algorithm.getClusters();

		final long stopFreeMemory = Runtime.getRuntime().freeMemory();
		final long stopTime = System.currentTimeMillis();
		final long elapsedTime = stopTime - startTime;
		final long usedMemory = startFreeMemory - stopFreeMemory;

		final Scorer scorer = new Scorer(input, clusters);

		System.out.println(algorithm.toString());
		System.out.println("Time: " + elapsedTime);
		System.out.println("Memory: " + usedMemory);
		System.out.println(clusters.toString());
		System.out.println(scorer.toString());

		Visualizer.showClusters(algorithm.toString(), clusters);
	}
}
