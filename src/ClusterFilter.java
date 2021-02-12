import processing.core.PApplet;
import java.util.ArrayList;

public class ClusterFilter implements PixelFilter {
    private static final int CLUSTERS_MAX_NUM = 100;
    private int k = -1;
    private ArrayList<Cluster> clusterList;
    private ArrayList<PixelFilter> filters = new ArrayList<>();

    // When k not given: calculate number of clusters
    public ClusterFilter() {
    }

    public ClusterFilter(int k) {
        if (k >= 1 && k <= 50) {
            this.k = k;
        }
    }


    @Override
    public DImage processImage(DImage img) {
        short[][] grid  = img.getBWPixelGrid();
        ArrayList<Point> allPoints = makePointList(grid);

        if (k == -1) {
            k = calculateNumClusters( allPoints );
        }

        clusterList = createClusters(k, allPoints);

        short[][] red   = new short[img.getHeight()][img.getWidth()];
        short[][] green = new short[img.getHeight()][img.getWidth()];
        short[][] blue  = new short[img.getHeight()][img.getWidth()];
        reAssignImageColors( red, green, blue, clusterList );
        img.setColorChannels( red, green, blue );
        return img;
    }


    private ArrayList<Cluster> createClusters(int k, ArrayList<Point> allPoints) {
        ArrayList<Cluster> clusters = initClusters(k, allPoints);
        Boolean stable = false;
        do {
            clearAllClusters( clusters );
            assignPointsToClusters( allPoints, clusters );
            stable = reCalculateCenters( clusters );
        } while ( !stable );
        return clusters;
    }

    private void displayInfo(DImage img) {
        int sum = 0;
        for (Cluster cluster : clusterList) {
            sum += cluster.size();
            System.out.println( "DEBUG: " + "Center:" + cluster.getCenter() + " #Points: " + cluster.size());
        }
        System.out.println("DEBUG: #points clusters=" + sum + " vs #points img=" + img.getHeight() * img.getWidth());
    }

    private ArrayList<Cluster> initClusters(int k, ArrayList<Point> points) {
        ArrayList<Cluster> out = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            Point p = points.get( (int)( Math.random() * points.size() ) );
            out.add( new Cluster( p ) );
        }
        return out;
    }

    private ArrayList<Point> makePointList(short[][] grid) {
        ArrayList<Point> out = new ArrayList<>();
        for (short r = 0; r < grid.length; r++) {
            for (short c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 255) {
                    out.add(new Point(c, r, grid[r][c]));   // x=c, y=r, val
                }
            }
        }
        return out;
    }

    private void clearAllClusters(ArrayList<Cluster> clusterList) {
        for (Cluster cluster : clusterList) {
            cluster.clearPoints();
        }
    }

    private void assignPointsToClusters(ArrayList<Point> allPoints, ArrayList<Cluster> clusterList) {
        for (Point point : allPoints) {
            Cluster closest = findClosest(point, clusterList);
            closest.add( point );
        }
    }

    private Cluster findClosest(Point point, ArrayList<Cluster> clusterList) {
        Cluster out = new Cluster();
        double dist, dist_min = Integer.MAX_VALUE;
        for (Cluster cluster : clusterList) {
            dist = point.distanceFrom( cluster.getCenter() );
            if (dist < dist_min) {
                out = cluster;
                dist_min = dist;
            }
        }
        return out;
    }

    private Boolean reCalculateCenters(ArrayList<Cluster> clusterList) {
        Boolean stable = true;
        for (Cluster cluster : clusterList) {
            stable &= cluster.calculateCenter();
        }
        return stable;
    }

    private void reAssignImageColors(short[][] red, short[][] green, short[][] blue, ArrayList<Cluster> clusterList) {
        for (Cluster cluster : clusterList) {
            short r = (short)(Math.random()*256);
            short g = (short)(Math.random()*256);
            short b = (short)(Math.random()*256);
            for (Point point : cluster.getPointList()) {
                red[point.getY()][point.getX()] = r;
                green[point.getY()][point.getX()] = g;
                blue[point.getY()][point.getX()] = b;
            }
        }
    }

    private int calculateNumClusters(ArrayList<Point> allPoints) {
        ArrayList<Cluster> clusters;
        k_loop : for (int k = 1; k < CLUSTERS_MAX_NUM; k++) {
            clusters = createClusters(k, allPoints);
            double density = 0;
            for (Cluster cluster : clusters) {
                density = calculateClusterDensity( cluster );
                if (density < 0.007) { continue k_loop; }
            }
            return k; // all clusters for this k value have a good density
        }
        return -1;
    }

    private double calculateClusterDensity(Cluster cluster) {
        double radius = clusterRadius( cluster );
        return cluster.size() / (Math.PI * radius * radius);
    }

    private double clusterRadius(Cluster cluster) {
        double dist, radius = 0;
        for (Point point : cluster.getPointList()) {
            dist = point.distanceFrom( cluster.getCenter() );
            if (dist > radius) {
                radius = dist;
            }
        }
        return radius;
    }


    @Override public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        for (Cluster cluster : clusterList) {
            window.fill(255, 0, 0);
            window.ellipse( cluster.getCenter().getX(), cluster.getCenter().getY(), 10, 10);
        }
    }
}