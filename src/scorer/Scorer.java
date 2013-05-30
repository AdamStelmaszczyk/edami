package scorer;

import java.util.HashMap;
import java.util.Map;

import structures.Clusters;
import structures.Point;
import structures.Points;

public class Scorer
{
	public final double qualityOfGroupsNumber;
	public final double similarityQuality;
	public final double discriminateQuality;
	public final double generalQuality;

	public Scorer(Points input, Clusters clusters)
	{
		final Clusters perfectClusters = getPerfectClusters(input);
		final int allPairs = getNumberOfAllPairs(input.size());
		final int A = getNumberOfPairsInOneCluster(perfectClusters);
		final int B = allPairs - A;
		final int a = getNumberOfPairsInOneCluster(clusters);
		final int b = allPairs - a;
		qualityOfGroupsNumber = (double) clusters.size() / perfectClusters.size();
		similarityQuality = (double) a / A;
		discriminateQuality = (double) b / B;
		generalQuality = Math.sqrt(similarityQuality * discriminateQuality);
	}

	private static int getNumberOfPairsInOneCluster(Clusters clusters)
	{
		int pairs = 0;
		for (final Points cluster : clusters)
		{
			for (int i = 0; i < cluster.size(); i++)
			{
				for (int j = i + 1; j < cluster.size(); j++)
				{
					if (cluster.get(i).clusterId == cluster.get(j).clusterId)
					{
						pairs++;
					}
				}
			}
		}
		return pairs;
	}

	private static int getNumberOfAllPairs(int n)
	{
		return (n * (n - 1)) / 2;
	}

	private static Clusters getPerfectClusters(Points input)
	{
		final Map<Integer, Points> clusterIdToCluster = new HashMap<Integer, Points>();
		for (final Point p : input)
		{
			Points cluster = clusterIdToCluster.get(p.clusterId);
			if (cluster == null)
			{
				cluster = new Points();
				cluster.add(p);
				clusterIdToCluster.put(p.clusterId, cluster);
			}
			else
			{
				cluster.add(p);
			}
		}
		return new Clusters(clusterIdToCluster.values());
	}
}
