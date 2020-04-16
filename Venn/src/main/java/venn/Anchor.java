package venn;

import java.util.ArrayList;

//Anchor points in each circle
public class Anchor {
	ArrayList<Point> points = new ArrayList<Point>();

	public void addPoint(Point p) {this.points.add(p);}

	public void removePoint(Point p) {if(this.points.contains(p)) {this.points.remove(p);}}

	public Point closest(double h) {
		double closest = Double.MAX_VALUE;
		Point found = null;
		for (int i=0;i<points.size();i++) {
			if (Math.abs(h-points.get(i).yValue)<closest) {closest = Math.abs(h-points.get(i).yValue);found=points.get(i);}
		}
		return found;
	}

	public void printAll() {
		for (Point p:points) {System.out.println(p.toString());}
	}

}
