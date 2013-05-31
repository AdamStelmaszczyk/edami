package scorer;

import structures.Clusters;
import structures.Point;
import structures.Points;

public class Scorer
{
	public final double randIndex;
	public final int truePositives;
	public final int trueNegatives;

	public Scorer(Points input, Clusters clusters)
	{
		final int allPairs = getNumberOfAllPairs(input.size());
		truePositives = getNumberOfTruePositives(clusters);
		trueNegatives = getNumberOfTrueNegatives(clusters);
		randIndex = (double) (truePositives + trueNegatives) / allPairs;
	}

	private static int getNumberOfTruePositives(Clusters clusters)
	{
		int result = 0;
		for (Points cluster : clusters)
		{
			for (int i = 0; i < cluster.size(); i++)
			{
				for (int j = i + 1; j < cluster.size(); j++)
				{
					if (cluster.get(i).clusterId == cluster.get(j).clusterId)
					{
						result++;
					}
				}
			}
		}
		return result;
	}

	private static int getNumberOfTrueNegatives(Clusters clusters)
	{
		int result = 0;
		for (int i = 0; i < clusters.size(); i++)
		{
			for (Point p : clusters.get(i))
			{
				for (int j = i + 1; j < clusters.size(); j++)
				{
					for (Point p2 : clusters.get(j))
					{
						if (p.clusterId != p2.clusterId)
						{
							result++;
						}
					}
				}
			}
		}
		return result;
	}

	private static int getNumberOfAllPairs(int n)
	{
		return (n * (n - 1)) / 2;
	}
}
