package algorithms;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import structures.Clusters;
import structures.Point;
import structures.Points;

public class Dbscan extends ClusteringAlgorithm
{
	private final double EPS;
	private final int MIN_PTS;
	private final Set<Point> unvisited = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
	private final Set<Point> clustered = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());

	public Dbscan(Points input, double eps, int minPts)
	{
		super(input);
		EPS = eps;
		MIN_PTS = minPts;
	}

	@Override
	public Clusters getClusters()
	{
		final Clusters clusters = new Clusters();
		unvisited.addAll(input);
		for (final Point point : unvisited)
		{
			unvisited.remove(point);
			final Points neighborhood = getNeighborhood(point);
			if (neighborhood.size() >= MIN_PTS)
			{
				final Points cluster = new Points();
				expandCluster(neighborhood, cluster);
				if (cluster.size() >= MIN_PTS)
				{
					clusters.add(cluster);
				}
			}
		}
		return clusters;
	}

	private void expandCluster(Points neighborhood, Points cluster)
	{
		for (int i = 0; i < neighborhood.size(); i++)
		{
			final Point p = neighborhood.get(i);
			if (unvisited.contains(p))
			{
				unvisited.remove(p);
				final Points neighborhood2 = getNeighborhood(p);
				if (neighborhood2.size() >= MIN_PTS)
				{
					neighborhood.addAll(neighborhood2);
				}
			}
			if (!clustered.contains(p))
			{
				cluster.add(p);
				clustered.add(p);
			}
		}
	}

	private Points getNeighborhood(Point point)
	{
		final Points neighborhood = new Points();
		for (final Point p : input)
		{
			final double dist = point.distanceTo(p);
			if (dist <= EPS)
			{
				neighborhood.add(p);
			}
		}
		return neighborhood;
	}

	@Override
	public String toString()
	{
		return "DBSCAN";
	}
}
