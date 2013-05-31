package algorithms;

import structures.Clusters;
import structures.Points;

public class Denclue extends ClusteringAlgorithm
{
	public Denclue(Points input)
	{
		super(input);
	}

	@Override
	public Clusters getClusters()
	{
		// TODO implement this
		final Clusters clusters = new Clusters();
		clusters.add(input);
		return clusters;
	}

	@Override
	public String toString()
	{
		return "DENCLUE";
	}
}
