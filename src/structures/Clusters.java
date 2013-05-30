package structures;
import java.util.ArrayList;
import java.util.Collection;

public class Clusters
{
	private final Collection<Points> clusters = new ArrayList<Points>();
	
	public void add(Points cluster)
	{
		clusters.add(cluster);
	}

	@Override
	public String toString()
	{
		return clusters.toString();
	}
}
