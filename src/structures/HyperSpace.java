package structures;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HyperSpace {

	private double sigma;
	int dimensions; 
	int min_pnt;

	public final Set<HyperCube> populatedCubes = Collections.newSetFromMap(new ConcurrentHashMap<HyperCube, Boolean>());
	public final Set<HyperCube> hightlyPopulatedCubes = Collections.newSetFromMap(new ConcurrentHashMap<HyperCube, Boolean>());
	public final Map<String, HyperCube> map = new HashMap<String, HyperCube>();

	public HyperSpace(double sigma, int min_pnt) {
		this.sigma = sigma;
		this.min_pnt =min_pnt;

	}

	public void addPoint(Point point) {

		//create cube key for the point
		double[] cube_bounds = new double[point.params.length];

		for (int j = 0; j < point.params.length; j++) {
			cube_bounds[j] = (point.params[j] - (point.params[j] % (2*sigma)) + (2*sigma));
		}
		String key = getCubeKey(cube_bounds);

		System.out.println(key);

		//find cube with such key
		HyperCube cube = null;

		for (HyperCube cube_iter : populatedCubes) {
			if (cube_iter.hypercube_key.equals(key)) {
				cube = cube_iter;
				populatedCubes.remove(cube_iter);
				break;
			}
		}

		if (cube == null) {
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

	private String getCubeKey(double[] cube_bounds) {
		String key = new String();
		DecimalFormat format = new DecimalFormat("0.#");

		for (int i = 0; i < cube_bounds.length; i++) {
			key += format.format(cube_bounds[i]) + ",";
		}
		return key;
	}

	public void print() {
		System.out.println("\n\n\n\n\n\nPopulated Cubes "+ populatedCubes.size() +"\n\n" );
		//for (HyperCube cube : populatedCubes) {
			//System.out.println("key: " + cube.hypercube_key);
			//System.out.println("mean: " + cube.mean()[0]+ " " + cube.mean()[1]);
			//for (Point point : cube.points) {
				//System.out.println("point: " + point.toString());
			//}
		//}
		System.out.println("\nHightly Populated Cubes "+ hightlyPopulatedCubes.size() +"\n");

		for (HyperCube cube : hightlyPopulatedCubes) {
			System.out.println("key: " + cube.hypercube_key);
			System.out.println("mean: " + cube.mean()[0]+ " " + cube.mean()[1]);
			System.out.println("ref: " + cube.neighbors.size());
			System.out.println("points.size(): " + cube.points.size() +"\n\n");
		}
		
		System.out.println("\n\n Popolated cubes + neighbors: " + map.size() +"\n\n");

	}

	public void connectMap() {
		for (HyperCube cube : populatedCubes) {
			if (cube.points.size() >= min_pnt)
			{
				getCubeConnections(cube);
				hightlyPopulatedCubes.add(cube);
			}
		}	
	}

	private void getCubeConnections(HyperCube cube) {
		if (!map.containsKey(cube.hypercube_key))
			map.put(cube.hypercube_key, cube);

		for (HyperCube populatedCube : populatedCubes) {
			if (cube.distanceTo(populatedCube) < 4*sigma)
			{
				cube.neighbors.add(populatedCube.hypercube_key);
				if (!map.containsKey(populatedCube.hypercube_key))
					map.put(populatedCube.hypercube_key, populatedCube);
			}
		}
	}
}
