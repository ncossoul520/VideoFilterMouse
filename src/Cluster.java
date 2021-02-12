import java.util.ArrayList;

public class Cluster {
    private Point center;
    private ArrayList<Point> pointList;

    public Cluster() {
        // Create empty list and randomly init center
        center = new Point();
        pointList = new ArrayList<>();
    }

    public Cluster(Point p) {
        center = p;
        pointList = new ArrayList<>();
    }


    public void add(Point p) {
        pointList.add(p);
    }


    public Boolean calculateCenter() {
        if (size() == 0) { return false; }

        boolean stable;
        int avgX = 0, avgY = 0;
        for (Point point : pointList) {
            avgX += point.getX();
            avgY += point.getY();
        }
        avgX /= size();
        avgY /= size();

        stable = avgX == getCenter().getX() && avgY == getCenter().getY();

        center.setX( (short)avgX );
        center.setY( (short)avgY );

        return stable;
    }

    public void clearPoints() {
        pointList.clear();
    }


    public Point getCenter() {
        return center;
    }

    public int size() {
        return pointList.size();
    }

    public ArrayList<Point> getPointList() {
        return pointList;
    }
}