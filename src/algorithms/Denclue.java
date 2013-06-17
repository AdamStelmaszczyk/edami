package algorithms;

import java.util.HashMap;
import java.util.Map;

import structures.Clusters;
import structures.HyperCube;
import structures.HyperSpace;
import structures.Point;
import structures.Points;

public class Denclue extends ClusteringAlgorithm
{
	private final double SIGMA = 0.9;
	private final int MIN_PTS = 3;
	private final HyperSpace hyperSpace = new HyperSpace(SIGMA, MIN_PTS);
	private final Map<Point, Points> attractorsWithPoints = new HashMap<Point, Points>();

	public Denclue(Points input)
	{
		super(input);
	}

	@Override
	public Clusters getClusters()
	{
		final Clusters clusters = new Clusters();
		
		for (final Point point : input)
		{
			hyperSpace.addPoint(point);
		}

		hyperSpace.connectMap();

		detDensAttractors();

		while (mergeClusters())
		{
			;
		}

		for (final Point point : attractorsWithPoints.keySet())
		{
			if (attractorsWithPoints.get(point).size() > MIN_PTS)
			{
				clusters.add(attractorsWithPoints.get(point));
			}
		}

		return clusters;
	}

	/** Find paths between attractors. */
	private boolean mergeClusters()
	{
		for (final Point p1 : attractorsWithPoints.keySet())
		{
			for (final Point p2 : attractorsWithPoints.keySet())
			{
				if (p1.equals(p2))
				{
					continue;
				}
				final Points points1 = attractorsWithPoints.get(p1);
				final Points points2 = attractorsWithPoints.get(p2);
				if (pathExists(p1, points1, p2, points2))
				{
					final Points union = new Points();
					final Point unionPoint = p1;
					union.addAll(points1);
					union.addAll(points2);
					attractorsWithPoints.remove(p1);
					attractorsWithPoints.remove(p2);
					attractorsWithPoints.put(unionPoint, union);
					return true;
				}
			}
		}
		return false;
	}

	/** Find density attractors. */
	private void detDensAttractors()
	{
		for (final HyperCube cube : hyperSpace.map.values())
		{
			for (final Point point : cube.points)
			{
				point.density = calculateDensity(point);
				final Point densityPoint = getDensityAttractor(point);
				if (!attractorsWithPoints.containsValue(densityPoint))
				{
					final Points points = new Points();
					points.add(point);
					attractorsWithPoints.put(densityPoint, points);
				}
			}
		}
	}

	/** Calculate the influence: I(x,y) = exp { - [distance(x,y)^2] / [2*(sigma^2)] } */
	private double calcInfluence(Point point1, Point point2)
	{
		final double distance = point1.distanceTo(point2);

		if (distance == 0)
		{
			return 0; // Influence is zero if entities are the same
		}

		final double exponent = -(distance * distance) / (2.0 * (SIGMA * SIGMA));
		final double influence = Math.exp(exponent);

		return influence;
	}

	/** Calculate the density in a point (sum of the influence of each another entity of data set). */
	private double calculateDensity(Point point)
	{
		double density = 0.0;
		for (final HyperCube cube : hyperSpace.map.values())
		{
			for (final Point p : cube.points)
			{
				density += calcInfluence(point, p);
			}
		}
		return density;
	}

	/** Calculate gradient of density functions */
	private double[] calculateGradient(Point point)
	{
		final double[] gradient = new double[point.params.length];

		// Iterate over all entities and calculate the factors of gradient
		for (final HyperCube cube : hyperSpace.map.values())
		{
			for (final Point otherPoint : cube.points)
			{
				final double influence = calcInfluence(point, otherPoint);

				// Calculate the gradient function for each dimension of data
				for (int i = 0; i < point.params.length; i++)
				{
					final double diff = otherPoint.params[i] - point.params[i];
					gradient[i] += diff * influence;
				}
			}
		}

		return gradient;
	}

	/** Find density-attractor for an entity (a hill climbing algorithm). */
	private Point getDensityAttractor(Point point)
	{
		int maxIter = 5;
		final double delta = 1;
		Point currentAttractor = new Point(point.params, point.clusterId);
		Point resultAttractor = null;
		Boolean isTop = false;

		do
		{
			// avoid loops
			if (--maxIter <= 0)
			{
				break;
			}

			final Point prevAttractor = currentAttractor;

			final double[] currentGradient = calculateGradient(prevAttractor);

			//Create point to represent gradient of current attractor
			final Point gradPoint = new Point(currentGradient, 0);

			//Get next possible attractor
			final double gradientEntityNorm = gradPoint.getEuclideanNorm();

			currentAttractor = prevAttractor;
			for (int i = 0; i < currentGradient.length; i++)
			{
				currentAttractor.params[i] += delta / gradientEntityNorm * gradPoint.params[i];
			}

			// Calculate density
			currentAttractor.density = calculateDensity(currentAttractor);

			// Break if the local maximum found
			isTop = currentAttractor.density < prevAttractor.density;
			if (isTop)
			{
				resultAttractor = prevAttractor;
			}
		}
		while (!isTop);

		if (maxIter <= 0)
		{
			resultAttractor = currentAttractor;
		}

		return resultAttractor;
	}

	/** Find path between two attractors. */
	private Boolean pathExists(Point point1, Points points1, Point point2, Points points2)
	{
		if (point1.distanceTo(point2) <= SIGMA)
		{
			return true;
		}
		
		for (final Point dependentPoint1 : points1)
		{
			for (final Point dependentPoint2 : points2)
			{
				if (dependentPoint1.distanceTo(dependentPoint2) <= SIGMA)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "DENCLUE";
	}
}
