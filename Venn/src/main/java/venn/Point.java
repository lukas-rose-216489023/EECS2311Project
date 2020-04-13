package venn;

public class Point {
	double xValue;
	double yValue;

	public Point(double x, double y) {this.xValue=x;this.yValue=y;}
	
	public double getX() {
		return xValue;
	}

	public double getY() {
		return yValue;
	}

	public String toString() {
		return (this.xValue+", "+this.yValue);
	}
}
