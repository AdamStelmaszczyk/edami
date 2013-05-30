package main;
import java.io.IOException;

import structures.Clusters;
import structures.Points;
import algorithms.ClusteringAlgorithm;
import algorithms.Dbscan;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		final Points points = PointsReader.getPoints(System.in);
		final ClusteringAlgorithm algorithm = new Dbscan();
		final Clusters clusters = algorithm.getClusters(points);
		System.out.println(clusters);
	}
}
