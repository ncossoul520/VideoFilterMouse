public class Point {
    private short x, y, val;

    public Point() {
        this.x   = (short)(Math.random()*256);
        this.y   = (short)(Math.random()*256);
        this.val = 255;
    }

    public Point(short x, short y, short val) {
        this.x   = x;
        this.y   = y;
        this.val = val;
    }


    public double distanceFrom(short xt, short yt) {
        return Math.sqrt( (x - xt)*(x - xt) + (y - yt)*(y - yt) );
    }

    public double distanceFrom(Point p2) {
        return Math.sqrt( (x - p2.getX()) * (x - p2.getX()) +
                          (y - p2.getY()) * (y - p2.getY()) );
    }


    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public short getVal() {
        return val;
    }

    public void setVal(short val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")= " + val;
    }
}
