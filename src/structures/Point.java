package structures;

import java.util.Arrays;

public class Point
{
	public final double[] params;
	public final int clusterId;
	public double density;

	public Point(double[] params, int clusterId)
	{
		this.params = new double[params.length];
		System.arraycopy(params, 0, this.params, 0, params.length);
		this.clusterId = clusterId;
	}

	public double distanceTo(Point point)
	{
		final int dimension = params.length;
		if (dimension != point.params.length)
		{
			throw new IllegalArgumentException("Given point is in different dimension");
		}
		double sum = 0.0;
		for (int i = 0; i < dimension; i++)
		{
			final double diff = params[i] - point.params[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}
	
	public double getEuclideanNorm() {
	    double sum = 0;

	    for(int i=0 ; i < this.params.length ; i++){
	    	sum += this.params[i] *  this.params[i];
	    }
	    
	    double norm = Math.sqrt(sum); 
	    return norm;
	}

	@Override
	public String toString()
	{
		return (Arrays.toString(params) + " dest:" + density);
	}

}
