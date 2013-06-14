package scorer;

import structures.Clusters;
import structures.Point;
import structures.Points;

public class Scorer
{
	public final int truePositives;
	public final int trueNegatives;
	public final double randIndex;

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
		for (final Points cluster : clusters)
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
			for (final Point p : clusters.get(i))
			{
				for (int j = i + 1; j < clusters.size(); j++)
				{
					for (final Point p2 : clusters.get(j))
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
		return n * (n - 1) / 2;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		final String newLine = System.getProperty("line.separator");
		sb.append("True positives: ");
		sb.append(truePositives);
		sb.append(newLine);
		sb.append("True negatives: ");
		sb.append(trueNegatives);
		sb.append(newLine);
		sb.append("Rand index: ");
		sb.append(randIndex);
		sb.append(newLine);
		return sb.toString();
	}
}
