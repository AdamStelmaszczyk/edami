package structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HyperCube
{
	public int dimension;
	public String hyperCubeKey; // String representation of the cube upper bounds
	public double[] upperBounds;
	public Set<Point> points = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
	public ArrayList<String> neighbors = new ArrayList<String>();
	private final double[] entitiesSum;

	public HyperCube(String hypercube_key, double[] upper_bounds)
	{
		hyperCubeKey = hypercube_key;
		upperBounds = upper_bounds;
		entitiesSum = new double[upper_bounds.length];
		dimension = upper_bounds.length;
	}

	public void addPoint(Point point)
	{
		points.add(point);

		for (int i = 0; i < point.params.length; i++)
		{
			entitiesSum[i] += point.params[i];
		}
	}

	public double[] mean()
	{
		final double[] mean = new double[entitiesSum.length];
		final int nr_of_points = points.size();

		for (int i = 0; i < entitiesSum.length; i++)
		{
			mean[i] = entitiesSum[i] / nr_of_points;
		}

		return mean;
	}

	public double distanceTo(HyperCube cube)
	{
		final int dimension = cube.dimension;

		double sum = 0.0;
		final double[] mean1 = mean(), mean2 = cube.mean();

		for (int i = 0; i < dimension; i++)
		{
			final double diff = mean1[i] - mean2[i];
			sum += diff * diff;
		}

		return Math.sqrt(sum);
	}
}
