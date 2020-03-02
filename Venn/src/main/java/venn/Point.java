package venn;

public class Point {
	double xValue;
	double yValue;

	public Point(double x, double y) {this.xValue=x;this.yValue=y;}

	public String toString() {
		return (this.xValue+", "+this.yValue);
	}
}
