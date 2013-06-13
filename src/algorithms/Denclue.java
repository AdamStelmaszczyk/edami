package algorithms;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import structures.Clusters;
import structures.HyperCube;
import structures.HyperSpace;
import structures.Point;
import structures.Points;

public class Denclue extends ClusteringAlgorithm
{
	private final double SIGMA = 0.7;
	private final int MIN_PNT = 5;

	private final Set<Point> unvisited = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
	private final HyperSpace hyperspace = new HyperSpace(SIGMA, MIN_PNT);
	private Map<Integer, Point>  attractors = new HashMap<Integer, Point>();

	private Map<Point, Points> attractorsWithPoints = new HashMap<Point, Points>();

	public Denclue(Points input)
	{
		super(input);
	}

	@Override
	public Clusters getClusters()
	{
		System.out.println("\n\t\tDENCLUE Algorithm");
		Clusters clusters = new Clusters();
		unvisited.addAll(input);
		
		System.out.println("Get populated cubes");
		detPopulatedCubes();
		
		System.out.println("Get hightly populated cubes and create their connection map");
		hyperspace.connectMap();
		
		System.out.println("Find denstity attractors");
		detDensAttractors();
		
		System.out.println("Find pathes between attractors");
		for (int i = 0; i < attractors.size(); i++) {
			for (int j = i+1; j < attractors.size(); j++) {
				if (pathBetweenExists(attractors.get(i), i, attractors.get(j)))
					break;
			}
		}
		
		//data
		System.out.println("\n\tDATA: ");
		hyperspace.print();

		System.out.println("\n\n\nAttractors "+ attractorsWithPoints.size()+"\n\n\n");

		for (Point point : attractorsWithPoints.keySet()) {
			System.out.println(point.toString());
			System.out.println(attractorsWithPoints.get(point).size());
			
			clusters.add(attractorsWithPoints.get(point));
		}

		return clusters;
	}


	private void detPopulatedCubes() {
		for (final Point point : unvisited)
		{
			hyperspace.addPoint(point);
			unvisited.remove(point);
		}
	}

	private Clusters detDensAttractors() {
		System.out.println("\nfind density attractors");
		int i = 0;
		for (HyperCube cube : hyperspace.map.values()) {
			for (Point point : cube.points) {
				point.density = calculateDensity(point);
				Point densityPoint = getDensityAttractor(point);

				if (!attractors.containsValue(densityPoint)) {
					attractors.put(i, densityPoint);
					i++;

					Points points = new Points();
					points.add(point);
					attractorsWithPoints.put(densityPoint, points);
				}
			}
		}
		return null;
	}

	//Calculate the influence of an entity in another. 
	//The chosen influence function was the Gaussian Influence Function, defined by:
	// I(x,y) = exp { - [distance(x,y)**2] / [2*(sigma**2)] }
	double calculateInfluence(Point point1, Point point2){
		double distance = point1.distanceTo(point2);

		if( distance == 0 ){
			return 0;  // Influence is zero if entities are the same
		}

		double exponent = - (distance*distance) / (2.0 *(SIGMA *SIGMA));
		double influence = Math.exp(exponent);

		return influence;

	}


	// Calculate the density in an entity. It's defined as the sum of the
	// influence of each another entity of dataset.
	double calculateDensity(Point _point){

		double density = 0;

		for (HyperCube cube : hyperspace.map.values()) {
			for (Point point : cube.points) {
				density += calculateInfluence(_point, point);
			}
		}


		return density;
	}


	//Calculate gradient of density functions in a given spatial point.
	double[] calculateGradient(Point point){
		double[] gradient = new double[point.params.length];


		// Iterate over all entities and calculate the factors of gradient
		for (HyperCube cube : hyperspace.map.values()) {
			for (Point other_point : cube.points) {

				double curr_influence = calculateInfluence(point, other_point);

				// Calculate the gradient function for each dimension of data
				for(int i=0 ; i < point.params.length ; i++){

					double curr_difference = (other_point.params[i] - point.params[i]);
					gradient[i] += curr_difference * curr_influence;

				}
			}
		}

		return gradient;
	}


	// Find density-attractor for an entity (a hill climbing algorithm)
	Point getDensityAttractor(Point point){
		double delta = 1;

		Point curr_attractor = point;
		Point found_attractor = null;

		int MAX_ITERATIONS = 1000;
		Boolean reachedTop = false;

		do{

			// Avoid infinite loops
			if( --MAX_ITERATIONS <= 0 )  break;


			// Store last calculated values for further comparison
			Point last_attractor = curr_attractor;


			// Calculate the gradient of density function at current candidate to attractor
			double[] curr_gradient = calculateGradient(last_attractor);


			// Build an entity to represent the gradient
			Point grad_point = new Point(curr_gradient, 0);

			// Calculate next candidate to attractor
			double grad_entity_norm = grad_point.getEuclideanNorm();


			//if( grad_entity_norm > 0 )
			//System.out.println("\n\n\n>>>>>\n\n\n");

			curr_attractor = last_attractor;
			for (int i = 0; i < curr_gradient.length; i++) {
				curr_attractor.params[i] +=  ( (double)(delta/grad_entity_norm)) * grad_point.params[i];
			}


			// Calculate density in current attractor
			double curr_density = calculateDensity(curr_attractor);

			curr_attractor.density = curr_density;

			// Verify whether local maxima was found
			reachedTop = ( curr_attractor.density < last_attractor.density );
			if( reachedTop ) 
				found_attractor = last_attractor;


		}while( !reachedTop );

		if( MAX_ITERATIONS <= 0 )  
			found_attractor = curr_attractor;


		return round(found_attractor);

	}

	//Find path between two attractors
	Boolean pathBetweenExists(Point point1, int key,Point point2 ){

		Map<Point, Boolean> usedEntities = new HashMap<Point, Boolean>();
		usedEntities.put(point1, true);
		usedEntities.put(point2, true);


		// If the distance between points <= sigma, a path exist
		if(point1.distanceTo(point2) <= SIGMA){
			
			System.out.println("Path found");
			System.out.println(attractors.size());
			attractors.remove(key);
			System.out.println(attractors.size());
			
			Points points = new Points();
			points.addAll(attractorsWithPoints.get(point1));
			attractorsWithPoints.get(point2).addAll(points);

			return true;
		}
		
		//to do
		
		return false;
	}

	private Point round(Point point) {
		DecimalFormat format = new DecimalFormat("#.###");

		for (int i = 0; i < point.params.length; i++) {
			point.params[i] = Double.valueOf(format.format(point.params[i]));
		}

		return point;
	}

	@Override

	public String toString()
	{
		return "DENCLUE";
	}
}
