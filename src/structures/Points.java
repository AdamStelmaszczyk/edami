package structures;
import java.util.ArrayList;
import java.util.Collection;

public class Points
{
	private final Collection<Point> points = new ArrayList<Point>();

	public void add(Point point)
	{
		points.add(point);
	}

	@Override
	public String toString()
	{
		return points.toString();
	}
}
