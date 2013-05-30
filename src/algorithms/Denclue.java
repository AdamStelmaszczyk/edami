package algorithms;
import structures.Clusters;
import structures.Points;

public class Denclue extends ClusteringAlgorithm
{
	@Override
	public Clusters getClusters(Points points)
	{
		// TODO implement this
		Clusters clusters = new Clusters();
		clusters.add(points);
		return clusters;
	}
}
