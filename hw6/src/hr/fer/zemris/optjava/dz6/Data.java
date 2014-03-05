package hr.fer.zemris.optjava.dz6;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private List<City> cities;
	
	public Data(String filename) {
		this.cities = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(filename))));
			boolean end = false;
			while(!end) {
				String line = br.readLine();
				line = line.trim();
				if(line.startsWith("DISPLAY_DATA_SECTION") ||
						line.startsWith("NODE_COORD_SECTION")) {
					while(true) {
						String data = br.readLine();
						data = data.trim();
						if(data.equals("EOF")) {
							end = true;
							break;
						}
						String[] parts = data.split("\\s+");
						cities.add(new City(Integer.parseInt(parts[0]) - 1, 
								Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
					}
				} else {
					continue;
				}
			}
		} catch (IOException e) {
			System.err.println("Error opening file " + filename);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("Error closing reader!");
				}
			}
		}
	}

	public List<City> getCities() {
		return cities;
	}
}
