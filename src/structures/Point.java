package structures;
import java.util.ArrayList;
import java.util.Collection;

public class Point
{
	private Collection<Double> parameters = new ArrayList<Double>();

	public void add(double parameter)
	{
		parameters.add(parameter);
	}

	@Override
	public String toString()
	{
		return parameters.toString();
	}
}
