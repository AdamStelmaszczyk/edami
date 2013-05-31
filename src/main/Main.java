package main;

import java.io.IOException;

import scorer.Scorer;
import structures.Clusters;
import structures.Points;
import algorithms.ClusteringAlgorithm;
import algorithms.Dbscan;
import algorithms.Denclue;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		double eps = 0.9;
		int minPts = 5;
		boolean denclue = false;
		if (args.length > 0)
		{
			eps = Double.parseDouble(args[0]);
		}
		if (args.length > 1)
		{
			minPts = Integer.parseInt(args[1]);
		}
		if (args.length > 2)
		{
			denclue = true;
		}
		final Points input = PointsReader.getPoints(System.in);
		ClusteringAlgorithm algorithm;
		if (denclue)
		{
			algorithm = new Denclue(input);
		}
		else
		{
			algorithm = new Dbscan(input, eps, minPts);
		}
		final Clusters clusters = algorithm.getClusters();
		final Scorer scorer = new Scorer(input, clusters);
		System.out.println(clusters.size() + " cluster(s) of size: " + clusters);
		System.out.println("True positives: " + scorer.truePositives);
		System.out.println("True negatives: " + scorer.trueNegatives);
		System.out.println("Rand index: " + scorer.randIndex);
	}
}
