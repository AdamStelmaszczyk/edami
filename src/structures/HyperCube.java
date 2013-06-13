package structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HyperCube {

	//Attributes

	public int dimension;

	public  String hypercube_key;   // String representation of the cube upper bounds
	
	public double[] upper_bounds;

	public  Set<Point> points = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());  // points in hypercube

	public  ArrayList<String> neighbors = new ArrayList<String>(); 

	private  double[] entities_sum;  // Sum of points
	
	//Methods
	
	public HyperCube(String hypercube_key, double[] upper_bounds) {
		this.hypercube_key = hypercube_key;
		this.upper_bounds = upper_bounds;
		this.entities_sum = new double[upper_bounds.length];
		this.dimension = upper_bounds.length;
	}

	public void addPoint(Point point) {
		points.add(point);
		
		for (int i = 0; i < point.params.length; i++) {
			entities_sum[i] += point.params[i];
		}
	}
	
	public double[] mean()
	{
		double[] mean = new double[entities_sum.length];
		int nr_of_points = points.size();
		
		for (int i = 0; i < entities_sum.length; i++) {
			mean[i] = entities_sum[i]/nr_of_points;
		}
		
		return mean;
	}

	public double distanceTo(HyperCube cube) {
		final int dimension = cube.dimension;

		double sum = 0.0;
		double[] mean1 = mean(), mean2 = cube.mean();
		
		for (int i = 0; i < dimension; i++)
		{
			final double diff = mean1[i] - mean2[i];
			sum += diff * diff;
		}
		
		return Math.sqrt(sum);
	}

}
