package structures;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HyperCube
{
	public final String hyperCubeKey; // String representation of the cube upper bounds
	public final double[] upperBounds;
	public final Set<Point> points = new HashSet<Point>();
	public final ArrayList<String> neighbors = new ArrayList<String>();

	private final double[] entitiesSum;

	public HyperCube(String hyperCubeKey, double[] upperBounds)
	{
		this.hyperCubeKey = hyperCubeKey;
		this.upperBounds = upperBounds;
		entitiesSum = new double[upperBounds.length];
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
		final int dimension = cube.upperBounds.length;
		double sum = 0.0;
		final double[] mean1 = mean(), mean2 = cube.mean();
		for (int i = 0; i < dimension; i++)
		{
			final double diff = mean1[i] - mean2[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}

	public static String getCubeKey(double[] cubeBounds)
	{
		final StringBuilder key = new StringBuilder();
		final DecimalFormat format = new DecimalFormat("0.#");
		for (final double bound : cubeBounds)
		{
			key.append(format.format(bound));
			key.append(",");
		}
		return key.toString();
	}
}
