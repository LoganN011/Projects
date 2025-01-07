package smart.rails;

/**
 * Point that represents a point a component could be at
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Point {
    private double x;
    private double y;

    /**
     * Constructor for a point
     * @param x x coordinate that the point is as a double
     * @param y y coordinate that the point is as a double
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get the x coordinate
     * @return the x coordinate as a double
     */
    public double getX() {
        return x;
    }

    /**
     * get the y coordinate
     * @return the y coordinate as a double
     */
    public double getY() {
        return y;
    }

    /**
     * To String of a point
     * @return A string representation of a point
     */
    public String toString() {
        return x + ", " + y;
    }

    /**
     * Check to see if two points are "equals"
     * The points can be off by 0.01
     *
     * @param p other point that is being compared to
     * @return if the two points are "equals"
     */
    public boolean equals(Point p) {
        return Math.abs(x - p.x) <= .01 && Math.abs(y - p.y) <= .01;

    }
}
