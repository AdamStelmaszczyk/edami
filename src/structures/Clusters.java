package structures;

import java.util.ArrayList;
import java.util.Collection;

public class Clusters extends ArrayList<Points>
{
	private static final long serialVersionUID = 1L;

	public Clusters()
	{
	}

	public Clusters(Collection<Points> values)
	{
		super(values);
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(size());
		sb.append(" cluster(s) of size: ");
		for (final Points cluster : this)
		{
			sb.append(cluster.size());
			sb.append(" ");
		}
		return sb.toString();
	}
}
