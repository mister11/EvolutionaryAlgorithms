package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;

public class Solution {

	public List<City> tour;
	public double tourLength;
	
	public Solution() {
		tour = new ArrayList<>();
		this.tourLength = 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Route: ");
		for(City c : tour) {
			sb.append(c + "->");
		}
		sb.append("\nLength: " + tourLength + "\n");
		return sb.toString();
	}
}
