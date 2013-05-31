package algorithms;

import structures.Clusters;
import structures.Points;

abstract public class ClusteringAlgorithm
{
	protected Points input;

	public ClusteringAlgorithm(Points input)
	{
		this.input = input;
	}

	abstract public Clusters getClusters();

	@Override
	abstract public String toString();
}
