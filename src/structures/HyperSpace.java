package structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HyperSpace
{
	public final Map<String, HyperCube> map = new HashMap<String, HyperCube>();

	private final double sigma;
	private final int minPts;
	private final Map<String, HyperCube> populatedCubes = new HashMap<String, HyperCube>();
	private final Set<HyperCube> highlyPopulatedCubes = new HashSet<HyperCube>();

	public HyperSpace(double sigma, int minPnt)
	{
		this.sigma = sigma;
		this.minPts = minPnt;
	}

	public void addPoint(Point point)
	{
		// create cube key for the point
		final double[] cubeBounds = new double[point.params.length];
		for (int j = 0; j < point.params.length; j++)
		{
			cubeBounds[j] = point.params[j] - point.params[j] % (2 * sigma) + 2 * sigma;
		}
		final String key = HyperCube.getCubeKey(cubeBounds);

		// find cube with such key
		HyperCube cube = populatedCubes.get(key);
		if (cube == null)
		{
			cube = new HyperCube(key, cubeBounds);
		}
		cube.addPoint(point);
		populatedCubes.put(key, cube);
	}

	public void connectMap()
	{
		for (final HyperCube cube : populatedCubes.values())
		{
			if (cube.points.size() >= minPts)
			{
				getCubeConnections(cube);
				highlyPopulatedCubes.add(cube);
			}
		}
	}

	private void getCubeConnections(HyperCube cube)
	{
		if (!map.containsKey(cube.hyperCubeKey))
		{
			map.put(cube.hyperCubeKey, cube);
		}

		for (final HyperCube populatedCube : populatedCubes.values())
		{
			if (cube.distanceTo(populatedCube) < 4 * sigma)
			{
				cube.neighbors.add(populatedCube.hyperCubeKey);
				if (!map.containsKey(populatedCube.hyperCubeKey))
				{
					map.put(populatedCube.hyperCubeKey, populatedCube);
				}
			}
		}
	}
}
