package hr.fer.zemris.optjava.dz6;

import java.util.List;

public class City {
	
	private int id;
	private Point point;
	public List<City> nearest;
	
	public City(int id, double x, double y) {
		this.id = id;
		this.point = new Point(x,y);
	}

	public int getId() {
		return id;
	}
	
	public Point getPoint() {
		return point;
	}
	
	@Override
	public String toString() {
		return (id+1) + "";
	}
}
