package hr.fer.zemris.optjava.dz6;

public class CityDistance implements Comparable<CityDistance> {
	
	private City first;
	private City second;
	private double distance;

	public CityDistance(City first, City second, double distance) {
		this.first = first;
		this.second = second;
		this.distance = distance;
	}

	@Override
	public int compareTo(CityDistance o) {
		if(this.distance < o.distance) {
			return -1;
		}
		if(this.distance > o.distance) {
			return 1;
		}
		return 0;
	}
	
	public City getFirst() {
		return first;
	}
	
	public City getSecond() {
		return second;
	}
	
	public double getDistance() {
		return distance;
	}
}
