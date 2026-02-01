public class Segment {
    public Point start, end;
    public boolean fromYoungerTwin;
    
    public Segment(Point start, Point end, boolean fromYoungerTwin) {
        this.start = start;
        this.end = end;
        this.fromYoungerTwin = fromYoungerTwin;
    }
    
    public double getLength() {
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}