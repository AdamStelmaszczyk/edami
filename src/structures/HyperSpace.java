package structures;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HyperSpace
{
	public final Map<String, HyperCube> map = new HashMap<String, HyperCube>();

	private final double sigma;
	private final int minPnt;
	private final Set<HyperCube> populatedCubes = Collections
			.newSetFromMap(new ConcurrentHashMap<HyperCube, Boolean>());
	private final Set<HyperCube> hightlyPopulatedCubes = Collections
			.newSetFromMap(new ConcurrentHashMap<HyperCube, Boolean>());

	public HyperSpace(double sigma, int minPnt)
	{
		this.sigma = sigma;
		this.minPnt = minPnt;
	}

	public void addPoint(Point point)
	{
		// create cube key for the point
		final double[] cube_bounds = new double[point.params.length];

		for (int j = 0; j < point.params.length; j++)
		{
			cube_bounds[j] = point.params[j] - point.params[j] % (2 * sigma) + 2 * sigma;
		}
		final String key = getCubeKey(cube_bounds);

		// find cube with such key
		HyperCube cube = null;

		for (final HyperCube cube_iter : populatedCubes)
		{
			if (cube_iter.hyperCubeKey.equals(key))
			{
				cube = cube_iter;
				populatedCubes.remove(cube_iter);
				break;
			}
		}

		if (cube == null)
		{
			cube = new HyperCube(key, cube_bounds);
			cube.addPoint(point);
			populatedCubes.add(cube);
		}
		else
		{
			cube.addPoint(point);
			populatedCubes.add(cube);
		}
	}

	private String getCubeKey(double[] cube_bounds)
	{
		StringBuilder key = new StringBuilder();
		final DecimalFormat format = new DecimalFormat("0.#");
		for (final double cube_bound : cube_bounds)
		{
			key.append(format.format(cube_bound));
			key.append(",");
		}
		return key.toString();
	}

	public void connectMap()
	{
		for (final HyperCube cube : populatedCubes)
		{
			if (cube.points.size() >= minPnt)
			{
				getCubeConnections(cube);
				hightlyPopulatedCubes.add(cube);
			}
		}
	}

	private void getCubeConnections(HyperCube cube)
	{
		if (!map.containsKey(cube.hyperCubeKey))
		{
			map.put(cube.hyperCubeKey, cube);
		}

		for (final HyperCube populatedCube : populatedCubes)
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
