package structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Points extends ArrayList<Point>
{
	private static final long serialVersionUID = 1L;

	public Points()
	{
	}

	public Points(Collection<Point> values)
	{
		super(values);
	}

	public double getStandardDeviation(int paramIndex)
	{
		final double average = getAverage(paramIndex);
		double sum = 0.0;
		for (int i = 0; i < size(); i++)
		{
			final double diff = get(i).params[paramIndex] - average;
			sum += diff * diff;
		}
		return Math.sqrt(sum / size());
	}

	public double getAverage(int paramIndex)
	{
		double sum = 0.0;
		for (int i = 0; i < size(); i++)
		{
			sum += get(i).params[paramIndex];
		}
		return sum / size();
	}

	public int getDimenstion()
	{
		if (isEmpty())
		{
			return 0;
		}
		return get(0).params.length;
	}

	public Clusters getPerfectClusters()
	{
		final Map<Integer, Points> clusterIdToCluster = new HashMap<Integer, Points>();
		for (final Point p : this)
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
